package com.xiaolin.video.exception;

import com.xiaolin.video.common.dto.RestResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 统一异常处理
 * @create 2023/7/2
 */
@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    @ExceptionHandler(ServerException.class)
    public RestResp<String> serverExceptionHandler(ServerException e) {
        log.error(e.getMessage());
        return RestResp.error(e.getMessage(), 501);
    }

    @ExceptionHandler(ClientException.class)
    public RestResp<String> clientExceptionHandler(ClientException e) {
        log.error(e.getMessage());
        return RestResp.error(e.getMessage(), 401);
    }

    @ExceptionHandler(OtherException.class)
    public RestResp<String> otherExceptionHandler(OtherException e) {
        log.error(e.getMessage());
        return RestResp.error(e.getMessage(), 600);
    }
}
