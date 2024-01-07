package com.pro.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pro.common.utils.PageUtils;
import com.pro.coupon.entity.SpuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 19:53:52
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

