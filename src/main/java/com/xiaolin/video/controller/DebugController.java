package com.xiaolin.video.controller;

import com.xiaolin.video.common.dto.RestResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 考虑程序的多态性，已经接口的友好型
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description
 * @create 2023/7/16
 */
@RequestMapping("/api/v1/debug")
@RestController
public class DebugController {

    @GetMapping("/video")
    public RestResp<String> test() {
//        m3U8Component.encodeM3U8("videos/扬名立万.HD1080P.x264.CHS.mp4", "videos/扬名立万");
        return RestResp.success();
    }
}

