package com.pro.order.feign;

import com.pro.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * @FeignClient 指明我们要从注册中心中发现的服务的名称
* */
@FeignClient(name = "product")
public interface ProductService {
    /**
     * 需要访问的远程方法
     * @return
     */
    @RequestMapping("/product/brand/list")
    public R list();
}
