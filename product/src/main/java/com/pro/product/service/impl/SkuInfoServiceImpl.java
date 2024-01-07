package com.pro.product.service.impl;

import com.pro.product.entity.SkuImagesEntity;
import com.pro.product.entity.SpuInfoDescEntity;
import com.pro.product.service.*;
import com.pro.product.vo.SkuItemSaleAttrVo;
import com.pro.product.vo.SpuItemGroupAttrVo;
import com.pro.product.vo.SpuItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pro.common.utils.PageUtils;
import com.pro.common.utils.Query;

import com.pro.product.dao.SkuInfoDao;
import com.pro.product.entity.SkuInfoEntity;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    @Autowired
    SkuInfoDao skuInfoDao;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

//    @Autowired
//    SeckillFeignService seckillFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据spu查询所有的sku信息
     * @param spuId
     * @return
     */
    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        List<SkuInfoEntity> list = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return list;
    }

//    @Trace
//    @Tags({
//            @Tag(key="item",value = "returnedObj")
//            ,@Tag(key="itemParam",value = "arg[0]")
//    })
    @Override
    public SpuItemVO item(Long skuId) throws ExecutionException, InterruptedException {
        SpuItemVO vo = new SpuItemVO();
        CompletableFuture<SkuInfoEntity> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            // 1.sku的基本信息 pms_sku_info
            SkuInfoEntity skuInfoEntity = getById(skuId);
            vo.setInfo(skuInfoEntity);
            // 获取对应的SPUID
//            Long spuId = skuInfoEntity.getSpuId();
            // 获取对应的CatalogId 类别编号
//            Long catalogId = skuInfoEntity.getCatalogId();
            return skuInfoEntity;
        }, threadPoolExecutor);

        CompletableFuture<Void> saleFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            // 3.获取spu中的销售属性的组合
            List<SkuItemSaleAttrVo> saleAttrs = skuSaleAttrValueService.getSkuSaleAttrValueBySpuId(res.getSpuId());
            vo.setSaleAttrs(saleAttrs);
        }, threadPoolExecutor);

        CompletableFuture<Void> spuFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            // 4.获取SPU的介绍
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
            vo.setDesc(spuInfoDescEntity);
        }, threadPoolExecutor);


        CompletableFuture<Void> groupFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            // 5.获取SPU的规格参数
            List<SpuItemGroupAttrVo> groupAttrVo = attrGroupService
                    .getAttrgroupWithSpuId(res.getSpuId(), res.getCatalogId());
            vo.setBaseAttrs(groupAttrVo);
        }, threadPoolExecutor);

        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
            // 2.sku的图片信息pms_sku_images
            List<SkuImagesEntity> images = skuImagesService.getImagesBySkuId(skuId);
            vo.setImages(images);
        }, threadPoolExecutor);

//        CompletableFuture<Void> seckillFuture = CompletableFuture.runAsync(() -> {
//            // 查询商品的秒杀活动
//            R r = seckillFeignService.getSeckillSessionBySkuId(skuId);
//            if(r.getCode() == 0){
//                SeckillVO seckillVO = JSON.parseObject(r.get("data").toString(),SeckillVO.class);
//                vo.setSeckillVO(seckillVO);
//            }
//        }, threadPoolExecutor);

        CompletableFuture.allOf(saleFuture,spuFuture,imageFuture,groupFuture).get();


        return vo;
    }
}
