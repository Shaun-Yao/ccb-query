package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.PersonArea;
import com.honji.order.model.PersonAreaVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2021-07-31
 */
public interface IPersonAreaService extends IService<PersonArea> {

    PageInfo<PersonAreaVO> listForIndex(String personName, int offset, int limit);
}
