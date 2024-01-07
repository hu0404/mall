package com.pro.product.service.impl;

import com.pro.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pro.common.utils.PageUtils;
import com.pro.common.utils.Query;

import com.pro.product.dao.BrandDao;
import com.pro.product.entity.BrandEntity;
import com.pro.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
//        带条件的查询
        System.out.println(params);
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        String key =(String)params.get("key");
        if(!StringUtils.isEmpty(key)){
            //添加对应的查询条件
            queryWrapper.eq("brand_id",key).or().like("name",key);
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void updateDetail(BrandEntity brand) {
        //1.更新数据源
        this.updateById(brand);
        if(!StringUtils.isEmpty(brand.getName())){
            //同步更新级联关系中的数据
            categoryBrandRelationService.updateBrandName(brand.getBrandId(),brand.getName());
        }
    }

}
