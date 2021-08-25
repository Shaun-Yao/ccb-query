package com.honji.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * <p>
 * 公户终端号历史表
 * </p>
 *
 * @author yao
 * @since 2021-08-12
 */
@Data
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PublicTerminalHis extends IdEntity {

    /**
     * 店铺代码
     */
    private String khdm;

    /**
     * 建行终端码
     */
    private String ccb;

    /**
     * 浦发刷卡或银联刷卡终端码
     */
    private String pos;

    /**
     * 充值终端码
     */
    private String topUp;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;


//    public PublicTerminalHis(String khdm, String ccb, String pos,
//                             String topUp, LocalDate createdDate) {
//        this.khdm = khdm;
//        this.ccb = ccb;
//        this.pos = pos;
//        this.topUp = topUp;
//        this.createdDate = createdDate;
//    }
}
