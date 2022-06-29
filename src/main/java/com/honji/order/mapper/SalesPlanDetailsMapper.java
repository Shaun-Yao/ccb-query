package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.SalesPlanDetails;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yao
 * @since 2021-07-31
 */
public interface SalesPlanDetailsMapper extends BaseMapper<SalesPlanDetails> {


    @Select({"<script>",
            "SELECT * FROM sales_plan_details where plan_id = '${planId}'",
            " ORDER BY date ",
            "</script>"})
    List<SalesPlanDetails> selectForIndex(String planId);
}
