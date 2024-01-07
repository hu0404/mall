package com.pro.search;

import lombok.Data;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class Search {

    @Data
    public class SkuESModel {
        private Long skuId;
        private Long spuId;
        private String subTitle;
        private BigDecimal skuPrice;
        private String skuImg;
        private Long saleCount;
        private Boolean hasStock;
        private Long hotScore;
        private Long brandId;
        private Long catalogId;
        private String brandName;
        private String brandImg;
        private String catalogName;
        private List<Attrs> attrs;

        @Data
        public class Attrs{
            private Long attrId;
            private String attrName;
            private String attrValue;
        }

    }
}
