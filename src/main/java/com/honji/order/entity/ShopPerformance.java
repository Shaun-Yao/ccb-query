package com.honji.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
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
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
public class ShopPerformance extends IdEntity {

    /**
     * 创建人
     */
    private String creator;

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
    private String ShopCode;

    /**
     * 店铺名称
     */
    private String ShopName;

    /**
     * 业绩分析时点：月份
     */
    private String perform_date;


    /**
     * 对应分析时点业绩
     */
    private BigDecimal amounts;

    /**
     * 对应分析时年度业绩
     */
    private BigDecimal totalAmounts;


    /**
     * 业绩分析时点：月份
     */
//    @DateTimeFormat(pattern = "yyyy-MM")
//    @JsonFormat(pattern = "yyyy-MM")
//    private BigDecimal date;

    /**
     * 同期月业绩,即去年同月
     */
    private BigDecimal lastYearMonthAmounts;


    /**
     * 对应分析时上一年业绩
     */
    private BigDecimal lastYearTotalAmounts;

}
