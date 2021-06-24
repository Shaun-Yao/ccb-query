package com.honji.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Bank;
import com.honji.order.mapper.BankMapper;
import com.honji.order.service.IBankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements IBankService {

    @Autowired
    private BankMapper bankMapper;

    @Override
    public PageInfo<Bank> listByAccount(String account, int offset, int limit) {
        PageHelper.startPage(offset / limit + 1, limit);
        QueryWrapper<Bank> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(account)) {
            queryWrapper.like("account", account);
        }
        List<Bank> banks = bankMapper.selectList(queryWrapper);
        return new PageInfo<>(banks);
    }
}
