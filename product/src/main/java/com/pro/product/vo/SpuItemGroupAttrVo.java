package com.pro.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class SpuItemGroupAttrVo {
    private String groupName;
    private List<Attr> baseAttrs;
}
