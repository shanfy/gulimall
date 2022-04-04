package com.yang.gulimall.order.dao;

import com.yang.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author shanfy
 * @email 815481278@qq.com
 * @date 2022-03-26 20:08:41
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
