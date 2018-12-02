package com.carlt.ucenter.service.imlp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carlt.ucenter.domain.BtnResourceDomain;
import com.carlt.ucenter.mapper.BtnResourceMapper;
import com.carlt.ucenter.model.BtnResource;
import com.carlt.ucenter.service.BtnResourceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 按钮级资源服务实现
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年10月11日 上午10:02:28
 */
@Service
public class BtnResServiceImpl implements BtnResourceService {

	Logger log = LoggerFactory.getLogger(BtnResServiceImpl.class);

	@Autowired
	private BtnResourceMapper btnResourceMapper;

	@Override
	public BtnResource getById(int id) {
		return btnResourceMapper.getById(id);
	}

	@Override
	public int insert(BtnResource resource) {
		return btnResourceMapper.insert(resource);
	}

	@Override
	public int update(BtnResource resource) {
		return btnResourceMapper.update(resource);
	}

	@Override
	public List<BtnResource> selectAll() {
		return btnResourceMapper.selectAll();
	}

	@Override
	public List<BtnResource> selectAll(int pageNum, int pageSize) {
		Page<BtnResource> page = PageHelper.startPage(pageNum, pageSize, true);
		log.debug("total Resource : {}", page.getTotal());
		log.debug("ret Resources : {}", page.getResult());
		return btnResourceMapper.selectAll();
	}

	@Override
	public Page<BtnResource> select(int pageNo, int pageSize,
			BtnResourceDomain queryRes) {
		Page<BtnResource> page = PageHelper.startPage(pageNo, pageSize, true);
		btnResourceMapper.select(queryRes);
		log.debug("total Resource : {}", page.getTotal());
		log.debug("ret Resources : {}", page.getResult());

		Page<BtnResource> pageRet = new Page<>(pageNo, pageSize);
		pageRet.setTotal(page.getTotal());
		pageRet.addAll(page.getResult());

		return pageRet;
	}

	@Override
	public List<BtnResource> getByUserId(int userId) {
		return btnResourceMapper.getByUserId(userId);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public int delete(List<String> ids) {
		int num = btnResourceMapper.delete(ids);
		if (num > 0) {
			btnResourceMapper.unlinkRoles(ids);
		}
		return num;
	}

	@Override
	public List<BtnResource> getByGroupCode(int userId, String groupCode) {
		return btnResourceMapper.getByGroupCodeByUserId(userId, groupCode);
	}

	@Override
	public List<BtnResource> getByRoleId(String roleId) {
		return btnResourceMapper.getByRoleId(roleId);
	}

	@Override
	public List<BtnResource> getByGroupCode(String groupCode) {
		return btnResourceMapper.getByGroupCode(groupCode);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public int linkRole(String roleId, List<String> resIds) {

		final List<String> addedResIds = new ArrayList<>();
		List<String> newResIds = resIds;
		List<BtnResource> addedReses = btnResourceMapper.getByRoleId(roleId);

		if (addedReses != null && !addedReses.isEmpty()) {
			addedResIds.addAll(addedReses.stream()
					.filter(res -> res != null && res.getId() > 0)
					.map(res -> res.getId() + "").distinct()
					.collect(Collectors.toList()));
			newResIds = resIds.stream().filter(id -> !addedResIds.contains(id))
					.distinct().collect(Collectors.toList());
		}

		if (!newResIds.isEmpty()) {
			return btnResourceMapper.linkRole(roleId, newResIds);
		}

		return 0;
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public int unlinkRole(String roleId, List<String> resIds) {
		return btnResourceMapper.unlinkRole(roleId, resIds);
	}

}
