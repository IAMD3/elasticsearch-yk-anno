package cool.yukai.builder.impl;


import com.google.auto.service.AutoService;
import cool.yukai.builder.FieldSetter;

import java.lang.reflect.Field;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2021/1/27 18:22
 **/
@AutoService(FieldSetter.class)
public class FieldValueSetterForNumber implements FieldSetter<Number> {

    @Override
    public void setVal(Field javaField, Number esValue, Object targetObj) throws Exception {
        Class<?> type = javaField.getType();

        if (Float.class == type) {
            javaField.set(targetObj, esValue.floatValue());
        } else if (Double.class == type) {
            javaField.set(targetObj, esValue.doubleValue());
        } else if (Long.class == type) {
            javaField.set(targetObj, esValue.longValue());
        } else if (Integer.class == type) {
            javaField.set(targetObj, esValue.intValue());
        } else if (Short.class == type) {
            javaField.set(targetObj, esValue.shortValue());
        } else if (Byte.class == type) {
            javaField.set(targetObj, esValue.byteValue());
        }else if(String.class == type){
            javaField.set(targetObj, esValue.toString());
        } else{
            javaField.set(targetObj,esValue);
        }

    }
}
