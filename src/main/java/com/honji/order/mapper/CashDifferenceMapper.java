package com.honji.order.mapper;

import com.honji.order.entity.CashDifference;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.model.DepositDTO;
import com.honji.order.model.DepositVO;
import com.honji.order.model.DifferenceDTO;
import com.honji.order.model.DifferenceVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yao
 * @since 2021-07-22
 */
public interface CashDifferenceMapper extends BaseMapper<CashDifference> {

    @Select({"<script>",
            "SELECT cd.*, cb.khmc as shopName FROM cash_difference cd\n",
            " LEFT JOIN cash_balance cb on cd.shop_code = cb.khdm " ,
            " where cd.shop_code in ",
            "<foreach item='item' index='index' collection='dto.shopCodes' open='(' separator=',' close=')'> #{item} </foreach>",
            "<if test='dto.begin !=null and dto.begin!=\"\"'>",
            "AND cd.date &gt;= '${dto.begin}'",
            "</if>",
            "<if test='dto.end !=null and dto.end!=\"\"'>",
            "AND cd.date &lt;= '${dto.end}'",
            "</if>",
            " ORDER BY cd.date desc ",
            "</script>"})
    List<DifferenceVO> query(@Param("dto") DifferenceDTO dto);
}
