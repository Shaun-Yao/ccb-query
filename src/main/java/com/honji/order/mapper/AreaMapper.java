package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.Area;
import com.honji.order.entity.Shop;
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
public interface AreaMapper extends BaseMapper<Area> {


    @Select("SELECT khdm as code, khmc as name FROM IP180.SPERP.dbo.kehu WHERE khsx3 = '${area}' and tzsy = 0")
    List<Shop> listShops(String area);
}
