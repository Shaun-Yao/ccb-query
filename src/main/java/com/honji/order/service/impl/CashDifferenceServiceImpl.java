package com.honji.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.CashBalance;
import com.honji.order.entity.CashDifference;
import com.honji.order.mapper.CashBalanceMapper;
import com.honji.order.mapper.CashDifferenceMapper;
import com.honji.order.model.DepositVO;
import com.honji.order.model.DifferenceDTO;
import com.honji.order.model.DifferenceVO;
import com.honji.order.service.ICashDifferenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yao
 * @since 2021-07-22
 */
@Service
public class CashDifferenceServiceImpl extends ServiceImpl<CashDifferenceMapper, CashDifference> implements ICashDifferenceService {

    @Autowired
    private CashDifferenceMapper differenceMapper;

    @Override
    public PageInfo<CashDifference> listForIndex(int offset, int limit, String shopCode) {
        PageHelper.startPage(offset / limit + 1, limit);
        QueryWrapper<CashDifference> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_code", shopCode);
        queryWrapper.orderByDesc("date");
        List<CashDifference> differences = differenceMapper.selectList(queryWrapper);
        return new PageInfo<>(differences);
    }

    @Override
    public PageInfo<DifferenceVO> query(DifferenceDTO dto) {
        PageHelper.startPage(dto.getOffset() / dto.getLimit() + 1, dto.getLimit());
        List<DifferenceVO> differenceVOs = new ArrayList<>();
        if (dto.getShopCodes() != null && dto.getShopCodes().size() > 0) {//店铺集合不为空才查询
            differenceVOs = differenceMapper.query(dto);
        }
        return new PageInfo<>(differenceVOs);
    }

    @Override
    public List<DifferenceVO> listAll(DifferenceDTO dto) {
        return differenceMapper.query(dto);
    }
}
