package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.PrivateTerminalHis;
import com.honji.order.entity.Sale;
import com.honji.order.model.TerminalDTO;
import org.apache.ibatis.annotations.Param;
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
public interface SaleMapper extends BaseMapper<Sale> {

    @Select({"<script>",
            "SELECT * FROM sale where shop_code = '${shopCode}'",
            " ORDER BY date desc ",
            "</script>"})
    List<Sale> selectForIndex(String shopCode);
}
