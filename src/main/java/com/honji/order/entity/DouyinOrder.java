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
 * @since 2022-08-25
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DouyinOrder extends IdEntity {

    private static final long serialVersionUID = 1L;

    public DouyinOrder(String orderNumber, String subOrderNumber, String paymentMethod, String goodsName, String goodsId,
                       Integer quantity, Double price, String submitTime, String finishTime,
                       String message, String color, String remark, String appChannel, String status,
                       String cancelReason, String afterSalesStatus, String orderType, Double totalAmount,
                       Double freight, Double totalDiscount, String platformOffers, String merchantOffers,
                       String darenOffers, Double changePrice, Double paymentDiscount, Double fee, Double redEnvelope,
                       String expressInformation, String darenId, String darenNickName, String adChannel) {
        this.orderNumber = orderNumber;
        this.subOrderNumber = subOrderNumber;
        this.paymentMethod = paymentMethod;
        this.goodsName = goodsName;
        this.goodsId = goodsId;
        this.quantity = quantity;
        this.price = price;
        this.submitTime = submitTime;
        this.finishTime = finishTime;
        this.message = message;
        this.color = color;
        this.remark = remark;
        this.appChannel = appChannel;
        this.status = status;
        this.cancelReason = cancelReason;
        this.afterSalesStatus = afterSalesStatus;
        this.orderType = orderType;
        this.totalAmount = totalAmount;
        this.freight = freight;
        this.totalDiscount = totalDiscount;
        this.platformOffers = platformOffers;
        this.merchantOffers = merchantOffers;
        this.darenOffers = darenOffers;
        this.changePrice = changePrice;
        this.paymentDiscount = paymentDiscount;
        this.fee = fee;
        this.redEnvelope = redEnvelope;
        this.expressInformation = expressInformation;
        this.darenId = darenId;
        this.darenNickName = darenNickName;
        this.adChannel = adChannel;
    }


    public DouyinOrder(String orderNumber, String subOrderNumber, String goodsName, String goodsId, String submitTime,
                       String finishTime, String status, double totalAmount, double freight, String orderType,
                       String expressInformation, String darenId, String darenNickName, int quantity, double price) {
        this.orderNumber = orderNumber;
        this.subOrderNumber = subOrderNumber;
        this.goodsName = goodsName;
        this.goodsId = goodsId;
        this.submitTime = submitTime;
        this.finishTime = finishTime;
        this.status = status;
        this.totalAmount = totalAmount;
        this.freight = freight;
        this.orderType = orderType;
        this.expressInformation = expressInformation;
        this.darenId = darenId;
        this.darenNickName = darenNickName;
        this.quantity = quantity;
        this.price = price;
    }

    private String orderNumber;

    private String subOrderNumber;

    /**
     * 支付方式
     */
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


}
