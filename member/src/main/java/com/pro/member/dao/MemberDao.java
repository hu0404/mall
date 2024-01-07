package com.pro.member.dao;

import com.pro.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 19:46:23
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
