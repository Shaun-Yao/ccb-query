package com.honji.order.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 账单
 * </p>
 *
 * @author yao
 * @since 2021-01-05
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Bill extends IdEntity {

    private static final long serialVersionUID = 1L;

    public Bill(LocalDateTime date, double amount, double fee, String terminalNum,
                       String orderId, String merchant, String shopCode, String type) {
        this.date = date;
//        this.time = time;
        this.amount = amount;
        this.fee = fee;
        this.terminalNum = terminalNum;
        this.shopCode = shopCode;
        this.orderId = orderId;
        this.merchant = merchant;
        this.type = type;
    }

    /**
     * 交易日期
     */
    private LocalDateTime date;

    /**
     * 交易时间
     */
//    private String time;

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
     * 商户名称（选填）
     */
    private String merchant;

    /**
     * 店铺代码（选填）
     */
    private String shopCode;

    /**
     * 类型编码
     */
    private String type;


}
