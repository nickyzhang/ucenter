package com.carlt.ucenter.mapper;

import java.util.List;

import com.carlt.ucenter.model.AuthInfo;

/**
 * 权限数据映射
 * @author ansen.zhu@carlt.com.cn

 
 * @version 1.0
 * @date 2018年8月25日 下午4:42:28
 */
public interface AuthorityMapper {

	/**
	 * 根据用户ID获取权限信息
	 * @param userId
	 * @return
	 */
	List<AuthInfo> selectByUserId(int userId);

	/**
	 * 根据用户编码获取权限信息
	 * @param userCode
	 * @return
	 */
	List<AuthInfo> selectByUserCode(String userCode);
	
}
