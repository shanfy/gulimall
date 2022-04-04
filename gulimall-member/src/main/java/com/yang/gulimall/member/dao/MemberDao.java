package com.yang.gulimall.member.dao;

import com.yang.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author shanfy
 * @email 815481278@qq.com
 * @date 2022-03-26 20:06:12
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
