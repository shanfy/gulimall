package com.yang.gulimall.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


/**
 * 核心原理
 * 1）、@EnableRedisHttpSession导入RedisHttpSessionConfiguration配置
 *      1、给容器中添加了一个组件
 *          RedisOperationsSessionRepository：Redis操作session，session的增删改查封装类
 *      2、SessionRepositoryFilter=》HttpFilter: session存储过滤器,每个请求过来都必须经过filter
 *          1)、创建的时候自动从容器中获取到了 RedisOperationsSessionRepository
 *          2）、原生的request和response都被包装，SessionRepositoryRequestWrapper和SessionRepositoryResponseWrapper
 *          3）、以后获取session,request.getSession()==>wrappedRequest.getSession()=>SessionRepository中获取到==》从redis中获取
 *
 *  通过使用装饰者模式，对原生request和response进行包装，增强功能
 *
 *
 */

@EnableRedisHttpSession     //整合Redis作为session存储
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallAuthServerApplication.class, args);
    }

}
