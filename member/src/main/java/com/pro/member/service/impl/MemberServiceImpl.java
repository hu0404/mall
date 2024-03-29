package com.pro.member.service.impl;

import com.pro.member.entity.MemberLevelEntity;
import com.pro.member.exception.PhoneExsitExecption;
import com.pro.member.exception.UsernameExsitException;
import com.pro.member.service.MemberLevelService;
import com.pro.member.vo.MemberLoginVO;
import com.pro.member.vo.MemberReigerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pro.common.utils.PageUtils;
import com.pro.common.utils.Query;

import com.pro.member.dao.MemberDao;
import com.pro.member.entity.MemberEntity;
import com.pro.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelService memberLevelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 完成会员的注册功能
     * @param vo
     */
    @Override
    public void register(MemberReigerVO vo) throws PhoneExsitExecption,UsernameExsitException{
        MemberEntity entity = new MemberEntity();
        // 设置会员等级 默认值
        MemberLevelEntity memberLevelEntity = memberLevelService.queryMemberLevelDefault();
        entity.setLevelId(memberLevelEntity.getId()); // 设置默认的会员等级

        // 添加对应的账号和手机号是不能重复的
        checkUsernameUnique(vo.getUserName());
        checkPhoneUnique(vo.getPhone());

        entity.setUsername(vo.getUserName());
        entity.setNickname(vo.getUserName());
        entity.setMobile(vo.getPhone());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(vo.getPassword());
        // 需要对密码做加密处理
        entity.setPassword(encode);
        // 设置其他的默认值
        this.save(entity);
    }

    /**
     * 校验手机号是否存在
     * @param phone
     * @throws PhoneExsitExecption
     */
    private void checkPhoneUnique(String phone) throws PhoneExsitExecption {
        int mobile = this.count(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if(mobile > 0){
            // 说明手机号是存在的
            throw new PhoneExsitExecption();
        }
    }

    /**
     * 校验账号是否存在
     * @param userName
     * @throws UsernameExsitException
     */
    private void checkUsernameUnique(String userName) throws UsernameExsitException {
        int username = this.count(new QueryWrapper<MemberEntity>().eq("username", userName));
        if(username > 0){
            throw new UsernameExsitException();
        }
    }

    @Override
    public MemberEntity login(MemberLoginVO vo) {
        // 1.根据账号或者手机号来查询会员信息
        MemberEntity entity = this.getOne(new QueryWrapper<MemberEntity>()
                .eq("username", vo.getUserName())
                .or()
                .eq("mobile", vo.getUserName()));
        if(entity != null){
            // 2.如果账号或者手机号存在 然后根据密码加密后的校验来判断是否登录成功
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean matches = encoder.matches(vo.getPassword(), entity.getPassword());
            if(matches){
                // 表明登录成功
                return entity;
            }
        }
        return null;
    }

}
