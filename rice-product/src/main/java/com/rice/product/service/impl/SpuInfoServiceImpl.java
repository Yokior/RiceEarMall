package com.rice.product.service.impl;

import com.rice.common.to.SkuReductionTo;
import com.rice.common.to.SpuBoundTo;
import com.rice.common.utils.R;
import com.rice.product.entity.*;
import com.rice.product.feign.CouponFeignService;
import com.rice.product.service.*;
import com.rice.product.vo.*;
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

import com.rice.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService
{

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignService couponFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo)
    {
        // 保存spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, spuInfoEntity);
        this.saveBaseSpuInfo(spuInfoEntity);


        // 保存spu的描述图片 pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        Long spuId = spuInfoEntity.getId();
        spuInfoDescEntity.setSpuId(spuId);
        spuInfoDescEntity.setDecript(String.join(",", decript));
        spuInfoDescService.save(spuInfoDescEntity);


        // 保存spu的图片集 pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(spuId, images);

        // 保存spu的规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        productAttrValueService.saveBaseAttr(spuId, baseAttrs);


        // 保存spu的积分信息 pms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(spuId);
        R r1 = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r1.getCode() != 0)
        {
            log.error("远程保存spu积分信息失败");
        }


        // 保存spu的对应的sku信息

        // -保存sku的基本信息 pms_sku_info
        List<Skus> skus = vo.getSkus();
        if (skus != null && skus.size() > 0)
        {
            skus.forEach(sku -> {
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);

                String defalutImg = "";
                for (Images image : sku.getImages())
                {
                    if (image.getDefaultImg() == 1)
                    {
                        defalutImg = image.getImgUrl();
                    }
                }

                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuId);
                skuInfoEntity.setSkuDefaultImg(defalutImg);

                skuInfoService.save(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();

                // -保存sku的图片信息 pms_sku_images
                List<SkuImagesEntity> skuImagesList = sku.getImages().stream()
                        .map(img ->
                        {
                            SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                            skuImagesEntity.setSkuId(skuId);
                            skuImagesEntity.setImgUrl(img.getImgUrl());
                            skuImagesEntity.setDefaultImg(img.getDefaultImg());
                            // 判断是否是默认图片 是就设置
                            return skuImagesEntity;
                        })
                        .collect(Collectors.toList());

                skuImagesService.saveBatch(skuImagesList);

                // -保存sku的销售属性 pms_sku_sale_attr_value
                List<Attr> attr = sku.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueList = attr.stream()
                        .map(a ->
                        {
                            SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                            BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                            skuSaleAttrValueEntity.setSkuId(skuId);

                            return skuSaleAttrValueEntity;
                        })
                        .collect(Collectors.toList());

                skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);

                // -保存sku的优惠信息
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(sku, skuReductionTo);
                skuReductionTo.setSkuId(skuId);

                R r2 = couponFeignService.saveSkuReduction(skuReductionTo);
                if (r2.getCode() != 0)
                {
                    log.error("远程保存sku的优惠信息失败");
                }
            });
        }



    }


    public void saveBaseSpuInfo(SpuInfoEntity infoEntity)
    {
        this.save(infoEntity);
    }

}