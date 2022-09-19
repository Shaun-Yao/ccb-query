package com.honji.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
     * 其他原因描述
     */
    private String other;

    /**
     * 方案类型：常规、创新
     */
    private String proposalType;


    /**
     * 方案内容
     */
    private String proposal;


    /**
     * 反馈
     */
    private String feedback;

    /**
     * 方案确定
     */
    private String confirmation;

    /**
     * 开始时间节点
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String beginDate;

    /**
     * 结束时间节点
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String endDate;


    /**
     * 频率
     */
    private String frequency;

    /**
     * 执行人
     */
    private String executor;


    /**
     * 结果
     */
    private String result;

    /**
     * 0: 暂存 1: 提交通知
     */
//    private int state;

}
