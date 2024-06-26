package com.rice.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rice.common.constant.ProductConstant;
import com.rice.common.to.SkuReductionTo;
import com.rice.common.to.SpuBoundTo;
import com.rice.common.to.es.SkuEsModel;
import com.rice.common.utils.R;
import com.rice.product.entity.*;
import com.rice.product.feign.CouponFeignService;
import com.rice.product.feign.SearchFeignService;
import com.rice.product.feign.WareFeignService;
import com.rice.product.service.*;
import com.rice.product.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    private SearchFeignService searchFeignService;


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
        spuInfoEntity.setCreateTime(LocalDateTime.now());
        spuInfoEntity.setUpdateTime(LocalDateTime.now());
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
            skus.forEach(sku ->
            {
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
                        .filter(entity ->
                        {
                            // 没有图片路径的无需保存
                            return StringUtils.isNotBlank(entity.getImgUrl());
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

                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1)
                {
                    R r2 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r2.getCode() != 0)
                    {
                        log.error("远程保存sku的优惠信息失败");
                    }
                }

            });
        }


    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params)
    {

        LambdaQueryWrapper<SpuInfoEntity> lqw = new LambdaQueryWrapper<>();

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key))
        {
            lqw.and((wq) -> wq.eq(SpuInfoEntity::getId, key).or().like(SpuInfoEntity::getSpuName, key));
        }

        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status))
        {
            lqw.eq(SpuInfoEntity::getPublishStatus, status);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equals(brandId))
        {
            lqw.eq(SpuInfoEntity::getBrandId, brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equals(catelogId))
        {
            lqw.eq(SpuInfoEntity::getCatalogId, catelogId);
        }


        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                lqw
        );
        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId)
    {
        List<SkuInfoEntity> skuInfoEntityList = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIds = skuInfoEntityList.stream()
                .map(SkuInfoEntity::getSkuId)
                .collect(Collectors.toList());

        // 查询当前sku所有可以被用来检索的规格属性
        List<ProductAttrValueEntity> baseAttrs = productAttrValueService.baseAttrlistforspu(spuId);
        List<Long> attrIds = baseAttrs.stream()
                .map(ProductAttrValueEntity::getAttrId)
                .collect(Collectors.toList());

        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);
        HashSet<Long> set = new HashSet<>(searchAttrIds);
        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream()
                .filter(item -> set.contains(item.getAttrId()))
                .map(item ->
                {
                    SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
                    BeanUtils.copyProperties(item, attrs);
                    return attrs;
                })
                .collect(Collectors.toList());
        // 发送远程调用 查询库存系统是否有库存
        Map<Long, Boolean> stockMap = null;
        try
        {
            R<List<SkuHasStockVo>> r = wareFeignService.getSkuHasStock(skuIds);
            List<SkuHasStockVo> skuHasStockVoList = r.getData();

            stockMap = skuHasStockVoList.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
        }
        catch (Exception e)
        {
            log.error("库存服务查询异常:原因{}",e);
        }


        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> esModelList = skuInfoEntityList.stream()
                .map(item ->
                {
                    SkuEsModel esModel = new SkuEsModel();
                    BeanUtils.copyProperties(item, esModel);
                    esModel.setSkuPrice(item.getPrice());
                    esModel.setSkuImg(item.getSkuDefaultImg());
                    if (finalStockMap == null)
                    {
                        esModel.setHasStock(true);
                    }
                    else
                    {
                        esModel.setHasStock(finalStockMap.get(item.getSkuId()));
                    }
                    // TODO 热度评分
                    esModel.setHotScore(0L);

                    // 查询品牌和分类的名字信息
                    BrandEntity brandEntity = brandService.getById(item.getBrandId());
                    esModel.setBrandName(brandEntity.getName());
                    esModel.setBrandImg(brandEntity.getLogo());

                    CategoryEntity categoryEntity = categoryService.getById(item.getCatalogId());
                    esModel.setCatalogName(categoryEntity.getName());

                    // 设置检索属性
                    esModel.setAttrs(attrsList);

                    return esModel;
                })
                .collect(Collectors.toList());

        // 将数据发送给ES保存
        R r = searchFeignService.productStatusUp(esModelList);
        if (r.getCode() == 0)
        {
            // 上架成功
            // 更改spu的状态
            SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
            spuInfoEntity.setId(spuId);
            spuInfoEntity.setPublishStatus(ProductConstant.StatusEnum.SPU_UP.getCode());
            spuInfoEntity.setUpdateTime(LocalDateTime.now());
            updateById(spuInfoEntity);
        }
        else
        {
            // 上架失败
            // TODO 重复调用 接口幂等性
        }
    }


    public void saveBaseSpuInfo(SpuInfoEntity infoEntity)
    {
        this.save(infoEntity);
    }

}