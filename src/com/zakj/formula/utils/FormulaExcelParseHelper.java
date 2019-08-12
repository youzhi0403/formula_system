package com.zakj.formula.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;

import com.zakj.formula.bean.FMaterial;
import com.zakj.formula.bean.FormulaDesc;
import com.zakj.formula.exception.CustomException;

public class FormulaExcelParseHelper {

	private  String group; // 原料列表中的组别
	private  boolean isMaterialList = true; // 判断是否开始和结束原料列表的数据
	private  int attentionRow = -1; // 注意事项的开始行数
	private  int exceptionRow = -1; //异常记录的开始行数
	private  int finalRow = -1; // 最后一行的行数
	private  ArrayList<FMaterial> list = new ArrayList<FMaterial>();
	private  FMaterial fMaterial = null;
	private  FormulaDesc formulaDesc = new FormulaDesc();  //封装了配方详情的实体
	private  ArrayList<String> fmaterialExceptions = new ArrayList<String>();
	
	public void handleRightCell(int rowIndex, int columnIndex, String value) {
		if (isMaterialList && rowIndex >= 7) {
			if (columnIndex == 9) {
				if (value.contains("乳化异常记录")) {
					isMaterialList = false;
					exceptionRow = rowIndex + 1;
					return;
				}
				if (formulaDesc.getTechnology_proc() == null) {
					formulaDesc.setTechnology_proc(value + "<>");
				} else {
					formulaDesc.setTechnology_proc(formulaDesc
							.getTechnology_proc() + value + "<>");
				}
			}
		}
		if (exceptionRow == rowIndex) {
			if (columnIndex == 9) {
				formulaDesc.setException_record(value);
			}
		}
	}

	public void handleCell(int rowIndex, int columnIndex, String value,
			Cell cell) throws CustomException {
		// 处理表头
		switch (rowIndex) {
			case 1: // 第一行  配方名
				if (columnIndex == 0) {
					if(StringUtils.isEmpty(value, true)){
//						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，配方名为空！");
						formulaDesc.setF_name("");
					} else {
						formulaDesc.setF_name(value.trim());
					}
				}
				break;
			case 2: // 第二行
				if (columnIndex == 0) { // 第1列    产品代码
					Matcher matcher = Pattern.compile("产品代码(:|：)(.*)").matcher(value.trim());
					if (matcher.find()) {
						if(StringUtils.isEmpty(matcher.group(2), true)){
//							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，产品代码为空！");
							//如果为null或者空串，都赋值空串，不让数据库中值为null。
							formulaDesc.setP_code("");
						} else {
							formulaDesc.setP_code(matcher.group(2).trim());
						}
					} else {
						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，内容格式错误！必须为“产品代码：”开头。");
					}
				}
				if (columnIndex == 9) { // 第9列   配方编号
					Matcher matcher = Pattern.compile("配方编号(:|：)(.*)").matcher(value.trim());
					if (matcher.find()) {
						if(StringUtils.isEmpty(matcher.group(2), true))
							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，配方不能为空！");
						formulaDesc.setF_number(matcher.group(2).trim());
					} else {
						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，内容格式错误！必须为“配方编号：”开头。");
					}
				}
				break;
			case 3: // 第三行
				if (columnIndex == 0) { //第1列   产品名称
					Matcher matcher = Pattern.compile("产品名称(:|：)(.*)").matcher(value.trim());
					if (matcher.find()) {
						if(StringUtils.isEmpty(matcher.group(2), true))
							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，产品名称为空！");
						formulaDesc.setP_name(matcher.group(2).trim());
					} else {
						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，内容格式错误！必须为“产品名称：”开头。");
					}
				}
				if (columnIndex == 6) { //第6列  批号
					Matcher matcher = Pattern.compile("批号(:|：)(.*)").matcher(value.trim());
					if (matcher.find()) {
						if(StringUtils.isEmpty(matcher.group(2), true)){
//							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，批号为空！");
							formulaDesc.setBatch_number("");
						} else {
							formulaDesc.setBatch_number(matcher.group(2).trim());
						}
					} else {
						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，内容格式错误！必须为“批号：”开头。");
					}
				}
				if(columnIndex == 10){  //第10列  计划量
//					Matcher matcher = Pattern.compile("计划量(:|：)(.*)").matcher(value);
//					if (matcher.find()) {
						if(StringUtils.isEmpty(value, true)){
							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，计划量为空！");
						} else {
							try {
								formulaDesc.setPlain_amount(new BigDecimal(value.trim()));
							} catch (NumberFormatException e){
								throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，计划量必须为数字！");
							}
						}
//					} else {
//						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，内容格式错误");
//					}
				}
				if (columnIndex == 13) { //第13列  实产量
//					Matcher matcher = Pattern.compile("实产量(:|：)(.*)").matcher(value);
//					if (matcher.find()) {
						if(StringUtils.isEmpty(value, true)){
//							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，实产量为空！");
							formulaDesc.setActual_output(new BigDecimal("0"));
						} else {
							try{
								formulaDesc.setActual_output(new BigDecimal(value.trim()));
							} catch (NumberFormatException e){
								throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，实产量必须为数字！");
							}
						}
//					} else {
//						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，内容格式错误！");
//					}
				}
				break;
			case 4:  // 第四行
				if (columnIndex == 0) {//第1列  水质PH：
					Matcher matcher = Pattern.compile("水质pH(:|：)(.*)").matcher(value.trim());
					if (matcher.find()) {
						if(StringUtils.isEmpty(matcher.group(2), true)){
//							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，水质pH为空！");
							formulaDesc.setWater_ph(new BigDecimal("0"));
						} else {
							try{
								formulaDesc.setWater_ph(new BigDecimal(matcher.group(2).trim()));
							} catch (NumberFormatException e){
								throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，水质pH为必须是数字！");
							}
						}
					} else {
						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，内容格式错误！必须为“水质pH：”开头。");
					}
				}
				if (columnIndex == 4) {//第4列  水质电导率：
					Matcher matcher = Pattern.compile("水质电导率(:|：)(.*)").matcher(value.trim());
					if (matcher.find()) {
						if(StringUtils.isEmpty(matcher.group(2), true)){
//							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，水质电导率为空！");
							formulaDesc.setEle_conductivity(new BigDecimal("0"));
						} else {
							try{
								formulaDesc.setEle_conductivity(new BigDecimal(matcher.group(2).trim()));
							} catch (NumberFormatException e){
								throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，水质电导率必须是数字！");
							}
						}
					} else {
						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，内容格式错误！必须为“水质电导率：”开头。");
					}
				}
				if (columnIndex == 7) {//第7列  设备状态：
					Matcher matcher = Pattern.compile("设备状态(:|：)(.*)").matcher(value.trim());
					if (matcher.find()) {
						if(StringUtils.isEmpty(matcher.group(2), true)){
//							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，设备状态为空！");
							formulaDesc.setEquipment_state("");
						} else {
							formulaDesc.setEquipment_state(matcher.group(2).trim());
						}
					} else {
						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，内容格式错误！必须为“设备状态：”开头。");
					}
				}
				if (columnIndex == 9) {//生产日期：
					Matcher matcher = Pattern.compile("生产日期(:|：)(.*)").matcher(value.trim());
					if (matcher.find()) {
						if(StringUtils.isEmpty(matcher.group(2), true)){
//							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，生产日期为空！");
							formulaDesc.setProduct_date("0");
						} else {
							formulaDesc.setProduct_date(matcher.group(2).trim());
						}
					} else {
						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，内容格式错误！必须为“生产日期：”开头。");
					}
				}
				break;
			case 5:  //  第五行
				if(columnIndex == 1){ //第2列  乳化开始时间
					if(StringUtils.isEmpty(value, true)){
//						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，乳化开始时间为空！");
						formulaDesc.setEmulStartTime("");
					} else {
						formulaDesc.setEmulStartTime(value.trim());
					}
				}
				if(columnIndex == 10){ //第10列  乳化结束时间
					if(StringUtils.isEmpty(value, true)){
//						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，乳化结束时间为空！");
						formulaDesc.setEmulEndTime("");
					} else {
						formulaDesc.setEmulEndTime(value.trim());
					}
				}
				break;
		}

		// 处理原料列表
		if (isMaterialList && rowIndex > 6) {
//			if (columnIndex == 0
//					&& cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {// 判断是否循环完原料列表
//				isMaterialList = false;
//				return;
//			}
			switch (columnIndex) {
				case 0: // 组别
					fMaterial = new FMaterial();
					group = StringUtils.isEmpty(value, true) ? group : value.trim();
					if(StringUtils.isEmpty(group, true)){
						fmaterialExceptions.add((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，组别为空！");
					} else { 
						fMaterial.setGroup(group);
					}
					break;
				case 1: // 原料代码
					if(StringUtils.isEmpty(value, true)){
						fmaterialExceptions.add((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，原料代码为空！");
					} else {
						fMaterial.setCode(value.trim());
					}
					break;
				case 2: // 商品名
					if(StringUtils.isEmpty(value, true)){
						fmaterialExceptions.add((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，商品名为空！");
					} else {
						fMaterial.setName(value.trim());
					}
					break;
				case 3: // 中文INCI名
					if(!StringUtils.isEmpty(value, true))
						fMaterial.setInci_cn(value.trim());
					break;
				case 4: // 计划量
					if(StringUtils.isEmpty(value, true))
						fmaterialExceptions.add((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，计划量为空！");
					else {
						try{
							fMaterial.setPlan_amount(new BigDecimal(value.trim()));
						} catch(NumberFormatException e){
							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，计划量必须为纯数字！");
						}
					}
					break;
				case 5: // 实称量
					if(!StringUtils.isEmpty(value, true)){
//						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，实称量为空！");
						try{
							fMaterial.setActual_amount(new BigDecimal(value.trim()));
						} catch(NumberFormatException e){
							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，实称量必须为数字！");
						}
					} else {
						fMaterial.setActual_amount(new BigDecimal("0"));
					}
					break;
				case 6: // 料批号
					if(!StringUtils.isEmpty(value, true))
//						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，料批号为空！");
						fMaterial.setM_batch_num(value.trim());
					break;
				case 7: // 核称重量
					if(!StringUtils.isEmpty(value, true)){
//						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，核称重量为空！");
						try{
							fMaterial.setChecked_weight(new BigDecimal(value.trim()));
						} catch(NumberFormatException e){
							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，核称重量必须为数字！");
						}
					} else {
						fMaterial.setChecked_weight(new BigDecimal("0"));
					}
					if(fmaterialExceptions.size() == 0){
						list.add(fMaterial);
						fmaterialExceptions.clear();
					} else if(fmaterialExceptions.size() == 3){
						isMaterialList = false;
						return;
					} else if(fmaterialExceptions.size() == 4){
						isMaterialList = false;
						throw new CustomException("原料不能为空！");
					} else {
						throw new CustomException(fmaterialExceptions.get(0));
					}
					break;
				default:
					break;
			}
		}

		// 处理表尾
		if (columnIndex == 0 && value.contains("注意事项"))
			attentionRow = rowIndex + 1;
		if (attentionRow == rowIndex) {
			if (columnIndex == 0) {  
				if (value.contains("理化指标")) { //第1列   理化指标
					Matcher matcher = Pattern.compile("理化指标(:|：)(.*)").matcher(value);
					if (matcher.find()) {
						if(StringUtils.isEmpty(matcher.group(2), true)){
//							throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，理化指标为空！");
							formulaDesc.setPhysicochemical_target("0");
						} else {
							formulaDesc.setPhysicochemical_target(matcher.group(2));
						}
					} else {
						throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，内容格式错误！必须为“理化指标：”开头。");
					}
					finalRow = rowIndex + 1;
				} else {  //第1列   注意事项
					attentionRow += 1;
					if (formulaDesc.getAttention_item() == null) {
						formulaDesc.setAttention_item(value + "<>");
					} else {
						formulaDesc.setAttention_item(formulaDesc.getAttention_item() + value + "<>");
					} 
				}
			}
		}

		//最后一行
		if (finalRow == rowIndex) {
			Matcher matcher = Pattern.compile(
					"工程师(:|：)(.*)称料(:|：)(.*)核称(:|：)(.*)配料员(:|：)(.*)乳化主管(:|：)(.*)").matcher(value);
			if (matcher.find()) {
//				if(StringUtils.isEmpty(matcher.group(2), true))
//					throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，工程师为空！");
				formulaDesc.setEngineer(matcher.group(2).trim());
//				if(StringUtils.isEmpty(matcher.group(4), true))
//					throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，称料为空！");
				formulaDesc.setMaterial_weigher(matcher.group(4).trim());
//				if(StringUtils.isEmpty(matcher.group(6), true))
//					throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，称为空！");
				formulaDesc.setMaterial_checker(matcher.group(6).trim());
//				if(StringUtils.isEmpty(matcher.group(8), true))
//					throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，配料员为空！");
				formulaDesc.setMaterial_distributor(matcher.group(8).trim());
//				if(StringUtils.isEmpty(matcher.group(10), true))
//					throw new CustomException((cell.getRowIndex()+1)+"行   "+(cell.getColumnIndex()+1)+"列，乳化主管为空！");
				formulaDesc.setSupervisor(matcher.group(10).trim());
			}
			finalRow = finalRow + 1;
		}
	}

	public ArrayList<FMaterial> getList() {
		return list;
	}
	public void setMaterialList(boolean isMaterialList) {
		this.isMaterialList = isMaterialList;
	}

	public FormulaDesc getFormulaDesc() {
		return formulaDesc;
	}

	public void setFormulaDesc(FormulaDesc formulaDesc) {
		this.formulaDesc = formulaDesc;
	}
	
}
