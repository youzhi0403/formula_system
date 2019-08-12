package com.zakj.formula.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.apache.catalina.User;

import sun.rmi.runtime.Log;

import com.mchange.v2.log.LogUtils;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.PrivilegeBean;
import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.bean.UserBean;
import com.zakj.formula.dao.user.IUserDao;
import com.zakj.formula.dao.user.impl.UserDaoImpl;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.ILoginService;
import com.zakj.formula.service.IUserManagerService;
import com.zakj.formula.utils.C3P0Utils;
import com.zakj.formula.utils.SecurityUtil;

public class LoginServiceImpl implements ILoginService{
	private IUserDao dao;
	
	public LoginServiceImpl() {
		dao = new UserDaoImpl();
	}

	@Override
	public String login(String account, String password, HttpSession session) throws CustomException {
		UserBean user = null;
		try {
			C3P0Utils.beginTransation();
			user = dao.find(account);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("登录异常，无法连接数据库！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
		if (user != null) {
			if(password.equals(user.getPassword())){
				//登录并保存user对象到session中
				IUserManagerService service = new UserManagerServiceImpl();
				List<String> codes = service.findPrivilegeUrlByUID(user.getId());
				session.setAttribute("privilege", codes);
				try {
					//根据用户id获取用户角色
					Set<RoleBean> roles = dao.findRolesByUid(user.getId());
					user.setRoles(roles);
					for (RoleBean roleBean : roles) {
						//根据用户角色获取
						Set<PrivilegeBean> privileges = dao.findPrivilegeByRid(roleBean.getId());
						roleBean.setPrivileges(privileges);
					}
					//获取所有需要进行权限判断的urls
					List<String> urls = dao.findAllPrivilegeUrils();
					user.setUrls(urls);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				session.setAttribute("user", user);
				
				//设置session永久存活
				session.setMaxInactiveInterval(0);
				return user.getName();
			} else {
				throw new CustomException("密码错误！");
			}
		} else {
			throw new CustomException("账号错误！");
		}
	}

	@Override
	public void exit() {
		
	}

	@Override
	public Map<String,Boolean> getUserPrivilege(List<String> codes) {
		Map<String,Boolean> map = new HashMap<String, Boolean>();
		map.put("product", codes.contains("product"));
		map.put("product_add", codes.contains("product_add"));
		map.put("product_delete", codes.contains("product_delete"));
		map.put("product_update", codes.contains("product_update"));
		map.put("product_showlist", codes.contains("product_showlist"));
		map.put("formula", codes.contains("formula"));
		map.put("formula_delete", codes.contains("formula_delete"));
		map.put("formula_update", codes.contains("formula_update"));
		map.put("formula_import", codes.contains("formula_import"));
		map.put("formula_export", codes.contains("formula_export"));
		map.put("formula_showlist", codes.contains("formula_showlist"));
		map.put("material", codes.contains("material"));
		map.put("material_add", codes.contains("material_add"));
		map.put("material_delete", codes.contains("material_delete"));
		map.put("material_update", codes.contains("material_update"));
		map.put("material_showlist", codes.contains("material_showlist"));
		map.put("supplier", codes.contains("supplier"));
		map.put("supplier_add", codes.contains("supplier_add"));
		map.put("supplier_delete", codes.contains("supplier_delete"));
		map.put("supplier_update", codes.contains("supplier_update"));
		map.put("supplier_showlist", codes.contains("supplier_showlist"));
		map.put("system", codes.contains("system"));
		map.put("user", codes.contains("user"));
		map.put("role", codes.contains("role"));
		map.put("user_add", codes.contains("user_add"));
		map.put("user_update", codes.contains("user_update"));
		map.put("user_delete", codes.contains("user_delete"));
		map.put("user_showlist", codes.contains("user_showlist"));
		map.put("role_add", codes.contains("role_add"));
		map.put("role_update", codes.contains("role_update"));
		map.put("role_delete", codes.contains("role_delete"));
		map.put("role_showlist", codes.contains("role_showlist"));
		map.put("role_privilege_setting", codes.contains("role_privilege_setting"));
		map.put("product_SC", codes.contains("product_SC"));
		map.put("product_SC_add", codes.contains("product_SC_add"));
		map.put("product_SC_update", codes.contains("product_SC_update"));
		map.put("product_SC_delete", codes.contains("product_SC_delete"));
		map.put("product_SC_showlist", codes.contains("product_SC_showlist"));
		return map;
	}
	
}
