package com.xiaolin.video.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.video.dao.entity.VideoDetail;
import com.xiaolin.video.service.VideoDetailService;
import com.xiaolin.video.dao.mapper.VideoDetailMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
* @author xlxing
* @description 针对表【video_detail(视频详情)】的数据库操作Service实现
* @createDate 2023-07-02 12:44:52
*/
@Service
public class VideoDetailServiceImpl extends ServiceImpl<VideoDetailMapper, VideoDetail>
    implements VideoDetailService{

    /**
     * 获取视频详情
     *
     * @param id 视频主键
     * @return 视频详情返回值
     */
    @Override
    public Optional<VideoDetail> one(Long id) {
        return Optional.ofNullable(this.getById(id));
    }
}




