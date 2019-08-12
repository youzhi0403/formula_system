package com.zakj.formula.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.zakj.formula.exception.CustomException;

public interface ILoginService {
	/**
	 * 登录
	 * 
	 * @param account 账号
	 * @param password 密码
	 * @return 0，成功登录     1，密码错误   -1，账号错误
	 * @throws CustomException 
	 */
	public String login(String account, String password, HttpSession session) throws CustomException;
	
	/**
	 * 退出
	 */
	public void exit();
	
	/**
	 * 获取用户的权限map
	 * 
	 * @param session
	 * @return 返回一个权限map对象，key为权限标志，value为权限的开启（true）或关闭（false）
	 */
	public  Map<String, Boolean> getUserPrivilege(List<String> codes);
}
