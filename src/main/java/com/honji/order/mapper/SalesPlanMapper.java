package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.SalesPlan;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yao
 * @since 2021-07-31
 */
public interface SalesPlanMapper extends BaseMapper<SalesPlan> {

    @Select("SELECT empname as name FROM IP216.honjinav.dbo._FR_人员档案 WHERE employeeno = '${jobNum}'")
    Map<String, Object> selectName(String jobNum);

    @Select("exec dbo.check_performance_by_shop_and_month @date = #{date}, @shopCode = #{shopCode}")
    Map<String, Object> selectPerformance(String date, String shopCode);
}
