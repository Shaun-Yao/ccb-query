package com.honji.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.PublicTerminal;
import com.honji.order.entity.PublicTerminal;
import com.honji.order.mapper.PublicTerminalMapper;
import com.honji.order.mapper.PublicTerminalMapper;
import com.honji.order.service.IPublicTerminalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yao
 * @since 2021-08-12
 */
@Service
public class PublicTerminalServiceImpl extends ServiceImpl<PublicTerminalMapper, PublicTerminal> implements IPublicTerminalService {

    @Autowired
    private PublicTerminalMapper terminalMapper;

    @Override
    public PageInfo<PublicTerminal> listForIndex(int offset, int limit, String search) {
        PageHelper.startPage(offset / limit + 1, limit);
        List<PublicTerminal> terminals = terminalMapper.selectForIndex(search);
        return new PageInfo<>(terminals);
    }
}
