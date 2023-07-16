package com.xiaolin.video.common.component;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description
 * @create 2023/7/9
 */
@SpringBootTest
class M3U8ComponentTest {

    @Autowired
    private M3U8Component m3u8Component;

    @Autowired
    private FFprobe fFprobe;

    @Test
    public void testFFmpegMedia2M3U8() throws IOException {
//        m3u8Component.convertToM3U8("videos/扬名立万.HD1080P.x264.CHS.mp4", "videos/扬名立万/");
        String inputFilePath = "videos/扬名立万.HD1080P.x264.CHS.mp4";
        Path inputFile = Path.of(inputFilePath);
        String fileNameWithoutExtension = inputFile.getFileName().toString().replaceFirst("[.][^.]+$", "");
        System.out.println(fileNameWithoutExtension);
        FFmpegProbeResult probe = fFprobe.probe(inputFilePath);
        FFmpegStream videoStream = probe.getStreams().get(0);
        int width = videoStream.width;
        int height = videoStream.height;
        System.out.println("分辨率：" + width +"x" + height);
    }
}
