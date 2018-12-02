package com.carlt.ucenter.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.carlt.ucenter.domain.AuditDomain;
import com.carlt.ucenter.model.AuditInfo;
import com.carlt.ucenter.service.AuditService;
import com.carlt.ucenter.util.Constant;
import com.github.pagehelper.Page;

/**
 * 审计控制器
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月15日 上午11:51:24
 *
 */
@RestController
@RequestMapping("/api/audit")
public class AuditController {

	Logger log = LoggerFactory.getLogger(AuditController.class);

	@Autowired
	private AuditService auditService;

	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public Map<String, Object> selectByPage(int pageNo, int pageSize, AuditDomain auditDomain) {

		Map<String, Object> ret = new HashMap<>();

		if (!StringUtils.isEmpty(auditDomain.getSortField()))
			auditDomain.setSortFiled();
			
		Page<AuditInfo> pageRet = auditService.select(pageNo, pageSize, auditDomain);

		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("total", pageRet.getTotal());
		ret.put("pages", pageRet.getPages());
		ret.put("curPageNum", pageRet.getPageNum());
		ret.put("pageSize", pageRet.getPageSize());
		ret.put("data", pageRet.getResult());

		return ret;
	}
}
