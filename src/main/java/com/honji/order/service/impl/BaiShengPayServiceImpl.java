package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.order.entity.BaiShengPay;
import com.honji.order.mapper.BaiShengPayMapper;
import com.honji.order.service.IBaiShengPayService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.formula.functions.T;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yao
 * @since 2020-05-05
 */
@Service
public class BaiShengPayServiceImpl extends ServiceImpl<BaiShengPayMapper, BaiShengPay> implements IBaiShengPayService {

    @Autowired
    private BaiShengPayMapper baiShengPayMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    //@Transactional(rollbackFor = Exception.class)
    public boolean fastSaveBatch(List<BaiShengPay> list) {
        if(CollectionUtils.isEmpty(list)) {
            return true;
        }
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH,false);
//通过新的session获取mapper
        //int size = 10000;
        long start = System.currentTimeMillis();
        try{
            for(int i = 0; i < list.size(); i++) {

                baiShengPayMapper.insert(list.get(i));
                /*if(i % 1000 == 0 || i == size - 1) {
//手动每1000个一提交，提交后无法回滚
                    session.commit();
//清理缓存，防止溢出
                    session.clearCache();
                }*/
            }

            session.commit();
            session.clearCache();


        long end = System.currentTimeMillis();

 System.out.println("插入耗时:--------------------------" + (start - end) + "--------------------------");

        } catch (Exception e) {
            session.rollback();
            return false;
        } finally{
            session.close();
        }
        return true;
    }
}
