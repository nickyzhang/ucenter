package com.carlt.ucenter.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存抽象类
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月25日 下午5:08:33
 * @param <T>
 */
public abstract class AbstractCacheService<T> extends RedisService<T> implements CacheService<T> {

	/**
	 * 获取缓存KEY
	 * @return
	 */
	protected abstract String getCacheKey();

	/**
	 * 获取缓存过期时间
	 * @return
	 */
	protected abstract long getCacheExpire();

	/**
	 * 本地K-V缓存
	 */
	protected final Map<String, T> localCache = new ConcurrentHashMap<String, T>();
	/**
	 * 本地K-HashMap缓存
	 */
	protected final Map<String, Map<String, T>> localHashCache = new ConcurrentHashMap<String, Map<String, T>>();

	@Override
	public T getCache(String key) {
		if (localCache.containsKey(key)) {
			return localCache.get(key);
		}

		T value = get(getCacheKey(), key);
		if (value != null) {
			put(getCacheKey(), key, value, getCacheExpire());
		}
		return value;
	}

	@Override
	public void putCache(String key, T value) {
		if (value != null) {
			put(getCacheKey(), key, value, getCacheExpire());
			localCache.put(key, value);
		}
	}

	@Override
	public void updateCache(String key, T value) {
		putCache(key, value);
	}

	@Override
	public void removeCache(String key) {
		localCache.remove(key);
		remove(getCacheKey(), key);
		
	}

	@Override
	public T getHashCache(String key, String hashKey) {
		if (localHashCache.containsKey(key) && localHashCache.get(key).containsKey(hashKey)) {
			return localHashCache.get(key).get(hashKey);
		}

		T value = get(key, hashKey);
		updateLocalHashCache(key, hashKey, value);

		return value;
	}

	/**
	 * 更新本地HashMap缓存
	 * 
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	private void updateLocalHashCache(String key, String hashKey, T value) {
		if (localHashCache.containsKey(key)) {
			localHashCache.get(key).put(hashKey, value);
		} else {
			Map<String, T> map = new ConcurrentHashMap<>();
			map.put(hashKey, value);
			localHashCache.put(key, map);
		}
	}

	@Override
	public void putHashCache(String key, String hashKey, T value) {
		put(key, hashKey, value, getCacheExpire());
		updateLocalHashCache(key, hashKey, value);
	}

	@Override
	public void updateHashCache(String key, String hashKey, T value) {
		putHashCache(key, hashKey, value);
	}

	@Override
	public void remoeHashCache(String key, String hashKey) {
		remove(key, hashKey);
		if(localHashCache.containsKey(key))
			localHashCache.get(key).remove(hashKey);
	}
	
	protected void clearLocalCache() {
		synchronized (this) {
			localCache.clear();
		}
	}
	
	protected void clearLocalHashCache() {
		synchronized (this) {
			localHashCache.clear();
		}
	}
	
}
