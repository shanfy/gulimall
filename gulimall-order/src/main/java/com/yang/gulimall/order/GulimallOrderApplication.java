package com.yang.gulimall.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 使用RabbitMQ
 * 1、引入amqp场景;RabbitAutoConfiguration 就会自动生效
 * 2、给容器中自动配置了
 *      RabbitTemplate、AmqpAdmin、CachingConnectionFactory、RabbitMessagingTemplate
 * 3、给配置文件中配置 spring.rabbitmg 信息
 * 4、@EnableRabbit: @EnabLeXxxxx;开启功能监听消息:
 * 5、监听消息:使用@RabbitListener;必须有@EnableRabbit
 *  *      @RabbitListener: 类+方法上（指定监听哪些队列即可）
 *          @RabbitHandler: 标在方法上 (重载区分不同的消息)
 * 使用RabbitMQ
 * 1、引入amqp场景;RabbitAutoConfiguration 就会自动生效
 *
 *

 *
 * Seata控制分布式事务
 *  1）、每一个微服务必须创建undo_Log
 *  2）、安装事务协调器：seate-server
 *  3）、整合
 *      1、导入依赖
 *      2、解压并启动seata-server：
 *          registry.conf:注册中心配置    修改 registry ： nacos
 *      3、所有想要用到分布式事务的微服务使用seata DataSourceProxy 代理自己的数据源
 *      4、每个微服务，都必须导入   registry.conf   file.conf
 *          vgroup_mapping.{application.name}-fescar-server-group = "default"
 *      5、启动测试分布式事务
 *      6、给分布式大事务的入口标注@GlobalTransactional
 *      7、每一个远程的小事务用@Transactional
 *
 *      本地事务失效问题
 * 同一个对象内事务方法互调默认失效，原因 绕过了代理对象，事务使用代理对象来控制的
 * 解决:使用代理对象来调用事务方法
 * 1)、引入aop-starter;spring-boot-starter-aop;引入了aspectj
 * 2)、@EnableAspectJAutoProxv;开启aspecti 动态代理功能。以后所有的动态代理都是gspecti创建的(即使没有接门也能创建动态代理对象)
 * 3)、用调用对象本类互调
 * eg：Object o =AopContext.currentProxy();
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableRedisHttpSession     //开启springsession
@EnableRabbit
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class, args);
    }

}
