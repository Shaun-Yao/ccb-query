package com.honji.order.mapper;

import com.honji.order.entity.PrivateTerminal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface PrivateTerminalMapper extends BaseMapper<PrivateTerminal> {

    @Select({"<script>",
            "SELECT * FROM private_terminal",
            "<if test='search != null and search !=\"\"'>",
            " where khdm like '%' + #{search} + '%'",
            " or bs_pay like '%' + #{search} + '%'",
            " or bs_mss like '%' + #{search} + '%'",
            " or yue_pay like '%' + #{search} + '%'",
            " or union_sys like '%' + #{search} + '%'",
            " or union_pay like '%' + #{search} + '%'",
            " or top_up like '%' + #{search} + '%'",
            "</if>",
            " ORDER BY khdm ",
            "</script>"})
    List<PrivateTerminal> selectForIndex(String search);
}
