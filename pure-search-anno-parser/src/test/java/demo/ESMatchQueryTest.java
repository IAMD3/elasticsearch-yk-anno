package demo;


import cool.yukai.anno.ESField;
import cool.yukai.constant.BoolQueryStrategy;
import cool.yukai.util.DSLParser;
import lombok.Builder;
import lombok.Data;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2020/12/22 16:44
 **/
public class ESMatchQueryTest {

    @Test
    public void doTestEsConfig() throws IllegalAccessException {

        IndexModelCls queryModel = IndexModelCls.builder()
                .shouldMatch1("should_valueA").shouldMatch2("should_valueB").shouldMatch3("should_valueC")
                .mustMatch1("match_valueA").mustMatch2("match_valueB")
                .build();

        SearchSourceBuilder searchSourceBuilder = DSLParser.parseObj(queryModel);
        System.out.println(searchSourceBuilder);
    }


    @Data
    @Builder
    static class IndexModelCls {
        @ESField(esFieldName = "must_match_field1", boolQueryStrategy = BoolQueryStrategy.MATCH_MUST, searchAnalyser = "x_searcher")
        private String mustMatch1;
        @ESField(esFieldName = "must_match_field2", boolQueryStrategy = BoolQueryStrategy.MATCH_MUST)
        private String mustMatch2;
        @ESField(esFieldName = "should_match_field1", boolQueryStrategy = BoolQueryStrategy.MATCH_SHOULD)
        private String shouldMatch1;
        @ESField(esFieldName = "should_match_field2", boolQueryStrategy = BoolQueryStrategy.MATCH_SHOULD, searchAnalyser = "x_searcher")
        private String shouldMatch2;
        @ESField(esFieldName = "should_match_field3", boolQueryStrategy = BoolQueryStrategy.MATCH_SHOULD)
        private String shouldMatch3;

    }
}
