package com.carlt.ucenter.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import com.carlt.ucenter.domain.BtnResourceDomain;
import com.carlt.ucenter.model.BtnResource;
import com.github.pagehelper.Page;

/**
 * 按钮级资源数据映射
 * @author ansen.zhu@carlt.com.cn

 
 * @version 1.0
 * @date 2018年10月11日 上午10:02:20
 */
public interface BtnResourceMapper {

	/**
	 * 根据主键过去资源信息
	 * @param id
	 * @return
	 */
	@Select("select * from sys_btn_res where id=#{id}")
	@Results(id="all_cols", value={
		@Result(column="id", property="id")
		,@Result(column="type", property="type")
		,@Result(column="group_code", property="groupCode")
		,@Result(column="name", property="name")
		,@Result(column="code", property="code")
		,@Result(column="value", property="value")
		,@Result(column="remark", property="remark")
		,@Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
	})
	BtnResource getById(int id);
	
	/**
	 * 获取所有资源列表
	 * @return
	 */
	@Select("select * from sys_btn_res order by group_code,update_time desc")
	@ResultMap("all_cols")
	List<BtnResource> selectAll();
	
	/**
	 * 添加资源
	 * @param res
	 * @return
	 */
	@Insert("insert into sys_btn_res (id, type, group_code, name, code, value, remark, update_time) "
			+ " values (#{id}, #{type}, #{groupCode}, #{name}, #{code}, #{value}, #{remark}, #{updateTime,jdbcType=TIMESTAMP})")
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	int insert(BtnResource res);
	
	/**
	 * 更新资源
	 * @param res
	 * @return
	 */
	@Update("UPDATE sys_btn_res SET group_code = #{groupCode}, type = #{type}, name = #{name}, code = #{code}"
			+ ", value = #{value}, update_time = now(), remark = #{remark} WHERE id = #{id}")
	int update(BtnResource res);

	/**
	 * 根据用户ID获取资源列表
	 * @param userId
	 * @return
	 */
	@Select("select distinct br.* from sys_btn_res br "
			+ "left join sys_role_btn_res rr on rr.res_id = br.id "
			+ "left join sys_user_role ur on rr.role_id = ur.role_id "
			+ "where ur.user_id = #{userId}")
	@ResultMap("all_cols")
	List<BtnResource> getByUserId(int userId);
	
	/**
	 * 批量删除资源
	 * @param resIds
	 * @return
	 */
	@Delete("<script>"
			+ "delete from sys_btn_res where id in"
			+ " <foreach collection='ids' item='id' index='index' open='(' close=')' separator=','> "
			+ "  #{id, jdbcType=INTEGER} "
			+ " </foreach>"
			+ "</script>")
	int delete(@Param("ids") List<String> resIds);
	
	/**
	 * 解除角色与资源关联关系
	 * @param resIds
	 * @return
	 */
	@Delete("<script>"
			+ "delete from sys_role_btn_res where res_id in "
			+ " <foreach collection='resIds' item='resId' index='index' open='(' close=')' separator=','> "
			+ "  #{resId, jdbcType=INTEGER} "
			+ " </foreach>"
			+ "</script>")
	int unlinkRoles(@Param("resIds") List<String> resIds);
	
	/**
	 * 解除角色与资源关联关系
	 * @param resIds
	 * @return
	 */
	@Delete("<script>"
			+ "delete from sys_role_btn_res where role_id = #{roleId, jdbcType=INTEGER} and res_id in "
			+ " <foreach collection='resIds' item='resId' index='index' open='(' close=')' separator=','> "
			+ "  #{resId, jdbcType=INTEGER} "
			+ " </foreach>"
			+ "</script>")
	int unlinkRole(@Param("roleId") String roleId, @Param("resIds") List<String> resIds);

	@Insert("<script>"
			+ "insert into sys_role_btn_res(role_id, res_id) values "
			+ " <foreach collection='resIds' item='resId' index='index' open='' close='' separator=','>"
			+ "  (#{roleId, jdbcType=INTEGER},#{resId, jdbcType=INTEGER})"
			+ " </foreach>"
			+ "</script>")
	int linkRole(@Param("roleId") String roleId, @Param("resIds") List<String> resIds);

	/**
	 * 根据角色ID获取资源列表
	 * @param userId
	 * @return
	 */
	@Select("select distinct br.* from sys_btn_res br "
			+ " left join sys_role_btn_res rr on rr.res_id = br.id "
			+ " where rr.role_id = #{roleId, jdbcType=INTEGER} "
			+ " order by group_code,update_time desc ")
	@ResultMap("all_cols")
	List<BtnResource> getByRoleId(String roleId);
	
	/**
	 * 根据过滤条件查询资源列表
	 * @param queryRes
	 * @return
	 */
	@Select("<script>"
			+ "select r.* from sys_btn_res r "
			+ "<where>  "
			+ "	<if test=\"name != null and name != ''\"> "
			+ "		name like '%${name}%' "
			+ "	</if> "
			+ "	<if test=\"remark != null and remark != ''\"> "
			+ "		and remark like '%${remark}%' "
			+ "	</if> "
			+ "	<if test=\"code != null and code != ''\"> "
			+ "		and code like '%${code}%' "
			+ "	</if> "
			+ "	<if test=\"value != null and value != ''\"> "
			+ "		and value like '%${value}%' "
			+ "	</if> "
			+ "	<if test=\"groupCode != null and groupCode != ''\"> "
			+ "		and group_code like '%${groupCode}%' "
			+ "	</if> "
			+ "	<if test='type != null and type > -1'> "
			+ "		and type = #{type} "
			+ "	</if> "
			+ "	<if test=\"updateTime != null and updateTime != ''\"> "
			+ "		and update_time  &gt;= #{updateTime} "
			+ "	</if> "
			+ "</where> "
			+ "	<if test=\"sortField != null and sortField != '' and sortType != null and sortType != ''\"> "
			+ "		order by ${sortField} ${sortType}"
			+ "	</if> "
			+ "</script>"
			)
	@ResultMap("all_cols")
	Page<BtnResource> select(BtnResourceDomain queryRes);

	@Select("select distinct br.* from sys_btn_res br "
			+ "left join sys_role_btn_res rr on rr.res_id = br.id "
			+ "left join sys_user_role ur on rr.role_id = ur.role_id "
			+ "where ur.user_id = #{userId} and br.group_code = #{groupCode}"
			+ " order by group_code,update_time desc ")
	@ResultMap("all_cols")
	List<BtnResource> getByGroupCodeByUserId(@Param("userId")int userId, @Param("groupCode")String groupCode);

	@Select("select distinct br.* from sys_btn_res br where br.group_code = #{groupCode}"
			+ " order by group_code,update_time desc ")
	@ResultMap("all_cols")
	List<BtnResource> getByGroupCode(String groupCode);
	
}
