package com.pro.product.service.impl;

import com.pro.product.vo.SkuItemSaleAttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pro.common.utils.PageUtils;
import com.pro.common.utils.Query;

import com.pro.product.dao.SkuSaleAttrValueDao;
import com.pro.product.entity.SkuSaleAttrValueEntity;
import com.pro.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {
    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVo> getSkuSaleAttrValueBySpuId(Long spuId) {
        List<SkuItemSaleAttrVo>  attrsVo = skuSaleAttrValueDao.getSkuSaleAttrValueBySpuId(spuId);
        return attrsVo;
    }

}
