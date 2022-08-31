package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.DouyinOrder;
import com.honji.order.model.DaRenVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yao
 * @since 2022-08-25
 */
public interface DouyinOrderMapper extends BaseMapper<DouyinOrder> {

    @Select({"<script>",
            "SELECT do.sub_order_number, payment_method, goods_name, goods_id, quantity, price, ",
            " submit_time, finish_time, message, color, remark, status, cancel_reason, after_sales_status, ",
            " total_amount, freight, total_discount, platform_offers, merchant_offers, daren_offers, ",
            " change_price, payment_discount, fee, red_envelope, express_information, daren_id, daren_nick_name, ",
            " LEFT(created_time, 7) as created_time, LEFT(order_time, 7) as order_time, loan, commission ",
            " FROM douyin_order do WITH(nolock) ",
            " LEFT JOIN ",
            " (SELECT sub_order_number, max(created_time) as created_time, max(order_time) as order_time,",
            " SUM(total_amount + actually_freight + freight_subsidy + platform_subsidy + daren_subsidy  ",
            " + douyin_subsidy + marketing_subsidy + refund_amount) as loan,",
            " sum(commission) as commission ",
            " FROM fund_bill WHERE sub_order_number != '' ",
            " GROUP BY sub_order_number) fb ",
            " on do.sub_order_number = fb.sub_order_number ",
            " order by do.sub_order_number",
            "</script>"})
    List<DaRenVO> selectAll();
}
