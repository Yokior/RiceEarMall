package com.rice.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rice.member.dao.MemberLevelDao;
import com.rice.member.entity.MemberLevelEntity;
import com.rice.member.exception.PhoneExistException;
import com.rice.member.exception.UsernameExistException;
import com.rice.member.vo.MemberLoginVo;
import com.rice.member.vo.MemberRegistVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.Query;

import com.rice.member.dao.MemberDao;
import com.rice.member.entity.MemberEntity;
import com.rice.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService
{

    @Autowired
    private MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(MemberRegistVo vo) throws PhoneExistException, UsernameExistException
    {
        MemberEntity memberEntity = new MemberEntity();

        // 设置默认会员等级
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultMemberLevel();
        memberEntity.setLevelId(levelEntity.getId());

        checkMobileUnique(vo.getPhone());
        checkUsernameUnique(vo.getUserName());
        // 检查唯一性通过 保存数据
        memberEntity.setUsername(vo.getUserName());
        memberEntity.setMobile(vo.getPhone());

        //  密码加密存储
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());
        memberEntity.setPassword(encode);

        // 保存数据
        save(memberEntity);
    }

    @Override
    public void checkMobileUnique(String mobile) throws PhoneExistException
    {
        LambdaQueryWrapper<MemberEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MemberEntity::getMobile, mobile);

        int count = count(lqw);
        if (count > 0)
        {
            throw new PhoneExistException();
        }
    }

    @Override
    public void checkUsernameUnique(String username) throws UsernameExistException
    {
        LambdaQueryWrapper<MemberEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MemberEntity::getUsername, username);

        int count = count(lqw);
        if (count > 0)
        {
            throw new UsernameExistException();
        }
    }

    @Override
    public MemberEntity login(MemberLoginVo vo)
    {
        String loginacct = vo.getLoginacct();
        String password = vo.getPassword();

        if (StringUtils.isBlank(loginacct) || StringUtils.isBlank(password))
        {
            return null;
        }

        // 数据库查询
        LambdaQueryWrapper<MemberEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MemberEntity::getUsername, loginacct);
        lqw.or().eq(MemberEntity::getMobile, loginacct);

        MemberEntity memberEntity = getOne(lqw);
        if (memberEntity == null)
        {
            return null;
        }

        // 对比密码
        String dbPassword = memberEntity.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(password, dbPassword);
        if (!matches)
        {
            return null;
        }

        return memberEntity;
    }

}