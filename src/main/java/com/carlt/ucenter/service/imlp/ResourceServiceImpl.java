package com.carlt.ucenter.service.imlp;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carlt.ucenter.domain.ResourceDomain;
import com.carlt.ucenter.mapper.ResourceMapper;
import com.carlt.ucenter.mapper.RoleMapper;
import com.carlt.ucenter.model.Resource;
import com.carlt.ucenter.model.Role;
import com.carlt.ucenter.service.ResourceService;
import com.carlt.ucenter.util.Constant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class ResourceServiceImpl implements ResourceService {

	Logger log = LoggerFactory.getLogger(ResourceServiceImpl.class);

	@Autowired
	private ResourceMapper resourceMapper;
	
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public Resource selectByPrimaryKey(int id) {
		return resourceMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Resource> selectAllByType(List<String> resTypes) {
		return resourceMapper.selectAllByType(resTypes);
	}

	@Override
	public int deleteByPrimaryKey(int id) {
		return resourceMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Resource resource) {
		return resourceMapper.insert(resource);
	}

	@Override
	public int updateByPrimaryKeySelective(Resource resource) {
		return resourceMapper.updateByPrimaryKey(resource);
	}

	/*
	 * 这个方法中用到了我们开头配置依赖的分页插件pagehelper
	 * 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可； pageNum 开始页数 pageSize
	 * 每页显示的数据条数
	 */
	@Override
	public List<Resource> selectAll(int pageNum, int pageSize) {
		Page<Resource> page = PageHelper.startPage(pageNum, pageSize, true);
		log.debug("total Resource : {}", page.getTotal());
		log.debug("ret Resources : {}", page.getResult());
		return resourceMapper.selectAll();
	}

	@Override
	public Page<Resource> select(int pageNo, int pageSize, ResourceDomain queryRes) {
		Page<Resource> page = PageHelper.startPage(pageNo, pageSize, true);
		resourceMapper.select(queryRes);
		log.debug("total user : {}", page.getTotal());
		log.debug("ret users : {}", page.getResult());

		Page<Resource> pageRet = new Page<>(pageNo, pageSize);
		pageRet.setTotal(page.getTotal());
		pageRet.addAll(page.getResult());

		return pageRet;
	}

	@Override
	public List<Resource> getResourceByUser(int userId) {
		return resourceMapper.getResourceByUser(userId);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public int deleteInterface(List<String> ids) {
		int num = resourceMapper.deleteInterface(ids);
		if (num > 0) {
			resourceMapper.unlinkRole(ids);
		}
		return num;
	}
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public int deleteMenu(List<String> ids) {
		int num = resourceMapper.deleteMenu(ids);
		if (num > 0) {
			int rows = resourceMapper.unlinkRole(ids);
			rows = resourceMapper.unlinkRoleByPid(ids);
			rows = resourceMapper.updateResesPidByPid("-1", ids);
		}
		return num;
	}

	@Override
	public List<Resource> selectAll() {
		return resourceMapper.selectAll();
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public int linkParentRes(String pResId, List<String> resIds) {
		int rows = resourceMapper.updateResesPid(pResId, resIds);
		log.info("linkParentRes, pResId:{}, resIds:{}, rows:{}", pResId, resIds, rows);
		// 更新父资源关联角色的资源
		if(rows > 0) {
			List<Role> roles = roleMapper.getRoleByResId(resIds);
			List<String> roleIds = roles.stream().map(role -> role.getId() + "").distinct().collect(Collectors.toList());
			roleIds.forEach(roleId -> {
				int num = roleMapper.unlinkReses(roleId, resIds);
				log.info("unlinkReses, pResId:{}, roleId:{}, unlinkNum:{}", pResId, roleId, num);
				num = roleMapper.linkReses(roleId, resIds);
				log.info("linkParentRes, pResId:{}, roleId:{}, linkNum:{}", pResId, roleId, num);
			});
		}
		return rows;
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public int unlinkParentRes(String pResId, List<String> resIds) {
		int rows = resourceMapper.updateResesPid(pResId, resIds);
		log.info("unlinkParentRes, pResId:{}, resIds:{}, rows:{}", pResId, resIds, rows);
		// 更新父资源关联角色的资源
		if(rows > 0) {
			List<Role> roles = roleMapper.getRoleByResId(resIds);
			List<String> roleIds = roles.stream().map(role -> role.getId() + "").distinct().collect(Collectors.toList());
			roleIds.forEach(roleId -> {
				int unlinkNum = roleMapper.unlinkReses(roleId, resIds);
				log.info("linkParentRes, pResId:{}, roleId:{}, unlinkNum:{}", pResId, roleId, unlinkNum);
			});
		}
		return rows;
	}
	
	@Override
	public List<Resource> getResourceByPids(List<String> resPids, int resType) {
		return resourceMapper.getResourceByPids(resPids, resType);
	}

	@Override
	public int hasSubResource(List<String> resIds, List<String> resTypes) {
		return resourceMapper.hasSubResource(resIds, resTypes);
	}

}
