package com.rice.coupon.service.impl;

import com.rice.common.to.MemberPrice;
import com.rice.common.to.SkuReductionTo;
import com.rice.coupon.entity.MemberPriceEntity;
import com.rice.coupon.entity.SkuLadderEntity;
import com.rice.coupon.service.MemberPriceService;
import com.rice.coupon.service.SkuLadderService;
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

import com.rice.coupon.dao.SkuFullReductionDao;
import com.rice.coupon.entity.SkuFullReductionEntity;
import com.rice.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService
{

    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo)
    {
        Long skuId = skuReductionTo.getSkuId();

        // 保存阶梯价格
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuId);
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());

        skuLadderService.save(skuLadderEntity);

        // 保存满减信息
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        this.save(skuFullReductionEntity);


        // 会员价格信息
        List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();

        List<MemberPriceEntity> memberPriceList = memberPrice.stream()
                .map(m ->
                {
                    MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                    memberPriceEntity.setSkuId(skuId);
                    memberPriceEntity.setMemberLevelId(m.getId());
                    memberPriceEntity.setMemberLevelName(m.getName());
                    memberPriceEntity.setMemberPrice(m.getPrice());
                    memberPriceEntity.setAddOther(1);

                    return memberPriceEntity;
                })
                .collect(Collectors.toList());

        memberPriceService.saveBatch(memberPriceList);

    }

}