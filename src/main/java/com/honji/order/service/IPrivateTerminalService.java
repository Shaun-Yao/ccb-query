package com.honji.order.service;

import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Bill;
import com.honji.order.entity.PrivateTerminal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.order.model.BillDTO;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2021-07-31
 */
public interface IPrivateTerminalService extends IService<PrivateTerminal> {

    PageInfo<PrivateTerminal> listForIndex(int offset, int limit, String search);
}
