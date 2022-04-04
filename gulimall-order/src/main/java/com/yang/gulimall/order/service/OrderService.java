package com.yang.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.common.utils.PageUtils;
import com.yang.gulimall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author shanfy
 * @email 815481278@qq.com
 * @date 2022-03-26 20:08:41
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

