package cool.yukai.query;

import org.elasticsearch.index.query.BoolQueryBuilder;

import java.util.Collection;

/**
 * <p>
 * date : 2020/12/22
 * time : 15:57
 * </p>
 *
 * @author Master T
 */
@FunctionalInterface
public interface TermsQuery {
    void termsQuery(String fieldName, Collection termList, BoolQueryBuilder boolQueryBuilder);
}
