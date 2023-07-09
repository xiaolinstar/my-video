package com.xiaolin.video.controller;

import com.xiaolin.video.common.dto.RestResp;
import com.xiaolin.video.dao.entity.VideoDetail;
import com.xiaolin.video.exception.ServerException;
import com.xiaolin.video.service.VideoDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description
 * @create 2023/7/2
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/video-detail")
public class VideoDetailController {
    private final VideoDetailService videoDetailService;

    @GetMapping("/{id}")
    public RestResp<VideoDetail> one(@PathVariable Long id) {
        VideoDetail videoDetail = videoDetailService.one(id).orElseThrow(() -> new ServerException("服务器端发生错误"));
        return RestResp.success(videoDetail);
    }
}
