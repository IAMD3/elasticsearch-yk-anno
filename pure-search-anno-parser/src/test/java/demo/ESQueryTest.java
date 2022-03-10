package demo;


import cool.yukai.anno.ESField;
import cool.yukai.constant.BoolQueryStrategy;
import cool.yukai.util.DSLParser;
import lombok.Builder;
import lombok.Data;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2020/12/22 16:44
 **/
public class ESQueryTest {

    @Test
    public void doTestEsConfig() throws IllegalAccessException {

        IndexModelCls queryModel = IndexModelCls.builder()
                .objFieldForMust("objFieldForMust")
                .collectedFieldForMust(Arrays.asList("collectedFieldForMust1", "collectedFieldForMust2"))
                .objFieldForMustNot("objFieldForMustNot")
                .collectedFieldForMustNot(Arrays.asList("collectedFieldForMustNot1", "collectedFieldForMustNot2"))
                .objFieldForShould("objFieldForShould")
                .collectedFieldForShould(Arrays.asList("collectedFieldForShould1", "collectedFieldForShould"))


                .shouldMatch1("should_valueA")
                .mustMatch1("match_valueA").mustMatch2("match_valueB")
                .objFieldForRangeFrom(Long.MIN_VALUE)
                .objFieldForRangeTo(Long.MAX_VALUE)
                .build();

        SearchSourceBuilder searchSourceBuilder = DSLParser.parseObj(queryModel);
        System.out.println(searchSourceBuilder);
    }


    @Data
    @Builder
    static class IndexModelCls {
        @ESField(esFieldName = "objFieldForMust")
        private String objFieldForMust;
        @ESField(esFieldName = "collectedFieldForMust")
        private List<String> collectedFieldForMust;
        @ESField(esFieldName = "objFieldForMustNot", forMustNot = true)
        private String objFieldForMustNot;
        @ESField(esFieldName = "collectedFieldForMustNot", forMustNot = true)
        private List<String> collectedFieldForMustNot;
        @ESField(esFieldName = "objFieldForShould", boolQueryStrategy = BoolQueryStrategy.TERM_SHOULD)
        private String objFieldForShould;
        @ESField(esFieldName = "collectedFieldForShould", boolQueryStrategy = BoolQueryStrategy.TERM_SHOULD)
        private List<String> collectedFieldForShould;


        @ESField(esFieldName = "must_match_field1", boolQueryStrategy = BoolQueryStrategy.MATCH_MUST, searchAnalyser = "x_searcher")
        private String mustMatch1;
        @ESField(esFieldName = "must_match_field2", boolQueryStrategy = BoolQueryStrategy.MATCH_MUST)
        private String mustMatch2;
        @ESField(esFieldName = "should_match_field1", boolQueryStrategy = BoolQueryStrategy.MATCH_SHOULD)
        private String shouldMatch1;

        @ESField(esFieldName = "objFieldForRangeFrom", boolQueryStrategy = BoolQueryStrategy.RANGE_FROM)
        private Long objFieldForRangeFrom;
        @ESField(esFieldName = "objFieldForRangeTo", boolQueryStrategy = BoolQueryStrategy.RANGE_FROM)
        private Long objFieldForRangeTo;

    }
}
