package com.pro.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.pro.product.vo.AttrResponseVo;
import com.pro.product.vo.AttrVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pro.product.entity.AttrEntity;
import com.pro.product.service.AttrService;
import com.pro.common.utils.PageUtils;
import com.pro.common.utils.R;



/**
 * 商品属性
 *
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 11:28:19
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    // attr/base/list/0?t=1640759871949&page=1&limit=10&key=aaa
    // attr/sale/list/
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseList(@RequestParam Map<String, Object> params
            ,@PathVariable("catelogId") Long catelogId
            ,@PathVariable("attrType") String attrType
    ){
        PageUtils page =attrService.queryBasePage(params,catelogId,attrType);
        return R.ok().put("page",page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);
        AttrResponseVo attr = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
//    @RequestMapping("/save")
//    public R save(@RequestBody AttrEntity attr){
//		attrService.save(attr);
//        return R.ok();
//    }
    @RequestMapping("/save")
    public R save(@RequestBody AttrVO vo){
        attrService.saveAttr(vo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVO attr){
//		attrService.updateById(attr);
        attrService.updateBaseAttr(attr);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
//		attrService.removeByIds(Arrays.asList(attrIds));
        attrService.removeByIdsDetails(attrIds);
        return R.ok();
    }

}
