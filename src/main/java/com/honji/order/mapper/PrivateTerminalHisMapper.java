package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.PrivateTerminal;
import com.honji.order.entity.PrivateTerminalHis;
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
public interface PrivateTerminalHisMapper extends BaseMapper<PrivateTerminalHis> {

    @Select({"<script>",
            "SELECT * FROM private_terminal_his where 1 = 1",
            "<if test='terminalDTO.date !=\"\"'>",
            " and created_date = '${terminalDTO.date}'",
            "</if>",
            "<if test='terminalDTO.search != null and terminalDTO.search !=\"\"'>",
            " where khdm like '%' + #{terminalDTO.search} + '%'",
            " or bs_pay like '%' + #{terminalDTO.search} + '%'",
            " or bs_mss like '%' + #{terminalDTO.search} + '%'",
            " or yue_pay like '%' + #{terminalDTO.search} + '%'",
            " or union_sys like '%' + #{terminalDTO.search} + '%'",
            " or union_pay like '%' + #{terminalDTO.search} + '%'",
            " or top_up like '%' + #{terminalDTO.search} + '%'",
            "</if>",
            " ORDER BY created_date desc ",
            "</script>"})
    List<PrivateTerminalHis> selectForIndex(@Param("terminalDTO") TerminalDTO terminalDTO);
}
