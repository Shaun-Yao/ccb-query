package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.CashBalance;
import com.honji.order.model.BalanceDTO;
import com.honji.order.model.BalanceVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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
            "-- 存款加上现金差异减去期初结余(8.31)等于实际应存回现金\n" +
            "SELECT ISNULL(deposit.amount, 0) + ISNULL(cd.amount, 0) - cb.balance as total\n" +
            "FROM cash_balance cb\n" +
            "LEFT JOIN \n" +
            "(SELECT shop_code, sum(amount) amount FROM deposit GROUP BY shop_code)\n" +
            "deposit ON cb.khdm = deposit.shop_code\n" +
            "LEFT JOIN \n" +
            "-- 现金差异\n" +
            "(SELECT shop_code, sum(amount - actual_amount) amount FROM cash_difference ",
            " WHERE date >= '2021-09-01' GROUP BY shop_code)cd \n" +
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


    //9.1号至查询日期收款现金减去实际应存回现金等于实时结余
    @Select({"<script>",
            "SELECT balance.shop_code, balance.shop_name, isnull(erp.现金, 0) - balance.total as total FROM(\n" +
            "-- 存款加上现金差异减去期初结余(8.31)等于实际应存回现金\n" +
            "SELECT cb.khdm as shop_code, cb.khmc as shop_name,\n" +
            "ISNULL(deposit.amount, 0) + ISNULL(cd.amount, 0) - cb.balance as total\n" +
            "FROM cash_balance cb\n" +
            "LEFT JOIN \n" +
            "(SELECT shop_code, sum(amount) amount FROM deposit \n" +
            "WHERE date BETWEEN '2021-09-01' and '${dto.date}' GROUP BY shop_code)deposit \n" +
            "ON cb.khdm = deposit.shop_code\n" +
            "LEFT JOIN \n" +
            "-- 现金差异\n" +
            "(SELECT shop_code, sum(amount - actual_amount) amount FROM cash_difference \n" +
            "WHERE date BETWEEN '2021-09-01' and '${dto.date}' GROUP BY shop_code)cd \n" +
            "ON cb.khdm = cd.shop_code\n" +
            "WHERE cb.khdm in ",
            "<foreach item='item' index='index' collection='dto.shopCodes' open='(' separator=',' close=')'> #{item} </foreach>",
            ") balance\n" +
            "left join\n" +
            "( SELECT * FROM\n" +
            "(\n" +
            "SELECT DM1 as shop_code, POSJS.JSMC, SUM ( VW_LSXHJS.JE ) JE  FROM\n" +
            "(SELECT CONVERT( VARCHAR (10), DM1 ) DM1, RQ, JE, JSFS  FROM IP180.SPERP.DBO.VW_LSXHJS \n" +
            " -- 期初结余是8.31，所以现金只统计9.1及以后的\n" +
            "WHERE dm1 in ",
            "<foreach item='item' index='index' collection='dto.shopCodes' open='(' separator=',' close=')'> #{item} </foreach>",
            " AND VW_LSXHJS.RQ BETWEEN '2021-09-01' and '${dto.date}'\n" +
            ") VW_LSXHJS\n" +
            "LEFT JOIN IP180.SPERP.DBO.POSJS POSJS ON VW_LSXHJS.JSFS= POSJS.JSDM \n" +
            "GROUP BY VW_LSXHJS.DM1, POSJS.JSMC\n" +
            ") result PIVOT (SUM ( JE ) FOR JSMC IN (现金)) pm \n" +
            ") erp on balance.shop_code = erp.shop_code\n",
            " ORDER BY balance.shop_code",
            "</script>"
    })
    List<BalanceVO> query(@Param("dto") BalanceDTO dto);
}
