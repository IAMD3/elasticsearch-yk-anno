package demo.show.me;

import cool.yukai.util.DSLParser;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2022/3/10 17:02
 **/
public class ESSimpleQueryDemo {

    public static void main(String[] args) {
        IndexDoc doc = new IndexDoc();
        doc.setFieldA("valueA 全文搜索");
        doc.setFieldB("valueB 精准匹配");

        SearchSourceBuilder query_dsl = DSLParser.parseObj(doc);
        System.out.println(query_dsl.toString());
    }
}
