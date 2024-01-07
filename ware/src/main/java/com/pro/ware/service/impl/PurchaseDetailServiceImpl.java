package com.pro.ware.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pro.common.utils.PageUtils;
import com.pro.common.utils.Query;

import com.pro.ware.dao.PurchaseDetailDao;
import com.pro.ware.entity.PurchaseDetailEntity;
import com.pro.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<>();
        // key=9&status=0&wareId=1
        // 添加对应的查询条件
        String key = (String)params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and(w->{
                w.eq("purchase_id",key).or()
                        .eq("sku_id",key);
            });
        }
        String status = (String)params.get("status");
        if(!StringUtils.isEmpty(status)){
            queryWrapper.eq("status",status);
        }

        String wareId = (String)params.get("wareId");
        if(!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }


        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listDetailByPurchaseId(Long id) {
        List<PurchaseDetailEntity> list = this.list(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", id));
        return list;
    }

}
