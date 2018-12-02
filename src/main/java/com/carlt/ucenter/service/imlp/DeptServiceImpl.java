package com.carlt.ucenter.service.imlp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlt.ucenter.mapper.DeptMapper;
import com.carlt.ucenter.model.Dept;
import com.carlt.ucenter.service.DeptService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class DeptServiceImpl implements DeptService {
	
	Logger log = LoggerFactory.getLogger(DeptServiceImpl.class);

	@Autowired
	private DeptMapper deptMapper;

	@Override
	public Dept selectByPrimaryKey(int id) {
		return deptMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Dept> selectAll() {
		return deptMapper.selectAll();
	}

	@Override
	public int deleteByPrimaryKey(int id) {
		return deptMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Dept dept) {
		return deptMapper.insert(dept);
	}

	@Override
	public int updateByPrimaryKeySelective(Dept dept) {
		return deptMapper.updateByPrimaryKeySelective(dept);
	}

	@Override
	public int updateByPrimaryKey(Dept dept) {
		return deptMapper.updateByPrimaryKey(dept);
	}

	/*
	 * 这个方法中用到了我们开头配置依赖的分页插件pagehelper
	 * 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可； pageNum 开始页数 pageSize
	 * 每页显示的数据条数
	 */
	@Override
	public List<Dept> selectAll(int pageNum, int pageSize) {
		Page<Dept> page = PageHelper.startPage(pageNum, pageSize, true);
		log.debug("total depts : ", page.getTotal());
		log.debug("ret detps:", page.getResult());
		return deptMapper.selectAll();
	}

}
