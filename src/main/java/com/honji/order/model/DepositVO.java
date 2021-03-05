package com.honji.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositVO {

    private String id;



    /**
     * 店铺代码
     */
    private String khdm;

    /**
     * 店铺名称
     */
    private String khmc;

    /**
     * 交易日期
     * 这里使用localdate类型会有类型转换问题
     */
    private String date;

    /**
     * 交易金额
     */
    private double amount;

    /**
     * 刷卡
     */
    private double cardPay;

    /**
     * 银联扫码
     */
//    private double unionPay;

    /**
     * 广发兑换券
     */
    private double cgbCoupon;

    /**
     * 建行主扫
     */
    private double ccbZs;

    /**
     * 建行被扫
     */
    private double ccbBs;

    /**
     * 支付宝
     */
    private double alipay;

    /**
     * 微信
     */
    private double wxpay;

    /**
     * 扫一扫
     */
    private double sys;

    /**
     * 码上收
     */
    private double mss;

    /**
     * 百胜支付
     */
    private double bsPay;

    /**
     * 商场代收款
     */
    private double mallCollection;

    /**
     * 合胜收款
     */
    private double heSheng;

    /**
     * 现金
     */
    private double cash;

    /**
     * 储值卡消费
     */
    private double cardConsumption;

    /**
     * 会员积分
     */
    private double memberPoints;

    /**
     * 礼券
     */
    private double giftCertificate;

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
    private double deposit;

    /**
     * 悦支付
     */
    private double yuePay;

    /**
     * 多收款金额
     */
    private double extraCash;

    private String bankName;

    private String image;

    /**
     * 结余
     */
    private double balance;

    /**
     * 现金调整金额
     */
    private double cashAdjustment;

}
