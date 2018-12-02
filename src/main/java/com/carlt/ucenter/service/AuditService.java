package com.carlt.ucenter.service;

import com.carlt.ucenter.domain.AuditDomain;
import com.carlt.ucenter.model.AuditInfo;
import com.github.pagehelper.Page;

/**
 * 审计服务
 * @author ansen.zhu@carlt.com.cn

 
 * @version 1.0
 * @date 2018年8月25日 下午5:11:50
 */
public interface AuditService {

	int save(AuditInfo audit);

	Page<AuditInfo> select(int pageNo, int pageSize, AuditDomain queryAudit);
}
