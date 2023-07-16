package com.xiaolin.video.controller;

import com.xiaolin.video.common.component.FileUploadComponent;
import com.xiaolin.video.common.dto.RestResp;
import com.xiaolin.video.common.dto.req.FileUploadRequestDto;
import com.xiaolin.video.common.dto.req.SliceFileUploadRequestDto;
import com.xiaolin.video.common.dto.resp.FileUploadDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 上传图片，下载图片
 * @create 2023/7/2
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/common")
@RequiredArgsConstructor
public class CommonController {

    @Value("${file.image-dir}")
    private String imageDir;

    @Value("${file.video-dir}")
    private String videoDir;

    private final FileUploadComponent fileUploadComponent;
    /**
     * 上传图片或文件 form-data格式的数据
     * 前端传输的参数名必须与后端参数一致
     * @param dto 文件请求信息
     * @return 上传文件状态
     * @throws IOException IO异常
     */
    @PostMapping("/image/upload")
    public RestResp<?> upload(@ModelAttribute FileUploadRequestDto dto) throws IOException {
        FileUploadDto fileUploadDto = fileUploadComponent.uploadFile(dto.getFile(), imageDir, UUID.randomUUID().toString());
        return RestResp.success(fileUploadDto);
    }

    /**
     * 下载图像
     * @param response HttpServletResponse设置返回值类型图像
     * @param name 图像名称
     * @throws IOException 写文件IO异常
     */
    @GetMapping("/image/download")
    public void download(HttpServletResponse response, @RequestParam String name) throws IOException {
        fileUploadComponent.downloadFile(response, imageDir, name);
    }

    /**
     * 分片上传视频
     * @param requestDto 上传视频信息
     * @return 上传消息
     * @throws IOException 文件处理IO异常
     */
    @PostMapping("/video/upload")
    public RestResp<FileUploadDto> uploadVideo(@ModelAttribute SliceFileUploadRequestDto requestDto) throws IOException {
        log.info("upload video:" + requestDto.getFile().getOriginalFilename());
        FileUploadDto fileUploadDto = fileUploadComponent.sliceUpload(requestDto, videoDir);
        return RestResp.success(fileUploadDto);
    }


    @PostMapping("/video/merge")
    public RestResp<?> mergeVideo(@ModelAttribute SliceFileUploadRequestDto requestDto) throws IOException {
        log.info("merge file: " + requestDto.getName());
        FileUploadDto sliceFilesMerge = fileUploadComponent.sliceFilesMerge(videoDir,
                requestDto.getName(), requestDto.getChunks(), requestDto.getMd5());
        return RestResp.success(sliceFilesMerge);
    }

    @GetMapping("/video/continue")
    public RestResp<?> continueUploadVideo(@ModelAttribute SliceFileUploadRequestDto requestDto) {
        Set<Integer> missingChunks = fileUploadComponent.continueUpload(requestDto, videoDir);
        return RestResp.success(missingChunks);
    }

}
