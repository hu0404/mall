package com.pro.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pro.common.utils.PageUtils;
import com.pro.member.entity.MemberReceiveAddressEntity;

import java.util.Map;

/**
 * 会员收货地址
 *
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 19:46:23
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

