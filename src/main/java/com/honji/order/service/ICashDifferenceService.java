package com.honji.order.service;

import com.github.pagehelper.PageInfo;
import com.honji.order.entity.CashDifference;
import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.order.entity.PrivateTerminal;
import com.honji.order.enums.DifferenceTypeEnum;
import com.honji.order.model.DepositDTO;
import com.honji.order.model.DepositVO;
import com.honji.order.model.DifferenceDTO;
import com.honji.order.model.DifferenceVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2021-07-22
 */
public interface ICashDifferenceService extends IService<CashDifference> {

    PageInfo<CashDifference> listForIndex(int offset, int limit, String shopCode, DifferenceTypeEnum type);
    PageInfo<DifferenceVO> query(DifferenceDTO dto);

    List<DifferenceVO> listAll(DifferenceDTO dto);
}
