package cool.yukai.builder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.ServiceLoader;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2021/1/25 19:36
 **/

public class FieldSetterContext {


    public static final FieldSetterContext INSTANCE = new FieldSetterContext();
    private final HashMap<Class, FieldSetter> setterMapper = new HashMap<>();
    private FieldSetterContext() {
        register();
    }

    public void register() {
        Spliterator<FieldSetter> spliterator = ServiceLoader.load(FieldSetter.class, Thread.currentThread()
                .getContextClassLoader())
                .spliterator();
        StreamSupport.stream(spliterator, false)
                .forEach(setter -> setterMapper.put(setter.fieldCls(), setter));
    }

    public void setField(Field javaField, Object esValue, Object targetObj) throws Exception {
        if (esValue instanceof Number) {
            Number esValue_num = (Number) esValue;

            FieldSetter<Number> fieldSetter = setterMapper.get(Number.class);
            fieldSetter.setVal(javaField, esValue_num, targetObj);
        } else if (esValue instanceof String) {
            String esValue_str = (String) esValue;

            FieldSetter<String> fieldSetter = setterMapper.get(String.class);
            fieldSetter.setVal(javaField, esValue_str, targetObj);
        } else {
            //default setter
            FieldSetter fieldSetter = setterMapper.get(Object.class);
            fieldSetter.setVal(javaField, esValue, targetObj);
        }
    }
}
