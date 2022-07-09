package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.SalesPlan;
import com.honji.order.entity.vo.SalesPlanVO;
import com.honji.order.mapper.SalesPlanMapper;
import com.honji.order.service.ISalesPlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public PageInfo<SalesPlanVO> listForIndex(String jobNum, int offset, int limit) {

        String result = salesPlanMapper.selectAManager(jobNum);//查询是否大区经理
        PageHelper.startPage(offset / limit + 1, limit);
        if ("admin".equals(jobNum)) {//领导查看数据
            return new PageInfo<>(salesPlanMapper.selectForIndex(jobNum, true));
        }

        if (StringUtils.isNotEmpty(result)) {//大区经理查看数据
            return new PageInfo<>(salesPlanMapper.selectForManager(jobNum));
        }

        return new PageInfo<>(salesPlanMapper.selectForIndex(jobNum, false));//督导查看数据
    }


    @Override
    public List<SalesPlanVO> listForExport() {
        return salesPlanMapper.selectForIndex("", true);
    }

}
