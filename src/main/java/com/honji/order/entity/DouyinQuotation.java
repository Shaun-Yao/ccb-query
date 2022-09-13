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
 * @since 2022-08-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DouyinQuotation extends IdEntity {

    private static final long serialVersionUID = 1L;

    public DouyinQuotation(String goodsId, String goodsCategory, String adjustment, String inOutside, String manufacturer, String itemNumber,
                           String code, String description, String cost, String accessory, String operatingFee, String freight) {
        this.goodsId = goodsId;
        this.goodsCategory = goodsCategory;
        this.adjustment = adjustment;
        this.inOutside = inOutside;
        this.manufacturer = manufacturer;
        this.itemNumber = itemNumber;
        this.code = code;
        this.description = description;
        this.cost = cost;
        this.accessory = accessory;
        this.operatingFee = operatingFee;
        this.freight = freight;
    }

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 商品分类
     */
    private String goodsCategory;

    /**
     * 件数调整
     */
    private String adjustment;

    /**
     * 内外部说明
     */
    private String inOutside;

    /**
     * 厂家
     */
    private String manufacturer;

    /**
     * 款号
     */
    private String itemNumber;

    /**
     * 用友编码
     */
    private String code;

    /**
     * 供应商做账说明
     */
    private String description;

    /**
     * 商品成本
     */
    private String cost;

    /**
     * 辅料费
     */
    private String accessory;

    /**
     * 操作费
     */
    private String operatingFee;

    /**
     * 快递费
     */
    private String freight;


}
