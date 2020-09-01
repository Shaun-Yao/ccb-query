package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.DailyDeposit;
import com.honji.order.model.DepositVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yao
 * @since 2020-07-20
 */
public interface DailyDepositMapper extends BaseMapper<DailyDeposit> {

    @Select("select deposit.*, bank.name as bankName from dbo.daily_deposit deposit " +
            "LEFT JOIN bank on deposit.bank = bank.account where khdm = #{shopCode} ORDER BY date desc")
    List<DepositVo> selectByShopCode(@Param("shopCode") String shopCode);

/*

    @Select(
            "select deposit.*, bank.name as bankName from dbo.daily_deposit deposit " +
                    "LEFT JOIN bank on deposit.bank = bank.account " +
                    "where khdm in (${shopCodes}) ORDER BY date desc" )
    List<DepositVo> selectByShopCodes(@Param("shopCodes") String shopCodes);
*/

    @Select("SELECT result.*, balance.balance FROM \n" +
            "(\n" +
            "\tselect deposit.*, bank.name as bankName from dbo.daily_deposit deposit \n" +
            "\tLEFT JOIN bank on deposit.bank = bank.account\n" +

            ") result\n" +
            "LEFT JOIN\n" +
            "(\n" +
            "\tSELECT balance.khdm, balance.amount + ISNULL(deposit.amount, 0) as \t    balance FROM\n" +
            "  (\n" +
            "\tSELECT khdm, balance as amount FROM cash_balance balance\n" +
            "\t) balance\n" +
            "\tLEFT JOIN\n" +
            "\t(\n" +
            "\tSELECT deposit.khdm, sum(ISNULL(cash, 0) - ISNULL(deposit, 0) + ISNULL(extra_cash, 0)) as amount FROM daily_deposit deposit \n" +
            "\tLEFT JOIN cash_balance balance \n" +
            "\tON deposit.khdm = balance.khdm \n" +
            "\tWHERE deposit.date >= balance.date\n" +
            "\tGROUP BY deposit.khdm\n" +
            "\t) deposit\n" +
            "\tON balance.khdm = deposit.khdm\n" +
            ") balance\n" +
            "on result.khdm = balance.khdm\n" +
            "\twhere result.khdm in (${shopCodes}) ORDER BY date desc " )
    List<DepositVo> selectByShopCodes(@Param("shopCodes") String shopCodes);

}
