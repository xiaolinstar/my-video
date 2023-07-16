package com.xiaolin.video.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * TODO 替换为ThreadPoolTaskExecutor
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 配置和创建线程池
 * @create 2023/7/15
 */
@Configuration
@Slf4j
public class ThreadPoolConfig {

    private static final int CORE_POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 3;

    private static final long KEEP_ALIVE_TIME = 12L;

    private static final int QUEUE_CAPACITY = 10;


    /**
     * 视频编码使用线程池技术
     * @return 线程池执行类
     */
    @Bean
    public ThreadPoolExecutor mediaThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.HOURS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                // 抛出服务拒绝异常
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
