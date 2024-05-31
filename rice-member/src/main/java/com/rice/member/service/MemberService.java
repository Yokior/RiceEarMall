package com.rice.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rice.common.utils.PageUtils;
import com.rice.member.entity.MemberEntity;
import com.rice.member.exception.PhoneExistException;
import com.rice.member.exception.UsernameExistException;
import com.rice.member.vo.MemberRegistVo;

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

    void regist(MemberRegistVo vo) throws PhoneExistException, UsernameExistException;

    void checkMobileUnique(String mobile) throws PhoneExistException;

    void checkUsernameUnique(String username) throws UsernameExistException;
}

