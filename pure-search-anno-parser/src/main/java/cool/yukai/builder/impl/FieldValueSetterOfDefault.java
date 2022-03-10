package cool.yukai.builder.impl;

import com.google.auto.service.AutoService;
import cool.yukai.builder.FieldSetter;

import java.lang.reflect.Field;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2021/1/27 19:43
 **/
@AutoService(FieldSetter.class)
public class FieldValueSetterOfDefault implements FieldSetter<Object> {
    @Override
    public void setVal(Field javaField, Object esValue, Object targetObj) throws Exception {
        javaField.set(targetObj,esValue);
    }
}
