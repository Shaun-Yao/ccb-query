package com.honji.order.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.honji.order.enums.DifferenceTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author yao
 * @since 2021-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CashDifference extends BaseEntity {


    /**
     * 店铺代码
     */
    private String shopCode;

    /**
     * 小票日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * 类型
     */
    private DifferenceTypeEnum type;

    /**
     * 小票金额
     */
    private BigDecimal amount;

    /**
     * 实收金额
     */
    private BigDecimal actualAmount;

    /**
     * 小票单号
     */
    private String number;

    /**
     * 小票单号 后4位
     */
    @TableField(exist = false)
    private String shortNumber;

    /**
     * 备注
     */
    private String remark;

    /**
     * 只返回小票单号后4位
     * @return
     */
    public String getShortNumber() {
        return number.substring(number.length() - 4);
    }


}
