package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.PublicTerminal;
import com.honji.order.entity.PublicTerminalHis;
import com.honji.order.mapper.PublicTerminalHisMapper;
import com.honji.order.mapper.PublicTerminalMapper;
import com.honji.order.model.TerminalDTO;
import com.honji.order.service.IPublicTerminalHisService;
import com.honji.order.service.IPublicTerminalService;
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
public class PublicTerminalHisServiceImpl extends ServiceImpl<PublicTerminalHisMapper, PublicTerminalHis> implements IPublicTerminalHisService {

    @Autowired
    private PublicTerminalHisMapper terminalMapper;

    @Override
    public PageInfo<PublicTerminalHis> listForIndex(TerminalDTO terminalDTO) {
        PageHelper.startPage(terminalDTO.getOffset() / terminalDTO.getLimit() + 1, terminalDTO.getLimit());
        List<PublicTerminalHis> terminals = terminalMapper.selectForIndex(terminalDTO);
        return new PageInfo<>(terminals);
    }
}
