package com.rice.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rice.product.dao.BrandDao;
import com.rice.product.entity.BrandEntity;
import com.rice.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.Query;

import com.rice.product.dao.CategoryBrandRelationDao;
import com.rice.product.entity.CategoryBrandRelationEntity;
import com.rice.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService
{

    @Autowired
    private BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<BrandEntity> getBrandByCatId(Long catId)
    {
        // 查询关系表数据
        LambdaQueryWrapper<CategoryBrandRelationEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CategoryBrandRelationEntity::getCatelogId, catId);
        List<CategoryBrandRelationEntity> relationEntityList = this.list(lqw);

        // 找到Brand实体类的ids
        List<Long> brandIdList = relationEntityList.stream()
                .map(CategoryBrandRelationEntity::getBrandId)
                .collect(Collectors.toList());

        List<BrandEntity> brandEntityList = brandService.listByIds(brandIdList);

        return brandEntityList;
    }

}