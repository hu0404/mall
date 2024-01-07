package com.pro.member.dao;

import com.pro.member.entity.MemberLevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员等级
 *
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 19:46:23
 */
@Mapper
public interface MemberLevelDao extends BaseMapper<MemberLevelEntity> {

    MemberLevelEntity queryMemberLevelDefault();
}
