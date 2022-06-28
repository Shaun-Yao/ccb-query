package com.honji.order.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

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
public class SalesPlan extends IdEntity {

    private ShopPerformance shopPerformance;

    /**
     * 账号
     */
    private List<SalesPlanDetails> details;



}
