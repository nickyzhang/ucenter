package com.carlt.ucenter.msg.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 审计消息发送器
 * @author ansen.zhu@carlt.com.cn
 * @date 2018年8月7日 上午9:33:10
 */
@Service
public class AuditMsgSender<T> {
	
	Logger log = LoggerFactory.getLogger(AuditMsgSender.class);
	
	@Value("${redis.pubsub.audit_topic.name:audit_topic}")
	private String auditTopic;

	@Autowired
	@Qualifier("plainRedisTemplate")
	protected RedisTemplate<String, Object> plainRedisTemplate;
	
	public void sendMessage(T msg) {
		log.info("start send redis msg:{}", msg);
		try {
			plainRedisTemplate.convertAndSend(auditTopic, msg);
			log.info("end send redis msg:{}", msg);
		} catch (Exception e) {
			log.error("send redis");
		}
	}
}
