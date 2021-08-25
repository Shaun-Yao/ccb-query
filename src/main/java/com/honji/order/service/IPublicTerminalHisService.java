package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.PublicTerminal;
import com.honji.order.entity.PublicTerminalHis;
import com.honji.order.model.TerminalDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2021-08-12
 */
public interface IPublicTerminalHisService extends IService<PublicTerminalHis> {
    PageInfo<PublicTerminalHis> listForIndex(TerminalDTO terminalDTO);

}
