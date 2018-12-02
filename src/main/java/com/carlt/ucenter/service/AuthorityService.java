package com.carlt.ucenter.service;

import java.util.List;

import com.carlt.ucenter.model.AuthInfo;

/**
 * 权限服务
 * @author ansen.zhu@carlt.com.cn

 
 * @version 1.0
 * @date 2018年8月25日 下午5:12:03
 */
public interface AuthorityService {

	List<AuthInfo> selectByUserId(int id);
	
	List<AuthInfo> selectByUserCode(String userCode);
}
