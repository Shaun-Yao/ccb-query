package com.honji.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum BillTypeEnum {

    BS_PAY("bs-pay", "百胜支付"),
    BS_SYS("bs-sys", "百胜扫一扫"),
    BS_SK("bs-sk", "百胜刷卡"),
    YUE_PAY("yue-pay", "悦支付"),
    ALI_PAY_PUBLIC("ali-pay-public", "支付宝公户"),
    ALI_PAY_PRIVATE("ali-pay-private", "支付宝公户")
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
