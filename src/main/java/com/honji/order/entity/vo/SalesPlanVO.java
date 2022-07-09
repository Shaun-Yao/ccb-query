package com.honji.order.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.honji.order.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
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
public class SalesPlanVO extends IdEntity {

    /**
     * 创建人工号
     */
    private String jobNum;

    /**
     * 创建人名字
     */
    private String name;

    /**
     * 创建日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    /**
     * 销售区域
     */
    private String area;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 店铺编码
     */
    private String shopName;



    /**
     * 业绩分析时点：月份
     */
    private String performDate;


    /**
     * 对应分析时点业绩
     */
    private BigDecimal amounts;

    /**
     * 对应分析时年度业绩
     */
    private BigDecimal totalAmounts;



    /**
     * 同期月业绩,即去年同月
     */
    private BigDecimal lastYearMonthAmounts;


    /**
     * 对应分析时上一年业绩
     */
    private BigDecimal lastYearTotalAmounts;

    private BigDecimal monthDiff;
    private BigDecimal yearDiff;
    private String monthPercent;
    private String yearPercent;

}
