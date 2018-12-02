package com.carlt.ucenter.cfg;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.carlt.ucenter.filter.LocalRequestFilter;

/**
 * 添加容器过滤器（暂未使用）
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月25日 下午5:02:32
 */
@Deprecated
@Configuration
@ServletComponentScan
public class ServletInitializeConfiguration extends SpringBootServletInitializer {

	@Autowired
	private LocalRequestFilter localRequestFilter;

    @Bean
    public FilterRegistrationBean contextFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(requestFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("localRequestFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
    
    /**
     * 实例化本地请求过滤器
     * @return
     */
    @Bean
    public Filter requestFilter() {
        return localRequestFilter;
    }

}
