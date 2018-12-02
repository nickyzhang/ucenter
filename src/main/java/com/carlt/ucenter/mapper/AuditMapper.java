package com.carlt.ucenter.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.carlt.ucenter.domain.AuditDomain;
import com.carlt.ucenter.model.AuditInfo;
import com.github.pagehelper.Page;

/**
 * 审计数据映射
 * @author ansen.zhu@carlt.com.cn

 
 * @version 1.0
 * @date 2018年8月25日 下午4:42:07
 */
public interface AuditMapper {

	/**
	 * 添加审计信息
	 * @param audit
	 * @return
	 */
	@Insert("INSERT INTO sys_user_opr_log (id, user_id, token, req_url, query_str, rmt_ip_addr, local_ip_addr, req_method, opr_time, access_ret, mark) "
			+ " VALUES (#{id}, #{userId}, #{token}, #{reqUrl}, #{queryStr}, #{rmtIpAddr}, #{localIpAddr}, #{reqMethod}, #{oprTime,jdbcType=TIMESTAMP}, #{accessRet}, #{mark})")
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	int save(AuditInfo audit);
	
	/**
	 * 根据过滤条件查询审计信息
	 * @param queryAudit
	 * @return
	 */
	@Select("<script>"
			+ "select r.*, u.user_code, u.user_name from sys_user_opr_log r left join sys_user u on r.user_id = u.id "
			+ "<where>  "
			+ "	<if test=\"userName != null and userName != ''\"> "
			+ "		and u.user_Name = #{userName} "
			+ "	</if> "
			+ "	<if test=\"userCode != null and userCode != ''\"> "
			+ "		and user_code = #{userCode} "
			+ "	</if> "
			+ "	<if test='reqMethod != null'> "
			+ "		and req_method = #{reqMethod} "
			+ "	</if> "
			+ "	<if test=\"accessRet != null and accessRet != ''\"> "
			+ "		and access_ret = #{accessRet} "
			+ "	</if> "
			+ "	<if test=\"rmtIpAddr != null and rmtIpAddr != ''\"> "
			+ "		and rmt_ip_addr like '%${rmtIpAddr}%' "
			+ "	</if> "
			+ "	<if test=\"localIpAddr != null and localIpAddr != ''\"> "
			+ "		and local_ip_addr like '%${localIpAddr}%' "
			+ "	</if> "
			+ "<choose>"
			+ "	<when test=\"startTime != null and endTime != null\"> "
			+ "		and opr_time  between #{startTime} and #{endTime} "
			+ "	</when> "
			+ "	<when test=\"startTime != null\"> "
			+ "		and opr_time  &gt;= #{startTime} "
			+ "	</when> "
			+ "	<when test=\"endTime != null\"> "
			+ "		and opr_time  &lt;= #{endTime} "
			+ "	</when> "
			+ "</choose>"
			+ "	<if test=\"token != null and token != ''\"> "
			+ "		and token like '%${token}%' "
			+ "	</if> "
			+ "	<if test=\"reqUrl != null and reqUrl != ''\"> "
			+ "		and req_url like '%${reqUrl}%' "
			+ "	</if> "
			+ "	<if test=\"queryStr != null and queryStr != ''\"> "
			+ "		and query_str like '%${queryStr}%' "
			+ "	</if> "
			+ "	<if test=\"mark != null and mark != ''\"> "
			+ "		and mark like '%${mark}%' "
			+ "	</if> "
			+ "</where> "
			+ "	<if test=\"sortField != null and sortField != '' and sortType != null and sortType != ''\"> "
			+ "		order by ${sortField} ${sortType}"
			+ "	</if> "
			+ "</script>"
			)
	@Results(id="all_cols", value={
			@Result(column="id", property="id")
			,@Result(column="user_id", property="userId")
			,@Result(column="user_name", property="userName")
			,@Result(column="user_code", property="userCode")
			,@Result(column="token", property="token")
			,@Result(column="req_url", property="reqUrl")
			,@Result(column="query_str", property="queryStr")
			,@Result(column="rmt_ip_addr", property="rmtIpAddr")
			,@Result(column="local_ip_addr", property="localIpAddr")
			,@Result(column="req_method", property="reqMethod")
			,@Result(column="access_ret", property="accessRet")
			,@Result(column="mark", property="mark")
			,@Result(column="opr_time", property="oprTime", jdbcType=JdbcType.TIMESTAMP)
		})
	Page<AuditInfo> select(AuditDomain queryAudit);

}
