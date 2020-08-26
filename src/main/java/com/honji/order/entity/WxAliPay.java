package com.honji.order.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 微信支付宝账单
 * </p>
 *
 * @author yao
 * @since 2020-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class WxAliPay extends IdEntity {

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

    /**
     * 账单类型 1.微信公户 2.微信私户 3.支付宝公户 4.支付宝私户
     */
    private int type;


    public WxAliPay(LocalDate date, double amount, double fee,
                    String orderId, String khdm, int type) {
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.orderId = orderId;
        this.khdm = khdm;
        this.type = type;
    }
}
