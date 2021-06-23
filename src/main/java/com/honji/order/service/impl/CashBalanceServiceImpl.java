package com.honji.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.CashBalance;
import com.honji.order.mapper.CashBalanceMapper;
import com.honji.order.model.DepositVO;
import com.honji.order.service.ICashBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
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
 * @since 2020-07-23
 */
@Service
public class CashBalanceServiceImpl extends ServiceImpl<CashBalanceMapper, CashBalance> implements ICashBalanceService {

    @Autowired
    private CashBalanceMapper cashBalanceMapper;

    @Override
    public double calBalance(String shopCode) {
        return cashBalanceMapper.calBalance(shopCode);
    }

    @Override
    public PageInfo<CashBalance> listByShopCode(String shopCode, int offset, int limit) {
        PageHelper.startPage(offset / limit + 1, limit);
        QueryWrapper<CashBalance> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(shopCode)) {
            queryWrapper.like("khdm", shopCode);
        }
        List<CashBalance> balances = cashBalanceMapper.selectList(queryWrapper);
        return new PageInfo<>(balances);
    }
}
