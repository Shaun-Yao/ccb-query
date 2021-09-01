package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.Deposit;
import com.honji.order.entity.Sale;
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
            "SELECT * FROM deposit where shop_code = '${shopCode}'",
            " ORDER BY date desc ",
            "</script>"})
    List<Deposit> selectForIndex(String shopCode);
}
