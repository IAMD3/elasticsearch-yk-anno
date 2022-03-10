package cool.yukai.util;


/**
 * @Author: Yukai
 * Description: master T
 * create time: 15/03/2021 16:10
 **/
public class ExceptUtil {

    public static RuntimeException fakeReturn(Throwable e) {
        if (e == null) {
            return new NullPointerException("t");
        }
        easyThrow(e);
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <EXCEPT extends Throwable> void easyThrow(Throwable e) throws EXCEPT {
        throw (EXCEPT) e;
    }



}
