package com.pro.auth_server;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
class AuthServerApplicationTests {

    @Resource
    RedisTemplate redisTemplate;
    @Test
    void contextLoads() {
//        redisTemplate.opsForValue().set("121","213");
        LinkedList<Object> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
//        ArrayList<Object> objects = new ArrayList<>(list);
//        System.out.println(objects);
//        int[] a={1,2};
//        int[] a2=new int[10];
        Integer[] a3=new Integer[]{1,2,3};
        list.add(2,11);
        list.addAll(new ArrayList<>(Arrays.asList(a3)));
        System.out.println(list);
    }

    public void main(String[] args) {

        Class<User> userClass = User.class;
    }

    @Data
    public class User{
        String a;
    }

}
