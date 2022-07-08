package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Area;
import com.honji.order.entity.Sale;
import com.honji.order.mapper.AreaMapper;
import com.honji.order.mapper.SaleMapper;
import com.honji.order.service.IAreaService;
import com.honji.order.service.ISaleService;
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
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {



}
