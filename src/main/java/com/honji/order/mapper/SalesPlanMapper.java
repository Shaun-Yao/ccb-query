package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.SalesPlan;
import com.honji.order.entity.vo.SalesPlanVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yao
 * @since 2021-07-31
 */
public interface SalesPlanMapper extends BaseMapper<SalesPlan> {


    /**
     * 根据工号查找是否是大区经理
     * @param shop
     * @return
     */
    @Select({"<script>",
            "SELECT top 1 job_num FROM IP180.SPERP.dbo.kehu ",
            "JOIN area on kehu.khsx3 = area.code ",
            "where khdm = '${shop}'",
            "</script>"})
    String selectManagerByShop(String shop);

    /**
     * 根据工号查找是否是大区经理
     * @param jobNum
     * @return
     */
    @Select("SELECT top 1 job_num FROM area WHERE job_num = '${jobNum}'")
    String selectAManager(String jobNum);

    @Select("SELECT empname as name FROM IP216.honjinav.dbo._FR_人员档案 WHERE employeeno = '${jobNum}'")
    Map<String, Object> selectName(String jobNum);

    @Select("exec dbo.check_performance_by_shop_and_month @date = #{date}, @shopCode = #{shopCode}")
    Map<String, Object> selectPerformance(String date, String shopCode);

    @Select({"<script>",
            "SELECT kehu.khmc as shopName, sales_plan.* FROM sales_plan ",
            "JOIN IP180.SPERP.dbo.kehu on sales_plan.shop_code = kehu.khdm ",
            "LEFT JOIN area on sales_plan.area = area.code ",
            "where area.job_num = '${jobNum}'",
            " ORDER BY perform_date desc ",
            "</script>"})
    List<SalesPlanVO> selectForManager(String jobNum);

    @Select({"<script>",
            "SELECT kehu.khmc as shopName, sales_plan.* FROM sales_plan ",
            "JOIN IP180.SPERP.dbo.kehu on sales_plan.shop_code = kehu.khdm ",
            "<if test='!isAdmin'>",//非管理员要加工号筛选
            " where job_num = '${jobNum}'",
            "</if>",
            "<if test='!isAdmin'>",
            " ORDER BY perform_date desc ",
            "</if>",
            "<if test='isAdmin'>",//管理员定制排序
            " ORDER BY perform_date, month_diff desc ",
            "</if>",
            "</script>"})
    List<SalesPlanVO> selectForIndex(String jobNum, boolean isAdmin);

    /**
     * 根据id查看此记录大区经理是否是此工号
     * @param id, jobNum
     * @return
     */
    @Select({"<script>",
            "SELECT count(*) FROM sales_plan ",
            " join area on sales_plan.area = area.code ",
            " WHERE sales_plan.id = '${id}' and area.job_num = '${jobNum}'",
            "</script>"})
    int belongTo(String id, String jobNum);

    @Insert({"<script>",
            "INSERT INTO IP155.HonjiNav_plus.dbo.WeChat_SendMessage",
            " (EmployeeNo, SendMessages, CreateDate) ",
            " VALUES ('${jobNum}', '${message}', GETDATE()) ",
            "</script>"})
    int notify(String jobNum, String message);
}
