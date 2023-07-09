package com.xiaolin.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class VideoApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(LocalDateTime.now());
    }

}
