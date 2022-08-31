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
 * @since 2022-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FundBill extends IdEntity {

    private static final long serialVersionUID = 1L;

    public FundBill(String createdTime, String serialNumber, String direction, Double orderAmount,
                    String account, String summary, String billType, String subOrderNumber, String orderNumber,
                    String afterSalesNumber, String orderTime, String goodsId, String orderType,
                    Double totalAmount, Double actuallyFreight, Double freightSubsidy, Double platformSubsidy,
                    Double darenSubsidy, Double douyinSubsidy, Double marketingSubsidy, Double refundAmount,
                    Double serviceCharge, Double commission, Double channelCommission, Double investmentServiceFee,
                    Double promotionFee, Double otherCommission, String remark) {
        this.createdTime = createdTime;
        this.serialNumber = serialNumber;
        this.direction = direction;
        this.orderAmount = orderAmount;
        this.account = account;
        this.summary = summary;
        this.billType = billType;
        this.subOrderNumber = subOrderNumber;
        this.orderNumber = orderNumber;
        this.afterSalesNumber = afterSalesNumber;
        this.orderTime = orderTime;
        this.goodsId = goodsId;
        this.orderType = orderType;
        this.totalAmount = totalAmount;
        this.actuallyFreight = actuallyFreight;
        this.freightSubsidy = freightSubsidy;
        this.platformSubsidy = platformSubsidy;
        this.darenSubsidy = darenSubsidy;
        this.douyinSubsidy = douyinSubsidy;
        this.marketingSubsidy = marketingSubsidy;
        this.refundAmount = refundAmount;
        this.serviceCharge = serviceCharge;
        this.commission = commission;
        this.channelCommission = channelCommission;
        this.investmentServiceFee = investmentServiceFee;
        this.promotionFee = promotionFee;
        this.otherCommission = otherCommission;
        this.remark = remark;
    }

    /**
     * 动账时间
     */
    private String createdTime;

    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 动账方向即账单类型
     */
    private String direction;

    /**
     * 动账金额
     */
    private Double orderAmount;

    /**
     * 动账账户
     */
    private String account;

    /**
     * 动账摘要
     */
    private String summary;

    /**
     * 计费类型
     */
    private String billType;

    /**
     * 子订单号
     */
    private String subOrderNumber;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 售后编号
     */
    private String afterSalesNumber;

    /**
     * 下单时间
     */
    private String orderTime;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 订单实付应结
     */
    private Double totalAmount;

    /**
     * 实付运费
     */
    private Double actuallyFreight;

    /**
     * 平台运费补贴
     */
    private Double freightSubsidy;

    /**
     * 平台补贴
     */
    private Double platformSubsidy;

    /**
     * 达人补贴
     */
    private Double darenSubsidy;

    /**
     * 抖音补贴
     */
    private Double douyinSubsidy;

    /**
     * 实际DOU分期营销补贴
     */
    private Double marketingSubsidy;

    /**
     * 退款金额
     */
    private Double refundAmount;

    /**
     * 服务费
     */
    private Double serviceCharge;

    /**
     * 佣金
     */
    private Double commission;

    /**
     * 渠道分成
     */
    private Double channelCommission;

    /**
     * 招商服务费
     */
    private Double investmentServiceFee;

    /**
     * 推广费
     */
    private Double promotionFee;

    /**
     * 其它分成
     */
    private Double otherCommission;

    /**
     * 备注
     */
    private String remark;


}
