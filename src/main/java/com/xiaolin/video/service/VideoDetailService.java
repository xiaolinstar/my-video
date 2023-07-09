package com.xiaolin.video.service;

import com.xiaolin.video.dao.entity.VideoDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Optional;

/**
* @author xlxing
* @description 针对表【video_detail(视频详情)】的数据库操作Service
* @createDate 2023-07-02 12:44:52
*/
public interface VideoDetailService extends IService<VideoDetail> {

    /**
     * 获取视频详情
     * @param id 视频主键
     * @return 视频详情返回值
     */
    Optional<VideoDetail> one(Long id);


}
