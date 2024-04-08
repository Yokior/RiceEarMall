package com.rice.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.Query;

import com.rice.product.dao.BrandDao;
import com.rice.product.entity.BrandEntity;
import com.rice.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService
{

    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {

        // 获取key
        String key = (String) params.get("key");

        LambdaQueryWrapper<BrandEntity> lqw = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(key))
        {
            lqw.eq(BrandEntity::getBrandId, key).or().like(BrandEntity::getName, key);
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                lqw
        );

        return new PageUtils(page);
    }

}