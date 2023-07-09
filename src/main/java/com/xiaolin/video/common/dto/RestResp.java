package com.xiaolin.video.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description RestFul 统一返回包装类
 * @create 2023/7/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestResp<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> RestResp<T>success(T data) {
        return new RestResp<>(0, "成功发送请求", data);
    }

    public static <T> RestResp<T>success() {
        return new RestResp<>(0, "请求成功响应", null);
    }
    public static <T> RestResp<T>error(String msg, Integer code) {
        return new RestResp<>(code, msg, null);
    }

}
