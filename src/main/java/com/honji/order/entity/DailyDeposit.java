package com.honji.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
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
     * 商场代收款
     */
    private BigDecimal mallCollection;

    /**
     * 合胜收款
     */
    private BigDecimal heSheng;

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

    /*更新值为null会报错，加上dbcType.DATE解决*/
    @TableField(jdbcType = JdbcType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate depositDate;

    /**
     * 存款金额
     */
    private BigDecimal deposit;


    /**
     * 多收款金额
     */
    private BigDecimal extraCash;

    /**
     * 现金调整金额，默认0
     */
    private BigDecimal cashAdjustment =  BigDecimal.ZERO;

    /**
     * 图片
     */
    private String image;

}
