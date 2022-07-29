package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.SalesPlan;
import com.honji.order.mapper.SalesPlanMapper;
import com.honji.order.model.SalesPlanDTO;
import com.honji.order.model.SalesPlanVO;
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
    public PageInfo<SalesPlanVO> listForIndex(SalesPlanDTO salesPlanDTO) {
        String jobNum = salesPlanDTO.getJobNum();

        if ("admin".equals(salesPlanDTO.getJobNum())) {//领导查看数据
            salesPlanDTO.setAdmin(true);
        } else {
            //这步查询需要在分页之前进行
            String result = salesPlanMapper.selectAManager(jobNum);//查询是否大区经理
            if (StringUtils.isNotEmpty(result)) {//大区经理查看数据
                salesPlanDTO.setManager(true);
            }
        }

        int offset = salesPlanDTO.getOffset();
        int limit = salesPlanDTO.getLimit();
        PageHelper.startPage(offset / limit + 1, limit);
        return new PageInfo<>(salesPlanMapper.selectForIndex(salesPlanDTO));
    }


    @Override
    public List<SalesPlanVO> listForExport(SalesPlanDTO salesPlanDTO) {
        String jobNum = salesPlanDTO.getJobNum();

        if ("admin".equals(salesPlanDTO.getJobNum())) {//领导查看数据
            salesPlanDTO.setAdmin(true);
        } else {
            String result = salesPlanMapper.selectAManager(jobNum);//查询是否大区经理
            if (StringUtils.isNotEmpty(result)) {//大区经理查看数据
                salesPlanDTO.setManager(true);
            }
        }
        return salesPlanMapper.selectForIndex(salesPlanDTO);
    }

}
