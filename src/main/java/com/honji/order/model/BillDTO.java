package com.honji.order.model;

import com.honji.order.enums.BillTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private BillTypeEnum billType;


    private int offset;

    private int limit;


}
