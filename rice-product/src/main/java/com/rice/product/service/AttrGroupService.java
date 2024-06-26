package com.rice.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rice.common.utils.PageUtils;
import com.rice.product.entity.AttrGroupEntity;
import com.rice.product.vo.AttrGroupWithAttrsVo;
import com.rice.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:10:00
 */
public interface AttrGroupService extends IService<AttrGroupEntity>
{

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageInfo(Map<String, Object> params, Long categoryId);

    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);
}

