package com.xiaolin.video.config;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description
 * @create 2023/7/12
 */

@Configuration
public class FFmpegConfig {
    @Value("${ffmpeg.path.ffmpeg}")
    private String ffmpegPath;

    @Value("${ffmpeg.path.ffprobe}")
    private String ffprobePath;

    @Bean
    public FFmpeg fFmpeg() throws IOException {
        return new FFmpeg(ffmpegPath);
    }

    @Bean
    public FFprobe fFprobe() throws IOException {
        return new FFprobe(ffprobePath);
    }

}
