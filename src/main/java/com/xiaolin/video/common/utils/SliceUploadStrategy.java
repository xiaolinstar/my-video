package com.xiaolin.video.common.utils;

import com.xiaolin.video.common.dto.req.SliceFileUploadRequestDto;
import com.xiaolin.video.common.dto.resp.FileUploadDto;

import java.io.IOException;
import java.util.Set;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 切片上传策略
 * @create 2023/7/8
 */
public interface SliceUploadStrategy {
    /**
     * 分片上传文件
     * @param requestDto 文件上传请求信息
     * @param fileDir 目标文件夹
     * @return 文件上传信息回复
     */
    FileUploadDto sliceUpload(SliceFileUploadRequestDto requestDto, String fileDir) throws IOException;

    /**
     * 合并文件，并进行md5校验
     * @param fileDir 文件夹
     * @param filename 文件名
     * @param chunks 文件总分片数
     * @param originalMd5 源文件的md5值
     * @return 文件合并是否成功
     */
    FileUploadDto sliceFilesMerge(String fileDir, String filename, int chunks, String originalMd5) throws IOException;

    /**
     * 秒传，服务器端已经存在用户将要传输的文件，服务器直接返回目标文件
     * @param requestDto 文件请求
     * @return 文件上传消息
     */
    FileUploadDto instantUpload(SliceFileUploadRequestDto requestDto);


    /**
     * 断点续传
     * @param requestDto 传输请求
     * @param fileDir 文件夹
     * @return 传输结果
     */
    Set<Integer> continueUpload(SliceFileUploadRequestDto requestDto, String fileDir);
}
