package com.honji.order.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *  大区表
 * </p>
 *
 * @author yao
 * @since 2022-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Area extends IdEntity {


    /**
     * 大区编码
     */
    private String code;

    /**
     * 大区名
     */
    private String name;

    /**
     * 大区经理工号
     */
    private String jobNum;


}
