package com.xiaolin.video.exception;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 客户端错误
 * @create 2023/7/2
 */
public class ClientException extends RuntimeException{
    public ClientException(String message) {
        super(message);
    }
}
