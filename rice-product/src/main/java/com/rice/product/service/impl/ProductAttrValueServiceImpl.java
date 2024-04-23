package com.rice.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rice.product.service.AttrService;
import com.rice.product.vo.BaseAttrs;
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

import com.rice.product.dao.ProductAttrValueDao;
import com.rice.product.entity.ProductAttrValueEntity;
import com.rice.product.service.ProductAttrValueService;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService
{

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveBaseAttr(Long spuId, List<BaseAttrs> baseAttrs)
    {

        List<ProductAttrValueEntity> collect = baseAttrs.stream()
                .map(b ->
                {
                    ProductAttrValueEntity entity = new ProductAttrValueEntity();

                    entity.setAttrId(b.getAttrId());
                    entity.setAttrName(attrService.getById(b.getAttrId()).getAttrName());
                    entity.setAttrValue(b.getAttrValues());
                    entity.setQuickShow(b.getShowDesc());
                    entity.setSpuId(spuId);

                    return entity;
                })
                .collect(Collectors.toList());

        this.saveBatch(collect);

    }

    @Override
    public List<ProductAttrValueEntity> baseAttrlistforspu(Long spuId)
    {
        LambdaQueryWrapper<ProductAttrValueEntity> lqw = new LambdaQueryWrapper<>();

        lqw.eq(ProductAttrValueEntity::getSpuId, spuId);

        return this.list(lqw);
    }

    @Override
    public void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> productAttrList)
    {
        LambdaQueryWrapper<ProductAttrValueEntity> lqw = new LambdaQueryWrapper<>();

        lqw.eq(ProductAttrValueEntity::getSpuId, spuId);

        remove(lqw);

        List<ProductAttrValueEntity> collect = productAttrList.stream()
                .peek(p -> p.setSpuId(spuId))
                .collect(Collectors.toList());

        saveBatch(collect);
    }


}