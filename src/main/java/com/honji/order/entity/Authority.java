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
 * @since 2020-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Authority extends IdEntity {


    private String jobNum;

    private String khdm;

    private String deptNum;

    private String khmc;

    private String name;

    /**
     * 凭证号，财务人员需要填写，督导不需要
     */
    private Integer certificate;

    /**
     * 1财务，2督导，3文员
     */
    private Integer type;


}
