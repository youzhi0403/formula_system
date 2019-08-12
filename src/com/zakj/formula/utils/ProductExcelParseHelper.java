package com.zakj.formula.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.bean.ProductBean;
import com.zakj.formula.exception.CustomException;

public class ProductExcelParseHelper {
	
	private List<ProductBean> pList = new ArrayList<>();
	private ProductBean product;
	private ArrayList<String> errorMsg = new ArrayList<>();
	private boolean isStop = false;

	public List<ProductBean> getpList() {
		return pList;
	}

	public void parseExcel(int rowIndex, int columnIndex, String value, List<String> productNames) throws CustomException {
		//从第二行开始解析
		if (rowIndex > 0 && !isStop){
			//判断单元格是否为空
			switch (columnIndex) {
			case 0: //产品名称
				errorMsg.clear();
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				} else if (productNames.contains(value.trim())){
					throw new CustomException("第"+(rowIndex+1)+"行，产品名称为“"+(value.trim())+"”已存在！");
				}
				product = new ProductBean();
				product.setName(value);
				break;
			case 1: //配方编号
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				product.setNumber(value);
				break;
			case 2: //主要成分
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				product.setMajor_composition(value);
				break;
			case 3: //配方描述
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				product.setFormula_desc(value);
				break;
			case 4://配方成本
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				try{
					product.setPrice(new BigDecimal(value.trim()));
				} catch(NumberFormatException e){
					throw new CustomException((rowIndex+1)+"行   "+(columnIndex+1)+"列，配方成本必须为纯数字！");
				}
				break;
			case 5://留样量
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				if (errorMsg.size() == 4){
					//证明已经到了结束的地方
					isStop = true;
					return;
				} else if (errorMsg.size() > 0){
					throw new CustomException(errorMsg.get(0));
				}
				try{
					product.setSample_quantity(new BigDecimal(value.trim()));
				} catch(NumberFormatException e){
					throw new CustomException((rowIndex+1)+"行   "+(columnIndex+1)+"列，留样量必须为纯数字！");
				}
				pList.add(product);
				break;

			default:
				break;
			}
		}
	}
	
}
