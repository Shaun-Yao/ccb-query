package com.honji.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.PersonArea;
import com.honji.order.mapper.PersonAreaMapper;
import com.honji.order.model.PersonAreaVO;
import com.honji.order.service.IPersonAreaService;
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
public class PersonAreaServiceImpl extends ServiceImpl<PersonAreaMapper, PersonArea> implements IPersonAreaService {

    @Autowired
    private PersonAreaMapper personAreaMapper;

    @Override
    public PageInfo<PersonAreaVO> listForIndex(String personName, int offset, int limit) {
        PageHelper.startPage(offset / limit + 1, limit);
        return new PageInfo<>(personAreaMapper.listForIndex());
    }
}
