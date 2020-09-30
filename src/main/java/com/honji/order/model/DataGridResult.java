package com.honji.order.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DataGridResult implements Serializable {

    private long total;
    private List<?> rows;

    public DataGridResult(IPage page) {
        this.setTotal(page.getTotal());
        this.setRows(page.getRecords());
    }

    public DataGridResult(long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public DataGridResult(PageInfo<?> pageInfo) {
//        DepositVO deposit = (DepositVO) pageInfo.getList().get(0);
//        System.out.println(deposit.getKhdm() + deposit.getAmount() + "====" + deposit.getCash_adjustment());
        this.total = pageInfo.getTotal();
        this.rows = pageInfo.getList();
    }
}
