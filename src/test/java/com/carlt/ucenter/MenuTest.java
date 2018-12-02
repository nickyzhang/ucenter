package com.carlt.ucenter;
//package com.hncy58.bigdata.gateway;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONObject;
//
//import com.hncy58.bigdata.gateway.domain.MenuInfo;
//import com.hncy58.bigdata.gateway.model.Resource;
//import com.hncy58.bigdata.gateway.util.Utils;
//
//public class MenuTest {
//
//	public static void main(String[] args) {
//		testGenerateMenuList();
//	}
//
//	public static void testGenerateMenuList() {
//
//		List<Resource> reses = new ArrayList<>();
//		Resource res0 = new Resource(0, 1, "root", "", "01", "", 1, "");
//		Resource res1 = new Resource(1, 1, "test1", "01", "0101", "", 1, "");
//		Resource res2 = new Resource(1, 1, "test1", "01", "0102", "", 2, "");
//		Resource res3 = new Resource(1, 1, "test1", "0101", "010102", "", 1, "");
//		Resource res4 = new Resource(1, 1, "test1", "0101", "010101", "", 2, "");
//		Resource res5 = new Resource(1, 1, "test1", "01", "0103", "", 1, "");
//		Resource res6 = new Resource(0, 1, "test1", "01", "0104", "", 2, "");
//		reses.add((res0));
//		reses.add((res1));
//		reses.add((res2));
//		reses.add((res3));
//		reses.add((res4));
//		reses.add((res5));
//		reses.add((res6));
//
//		System.out.println(JSONObject.wrap(Utils.generateMenu(reses)));
//	}
//
//	public static List<MenuInfo> treeMenuList(List<MenuInfo> menuList, String parentCode) {
//		List<MenuInfo> menus = new ArrayList<>();
//		for (MenuInfo menu : menuList) {
//			String code = menu.getResCode();
//			String pCode = menu.getpResCode();
//			if (parentCode.equals(pCode)) {
//				List<MenuInfo> childMenus = treeMenuList(menuList, code);
//				menu.setSubMenus(childMenus);
//				menus.add(menu);
//			}
//		}
//		return menus;
//	}
//
//}
