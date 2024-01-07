package com.pro.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.pro.common.dto.SkuHasStockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pro.ware.entity.WareSkuEntity;
import com.pro.ware.service.WareSkuService;
import com.pro.common.utils.PageUtils;
import com.pro.common.utils.R;



/**
 * 商品库存
 *
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 20:10:44
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;
    /**
     * 查询对应的skuId是否有库存
     * @param skuIds
     * @return
     */
    @PostMapping("/hasStock")
    public List<SkuHasStockDto> getSkusHasStock(@RequestBody List<Long> skuIds){
        List<SkuHasStockDto> list = wareSkuService.getSkusHasStock(skuIds);
        return list;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
