package com.pro.product.vo;

import lombok.Data;

@Data
public class AttrResponseVo extends AttrVO{
    private String catelogName;

    private String groupName;

    private Long[] catelogPath;

}
