package com.carlt.ucenter.msg.handler;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

@Service("authChangeMsgHandler")
public class AuthChangeMsgHandler implements Handler<AuthChangeMsg> {

	Logger log = LoggerFactory.getLogger(AuthChangeMsgHandler.class);

	@Autowired
	private TokenService tokenService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private UserService userService;

	@Override
	public Object handle(AuthChangeMsg msg) {

		log.info("res msg handler start to handle msg:{}", msg.getData());
		if (msg.getData() == null)
			return null;

		switch (msg.getOperate()) {
		case "add":
			doAddOpt((List<?>) msg.getData());
			break;
		case "delete":
			doDeleteOpt((List<?>) msg.getData());
			break;
		case "linkPRes":
			doLinkPResOpt((List<?>) msg.getData());
			break;
		case "unlinkPRes":
			doUnlinkPResOpt((List<?>) msg.getData());
			break;
		default:
			break;
		}

		return msg;
	}

	private void doUnlinkPResOpt(List<?> data) {
		if (data.isEmpty() || data.size() < 2 || null == data.get(0) || StringUtils.isEmpty(data.get(0).toString()))
			return;
		List<String> resIds = Arrays.asList(data.get(1).toString().trim().split(","));
		List<User> users = userService.selectUserByRes(resIds);
		// 循环更新用户权限
		users.forEach(user -> updateUserAuth(user.getId()));
	}

	private void doLinkPResOpt(List<?> data) {
		doAddOpt(data);
	}

	private void doAddOpt(List<?> data) {
		if (data.isEmpty() || null == data.get(0))
			return;
		String pid = data.get(0).toString();
		if (StringUtils.isEmpty(pid))
			return;
		List<User> users = userService.selectUserByRes(Arrays.asList(pid));
		// 循环更新用户权限
		users.forEach(user -> updateUserAuth(user.getId()));
	}

	private void updateUserAuth(int userId) {
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

	private void doDeleteOpt(List<?> data) {
		if (data.isEmpty())
			return;
		data.forEach(t -> {
			if (null == t || StringUtils.isEmpty(t.toString()))
				return;
			int userId = Integer.valueOf(t.toString());
			updateUserAuth(userId);
		});
	}
}
