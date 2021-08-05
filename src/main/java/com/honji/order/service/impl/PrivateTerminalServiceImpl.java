package com.honji.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Bill;
import com.honji.order.entity.PrivateTerminal;
import com.honji.order.mapper.PrivateTerminalMapper;
import com.honji.order.service.IPrivateTerminalService;
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
 * @since 2021-07-31
 */
@Service
public class PrivateTerminalServiceImpl extends ServiceImpl<PrivateTerminalMapper, PrivateTerminal> implements IPrivateTerminalService {

    @Autowired
    private PrivateTerminalMapper terminalMapper;

    @Override
    public PageInfo<PrivateTerminal> listForIndex(int offset, int limit, String search) {
        PageHelper.startPage(offset / limit + 1, limit);
        List<PrivateTerminal> terminals = terminalMapper.selectForIndex(search);
        return new PageInfo<>(terminals);
    }
}
