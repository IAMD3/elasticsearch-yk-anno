package cool.yukai.util;


import cool.yukai.constant.BoolQueryStrategy;
import cool.yukai.model.ESBoolQueryModel;
import cool.yukai.query.Query;
import cool.yukai.query.TermsQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2020/9/4 17:38
 **/
public class DSLParser {

    /**
     * 0 Sort
     * 1 OBJ Fields -> ES Index Fields
     * 2 CamelCase -> sSubLineCase
     * 3 buildTermsQueryForEsFields
     */

    public static SearchSourceBuilder parseObj(Object objForParsed) {
        List<ESBoolQueryModel> queryModelList = EsQueryHelper.describeQueryFieldOnlyViaAnno(objForParsed);
        if (queryModelList == null || queryModelList.size() == 0) return new SearchSourceBuilder();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        buildTermsQuery(queryModelList);

        searchSourceBuilder
                .query(queryModelList.get(0)
                        .getBoolQueryBuilder());

        return searchSourceBuilder;
    }


    public static void buildTermsQuery(List<ESBoolQueryModel> esModelList) {
        Map<BoolQueryStrategy, List<ESBoolQueryModel>> strategyMap =
                esModelList.stream()
                        .collect(Collectors.groupingBy(ESBoolQueryModel::getBoolQueryStrategy));

        strategyMap.forEach(DSLParser::buildEsQueryViaStrategy);
    }


    private static void buildEsQueryViaStrategy(BoolQueryStrategy strategy, List<ESBoolQueryModel> esBoolQueryModels) {
        switch (strategy) {
            case TERM_SHOULD:
                goBuildQuery(esBoolQueryModels
                        , DSLParser::goBuildShouldTermsQuery
                        , DSLParser::goBuildShouldTermQuery);
                break;
            case TERM_MUST:
                //must/mustNot division
                List<ESBoolQueryModel> mustNotEsBoolQueryModels = esBoolQueryModels
                        .stream()
                        .filter(ESBoolQueryModel::isForMustNot)
                        .collect(Collectors.toList());

                List<ESBoolQueryModel> mustEsBoolQueryModels = esBoolQueryModels
                        .stream()
                        .filter(model -> !model.isForMustNot())
                        .collect(Collectors.toList());

                goBuildQuery(mustEsBoolQueryModels,
                        DSLParser::goBuildMustTermsQuery,
                        DSLParser::goBuildMustTermQuery);

                goBuildQuery(mustNotEsBoolQueryModels,
                        DSLParser::goBuildMustNotTermsQuery,
                        DSLParser::goBuildMustNotTermQuery);
                break;

            case RANGE_FROM:
                goBuildQuery(esBoolQueryModels, DSLParser::goBuildRangeFromQuery);
                break;
            case RANGE_TO:
                goBuildQuery(esBoolQueryModels, DSLParser::goBuildRangeToQuery);
                break;
            case MATCH_MUST:
                goBuildQuery(esBoolQueryModels, DSLParser::goBuildMustMatchQuery);
                break;
            case MATCH_SHOULD:
                goBuildQuery(esBoolQueryModels, DSLParser::goBuildShouldMatchQuery);
                break;
            default:
                break;
        }

    }


    /**
     * Yukai is so brilliant xxD!
     *
     * @param esBoolQueryModels
     * @param Query
     */
    private static void goBuildQuery(List<ESBoolQueryModel> esBoolQueryModels, Query Query) {
        goBuildQuery(esBoolQueryModels, null, Query);
    }

    private static void goBuildQuery(List<ESBoolQueryModel> esBoolQueryModels,/**nullable**/TermsQuery termsQuery,/**generic**/Query Query) {
        BoolQueryBuilder boolQueryBuilder = extractBoolQueryBuilder(esBoolQueryModels);
        if (boolQueryBuilder == null) return;

        Map<Boolean, List<ESBoolQueryModel>> boolQueryModelGrouped = esBoolQueryModels
                .stream()
                .collect(Collectors.groupingBy(ESBoolQueryModel::isCollection));

        List<ESBoolQueryModel> collectedModelList =
                termsQuery == null ? null : boolQueryModelGrouped.get(true);

        List<ESBoolQueryModel> singleModelList =
                Query == null ? null : boolQueryModelGrouped.get(false);


        if (collectedModelList != null) {
            collectedModelList.forEach(collectedModel -> {
                termsQuery.termsQuery(collectedModel.getFieldName(), (Collection) collectedModel.getObject(), boolQueryBuilder);
            });
        }

        if (singleModelList != null) {
            singleModelList.forEach(singleModel -> {
                Query.query(singleModel, boolQueryBuilder);
            });
        }
    }

    private static BoolQueryBuilder extractBoolQueryBuilder(List<ESBoolQueryModel> esBoolQueryModels) {
        if (esBoolQueryModels == null || esBoolQueryModels.size() == 0) return null;

        return esBoolQueryModels
                .get(0)
                .getBoolQueryBuilder();
    }


    private static MatchQueryBuilder genMatchQueryBuilder(ESBoolQueryModel esBoolQueryModel) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(esBoolQueryModel.getFieldName(), esBoolQueryModel.getObject());
        Optional.ofNullable(esBoolQueryModel.getSearchAnalyzer()).ifPresent(matchQueryBuilder::analyzer);
        return matchQueryBuilder;
    }

    private static void goBuildMustMatchQuery(ESBoolQueryModel esBoolQueryModel, BoolQueryBuilder boolQueryBuilder) {
        MatchQueryBuilder matchQueryBuilder = genMatchQueryBuilder(esBoolQueryModel);
        boolQueryBuilder.must(matchQueryBuilder);
    }


    private static void goBuildShouldMatchQuery(ESBoolQueryModel esBoolQueryModel, BoolQueryBuilder boolQueryBuilder) {
        MatchQueryBuilder matchQueryBuilder = genMatchQueryBuilder(esBoolQueryModel);
        boolQueryBuilder.should((matchQueryBuilder));
    }


    private static void goBuildShouldTermsQuery(String fieldName, Collection termList, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.should(QueryBuilders.termsQuery(fieldName, termList));
    }


    private static void goBuildShouldTermQuery(ESBoolQueryModel m, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.should(QueryBuilders.termQuery(m.getFieldName(), m.getObject()));
    }


    private static void goBuildMustTermsQuery(String fieldName, Collection termList, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(fieldName, termList));
    }

    private static void goBuildMustTermQuery(ESBoolQueryModel m, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.must(QueryBuilders.termQuery(m.getFieldName(), m.getObject()));
    }

    private static void goBuildMustNotTermQuery(ESBoolQueryModel m, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(m.getFieldName(), m.getObject()));
    }

    private static void goBuildMustNotTermsQuery(String filedName, Collection termList, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.mustNot(QueryBuilders.termsQuery(filedName, termList));
    }

    private static void goBuildRangeFromQuery(ESBoolQueryModel m, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.filter(QueryBuilders.rangeQuery(m.getFieldName()).gte(m.getObject()));
    }

    private static void goBuildRangeToQuery(ESBoolQueryModel m, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.filter(QueryBuilders.rangeQuery(m.getFieldName()).lte(m.getObject()));
    }
}
