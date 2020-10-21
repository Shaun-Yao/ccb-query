package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.DailyDeposit;
import com.honji.order.model.DepositDTO;
import com.honji.order.model.DepositVO;
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
    List<DepositVO> selectByShopCode(@Param("shopCode") String shopCode);

/*

    @Select(
            "select deposit.*, bank.name as bankName from dbo.daily_deposit deposit " +
                    "LEFT JOIN bank on deposit.bank = bank.account " +
                    "where khdm in (${shopCodes}) ORDER BY date desc" )
    List<DepositVO> selectByShopCodes(@Param("shopCodes") String shopCodes);
*/
/*

    @Select("SELECT result.*, balance.balance FROM \n" +
            "(\n" +
            "\tselect deposit.*, bank.name as bankName from dbo.daily_deposit deposit \n" +
            "\tLEFT JOIN bank on deposit.bank = bank.account where date = '${begin}'\n" +

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
            "\t ORDER BY date desc " )
    //date BETWEEN '${begin}' AND '${end}' and
    List<DepositVO> selectByShopCodes(@Param("shopCodes") String shopCodes,
                                      @Param("begin")String begin, @Param("end")String end);

*/

/*
    @Select({"<script>",
            " SELECT balance.balance,result.* FROM\n" +
                    "\t (\n" +
                    "\t\t SELECT\n" +
                    "\t\tdeposit.*,\n" +
                    "\t\tbank.name AS bankName \n" +
                    "\tFROM\n" +
                    "\t\tdbo.daily_deposit deposit \n" +
                    "\t\tLEFT JOIN bank ON deposit.bank = bank.account  \n" +
                    "\t) result \n" +
                    "\tLEFT JOIN  (\n" +
                    "\t\t SELECT out.khdm, out.date,\n" +
                    "cash_balance.balance + \n" +
                    "(\n" +
                    "\tSELECT SUM(ISNULL( cash, 0 ) - ISNULL( deposit, 0 ) + ISNULL( extra_cash, 0 ) + cash_adjustment) as amount FROM\n" +
                    "\t(\n" +
                    "\tSELECT  cash.khdm, cash.date, cash.cash, deposit.deposit, cash.extra_cash, cash.cash_adjustment\n" +
                    "\tFROM daily_deposit cash\n" +
                    "\tLEFT JOIN daily_deposit deposit \n" +
                    "\tON cash.khdm = deposit.khdm and cash.date = deposit.deposit_date\n" +
                    "\t) inn\n" +
                    "\tWHERE out.khdm = inn.khdm\n" +
                    "\tand out.date >= inn.date\n" +
                    ") as balance\n" +
                    "from daily_deposit out\n" +
                    "LEFT JOIN cash_balance ON out.khdm = cash_balance.khdm  \n" +
                    "\t) balance  ON result.khdm = balance.khdm AND result.date = balance.date" +
            "\twhere 1 = 1 and result.khdm in (${shopCodes}) ",
            "<if test='depositDTO.begin !=null and depositDTO.begin!=\"\"'>",
            "AND result.date &gt;= '${depositDTO.begin}'",
            "</if>",
            "<if test='depositDTO.end !=null and depositDTO.end!=\"\"'>",
            "AND result.date &lt;= '${depositDTO.end}'",
            "</if>",
            "<if test='depositDTO.depositBegin !=null and depositDTO.depositBegin!=\"\"'>",
            "AND result.deposit_date &gt;= '${depositDTO.depositBegin}'",
            "</if>",
            "<if test='depositDTO.depositEnd !=null and depositDTO.depositEnd!=\"\"'>",
            "AND result.deposit_date &lt;= '${depositDTO.depositEnd}'",
            "</if>",
            " ORDER BY result.date desc ",
            "</script>"})*/
@Select({"<script>",
        " SELECT balance_result.TotalAmount AS balance, daily_deposit.* FROM daily_deposit\n" +
                "\tLEFT JOIN (\n" +
                "\tSELECT outt.*,\n" +
                "\t\t(\n" +
                "\t\tSELECT SUM(ISNULL(cash, 0) - ISNULL(deposit, 0) + ISNULL(extra_cash, 0) + ISNULL(cash_adjustment, 0)) AS amount \n" +
                "\t\tFROM balance_history inn \n" +
                "\t\tWHERE inn.khdm= outt.khdm \n" +
                "\t\t\tAND (inn.date &lt; outt.date \n" +
                "\t\t\tOR ( inn.date = outt.date AND inn.RowNo &lt;= outt.RowNo )) \n" +
                "\t\t) + ba.balance TotalAmount \n" +
                "\tFROM balance_history outt\n" +
                "\t\tJOIN cash_balance ba ON outt.khdm= ba.khdm \n" +
                "\t) balance_result ON daily_deposit.id = balance_result.id " +
                "\twhere 1 = 1 and daily_deposit.khdm in (${shopCodes}) ",
        "<if test='depositDTO.begin !=null and depositDTO.begin!=\"\"'>",
        "AND daily_deposit.date &gt;= '${depositDTO.begin}'",
        "</if>",
        "<if test='depositDTO.end !=null and depositDTO.end!=\"\"'>",
        "AND daily_deposit.date &lt;= '${depositDTO.end}'",
        "</if>",
        "<if test='depositDTO.depositBegin !=null and depositDTO.depositBegin!=\"\"'>",
        "AND daily_deposit.deposit_date &gt;= '${depositDTO.depositBegin}'",
        "</if>",
        "<if test='depositDTO.depositEnd !=null and depositDTO.depositEnd!=\"\"'>",
        "AND daily_deposit.deposit_date &lt;= '${depositDTO.depositEnd}'",
        "</if>",
        " ORDER BY daily_deposit.date desc ",
        "</script>"})
    List<DepositVO> selectByShopCodes(@Param("shopCodes") String shopCodes, @Param("depositDTO")DepositDTO depositDTO);


}
