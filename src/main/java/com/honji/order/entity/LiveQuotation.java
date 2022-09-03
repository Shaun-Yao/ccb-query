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
 * @since 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LiveQuotation extends IdEntity {

    private static final long serialVersionUID = 1L;

    public LiveQuotation(String shop, String goodsId, String price, String totalCommission,
                         String onlineCommission, String offlineCommission, String status) {
        this.shop = shop;
        this.goodsId = goodsId;
        this.price = price;
        this.totalCommission = totalCommission;
        this.onlineCommission = onlineCommission;
        this.offlineCommission = offlineCommission;
        this.status = status;
    }

    /**
     * 店铺
     */
    private String shop;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 售价
     */
    private String price;

    /**
     * 佣金
     */
    private String totalCommission;

    /**
     * 线上佣金
     */
    private String onlineCommission;

    /**
     * 线下佣金
     */
    private String offlineCommission;

    /**
     * 状态
     */
    private String status;


}
