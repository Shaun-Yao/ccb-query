package com.honji.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;


@Data
public abstract class LongIdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //@TableId(type = IdType.AUTO)
    @TableId(type = IdType.ASSIGN_UUID)
    protected Long id;

}
