package com.carlt.ucenter.service;

import java.util.List;

import com.carlt.ucenter.domain.ResourceDomain;
import com.carlt.ucenter.model.Resource;
import com.github.pagehelper.Page;

/**
 * 资源服务
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月25日 下午5:12:12
 */
public interface ResourceService {

	Resource selectByPrimaryKey(int id);

	List<Resource> selectAllByType(List<String> resTypes);
	
	List<Resource> selectAll();

	List<Resource> selectAll(int pageNo, int pageSize);

	int deleteByPrimaryKey(int id);

	int insert(Resource resource);

	int updateByPrimaryKeySelective(Resource resource);

	List<Resource> getResourceByUser(int userId);

	int deleteInterface(List<String> ids);

	Page<Resource> select(int pageNo, int pageSize, ResourceDomain queryRes);

	int linkParentRes(String pResId, List<String> resList);

	int unlinkParentRes(String pResId, List<String> resList);

	List<Resource> getResourceByPids(List<String> resPids, int resType);

	int deleteMenu(List<String> ids);

	int hasSubResource(List<String> resIds, List<String> resTypes);
}
