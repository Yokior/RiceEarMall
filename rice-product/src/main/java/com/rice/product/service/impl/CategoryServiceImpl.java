package com.rice.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.Query;

import com.rice.product.dao.CategoryDao;
import com.rice.product.entity.CategoryEntity;
import com.rice.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService
{

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree()
    {
        // 查出所有分类
        List<CategoryEntity> categoryList = categoryDao.selectList(null);


        // 组装成父子树形结构

        // 查询所有的一级分类
        List<CategoryEntity> lv1Category = categoryList.stream()
                .filter(c -> c.getParentCid() == 0)
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity, categoryList)))
                .sorted((m1, m2) -> {
                    return (m1.getSort() == null ? 0 : m1.getSort()) - (m2.getSort() == null ? 0 : m2.getSort());
                })
                .collect(Collectors.toList());

        return lv1Category;
    }

    @Override
    public void removeMenuByIds(List<Long> menuList)
    {
        // TODO 检查当前删除的菜单，是否被别的地方引用

        baseMapper.deleteBatchIds(menuList);
    }


    private List<CategoryEntity> getChildren(CategoryEntity categoryEntity, List<CategoryEntity> categoryList)
    {
        Long catId = categoryEntity.getCatId();

        List<CategoryEntity> childrenList = categoryList.stream()
                // 如果filter后没有数据 自动return 所以不用加结束条件
                .filter(c -> catId.equals(c.getParentCid()))
                // 处理子分类的子分类
                .peek(category -> category.setChildren(getChildren(category, categoryList)))
                .sorted((m1, m2) -> {
                    return (m1.getSort() == null ? 0 : m1.getSort()) - (m2.getSort() == null ? 0 : m2.getSort());
                })
                .collect(Collectors.toList());

        return childrenList;
    }


}