package com.xiaolin.video.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description
 * @create 2023/7/15
 */

public class MyOSSClient extends OSSClient implements OSS, DisposableBean {

    public MyOSSClient(String endpoint, String accessKey, String accessSecret) {
        super(endpoint, accessKey, accessSecret);
    }
    public MyOSSClient(String endpoint, CredentialsProvider provider) {
        super(endpoint, provider);
    }
    @Override
    public void destroy() throws Exception {
        this.shutdown();
    }
}
