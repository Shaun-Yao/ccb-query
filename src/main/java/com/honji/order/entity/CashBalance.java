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
 * @since 2020-07-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CashBalance extends IdEntity {

    private String khdm;

    private Double balance;

    private LocalDate date;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 1.公户 2.私户
     */
    private Integer type;

}
