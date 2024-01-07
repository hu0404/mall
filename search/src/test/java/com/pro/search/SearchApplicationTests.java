package com.pro.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pro.search.config.MallElasticSearchConfiguration;
import lombok.Data;
import lombok.ToString;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class SearchApplicationTests {
    @Autowired
    private RestHighLevelClient client;

    @Test
    void saveIndex() throws Exception {
        IndexRequest indexRequest = new IndexRequest("system");
        indexRequest.id("1");
        // indexRequest.source("name","bobokaoya","age",18,"gender","男");
        User user = new User();
        user.setName("bobo");
        user.setAge(22);
        user.setGender("男");
        // 用Jackson中的对象转json数据
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        indexRequest.source(json, XContentType.JSON);
        // 执行操作
        IndexResponse index = client.index(indexRequest, MallElasticSearchConfiguration.COMMON_OPTIONS);
        // 提取有用的返回信息
        System.out.println(index);
    }
    @Data
    class User{
        private String name;
        private Integer age;
        private String gender;
    }


    @Test
    void searchIndexAll() throws IOException {
        // 1.创建一个 SearchRequest 对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("system"); // 设置我们要检索的数据对应的索引库
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        /*sourceBuilder.query();
        sourceBuilder.from();
        sourceBuilder.size();
        sourceBuilder.aggregation();*/
        searchRequest.source(sourceBuilder);

        // 2.如何执行检索操作
        SearchResponse response = client.search(searchRequest, MallElasticSearchConfiguration.COMMON_OPTIONS);
        // 3.获取检索后的响应对象，我们需要解析出我们关心的数据
        System.out.println("ElasticSearch检索的信息："+response);
        //ElasticSearch检索的信息：{"took":0,"timed_out":false,"_shards":
        // {"total":1,"successful":1,"skipped":0,"failed":0},"hits":
        // {"total":{"value":1,"relation":"eq"},"max_score":1.0,"hits":
        // [{"_index":"system","_type":"_doc","_id":"1","_score":1.0,
        // "_source":{"name":"bobo","age":22,"gender":"男"}}]}}
    }

    @Test
    void searchIndexByAddress() throws IOException {
        // 1.创建一个 SearchRequest 对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("system"); // 设置我们要检索的数据对应的索引库
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 查询出bank下 address 中包含 mill的记录
        sourceBuilder.query(QueryBuilders.matchQuery("gender","男"));
        searchRequest.source(sourceBuilder);
        // System.out.println(searchRequest);

        // 2.如何执行检索操作
        SearchResponse response = client.search(searchRequest, MallElasticSearchConfiguration.COMMON_OPTIONS);
        // 3.获取检索后的响应对象，我们需要解析出我们关心的数据
        System.out.println("ElasticSearch检索的信息："+response);
        /*ElasticSearch检索的信息：{
        "took":5,
        "timed_out":false,
        "_shards":{
            "total":1,
            "successful":1,
            "skipped":0,
            "failed":0
        },
        "hits":{
        "total":{
            "value":1,
            "relation":"eq"
        },
        "max_score":0.2876821,
        "hits":[{
            "_index":"system",
            "_type":"_doc",
            "_id":"1",
            "_score":0.2876821,
            "_source":{
            "name":"bobo",
            "age":22,
            "gender":"男"
        }}]}}
         */
    }


    @Test
    void searchIndexAggregation() throws IOException {
        // 1.创建一个 SearchRequest 对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bobo"); // 设置我们要检索的数据对应的索引库
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 查询出bank下 所有的文档
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        // 聚合 aggregation
        // 聚合bank下年龄的分布和每个年龄段的平均薪资
        AggregationBuilder aggregationBuiler = AggregationBuilders.terms("ageAgg")
                .field("age")
                .size(10);
        // 嵌套聚合
        aggregationBuiler.subAggregation(AggregationBuilders.sum("sumAvg").field("age"));
        sourceBuilder.aggregation(aggregationBuiler);

        // 聚合平均年龄
        AvgAggregationBuilder balanceAggBuilder = AggregationBuilders.avg("ageAvg").field("age");
        sourceBuilder.aggregation(balanceAggBuilder);

        sourceBuilder.size(0); // 聚合的时候就不用显示满足条件的文档内容了
        searchRequest.source(sourceBuilder);
        System.out.println(sourceBuilder);

        // 2.如何执行检索操作
        SearchResponse response = client.search(searchRequest, MallElasticSearchConfiguration.COMMON_OPTIONS);
        // 3.获取检索后的响应对象，我们需要解析出我们关心的数据
        System.out.println(response);
    }


    @Test
    void searchIndexResponse() throws IOException {
        // 1.创建一个 SearchRequest 对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bobo"); // 设置我们要检索的数据对应的索引库
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 查询出bank下 address 中包含 mill的记录
        sourceBuilder.query(QueryBuilders.matchQuery("nameid","5"));
        searchRequest.source(sourceBuilder);
        // System.out.println(searchRequest);

        // 2.如何执行检索操作
        SearchResponse response = client.search(searchRequest, MallElasticSearchConfiguration.COMMON_OPTIONS);
        // 3.获取检索后的响应对象，我们需要解析出我们关心的数据
        // System.out.println("ElasticSearch检索的信息："+response);
        RestStatus status = response.status();
        TimeValue took = response.getTook();
        SearchHits hits = response.getHits();
        TotalHits totalHits = hits.getTotalHits();
        TotalHits.Relation relation = totalHits.relation;
        long value = totalHits.value;
        float maxScore = hits.getMaxScore(); // 相关性的最高分
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit documentFields : hits1) {
            /*"_index" : "bank",
                    "_type" : "account",
                    "_id" : "970",
                    "_score" : 5.4032025*/
            //documentFields.getIndex(),documentFields.getType(),documentFields.getId(),documentFields.getScore();
            String json = documentFields.getSourceAsString();
            //System.out.println(json);
            // JSON字符串转换为 Object对象
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(json, Account.class);
            System.out.println("account = " + account);
            //account = SearchApplicationTests.Account(name=bobo5, age=33, nameid=5)
        }
        System.out.println(relation.toString()+"--->" + value + "--->" + status);
    }

    @ToString
    @Data
    static class Account {
        private String name;
        private int age;
        private String nameid;
    }

}
