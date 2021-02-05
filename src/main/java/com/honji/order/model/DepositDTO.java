package com.honji.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositDTO {


    /**
     * 工号
     */
    private String jobNum;

    /**
     * 门店代码
     * bootstrpa-table 传数组参数没有加上数组index，加上traditional:true 也无效
     */
    private List<String> shopCodes ;

    /**
     * 营业开始日期
     */
    private String begin;

    /**
     * 营业结束日期
     */
    private String end;

    /**
     * 存款开始日期
     */
    private String depositBegin;

    /**
     * 存款结束日期
     */
    private String depositEnd;

    private int offset;

    private int limit;


}
