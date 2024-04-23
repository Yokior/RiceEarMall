package com.rice.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.rice.product.entity.ProductAttrValueEntity;
import com.rice.product.service.ProductAttrValueService;
import com.rice.product.vo.AttrRespVo;
import com.rice.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rice.product.entity.AttrEntity;
import com.rice.product.service.AttrService;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.R;


/**
 * 商品属性
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:10:00
 */
@RestController
@RequestMapping("product/attr")
public class AttrController
{
    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrList(@PathVariable("spuId") Long spuId)
    {
        List<ProductAttrValueEntity> productAttrValueEntityList = productAttrValueService.baseAttrlistforspu(spuId);
        return R.ok().put("data", productAttrValueEntityList);
    }


    @GetMapping("/{attrType}/list/{categoryId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("categoryId") Long categoryId,
                          @PathVariable("attrType") String attrType
    )
    {
        PageUtils page = attrService.queryBaseAttrPage(params, categoryId, attrType);
        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params)
    {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId)
    {
//        AttrEntity attr = attrService.getById(attrId);

        AttrRespVo respVo = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", respVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attrVo)
    {
//        attrService.save(attr);

        attrService.saveAttr(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId, @RequestBody List<ProductAttrValueEntity> productAttrList)
    {
        productAttrValueService.updateSpuAttr(spuId, productAttrList);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attrVo)
    {
        attrService.updateAttr(attrVo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds)
    {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
