package com.carlt.ucenter.service;

/**
 * 缓存服务接口
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月25日 下午5:05:56
 * @param <T>
 */
public interface CacheService<T>  {
	
	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	T getCache(String key);
	
	/**
	 * 设置缓存
	 * @param key
	 * @param value
	 */
	void putCache(String key, T value);
	
	/**
	 * 更缓存
	 * @param key
	 * @param value
	 */
	void updateCache(String key, T value);
	
	/**
	 * 删除缓存
	 * @param key
	 */
	void removeCache(String key);
	
	T getHashCache(String key, String hashKey);
	
	/**
	 * 设置HashMap缓存
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	void putHashCache(String key, String hashKey, T value);
	
	/**
	 * 更新HashMap缓存
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	void updateHashCache(String key, String hashKey, T value);
	
	/**
	 * 删除HashMap缓存
	 * @param key
	 * @param hashKey
	 */
	void remoeHashCache(String key, String hashKey);
}
