package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.SalesPlan;
import com.honji.order.model.SalesPlanDTO;
import com.honji.order.model.SalesPlanVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2021-07-31
 */
public interface ISalesPlanService extends IService<SalesPlan> {

    Map<String, Object> getPerformance(String date, String shopCode);
    PageInfo<SalesPlanVO> listForIndex(SalesPlanDTO salesPlanDTO);
    List<SalesPlanVO> listForExport(SalesPlanDTO salesPlanDTO);
}
