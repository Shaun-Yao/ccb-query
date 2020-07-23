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
public class DirectShop extends IdEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺代码
     */
    private String shopCode;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 1.公户 2.私户
     */
    private Integer type;


}
