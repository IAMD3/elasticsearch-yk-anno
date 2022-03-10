# 简单 & 轻量 &  Elasticsearch DSL声明式生成框架


## 直接案例: 声明式生成DSL

### ES 文档对应DTO
``` java
@Data
public class IndexDoc {
   @ESField(esFieldName = "field_a", boolQueryStrategy = BoolQueryStrategy.MATCH_MUST)
    private String fieldA;
    @ESField(esFieldName = "field_B", boolQueryStrategy = BoolQueryStrategy.TERM_MUST)
    private String fieldB;
}
```

### 转DSL
```java
 public static void main(String[] args) {
        IndexDoc doc = new IndexDoc();
         doc.setFieldA("valueA 全文搜索");
         doc.setFieldB("valueB 精准匹配");

        SearchSourceBuilder query_dsl = DSLParser.parseObj(doc);
        System.out.println(query_dsl.toString());
    }

```

<font color="puple">DSL生成效果:</font>
```DSL
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "field_a": {
              "query": "valueA 全文搜索",
              "operator": "OR",
              "prefix_length": 0,
              "max_expansions": 50,
              "fuzzy_transpositions": true,
              "lenient": false,
              "zero_terms_query": "NONE",
              "auto_generate_synonyms_phrase_query": true,
              "boost": 1
            }
          }
        },
        {
          "term": {
            "field_B": {
              "value": "valueB 精准匹配",
              "boost": 1
            }
          }
        }
      ],
      "adjust_pure_negative": true,
      "boost": 1
    }
  }
}

```

## 直接案例: 返回的resourceMap ---> 文档DTO
### ES 文档对应DTO
```java
@ToString
public class MyDTO {
    @ESField(esFieldName = "name")
    private String name;
    @ESField(esFieldName = "num_int")
    private int num_int;
    @ESField(esFieldName = "num_Int")
    private Integer num_Int;
    @ESField(esFieldName = "num_Double")
    private Double num_Double;
    @ESField(esFieldName = "num_double")
    private double num_double;
    @ESField(esFieldName = "num_float")
    private float num_float;
    @ESField(esFieldName = "num_Float")
    private Float num_Float;
    @ESField(esFieldName = "num_String1")
    private String num_String1;
    @ESField(esFieldName = "num_String2")
    private String num_String2;
    @ESField(esFieldName = "num_String3")
    private String num_String3;
  
}

```
###从map----> 文档DTO

```java
 public static void main(String[] args) throws Exception {
        Map<String,Object> esSourceMap = new HashMap() {{
            put("name","GoLanguage is best");
            put("num_int","1");
            put("num_Int",2);
            put("num_Double",3);
            put("num_double","4");
            put("num_float","5.5");
            put("num_Float",6.6D);
            put("num_String1",6.6D);
            put("num_String2", 6L);
            put("num_String3", 6f);
        }};

        MyDTO myDTO = EsQueryHelper.parseObject(MyDTO.class, esSourceMap);
        System.err.println(myDTO);
    }

```

### 效果
```rst
MyDTO(name=GoLanguage is best, num_int=1, num_Int=2, num_Double=3.0, num_double=4.0, num_float=5.5, num_Float=6.6, num_String1=6.6, num_String2=6, num_String3=6.0)
```

## 几行代码就能生成DSL跟转换 类似mybatis的selective功能 (更轻更简单)


## pom坐标
```pom
        <dependency>
            <artifactId>pure-search-anno-parser</artifactId>
            <groupId>cool.yukai</groupId>
            <version>6.8.X-SNAPSHOT</version>
        </dependency>
```