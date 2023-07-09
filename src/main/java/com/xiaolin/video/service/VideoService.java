package com.xiaolin.video.service;

import com.xiaolin.video.dao.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
* @author xlxing
* @description 针对表【video(视频基本信息)】的数据库操作Service
* @createDate 2023-07-02 12:44:52
*/
public interface VideoService extends IService<Video> {

    /**
     * 根据视频主键id获取视频基本信息
     * @param id 视频主键
     */
    Optional<Video> getVideoById(Long id);

    /**
     * 上传视频到服务器
     * @param videoFile 视频文件
     * @return 视频资源地址
     */
    Optional<String> uploadVideo(MultipartFile videoFile);
}
