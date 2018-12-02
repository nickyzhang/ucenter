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

import com.carlt.ucenter.domain.ResourceDomain;
import com.carlt.ucenter.model.Resource;
import com.github.pagehelper.Page;

/**
 * 资源数据映射
 * @author ansen.zhu@carlt.com.cn

 
 * @version 1.0
 * @date 2018年8月25日 下午4:42:39
 */
public interface ResourceMapper {

	/**
	 * 根据主键过去资源信息
	 * @param id
	 * @return
	 */
	@Select("select * from sys_res where id=#{id}")
	@Results(id="all_cols", value={
		@Result(column="id", property="id")
		,@Result(column="pid", property="pid")
		,@Result(column="res_type", property="resType")
		,@Result(column="res_name", property="resName")
		,@Result(column="res_uri", property="resUri")
		,@Result(column="rank", property="rank")
		,@Result(column="mark", property="mark")
		,@Result(column="res_icon", property="resIcon")
		,@Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP)
		,@Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
	})
	Resource selectByPrimaryKey(int id);
	
	/**
	 * 获取所有资源列表
	 * @return
	 */
	@Select("select * from sys_res")
	@ResultMap("all_cols")
	List<Resource> selectAll();
	
	/**
	 * 根据资源类型查询资源列表
	 * @param resTypes
	 * @return
	 */
	@Select("<script>"
			+ "select * from sys_res where res_type in "
			+ " <foreach collection='resTypes' item='resType' index='index' open='(' close=')' separator=','> "
			+ "  #{resType, jdbcType=INTEGER} "
			+ " </foreach>"
			+ "</script>")
	@ResultMap("all_cols")
	List<Resource> selectAllByType(@Param("resTypes") List<String> resTypes);
	
	/**
	 * 根据主键删除资源
	 * @param id
	 * @return
	 */
	@Delete("delete from sys_res where id = #{id}")
	int deleteByPrimaryKey(int id);
	
	/**
	 * 添加资源
	 * @param res
	 * @return
	 */
	@Insert("insert into sys_res (id, pid, res_type, res_name, res_uri, rank, mark, res_icon, create_time, update_time) "
			+ " values (#{id}, #{pid}, #{resType}, #{resName}, #{resUri}, #{rank}, #{mark}, #{resIcon}, #{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})")
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	int insert(Resource res);
	
	/**
	 * 更新资源
	 * @param res
	 * @return
	 */
	@Update("UPDATE sys_res SET pid = #{pid}, res_type = #{resType}, res_name = #{resName}, res_uri = #{resUri}"
			+ ", rank = #{rank}, update_time = now(), mark = #{mark}, res_icon = #{resIcon} WHERE id = #{id}")
	int updateByPrimaryKey(Resource res);

	/**
	 * 根据用户ID获取资源列表
	 * @param userId
	 * @return
	 */
	@Select("select r.* from sys_user u "
			+ "left join sys_user_role ur on u.id = ur.user_id "
			+ "left join sys_role_res rr on ur.role_id = rr.role_id "
			+ "left join sys_res r on rr.res_id = r.id "
			+ "where u.id = #{userId}")
	@ResultMap("all_cols")
	List<Resource> getResourceByUser(int userId);
	
	/**
	 * 根据父资源ID和资源类型获取资源
	 * @param resPids
	 * @param resType
	 * @return
	 */
	@Select("<script>"
			+ "select r.* from sys_res r where r.res_type = #{resType} and r.pid in "
			+ " <foreach collection='resPids' item='resPid' index='index' open='(' close=')' separator=','> "
			+ "  #{resPid, jdbcType=INTEGER} "
			+ " </foreach>"
			+ "</script>")
	@ResultMap("all_cols")
	List<Resource> getResourceByPids(@Param("resPids") List<String> resPids, @Param("resType") int resType);

	/**
	 * 批量删除接口资源
	 * @param ids
	 * @return
	 */
	@Delete("<script>"
			+ "delete from sys_res where res_type = 3 and id in"
			+ " <foreach collection='ids' item='id' index='index' open='(' close=')' separator=','> "
			+ "  #{id, jdbcType=INTEGER} "
			+ " </foreach>"
			+ "</script>")
	int deleteInterface(@Param("ids") List<String> ids);
	
	/**
	 * 批量删除菜单资源（虚拟菜单和菜单（res_type = 2,3））
	 * @param ids
	 * @return
	 */
	@Delete("<script>"
			+ "delete from sys_res where res_type in (1,2) and id in "
			+ " <foreach collection='ids' item='id' index='index' open='(' close=')' separator=','> "
			+ "  #{id, jdbcType=INTEGER} "
			+ " </foreach>"
			+ "</script>")
	int deleteMenu(@Param("ids") List<String> ids);

	/**
	 * 解除角色与资源关联关系
	 * @param resIds
	 * @return
	 */
	@Delete("<script>"
			+ "delete from sys_role_res where res_id in "
			+ " <foreach collection='resIds' item='resId' index='index' open='(' close=')' separator=','> "
			+ "  #{resId, jdbcType=INTEGER} "
			+ " </foreach>"
			+ "</script>")
	int unlinkRole(@Param("resIds") List<String> resIds);
	
	/**
	 * 通过父资源ID解除角色与接口（resType=3）关联关系
	 * @param resIds
	 * @return
	 */
	@Delete("<script>"
			+ "delete from sys_role_res where res_id in (select id from sys_res r where r.res_type = 3 and r.pid in "
			+ " <foreach collection='resIds' item='resId' index='index' open='(' close=')' separator=','> "
			+ "  #{resId, jdbcType=INTEGER} "
			+ " </foreach>"
			+ ")"
			+ "</script>")
	int unlinkRoleByPid(@Param("resIds") List<String> resIds);

	/**
	 * 根据过滤条件查询资源列表
	 * @param queryRes
	 * @return
	 */
	@Select("<script>"
			+ "select r.* from sys_res r "
			+ "<where>  "
			+ "	<if test=\"resName != null and resName != ''\"> "
			+ "		res_name like '%${resName}%' "
			+ "	</if> "
			+ "	<if test=\"pid != null and pid > -2\"> "
			+ "		and pid = #{pid} "
			+ "	</if> "
			+ "	<if test=\"mark != null and mark != ''\"> "
			+ "		and mark like '%${mark}%' "
			+ "	</if> "
			+ "	<if test=\"resUri != null and resUri != ''\"> "
			+ "		and res_uri like '%${resUri}%' "
			+ "	</if> "
			+ "	<if test='resType != null'> "
			+ "		and res_type = #{resType} "
			+ "	</if> "
			+ "	<if test=\"createTime != null and createTime != ''\"> "
			+ "		and create_time  &gt;= #{createTime} "
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
	Page<Resource> select(ResourceDomain queryRes);
	
	/**
	 * 更新用户资源的父资源ID
	 * @param pResId
	 * @param resIds
	 * @return
	 */
	@Update("<script>"
			+ "UPDATE sys_res SET pid = #{pResId}, update_time = now() WHERE id in "
			+ " <foreach collection='resIds' item='resId' index='index' open='(' close=')' separator=','> "
			+ "  #{resId, jdbcType=INTEGER} "
			+ " </foreach>"
			+ "</script>")
	int updateResesPid(@Param("pResId") String pResId, @Param("resIds") List<String> resIds);
	
	/**
	 * 根据父资源ID重置子接口(res_type=3)ID的父资源ID
	 * @param pResId 新父资源ID
	 * @param resPids 待更改子资源的父资源列表
	 * @return
	 */
	@Update("<script>"
			+ "UPDATE sys_res SET pid = #{pResId, jdbcType=INTEGER}, update_time = now() WHERE res_type = 3 and pid in "
			+ " <foreach collection='resPids' item='resPid' index='index' open='(' close=')' separator=','> "
			+ "  #{resPid, jdbcType=INTEGER} "
			+ " </foreach>"
			+ "</script>")
	int updateResesPidByPid(@Param("pResId") String pResId, @Param("resPids") List<String> resPids);

	/**
	 * 查询资源列表中有子菜单（包括虚拟菜单）的资源数目
	 * @param resIds
	 * @param resTypes
	 * @return
	 */
	@Select("<script>"
			+ "select count(1) from sys_res where res_type in "
			+ " <foreach collection='resTypes' item='resType' index='index' open='(' close=')' separator=','> "
			+ "  #{resType, jdbcType=INTEGER} "
			+ " </foreach>"
			+ " and pid in "
			+ " <foreach collection='resIds' item='resId' index='index' open='(' close=')' separator=','> "
			+ "  #{resId, jdbcType=INTEGER} "
			+ " </foreach>"
			+ "</script>")
	int hasSubResource(@Param("resIds")List<String> resIds, @Param("resTypes") List<String> resTypes);
}
