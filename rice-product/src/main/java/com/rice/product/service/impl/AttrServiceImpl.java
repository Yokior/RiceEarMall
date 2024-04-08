package com.rice.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rice.product.dao.AttrAttrgroupRelationDao;
import com.rice.product.dao.AttrGroupDao;
import com.rice.product.dao.CategoryDao;
import com.rice.product.entity.AttrAttrgroupRelationEntity;
import com.rice.product.entity.AttrGroupEntity;
import com.rice.product.entity.CategoryEntity;
import com.rice.product.vo.AttrRespVo;
import com.rice.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
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

import com.rice.product.dao.AttrDao;
import com.rice.product.entity.AttrEntity;
import com.rice.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService
{

    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long categoryId)
    {
        LambdaQueryWrapper<AttrEntity> lqw = new LambdaQueryWrapper<>();

        if (categoryId != 0)
        {
            lqw.eq(AttrEntity::getCatelogId, categoryId);
        }

        String key = (String) params.get("key");

        if (StringUtils.isNotBlank(key))
        {
            lqw.and(t -> t.eq(AttrEntity::getAttrId, key).or().like(AttrEntity::getAttrName, key));
        }

        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), lqw);

        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();

        List<AttrRespVo> attrRespVoList = records.stream()
                .map(attrEntity ->
                {
                    AttrRespVo attrRespVo = new AttrRespVo();
                    BeanUtils.copyProperties(attrEntity, attrRespVo);

                    //设置分类和分类名称
                    LambdaQueryWrapper<AttrAttrgroupRelationEntity> lqw2 = new LambdaQueryWrapper<>();
                    lqw2.eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId());
                    AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(lqw2);
                    if (relationEntity != null)
                    {
                        Long attrGroupId = relationEntity.getAttrGroupId();
                        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
                        attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                    }

                    CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
                    if (categoryEntity != null)
                    {
                        attrRespVo.setCatelogName(categoryEntity.getName());
                    }
                    return attrRespVo;
                })
                .collect(Collectors.toList());

        pageUtils.setList(attrRespVoList);

        return pageUtils;
    }

    @Override
    @Transactional
    public void saveAttr(AttrVo attrVo)
    {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);

        this.save(attrEntity);

        // 保存关联关系
        AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
        relation.setAttrGroupId(attrVo.getAttrGroupId());
        relation.setAttrId(attrVo.getAttrId());
        relationDao.insert(relation);
    }

}