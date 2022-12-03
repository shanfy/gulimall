package com.yang.gulimall.product.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.common.utils.PageUtils;
import com.yang.common.utils.R;
import com.yang.gulimall.product.entity.BrandEntity;
import com.yang.gulimall.product.entity.CategoryBrandRelationEntity;
import com.yang.gulimall.product.entity.CategoryEntity;
import com.yang.gulimall.product.service.BrandService;
import com.yang.gulimall.product.service.CategoryBrandRelationService;
import com.yang.gulimall.product.service.CategoryService;
import com.yang.gulimall.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 品牌分类关联
 *
 * @author shanfy
 * @email 815481278@qq.com
 * @date 2022-03-26 17:59:21
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;
    /**
     * 获取当前品牌关联的所有分类列表
     */
    @GetMapping("/catelog/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R cateloglist(@RequestParam("brandId")Long brandId){
        List<CategoryBrandRelationEntity> entities =
                categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId)
        );
        //这里只是进行了简单的处理，没有进行判空
        Set<BrandEntity> brandEntitySet = new HashSet<>();
        Set<CategoryEntity> categorySet = new HashSet<>();
        entities.stream().forEach(entity->{
            brandEntitySet.add(brandService.getById(entity.getBrandId()));
            categorySet.add(categoryService.getById(entity.getCatelogId()));
        });
        Map<Long,String>  brandEntityMap =
                brandEntitySet.stream().collect(Collectors.toMap(BrandEntity::getBrandId, BrandEntity::getName));
        Map<Long,String> categoryEntityMap =
                categorySet.stream().collect(Collectors.toMap(CategoryEntity::getCatId,
                       CategoryEntity::getName));
        entities.forEach(entity->{
            entity.setBrandName(brandEntityMap.get(entity.getBrandId())+"");
            entity.setCatelogName(categoryEntityMap.get(entity.getCatelogId())+"");
        });

        return R.ok().put("data", entities);
    }

    /**
     *  /product/categorybrandrelation/brands/list
     *
     *  1、Controller：处理请求，接受和校验数据
     *  2、Service接受controller传来的数据，进行业务处理
     *  3、Controller接受Service处理完的数据，封装页面指定的vo
     */
    @GetMapping("/brands/list")
    public R relationBrandsList(@RequestParam(value = "catId",required = true)Long catId){
        List<BrandEntity> vos = categoryBrandRelationService.getBrandsByCatId(catId);

        List<BrandVo> collect = vos.stream().map(item -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());

            return brandVo;
        }).collect(Collectors.toList());

        return R.ok().put("data",collect);

    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
   //     可以选择冗余减少关联查询，不过目前更多的还是多次单表查询，在业务层进行数据聚合
     /*   BrandEntity entity =
                brandService.getById(categoryBrandRelation.getBrandId());
        categoryBrandRelation.setBrandName(entity.getName());
        CategoryEntity categoryEntity =
                categoryService.getById(categoryBrandRelation.getCatelogId());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());*/
		categoryBrandRelationService.save(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
