package com.honji.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DifferenceVO {

    private String id;


    /**
     * 店铺代码
     */
    private String shopCode;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 创建日期
     */

    private String createdTime;

    /**
     * 小票日期
     * 这里使用localdate类型会有类型转换问题
     */
    private String date;

    /**
     * 小票单号
     */
    private String number;

    /**
     * 小票金额
     */
    private BigDecimal amount;

    /**
     * 实收金额
     */
    private BigDecimal actualAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 差额
     */
    private BigDecimal difference;

    public BigDecimal getDifference(){
        return amount.subtract(actualAmount);
    }

}
