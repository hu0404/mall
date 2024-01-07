package com.pro.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pro.common.utils.PageUtils;
import com.pro.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 15:48:47
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

