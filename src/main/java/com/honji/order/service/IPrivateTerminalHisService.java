package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.PrivateTerminal;
import com.honji.order.entity.PrivateTerminalHis;
import com.honji.order.model.TerminalDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2021-07-31
 */
public interface IPrivateTerminalHisService extends IService<PrivateTerminalHis> {

    PageInfo<PrivateTerminalHis> listForIndex(TerminalDTO terminalDTO);
}
