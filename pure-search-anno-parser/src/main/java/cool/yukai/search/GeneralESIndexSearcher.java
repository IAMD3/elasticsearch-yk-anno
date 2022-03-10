package cool.yukai.search;

import cool.yukai.sort.Sort;
import cool.yukai.util.DSLParser;
import cool.yukai.util.ExceptUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2020/12/29 14:56
 **/
@Slf4j
public class GeneralESIndexSearcher {

    private final static int ES_DEFAULT_FROM = 0;
    private final static int ES_DEFAULT_PAGE_SIZE = 100;


    public static <PARAM> SearchResponse search(RestHighLevelClient client, PARAM param, String indexName) {
        return search(client, param, indexName, null);
    }

    public static <PARAM> SearchResponse search(RestHighLevelClient client, PARAM param, String indexName, Sort sort) {
        return search(client, param, indexName, ES_DEFAULT_FROM, ES_DEFAULT_PAGE_SIZE, sort);
    }

    public static <PARAM> SearchResponse search(RestHighLevelClient client, PARAM param, String indexName, int from, int pageSize, Sort sort) {
        SearchSourceBuilder searchSourceBuilder;
        if (sort == null) {
            searchSourceBuilder = genSearchBuilder(param, from, pageSize, null);
        } else {
            searchSourceBuilder = genSearchBuilder(param, from, pageSize, sb -> {
                sb.sort(sort.getFieldName(), sort.getAsc() ? SortOrder.ASC : SortOrder.DESC);
            });
        }

        return search(client, searchSourceBuilder, indexName);
    }

    @SuppressWarnings("all")
    public static SearchResponse search(RestHighLevelClient client, SearchSourceBuilder searchSourceBuilder, String indexName) {
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);

        log.info("search dsl:{}", searchSourceBuilder.toString());
        try {
            return client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw ExceptUtil.fakeReturn(e);
        }

    }


    public static <PARAM> SearchSourceBuilder genSearchBuilder(PARAM param
            , Consumer<SearchSourceBuilder> injector) throws IllegalAccessException {
        return genSearchBuilder(param, ES_DEFAULT_FROM, ES_DEFAULT_PAGE_SIZE, injector);
    }

    public static <PARAM> SearchSourceBuilder genSearchBuilder(PARAM param, int from, int pageSize
            , Consumer<SearchSourceBuilder> injector) {

        SearchSourceBuilder searchSourceBuilder;

        searchSourceBuilder = DSLParser.parseObj(param);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(pageSize);

        Optional.ofNullable(injector)
                .ifPresent(i -> i.accept(searchSourceBuilder));

        return searchSourceBuilder;
    }


}

