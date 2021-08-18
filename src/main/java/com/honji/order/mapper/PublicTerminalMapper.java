package com.honji.order.mapper;

import com.honji.order.entity.PrivateTerminal;
import com.honji.order.entity.PublicTerminal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yao
 * @since 2021-08-12
 */
public interface PublicTerminalMapper extends BaseMapper<PublicTerminal> {

    @Select({"<script>",
            "SELECT * FROM public_terminal",
            "<if test='search != null and search !=\"\"'>",
            " where khdm like '%' + #{search} + '%'",
            " or ccb like '%' + #{search} + '%'",
            " or pos like '%' + #{search} + '%'",
            " or top_up like '%' + #{search} + '%'",
            " ORDER BY khdm ",
            "</if>",
            "</script>"})
    List<PublicTerminal> selectForIndex(String search);
}
