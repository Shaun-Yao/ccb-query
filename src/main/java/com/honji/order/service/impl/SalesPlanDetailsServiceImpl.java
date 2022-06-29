package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.SalesPlanDetails;
import com.honji.order.mapper.SalesPlanDetailsMapper;
import com.honji.order.service.ISalesPlanDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yao
 * @since 2021-01-05
 */
@Service
public class SalesPlanDetailsServiceImpl extends ServiceImpl<SalesPlanDetailsMapper, SalesPlanDetails> implements ISalesPlanDetailsService {

    @Autowired
    private SalesPlanDetailsMapper salesPlanDetailsMapper;


    @Override
    public PageInfo<SalesPlanDetails> listForIndex(String planId, int offset, int limit) {
        PageHelper.startPage(offset / limit + 1, limit);
        return new PageInfo<>(salesPlanDetailsMapper.selectForIndex(planId));
    }
}
