package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.DetailsProposal;
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
public interface DetailsProposalMapper extends BaseMapper<DetailsProposal> {


    @Select({"<script>",
            "SELECT * FROM details_proposal where details_id = '${detailsId}'",
            " ORDER BY date ",
            "</script>"})
    List<DetailsProposal> selectForIndex(String detailsId);
}
