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
public class Bank extends IdEntity {


    /**
     * 账号
     */
    private String account;

    /**
     * 名称
     */
    private String name;

    /**
     * 到账编码
     */
    private String code;

    /**
     * 1.公户 2.私户
     */
    private Integer type;


    /**
     * 0禁用 1启用
     */
    private Integer enabled;


}
