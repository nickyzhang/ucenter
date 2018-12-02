package com.carlt.ucenter.msg.receiver;

/**
 * 消息接收器
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月22日 下午4:57:36
 * @param <T>
 */
public interface MsgReceiver<T> {
	
	/**
	 * 接收消息
	 * @param srcMsg
	 */
	void receiveMessage(T srcMsg);
}
