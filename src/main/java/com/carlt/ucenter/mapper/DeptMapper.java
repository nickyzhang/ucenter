package com.carlt.ucenter.mapper;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import com.carlt.ucenter.model.Dept;

public interface DeptMapper {

	@Select("select * from sys_dept where id=#{id}")
	@Results(id = "all_cols", value = { @Result(property = "id", column = "id"),
			@Result(column = "par_dept_code", property = "parDeptCode"),
			@Result(column = "dept_code", property = "deptCode"), @Result(column = "dept_name", property = "deptName"),
			@Result(column = "mark", property = "mark"), @Result(column = "mobile_phone", property = "mobilePhone"),
			@Result(column = "email", property = "email"), @Result(column = "leader", property = "leader"),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP, javaType = Timestamp.class),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP, javaType = Timestamp.class) })
	Dept selectByPrimaryKey(int id);

	@ResultMap(value = "all_cols")
	@Select("select * from sys_dept")
	List<Dept> selectAll();

	@Delete("delete from sys_dept where id = #{id}")
	int deleteByPrimaryKey(int id);

	@Insert("insert into sys_dept values(#{id}, #{parDeptCode}, #{deptCode}, #{deptName}, #{mark}, #{mobilePhone}, #{email}, #{leader}, #{createTime}, #{updateTime})")
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	int insert(Dept dept);

	@Update("update sys_dept set dept_code=#{deptCode}, dept_name=#{deptName} where id=#{id}")
	int updateByPrimaryKeySelective(Dept dept);

	@Update("update sys_dept set dept_code=#{deptCode}, dept_name=#{deptName} where id=#{id}")
	int updateByPrimaryKey(Dept dept);

}
