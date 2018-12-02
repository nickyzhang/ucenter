package com.carlt.ucenter.msg.receiver;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import com.carlt.ucenter.domain.AuthChangeMsg;
import com.carlt.ucenter.msg.handler.Handler;
import com.carlt.ucenter.util.Utils;

/**
 * 资源权限更改消息接收器
 * 
 * @author ansen.zhu@carlt.com.cn
 * @date 2018年8月7日 上午9:32:49
 */
@Service("authChageRedisMsgReceiver")
public class AuthChageRedisMsgReceiver implements MsgReceiver<String> {

	Logger log = LoggerFactory.getLogger(AuthChageRedisMsgReceiver.class);

	@Autowired
	@Qualifier("userRoleChangeMsgHandler")
	private Handler<AuthChangeMsg> userMsgHandler;

	@Autowired
	@Qualifier("roleChangeMsgHandler")
	private Handler<AuthChangeMsg> roleMsgHandler;

	@Autowired
	@Qualifier("authChangeMsgHandler")
	private Handler<AuthChangeMsg> authMsgHandler;

	/**
	 * 接收到redis消息处理
	 * 
	 * @param msg
	 */
	@Override
	public void receiveMessage(String srcMsg) {

		// 解决string序列化之后不是字符串问题
		// if (!srcMsg.contains("{") || !srcMsg.endsWith("}")) {
		// log.info("received redis msg is not json string, msg:{}", srcMsg);
		// }
		// log.debug("received redis msg:{}", srcMsg);
		// AuthChangeMsg msg = new
		// AuthChangeMsg(srcMsg.substring(srcMsg.indexOf("{")));
		// log.info("received redis msg, type:{}, operate:{}, data:{}",
		// msg.getType(), msg.getOperate(), msg.getData());

		Jackson2JsonRedisSerializer<? extends Object> serializer = Utils.getJsonSerializer();
		Object ret = serializer.deserialize(srcMsg.getBytes());
		AuthChangeMsg msg = null;
		if (ret instanceof AuthChangeMsg)
			msg = (AuthChangeMsg) ret;

		if (msg == null || StringUtils.isEmpty(msg.getType()) || StringUtils.isEmpty(msg.getOperate())
				|| msg.getData() == null)
			return;

		try {
			handle(msg);
		} catch (Exception e) {
			log.error("消息处理异常：" + e.getMessage(), e);
		}
	}

	/**
	 * 处理消息
	 * 
	 * @param msg
	 */
	private void handle(AuthChangeMsg msg) {
		switch (msg.getType()) {
		case "user":
			userMsgHandler.handle(msg);
			break;
		case "role":
			roleMsgHandler.handle(msg);
			break;
		case "res":
			authMsgHandler.handle(msg);
			break;
		default:
			break;
		}
	}
}
