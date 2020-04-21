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
 * @since 2020-04-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CcbPos extends IdEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 柜台号
     */
    private String posId;

    /**
     * 客户代码
     */
    private String khdm;


}
