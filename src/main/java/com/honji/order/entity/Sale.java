package com.honji.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2020-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Sale extends IdEntity {


    /**
     * 店铺代码
     */
    private String shopCode;

    /**
     * 营业日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * 营业额
     */
    private BigDecimal amount;


    /**
     * 现金
     */
    private BigDecimal cash;

    /**
     * 多收款金额
     */
    private BigDecimal extraCash;

    /**
     * 备注
     */
    private String remark;

    /**
     * 图片
     */
    private String image;

}
