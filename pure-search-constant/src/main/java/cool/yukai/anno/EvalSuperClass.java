package cool.yukai.anno;

import java.lang.annotation.*;

/**
 *标记 尝试解析superClass 字段
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EvalSuperClass {
}
