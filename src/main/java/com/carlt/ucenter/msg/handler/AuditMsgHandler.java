package com.carlt.ucenter.msg.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlt.ucenter.model.AuditInfo;
import com.carlt.ucenter.service.AuditService;

/**
 * 审计消息处理器
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月25日 下午5:04:21
 */
@Service("auditMsgHandler")
public class AuditMsgHandler implements Handler<AuditInfo> {

	Logger log = LoggerFactory.getLogger(AuditMsgHandler.class);

	@Autowired
	private AuditService auditService;

	@Override
	public Object handle(AuditInfo msg) {

		log.info("res msg handler start to handle msg:{}", msg);
		if (msg == null)
			return null;

		int ret = auditService.save(msg);

		log.info("audit info save ret:{}", ret);

		return ret;
	}
}
