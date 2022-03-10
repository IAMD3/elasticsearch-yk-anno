package cool.yukai.anno;


import cool.yukai.constant.BoolQueryStrategy;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ESField {

    /**
     * @return 对应的ES 索引中mapping下的fieldName , {@code NULL}代表Class中字段不做映射处理
     */
    String esFieldName();

    /**
     * @return 是否用于mustNot拼接
     */
    boolean forMustNot() default false;

    /**
     * @return  bool query strategy, using 'and' query as default
     */
    BoolQueryStrategy boolQueryStrategy() default  BoolQueryStrategy.TERM_MUST;


    /**
     * @return if a collection got split via comma
     */
    boolean commaSplit() default false;

    /**
     * @return search_analyser for match query xxxD
     */
    String searchAnalyser() default "";
}
