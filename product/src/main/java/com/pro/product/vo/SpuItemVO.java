package com.pro.product.vo;

import com.pro.product.entity.SkuImagesEntity;
import com.pro.product.entity.SkuInfoEntity;
import com.pro.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * 商品详情页的数据对象
 */
@Data
public class SpuItemVO {
    // 1.sku的基本信息 pms_sku_info
    SkuInfoEntity info;

    boolean hasStock = true; // 是否有库存
    // 2.sku的图片信息pms_sku_images
    List<SkuImagesEntity> images;
    // 3.获取spu中的销售属性的组合
    List<SkuItemSaleAttrVo> saleAttrs;
    // 4.获取SPU的介绍
    SpuInfoDescEntity desc;

    // 5.获取SPU的规格参数
    List<SpuItemGroupAttrVo> baseAttrs;

    // 6.绑定的对应的秒杀服务
    SeckillVO seckillVO;

}
