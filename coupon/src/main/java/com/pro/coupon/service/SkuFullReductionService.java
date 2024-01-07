package com.pro.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pro.common.dto.SkuReductionDTO;
import com.pro.common.utils.PageUtils;
import com.pro.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 19:53:52
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionDTO dto);
}

