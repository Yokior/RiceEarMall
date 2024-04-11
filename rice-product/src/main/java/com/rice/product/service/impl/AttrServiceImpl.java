package com.rice.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rice.common.constant.ProductConstant;
import com.rice.product.dao.AttrAttrgroupRelationDao;
import com.rice.product.dao.AttrGroupDao;
import com.rice.product.dao.CategoryDao;
import com.rice.product.entity.AttrAttrgroupRelationEntity;
import com.rice.product.entity.AttrGroupEntity;
import com.rice.product.entity.CategoryEntity;
import com.rice.product.service.CategoryService;
import com.rice.product.vo.AttrGroupRelationVo;
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

    @Autowired
    private CategoryService categoryService;

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
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long categoryId, String attrType)
    {
        LambdaQueryWrapper<AttrEntity> lqw = new LambdaQueryWrapper<>();

        if (categoryId != 0)
        {
            lqw.eq(AttrEntity::getCatelogId, categoryId);
            lqw.eq(AttrEntity::getAttrType, "base".equalsIgnoreCase(attrType) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
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

                    if ("base".equalsIgnoreCase(attrType))
                    {
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
        if (attrVo.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())
        {
            AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
            relation.setAttrGroupId(attrVo.getAttrGroupId());
            relation.setAttrId(attrVo.getAttrId());
            relationDao.insert(relation);
        }
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId)
    {
        AttrRespVo respVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, respVo);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())
        {
            // 设置分组信息
            LambdaQueryWrapper<AttrAttrgroupRelationEntity> lqw = new LambdaQueryWrapper<>();
            lqw.eq(AttrAttrgroupRelationEntity::getAttrId, attrId);
            AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(lqw);
            if (relationEntity != null)
            {
                respVo.setAttrGroupId(relationEntity.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                if (attrGroupEntity != null)
                {
                    respVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }

        // 设置分类信息
        Long catelogId = attrEntity.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);
        respVo.setCatelogPath(path);

        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity != null)
        {
            respVo.setCatelogName(categoryEntity.getName());
        }


        return respVo;
    }

    @Override
    public void updateAttr(AttrVo attrVo)
    {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.updateById(attrEntity);

        if (attrVo.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())
        {
            // 修改分组关联
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();

            LambdaQueryWrapper<AttrAttrgroupRelationEntity> qLqw = new LambdaQueryWrapper<>();
            qLqw.eq(AttrAttrgroupRelationEntity::getAttrId, attrVo.getAttrId());
            Integer count = relationDao.selectCount(qLqw);
            relationEntity.setAttrId(attrVo.getAttrId());
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());

            if (count > 0)
            {
                // 有数据 进行修改操作
                LambdaUpdateWrapper<AttrAttrgroupRelationEntity> lqw = new LambdaUpdateWrapper<>();
                lqw.eq(AttrAttrgroupRelationEntity::getAttrId, attrVo.getAttrId());
                relationDao.update(relationEntity, lqw);
            }
            else
            {
                // 没有数据 进行添加操作
                relationDao.insert(relationEntity);
            }
        }


    }

    /**
     * 根据分组id获取关联的所有基本属性
     *
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId)
    {
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrgroupId);

        List<AttrAttrgroupRelationEntity> relationList = relationDao.selectList(lqw);

        // 获取所有attrId
        List<Long> attrIdList = relationList.stream()
                .map(AttrAttrgroupRelationEntity::getAttrId)
                .collect(Collectors.toList());

        if (attrIdList.isEmpty())
        {
            return null;
        }

        // 根据id查询
        List<AttrEntity> attrEntityList = this.listByIds(attrIdList);

        return attrEntityList;
    }

    @Override
    public void deleteRelation(List<AttrGroupRelationVo> relationVoList)
    {
        List<AttrAttrgroupRelationEntity> relationList = relationVoList.stream()
                .map(re ->
                {
                    AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
                    relation.setAttrId(re.getAttrId());
                    relation.setAttrGroupId(re.getAttrGroupId());
                    return relation;
                })
                .collect(Collectors.toList());
        relationDao.deleteBatchRelation(relationList);
    }

    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId)
    {
        //  1. 当前分组只能关联自己所属的分类里面的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

        //  2. 当前分组只能关联别的分组没有引用的属性(包括去除自己的属性)
        List<AttrGroupEntity> group = attrGroupDao.selectList(new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getCatelogId, catelogId));

        // 当前分类下的其他分组的id集合
        List<Long> collect = group.stream()
                .map(AttrGroupEntity::getAttrGroupId)
                .collect(Collectors.toList());

        // 这些分组关联的属性
        List<AttrAttrgroupRelationEntity> relationList = relationDao.selectList(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().in(AttrAttrgroupRelationEntity::getAttrGroupId, collect));
        List<Long> attrIdList = relationList.stream()
                .map(AttrAttrgroupRelationEntity::getAttrId)
                .collect(Collectors.toList());

        // 去除这些属性
        LambdaQueryWrapper<AttrEntity> lqw = new LambdaQueryWrapper<AttrEntity>().eq(AttrEntity::getCatelogId, catelogId).eq(AttrEntity::getAttrType, ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());

        if (!attrIdList.isEmpty())
        {
            lqw.notIn(AttrEntity::getAttrId, attrIdList);
        }

        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key))
        {
            lqw.and(t -> t.eq(AttrEntity::getAttrId, key).or().like(AttrEntity::getAttrName, key));
        }

        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), lqw);

        PageUtils pageUtils = new PageUtils(page);

        return pageUtils;
    }

}