package com.carlt.ucenter.service.imlp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.carlt.ucenter.service.AbstractCacheService;

@Service
public class AuthInfoCacheService extends AbstractCacheService<Object> {

	@Value("${cache.user.key:authinfo}")
	protected String cacheKey;
	
	@Value("${cache.user.key:-1}")
	protected long cacheExpire;

	@Value("${redis.pubsub.auth_topic.name:auth_topic}")
	private String topic;
	
	@Override
	protected String getCacheKey() {
		return cacheKey;
	}

	@Override
	protected long getCacheExpire() {
		return cacheExpire;
	}
	
	public void sendMsg(Object msg) {
		sendChannelMess(topic, msg);
	}
}
