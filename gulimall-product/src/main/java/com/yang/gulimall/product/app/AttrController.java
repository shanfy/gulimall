package com.yang.gulimall.product.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.common.constant.AttrEnum;
import com.yang.common.utils.PageUtils;
import com.yang.common.utils.R;
import com.yang.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.yang.gulimall.product.entity.AttrEntity;
import com.yang.gulimall.product.entity.ProductAttrValueEntity;
import com.yang.gulimall.product.service.AttrAttrgroupRelationService;
import com.yang.gulimall.product.service.AttrService;
import com.yang.gulimall.product.service.ProductAttrValueService;
import com.yang.gulimall.product.vo.AttrRespVo;
import com.yang.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 商品属性
 *
 * @author shanfy
 * @email 815481278@qq.com
 * @date 2022-03-26 17:59:21
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    ///product/attr/info/{attrId}

    // /product/attr/base/listforspu/{spuId}
    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrlistforspu(@PathVariable("spuId") Long spuId){

        List<ProductAttrValueEntity> entities = productAttrValueService.baseAttrlistforspu(spuId);

        return R.ok().put("data",entities);
    }

    //product/attr/sale/list/0?
    ///product/attr/base/list/{catelogId}
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId,
                          @PathVariable("attrType")String type){

        PageUtils page = attrService.queryBaseAttrPage(params,catelogId,type);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 基本属性（spu规格参数）列表
     */
    @RequestMapping("/base/list/{catelogId}")
    public R baseList(@RequestParam Map<String, Object> params,
                      @PathVariable("catelogId") Long catelogId){
        // PageUtils page = attrService.queryPage(params);
        PageUtils page = attrService.queryPage(params,catelogId,0);
        return R.ok().put("page", page);
    }

    /**
     * 销售属性（sku可选的属性）列表
     */
    @RequestMapping("/sale/list/{catelogId}")
    public R saleList(@RequestParam Map<String, Object> params,
                      @PathVariable("catelogId") Long catelogId){
        // PageUtils page = attrService.queryPage(params);
        PageUtils page = attrService.queryPage(params,catelogId,1);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
		// AttrEntity attr = attrService.getById(attrId);
        AttrRespVo vo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", vo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attrVo){
        AttrEntity entity = new AttrEntity();
        BeanUtils.copyProperties(attrVo,entity);
        //1、保存属性实体
		attrService.save(entity);
		//销售属性不需要保存属性分组
		if(attrVo.getAttrType()== AttrEnum.ATTR_TYPE_BASE.getCode()){
            //2、保存属性和属性分组关联实体
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(entity.getAttrId());
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationService.save(relationEntity);
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attrVo){
  /*      AttrEntity oldEntity = attrService.getById(attrVo.getAttrId());
        AttrAttrgroupRelationEntity oldRelationEntity =
                attrAttrgroupRelationService.getOne(new QueryWrapper<>().eq(
                        "a"))*/
        //更新也一样，先更新属性实体，再更新关联表，这里选择，先删再重新插入
        AttrEntity entity = new AttrEntity();
        BeanUtils.copyProperties(attrVo,entity);
        //更新属性主体
        attrService.updateById(entity);

        //先删除原先属性的关联分组
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id",attrVo.getAttrId());
        attrAttrgroupRelationService.remove(wrapper);

        //销售属性不需要保存分组  0-基本属性 1-销售属性
        if(attrVo.getAttrType()==0){
            //重新保存属性关联分组关系
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(entity.getAttrId());
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationService.save(relationEntity);
        }
         return R.ok();
    }

    ///product/attr/update/{spuId}
    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId,
                           @RequestBody List<ProductAttrValueEntity> entities){

        productAttrValueService.updateSpuAttr(spuId,entities);

        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
        //刪除属性主体
		attrService.removeByIds(Arrays.asList(attrIds));
        //删除原先属性的关联分组
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.in("attr_id",attrIds);
        attrAttrgroupRelationService.remove(wrapper);
        return R.ok();
    }

}
