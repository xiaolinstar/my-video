package com.xiaolin.video.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.video.dao.entity.VideoRecord;
import com.xiaolin.video.service.VideoRecordService;
import com.xiaolin.video.dao.mapper.VideoRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author xlxing
* @description 针对表【video_record(用户视频记录)】的数据库操作Service实现
* @createDate 2023-07-02 12:44:52
*/
@Service
public class VideoRecordServiceImpl extends ServiceImpl<VideoRecordMapper, VideoRecord>
    implements VideoRecordService{

}




