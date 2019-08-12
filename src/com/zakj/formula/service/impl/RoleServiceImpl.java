package com.zakj.formula.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.PrivilegeBean;
import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.dao.privilege.impl.PrivilegeDaoImpl;
import com.zakj.formula.dao.role.IRoleDao;
import com.zakj.formula.dao.role.impl.RoleDaoImpl;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IRoleService;
import com.zakj.formula.utils.C3P0Utils;

public class RoleServiceImpl implements IRoleService {
	
	private IRoleDao dao;

	public RoleServiceImpl(){
		dao = new RoleDaoImpl();
	}
	
	@Override
	public void addRole(RoleBean role) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			dao.add(role);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_ADD);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void deleteRole(Integer id) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			dao.delete(id);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("该角色已被用户使用，无法删除！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void updateRole(RoleBean role) throws CustomException {
		try {
			dao.update(role);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_UPDATE);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public PageBean<RoleBean> showRoleList(int currentPage, int currentCount,
			RoleBean role) throws CustomException {
		PageBean<RoleBean> pageBean = new PageBean<RoleBean>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setCurrentCount(currentCount);
		
		try {
			C3P0Utils.beginTransation();
			List<RoleBean> list = dao.findRoleList(role);
			int totalCount = list.size();
			int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
			pageBean.setTotalPage(totalPage);
			pageBean.setTotalCount(totalCount);
			int start = (currentPage-1)*currentCount;
			if(start > totalCount){
				currentPage = totalPage;
				start = currentPage!=0?((currentPage-1)*currentCount):0;
			}
			int end = currentPage*currentCount;
			end = end > totalCount? totalCount : end;
			pageBean.setList(list.subList(start, end));
			
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
		return pageBean;
	}

	//获取展示指定角色的权限树ztree
	@Override
	public ArrayList<Map<String,Object>> showPrivilegeZtreeList(Integer rid) throws CustomException {
		try {
			PrivilegeDaoImpl privilegeDao = new PrivilegeDaoImpl();
			C3P0Utils.beginTransation();
			//load出所有的权限
			List<PrivilegeBean> privilegeList = privilegeDao.findAllPrilegeList();
			//load出指定角色的权限
			List<PrivilegeBean> rolePrivileges = privilegeDao.findPrivilegeListByRid(rid);
			//对比权限，并拼接成ztree的json格式数据
			ArrayList<Map<String, Object>> mapList = new ArrayList<>(); 
			for (PrivilegeBean privilegeBean : privilegeList) {
				Map<String, Object> map = new HashMap<String, Object>();
	            map.put("id", privilegeBean.getId());  
	            map.put("pId", privilegeBean.getZindex()!=null?privilegeBean.getZindex():0);  
	            map.put("name", privilegeBean.getName());  
	            for(PrivilegeBean rolePrivilege:rolePrivileges){
	                if(rolePrivilege.getId()==privilegeBean.getId()){
	                    map.put("checked", true);
	                }  
	            }  
	            mapList.add(map);  
			} 
			return mapList;
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public List<RoleBean> showRoleNameList() throws CustomException {
		try {
			C3P0Utils.beginTransation();
			List<RoleBean> roleList = dao.findRoleList(new RoleBean());
			ArrayList<RoleBean> roleNames = new ArrayList<RoleBean>();
			RoleBean role = null;
			for (RoleBean roleBean : roleList) {
				role = new RoleBean();
				role.setId(roleBean.getId());
				role.setName(roleBean.getName());
				roleNames.add(role);
			}
			return roleNames;
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
		
	}

	@Override
	public void savePrivilege(RoleBean role) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			dao.saveRolePrivileges(role);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_ADD);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

}
