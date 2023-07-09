package com.xiaolin.video;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 启动类
 * @author xingxiaolin xlxing@bupt.edu.cn
 */
@SpringBootApplication
@MapperScan("com.xiaolin.video.dao.mapper")
public class VideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoApplication.class, args);
    }

}
