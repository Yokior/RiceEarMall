package com.rice.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.rice.product.entity.BrandEntity;
import com.rice.product.vo.BrandVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rice.product.entity.CategoryBrandRelationEntity;
import com.rice.product.service.CategoryBrandRelationService;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.R;


/**
 * 品牌分类关联
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:10:00
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController
{
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;


    @GetMapping("/brands/list")
    public R relationBrandsList(@RequestParam("catId") Long catId)
    {
        List<BrandEntity> brandEntityList = categoryBrandRelationService.getBrandByCatId(catId);

        List<BrandVo> brandVoList = brandEntityList.stream()
                .map(b ->
                {
                    BrandVo brandVo = new BrandVo();
                    brandVo.setBrandId(b.getBrandId());
                    brandVo.setBrandName(b.getName());
                    return brandVo;
                })
                .collect(Collectors.toList());

        return R.ok().put("data", brandVoList);

    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params)
    {
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id)
    {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation)
    {
        categoryBrandRelationService.save(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation)
    {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids)
    {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
