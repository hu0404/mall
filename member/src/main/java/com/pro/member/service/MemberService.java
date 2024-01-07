package com.pro.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pro.common.utils.PageUtils;
import com.pro.member.entity.MemberEntity;
import com.pro.member.vo.MemberLoginVO;
import com.pro.member.vo.MemberReigerVO;

import java.util.Map;

/**
 * 会员
 *
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 19:46:23
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberReigerVO vo);

    MemberEntity login(MemberLoginVO vo);
}

