package com.honji.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.SalesPlan;
import com.honji.order.entity.SalesPlanDetails;
import com.honji.order.mapper.SalesPlanDetailsMapper;
import com.honji.order.mapper.SalesPlanMapper;
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


    @Autowired
    private SalesPlanMapper salesPlanMapper;

    @Autowired
    private ISalesPlanDetailsService salesPlanDetailsService;


    @Override
    public PageInfo<SalesPlanDetails> listForIndex(String planId, int offset, int limit) {
        PageHelper.startPage(offset / limit + 1, limit);
        return new PageInfo<>(salesPlanDetailsMapper.selectForIndex(planId));
    }

    @Override
    //@Transactional 两个数据库无法启动分布式事务
    public void saveFeedback(String id, String feedback) {
        SalesPlanDetails details = salesPlanDetailsMapper.selectById(id);
        SalesPlan salesPlan = salesPlanMapper.selectById(details.getPlanId());
        UpdateWrapper<SalesPlanDetails> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("feedback", feedback);
        salesPlanDetailsService.update(updateWrapper);
        salesPlanMapper.notify(salesPlan.getJobNum(), "您的业绩下降原因已经反馈，请查看！");
    }
}
