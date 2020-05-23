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
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BaiShengSwipe extends IdEntity {

    private static final long serialVersionUID = 1L;

    public BaiShengSwipe(LocalDate date, String time, double amount, double fee,
                         String terminalNum, String orderId, String merchant, int type) {
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.fee = fee;
        this.terminalNum = terminalNum;
        this.orderId = orderId;
        this.merchant = merchant;
        this.type = type;
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
     * 订单号
     */
    private String orderId;

    /**
     * 商户名称
     */
    private String merchant;

    /**
     * 类型 1.百胜刷卡 2.浦发刷卡 3.银联刷卡 4.充值
     */
    private int type;

}
