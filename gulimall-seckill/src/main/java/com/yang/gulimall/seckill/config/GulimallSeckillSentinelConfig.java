package com.yang.gulimall.seckill.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.yang.common.exception.BizCodeEnume;
import com.yang.common.utils.R;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 自定义阻塞返回方法
 **/
@Configuration
public class GulimallSeckillSentinelConfig {

    /*public GulimallSeckillSentinelConfig() {

        WebCallbackManager.setUrlBlockHandler(new UrlBlockHandler() {
            @Override
            public void blocked(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws IOException {
                R error = R.error(BizCodeEnume.TO_MANY_REQUEST.getCode(),
                        BizCodeEnume.TO_MANY_REQUEST.getMsg());
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().write(JSON.toJSONString(error));

            }
        });

    }*/
    @Bean(name = "seckillSentinelExceptionHandler")
    public BlockExceptionHandler customSentinelExceptionHandler(){
        return new BlockExceptionHandler() {
            @Override
            public void handle(HttpServletRequest request,
                               HttpServletResponse response, BlockException e) throws Exception {
                R error = R.error(BizCodeEnume.TO_MANY_REQUEST.getCode(),
                        BizCodeEnume.TO_MANY_REQUEST.getMsg());
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().write(JSON.toJSONString(error));
            }
        };
    }

}
