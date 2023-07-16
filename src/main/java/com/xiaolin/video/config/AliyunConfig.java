package com.xiaolin.video.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义MyOSSClient可以在Spring程序关闭的时候shutdown
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 阿里云服务相关配置，获取阿里云OSS访问的Bean
 * @create 2023/7/15
 */
@Configuration
public class AliyunConfig {

    /**
     * TODO 优先使用本地环境变量安全创建
     * @return 自定义的OSSClient，支持在Spring程序终止时关闭
     */
    @Bean
    public MyOSSClient myOSSClient(){
        // Endpoint地域节点
        String endpoint = "";
        String accessKey = "";
        String accessSecret = "";
//        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        return new MyOSSClient(endpoint, accessKey, accessSecret);
    }
}
