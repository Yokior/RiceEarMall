package com.rice.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rice.product.dao.AttrAttrgroupRelationDao;
import com.rice.product.entity.AttrAttrgroupRelationEntity;
import com.rice.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.Query;

import com.rice.product.dao.AttrGroupDao;
import com.rice.product.entity.AttrGroupEntity;
import com.rice.product.service.AttrGroupService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService
{



    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageInfo(Map<String, Object> params, Long categoryId)
    {
        LambdaQueryWrapper<AttrGroupEntity> lqw = new LambdaQueryWrapper<>();
        if (categoryId == 0)
        {
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    lqw);
            return new PageUtils(page);
        }
        else
        {
            String key = (String) params.get("key");
            lqw.eq(AttrGroupEntity::getCatelogId, categoryId);
            if (StringUtils.isNotBlank(key))
            {
                lqw.and(t -> t.like(AttrGroupEntity::getAttrGroupName, key).or().like(AttrGroupEntity::getAttrGroupId, key));
            }
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), lqw);

            return new PageUtils(page);
        }

    }



}