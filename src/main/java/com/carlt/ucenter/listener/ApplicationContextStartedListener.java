package com.carlt.ucenter.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/***
 * 初始化时间监听器
 * @author ansen.zhu@carlt.com.cn
 * @company hisunpay 高阳通联
 * @website http://www.hisunpay.com
 * @date 2018年8月6日 下午6:52:23
 */
@Component
@Deprecated
public class ApplicationContextStartedListener {

	Logger log = LoggerFactory.getLogger(ApplicationContextStartedListener.class);
	
	@EventListener
	public void contextRefreshedEvent(ContextClosedEvent event) {
		log.info("contextRefreshedEvent trigger...");
	}
	
	@EventListener
	public void contextRefreshedEvent(ContextRefreshedEvent event) {
		log.info("contextRefreshedEvent trigger...");
	}
}
