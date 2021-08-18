package com.honji.order.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yao
 * @since 2021-07-31
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PrivateTerminal extends IdEntity {
    public PrivateTerminal(String shopCode, String bsPay,
                           String yuePay, String unionSys, String unionPay, String topUp) {
        this.khdm = shopCode;
        this.bsPay = bsPay;
        this.yuePay = yuePay;
        this.unionSys = unionSys;
        this.unionSys = unionSys;
        this.unionPay = unionPay;
        this.topUp = topUp;
    }

    /**
     * 店铺代码
     */
    private String khdm;

    /**
     * 百胜支付终端码
     */
    private String bsPay;

    /**
     * 码上收终端码
     */
//    private String bsMss;

    /**
     * 悦支付终端码
     */
    private String yuePay;

    /**
     * 银联扫一扫终端码
     */
    private String unionSys;

    /**
     * 银联刷卡
     */
    private String unionPay;

    /**
     * 充值终端码
     */
    private String topUp;


}
