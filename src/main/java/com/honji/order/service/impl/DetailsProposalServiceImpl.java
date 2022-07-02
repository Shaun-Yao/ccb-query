package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.DetailsProposal;
import com.honji.order.mapper.DetailsProposalMapper;
import com.honji.order.service.IDetailsProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yao
 * @since 2021-01-05
 */
@Service
public class DetailsProposalServiceImpl extends ServiceImpl<DetailsProposalMapper, DetailsProposal> implements IDetailsProposalService {

    @Autowired
    private DetailsProposalMapper detailsProposalMapper;


    @Override
    public PageInfo<DetailsProposal> listForIndex(String detailsId, int offset, int limit) {
        PageHelper.startPage(offset / limit + 1, limit);
        return new PageInfo<>(detailsProposalMapper.selectForIndex(detailsId));
    }
}
