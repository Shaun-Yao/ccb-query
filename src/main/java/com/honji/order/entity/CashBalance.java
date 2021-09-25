package com.honji.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.honji.order.enums.ShopTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author yao
 * @since 2020-07-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CashBalance extends IdEntity {

    private String khdm;
    private String khmc;

    private Double balance;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 充值到账科目编码
     */
    private String czdzCode;

    /**
     * 充值未到账科目编码
     */
    private String czwdzCode;

    /**
     * 私户到账科目编码
     */
    private String shdzCode;

    /**
     * 私户未到账科目编码
     */
    private String shwdzCode;

    /**
     * 1.公户 2.私户
     */
    private ShopTypeEnum type;

}
