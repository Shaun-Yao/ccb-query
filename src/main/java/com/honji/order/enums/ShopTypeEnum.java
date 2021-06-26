package com.honji.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ShopTypeEnum {

    CCB_SM("public", "公户"),
    CCB_LX("private", "私户")
    ;
    ShopTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @EnumValue
    private final String code;
    private final String desc;


}
