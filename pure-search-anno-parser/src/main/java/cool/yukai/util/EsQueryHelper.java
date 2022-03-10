package cool.yukai.util;


import cool.yukai.anno.ESField;
import cool.yukai.anno.EvalSuperClass;
import cool.yukai.builder.FieldSetterContext;
import cool.yukai.constant.SearchGeneralInfoConstant;
import cool.yukai.model.ESBoolQueryModel;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2020/9/3 20:58
 **/
public class EsQueryHelper {


    /**
     * 以注解 {@code com.dfire.elastic.anno.ESField}
     * 的方式对JAVA Class名字段进行适配映射
     * 生成的map只包含有ESField注解的字段
     */
    public static List<ESBoolQueryModel> describeQueryFieldOnlyViaAnno(Object input) {
        List<ESBoolQueryModel> queryList = new ArrayList<>();
        Class<?> objClazz = input.getClass();
        fillList(input, queryList, objClazz);

        if (objClazz.getAnnotation(EvalSuperClass.class) != null) {
            Class<?> superclass = objClazz.getSuperclass();
            fillList(input, queryList, superclass);
        }
        return removeGeneralInfo(queryList);
    }

    @SuppressWarnings("all")
    private static void fillList(Object input, List<ESBoolQueryModel> queryModelList, Class<?> objClazz) {
        //shared
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        Field[] fields = objClazz.getDeclaredFields();
        for (Field field : fields) {
            ESField esField = field.getAnnotation(ESField.class);
            if (esField != null) {
                field.setAccessible(true);
                String fieldName = esField.esFieldName();
                Object value = null;
                try {
                    value = field.get(input);
                } catch (IllegalAccessException e) {
                    throw ExceptUtil.fakeReturn(e);
                }

                if (value == null || (value instanceof Collection &&
                        CollectionUtils.isEmpty((Collection) value))) {
                    continue;
                }

                if (esField.commaSplit() && value instanceof String) {
                    // reformat value of the format like  "1,2,3,4"
                    value = Arrays.asList(((String) value).split(SearchGeneralInfoConstant.COMMA));
                }


                ESBoolQueryModel<Object> esBoolQueryModel = ESBoolQueryModel.builder()
                        .fieldName(fieldName)
                        .object(value)
                        .forMustNot(esField.forMustNot())
                        .boolQueryStrategy(esField.boolQueryStrategy())
                        .isCollection(value instanceof Collection)
                        .boolQueryBuilder(boolQueryBuilder)
                        .searchAnalyzer(Strings.isEmpty(esField.searchAnalyser()) ? null : esField.searchAnalyser())
                        .build();


                queryModelList.add(esBoolQueryModel);
            }
        }
    }


    public static <T> T sneakyParseObject(Class<T> clazz, Map<String, Object> sourceMap) {
        try {
            return parseObject(clazz, sourceMap);
        } catch (Exception e) {
            throw ExceptUtil.fakeReturn(e);
        }
    }

    public static <T> T parseObject(Class<T> clazz, Map<String, Object> sourceMap) throws Exception {
        T parsedObj = clazz.newInstance();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ESField anno = field.getAnnotation(ESField.class);
            if (anno != null) {
                String esFieldName = anno.esFieldName();
                Object esFieldVal = sourceMap.get(esFieldName);
                FieldSetterContext.INSTANCE.setField(field, esFieldVal, parsedObj);
            }
        }
        return parsedObj;
    }


    @Deprecated
    public static Map<String, Object> toSubLineKeyMap(Map<String, Object> camelCaseKeyMap) {
        Map<String, Object> subLineKeyMap = new HashMap<>();
        camelCaseKeyMap.forEach((k, v) -> {
            subLineKeyMap.put(camelCaseCharToSubLine(k), v);
        });

        return subLineKeyMap;
    }


    public static Map<String, Object> getValueNotEmptyMapForEs(Map<String, Object> from) {
        Map<String, Object> to = new HashMap<>();
        from.forEach((k, v) -> {
            if (v != null) {
                if (v instanceof String) {
                    GO_ONLY_AVAILABLE((String) v, () -> to.put(k, v));
                } else if (v instanceof Collection) {
                    GO_ONLY_AVAILABLE((Collection) v, () -> to.put(k, v));
                } else {
                    //其他数据类型不需要检查是否为空
                    to.put(k, v);
                }
            }
        });

        return to;
    }


    public static String camelCaseCharToSubLine(String camelCaseStr) {
        return camelCaseStr.replaceAll("[A-Z]", "_$0").toLowerCase();
    }


    public static boolean GO_ONLY_AVAILABLE(Collection input, Runnable action) {
        if (!CollectionUtils.isEmpty(input)) {
            action.run();
            return true;
        }
        return false;
    }

    public static boolean GO_ONLY_AVAILABLE(String input, Runnable action) {
        if (!Strings.isEmpty(input)) {
            action.run();
            return true;
        }
        return false;
    }


    public static List<ESBoolQueryModel> removeGeneralInfo(List<ESBoolQueryModel> input) {
        return input.stream().filter(queryModel -> {
            return !SearchGeneralInfoConstant
                    .PAGE_INFO_JAVA_FIELD_NAME.contains(queryModel.getFieldName());
        }).filter(queryModel -> {
            return queryModel.getObject() != null;
        }).collect(Collectors.toList());
    }

}
