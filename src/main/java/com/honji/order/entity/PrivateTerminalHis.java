package com.honji.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * <p>
 * 私户终端号历史表
 * </p>
 *
 * @author yao
 * @since 2021-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PrivateTerminalHis extends IdEntity {

    /**
     * 店铺代码
     */
    private String khdm;

    /**
     * 百胜支付终端码
     */
    private String bsPay;

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

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;


}
