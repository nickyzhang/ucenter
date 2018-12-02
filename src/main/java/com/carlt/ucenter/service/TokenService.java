package com.carlt.ucenter.service;

/**
 * Token服务
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月25日 下午5:09:28
 */
public interface TokenService {
	
	/**
	 * 生成token
	 * @param userId
	 * @return
	 */
	String generateToken(int userId);
	
	/**
	 * 移除token
	 * @param token
	 */
	void removeToken(String token);
	
	/**
	 * 验证token是否存在
	 * @param token
	 * @return
	 */
	boolean validateToken(String token);
	
	/**
	 * 获取缓存
	 * @param token
	 * @param key
	 * @return
	 */
	Object getCacheFromToken(String token, String key);
	
	/**
	 * 获取缓存
	 * @param token
	 * @param key
	 * @param t
	 */
	void putCacheByToken(String token, String key, Object t);
	
	/**
	 * 判断用户是否已经缓存
	 * @param userId
	 * @return
	 */
	boolean exists(int userId);
	
	/**
	 * 通过用户ID获取token
	 * @param userId
	 * @return
	 */
	String getToken(int userId);
	
	/**
	 * 设置用户token缓存
	 * @param userId
	 * @param token
	 */
	void putToken(int userId, String token);
	
}
