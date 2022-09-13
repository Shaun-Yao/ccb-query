package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.SalesPlan;
import com.honji.order.model.SalesPlanDTO;
import com.honji.order.model.SalesPlanVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
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
            " JOIN IP180.SPERP.dbo.kehu on sales_plan.shop_code = kehu.khdm ",
            " WHERE id = '${id}'",
            "</script>"})
    SalesPlanVO selectOne(String id);

    @Select({"<script>",
            "SELECT kehu.khmc as shopName, sales_plan.* FROM sales_plan ",
            "JOIN IP180.SPERP.dbo.kehu on sales_plan.shop_code = kehu.khdm ",
            " JOIN area on sales_plan.area = area.code ",
            "<if test='salesPlanDTO.feedbackState != 0'>",//details start
            " JOIN (SELECT plan_id FROM sales_plan_details WHERE plan_id ",
            "<if test='salesPlanDTO.feedbackState == 1'>",
            " in ",
            "</if>",
            "<if test='salesPlanDTO.feedbackState == 2'>",
            " not in ",
            "</if>",
            "(SELECT plan_id FROM sales_plan_details WHERE feedback = '' GROUP BY plan_id) ", "" +
            "GROUP BY plan_id) details ",
            " on sales_plan.id = details.plan_id",
            "</if>",// details end
            "where state = 1 ",
            "<if test='!salesPlanDTO.isAdmin'>",
            " and area.job_num = '${salesPlanDTO.jobNum}'",
            "</if>",
            "<if test='salesPlanDTO.shopCodes != null and salesPlanDTO.shopCodes.size() > 0'>",
            " and shop_code in ",
            "<foreach item='item' index='index' collection='salesPlanDTO.shopCodes' open='(' separator=',' close=')'> #{item} </foreach>",
            "</if>",
            "<if test='!salesPlanDTO.isAdmin'>",
            " ORDER BY perform_date desc ",
            "</if>",
            "<if test='salesPlanDTO.isAdmin'>",
            " ORDER BY perform_date desc, convert(decimal(6, 2), SUBSTRING(month_percent, 1, 4)) ",
            "</if>",
            "</script>"})
    List<SalesPlanVO> selectForManager(@Param("salesPlanDTO")SalesPlanDTO salesPlanDTO);

    @Select({"<script>",
            "SELECT kehu.khmc as shopName, area.name as areaName, sales_plan.* FROM sales_plan ",
            "JOIN IP180.SPERP.dbo.kehu on sales_plan.shop_code = kehu.khdm ",
//            " JOIN area on sales_plan.area = area.code ",
            " JOIN area on kehu.khsx3 = area.code ",
            "<if test='salesPlanDTO.feedbackState != 0'>",//details start
            " JOIN (SELECT plan_id FROM sales_plan_details WHERE plan_id ",
            "<if test='salesPlanDTO.feedbackState == 1'>",
            " in ",
            "</if>",
            "<if test='salesPlanDTO.feedbackState == 2'>",
            " not in ",
            "</if>",
            "(SELECT plan_id FROM sales_plan_details WHERE feedback = '' GROUP BY plan_id) ", "" +
            "GROUP BY plan_id) details ",
            " on sales_plan.id = details.plan_id",
            "</if>",// details end

            "<if test='salesPlanDTO.isAdmin'>",//管理员
            " where state = 1 ",
            "</if>",
            "<if test='salesPlanDTO.isManager'>",//经理
            " where state = 1 and area.job_num = '${salesPlanDTO.jobNum}'",
            "</if>",
            "<if test='!salesPlanDTO.isAdmin and !salesPlanDTO.isManager'>",//不是管理员也不是经理
            " where sales_plan.job_num = '${salesPlanDTO.jobNum}' ",
            "</if>",
            "<if test='salesPlanDTO.area != null and salesPlanDTO.area.length > 0'>",//大区筛选
            " and area.code = '${salesPlanDTO.area}' ",
            "</if>",
            "<if test='salesPlanDTO.shopCodes != null and salesPlanDTO.shopCodes.size() > 0'>",//门店筛选
            " and shop_code in ",
            "<foreach item='item' index='index' collection='salesPlanDTO.shopCodes' open='(' separator=',' close=')'> #{item} </foreach>",
            "</if>",
            "<if test='salesPlanDTO.performDate.length > 0'>",//业绩日期筛选
            " and perform_date = '${salesPlanDTO.performDate}'",
            "</if>",
            " ORDER BY perform_date desc ",
            "</script>"})
    List<SalesPlanVO> selectForIndex(@Param("salesPlanDTO")SalesPlanDTO salesPlanDTO);

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
            " (EmployeeNo,CreateDate,msgtype,agentid,title,descriptions,url,btntxt) ",
            " VALUES ('${jobNum}', GETDATE(), 'textcard','1000030','浪漫春天直营业绩下降分析及方案跟进', '${message}', ",
            " '${link}','点击查看更多信息') ",
            "</script>"})
    int notify(String jobNum, String message, String link);
}
