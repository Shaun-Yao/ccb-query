package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.CashBalance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yao
 * @since 2020-07-23
 */
public interface CashBalanceMapper extends BaseMapper<CashBalance> {


    //9.1号至今收款现金减去实际应存回现金等于实时结余
    @Select({"SELECT erp.total - balance.total as total FROM(\n" +
            "-- 8.31 期初结余减去存款再减去现金差异等于实际应存回现金\n" +
            "SELECT ISNULL(deposit.amount, 0) - ISNULL(cd.amount, 0) - cb.balance as total\n" +
            "FROM cash_balance cb\n" +
            "LEFT JOIN \n" +
            "(SELECT shop_code, sum(amount) amount FROM deposit GROUP BY shop_code)\n" +
            "deposit ON cb.khdm = deposit.shop_code\n" +
            "LEFT JOIN \n" +
            "-- 现金差异\n" +
            "(SELECT shop_code, sum(amount - actual_amount) amount FROM cash_difference GROUP BY shop_code)cd \n" +
            "ON cb.khdm = cd.shop_code\n" +
            "WHERE cb.khdm = '${shopCode}'\n" +
            ") balance\n" +
            "join\n" +
            "( SELECT SUM(现金) total FROM\n" +
            "(\n" +
            "SELECT POSJS.JSMC, SUM ( VW_LSXHJS.JE ) JE  FROM\n" +
            "(SELECT CONVERT( VARCHAR ( 10 ), DM1 ) DM1, RQ, JE, JSFS  FROM IP180.SPERP.DBO.VW_LSXHJS \n" +
            " -- 期初结余是8.31，所以现金只统计9.1及以后的\n" +
            "WHERE dm1 = '${shopCode}' AND VW_LSXHJS.RQ >= '2021-09-01' \n" +
            ") VW_LSXHJS\n" +
            "LEFT JOIN IP180.SPERP.DBO.POSJS POSJS ON VW_LSXHJS.JSFS= POSJS.JSDM \n" +
            "GROUP BY VW_LSXHJS.DM1, POSJS.JSMC, VW_LSXHJS.RQ \n" +
            ") result PIVOT (SUM ( JE ) FOR JSMC IN (现金)) pm \n" +
            ") erp on 1 = 1"})
    double calBalance(@Param("shopCode") String shopCode);

}
