package com.carlt.ucenter.service.imlp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carlt.ucenter.domain.RoleDomain;
import com.carlt.ucenter.exception.RestfulJsonException;
import com.carlt.ucenter.mapper.ResourceMapper;
import com.carlt.ucenter.mapper.RoleMapper;
import com.carlt.ucenter.model.Resource;
import com.carlt.ucenter.model.Role;
import com.carlt.ucenter.service.RoleService;
import com.carlt.ucenter.util.Constant;
import com.carlt.ucenter.util.Utils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class RoleServiceImpl implements RoleService {

	Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private ResourceMapper resourceMapper;

	@Override
	public Role selectByPrimaryKey(int id) {
		return roleMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Role> selectAll() {
		return roleMapper.selectAll();
	}

	@Override
	public int deleteByPrimaryKey(int id) {
		return roleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Role role) {
		return roleMapper.insert(role);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public int updateByPrimaryKeySelective(Role role, List<String> resIds) {
		int num = roleMapper.updateByPrimaryKeySelective(role);
		if (num > 0 && resIds != null) {
			int resNum = linkRes(role.getId() + "", resIds);
			log.info("update role:{}, update res num:{}", role.getId(), resNum);
		}
		return num;
	}

	@Override
	public int updateByPrimaryKey(Role role) {
		return roleMapper.updateByPrimaryKey(role);
	}

	/*
	 * 这个方法中用到了我们开头配置依赖的分页插件pagehelper
	 * 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可； pageNum 开始页数 pageSize
	 * 每页显示的数据条数
	 */
	@Override
	public Page<Role> select(int pageNo, int pageSize, RoleDomain roleDomain) {
		Page<Role> page = PageHelper.startPage(pageNo, pageSize, true);
		roleMapper.select(roleDomain);
		log.debug("total user : {}", page.getTotal());
		log.debug("ret users : {}", page.getResult());

		Page<Role> pageRet = new Page<>(pageNo, pageSize);
		pageRet.setTotal(page.getTotal());
		pageRet.addAll(page.getResult());

		return pageRet;
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public int linkRes(String roleId, List<String> resIds) {

		List<Resource> linkedReses = getResourceByRole(Integer.parseInt(roleId));
		// 待关联资源列表
		List<String> addResIds = new ArrayList<>();
		// 未关联资源列表
		List<String> unlinkResIds = new ArrayList<>();
		// 所有待关联资源子资源列表
		List<Resource> allChildReses = new ArrayList<>();
		// 获取所有菜单资源的子资源
		if (null != resIds && !resIds.isEmpty())
			allChildReses = resourceMapper.getResourceByPids(resIds, Constant.API_RES_TYPE);
		// 如果存在资资源关联则加入到关联列表中
		List<String> newResIds = new ArrayList<>();
		newResIds.addAll(resIds);
		if (!allChildReses.isEmpty()) {
			// 加入子节点资源并去重
			newResIds.addAll(allChildReses.stream().filter(res -> res != null && res.getId() > 0)
					.map(res -> res.getId() + "").distinct().collect(Collectors.toList()));
		}
		// 对已经待关联和已关联的资源进行差集计算
		if (!linkedReses.isEmpty()) {
			fillUnlinkAndAddResIds(unlinkResIds, addResIds, newResIds, linkedReses);
		} else {
			// 如果角色之前没有关联任何资源，则直接关联当前待关联的资源
			addResIds = resIds;
		}

		int added = 0;
		int deleted = 0;
		if (!unlinkResIds.isEmpty()) {
			deleted = roleMapper.unlinkReses(roleId, unlinkResIds);
			log.info("unlink roles, user:{}, roles:{}, ret:{}", roleId, unlinkResIds, deleted);
		}

		if (!addResIds.isEmpty()) {
			added = roleMapper.linkReses(roleId, addResIds);
			log.info("add roles, user:{}, roles:{}, ret:{}", roleId, addResIds, added);
		}
		// 返回删除和新添加的资源总和
		return deleted + added;
	}

	/**
	 * 填充需要移除和增加的资源关联关系
	 * 
	 * @param unlinkResIds
	 *            待移除的资源
	 * @param addResIds
	 *            待增加的资源
	 * @param allResIds
	 *            所有用户关联的资源
	 * @param linkedReses
	 *            已经关联的资源
	 */
	private void fillUnlinkAndAddResIds(List<String> unlinkResIds, List<String> addResIds, List<String> allResIds,
			List<Resource> linkedReses) {

		Set<String> linkedRoleIds = linkedReses.stream().filter(res -> res != null && res.getId() > 0)
				.map(res -> res.getId() + "").distinct().collect(Collectors.toSet());
		addResIds.addAll(
				allResIds.stream().filter(id -> !linkedRoleIds.contains(id)).distinct().collect(Collectors.toList()));
		unlinkResIds.addAll(
				linkedRoleIds.stream().filter(id -> !allResIds.contains(id)).distinct().collect(Collectors.toList()));
	}

	@Override
	public List<Role> getRoleByUserId(String userId) throws RestfulJsonException {
		try {
			return roleMapper.getRoleByUserId(userId);
		} catch (Exception e) {
			Utils.handleRestfulJsonException(e);
		}
		return Collections.emptyList();
	}

	@Override
	public List<Resource> getResourceByRole(int roleId) {
		return roleMapper.getResourceByRole(roleId);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public int delete(List<String> ids) {
		int num = roleMapper.delete(ids);
		// 删除成功之后需要同时删除角色和资源以及用户的关联关系
		if (num > 0) {
			roleMapper.unlinkRoleReses(ids);
			roleMapper.unlinkRoleUsers(ids);
		}
		return num;
	}

}
