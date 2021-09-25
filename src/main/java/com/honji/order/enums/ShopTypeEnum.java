package com.honji.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ShopTypeEnum {

    PUBLIC( "公户"),
    PRIVATE( "私户")
    ;
    ShopTypeEnum(String desc) {
        this.desc = desc;
    }

    @EnumValue
    private final String desc;

    public static ShopTypeEnum findByDesc(final String desc){
        return Arrays.stream(values()).filter(value -> value.getDesc().equals(desc)).findFirst().orElse(null);
    }

}
