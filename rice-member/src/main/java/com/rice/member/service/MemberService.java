package com.rice.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rice.common.utils.PageUtils;
import com.rice.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:42:00
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

