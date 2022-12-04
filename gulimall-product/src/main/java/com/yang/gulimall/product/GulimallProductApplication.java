package com.yang.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 1、整合MyBatis-Plus
 *      1）、导入依赖
 *      <dependency>
 *             <groupId>com.baomidou</groupId>
 *             <artifactId>mybatis-plus-boot-starter</artifactId>
 *             <version>3.2.0</version>
 *      </dependency>
 *      2）、配置
 *          1、配置数据源；
 *              1）、导入数据库的驱动。https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-versions.html
 *              2）、在application.yml配置数据源相关信息
 *          2、配置MyBatis-Plus；
 *              1）、使用@MapperScan
 *              2）、告诉MyBatis-Plus，sql映射文件位置
 *
 * 2、逻辑删除
 *  1）、配置全局的逻辑删除规则（省略）
 *  2）、配置逻辑删除的组件Bean（省略）
 *  3）、给Bean加上逻辑删除注解@TableLogic
 *
 * 3、JSR303
 *   1）、给Bean添加校验注解:javax.validation.constraints，并定义自己的message提示
 *   2)、开启校验功能@Valid
 *      效果：校验错误以后会有默认的响应；
 *   3）、给校验的bean后紧跟一个 BindingResult，就可以获取到校验的结果
 *   4）、分组校验（多场景的复杂校验）
 *         1)、	@NotBlank(message = "品牌名必须提交",groups = {AddGroup.class,UpdateGroup.class})
 *          给校验注解标注什么情况需要进行校验
 *         2）、@Validated({AddGroup.class})
 *         3)、默认没有指定分组的校验注解@NotBlank，在分组校验情况@Validated({AddGroup.class})下不生效，只会在@Validated生效；
 *
 *   5）、自定义校验
 *      1）、编写一个自定义的校验注解
 *      2）、编写一个自定义的校验器 ConstraintValidator
 *      3）、关联自定义的校验器和自定义的校验注解
 *      @Documented
 * @Constraint(validatedBy = { ListValueConstraintValidator.class【可以指定多个不同的校验器，适配不同类型的校验】 })
 * @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
 * @Retention(RUNTIME)
 * public @interface ListValue {
 *
 * 4、统一的异常处理
 * @ControllerAdvice
 *  1）、编写异常处理类，使用@ControllerAdvice。
 *  2）、使用@ExceptionHandler标注方法可以处理的异常。
 *
 *
 *  7、整合redisson作为redis分布式锁等功能框架
 *      1）、引入依赖
*          <dependency>
 *             <groupId>org.redisson</groupId>
 *             <artifactId>redisson</artifactId>
 *             <version>3.12.0</version>
 *         </dependency>
 *      2）、配置redisson
 *      3)、使用，参照文档
 *  8、整合SpringCache简化缓存开发
 *      1）、引入依赖
 *           redis依赖+cache依赖
*           <dependency>
 *             <groupId>org.springframework.boot</groupId>
 *             <artifactId>spring-boot-starter-data-redis</artifactId>
 *             <exclusions>
 *                 <exclusion>
 *                     <groupId>io.lettuce</groupId>
 *                     <artifactId>lettuce-core</artifactId>
 *                 </exclusion>
 *             </exclusions>
 *         </dependency>
 *             <dependency>
 *             <groupId>org.springframework.boot</groupId>
 *             <artifactId>spring-boot-starter-cache</artifactId>
 *         </dependency>
 *     2）、写配置(自动配置类：CacheAutoConfiguration)
 *          CacheAutoConfiguration会导入RedisCacheConfiguration；
 *          自动配置了缓存管理器 RedisCacheManager；
 *          手动配置使用redis作为缓存：
 *          spring.cache.type=redis
 *          #spring.cache.cache-names=qq,毫秒为单位
 *          spring.cache.redis.time-to-live=3600000
 *           #如果指定了前缀就用我们指定的前缀，如果没有就默认使用缓存的名字作为前缀
 *          #spring.cache.redis.key-prefix=CACHE_
 *          spring.cache.redis.use-key-prefix=true
 *          #是否缓存空值，防止缓存穿透
 *          spring.cache.redis.cache-null-values=true
 *     3）、测试通过注解使用缓存
 *          @Cacheable ： 触发将数据保存到缓存的操作
 *          @CacheEvict ： 触发将数据从缓存删除的操作
 *          @CachePut ：不影响方法执行更新缓存
 *          @Caching ： 组合以上多个操作
 *          @CacheConfig ： 在类级别共享缓存的相同配置
 *          1） @EnableCaching 开启缓存功能
 *          2) 只需要使用注解就能完成缓存操作
 *      4）、原理
 *          CacheAutoConfiguration-》RedisCacheConfiguration
 *          -》初始化所有的缓存，每个缓存决定使用什么配置
 *          -》如果RedisCacheConfiguration已经有就用现有的，否则使用默认的
 *          -》想更改缓存配置，只需要给容器放入自定义的RedisCacheConfiguration即可
 *          -》就会应用到当前RedisCacheManager管理的所有的缓存分区中
 *
 *
 *
 *
 *
 */
@SpringBootApplication
@MapperScan("com.yang.gulimall.product.dao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.yang.gulimall.product.feign")
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
