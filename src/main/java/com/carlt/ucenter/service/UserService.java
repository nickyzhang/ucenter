package com.carlt.ucenter.service;

import java.util.List;

import com.carlt.ucenter.domain.UserDomain;
import com.carlt.ucenter.exception.RestfulJsonException;
import com.carlt.ucenter.model.User;
import com.github.pagehelper.Page;

/**
 * 用户服务
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月25日 下午5:12:24
 */
public interface UserService {

	User selectByPrimaryKey(int id);

	List<User> selectAll();

	List<User> selectUserByRole(List<String> roleIds);

	Page<User> selectAll(int pageNo, int pageSize, UserDomain queryUser);

	int delete(List<String> ids);

	int insert(User user);

	int updateByPrimaryKeySelective(User user, List<String> allRoleIds);
	
	int updateByToken(User user);

	User selectByUserCode(String userCode);

	int linkRole(String userId, List<String> roleIds);

	int unLinkRole(String userId, List<String> roleIds) throws RestfulJsonException;

	List<User> selectUserByRes(List<String> resIds);

	int logout(int userId);

	int updatePWDByToken(Integer id, String oldPassword, String newPassword);

	boolean userExists(String userCode);
	
}
