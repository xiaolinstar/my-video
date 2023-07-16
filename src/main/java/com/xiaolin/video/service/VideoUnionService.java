package com.xiaolin.video.service;

import com.xiaolin.video.common.dto.req.VideoUploadRequestDto;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 视频服务类联合服务类，Controller直接调用该接口
 * @create 2023/7/2
 */

public interface VideoUnionService {
    /**
     * 新增视频
     * @param videoUploadRequestDto 视频相关信息
     * @return 添加结果
     */
    boolean insertOne(VideoUploadRequestDto videoUploadRequestDto);

    /**
     * 删除视频
     * @param id 主键
     * @return 删除结果
     */
    boolean deleteOne(Long id);

    boolean updateOne(Long id, VideoUploadRequestDto videoUploadRequestDto);
}
