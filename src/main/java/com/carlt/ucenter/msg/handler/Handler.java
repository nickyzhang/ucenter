package com.carlt.ucenter.msg.handler;

/**
 * 消息处理器
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月25日 下午5:04:38
 * @param <T>
 */
public interface Handler<T> {
	
	/**
	 * 处理消息
	 * @param msg
	 * @return
	 */
	Object handle(T msg);
}
