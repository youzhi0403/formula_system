package com.zakj.formula.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.exception.CustomException;

public class MaterialExcelParseHelper {
	
	private List<MaterialBean> mList = new ArrayList<MaterialBean>();
	private MaterialBean material;
	private ArrayList<String> errorMsg = new ArrayList<>();
	private boolean isStop = false;

	public List<MaterialBean> getmList() {
		return mList;
	}

	public void parseXls(int rowIndex, int columnIndex, String value, List<String> materialCodes) throws CustomException {
		//从第二行开始解析
		if (rowIndex > 0){
			//判断单元格是否为空
			switch (columnIndex) {
			case 0: //商品名
				errorMsg.clear();
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				material = new MaterialBean();
				material.setName(value.trim());
				break;
			case 1: //中文名
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				material.setChineseName(value.trim());
				break;
			case 2: //代码
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				} else if (materialCodes.contains(value.trim())){
					throw new CustomException("解析失败，第"+(rowIndex+1)+"行，原料代码为“"+(materialCodes.indexOf(value.trim()))+"”已存在！");
				}
				material.setCode(value.trim());
				break;
			case 3: //单价
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				try{
					material.setPrice(new BigDecimal(value));
				} catch (NumberFormatException e){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不是纯数字！");
				}
				break;
			case 4://单位
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				material.setUnit(value.trim());
				break;
			case 5://产地
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				material.setOrigin(value.trim());
				break;
			case 6://外观状态
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				material.setMApparentState(value.trim());
				break;
			case 7://用途
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				material.setApplication(value.trim());
				break;
			case 8://中文INCI
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				material.setInci_cn(value.trim());
				break;
			case 9://英文INCI
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				material.setInci_en(value.trim());
				break;
			case 10://包装方式
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				material.setPackingWay(value.trim());
				break;
			case 11://供应商名字
				if (StringUtils.isEmpty(value, true)){
					errorMsg.add("解析失败，第"+(rowIndex+1)+"行，第"+(columnIndex+1)+"列不能为空！");
					return;
				}
				if (errorMsg.size() == 4){
					//证明已经到了结束的地方
					isStop = true;
					return;
				} else if (errorMsg.size() > 0){
					throw new CustomException(errorMsg.get(0));
				}
				material.setSupplier(value.trim());
				mList.add(material);
				break;

			default:
				break;
			}
		}
	}
	
}
