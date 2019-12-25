package com.wzy.seckill.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Redis配置从application.properties加载配置文件
 * @author wzy
 * @version 1.0
 * @date 2019/12/16 20:13
 */
@Configuration
@ConfigurationProperties(prefix = "redis")
@Data
public class RedisConfig {
    private String host;

    private int port;

    private int timeout;//秒

    private String password;

    private int poolMaxTotal;

    private int poolMaxIdle;

    private int poolMaxWait;//秒
}
