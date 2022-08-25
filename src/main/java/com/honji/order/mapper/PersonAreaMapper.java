package com.honji.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.order.entity.PersonArea;
import com.honji.order.model.Person;
import com.honji.order.model.PersonAreaVO;
import com.honji.order.model.SmallArea;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yao
 * @since 2021-07-31
 */
public interface PersonAreaMapper extends BaseMapper<PersonArea> {

    @Select({"<script>",
            "SELECT area.id, person.cpsn_name as personName, quyu.qymc as areaName FROM IP180.SPERP.dbo.person_area area",
            "JOIN IP180.SPERP.dbo.quyu on area.area_code = quyu.qydm ",
            "JOIN IP224.UFDATA_100_2018.dbo.hr_hi_person person on area.job_num = person.cpsn_num ",
            "ORDER BY personName ",
            "</script>"})
    List<PersonAreaVO> listForIndex();

    @Select({"<script>",
            "SELECT cpsn_num as job_num, cpsn_name as name ",
            "FROM IP224.UFDATA_100_2018.dbo.hr_hi_person ",
            "WHERE rEmployState = 10 ",
            "ORDER BY name ",
            "</script>"})
    List<Person> selectPersons();

    @Select({"<script>",
            "SELECT qydm as code, qymc as name FROM IP180.SPERP.dbo.quyu ",
            "WHERE qymc LIKE '%直营%' or qymc = '特卖区' ",
            "ORDER BY name ",
            "</script>"})
    List<SmallArea> selectAreas();

    @Insert({"<script>",
            " INSERT INTO IP180.SPERP.dbo.person_area (job_num, area_code) ",
            " VALUES('${personArea.jobNum}', '${personArea.areaCode}') ",
            "</script>"})
    int save(@Param("personArea")PersonArea personArea);

    @Update({"<script>",
            "UPDATE IP180.SPERP.dbo.person_area SET job_num = '${personArea.jobNum}', area_code = '${personArea.areaCode}' ",
            " WHERE id = '${personArea.id}' ",
            "</script>"})
    int update(@Param("personArea")PersonArea personArea);

}
