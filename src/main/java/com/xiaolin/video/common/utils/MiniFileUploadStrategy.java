package com.xiaolin.video.common.utils;

import com.xiaolin.video.common.dto.req.FileUploadRequestDto;
import com.xiaolin.video.common.dto.resp.FileUploadDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 常规文件或小文件上传策略
 * @create 2023/7/9
 */
public interface MiniFileUploadStrategy {

    /**
     * 小文件上传
     * @param file 文件上传请求
     * @return 文件上传回复消息
     */
    FileUploadDto uploadFile(MultipartFile file, String fileDir, String fileName) throws IOException;

    /**
     * 下载文件 文件存放在HttpServletResponse中
     * @param response HttpServletResponse
     * @param fileDir 文件路径
     * @param name 文件名
     * @throws IOException 写文件时可能的IO异常
     */
    void downloadFile(HttpServletResponse response, String fileDir, String name) throws IOException;
}
