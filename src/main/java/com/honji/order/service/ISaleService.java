package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.PrivateTerminalHis;
import com.honji.order.entity.Sale;
import com.honji.order.model.TerminalDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2021-07-31
 */
public interface ISaleService extends IService<Sale> {

    PageInfo<Sale> listForIndex(String shopCode, int offset, int limit);
}
