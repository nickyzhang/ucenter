package com.carlt.ucenter.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.carlt.ucenter.domain.MenuInfo;
import com.carlt.ucenter.exception.RestfulJsonException;
import com.carlt.ucenter.model.AuthInfo;
import com.carlt.ucenter.model.Resource;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageException;

/**
 * 工具类
 * 
 * @author ansen.zhu@carlt.com.cn

 
 * @version 1.0
 * @date 2018年8月9日 下午12:57:27
 */
public class Utils {

	private static final Object syncObj = new Object();

	private static Jackson2JsonRedisSerializer<? extends Object> serializer;

	public static boolean hasSuperRole(AuthInfo auth) {
//		return auth.getRoles().stream().filter(role -> role.getRoleId() == Constant.ADMIN_ROLE_ID).count() > 0;
		return auth.getRoles().stream().filter(role -> role.getRoleType() == Constant.ADMIN_ROLE_TYPE).count() > 0;
	}

	private static List<MenuInfo> treeMenuList(List<MenuInfo> menuList, int parentCode) {
		List<MenuInfo> menus = new ArrayList<>();
		for (MenuInfo menu : menuList) {
			int id = menu.getId();
			int pid = menu.getPid();
			if (parentCode == pid) {
				List<MenuInfo> childMenus = treeMenuList(menuList, id);
				menu.setSubMenus(childMenus);
				menus.add(menu);
			}
		}

		sortMenus(menus);

		return menus;
	}

	/**
	 * 对菜单进行排序
	 * 
	 * @param menus
	 */
	private static void sortMenus(List<MenuInfo> menus) {
		Collections.sort(menus, new MenuInfoComparator());
		menus.forEach(menu -> sortMenus(menu.getSubMenus()));
	}

	/**
	 * 根据资源列表和资源类型生成资源树
	 * @param authInfo
	 * @param resTypes
	 * @return
	 */
	public static Object generateMenu(AuthInfo authInfo, Set<Integer> resTypes) {
		Set<Resource> reses = new HashSet<>();
		Map<Integer, Resource> cache = new HashMap<>();
		authInfo.getRoles().forEach(role -> {
			role.getResources().forEach(res -> {
				if(! cache.containsKey(res.getId())) {
					reses.add(res);
					cache.put(res.getId(), res);
				}
			});
		});

		return generateMenu(new ArrayList<>(reses), resTypes);
	}

	/**
	 * 
	 * 根据资源列表和资源类型生成资源树
	 * @param authInfo
	 * @param resTypes
	 * @return
	 */
	public static Object generateMenu(List<Resource> reses, Set<Integer> resTypes) {
		MenuInfo root = null;
		boolean findRoot = false;
		List<MenuInfo> menus = new ArrayList<>();
		List<MenuInfo> rootMenus = new ArrayList<>();

		reses.forEach(res -> {
			// 过滤不需要的资源类型
			if(resTypes == null || resTypes.isEmpty() || resTypes.contains(res.getResType())) { 
				menus.add(MenuInfo.resourceToMenu(res));
			}
		});
		sortMenus(menus);

		for (MenuInfo menu : menus) {
			if (menu.getId() < 1)
				continue;
			if (menu.getId() == Constant.RES_ROOT_ID) {
				root = menu;
				findRoot = true;
			} else if (root == null) {
				root = menu;
				rootMenus.add(menu);
			} else if (!findRoot) {
				if (root.getPid() == menu.getPid()) {
					rootMenus.add(menu);
				}
			}
		}

		if (root == null)
			return Collections.emptyList();

		if (findRoot)
			return treeMenuList(menus, root.getId());

		List<MenuInfo> retMenues = new ArrayList<>();
		rootMenus.forEach(menu -> {
			menu.setSubMenus(treeMenuList(menus, menu.getId()));
			retMenues.add(menu);
		});

		return retMenues;
	}

	public static void handleRestfulJsonException(Exception e) throws RestfulJsonException {
		if (e instanceof RestfulJsonException) {
			throw (RestfulJsonException) e;
		}
		throw new RestfulJsonException(e.getMessage(), e);
	}

	public static void handlePageException(Exception e) throws PageException {
		if (e instanceof PageException) {
			throw (PageException) e;
		}
		throw new PageException(e.getMessage(), e);
	}

	public static Jackson2JsonRedisSerializer<? extends Object> getJsonSerializer() {

		synchronized (syncObj) {
			if (serializer != null)
				return serializer;

			Jackson2JsonRedisSerializer<Object> ret = new Jackson2JsonRedisSerializer<Object>(Object.class);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
			objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
			ret.setObjectMapper(objectMapper);
			
			serializer = ret;
			return serializer;
		}
	}

	public static String getIPAddress(HttpServletRequest request) {
		String ip = null;

		// X-Forwarded-For：Squid 服务代理
		String ipAddresses = request.getHeader("X-Forwarded-For");

		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// Proxy-Client-IP：apache 服务代理
			ipAddresses = request.getHeader("Proxy-Client-IP");
		}

		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// WL-Proxy-Client-IP：weblogic 服务代理
			ipAddresses = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// HTTP_CLIENT_IP：有些代理服务器
			ipAddresses = request.getHeader("HTTP_CLIENT_IP");
		}

		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// X-Real-IP：nginx服务代理
			ipAddresses = request.getHeader("X-Real-IP");
		}

		// 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
		if (ipAddresses != null && ipAddresses.length() != 0) {
			ip = ipAddresses.split(",")[0];
		}

		// 还是不能获取到，最后再通过request.getRemoteAddr();获取
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
