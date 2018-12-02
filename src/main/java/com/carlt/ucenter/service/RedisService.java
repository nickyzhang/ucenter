package com.carlt.ucenter.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis服务抽象类
 * 
 * @author ansen.zhu@carlt.com.cn
 * @date 2018年8月7日 上午9:33:24
 * @param <T>
 */
public abstract class RedisService<T> {

	@Autowired
	@Qualifier("redisTemplate")
	protected RedisTemplate<String, Object> redisTemplate;
	@Autowired
	@Qualifier("plainRedisTemplate")
	protected RedisTemplate<String, Object> plainRedisTemplate;
	@Resource
	protected HashOperations<String, String, T> hashOperations;

	
	
	// 向通道发送消息的方法
	public void sendChannelMess(String channel, T doamin) {
		plainRedisTemplate.convertAndSend(channel, doamin);
	}

	/**
	 * 添加
	 *
	 * @param key
	 *            key
	 * @param doamin
	 *            对象
	 * @param expire
	 *            过期时间(单位:秒),传入 -1 时表示不设置过期时间
	 */
	public void put(String key, String hashKey, T doamin, long expire) {
		hashOperations.put(key, hashKey, doamin);
		if (expire != -1) {
			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
		}
	}

	/**
	 * 删除
	 *
	 * @param key
	 *            传入key的名称
	 */
	public void remove(String key, String hashKey) {
		hashOperations.delete(key, hashKey);
	}

	/**
	 * 查询
	 *
	 * @param key
	 *            查询的key
	 * @return
	 */
	public T get(String key, String hashKey) {
		return hashOperations.get(key, hashKey);
	}

	/**
	 * 获取当前redis库下所有对象
	 *
	 * @return
	 */
	public List<T> getAll(String key) {
		return hashOperations.values(key);
	}

	/**
	 * 查询查询当前redis库下所有key
	 *
	 * @return
	 */
	public Set<String> getKeys(String key) {
		return hashOperations.keys(key);
	}

	/**
	 * 判断key是否存在redis中
	 *
	 * @param key
	 *            传入key的名称
	 * @return
	 */
	public boolean isKeyExists(String key, String hashKey) {
		return hashOperations.hasKey(key, hashKey);
	}

	/**
	 * 查询当前key下缓存数量
	 *
	 * @return
	 */
	public long count(String key) {
		return hashOperations.size(key);
	}

	/**
	 * 清空redis
	 */
	public void empty(String key) {
		Set<String> set = hashOperations.keys(key);
		set.stream().forEach(hashKey -> hashOperations.delete(key, hashKey));
	}
}
