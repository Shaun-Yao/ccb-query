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


    @Select({"<script>",
            "exec daily_sales_query '${depositDTO.begin}', '${depositDTO.end}', " +
            "'${depositDTO.depositBegin}', '${depositDTO.depositBegin}', '${depositDTO.shopCodeStr}'",
            "</script>"})
    List<DepositVO> testProc(@Param("depositDTO")DepositDTO depositDTO);


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
    List<DepositVO> selectByShopCodes(@Param("depositDTO")DepositDTO depositDTO);

    /**
     * >=, <= 要换为 &gt;= &lt;=
     * 合并销售单和退货单再分组统计为最终销售金额，退款yxyjje为负数
     * @return
     */
    @Select({"<script>",
            "SELECT deposit_result.*, amount_result.amount FROM" +
            "(SELECT erp_ls.khdm, balance_result.khmc, erp_ls.rq as date, \n" +
                    "balance_result.TotalAmount AS balance, deposit.cash_adjustment, \n" +
                    " 刷卡 as cardPay, \n" +
                    "广发兑换券 as cgbCoupon, 建行扫码 as ccbZs,  " +
                    "支付宝扫码 as alipay, 微信扫码 as wxpay, 扫一扫 as sys, 码上收 as mss, 百胜扫码 as bsPay, 商场代收款 as mallCollection,\n" +
                    "合胜收款 as heSheng, deposit.cash, \n" +
                    "储值卡消费 as cardConsumption, 会员积分 memberPoints,礼券 as giftCertificate,\n" +
                    "deposit.extra_cash, 悦扫码 as yuePay,\n" +
                    "bankName, deposit.deposit_date, deposit.deposit, deposit.image FROM( \n" +
                    "SELECT * FROM \n" +
                    "(SELECT VW_LSXHJS.DM1 AS KHDM, POSJS.JSMC,\n" +
                    "CONVERT(varchar(10), VW_LSXHJS.RQ, 23) RQ,sum(VW_LSXHJS.JE) JE FROM\n" +
                    "(select convert(varchar(10),DM1) DM1, RQ, JE, JSFS From IP180.SPERP.DBO.VW_LSXHJS)  VW_LSXHJS\n" +
                    "LEFT JOIN IP180.SPERP.DBO.BIZHONG KHJSBZ ON KEHU.JSHB = KHJSBZ.BZDM\n" +
                    "Left Join IP180.SPERP.DBO.POSJS POSJS on VW_LSXHJS.JSFS= POSJS.JSDM \n" +
                    "WHERE 1 = 1 " +
                    "<if test='depositDTO.begin !=null and depositDTO.begin!=\"\"'>",
                    "AND VW_LSXHJS.RQ &gt;= '${depositDTO.begin}'",
                    "</if>",
                    "<if test='depositDTO.end !=null and depositDTO.end!=\"\"'>",
                    "AND VW_LSXHJS.RQ &lt;= '${depositDTO.end}'",
                    "</if>",
                    "Group By VW_LSXHJS.DM1, POSJS.JSMC,VW_LSXHJS.RQ) result " +
                    "PIVOT (SUM(JE) for JSMC in (现金,刷卡,扫一扫,浦发扫码,建行扫码,建行离线,百胜扫码,码上收,储值卡消费,微信扫码,支付宝扫码, 礼券,会员积分,广发兑换券,商场代收款,合胜收款,悦扫码)) pm\t\t\t\t\t\t\t\n" +
                    ") erp_ls\n" +
                    "JOIN \n" +
                    "( SELECT deposit.*, bank.name AS bankName FROM dbo.daily_deposit deposit LEFT JOIN bank ON deposit.bank = bank.account ) deposit\n" +
                    "ON erp_ls.khdm = deposit.khdm and erp_ls.rq = deposit.date\n" +
                    "LEFT JOIN (\n" +
                    "     SELECT outt.*,(\n" +
                    "     SELECT SUM(ISNULL(cash, 0) - ISNULL(deposit, 0) + ISNULL(extra_cash, 0) + ISNULL(cash_adjustment, 0)) AS amount FROM balance_history inn \n" +
                    "                WHERE inn.khdm= outt.khdm \n" +
                    "                AND (inn.date > outt.date \n" +
                    "                OR ( inn.date = outt.date AND inn.RowNo &gt;= outt.RowNo )) \n" +
                    "                ) + ba.balance TotalAmount, ba.khmc  \n" +
                    "                FROM balance_history outt\n" +
                    "                JOIN cash_balance ba ON outt.khdm= ba.khdm \n" +
                    "                ) balance_result ON deposit.id = balance_result.id \n" +
                    "where deposit.khdm in " +
                    "<foreach item='item' index='index' collection='depositDTO.shopCodes' open='(' separator=',' close=')'> #{item} </foreach>",
                    "<if test='depositDTO.depositBegin !=null and depositDTO.depositBegin!=\"\"'>",
                    "AND deposit.deposit_date &gt;= '${depositDTO.depositBegin}'",
                    "</if>",
                    "<if test='depositDTO.depositEnd !=null and depositDTO.depositEnd!=\"\"'>",
                    "AND deposit.deposit_date &lt;= '${depositDTO.depositEnd}'",
                    "</if>",
                    ") deposit_result\n" +
                    "JOIN(" +
                            "SELECT khdm, CONVERT(varchar(10), rq, 120) as date, SUM(yxyjje) as amount FROM\n" +
                            "(\n" +
                            "\tSELECT dm1 as khdm, rq, yxyjje FROM IP180.SPERP.DBO.LSXHD xhd\n" +
                            "\tWHERE yxyjje > 0 " +
                            "<if test='depositDTO.begin !=null and depositDTO.begin!=\"\"'>",
                            "AND xhd.rq &gt;= '${depositDTO.begin}'",
                            "</if>",
                            "<if test='depositDTO.end !=null and depositDTO.end!=\"\"'>",
                            "AND xhd.rq &lt;= '${depositDTO.end}'",
                            "</if>",
                            "\tUNION ALL " +
                            "\tSELECT dm1 as khdm, rq, -yxyjje as yxyjje FROM IP180.SPERP.DBO.LSTHD thd\n" +
                            "\tWHERE yxyjje > 0 " +
                            "<if test='depositDTO.begin !=null and depositDTO.begin!=\"\"'>",
                            "AND thd.rq &gt;= '${depositDTO.begin}'",
                            "</if>",
                            "<if test='depositDTO.end !=null and depositDTO.end!=\"\"'>",
                            "AND thd.rq &lt;= '${depositDTO.end}'",
                            "</if>",
                            ") lsd GROUP BY khdm, rq\n" +
                    ")amount_result ",
                    "ON deposit_result.khdm = amount_result.khdm and deposit_result.date = amount_result.date",
//                    "ORDER BY deposit_result.date desc ",
            "</script>"})
    List<DepositVO> test(@Param("depositDTO")DepositDTO depositDTO);

}