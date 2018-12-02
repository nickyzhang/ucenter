package com.carlt.ucenter.service;

import java.util.List;

import com.carlt.ucenter.domain.RoleDomain;
import com.carlt.ucenter.exception.RestfulJsonException;
import com.carlt.ucenter.model.Resource;
import com.carlt.ucenter.model.Role;
import com.github.pagehelper.Page;

/**
 * 角色服务
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月25日 下午5:12:18
 */
public interface RoleService {

	Role selectByPrimaryKey(int id);

	List<Role> selectAll();

	Page<Role> select(int pageNo, int pageSize, RoleDomain roleDomain);

	int deleteByPrimaryKey(int id);

	int insert(Role role);

	int updateByPrimaryKeySelective(Role role, List<String> resIds);

	int updateByPrimaryKey(Role role);

	int linkRes(String roleId, List<String> asList);

	List<Role> getRoleByUserId(String userId) throws RestfulJsonException;

	List<Resource> getResourceByRole(int roleId);

	int delete(List<String> ids);
}
