package com.xiaolin.video.common.component;

import com.xiaolin.video.common.dto.req.SliceFileUploadRequestDto;
import com.xiaolin.video.common.dto.resp.FileUploadDto;
import com.xiaolin.video.common.utils.MiniFileUploadStrategy;
import com.xiaolin.video.common.utils.SliceUploadStrategy;
import com.xiaolin.video.exception.ClientException;
import com.xiaolin.video.exception.ServerException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO 将文件存储到云服务器OSS中
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 文件上传：小文件上传，大文件分片上传
 * @create 2023/7/3
 */
@Component
@RequiredArgsConstructor
public class FileUploadComponent implements SliceUploadStrategy, MiniFileUploadStrategy {

    private final RedisTemplate<String, Object> redisTemplate;


    /**
     * 分片上传文件
     *
     * @param requestDto 文件上传请求信息
     * @param fileDir 目标文件夹
     * @return 文件上传信息回复
     */
    @Override
    public FileUploadDto sliceUpload(SliceFileUploadRequestDto requestDto, String fileDir) throws IOException {
        String name = requestDto.getName();
        Integer chunk = requestDto.getChunk();
        MultipartFile file = requestDto.getFile();
        String targetFileName = fileDir + name + "-" + chunk;
        byte[] bytes = file.getBytes();

        // 写文件
        Path path = Path.of(targetFileName);
        Files.write(path, bytes);

        return FileUploadDto.builder()
                .chunk(requestDto.getChunk())
                .chunks(requestDto.getChunks())
                .uploadComplete(true)
                .fileName(name)
                .build();
    }

    /**
     * 合并文件，并进行md5校验
     *
     * @param fileDir 文件夹
     * @param filename    文件名，包含文件拓展符
     * @param chunks      文件总分片数
     * @param originalMd5 源文件的md5值
     * @return 文件合并是否成功
     */
    @Override
    public FileUploadDto sliceFilesMerge(String fileDir, String filename, int chunks, String originalMd5) throws IOException {
        Path destPath = Paths.get(fileDir + filename);

        // 1. 检查文件分块是否已经完整
        List<Integer> notExistChunks = new ArrayList<>();
        for (int i=0; i<chunks; i++) {
            Path srcPath = Paths.get(fileDir + filename + "-" + i);
            if (!srcPath.toFile().exists()) {
                notExistChunks.add(i);
            }
        }
        if (!CollectionUtils.isEmpty(notExistChunks)) {
            String collected = notExistChunks.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            throw new ClientException("文件片段不完整，无法进行合并，缺少: " + collected);
        }

        // 2. 文件合并
        for (int i=0 ;i<chunks; i++) {
            Path srcPath = Paths.get(fileDir + filename + "-" + i);
            Files.write(destPath, Files.readAllBytes(srcPath), StandardOpenOption.APPEND);
            // 删除切片文件
            Files.delete(srcPath);
        }

        // 3. 校验md5
        try (FileInputStream fileInputStream = new FileInputStream(destPath.toFile())) {
            String targetFileMd5 = DigestUtils.md5DigestAsHex(fileInputStream);

            boolean success = Objects.equals(originalMd5, targetFileMd5);
            if (!success) {
                throw new ServerException("目标文件与源文件的MD5不一致");
            }
            return FileUploadDto.builder()
                    .uploadComplete(true)
                    .fileName(filename)
                    .build();
        } catch (FileNotFoundException e) {
            throw new ServerException("文件合并时发生错误");
        }
    }

    /**
     * 秒传，服务器端已经存在用户将要传输的文件，服务器直接返回目标文件
     * TODO 优化Redis存储
     * @param requestDto 文件请求
     * @return 文件上传消息
     */
    @Override
    public FileUploadDto instantUpload(SliceFileUploadRequestDto requestDto) {
        // 判断将要上传的文件加是否已存在，如果存在则直接返回
        return Optional.ofNullable(redisTemplate.opsForValue().get(requestDto.getMd5()))
                .map(value -> FileUploadDto.builder()
                        .uploadComplete(true)
                        .fileName(value.toString())
                        .build())
                .orElse(FileUploadDto.builder()
                        .uploadComplete(false)
                        .build());
    }

    /**
     * 断点续传
     * TODO 使用Redis记录已经上传的文件
     * @param requestDto 传输请求
     * @return 还缺少的文件切片
     */
    @Override
    public Set<Integer> continueUpload(SliceFileUploadRequestDto requestDto, String fileDir) {
        String name = requestDto.getName();
        Integer chunks = requestDto.getChunks();
        Set<Integer> result = new HashSet<>();
        for (int i=0; i<chunks; i++) {
            Path chunkPath = Paths.get(fileDir + name + '-' + i);
            if (!chunkPath.toFile().exists()) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * 小文件上传
     *
     * @param file     文件上传请求
     * @param fileDir 上传的目标文件夹
     * @param fileName 上传的文件名，不包括拓展名
     * @return 文件上传回复消息
     */
    @Override
    public FileUploadDto uploadFile(MultipartFile file, String fileDir, String fileName) throws IOException {
        // 获取原始文件名的后缀名
        String originalFilename = Optional.ofNullable(file.getOriginalFilename()).orElseThrow();

        String targetFilename;
        if (Objects.isNull(fileName)) {
            targetFilename = originalFilename;
        } else {
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            targetFilename = fileName + suffix;
        }

        // 创建目录文件
        File dir = new File(fileDir);
        if (!dir.exists()) {
            boolean res = dir.mkdirs();
            if (!res) {
                throw new ServerException("创建文件夹失败");
            }
        }
        // FIXME 在进行文件转移的时候，需要考虑新建文件的路径，相对路径和绝对路径可能会出现问题
        String targetFilePath;
        if (fileDir.endsWith("/")) {
            targetFilePath = fileDir + targetFilename;
        } else {
            targetFilePath = fileDir + '/' + targetFilename;
        }
        File newFile = new File(targetFilePath);
        file.transferTo(newFile.getAbsoluteFile());
        return FileUploadDto.builder()
                .uploadComplete(true)
                .fileName(targetFilePath)
                .build();
    }

    /**
     * 小文件上传
     *
     * @param file    上传文件
     * @param fileDir 文件目录
     * @return 文件上传回复消息
     */
    @Override
    public FileUploadDto uploadFile(MultipartFile file, String fileDir) throws IOException {
        return uploadFile(file, fileDir, null);
    }


    /**
     * 下载文件 文件存放在HttpServletResponse中
     * @param response HttpServletResponse
     * @param fileDir 文件路径
     * @param name 文件名
     * @throws IOException 写文件时可能的IO异常
     */
    @Override
    public void downloadFile(HttpServletResponse response, String fileDir, String name) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(fileDir + name)) {
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                response.setContentType("image/jpeg");
                int len = 0;
                byte[] bytes = new byte[1024];
                while ((len = fileInputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                    outputStream.flush();
                }
            }
        }
    }
}
