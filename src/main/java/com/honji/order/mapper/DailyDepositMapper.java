package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.DailyDeposit;
import com.honji.order.model.DepositVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yao
 * @since 2020-07-20
 */
public interface DailyDepositMapper extends BaseMapper<DailyDeposit> {

    @Select("select deposit.*, bank.name as bankName from dbo.daily_deposit deposit \n" +
            "LEFT JOIN bank on deposit.bank = bank.account where khdm = #{shopCode}")
    List<DepositVo> selectList(@Param("shopCode") String shopCode);


}
