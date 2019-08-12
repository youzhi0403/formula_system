package com.zakj.formula.dao.formula.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.ws.org.objectweb.asm.Type;
import com.zakj.formula.bean.FMaterial;
import com.zakj.formula.bean.FormulaDesc;
import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.dao.formula.IFormulaDao;
import com.zakj.formula.utils.C3P0Utils;
import com.zakj.formula.utils.DBHelper;

public class FormulaDaoImpl implements IFormulaDao {

	@Override
	public void deleteFormulaDesc(Integer id) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call deleteFormulaDesc(?)}");
		cstmt.setObject(1, id);
		cstmt.executeUpdate();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public void addFormulaDesc(FormulaDesc formulaDesc) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call insertFormulaDesc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		DBHelper.prepareCall(cstmt, 
				null,
				formulaDesc.getF_name(),
				formulaDesc.getP_code(), 
				formulaDesc.getF_number(),
				formulaDesc.getP_name(), 
				formulaDesc.getBatch_number(),
				formulaDesc.getPlain_amount(), 
				formulaDesc.getActual_output(),
				formulaDesc.getWater_ph(), 
				formulaDesc.getEle_conductivity(),
				formulaDesc.getEquipment_state(),
				formulaDesc.getProduct_date(),
				formulaDesc.getPhysicochemical_target(),
				formulaDesc.getEngineer(), 
				formulaDesc.getMaterial_weigher(),
				formulaDesc.getMaterial_checker(),
				formulaDesc.getMaterial_distributor(),
				formulaDesc.getSupervisor(), 
				formulaDesc.getTechnology_proc(),
				formulaDesc.getException_record(),
				formulaDesc.getAttention_item(),
				formulaDesc.getEmulStartTime(), 
				formulaDesc.getEmulEndTime());
		cstmt.registerOutParameter(1, Type.INT);
		cstmt.executeUpdate();
		Integer fid = cstmt.getInt(1);

		List<FMaterial> fmlist = formulaDesc.getFmlist();
		cstmt = conn.prepareCall("{call insertF_Material(?,?,?,?,?,?,?,?)}");
		for (FMaterial fMaterial : fmlist) {
			DBHelper.prepareCall(cstmt, 
					fMaterial.getGroup(),
					fMaterial.getName(), 
					fMaterial.getPlan_amount(),
					fMaterial.getActual_amount(), 
					fMaterial.getM_batch_num(),
					fMaterial.getChecked_weight(), 
					fMaterial.getMid(), 
					fid);
			cstmt.addBatch();
		}
		cstmt.executeBatch();
		C3P0Utils.close(cstmt, null);
	}

	/**
	 * 检查配方原料列表中的原料是否在原料库中
	 * 
	 * @param fMaterialList
	 * @return 全部都在则返回 0 ， 有不在的，则返回相应的配方原料的下标
	 * @throws SQLException
	 */
	@Override
	public int checkFMaterialList(List<FMaterial> fMaterialList)
			throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call selectMaterialByName(?)}");
		ResultSet resultSet = null;
		for (FMaterial fMaterial : fMaterialList) {
			cstmt.setObject(1, fMaterial.getName());
			resultSet = cstmt.executeQuery();
			if (!resultSet.next()) {
				C3P0Utils.close(cstmt, resultSet);
				return fMaterialList.indexOf(fMaterial);
			} else {
				fMaterial.setFmId(resultSet.getInt(1));
			}
			C3P0Utils.close(cstmt, resultSet);
		}
		return 0;
	}

	@Override
	public PageBean<FormulaDesc> findFormulaDescList(FormulaDesc formulaDesc,
			PageBean<FormulaDesc> pageBean) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call pro_FormulaDescPagination(?,?,?,?)}");
		DBHelper.prepareCall(cstmt, 
				formulaDesc.getF_name()==null?"":formulaDesc.getF_name(), 
				formulaDesc.getP_code()==null?"":formulaDesc.getP_code(),
				pageBean.getLimit(),
				pageBean.getCurrentCount());
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<FormulaDesc> formulaDescList = new ArrayList<FormulaDesc>();
		while (resultSet.next()) {
			formulaDesc = new FormulaDesc();
			formulaDesc.setId(resultSet.getInt("id"));
			formulaDesc.setF_name(resultSet.getString("formula_name"));
			formulaDesc.setP_code(resultSet.getString("product_code"));
			formulaDesc.setF_number(resultSet.getString("formula_number"));
			formulaDesc.setP_name(resultSet.getString("product_name"));
			formulaDescList.add(formulaDesc);
		}
		
		cstmt = conn.prepareCall("{call formulaTotalCount()}");
		resultSet = cstmt.executeQuery();
		if(resultSet.next()) {
			pageBean.setTotalCount(resultSet.getInt(1));
		}

		ArrayList<FMaterial> FMaterialList = null;
		FMaterial fMaterial = null;
		cstmt = conn.prepareCall("{call selectFM_Material(?)}");
		for (FormulaDesc formulaDesc2 : formulaDescList) {
			FMaterialList = new ArrayList<FMaterial>();
			DBHelper.prepareCall(cstmt, formulaDesc2.getId());
			resultSet = cstmt.executeQuery();
			while (resultSet.next()) {
				fMaterial = new FMaterial();
				fMaterial.setPlan_amount(resultSet.getBigDecimal("plan_amount"));
				fMaterial.setPrice(resultSet.getBigDecimal("price"));
				fMaterial.setUnit(resultSet.getString("unit"));
				fMaterial.setName(resultSet.getString("name"));
				fMaterial.setCode(resultSet.getString("code"));
				fMaterial.setGroup(resultSet.getString("group"));
				FMaterialList.add(fMaterial);
			}
			formulaDesc2.setFmlist(FMaterialList);
			FMaterialList = null;
		}
		pageBean.setList(formulaDescList);
		pageBean.setTotalPage((int) Math.ceil(1.0 * pageBean.getTotalCount()
				/ pageBean.getCurrentCount()));
		C3P0Utils.close(cstmt, resultSet);
		return pageBean;
	}

	@Override
	public FormulaDesc findFormulaDescById(Integer id) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call selectFormulaDescById(?)}");
		DBHelper.prepareCall(cstmt, id);
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<FormulaDesc> formulaDescList = new ArrayList<FormulaDesc>();
		FormulaDesc formulaDesc = null;
		while (resultSet.next()) {
			formulaDesc = new FormulaDesc();
			formulaDesc.setId(resultSet.getInt(1));
			formulaDesc.setF_name(resultSet.getString(2));
			formulaDesc.setP_code(resultSet.getString(3));
			formulaDesc.setF_number(resultSet.getString(4));
			formulaDesc.setP_name(resultSet.getString(5));
			formulaDesc.setBatch_number(resultSet.getString(6));
			formulaDesc.setPlain_amount(resultSet.getBigDecimal(7));
			formulaDesc.setActual_output(resultSet.getBigDecimal(8));
			formulaDesc.setWater_ph(resultSet.getBigDecimal(9));
			formulaDesc.setEle_conductivity(resultSet.getBigDecimal(10));
			formulaDesc.setEquipment_state(resultSet.getString(11));
			formulaDesc.setProduct_date(resultSet.getString(12));
			formulaDesc.setPhysicochemical_target(resultSet.getString(13));
			formulaDesc.setEngineer(resultSet.getString(14));
			formulaDesc.setMaterial_weigher(resultSet.getString(15));
			formulaDesc.setMaterial_checker(resultSet.getString(16));
			formulaDesc.setMaterial_distributor(resultSet.getString(17));
			formulaDesc.setSupervisor(resultSet.getString(18));
			formulaDesc.setTechnology_proc(resultSet.getString(19));
			formulaDesc.setException_record(resultSet.getString(20));
			formulaDesc.setAttention_item(resultSet.getString(21));
			formulaDesc.setEmulStartTime(resultSet.getString(22));
			formulaDesc.setEmulEndTime(resultSet.getString(23));
			formulaDescList.add(formulaDesc);
		}

		if (formulaDesc == null) {
			return null;
		}
		ArrayList<FMaterial> fMaterialList = new ArrayList<FMaterial>();
		FMaterial fMaterial = null;
		cstmt = conn.prepareCall("{call selectSingleFM_Material(?)}");
		DBHelper.prepareCall(cstmt, formulaDesc.getId());
		resultSet = cstmt.executeQuery();
		while (resultSet.next()) {
			fMaterial = new FMaterial();
			fMaterial.setGroup(resultSet.getString("group"));
			fMaterial.setName(resultSet.getString("name"));
			fMaterial.setCn_name(resultSet.getString("cn_name"));
			fMaterial.setPlan_amount(resultSet.getBigDecimal("plan_amount"));
			fMaterial.setActual_amount(resultSet.getBigDecimal("actual_amout"));
			fMaterial.setM_batch_num(resultSet.getString("m_batch_num"));
			fMaterial.setChecked_weight(resultSet.getBigDecimal("checked_weight"));
			fMaterial.setPrice(resultSet.getBigDecimal("price"));
			fMaterial.setUnit(resultSet.getString("unit"));
			fMaterial.setInci_cn(resultSet.getString("inci_cn"));
			fMaterial.setInci_en(resultSet.getString("inci_en"));
			fMaterial.setCode(resultSet.getString("code"));
			fMaterialList.add(fMaterial);
		}
		formulaDesc.setFmlist(fMaterialList);
		C3P0Utils.close(cstmt, resultSet);
		return formulaDesc;
	}

	@Override
	/**
	 * author:cwj
	 */
	public List<String> updateFormulaDesc(FormulaDesc formulaDesc)
			throws SQLException {
		// 用一个List来 装不存在的原料名字
		List<String> list = new ArrayList<String>();
		ResultSet rs = null;
		CallableStatement cstmt = null;
		Connection conn = C3P0Utils.getCurrentConnection();
		// 首先根据获取所有的原料，根据名字一一去比对，查看有没有这个原料,错误则返回
		cstmt = conn
				.prepareCall("{call proc_Material_commodityName_select(?)}");
		for (FMaterial fMaterial : formulaDesc.getFmlist()) {
			cstmt.setString(1, fMaterial.getName());
			boolean flag = cstmt.execute();
			if (flag) {
				ResultSet resultSet = cstmt.getResultSet();
				if (resultSet.next()) {
					fMaterial.setMid(resultSet.getInt("id"));
				} else {
					list.add(fMaterial.getName());
				}
			}
		}

		if (list.size() > 0) {
			// 把list return回去，待会改
			return list;
		}

		// 先update formulaDesc 表
		cstmt = conn
				.prepareCall("{call proc_FormulaDesc_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		cstmt.setObject(1, formulaDesc.getF_name());
		cstmt.setObject(2, formulaDesc.getP_code());
		cstmt.setObject(3, formulaDesc.getF_number());
		cstmt.setObject(4, formulaDesc.getP_name());
		cstmt.setObject(5, formulaDesc.getBatch_number());
		cstmt.setObject(6, formulaDesc.getPlain_amount());
		cstmt.setObject(7, formulaDesc.getActual_output());
		cstmt.setObject(8, formulaDesc.getWater_ph());
		cstmt.setObject(9, formulaDesc.getEle_conductivity());
		cstmt.setObject(10, formulaDesc.getEquipment_state());
		cstmt.setObject(11, formulaDesc.getProduct_date());
		cstmt.setObject(12, formulaDesc.getPhysicochemical_target());
		cstmt.setObject(13, formulaDesc.getEngineer());
		cstmt.setObject(14, formulaDesc.getMaterial_weigher());
		cstmt.setObject(15, formulaDesc.getMaterial_checker());
		// 原料分配员
		cstmt.setObject(16, formulaDesc.getMaterial_distributor());
		cstmt.setObject(17, formulaDesc.getSupervisor()); // 主管
		// 乳化工艺记录,
		StringBuffer sb = new StringBuffer("");
		for (String str : formulaDesc.getProcList()) {
			sb.append(str + "<>");
		}
		cstmt.setObject(18, sb.toString());
		// 乳化异常记录，需要分割字段存入list
		cstmt.setObject(19, formulaDesc.getException_record());
		// 注意事项
		StringBuffer sb1 = new StringBuffer("");
		for (String str : formulaDesc.getAttentionList()) {
			sb1.append(str + "<>");
		}
		cstmt.setObject(20, sb1.toString());
		cstmt.setObject(21, formulaDesc.getEmulStartTime());
		cstmt.setObject(22, formulaDesc.getEmulEndTime());
		cstmt.setObject(23, formulaDesc.getId());
		cstmt.executeUpdate();

		// 3.根据id删除F_Material
		cstmt = conn.prepareCall("{call proc_F_Material_delete(?)}");
		cstmt.setObject(1, formulaDesc.getId());
		cstmt.executeUpdate();

		// 4.insert数据
		cstmt = conn
				.prepareCall("{call proc_F_Material_insert(?,?,?,?,?,?,?,?)}");
		for (FMaterial fMaterial : formulaDesc.getFmlist()) {
			DBHelper.prepareCall(cstmt, 
					fMaterial.getGroup(),
					fMaterial.getName(), 
					fMaterial.getPlan_amount(),
					fMaterial.getActual_amount(), 
					fMaterial.getM_batch_num(),
					fMaterial.getChecked_weight(), 
					fMaterial.getMid(),
					formulaDesc.getId());
			cstmt.addBatch();
		}
		cstmt.executeBatch();
		C3P0Utils.close(cstmt, rs);
		// 再删除 formulaDescID=? 的数据
		// 将数据一一插入 F_Material表

		return list;
	}

	@Override
	public List<MaterialBean> findMaterialNameList(MaterialBean material)
			throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call pro_M_Name(?)}");
		DBHelper.prepareCall(cstmt, material.getName());
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<MaterialBean> list = new ArrayList<MaterialBean>();
		while (resultSet.next()) {
			material = new MaterialBean();
			material.setId(resultSet.getInt("id"));
			material.setName(resultSet.getString("code"));
			list.add(material);
		}
		C3P0Utils.close(cstmt, resultSet);
		return list;
	}

	@Override
	public List<MaterialBean> findAllMaterialList() throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call selectAllMaterial()}");
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<MaterialBean> list = new ArrayList<MaterialBean>();
		MaterialBean material = null;
		while (resultSet.next()) {
			material = new MaterialBean();
			material.setId(resultSet.getInt("id"));
			material.setName(resultSet.getString("name"));
			material.setOrigin(resultSet.getString("origin"));
			material.setMApparentState(resultSet.getString("mApparentState"));
			material.setSid(resultSet.getInt("supplierId"));
			material.setPrice(resultSet.getBigDecimal("price"));
			material.setUnit(resultSet.getString("unit"));
			material.setCode(resultSet.getString("code"));
			material.setInci_cn(resultSet.getString("inci_cn"));
			material.setInci_en(resultSet.getString("inci_en"));
			material.setApplication(resultSet.getString("application"));
			material.setPackingWay(resultSet.getString("packingWay"));
			list.add(material);
		}
		C3P0Utils.close(cstmt, resultSet);
		return list;
	}
}
