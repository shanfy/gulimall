package com.yang.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.common.utils.PageUtils;
import com.yang.gulimall.product.entity.AttrEntity;
import com.yang.gulimall.product.vo.AttrGroupRelationVo;
import com.yang.gulimall.product.vo.AttrRespVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author shanfy
 * @email 815481278@qq.com
 * @date 2022-03-26 17:59:21
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId,int type);

    AttrRespVo getAttrInfo(Long attrId);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    List<Long> selectSearchAttrs(List<Long> attrIds);
}

