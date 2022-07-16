package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.SalesPlanDetails;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2021-07-31
 */
public interface ISalesPlanDetailsService extends IService<SalesPlanDetails> {


    PageInfo<SalesPlanDetails> listForIndex(String planId, boolean showAll, int offset, int limit);
    void saveFeedback(String id, String feedback);
}
