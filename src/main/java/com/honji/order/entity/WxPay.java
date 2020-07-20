package com.honji.order.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author yao
 * @since 2020-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class WxPay extends IdEntity {

    private LocalDate date;

    private double amount;

    /**
     * 手续费
     */
    private double fee;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 客户代码
     */
    private String khdm;


    public WxPay(LocalDate date, double amount, double fee, String orderId, String khdm) {
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.orderId = orderId;
        this.khdm = khdm;
    }
}
