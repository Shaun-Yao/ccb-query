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
 * @since 2021-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PublicTerminal extends IdEntity {

    /**
     * 店铺代码
     */
    private String khdm;

    /**
     * 建行终端码
     */
    private String ccb;

    /**
     * 浦发刷卡或银联刷卡终端码
     */
    private String pos;

    /**
     * 充值终端码
     */
    private String topUp;

    public PublicTerminal(String khdm, String ccb, String pos, String topUp) {
        this.khdm = khdm;
        this.ccb = ccb;
        this.pos = pos;
        this.topUp = topUp;
    }
}
