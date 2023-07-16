package com.xiaolin.video.common.component;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.xiaolin.video.config.MyOSSClient;
import com.xiaolin.video.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description
 * @create 2023/7/15
 */

@Component
@RequiredArgsConstructor
public class AliyunOSSComponent {
    private final MyOSSClient myOSSClient;

    /**
     * 文件小于5G
     * 上传单个文件
     * 默认上传文件的路径对应oss中的文件路径
     * @param bucketName oss中数据桶
     * @param inputFilePath 将要上传的文件路径
     * @return 文件上传成功的ETag
     */
    public String uploadFile2Oss(String bucketName, String inputFilePath) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, inputFilePath, new File(inputFilePath));
            PutObjectResult putObjectResult = myOSSClient.putObject(putObjectRequest);
            return putObjectResult.getETag();
        } catch (OSSException | com.xiaolin.video.exception.ClientException oe) {
            throw new ServerException("上传文件失败，阿里云OSS错误");
        } catch (Exception e) {
            throw new ServerException("上传文件失败，服务器错误");
        }
    }

    /**
     * 上传文件夹
     * @param bucketName oss数据桶
     * @param inputDir 上传的文件夹
     * @param recursion 是否递归上传
     * @return 上传文件的所有信息 key: 文件路径 value:文件Etag
     */
    public Map<String, String> uploadDirectory2Oss(String bucketName, String inputDir, boolean recursion) {
        File dir = new File(inputDir);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new com.xiaolin.video.exception.ClientException("文件夹不存在");
        }
        try {
            File[] files = dir.listFiles();
            if (files == null) {
                return new HashMap<>();
            }
            Map<String, String> result = Arrays.stream(files)
                    .filter(file -> !file.isDirectory())
                    .map(file -> {
                        PutObjectRequest request = new PutObjectRequest(bucketName, file.getPath(), file);
                        PutObjectResult putObjectResult = myOSSClient.putObject(request);
                        return Pair.of(file.getName(), putObjectResult.getETag());
                    }).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

            Arrays.stream(files)
                    .filter(file -> file.isDirectory() && recursion)
                    .map(otherDir -> uploadDirectory2Oss(bucketName, otherDir.getPath(), true))
                    .forEach(result::putAll);
            return result;
        } catch (OSSException | ClientException e) {
            throw new ServerException("上传文件夹失败，阿里云OSS错误");
        } catch (RuntimeException e) {
            throw new ServerException("上传文件夹失败，服务器错误");
        }
    }
}
