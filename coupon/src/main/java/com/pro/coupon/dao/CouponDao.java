package com.pro.coupon.dao;

import com.pro.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 19:53:52
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
