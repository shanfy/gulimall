package com.yang.gulimall.order.feign;

import com.yang.gulimall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Description:
 **/

@FeignClient("gulimall-cart")
public interface CartFeignService {

    /**
     * 查询当前用户购物车选中的商品项
     * @return
     */
    @GetMapping(value = "/currentUserCartItems")
    List<OrderItemVo> getCurrentCartItems();

}
