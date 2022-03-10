package reflection.dto;

import cool.yukai.anno.ESField;
import lombok.ToString;

/**
 * @Author: Yukai
 * Description: master T
 * create time: 2021/1/28 10:53
 **/
@ToString
public class MyDTO {
    @ESField(esFieldName = "name")
    private String name;
    @ESField(esFieldName = "num_int")
    private int num_int;
    @ESField(esFieldName = "num_Int")
    private Integer num_Int;
    @ESField(esFieldName = "num_Double")
    private Double num_Double;
    @ESField(esFieldName = "num_double")
    private double num_double;
    @ESField(esFieldName = "num_float")
    private float num_float;
    @ESField(esFieldName = "num_Float")
    private Float num_Float;
    @ESField(esFieldName = "num_String1")
    private String num_String1;
    @ESField(esFieldName = "num_String2")
    private String num_String2;
    @ESField(esFieldName = "num_String3")
    private String num_String3;


}
