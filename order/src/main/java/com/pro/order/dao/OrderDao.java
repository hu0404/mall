package com.pro.order.dao;

import com.pro.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 15:48:47
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
