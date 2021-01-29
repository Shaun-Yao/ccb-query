package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Bill;
import com.honji.order.model.BillDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2021-01-05
 */
public interface IBillService extends IService<Bill> {

    PageInfo<Bill> listForIndex(BillDTO billDTO);

    void removeByMonth(BillDTO billDTO);
}
