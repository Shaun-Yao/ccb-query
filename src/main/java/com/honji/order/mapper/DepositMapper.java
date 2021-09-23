package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.Deposit;
import com.honji.order.entity.Sale;
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
 * @since 2021-08-31
 */
public interface DepositMapper extends BaseMapper<Deposit> {

    @Select({"<script>",
            "SELECT deposit.*, bank.name as bankName FROM deposit ",
            " LEFT JOIN bank on deposit.bank = bank.account",
            " where shop_code = '${shopCode}'",
            " ORDER BY date desc ",
            "</script>"})
    List<Deposit> selectForIndex(String shopCode);

    @Select({"<script>",
            "SELECT deposit_result.*, cb.khmc FROM (\n" +
                    "   SELECT erp_ls.khdm, erp_ls.rq AS DATE, deposit.id,\n" +
                    "   刷卡 AS cardPay, 广发兑换券 AS cgbCoupon, 建行扫码 AS ccbZs, 建行离线 AS ccbBs, 支付宝扫码 AS alipay, 微信扫码 AS wxpay, 扫一扫 AS sys, 码上收 AS mss, 百胜扫码 AS bsPay, 商场代收款 AS mallCollection, \n" +
                    " 合胜收款 AS heSheng, 现金 as cash, 储值卡消费 AS cardConsumption, 会员积分 memberPoints, \n" +
                    " 礼券 AS giftCertificate, 悦扫码 AS yuePay, 万达支付 AS wan_da,\n" +
                    "  bank.name bankName, deposit.date as deposit_date, deposit.amount as deposit, deposit.image \n" +
                    "   FROM (\n" +
                    "    SELECT * FROM (\n" +
                    "     SELECT VW_LSXHJS.DM1 AS KHDM, VW_LSXHJS.ysr AS KHMC, POSJS.JSMC,\n" +
                    "     CONVERT ( VARCHAR ( 10 ), VW_LSXHJS.RQ, 23 ) RQ, SUM ( VW_LSXHJS.JE ) JE \n" +
                    "     FROM (\n" +
                    "      SELECT CONVERT ( VARCHAR ( 10 ), DM1 ) DM1, YSR, RQ, JE, JSFS \n" +
                    "      FROM IP180.SPERP.DBO.VW_LSXHJS \n" +
                    "      WHERE VW_LSXHJS.RQ BETWEEN '${depositDTO.begin}' AND '${depositDTO.end}' \n" +
                    "       AND VW_LSXHJS.dm1 IN  \n" +
                    "<foreach item='item' index='index' collection='depositDTO.shopCodes' open='(' separator=',' close=')'> #{item} </foreach>",
                    "      ) VW_LSXHJS\n" +
                    "      LEFT JOIN IP180.SPERP.DBO.POSJS POSJS ON VW_LSXHJS.JSFS = POSJS.JSDM \n" +
                    "     GROUP BY VW_LSXHJS.DM1, YSR, POSJS.JSMC, VW_LSXHJS.RQ \n" +
                    "      ) result PIVOT (\n" +
                    "     SUM ( JE ) FOR JSMC IN (现金, 刷卡, 扫一扫, 浦发扫码, 建行扫码, 建行离线, 百胜扫码, 码上收, 储值卡消费, 微信扫码, 支付宝扫码, 礼券, 会员积分, 广发兑换券, 商场代收款, 合胜收款, 悦扫码, 万达支付 )) pm \n" +
                    "    ) erp_ls\n" +
                    "    LEFT JOIN deposit ON erp_ls.khdm = deposit.shop_code \n" +
                    "    AND erp_ls.rq = deposit.sale_date\n" +
                    "    LEFT JOIN bank ON deposit.bank = bank.account \n" +
                    "   ) deposit_result\n" +
                    "   LEFT JOIN cash_balance cb ON deposit_result.khdm = cb.khdm ",
                    " WHERE 1 = 1 " +
                    "<if test='depositDTO.depositBegin !=null and depositDTO.depositBegin!=\"\"'>" +
                    "AND deposit_result.deposit_date &gt;= '${depositDTO.depositBegin}'" +
                    "</if>" +
                    "<if test='depositDTO.depositEnd !=null and depositDTO.depositEnd!=\"\"'>",
                    "AND deposit_result.deposit_date &lt;= '${depositDTO.depositEnd}'",
                    "</if>",
                    " ORDER BY deposit_result.date desc",
            "</script>"})
    List<DepositVO> search(@Param("depositDTO") DepositDTO depositDTO);
}
