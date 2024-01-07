package com.pro.coupon;

import com.pro.coupon.entity.MemberPriceEntity;
import com.pro.coupon.service.MemberPriceService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedList;

@SpringBootTest(classes = CouponApplication.class)
class CouponApplicationTests {
    @Autowired
    MemberPriceService memberPriceService;

    @Test
    void contextLoads() {
        LinkedList<MemberPriceEntity> list = new LinkedList<>();
        MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
        BigDecimal bigDecimal = new BigDecimal("22");
        memberPriceEntity.setMemberPrice(bigDecimal);
        list.add(memberPriceEntity);
//        memberPriceService.saveBatch(list);
        memberPriceService.save(memberPriceEntity);

    }

}
