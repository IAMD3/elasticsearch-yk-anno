package cool.yukai.query;

import cool.yukai.model.ESBoolQueryModel;
import org.elasticsearch.index.query.BoolQueryBuilder;

/**
 * <p>
 * date : 2020/12/22
 * time : 15:59
 * </p>
 *
 * @author Master T
 */
@FunctionalInterface
public interface Query {
    void query(ESBoolQueryModel m, BoolQueryBuilder boolQueryBuilder);
}
