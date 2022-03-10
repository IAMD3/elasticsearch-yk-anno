package cool.yukai.model;

import cool.yukai.constant.BoolQueryStrategy;
import lombok.Builder;
import lombok.Data;
import org.elasticsearch.index.query.BoolQueryBuilder;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2020/12/22 14:09
 **/
@Data
@Builder
public class ESBoolQueryModel<T> {
    private String fieldName;
    private T object;
    private BoolQueryStrategy boolQueryStrategy;
    private boolean forMustNot;
    private boolean isCollection;
    private String searchAnalyzer;
    //shared
    private BoolQueryBuilder boolQueryBuilder;
}
