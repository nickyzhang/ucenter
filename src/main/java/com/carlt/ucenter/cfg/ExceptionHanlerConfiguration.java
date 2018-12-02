package com.carlt.ucenter.cfg;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.carlt.ucenter.domain.ErrorInfo;
import com.carlt.ucenter.exception.PageException;
import com.carlt.ucenter.exception.RestfulJsonException;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.DefaultRateLimiterErrorHandler;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.RateLimiterErrorHandler;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 统一异常处理
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月8日 下午5:25:07
 */
@Configuration
@ControllerAdvice
public class ExceptionHanlerConfiguration {

	Logger log = LoggerFactory.getLogger(ExceptionHanlerConfiguration.class);

	public static final String DEFAULT_ERROR_VIEW = "error";

	@ExceptionHandler(value = PageException.class)
	public ModelAndView pageExceptionHanler(HttpServletRequest req, Exception e) throws Exception {

		log.error("捕获到全局Page异常:" + e.getMessage(), e);

		ModelAndView mav = new ModelAndView();
		mav.addObject("code", "9997");
		mav.addObject("msg", "捕获到全局Page异常:" + e.getMessage());
		mav.addObject("exception", e);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName(DEFAULT_ERROR_VIEW);

		return mav;
	}

	@ExceptionHandler(value = RestfulJsonException.class)
	@ResponseBody
	public Map<String, Object> restfulJsonException(HttpServletRequest req, Exception e) throws Exception {

		log.error("捕获到全局Restful异常:" + e.getMessage(), e);
		Map<String, Object> ret = new HashMap<>();
		ErrorInfo<Object> err = new ErrorInfo<>();

		err.setMessage(e.getMessage());
		err.setCode(ErrorInfo.ERROR);
		err.setData(e.getCause());
		err.setUrl(req.getRequestURL().toString());

		ret.put("code", "9998");
		ret.put("msg", "捕获到全局API接口异常:" + e.getMessage());
		ret.put("data", err);

		return ret;
	}

	/**
	 * 默认所有异常全部以Json返回，除非有特殊定义
	 * 
	 * @param req
	 * @param e
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Map<String, Object> globalException(HttpServletRequest req, Exception e) throws Exception {

		log.error("捕获到全局异常:" + e.getMessage(), e);
		Map<String, Object> ret = new HashMap<>();
		ErrorInfo<Object> err = new ErrorInfo<>();

		err.setMessage(e.getMessage());
		err.setCode(ErrorInfo.ERROR);
		err.setData(e.getCause());
		err.setUrl(req.getRequestURL().toString());

		ret.put("code", "9999");
		ret.put("msg", "捕获到全局异常:" + e.getMessage());
		ret.put("data", err);

		return ret;
	}

	/**
	 * 重写网关的异常属性，输出系统自定义的公异常属性
	 * 
	 * @return
	 */
	@Bean
	public DefaultErrorAttributes errorAttributes() {
		return new DefaultErrorAttributes() {
			@Override
			public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
					boolean includeStackTrace) {

				ZuulException exception = (ZuulException) requestAttributes
						.getAttribute("javax.servlet.error.exception", RequestAttributes.SCOPE_REQUEST);
				int statusCode = (int) requestAttributes.getAttribute("javax.servlet.error.status_code",
						RequestAttributes.SCOPE_REQUEST);
				String msg = (String) requestAttributes.getAttribute("javax.servlet.error.message",
						RequestAttributes.SCOPE_REQUEST);
				Map<String, Object> errorAttributes = new HashMap<>();
				Throwable cause = exception != null ? exception.getCause() : null;
				
				errorAttributes.put("code", "9994");
				errorAttributes.put("statusCode", statusCode);

				if (!StringUtils.isEmpty(msg)) {
					errorAttributes.put("msg", msg);
				} else {
					errorAttributes.put("msg", null != cause ? exception.getMessage() : "网关捕获到异常");
				}

				return errorAttributes;
			}
		};
	}

	/**
	 * 限流错误处理
	 * 
	 * @return
	 */
	@Bean
	public RateLimiterErrorHandler rateLimitErrorHandler() {
		return new DefaultRateLimiterErrorHandler() {

			/**
			 * 限流错误统一返回码
			 */
			private static final String errorCode = "9996";

			@Override
			public void handleSaveError(String key, Exception e) {
				generateResponseBody(RequestContext.getCurrentContext(), errorCode,
						key + ", handle save error:" + e.getMessage());
			}

			@Override
			public void handleFetchError(String key, Exception e) {
				generateResponseBody(RequestContext.getCurrentContext(), errorCode,
						key + ", handle fetch error:" + e.getMessage());
			}

			@Override
			public void handleError(String msg, Exception e) {
				generateResponseBody(RequestContext.getCurrentContext(), errorCode,
						msg + ", handle error:" + e.getMessage());
			}

			private Object generateResponseBody(RequestContext ctx, String code, String msg) {
				Map<String, Object> ret = new HashMap<>();
				ret.put("code", code);
				ret.put("statusCode", ctx.get("error.status_code"));
				ret.put("msg", msg);
				// 过滤该请求，不往下级服务去转发请求，到此结束
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(200);
				ctx.setResponseBody(JSONObject.wrap(ret).toString());
				setResponseAttrs(ctx.getResponse());
				return null;
			}

			/**
			 * 设置响应属性
			 * 
			 * @param response
			 */
			private void setResponseAttrs(HttpServletResponse response) {
				response.setContentType("application/json;charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.setLocale(new java.util.Locale("zh", "CN"));
			}
		};
	}
}
