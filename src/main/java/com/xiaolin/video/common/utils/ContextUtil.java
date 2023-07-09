package com.xiaolin.video.common.utils;

/**
 * 存储用户id
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 上下文工具，封装ThreadLocal数据结构
 * @create 2023/7/2
 */
public class ContextUtil {
    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<Long>();

    public static void setCurrentId(Long id) {
        THREAD_LOCAL.set(id);
    }

    public static Long getCurrentId() {
        return THREAD_LOCAL.get();
    }
}
