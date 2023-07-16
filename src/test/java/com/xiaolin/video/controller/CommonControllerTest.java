package com.xiaolin.video.controller;

import com.xiaolin.video.common.component.AliyunOSSComponent;
import com.xiaolin.video.common.component.FileUploadComponent;
import com.xiaolin.video.common.component.M3U8Component;
import com.xiaolin.video.common.dto.resp.FileUploadDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 上传文件 文件转M3U8 上传到阿里云OSS
 * @create 2023/7/15
 */
@SpringBootTest
class CommonControllerTest {

    @Autowired
    private FileUploadComponent fileUploadComponent;

    @Autowired
    private M3U8Component m3U8Component;

    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;

    @Autowired
    private ThreadPoolExecutor mediaThreadPoolExecutor;


    @Test
    @Order(1)
    public void testFileUpload() throws IOException {
        String fileDir = "videos/";
        String fileName = "性爱自修室-1080p.m4v";
        MultipartFile file = new MockMultipartFile(fileName, fileName, "multipart/form-data", new FileInputStream(fileDir+fileName));
        FileUploadDto uploadFile = fileUploadComponent.uploadFile(file, fileDir, "性爱自修室3");
        assertNotNull(uploadFile);
    }

    @Test
    @Order(2)
    public void testEncodeM3U8() {
        String fileName = "videos/功夫BD国粤双语中字.mp4";
        String targetDirectory = m3U8Component.encodeM3U8(fileName);
        System.out.println(targetDirectory);
    }
    @AfterEach
    public void cleanup() throws InterruptedException {
        mediaThreadPoolExecutor.shutdown();
        boolean awaited = mediaThreadPoolExecutor.awaitTermination(1, TimeUnit.HOURS);
        if (awaited) {
            System.out.println("已经没有线程在执行");
        }
    }

    @Test
    public void testUploadFile2Oss() {
        String bucketName = "vod-videos-xiaolin";
        String dir = "videos/性爱自修室m3u8";
        aliyunOSSComponent.uploadDirectory2Oss(bucketName, dir, true);
    }

    @Test
    public void testVideoUpload() throws IOException {
        String fileDir = "videos/";
        String fileName = "功夫BD国粤双语中字.mp4";
        String bucketName = "vod-videos-xiaolin";
        // 1. 模拟前端文件上传请求
        MultipartFile file = new MockMultipartFile(fileName, fileName, "multipart/form-data", new FileInputStream(fileDir+fileName));
        // 2. 上传文件到服务器
        FileUploadDto fileUploadDto = fileUploadComponent.uploadFile(file, fileDir, "功夫");
        String filePath = fileUploadDto.getFileName();
        // 3. 文件编码位m3u8格式
        String m3u8Directory = m3U8Component.encodeM3U8(filePath);
        // 4. 文件上传到阿里云OSS

        Map<String, String> uploadMap = aliyunOSSComponent.uploadDirectory2Oss(bucketName, m3u8Directory, false);
        uploadMap.forEach(
                (key, value) -> System.out.println(key + ":" + value)
        );
    }
}