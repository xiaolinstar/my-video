package com.xiaolin.video.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.video.dao.entity.Video;
import com.xiaolin.video.exception.ClientException;
import com.xiaolin.video.service.VideoService;
import com.xiaolin.video.dao.mapper.VideoMapper;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
* @author xlxing
* @description 针对表【video(视频基本信息)】的数据库操作Service实现
* @createDate 2023-07-02 12:44:52
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{

    /**
     * 根据视频主键id获取视频基本信息
     * @param id 视频主键
     */
    @Override
    public Optional<Video> getVideoById(Long id) {
        return Optional.ofNullable(this.getById(id));
    }

    /**
     * 上传视频到服务器
     * 大文件上传问题
     * @param videoFile 视频文件
     * @return 视频资源地址
     */
    @Override
    public Optional<String> uploadVideo(MultipartFile videoFile) {
        if (videoFile.isEmpty()) {
            throw new ClientException("文件为空，请重新上传");
        }
        throw new NotImplementedException();
    }

}




