package com.honji.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDTO {


    /**
     * 开始日期
     */
//    private String begin;

    /**
     * 结束日期
     */
//    private String end;

    /**
     * 年月
     */
    private String month;

    /**
     * 账单类型
     */
//    private BillTypeEnum billType;
//    @JsonProperty(value = "billTypes[]")
//    private String[] billTypes = new String[]{};
    private List<String> billTypes = new ArrayList<>();


    private int offset;

    private int limit;


}
