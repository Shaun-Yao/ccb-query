package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.Bill;
import com.honji.order.model.BillDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yao
 * @since 2021-01-05
 */
public interface BillMapper extends BaseMapper<Bill> {

    @Select({"<script>",
            "SELECT * FROM bill WHERE type = '${billDTO.billType.code}'",
            "<if test='billDTO.month !=null and billDTO.month!=\"\"'>",
            "AND CONVERT(char(7), date, 23) = '${billDTO.month}'",
            "</if>",
            " ORDER BY date desc ",
    "</script>"})
    List<Bill> selectForIndex(@Param("billDTO")BillDTO billDTO);

    @Select({"<script>",
            "DELETE FROM bill WHERE type in ",
            "<foreach item='item' index='index' collection='types' open='(' separator=',' close=')'> #{item} </foreach>",
            " and CONVERT(char(7), date, 23) = '${month}'",
            "</script>"})
    void deleteByMonth(@Param("types") List<String> types, @Param("month") String month);
}
