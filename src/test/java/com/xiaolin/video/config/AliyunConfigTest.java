package com.xiaolin.video.config;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.UploadFileResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 阿里云存储测试
 * @create 2023/7/15
 */

@SpringBootTest
class AliyunConfigTest {

    @Autowired
    private OSS videoOssClient;

    @Test
    public void testAliyunUploadFile() {
        String bucketName = "vod-videos-xiaolin";
        String uploadFilePath = "videos/扬名立万/output_360p_000.ts";
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uploadFilePath, new File(uploadFilePath));
            PutObjectResult putObjectResult = videoOssClient.putObject(putObjectRequest);
            assertNotNull(putObjectResult);
        } catch (OSSException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testFile() {
        String uploadFilePath = "videos/扬名立万/output_360p_000.ts";
        File file = new File(uploadFilePath);
        System.out.println(file.getName());
        System.out.println(file.getPath());
    }
}
