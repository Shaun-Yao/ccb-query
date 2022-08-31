package com.honji.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaRenVO {

    private String orderNumber;
    private String subOrderNumber;
    private String paymentMethod;

    private String goodsName;

    private String goodsId;

    private Integer quantity;

    private Double price;

    private String submitTime;

    private String finishTime;

    /**
     * 买家留言
     */
    private String message;

    private String color;

    /**
     * 商家备注
     */
    private String remark;

    /**
     * app渠道
     */
    private String appChannel;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 售后状态
     */
    private String afterSalesStatus;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 应付金额
     */
    private Double totalAmount;

    /**
     * 运费
     */
    private Double freight;

    /**
     * 优惠总金额
     */
    private Double totalDiscount;

    /**
     * 平台优惠
     */
    private String platformOffers;

    /**
     * 商家优惠
     */
    private String merchantOffers;

    /**
     * 达人优惠
     */
    private String darenOffers;

    /**
     * 商家改价
     */
    private Double changePrice;

    /**
     * 支付优惠
     */
    private Double paymentDiscount;

    /**
     * 手续费
     */
    private Double fee;

    /**
     * 红包抵扣
     */
    private Double redEnvelope;

    /**
     * 快递信息
     */
    private String expressInformation;

    /**
     * 达人id
     */
    private String darenId;

    /**
     * 达人昵称
     */
    private String darenNickName;

    /**
     * 广告渠道
     */
    private String adChannel;

    private String createdTime;
    private String orderTime;

    /**
     * 贷款
     */
    private double loan;

    /**
     * 佣金
     */
    private double commission;


}
