package com.yang.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.common.constant.AttrEnum;
import com.yang.common.constant.ProductConstant;
import com.yang.common.utils.PageUtils;
import com.yang.common.utils.Query;
import com.yang.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.yang.gulimall.product.dao.AttrDao;
import com.yang.gulimall.product.dao.AttrGroupDao;
import com.yang.gulimall.product.dao.CategoryDao;
import com.yang.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.yang.gulimall.product.entity.AttrEntity;
import com.yang.gulimall.product.entity.AttrGroupEntity;
import com.yang.gulimall.product.entity.CategoryEntity;
import com.yang.gulimall.product.service.AttrAttrgroupRelationService;
import com.yang.gulimall.product.service.AttrGroupService;
import com.yang.gulimall.product.service.AttrService;
import com.yang.gulimall.product.service.CategoryService;
import com.yang.gulimall.product.vo.AttrGroupRelationVo;
import com.yang.gulimall.product.vo.AttrRespVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * type表示查询的是基本属性还是销售属性 0-基本属性 1-销售属性
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId,
                               int type) {
        Object key = params.get("key");
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        if(ObjectUtils.isNotEmpty(key)){
            wrapper.likeRight("attr_name",key);
        }
        if(catelogId!=0){
            // 物品分类为0时，默认查出所有的属性,不为0时，才加入查询
            wrapper.eq("catelog_id",catelogId);

        }
        wrapper.eq("attr_type",type);
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),wrapper);
        //属性和属性分组确定唯一值，暂时这么定吧
        List<AttrRespVo> respVos =  page.getRecords().stream().map(entity->{
            AttrRespVo vo = new AttrRespVo();
            BeanUtils.copyProperties(entity,vo);
            //0-基本属性才有分组，此时才进行分组名称渲染
            if(type== AttrEnum.ATTR_TYPE_BASE.getCode()){
                AttrAttrgroupRelationEntity relationEntity= attrAttrgroupRelationService
                        .getOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                                .eq("attr_id", entity.getAttrId()));
                if(relationEntity!=null){
                    AttrGroupEntity groupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
                    vo.setGroupName(groupEntity.getAttrGroupName());
                }
            }

            CategoryEntity categoryEntity = categoryService.getById(entity.getCatelogId());
            if(categoryEntity!=null){
                vo.setCatelogName(categoryEntity.getName());
            }
            return vo;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(respVos);
        return pageUtils;
    }

    /**
     * 获取属性的详情信息
     * @param attrId
     * @return
     */
    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrEntity entity = this.getById(attrId);
        AttrRespVo vo = new AttrRespVo();
        BeanUtils.copyProperties(entity,vo);

        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id",entity.getAttrId());
        AttrAttrgroupRelationEntity relationEntity =
                attrAttrgroupRelationService.getOne(wrapper);
        if(relationEntity!=null){
            vo.setAttrGroupId(relationEntity.getAttrGroupId());
        }
        // 回显分类所需的数据格式
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(entity.getCatelogId(), paths);
        Collections.reverse(parentPath);
        vo.setCatelogPath(parentPath.toArray(new Long[0]));
        return vo;
    }

    /**
     * 根据分组id查找关联的所有基本属性
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {

        List<AttrAttrgroupRelationEntity> entities =
                attrAttrgroupRelationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));

        List<Long> attrIds = entities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());

        if(attrIds.isEmpty()){
            return Collections.emptyList();
        }
        return this.listByIds(attrIds);
    }

    /**
     * 获取当前分组没有关联的所有基本属性
     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //1、当前分组只能关联自己所属的分类里面的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //2、当前分组只能关联别的分组没有引用的属性
        //2.1)、当前分类下的其他分组
        List<AttrGroupEntity> group =
                attrGroupService.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> collect = group.stream().map(item -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());

        //2.2)、这些分组关联的属性
        List<AttrAttrgroupRelationEntity> groupId = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));
        List<Long> attrIds = groupId.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        //2.3)、从当前分类的所有属性中移除这些属性；
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type",AttrEnum.ATTR_TYPE_BASE.getCode());
        if(attrIds!=null && attrIds.size()>0){
            wrapper.notIn("attr_id", attrIds);
        }
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);

        PageUtils pageUtils = new PageUtils(page);

        return pageUtils;
    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        //relationDao.delete(new QueryWrapper<>().eq("attr_id",1L).eq("attr_group_id",1L));
        //
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        relationDao.deleteBatchRelation(entities);
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type","base".equalsIgnoreCase(type)? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        if(catelogId != 0){
            queryWrapper.eq("catelog_id",catelogId);
        }

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            //attr_id  attr_name
            queryWrapper.and((wrapper)->{
                wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respVos = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            //1、设置分类和分组的名字
            if("base".equalsIgnoreCase(type)){
                AttrAttrgroupRelationEntity attrId = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attrId != null && attrId.getAttrGroupId()!=null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrId.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }

            }


            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respVos);
        return pageUtils;
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //1、收集当前节点idag
        paths.add(catelogId);
        CategoryEntity byId = categoryService.getById(catelogId);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }

}