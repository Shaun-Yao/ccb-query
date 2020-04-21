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

    private Integer payAmount;

    private Integer refundAmount;

    public CcbOrder(String khdm, LocalDate date, Integer payAmount, Integer refundAmount) {
        this.khdm = khdm;
        this.date = date;
        this.payAmount = payAmount;
        this.refundAmount = refundAmount;
    }
}
