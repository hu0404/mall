package com.pro.order.controller;

import java.util.Arrays;
import java.util.Map;

import com.pro.order.feign.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import com.pro.order.entity.OrderEntity;
import com.pro.order.service.OrderService;
import com.pro.common.utils.PageUtils;
import com.pro.common.utils.R;



/**
 * 订单
 *
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 15:48:47
 */
@RestController
@RefreshScope
@RequestMapping("order/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    ProductService productService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OrderEntity order){
		orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OrderEntity order){
		orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

//    自定义

//    OpenFeign调用接口
    @RequestMapping("/products")
    public R queryProduct(){
        // OpenFegin 远程调用服务
        R res = productService.list();
        return R.ok().put("products",res);
    }

    @Value("${user.username}")
    private String name;
    @Value("${user.age}")
    private Integer age;

    @RequestMapping("/users")
    public R queryUser(){
        return R.ok().put("name",name).put("age",age);
    }

}
