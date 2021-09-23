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

    /**
     * 查询当前结余
     * @param shopCode
     * @return
     */
    @Select("SELECT balance.amount + ISNULL(deposit.amount, 0) as balance FROM\n" +
            "(\n" +
            "SELECT balance as amount FROM cash_balance balance WHERE balance.khdm = #{shopCode}\n" +
            ") balance\n" +
            "LEFT JOIN\n" +
            "(\n" +
            "SELECT sum(ISNULL(cash, 0) - ISNULL(deposit, 0) + ISNULL(extra_cash, 0)+ ISNULL(cash_adjustment, 0)) as amount " +
            "FROM daily_deposit deposit \n" +
            "LEFT JOIN cash_balance balance \n" +
            "ON deposit.khdm = balance.khdm \n" +
            "WHERE deposit.khdm = #{shopCode} AND deposit.date >= balance.date\n" +
            "GROUP BY deposit.khdm\n" +
            ") deposit\n" +
            "ON 1 = 1")
    double calBalance(@Param("shopCode") String shopCode);

    @Select({"SELECT erp.amount - deposit.amount as amount FROM \n" ,
            "( SELECT sum(现金) amount FROM \n" ,
            "\t(SELECT POSJS.JSMC, sum(VW_LSXHJS.JE) JE FROM\n" ,
            "\t(select convert(varchar(10),DM1) DM1, RQ, JE, JSFS From IP180.SPERP.DBO.VW_LSXHJS" ,
            "\tWHERE dm1 = '${shopCode}' and VW_LSXHJS.RQ >= '2021-09-01')  VW_LSXHJS\n" ,
            "\tLeft Join IP180.SPERP.DBO.POSJS POSJS on VW_LSXHJS.JSFS= POSJS.JSDM   \n" ,
            "\tGroup By VW_LSXHJS.DM1, POSJS.JSMC,VW_LSXHJS.RQ\n" ,
            "\t) result " ,
            "\tPIVOT (SUM(JE) for JSMC in (现金)) pm\n" ,
            ") erp\n" ,
            "JOIN (SELECT isnull(SUM(case when 1=1 then amount else 0 end), 0) amount FROM deposit WHERE shop_code = '${shopCode}'\n" ,
            ") deposit ON 1 = 1"})
    double calBalance2(@Param("shopCode") String shopCode);

}
