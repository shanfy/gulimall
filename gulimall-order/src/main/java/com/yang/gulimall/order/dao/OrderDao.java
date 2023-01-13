package com.yang.gulimall.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.gulimall.order.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 * 
 * @author shanfy
 * @email 815481278@qq.com
 * @date 2022-03-26 20:08:41
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    /**
     * 修改订单状态
     * @param orderSn
     * @param code
     * @param payType
     */
    void updateOrderStatus(@Param("orderSn") String orderSn,
                           @Param("code") Integer code,
                           @Param("payType") Integer payType);
}
