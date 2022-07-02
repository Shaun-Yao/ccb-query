package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.DetailsProposal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2021-07-31
 */
public interface IDetailsProposalService extends IService<DetailsProposal> {


    PageInfo<DetailsProposal> listForIndex(String detailsId, int offset, int limit);
}
