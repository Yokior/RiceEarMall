package com.rice.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rice.common.utils.R;
import com.rice.ware.feign.ProductFeignService;
import com.rice.ware.vo.SkuHasStockVo;
import org.apache.commons.lang.StringUtils;
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

import com.rice.ware.dao.WareSkuDao;
import com.rice.ware.entity.WareSkuEntity;
import com.rice.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService
{

    @Autowired
    private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {

        LambdaQueryWrapper<WareSkuEntity> lqw = new LambdaQueryWrapper<>();

        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId))
        {
            lqw.eq(WareSkuEntity::getSkuId, skuId);
        }

        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId))
        {
            lqw.eq(WareSkuEntity::getWareId, wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                lqw
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum)
    {
        // 判断是否有库存 没有就新建
        LambdaQueryWrapper<WareSkuEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(WareSkuEntity::getSkuId, skuId);
        lqw.eq(WareSkuEntity::getWareId, wareId);
        List<WareSkuEntity> wareSkuEntityList = this.list(lqw);

        if (wareSkuEntityList == null || wareSkuEntityList.size() == 0)
        {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            // 远程查询sku名字
            try
            {
                R info = productFeignService.info(skuId);
                if (info.getCode() == 0)
                {
                    Map<String, Object> data = (Map<String, Object>) info.get("skuInfo");
                    wareSkuEntity.setSkuName((String) data.get("skuName"));
                }
            }
            catch (Exception e)
            {
                // 有异常 不回滚
            }

            save(wareSkuEntity);
        }
        else
        {
            this.baseMapper.addStock(skuId, wareId, skuNum);
        }

    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds)
    {

        List<SkuHasStockVo> skuHasStockVoList = skuIds.stream()
                .map(item ->
                {
                    SkuHasStockVo skuHasStockVo = new SkuHasStockVo();

                    Long count = this.baseMapper.getSkuStock(item);
                    skuHasStockVo.setSkuId(item);
                    skuHasStockVo.setHasStock(count > 0);

                    return skuHasStockVo;
                })
                .collect(Collectors.toList());

        return skuHasStockVoList;
    }

}