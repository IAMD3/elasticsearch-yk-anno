package cool.yukai.builder.impl;

import com.google.auto.service.AutoService;
import cool.yukai.builder.FieldSetter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2021/1/27 14:51
 **/
@AutoService(FieldSetter.class)
public class FieldValueSetterForString implements FieldSetter<String> {

    @Override
    public void setVal(Field javaField, String esValue, Object targetObj) throws Exception {
        Class<?> type = toPackagedCls(javaField.getType());
        //constructor with a string type input
        Constructor<?> constructor = type.getConstructor(fieldCls());
        Object runTimeFieldVal = constructor.newInstance(esValue);

        javaField.set(targetObj, runTimeFieldVal);
    }


    private Class toPackagedCls(Class primitiveCls) {
        if (primitiveCls == byte.class) {
            return Byte.class;
        }
        if (primitiveCls == int.class) {
            return Integer.class;
        }
        if (primitiveCls == long.class) {
            return Long.class;
        }
        if (primitiveCls == short.class) {
            return Short.class;
        }
        if (primitiveCls == float.class) {
            return Float.class;
        }
        if (primitiveCls == double.class) {
            return Double.class;
        }
        return primitiveCls;
    }

}
