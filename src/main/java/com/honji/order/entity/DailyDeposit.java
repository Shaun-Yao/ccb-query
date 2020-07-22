package com.honji.order.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author yao
 * @since 2020-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DailyDeposit extends IdEntity {


    /**
     * 店铺代码
     */
    private String khdm;

    /**
     * 交易日期
     */
    private LocalDate date;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * POS机/刷卡/银联扫码
     */
    private BigDecimal pos;

    /**
     * 广发兑换券
     */
    private BigDecimal cgbCoupon;

    /**
     * 建行主扫
     */
    private BigDecimal ccbZs;

    /**
     * 建行被扫
     */
    private BigDecimal ccbBs;

    /**
     * 支付宝
     */
    private BigDecimal alipay;

    /**
     * 微信
     */
    private BigDecimal wxpay;

    /**
     * 扫一扫
     */
    private BigDecimal sys;

    /**
     * 码上收
     */
    private BigDecimal mss;

    /**
     * 百胜支付
     */
    private BigDecimal bsPay;

    /**
     * 现金
     */
    private BigDecimal cash;

    /**
     * 会员积分/储值卡消费/礼券
     */
    private BigDecimal memberPoints;

    /**
     * 存款银行
     */
    private String bank;

    /**
     * 存款日期
     */
    private LocalDate depositDate;

    /**
     * 存款金额
     */
    private BigDecimal deposit;

    /**
     * 图片
     */
    private byte[] picture;

    /**
     * 多收款金额
     */
    private BigDecimal extraCash;


}
