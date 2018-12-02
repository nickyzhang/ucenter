package com.carlt.ucenter.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.carlt.ucenter.domain.UserDomain;
import com.carlt.ucenter.model.User;

/**
 * 用户数据映射
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月25日 下午4:42:58
 */
public interface UserMapper {

	/**
	 * 根据主键获取用户信息
	 * @param id
	 * @return
	 */
	User selectByPrimaryKey(int id);

	/**
	 * 查询所有用户
	 * @return
	 */
	List<User> selectAll();

	/**
	 * 根据条件查询用户信息
	 * @param queryUser
	 * @return
	 */
	List<User> select(UserDomain queryUser);

	/**
	 * 根据主键删除用户
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(int id);

	/**
	 * 根据主键批量删除用户
	 * @param ids
	 * @return
	 */
	int delete(@Param("ids") List<String> ids);

	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	int insert(User user);

	/**
	 * 更新用户
	 * @param user
	 * @return
	 */
	int updateByPrimaryKeySelective(User user);

	/**
	 * 根据用户编码查询用户
	 * @param userCode
	 * @return
	 */
	User selectByUserCode(String userCode);

	/**
	 * 用户关联角色
	 * @param userId 用户主键
	 * @param roleIds 资源主键列表
	 * @return
	 */
	int linkRoles(@Param("userId") String userId, @Param("roleIds") List<String> roleIds);

	/**
	 * 解除用户关联角色
	 * @param userId 用户主键
	 * @param roleIds 资源主键列表
	 * @return
	 */
	int unlinkRoles(@Param("userId") String userId, @Param("roleIds") List<String> roleIds);

	/**
	 * 解除用户所有角色关联
	 * @param userIds
	 * @return
	 */
	int unlinkUserRoles(@Param("userIds") List<String> userIds);

	/**
	 * 根据角色ID列表查询用户列表
	 * @param roleIds
	 * @return
	 */
	List<User> selectUserByRole(@Param("roleIds") List<String> roleIds);

	/**
	 * 根据资源ID列表查询用户列表
	 * @param resIds
	 * @return
	 */
	List<User> selectUserByRes(@Param("resIds") List<String> resIds);

	/**
	 * 根据token查询用户后更新用户信息
	 * @param user
	 * @return
	 */
	int updateByToken(User user);

	/**
	 * 用户登出（更新用户的登录状态）
	 * @param userId
	 * @return
	 */
	int logout(int userId);

	/**
	 * 修改用户密码
	 * @param id 用户ID
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return
	 */
	int updatePWDByToken(@Param("id") Integer id, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);

}
