package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.SalesPlan;
import com.honji.order.mapper.SalesPlanMapper;
import com.honji.order.service.ISalesPlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yao
 * @since 2021-01-05
 */
@Service
public class SalesPlanServiceImpl extends ServiceImpl<SalesPlanMapper, SalesPlan> implements ISalesPlanService {

    @Autowired
    private SalesPlanMapper salesPlanMapper;


    @Override
    public Map<String, Object> getPerformance(String date, String shopCode) {
        return salesPlanMapper.selectPerformance(date, shopCode);
    }

    @Override
    public PageInfo<SalesPlan> listForIndex(String jobNum, int offset, int limit) {

        String result = salesPlanMapper.selectManager(jobNum);//查询是否大区经理
        PageHelper.startPage(offset / limit + 1, limit);
        if (StringUtils.isNotEmpty(result)) {
            return new PageInfo<>(salesPlanMapper.selectForManager(jobNum));
        }

        return new PageInfo<>(salesPlanMapper.selectForIndex(jobNum));
    }
}
