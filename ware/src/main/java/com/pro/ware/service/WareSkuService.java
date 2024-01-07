package com.pro.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pro.common.dto.SkuHasStockDto;
import com.pro.common.utils.PageUtils;
import com.pro.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 20:10:44
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuHasStockDto> getSkusHasStock(List<Long> skuIds);

    void addStock(Long skuId, Long wareId, Integer skuNum);
}

