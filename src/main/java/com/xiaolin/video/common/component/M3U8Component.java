package com.xiaolin.video.common.component;

import com.xiaolin.video.exception.ClientException;
import com.xiaolin.video.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TODO 多种分辨率的m3u8合并为master.m3u8
 * TODO 对更多参数需要进一步了解和设置
 * TODO m3u8无法访问到默认相对路径的ts文件， 比如无法访问'0001.ts'而可以访问到'./0001.ts'
 * Done 将转码任务加入到线程池中，自动运行，待测试
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 视频转M3U8格式
 * @create 2023/7/9
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class M3U8Component {
    private final FFmpeg fFmpeg;
    private final FFprobe fFprobe;
    private final ThreadPoolExecutor mediaThreadPoolExecutor;

    public Pair<Integer, Integer> getVideoResolution(String inputFilePath) throws IOException {
        FFmpegProbeResult result = fFprobe.probe(inputFilePath);
        FFmpegStream videoStream = result.getStreams().get(0);
        return Pair.of(videoStream.width, videoStream.height);
    }

    /**
     * 创建输入文件同名目录
     * @param inputFilePath 文件路径
     * @return m3u8文件路径
     */
    public String encodeM3U8(String inputFilePath) {
        return encodeM3U8(inputFilePath, null);
    }

    public String encodeM3U8(String inputFilePath, String outputDirectory) {
        String targetDirectory = Optional.ofNullable(outputDirectory)
                .orElse(inputFilePath.substring(0, inputFilePath.lastIndexOf(".")));
        try {
            log.info("start encode {}  to m3u8 file, and save at {}", inputFilePath, targetDirectory);
            String fileNameWithoutExt = Path.of(inputFilePath)
                    .getFileName()
                    .toString()
                    .replaceFirst("[.][^.]+$", "");
            Pair<Integer, Integer> videoResolution = this.getVideoResolution(inputFilePath);
            File targetFile = new File(targetDirectory);
            if (!targetFile.exists()) {
                boolean succeed = targetFile.mkdirs();
                if (!succeed) {
                    throw new ServerException("创建文件夹'" + targetDirectory + "'失败");
                }
            }
            String outputPath1080p = targetDirectory + "/" + fileNameWithoutExt +"_1080p.m3u8";
            String outputPath720p = targetDirectory + "/" + fileNameWithoutExt +"_720p.m3u8";
            String outputPath360p = targetDirectory + "/" + fileNameWithoutExt +"_360p.m3u8";
            FFmpegBuilder builder = new FFmpegBuilder()
                    .overrideOutputFiles(true)
                    .setInput(inputFilePath)
                    .addOutput(outputPath1080p)
                    .setFormat("hls")
                    .setAudioCodec("aac")
                    .setVideoCodec("libx264")
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                    .addExtraArgs("-vf", "scale=1920:1080")
                    .addExtraArgs("-hls_time", "10")
                    .addExtraArgs("-hls_list_size", "0")
                    .addExtraArgs("-hls_segment_filename", targetDirectory + "/output_1080p_%03d.ts")
                    .done()
                    .addOutput(outputPath720p)
                    .setFormat("hls")
                    .setAudioCodec("aac")
                    .setVideoCodec("libx264")
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                    .addExtraArgs("-vf", "scale=1280:720")
                    .addExtraArgs("-hls_time", "10")
                    .addExtraArgs("-hls_list_size", "0")
                    .addExtraArgs("-hls_segment_filename", targetDirectory + "/output_720p_%03d.ts")
                    .done()
                    .addOutput(outputPath360p)
                    .setFormat("hls")
                    .setAudioCodec("aac")
                    .setVideoCodec("libx264")
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                    .addExtraArgs("-vf", "scale=640:360")
                    .addExtraArgs("-hls_time", "10")
                    .addExtraArgs("-hls_list_size", "0")
                    .addExtraArgs("-hls_segment_filename", targetDirectory + "/output_360p_%03d.ts")
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(fFmpeg, fFprobe);
            FFmpegProbeResult in = fFprobe.probe(inputFilePath);
            FFmpegJob job = executor.createJob(builder, new ProgressListener() {
                final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);
                @Override
                public void progress(Progress progress) {
                    int percentage = (duration_ns > 0) ? (int) (progress.out_time_ns / duration_ns * 100) : 99;
                    // 日志中输出转换进度信息
                    log.debug("[{}%] status: {}, frame: {}, time: {} ms, fps: {}, speed: {}x",
                            percentage,
                            progress.status,
                            progress.frame,
                            FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
                            progress.fps.doubleValue(),
                            progress.speed
                    );
                }
            });
            mediaThreadPoolExecutor.submit(job);
            log.info("添加编码任务 {} 到线程池中", inputFilePath);
            return targetDirectory;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ClientException("视频编码错误");
        } catch (RejectedExecutionException e) {
            log.debug(e.getMessage());
            log.debug("线程池已满，拒绝添加新任务");
            throw new ServerException("服务器错误");
        } catch (Exception e) {
            throw new ServerException("其他服务器错误:" + e.getMessage());
        }
    }

    /* 这两个方法先注释掉
    public void convertToM3U8(String inputFilePath, String outputDirectory) {
        try {
            String fileNameWithoutExt = Path.of(inputFilePath)
                    .getFileName()
                    .toString()
                    .replaceFirst("[.][^.]+$", "");
            String outputPath = outputDirectory + "/" + fileNameWithoutExt +".m3u8";
            FFmpegBuilder builder = new FFmpegBuilder()
                    // 覆盖输出文件
                    .overrideOutputFiles(true)
                    // 输入文件地址
                    .setInput(inputFilePath)
                    // 输出文件名
                    .addOutput(outputPath)
                    .setFormat("hls")
                    .setAudioCodec("aac")
                    .setVideoCodec("libx264")
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                    .setConstantRateFactor(18)
                    .addExtraArgs("-hls_time", "10")
                    .addExtraArgs("-hls_list_size", "0")
                    .addExtraArgs("-hls_segment_filename", outputDirectory + "/output%03d.ts")
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(fFmpeg, fFprobe);
            FFmpegProbeResult in = fFprobe.probe(inputFilePath);
            FFmpegJob job = executor.createJob(builder, new ProgressListener() {
                final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);
                @Override
                public void progress(Progress progress) {
                    int percentage = (duration_ns > 0) ? (int) (progress.out_time_ns / duration_ns * 100) : 99;
                    System.out.print(percentage + " ");
                    System.out.print(progress.status + " ");
                    System.out.print(progress.frame + " ");
                    System.out.print(FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS) + " ");
                    System.out.print(progress.fps.doubleValue() + " ");
                    System.out.println(progress.speed);
                }
            });
            job.run();
            System.out.println("转码完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void encodeM3U8WithMaster(String inputFilePath, String outputDirectory) {
        try {
            String fileNameWithoutExt = Path.of(inputFilePath)
                    .getFileName()
                    .toString()
                    .replaceFirst("[.][^.]+$", "");
            Pair<Integer, Integer> videoResolution = this.getVideoResolution(inputFilePath);
            String outputPath1080p = outputDirectory + "/" + fileNameWithoutExt +"_1080p.m3u8";
            String outputPath720p = outputDirectory + "/" + fileNameWithoutExt +"_720p.m3u8";
            String outputPath360p = outputDirectory + "/" + fileNameWithoutExt +"_360p.m3u8";
            String masterM3U8 = outputDirectory + "/master.m3u8";
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(inputFilePath)
                    .addExtraArgs("-filter_complex", "[0:v]split=3[v1][v2][v3]; [v1]copy[v1out]; [v2]scale=w=1280:h=720[v2out]; [v3]scale=w=640:h=360[v3out]")
                    .addOutput("stream_%v/data%02d.ts")
                    .setVideoCodec("libx264")
                    .addExtraArgs("-x264-params", "nal-hrd=cbr:force-cfr=1")
                    .addExtraArgs("-b:v:0", "5M")
                    .addExtraArgs("-maxrate:v:0", "5M")
                    .addExtraArgs("-minrate:v:0", "5M")
                    .addExtraArgs("-bufsize:v:0", "10M")
                    .addExtraArgs("-preset", "slow")
                    .addExtraArgs("-g", "48")
                    .addExtraArgs("-sc_threshold", "0")
                    .addExtraArgs("-keyint_min", "48")
                    .done()
                    .addOutput("stream_%v/data%02d.ts")
                    .setVideoCodec("libx264")
                    .addExtraArgs("-x264-params", "nal-hrd=cbr:force-cfr=1")
                    .addExtraArgs("-b:v:1", "3M")
                    .addExtraArgs("-maxrate:v:1", "3M")
                    .addExtraArgs("-minrate:v:1", "3M")
                    .addExtraArgs("-bufsize:v:1", "3M")
                    .addExtraArgs("-preset", "slow")
                    .addExtraArgs("-g", "48")
                    .addExtraArgs("-sc_threshold", "0")
                    .addExtraArgs("-keyint_min", "48")
                    .done()
                    .addOutput("stream_%v/data%02d.ts")
                    .setVideoCodec("libx264")
                    .addExtraArgs("-x264-params", "nal-hrd=cbr:force-cfr=1")
                    .addExtraArgs("-b:v:2", "1M")
                    .addExtraArgs("-maxrate:v:2", "1M")
                    .addExtraArgs("-minrate:v:2", "1M")
                    .addExtraArgs("-bufsize:v:2", "1M")
                    .addExtraArgs("-preset", "slow")
                    .addExtraArgs("-g", "48")
                    .addExtraArgs("-sc_threshold", "0")
                    .addExtraArgs("-keyint_min", "48")
                    .done()
                    .addOutput("stream_%v.m3u8")
                    .setAudioCodec("aac")
                    .addExtraArgs("-b:a:0", "96k")
                    .addExtraArgs("-ac", "2")
                    .done()
                    .addOutput("stream_%v.m3u8")
                    .setAudioCodec("aac")
                    .addExtraArgs("-b:a:1", "72k")
                    .addExtraArgs("-ac", "2")
                    .done()
                    .addOutput("stream_%v.m3u8")
                    .setAudioCodec("aac")
                    .addExtraArgs("-b:a:2", "48k")
                    .addExtraArgs("-ac", "2")
                    .done()
                    .setFormat("hls")
                    .addExtraArgs("-hls_time", "2")
                    .addExtraArgs("-hls_playlist_type", "vod")
                    .addExtraArgs("-hls_flags", "independent_segments")
                    .addExtraArgs("-hls_segment_type", "mpegts")
                    .addOutput("master.m3u8")
                    .addExtraArgs("-master_pl_name", "master.m3u8")
                    .addExtraArgs("-var_stream_map", "v:0,a:0 v:1,a:1 v:2,a:2")
                    .done();
            FFmpegExecutor executor = new FFmpegExecutor(fFmpeg, fFprobe);
            FFmpegProbeResult in = fFprobe.probe(inputFilePath);
            FFmpegJob job = executor.createJob(builder, new ProgressListener() {
                final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);
                @Override
                public void progress(Progress progress) {
                    int percentage = (duration_ns > 0) ? (int) (progress.out_time_ns / duration_ns * 100) : 99;
                    System.out.print(percentage + " ");
                    System.out.print(progress.status + " ");
                    System.out.print(progress.frame + " ");
                    System.out.print(FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS) + " ");
                    System.out.print(progress.fps.doubleValue() + " ");
                    System.out.println(progress.speed);
                }
            });
            job.run();
            System.out.println("转码完成！");

        } catch (IOException e) {
            throw new ClientException("上传文件分辨率错误");
        }
    }

     */
}
