package com.honji.order.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author yao
 * @since 2020-05-05
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BaiShengPay extends IdEntity {

    private static final long serialVersionUID = 1L;

    public BaiShengPay(LocalDate date, String time, double amount, double fee, String terminalNum, String orderId, String merchant) {
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.fee = fee;
        this.terminalNum = terminalNum;
        this.orderId = orderId;
        this.merchant = merchant;
    }

    /**
     * 交易日期

     */
    private LocalDate date;

    /**
     * 交易时间
     */
    private String time;

    /**
     * 交易金额
     */
    private double amount;

    /**
     * 手续费
     */
    private double fee;

    /**
     * 终端号
     */
    private String terminalNum;

    /**
     * 银商订单号
     */
    private String orderId;

    /**
     * 商户名称
     */
    private String merchant;


}
