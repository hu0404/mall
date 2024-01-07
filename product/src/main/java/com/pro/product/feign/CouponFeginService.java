package com.pro.product.feign;

import com.pro.common.dto.SkuReductionDTO;
import com.pro.common.dto.SpuBoundsDTO;
import com.pro.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("coupon")
public interface CouponFeginService {

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveFullReductionInfo(@RequestBody SkuReductionDTO dto);


    @RequestMapping("/coupon/spubounds/saveSpuBounds")
    R saveSpuBounds(@RequestBody SpuBoundsDTO spuBounds);
}
