package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.PublicTerminalHis;
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
 * @since 2021-08-12
 */
public interface PublicTerminalHisMapper extends BaseMapper<PublicTerminalHis> {

    @Select({"<script>",
            "SELECT * FROM public_terminal_his where 1 = 1",
            "<if test='terminalDTO.date !=\"\"'>",
            " and created_date = '${terminalDTO.date}'",
            "</if>",
            "<if test='terminalDTO.search != null and terminalDTO.search !=\"\"'>",
            " and (khdm like '%' + #{terminalDTO.search} + '%'",
            " or ccb like '%' + #{terminalDTO.search} + '%'",
            " or pos like '%' + #{terminalDTO.search} + '%'",
            " or top_up like '%' + #{terminalDTO.search} + '%')",
            " ORDER BY khdm ",
            "</if>",
            "</script>"})
    List<PublicTerminalHis> selectForIndex(@Param("terminalDTO") TerminalDTO terminalDTO);
}
