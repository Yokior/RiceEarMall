package com.rice.member.dao;

import com.rice.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:42:00
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
