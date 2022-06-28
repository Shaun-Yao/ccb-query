package com.honji.order.entity;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author yao
 * @since 2020-07-23
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
public class SalesPlanDetails extends IdEntity {


    /**
     * 原因类型
     */
    private String reasonType;

    /**
     * 原因
     */
    private String reason;


    /**
     * 原因
     */
    private String reasonAnalysis;

    /**
     * 常规方案
     */
    private String convention;


    /**
     * 创新方案
     */
    private String innovation;



}
