package cool.yukai.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2020/9/5 15:47
 **/
public class SearchGeneralInfoConstant {

    public static final String ES_SEARCH_AFTER_FIELD = "create_time";
    public static final String COMMA = ",";
    private static final String CLASS_NAME = "class";

    /**
     * 用于被移除的分页相关信息 -> 不能将这些字段映射成_分词格式的ES index fieldName
     */
    public static final List<String> PAGE_INFO_JAVA_FIELD_NAME = new ArrayList<String>() {{
        add("pageSize");
        add("totalRecord");
        add("pageIndex");
        add("totalPage");
        add("orderBy");
        add("desc");
        add("cursorMark");
        add("endPos");
        add("originalPageIndex");
        add("startPos");
        add("sorts");
        add("");

        add(CLASS_NAME);

        add(null);
    }};
}
