package demo.show.me;

import cool.yukai.anno.ESField;
import cool.yukai.constant.BoolQueryStrategy;
import lombok.Data;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2022/3/10 17:03
 **/
@Data
public class IndexDoc {
    @ESField(esFieldName = "field_a", boolQueryStrategy = BoolQueryStrategy.MATCH_MUST)
    private String fieldA;
    @ESField(esFieldName = "field_B", boolQueryStrategy = BoolQueryStrategy.TERM_MUST)
    private String fieldB;
}
