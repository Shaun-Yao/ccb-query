package com.honji.order.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *  店铺表
 * </p>
 *
 * @author yao
 * @since 2022-07-04
 */
@Data
@Accessors(chain = true)
public class Shop  {


    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;



}
