package com.pro.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pro.common.utils.PageUtils;
import com.pro.ware.entity.WareOrderTaskDetailEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 20:10:44
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

