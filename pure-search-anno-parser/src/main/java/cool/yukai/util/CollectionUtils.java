package cool.yukai.util;

import java.util.Collection;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2022/3/10 14:35
 **/
public class CollectionUtils {

    public static boolean isEmpty(Collection c) {
        if (c == null) return true;
        if (c.isEmpty()) return true;
        return false;
    }
}
