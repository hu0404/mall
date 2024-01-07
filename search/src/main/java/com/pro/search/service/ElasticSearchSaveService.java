package com.pro.search.service;

import com.pro.common.dto.es.SkuESModel;

import java.io.IOException;
import java.util.List;

public interface ElasticSearchSaveService {
    Boolean productStatusUp(List<SkuESModel> skuESModels) throws IOException;
}
