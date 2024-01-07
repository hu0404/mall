package com.pro.search.service;

import com.pro.search.vo.SearchParam;
import com.pro.search.vo.SearchResult;
import org.springframework.stereotype.Service;

public interface MallSearchService {
    SearchResult search(SearchParam param);

}
