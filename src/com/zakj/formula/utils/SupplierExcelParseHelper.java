package com.zakj.formula.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.bean.ProductBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.exception.CustomException;

public class SupplierExcelParseHelper {
	
	private List<SupplierBean> sList = new ArrayList<>();
	private SupplierBean supplier;
	private ArrayList<String> errorMsg = new ArrayList<>();
	private boolean isStop = false;

	public List<SupplierBean> getsList() {
		return sList;
	}

	public void parseXls(int rowIndex, int columnIndex, String value, List<String> supplierNames) throws CustomException {
		//从第二行开始解析
		if (rowIndex > 0 && !isStop){
			//判断单元格是否为空
			switch (columnIndex) {
			case 0: //联系人
				errorMsg.clear();
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				supplier = new SupplierBean();
				supplier.setContact(value.trim());
				break;
			case 1: //公司名称
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				} else if (supplierNames.contains(value.trim())){
					throw new CustomException("解析失败，第"+(rowIndex+1)+"行，供应商“"+(supplierNames.indexOf(value.trim()))+"”已存在！");
				}
				supplier.setCompanyName(value.trim());
				break;
			case 2: //地址
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				supplier.setAddress(value.trim());
				break;
			case 3: //电话
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					//判断是否全部为空
				}
				if (errorMsg.size() == 4){
					//证明已经到了结束的地方
					isStop = true;
					return;
				} else if (errorMsg.size() > 0){
					throw new CustomException(errorMsg.get(0));
				}
				supplier.setTelephone(value.trim());
				sList.add(supplier);
				break;
			default:
				break;
			}
		}
	}
	
}
