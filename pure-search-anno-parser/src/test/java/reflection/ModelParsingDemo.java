package reflection;

import cool.yukai.util.EsQueryHelper;
import org.junit.Test;
import reflection.dto.MyDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2021/1/28 10:58
 **/
public class ModelParsingDemo {

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


}
