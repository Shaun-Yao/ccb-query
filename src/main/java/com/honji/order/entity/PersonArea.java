package com.honji.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *  大区表
 * </p>
 *
 * @author yao
 * @since 2022-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("IP180.SPERP.dbo.person_area")
public class PersonArea extends IdEntity {

    /**
     * 大区名
     */
    private String jobNum;

    /**
     * 大区编码
     */
    private String areaCode;


}
