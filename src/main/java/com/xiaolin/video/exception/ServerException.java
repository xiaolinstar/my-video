package com.xiaolin.video.exception;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 服务端异常
 * @create 2023/7/2
 */
public class ServerException extends RuntimeException{
    public ServerException(String message) {
        super(message);
    }
}
