package com.rice.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.rice.product.entity.AttrEntity;
import com.rice.product.service.AttrAttrgroupRelationService;
import com.rice.product.service.AttrService;
import com.rice.product.service.CategoryService;
import com.rice.product.vo.AttrGroupRelationVo;
import com.rice.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rice.product.entity.AttrGroupEntity;
import com.rice.product.service.AttrGroupService;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.R;


/**
 * 属性分组
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:10:00
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController
{
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService relationService;


    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId)
    {
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVoList = attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);

        return R.ok().put("data", attrGroupWithAttrsVoList);
    }



    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> relationVoList)
    {
        relationService.saveBatchRelation(relationVoList);

        return R.ok();
    }


    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody List<AttrGroupRelationVo> relationVoList)
    {
        attrService.deleteRelation(relationVoList);
        return R.ok();
    }


    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId)
    {
        List<AttrEntity> attrList = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data", attrList);
    }


    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@RequestParam Map<String, Object> params, @PathVariable("attrgroupId") Long attrgroupId)
    {
        PageUtils page = attrService.getNoRelationAttr(params, attrgroupId);
        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list/{categoryId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("categoryId") Long categoryId)
    {
//        PageUtils page = attrGroupService.queryPage(params);

        PageUtils page = attrGroupService.queryPageInfo(params, categoryId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId)
    {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);

        attrGroup.setCatelogPath(path);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup)
    {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup)
    {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds)
    {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
