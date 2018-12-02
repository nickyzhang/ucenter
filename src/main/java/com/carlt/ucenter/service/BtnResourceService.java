package com.carlt.ucenter.service;

import java.util.List;

import com.carlt.ucenter.domain.BtnResourceDomain;
import com.carlt.ucenter.model.BtnResource;
import com.github.pagehelper.Page;

/**
 * 按钮级资源服务
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年10月11日 上午9:09:20
 */
public interface BtnResourceService {

	BtnResource getById(int id);

	int insert(BtnResource resource);

	int update(BtnResource resource);

	int delete(List<String> ids);
	
	List<BtnResource> selectAll();

	List<BtnResource> selectAll(int pageNum, int pageSize);

	Page<BtnResource> select(int pageNo, int pageSize, BtnResourceDomain queryRes);

	List<BtnResource> getByUserId(int userId);

	List<BtnResource> getByGroupCode(int userId, String groupCode);

	List<BtnResource> getByGroupCode(String groupCode);
	
	List<BtnResource> getByRoleId(String roleId);

	int linkRole(String roleId, List<String> resIds);
	
	int unlinkRole(String roleId, List<String> resIds);
}
