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

}
