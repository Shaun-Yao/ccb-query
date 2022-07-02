package com.honji.order.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class SalesPlanDetails extends IdEntity {


    /**
     * 主表id
     */
    private String planId;

    /**
     * 原因类型
     */
    private String reasonType;

    /**
     * 主要原因
     */
    private String primaryReason;


    /**
     * 具体原因分析
     */
    private String reason;

    /**
     * 常规方案
     */
    private String convention;


    /**
     * 创新方案
     */
    private String innovation;


    /**
     * 反馈
     */
    private String feedback;



    /**
     * 执行人
     */
    private String executor;


    /**
     * 结果
     */
    private String result;

}
