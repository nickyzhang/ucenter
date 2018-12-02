package com.carlt.ucenter.msg.handler;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlt.ucenter.domain.AuthChangeMsg;
import com.carlt.ucenter.model.AuthInfo;
import com.carlt.ucenter.model.User;
import com.carlt.ucenter.service.AuthorityService;
import com.carlt.ucenter.service.TokenService;
import com.carlt.ucenter.service.UserService;
import com.carlt.ucenter.util.Utils;
import com.github.pagehelper.util.StringUtil;

/**
 * 角色更改消息处理器
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月25日 下午5:04:56
 */
@Service("roleChangeMsgHandler")
public class RoleChangeMsgHandler implements Handler<AuthChangeMsg> {

	Logger log = LoggerFactory.getLogger(RoleChangeMsgHandler.class);

	@Autowired
	private TokenService tokenService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private UserService userService;

	@Override
	public Object handle(AuthChangeMsg msg) {

		log.info("role change msg handler start to handle msg, operate:{}, data:{}", msg.getOperate(), msg.getData());

		switch (msg.getOperate()) {
		case "delete":
			doDeleteOpt((List<?>) msg.getData());
			break;
		case "linkRes":
			doLinkResOpt((List<?>) msg.getData());
			break;
		default:
			break;
		}

		return null;
	}

	private void doLinkResOpt(List<?> data) {
		String roleIds = data.get(0).toString();
		List<User> users = userService.selectUserByRole(Arrays.asList(roleIds.split(",")));

		if (users.isEmpty())
			return;

		users.forEach(user -> {
			String token = tokenService.getToken(user.getId());

			if (StringUtil.isEmpty(token)) {
				log.info("user not login(token is null), no need to update auth cache, userId:{}", user.getId());
				return;
			}

			// 如果用户已经登录，则更新用户的缓存信息
			List<AuthInfo> infos = authorityService.selectByUserId(user.getId());
			if (!infos.isEmpty()) {
				tokenService.putCacheByToken(token, "authinfo", infos.get(0));
				if (Utils.hasSuperRole(infos.get(0)))
					tokenService.putCacheByToken(token, "superrole", "1");
			}
		});
	}

	private void doDeleteOpt(List<?> data) {

		if (data == null || data.isEmpty())
			return;

		for (int i = 0; i < data.size(); i++) {
			int userId = Integer.valueOf(data.get(i).toString());
			String token = tokenService.getToken(userId);

			if (StringUtil.isEmpty(token)) {
				log.info("user not login(token is null), no need to update auth cache, userId:{}", userId);
				return;
			}
			// 如果用户已经登录，则更新用户的缓存信息
			List<AuthInfo> infos = authorityService.selectByUserId(userId);
			if (!infos.isEmpty()) {
				tokenService.putCacheByToken(token, "authinfo", infos.get(0));
				if (Utils.hasSuperRole(infos.get(0)))
					tokenService.putCacheByToken(token, "superrole", "1");
				log.info("user auth cache updated, userId:{}", userId);
			}
		}
	}

}
