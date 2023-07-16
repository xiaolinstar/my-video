package com.xiaolin.video.controller;

import com.xiaolin.video.common.component.M3U8Component;
import com.xiaolin.video.common.dto.RestResp;
import com.xiaolin.video.common.dto.req.VideoUploadRequestDto;
import com.xiaolin.video.dao.entity.Video;
import com.xiaolin.video.exception.ClientException;
import com.xiaolin.video.service.VideoService;
import com.xiaolin.video.service.VideoUnionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description
 * @create 2023/7/2
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/video")
public class VideoController {
    private final VideoService videoService;
    private final VideoUnionService videoUnionService;
    private final M3U8Component m3U8Component;

    /**
     * 根据视频主键查询视频基本信息
     * @param id 主键
     * @return 视频基本信息
     */
    @GetMapping("/{id}")
    public RestResp<Video> one(@PathVariable Long id) {
        Optional<Video> videoOptional = videoService.getVideoById(id);
        Video video = videoOptional.orElseThrow(() -> new ClientException("Not found video: " + id));
        return RestResp.success(video);
    }

    /**
     * 新增一个视频，必填项
     * @param videoDto 视频详情
     * @return 添加结果
     */
    @PostMapping
    public RestResp<String> insertOne(@RequestBody VideoUploadRequestDto videoDto) {
        boolean insertOne = videoUnionService.insertOne(videoDto);
        return insertOne? RestResp.success(): RestResp.error("添加失败", 500);
    }

    /**
     * 根据id删除视频
     * @param id 视频主键
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public RestResp<String> removeOne(@PathVariable Long id) {
        boolean deleteOne = videoUnionService.deleteOne(id);
        return deleteOne? RestResp.success(): RestResp.error("删除失败", 503);
    }

    /**
     * 删除视频
     * @param id 视频主键
     * @param videoDto 视频信息
     * @return 删除消息
     */
    @PutMapping("/{id}")
    public RestResp<String> updateOne(@PathVariable Long id, @RequestBody VideoUploadRequestDto videoDto) {
        boolean updated = videoUnionService.updateOne(id, videoDto);
        return updated? RestResp.success(): RestResp.error("更新失败", 504);
    }

}
