package com.honji.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum BillTypeEnum {

    CCB_SM("ccb-sm", "建行扫码"),
    CCB_LX("ccb-lx", "建行离线"),
    BS_PAY("bs-pay", "百胜支付"),
    BS_SYS("bs-sys", "扫一扫"),
    BS_MSS("bs-mss", "百胜码上收"),
    BS_SK("bs-sk", "百胜刷卡"),
    PF_SK("pf-sk", "浦发刷卡"),
    PF_SM("pf-sm", "浦发扫码"),
    UNION_PAY("union-pay", "银联刷卡"),
    HE_SHENG("he-sheng", "合胜收款"),
    YUE_PAY("yue-pay", "悦支付"),
    TOP_UP("top-up", "充值"),
    ALI_PAY_PUBLIC("ali-pay-public", "支付宝公户"),
    ALI_PAY_PRIVATE("ali-pay-private", "支付宝私户"),
    WX_PAY_PUBLIC("wx-pay-public", "微信支付公户"),
    WX_PAY_PRIVATE("wx-pay-private", "微信支付私户")
    ;
    BillTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @EnumValue
    private final String code;
    private final String desc;


//    @Override
//    public String toString() {
//        return this.code;
//    }
}
