package com.honji.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ShopTypeEnum {

    PUBLIC("public", "公户"),
    PRIVATE("private", "私户")
    ;
    ShopTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @EnumValue
    private final String code;
    private final String desc;


}
