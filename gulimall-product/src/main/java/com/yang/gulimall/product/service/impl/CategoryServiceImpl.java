package com.yang.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.common.utils.PageUtils;
import com.yang.common.utils.Query;
import com.yang.gulimall.product.dao.CategoryDao;
import com.yang.gulimall.product.entity.CategoryEntity;
import com.yang.gulimall.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1、查询出所有的分类数据
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2、组装成父子的树形结构
        // 2.1找到所有的一级分类
        List<CategoryEntity> level1List =
                entities.stream().filter(entry -> entry.getParentCid() == 0).map(entry -> {
            entry.setChildren(getChildren(entry,entities));
            return entry;
        }).sorted(Comparator.comparingInt(m -> (m.getSort() != null ? m.getSort() : 0)))
                        .collect(Collectors.toList());

        return level1List;
    }

    @Override
    public int removeMenuByIds(Long[] catIds) {
        return baseMapper.deleteBatchIds(Arrays.asList(catIds));
    }

    /**
     * 格式如 [2,34,255]
     * @param catelogId
     * @return
     */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        //将父节点放在最前面
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //1、收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }

    /**
     * 递归获取指定菜单下的所有子菜单
     * @param root
     * @param categoryEntities
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root,
                                             List<CategoryEntity> categoryEntities){
        List<CategoryEntity> children =
                categoryEntities.stream()
                        .filter(entry -> entry.getParentCid().equals(root.getCatId()))
                        .map(entry->{
                            //1、递归查找子菜单
                            entry.setChildren(getChildren(entry,categoryEntities));
                            return entry;
                        }).sorted(Comparator.comparingInt(m -> (m.getSort() != null ? m.getSort() : 0)))
                        .collect(Collectors.toList());
        return children;
    }
}