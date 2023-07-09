package com.xiaolin.video.service.impl;

import com.xiaolin.video.common.dto.req.VideoUploadRequestDto;
import com.xiaolin.video.dao.entity.Video;
import com.xiaolin.video.dao.entity.VideoDetail;
import com.xiaolin.video.service.VideoDetailService;
import com.xiaolin.video.service.VideoService;
import com.xiaolin.video.service.VideoUnionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description
 * @create 2023/7/2
 */
@Service
@RequiredArgsConstructor
public class VideoUnionServiceImpl implements VideoUnionService {
    private final VideoService videoService;
    private final VideoDetailService videoDetailService;
    private final TransactionTemplate transactionTemplate;

    /**
     * 新增视频
     *
     * @param videoUploadRequestDto 视频相关信息
     * @return 添加状态
     */
    @Override
    public boolean insertOne(VideoUploadRequestDto videoUploadRequestDto) {
        Video video = Video.builder()
                .icon(videoUploadRequestDto.getIcon())
                .name(videoUploadRequestDto.getName())
                .tag(videoUploadRequestDto.getTag())
                .type(videoUploadRequestDto.getType())
                .district(videoUploadRequestDto.getDistrict())
                .year(videoUploadRequestDto.getYear())
                .build();

        VideoDetail videoDetail = VideoDetail.builder()
                .director(videoUploadRequestDto.getDirector())
                .scriptWriter(videoUploadRequestDto.getScriptWriter())
                .resource(videoUploadRequestDto.getResource())
                .leadingActor(videoUploadRequestDto.getLeadingActor())
                .language(videoUploadRequestDto.getLanguage())
                .releaseDate(videoUploadRequestDto.getReleaseDate())
                .duration(videoUploadRequestDto.getDuration())
                .description(videoUploadRequestDto.getDescription())
                .alias(videoUploadRequestDto.getAlias())
                .build();

        // 使用事物存数据库
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            boolean savedVideo = videoService.save(video);
            boolean savedVideoDetail = videoDetailService.save(videoDetail);
            return savedVideo && savedVideoDetail;
        }));
    }

    /**
     * 删除视频
     *
     * @param id 主键
     * @return 删除结果
     */
    @Override
    public boolean deleteOne(Long id) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            boolean removed1 = videoService.removeById(id);
            boolean removed2 = videoDetailService.removeById(id);
            return removed1 && removed2;
        }));
    }


}
