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


    private String khdm;

    private LocalDate date;

    private Integer amount;

    /**
     * 手续费
     */
    private double fee;

    /**
     * 订单号
     */
    private String num;

    /**
     * 付款类型 1是主扫 2是被扫
     */
    private int type;

    public CcbOrder(String khdm, LocalDate date, Integer amount, double fee, String num, int type) {
        this.khdm = khdm;
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.num = num;
        this.type = type;
    }
}
