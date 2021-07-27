package com.honji.order.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public abstract class BaseEntity extends IdEntity {

    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    protected LocalDateTime createdTime;

    @TableField(fill = FieldFill.UPDATE)
    protected LocalDateTime updatedTime;
}
