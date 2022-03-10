package cool.yukai.sort;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2022/3/4 10:31
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sort {
    private String fieldName;
    private Boolean asc = true;
}
