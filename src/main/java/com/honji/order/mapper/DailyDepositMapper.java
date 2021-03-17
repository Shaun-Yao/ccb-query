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

    final static String querySql = " WHERE 1 = 1 ";

    @Select("select deposit.*, bank.name as bankName from dbo.daily_deposit deposit " +
            "LEFT JOIN bank on deposit.bank = bank.account where khdm = #{shopCode} ORDER BY date desc")
    List<DepositVO> selectByShopCode(@Param("shopCode") String shopCode);


    /**
     * >=, <= 要换为 &gt;= &lt;=
     * @return
     */
    @Select({"<script>",
            "SELECT deposit_result.*, amount_result.amount FROM\n" +
                    "(SELECT erp_ls.khdm, balance_result.khmc, erp_ls.rq AS DATE,\n" +
                    "balance_result.TotalAmount AS balance, deposit.cash_adjustment,\n" +
                    "刷卡 AS cardPay, 广发兑换券 AS cgbCoupon, 建行扫码 AS ccbZs,\n" +
                    "建行离线 AS ccbBs, 支付宝扫码 AS alipay, 微信扫码 AS wxpay, \n" +
                    "扫一扫 AS sys, 码上收 AS mss, 百胜扫码 AS bsPay, 商场代收款 AS mallCollection, 合胜收款 AS heSheng, deposit.cash, 储值卡消费 AS cardConsumption, \n" +
                    "会员积分 memberPoints,礼券 AS giftCertificate, deposit.extra_cash, \n" +
                    "悦扫码 AS yuePay, bank.name bankName, deposit.deposit_date, \n" +
                    "deposit.deposit, deposit.image \n" +
                    "FROM (\n" +
                    "\tSELECT * FROM(\n" +
                    "\t\tSELECT VW_LSXHJS.DM1 AS KHDM, POSJS.JSMC,\n" +
                    "\t\t\tCONVERT ( VARCHAR ( 10 ), VW_LSXHJS.RQ, 23 ) RQ,\n" +
                    "\t\t\tSUM ( VW_LSXHJS.JE ) JE FROM\n" +
                    "\t\t\t( SELECT CONVERT ( VARCHAR ( 10 ), DM1 ) DM1, RQ, JE, JSFS FROM IP180.SPERP.DBO.VW_LSXHJS " +
                    " WHERE VW_LSXHJS.RQ &gt;= '${depositDTO.begin}' AND VW_LSXHJS.dm1 IN " +
                    "<foreach item='item' index='index' collection='depositDTO.shopCodes' open='(' separator=',' close=')'> #{item} </foreach>",
                    "<if test='depositDTO.end !=null and depositDTO.end!=\"\"'>",
                    "AND VW_LSXHJS.RQ &lt;= '${depositDTO.end}'",
                    "</if>",
                    " ) VW_LSXHJS\n" +
                    "LEFT JOIN IP180.SPERP.DBO.POSJS POSJS ON VW_LSXHJS.JSFS= POSJS.JSDM \n" +
                    "\t\tGROUP BY VW_LSXHJS.DM1, POSJS.JSMC, VW_LSXHJS.RQ ) result \n" +
                    "\t\tPIVOT ( SUM (JE) FOR JSMC IN (现金,刷卡,扫一扫,浦发扫码,建行扫码,建行离线,百胜扫码,码上收,储值卡消费,微信扫码,支付宝扫码, 礼券,会员积分,广发兑换券,商场代收款,合胜收款,悦扫码)) pm \n" +
                    "\t) erp_ls\n" +
                    "\tJOIN daily_deposit deposit\n" +
                    "\tON erp_ls.khdm = deposit.khdm AND erp_ls.rq = deposit.date \n" +
                    "\tJOIN bank ON deposit.bank = bank.account \n" +
                    "\tLEFT JOIN (\n" +
                    "\tSELECT\n" +
                    "\t\toutt.*,(SELECT SUM(\n" +
                    "\t\t\tISNULL( cash, 0 ) - ISNULL( deposit, 0 ) + ISNULL( extra_cash, 0 ) + ISNULL( cash_adjustment, 0 )) AS amount \n" +
                    "\t\tFROM balance_history inn \n" +
                    "\t\tWHERE inn.khdm= outt.khdm \n" +
                    "\t\t\tAND ( inn.date > outt.date \n" +
                    "\t\t\tOR ( inn.date = outt.date AND inn.RowNo >= outt.RowNo )) \n" +
                    "\t\t) + ba.balance TotalAmount,\n" +
                    "\t\tba.khmc \n" +
                    "\tFROM balance_history outt\n" +
                    "\t\tJOIN cash_balance ba ON outt.khdm= ba.khdm \n" +
                    "\t\tWHERE outt.date &gt;= '${depositDTO.begin}' \n" +
                    "<if test='depositDTO.end !=null and depositDTO.end!=\"\"'>",
                    "AND outt.date &lt;= '${depositDTO.end}'",
                    "</if>",
                    "\t) balance_result ON deposit.id = balance_result.id \n" +
                    "\n" +
                    ") deposit_result\n" +
                    "JOIN (\n" +
                    "SELECT khdm, CONVERT(varchar(10), rq, 120) as date, SUM(yxyjje) as amount FROM\n" +
                    "(\n" +
                    "\tSELECT dm1 as khdm, rq, ISNULL(yxyjje, 0) as yxyjje FROM IP180.SPERP.DBO.LSXHD xhd\n" +
                    "\tUNION ALL\n" +
                    "\tSELECT dm1 as khdm, rq, ISNULL(-yxyjje, 0) as yxyjje FROM IP180.SPERP.DBO.LSTHD thd\n" +
                    ") lsd\n" +
                    "WHERE rq &gt;= '${depositDTO.begin}' and khdm in " +
                     "<foreach item='item' index='index' collection='depositDTO.shopCodes' open='(' separator=',' close=')'> #{item} </foreach>",
                    "<if test='depositDTO.end !=null and depositDTO.end!=\"\"'>",
                    "AND rq &lt;= '${depositDTO.end}'",
                    "</if>",
                    "GROUP BY khdm, rq\n" +
                    ")amount_result\n" +
                    "ON deposit_result.khdm = amount_result.khdm \n" +
                    "and deposit_result.date = amount_result.date\n" +
                    " WHERE 1 = 1 " +
                    "<if test='depositDTO.depositBegin !=null and depositDTO.depositBegin!=\"\"'>" +
                    "AND deposit_result.deposit_date &gt;= '${depositDTO.depositBegin}'" +
                    "</if>" +
                    "<if test='depositDTO.depositEnd !=null and depositDTO.depositEnd!=\"\"'>",
                    "AND deposit_result.deposit_date &lt;= '${depositDTO.depositEnd}'",
                    "</if>",
                    " ORDER BY deposit_result.date desc",
            "</script>"})
    List<DepositVO> test(@Param("depositDTO")DepositDTO depositDTO);


    @Select({"<script>",
            " SELECT balance_result.TotalAmount AS balance, balance_result.khmc, daily_deposit.* FROM " +
                    "( SELECT deposit.*, bank.name AS bankName FROM dbo.daily_deposit deposit LEFT JOIN bank ON deposit.bank = bank.account ) daily_deposit\n" +
                    "\tLEFT JOIN (\n" +
                    "\tSELECT outt.*,\n" +
                    "\t\t(\n" +
                    "\t\tSELECT SUM(ISNULL(cash, 0) - ISNULL(deposit, 0) + ISNULL(extra_cash, 0) + ISNULL(cash_adjustment, 0)) AS amount \n" +
                    "\t\tFROM balance_history inn \n" +
                    "\t\tWHERE inn.khdm= outt.khdm \n" +
                    "\t\t\tAND (inn.date &lt; outt.date \n" +
                    "\t\t\tOR ( inn.date = outt.date AND inn.RowNo &lt;= outt.RowNo )) \n" +
                    "\t\t) + ba.balance TotalAmount, ba.khmc  \n" +
                    "\tFROM balance_history outt\n" +
                    "\t\tJOIN cash_balance ba ON outt.khdm= ba.khdm \n" +
                    " where outt.date between '${depositDTO.begin}' and '${depositDTO.end}'" +
                    "\t) balance_result ON daily_deposit.id = balance_result.id " +
                    "\twhere daily_deposit.date between '${depositDTO.begin}' and '${depositDTO.end}' " +
                    " and daily_deposit.khdm in ",
            "<foreach item='item' index='index' collection='depositDTO.shopCodes' open='(' separator=',' close=')'> #{item} </foreach>",
            "<if test='depositDTO.depositBegin !=null and depositDTO.depositBegin!=\"\"'>",
            "AND daily_deposit.deposit_date &gt;= '${depositDTO.depositBegin}'",
            "</if>",
            "<if test='depositDTO.depositEnd !=null and depositDTO.depositEnd!=\"\"'>",
            "AND daily_deposit.deposit_date &lt;= '${depositDTO.depositEnd}'",
            "</if>",
            " ORDER BY daily_deposit.date desc ",
            "</script>"})
    List<DepositVO> selectByShopCodes(@Param("depositDTO")DepositDTO depositDTO);

}