package com.honji.order.service;

import com.github.pagehelper.PageInfo;
import com.honji.order.entity.PrivateTerminal;
import com.honji.order.entity.PublicTerminal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2021-08-12
 */
public interface IPublicTerminalService extends IService<PublicTerminal> {
    PageInfo<PublicTerminal> listForIndex(int offset, int limit, String search);

}
