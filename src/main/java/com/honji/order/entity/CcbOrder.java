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
 * @since 2020-04-20
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CcbOrder extends IdEntity {


    private String terminalNum;

    private LocalDate date;

    private double amount;

    /**
     * 手续费
     */
    private double fee;

    /**
     * 银行流水号
     */
    private String num;

    /**
     * 银行流水号
     */
    private String orderId;

    /**
     * 付款类型 1是主扫 2是被扫
     */
    private int type;

    public CcbOrder(String terminalNum, LocalDate date, double amount, double fee,
                    String num, String orderId, int type) {
        this.terminalNum = terminalNum;
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.num = num;
        this.orderId = orderId;
        this.type = type;
    }
}
