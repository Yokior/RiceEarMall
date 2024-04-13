package com.rice.product.service.impl;

import com.rice.product.vo.AttrGroupRelationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.Query;

import com.rice.product.dao.AttrAttrgroupRelationDao;
import com.rice.product.entity.AttrAttrgroupRelationEntity;
import com.rice.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveBatchRelation(List<AttrGroupRelationVo> relationVoList)
    {
        List<AttrAttrgroupRelationEntity> relationEntityList = relationVoList.stream()
                .map(v ->
                {
                    AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
                    BeanUtils.copyProperties(v, relationEntity);
                    return relationEntity;
                })
                .collect(Collectors.toList());

        this.saveBatch(relationEntityList);
    }

}