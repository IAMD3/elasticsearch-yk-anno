package cool.yukai.builder;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * <p>
 * date : 2021/1/25
 * time : 19:25
 * </p>
 *
 * @author Master T
 */

@FunctionalInterface
public interface FieldSetter<T> {
    void setVal(Field javaField, T esValue, Object targetObj) throws Exception;

    default Class<T> fieldCls() {
        ParameterizedType type = (ParameterizedType)this.getClass().getGenericInterfaces()[0];
        return (Class<T>)type.getActualTypeArguments()[0];
    };
}
