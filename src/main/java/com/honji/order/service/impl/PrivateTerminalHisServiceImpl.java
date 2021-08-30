package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.PrivateTerminal;
import com.honji.order.entity.PrivateTerminalHis;
import com.honji.order.mapper.PrivateTerminalHisMapper;
import com.honji.order.mapper.PrivateTerminalMapper;
import com.honji.order.model.TerminalDTO;
import com.honji.order.service.IPrivateTerminalHisService;
import com.honji.order.service.IPrivateTerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class PrivateTerminalHisServiceImpl extends ServiceImpl<PrivateTerminalHisMapper, PrivateTerminalHis> implements IPrivateTerminalHisService {

    @Autowired
    private PrivateTerminalHisMapper terminalMapper;

    @Override
    public PageInfo<PrivateTerminalHis> listForIndex(TerminalDTO terminalDTO) {
        PageHelper.startPage(terminalDTO.getOffset() / terminalDTO.getLimit() + 1, terminalDTO.getLimit());
        List<PrivateTerminalHis> terminals = terminalMapper.selectForIndex(terminalDTO);
        return new PageInfo<>(terminals);
    }
}
