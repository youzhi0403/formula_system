package com.zakj.formula.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import com.zakj.formula.bean.FMaterial;
import com.zakj.formula.bean.FormulaDesc;
import com.zakj.formula.dao.formula.IFormulaDao;

public class ExcelExportHelper {

	public static String FILE_NAME = "";// 用于保存配方表名，方便导出excel文件时，设置成文件名

	private int currentRowIndex = 7; // 当前创建的行的下标
	private Row currentRow = null;
	private Cell currentCell = null;
	private int groupStartIndex = 7;// 保存每一个原料组的开始行下标
	private List<Cell> cells = new ArrayList<Cell>(); // 用来保存需要居中的cell
	HSSFWorkbook hssfWorkbook;

	/*
	 * public HSSFWorkbook createXlsExcel(int fid, IFormulaDao dao, String
	 * demoPath) throws FileNotFoundException, IOException {
	 * 
	 * FormulaDesc formulaDesc = null; try { C3P0Utils.beginTransation();
	 * formulaDesc = dao.findFormulaDescById(fid); } catch (SQLException e) {
	 * C3P0Utils.rollback(); e.printStackTrace(); } finally {
	 * C3P0Utils.commitAndRelease(); }
	 * 
	 * FileInputStream fis = new FileInputStream(new File(demoPath));
	 * HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fis); HSSFSheet hssfSheet =
	 * hssfWorkbook.getSheetAt(0);
	 * 
	 * // 获取需要插入表格的数据 List<FMaterial> fmlist = formulaDesc.getFmlist(); String[]
	 * procs = formulaDesc.getTechnology_proc().split("<>"); String[]
	 * attention_items = formulaDesc.getAttention_item().split("<>");
	 * 
	 * FILE_NAME = formulaDesc.getF_name()+".xls";//配方名称作为导出的文件名
	 * hssfWorkbook.setSheetName(0, formulaDesc.getF_name());
	 * 
	 * // 创建表头
	 * hssfSheet.getRow(1).getCell(0).setCellValue(formulaDesc.getF_name());//
	 * 配方名 cells.add(hssfSheet.getRow(1).getCell(0));
	 * hssfSheet.getRow(2).getCell(1).setCellValue(
	 * hssfSheet.getRow(2).getCell(1).getStringCellValue() +
	 * formulaDesc.getP_code());// 产品代码
	 * cells.add(hssfSheet.getRow(2).getCell(1));
	 * hssfSheet.getRow(2).getCell(9).setCellValue(
	 * hssfSheet.getRow(2).getCell(9).getStringCellValue() +
	 * formulaDesc.getF_number());// 配方编码
	 * hssfSheet.getRow(3).getCell(0).setCellValue(
	 * hssfSheet.getRow(3).getCell(0).getStringCellValue() +
	 * formulaDesc.getP_name());// 产品名称
	 * cells.add(hssfSheet.getRow(3).getCell(0)); hssfSheet .getRow(3)
	 * .getCell(6) .setCellValue(
	 * hssfSheet.getRow(3).getCell(6).getStringCellValue() +
	 * formulaDesc.getBatch_number());// 料批号 hssfSheet .getRow(3) .getCell(8)
	 * .setCellValue( hssfSheet.getRow(3).getCell(8).getStringCellValue() +
	 * formulaDesc.getPlain_amount());// 计划量 hssfSheet .getRow(3) .getCell(11)
	 * .setCellValue( hssfSheet.getRow(3).getCell(11).getStringCellValue() +
	 * formulaDesc.getActual_output());// 实产量 hssfSheet .getRow(4) .getCell(0)
	 * .setCellValue( hssfSheet.getRow(4).getCell(0).getStringCellValue() +
	 * formulaDesc.getWater_ph());// 水质pH hssfSheet .getRow(4) .getCell(4)
	 * .setCellValue( hssfSheet.getRow(4).getCell(4).getStringCellValue() +
	 * formulaDesc.getEle_conductivity());// 水质导电率 hssfSheet .getRow(4)
	 * .getCell(6) .setCellValue(
	 * hssfSheet.getRow(4).getCell(6).getStringCellValue() +
	 * formulaDesc.getEquipment_state());// 设备状态 hssfSheet .getRow(4)
	 * .getCell(10) .setCellValue(
	 * hssfSheet.getRow(4).getCell(10).getStringCellValue() +
	 * formulaDesc.getProduct_date());// 生产日期
	 * 
	 * // 判断左右边的长度 if (fmlist.size() >= procs.length) { // 将尾部移至最后
	 * hssfSheet.shiftRows(6, 8, fmlist.size()); // 在中间空白的部分，创建行 for (int i = 0;
	 * i < fmlist.size(); i++) { hssfSheet.createRow(i + 6); } } else { //
	 * 将尾部移至最后 hssfSheet.shiftRows(6, 8, procs.length); // 在中间空白的部分，创建行 for (int
	 * i = 0; i < procs.length; i++) { hssfSheet.createRow(i + 6); } }
	 * 
	 * // 先创建左边 for (FMaterial fMaterial : fmlist) { // 获取上一行的原料的组别，并与当前原料的组别对比
	 * if (currentRowIndex >= 7) { String lastGroup =
	 * hssfSheet.getRow(currentRowIndex - 1) .getCell(0).getStringCellValue();
	 * if (!fMaterial.getGroup().equals(lastGroup)) { // 合并组别相同的单元格
	 * hssfSheet.addMergedRegion(new CellRangeAddress( groupStartIndex,
	 * currentRowIndex - 1, 0, 0)); groupStartIndex = currentRowIndex; } }
	 * 
	 * currentRow = hssfSheet.getRow(currentRowIndex); for (int i = 0; i < 8;
	 * i++) { currentCell = currentRow.createCell(i); cells.add(currentCell);
	 * switch (i) { case 0: currentCell.setCellValue(fMaterial.getGroup());//配方组
	 * break; case 1: currentCell.setCellValue(fMaterial.getM_name());//原料名
	 * break; case 2: currentCell.setCellValue(fMaterial.getCn_name());//中文名
	 * break; case 3: currentCell.setCellValue(fMaterial.getPlan_amount());//计划量
	 * break; case 4:
	 * currentCell.setCellValue(fMaterial.getActual_amount());//实际量 break; case
	 * 5: currentCell.setCellValue(fMaterial.getM_batch_num());//料批量 break; case
	 * 6: currentCell.setCellValue(fMaterial.getChecked_weight());//乳化核称重量
	 * break; case 7: // TODO 计算成本 break;
	 * 
	 * default: break; } }
	 * 
	 * // 如果已经是最后一个原料，则直接合并最后一个组别的单元格，并合并最后一行，设置公式 if (fmlist.get(fmlist.size()
	 * - 1) == fMaterial) { hssfSheet.addMergedRegion(new
	 * CellRangeAddress(groupStartIndex, currentRowIndex, 0, 0));
	 * hssfSheet.addMergedRegion(new CellRangeAddress( ++currentRowIndex,
	 * currentRowIndex, 0, 7)); HSSFCell cell =
	 * hssfSheet.getRow(currentRowIndex).createCell(0); HSSFCellStyle cellStyle
	 * = hssfWorkbook.createCellStyle();
	 * cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	 * cell.setCellStyle(cellStyle);
	 * cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	 * cell.setCellFormula("SUM(D7:D" + (currentRowIndex) + ")"); break; }
	 * currentRowIndex++; }
	 * 
	 * // 再创建右边 currentRowIndex = 6; // 回到下标为6的行，方便创建右边的表格 int[] indexs = new
	 * int[procs.length * 2];// 用于保存需要合并的行 int i = 0; for (String proc_item :
	 * procs) { // 把内容写进入右边的表格 hssfSheet.getRow(currentRowIndex).createCell(8)
	 * .setCellValue(proc_item); if (!StringUtils.isEmpty(proc_item, true)) { if
	 * (i % 2 == 0) { // 如果是合并的起始行，则关闭上一个起始行，并可是一个新的起始行 if (currentRowIndex ==
	 * 6) { // 第一行不能为空 indexs[i] = currentRowIndex; } else { indexs[++i] =
	 * currentRowIndex - 1; indexs[++i] = currentRowIndex; } } } // 如果是最后一个 if
	 * (procs[procs.length - 1].equals(proc_item)) { int index = indexs[i];
	 * indexs[++i] = index; } currentRowIndex++; }
	 * 
	 * // 开始合并 for (int j = 0; j < indexs.length; j++) { if (j % 2 == 0) {
	 * hssfSheet.addMergedRegion(new CellRangeAddress(indexs[j], indexs[j + 1],
	 * 8, 12)); } }
	 * 
	 * // 最后创建尾部 currentRowIndex = hssfSheet.getLastRowNum() - 1; //
	 * 移到需要创建“注意事项”的位置 hssfSheet.shiftRows(hssfSheet.getLastRowNum() - 1,
	 * hssfSheet.getLastRowNum(), attention_items.length); for (String
	 * attention_item : attention_items) { // 先合并0-6列 - 注意事项 currentCell =
	 * hssfSheet.createRow(currentRowIndex).createCell(0);
	 * hssfSheet.addMergedRegion(new CellRangeAddress(currentRowIndex,
	 * currentRowIndex, 0, 6)); currentCell.setCellValue(attention_item);
	 * currentRowIndex++; } // 回到“乳化异常记录”的开始行 currentRowIndex =
	 * hssfSheet.getLastRowNum() - 1 - attention_items.length; // 合并“乳化异常记录”的单元格
	 * hssfSheet.addMergedRegion(new CellRangeAddress(currentRowIndex,
	 * currentRowIndex + attention_items.length, 8, 12));
	 * hssfSheet.getRow(currentRowIndex).createCell(8)
	 * .setCellValue(formulaDesc.getException_record());
	 * cells.add(hssfSheet.getRow(currentRowIndex).getCell(8)); // 理化指标
	 * currentRowIndex = attention_items.length + currentRowIndex;
	 * hssfSheet.getRow(currentRowIndex).getCell(0).setCellValue(
	 * hssfSheet.getRow(currentRowIndex).getCell(0) .getStringCellValue() +
	 * formulaDesc.getPhysicochemical_target()); currentRowIndex++; // 最后一行
	 * hssfSheet.getRow(currentRowIndex).getCell(0).setCellValue( "工程师:" +
	 * formulaDesc.getEngineer() + "   称料:" + formulaDesc.getMaterial_weigher()
	 * + "   核称:" + formulaDesc.getMaterial_checker() + "   配料员:" +
	 * formulaDesc.getMaterial_distributor() + "   乳化主管:" +
	 * formulaDesc.getSupervisor());
	 * cells.add(hssfSheet.getRow(currentRowIndex).getCell(0));
	 * 
	 * // 最后，设置字体大小，居中等属性 HSSFCellStyle cellStyle =
	 * hssfWorkbook.createCellStyle(); cellStyle.setWrapText(true); // 自动换行
	 * HSSFFont font = hssfWorkbook.createFont(); font.setFontName("宋体"); //
	 * 设置字体为 - “宋体” font.setFontHeightInPoints((short) 10);// 设置字体大小
	 * cellStyle.setFont(font); for (Row row : hssfSheet) { for (Cell cell :
	 * row) { // 设置表格一些属性（字体，位置等） cell.setCellStyle(cellStyle); } }
	 * 
	 * //设置需要居中的单元格 for (Cell cell : cells) { HSSFCellStyle cellStyle1 =
	 * hssfWorkbook.createCellStyle(); cellStyle1.setWrapText(true); // 自动换行
	 * cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直 居中
	 * cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	 * font.setFontName("宋体"); // 设置字体为 - “宋体”
	 * font.setFontHeightInPoints((short) 10);// 设置字体大小
	 * cellStyle1.setFont(font); cell.setCellStyle(cellStyle1); }
	 * 
	 * fis.close();//释放模板资源 return hssfWorkbook; }
	 */

	public HSSFWorkbook createXlsExcel(int fid, IFormulaDao dao, String demoPath)
			throws FileNotFoundException, IOException {

		FormulaDesc formulaDesc = null;
		try {
			C3P0Utils.beginTransation();
			formulaDesc = dao.findFormulaDescById(fid);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
		} finally {
			C3P0Utils.commitAndRelease();
		}

		FileInputStream fis = new FileInputStream(new File(demoPath));
		hssfWorkbook = new HSSFWorkbook(fis);
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

		// 获取需要插入表格的数据
		List<FMaterial> fmlist = formulaDesc.getFmlist();
		String[] procs = formulaDesc.getTechnology_proc().split("<>");
		String[] attention_items = formulaDesc.getAttention_item().split("<>");

		FILE_NAME = formulaDesc.getF_name() + ".xls";// 配方名称作为导出的文件名
		hssfWorkbook.setSheetName(0, formulaDesc.getF_name());

		// 创建表头
		hssfSheet.getRow(1).getCell(0).setCellValue(hssfSheet.getRow(1).getCell(0).getStringCellValue()+formulaDesc.getF_name());// 配方名
		cells.add(hssfSheet.getRow(1).getCell(0));
		hssfSheet.getRow(2).getCell(0).setCellValue(hssfSheet.getRow(2).getCell(0).getStringCellValue()+formulaDesc.getP_code());// 产品代码
		hssfSheet.getRow(2).getCell(9).setCellValue(hssfSheet.getRow(2).getCell(9).getStringCellValue()+formulaDesc.getF_number());// 配方编码
		hssfSheet.getRow(3).getCell(0).setCellValue(hssfSheet.getRow(3).getCell(0).getStringCellValue()+formulaDesc.getP_name());// 产品名称
		hssfSheet.getRow(3).getCell(6).setCellValue(hssfSheet.getRow(3).getCell(6).getStringCellValue()+formulaDesc.getBatch_number());// 批号
		if (formulaDesc.getPlain_amount().floatValue() != 0){
			hssfSheet.getRow(3).getCell(10).setCellValue(formulaDesc.getPlain_amount().toString());// 计划量
		}
		if (formulaDesc.getActual_output().floatValue() != 0){
			hssfSheet.getRow(3).getCell(13).setCellValue(formulaDesc.getActual_output().toString());//实产量
		}
		if (formulaDesc.getWater_ph().floatValue() != 0){
			hssfSheet.getRow(4).getCell(1).setCellValue(formulaDesc.getWater_ph().toString());
		}
		if (formulaDesc.getEle_conductivity().floatValue() != 0){
			hssfSheet.getRow(4).getCell(4).setCellValue(formulaDesc.getEle_conductivity().toString());
		}
		hssfSheet.getRow(4).getCell(7).setCellValue(hssfSheet.getRow(4).getCell(7).getStringCellValue()+formulaDesc.getEquipment_state());// 设备状态
		hssfSheet.getRow(4).getCell(9).setCellValue(hssfSheet.getRow(4).getCell(9).getStringCellValue()+formulaDesc.getProduct_date());// 生产日期
		hssfSheet.getRow(5).getCell(1).setCellValue(hssfSheet.getRow(5).getCell(1).getStringCellValue()+formulaDesc.getEmulStartTime());// 乳化开始时间
		hssfSheet.getRow(5).getCell(10).setCellValue(hssfSheet.getRow(5).getCell(10).getStringCellValue()+formulaDesc.getEmulEndTime());// 乳化结束时间

		// 判断左右边的长度
		if (fmlist.size() >= procs.length) {
			// 将尾部移至最后
			hssfSheet.shiftRows(7, 9, fmlist.size() + 1);
			// 在中间空白的部分，创建行
			for (int i = 0; i < fmlist.size() + 1; i++) {
				hssfSheet.createRow(i + 7);
			}
		} else {
			// 将尾部移至最后
			hssfSheet.shiftRows(7, 9, procs.length + 1);
			// 在中间空白的部分，创建行
			for (int i = 0; i < procs.length + 1; i++) {
				hssfSheet.createRow(i + 7);
			}
		}

		//创建当前单元格样式，用于给需要显示数字格式的单元格设置为数字格式
		HSSFCellStyle currentCellStyle = hssfWorkbook.createCellStyle();
		// 先创建左边
		for (FMaterial fMaterial : fmlist) {
			// 获取上一行的原料的组别，并与当前原料的组别对比
			if (currentRowIndex >= 8) {
				String lastGroup = hssfSheet.getRow(currentRowIndex - 1)
						.getCell(0).getStringCellValue();
				if (!fMaterial.getGroup().equals(lastGroup)) {
					// 合并组别相同的单元格
					hssfSheet.addMergedRegion(new CellRangeAddress(
							groupStartIndex, currentRowIndex - 1, 0, 0));
					groupStartIndex = currentRowIndex;
				}
			}

			currentRow = hssfSheet.getRow(currentRowIndex);
			for (int i = 0; i < 8; i++) {
				currentCell = currentRow.createCell(i);
				cells.add(currentCell);
				switch (i) {
				case 0:
					currentCell.setCellValue(fMaterial.getGroup());// 配方组
					break;
				case 1:
					currentCell.setCellValue(fMaterial.getCode());// 原料代码
					break;
				case 2:
					currentCell.setCellValue(fMaterial.getName());// 商品名
					break;
				case 3:
					currentCell.setCellValue(fMaterial.getInci_cn());// 中文INCI名
					break;
				case 4:
//					currentCell.setCellValue(fMaterial.getPlan_amount().toPlainString());// 计划量
					setCellContent(fMaterial.getPlan_amount().toPlainString(), currentCell);
					break;
				case 5:
					if (fMaterial.getActual_amount().floatValue() != 0){
						currentCell.setCellValue(fMaterial.getActual_amount().toString());//实称量
					}
					break;
				case 6:
					currentCell.setCellValue(fMaterial.getM_batch_num());// 料批量
					break;
				case 7:
					if (fMaterial.getChecked_weight().floatValue() != 0){
						currentCell.setCellValue(fMaterial.getChecked_weight().toString()); //乳化核称重量
					}
					break;
				case 8:
					currentCell.setCellValue((fMaterial.getPrice().multiply(fMaterial.getPlan_amount())).toString());
					break;

				default:
					break;
				}
			}

			// 如果已经是最后一个原料，则直接合并最后一个组别的单元格
			if (fmlist.get(fmlist.size() - 1) == fMaterial) {
				hssfSheet.addMergedRegion(new CellRangeAddress(groupStartIndex,
						currentRowIndex, 0, 0));
				break;
			}
			currentRowIndex++;
		}

		// 再创建右边
		currentRowIndex = 7; // 回到下标为7的行，方便创建右边的表格
		int[] indexs = new int[procs.length * 2];// 用于保存需要合并的行
		int i = 0;//计算器
		for (String proc_item : procs) {
			// 把内容写进入右边的表格
			hssfSheet.getRow(currentRowIndex).createCell(9).setCellValue(proc_item);
			if (!StringUtils.isEmpty(proc_item, true)) {
				if (i % 2 == 0) { // 如果是合并的起始行，则关闭上一个起始行，并开始一个新的起始行
					if (currentRowIndex == 7) { // 第一行不能为空
						indexs[i] = currentRowIndex;
					} else {
						indexs[++i] = currentRowIndex - 1;
						indexs[++i] = currentRowIndex;
					}
				}
			}
			// 如果是最后一个
			if (!StringUtils.isEmpty(proc_item, true)
					&& procs[procs.length - 1].equals(proc_item)) {
				int index = indexs[i];
				indexs[++i] = index;
			}
			currentRowIndex++;
		}

		// 开始合并
		for (int j = 0; j < indexs.length; j++) {
			if (j % 2 == 0) {
				if(indexs[j+1] == 0){
					hssfSheet.addMergedRegion(new CellRangeAddress(indexs[j],
							indexs[j], 9, 14));
					break;
				}
				hssfSheet.addMergedRegion(new CellRangeAddress(indexs[j],
						indexs[j + 1], 9, 14));
			}
		}

		// 设置公式，计算原料计划量总量
		currentRowIndex = hssfSheet.getLastRowNum() - 3;
		hssfSheet.addMergedRegion(new CellRangeAddress(currentRowIndex,
				currentRowIndex, 0, 8));
		HSSFCell cell1 = hssfSheet.createRow(currentRowIndex).createCell(0);
		HSSFCellStyle cellStyle1 = hssfWorkbook.createCellStyle();
		cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell1.setCellStyle(cellStyle1);
		cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell1.setCellFormula("SUM(E8:E" + (currentRowIndex) + ")");
		cells.add(cell1);

		// 最后创建尾部
		// 根据“注意事项”的list长度，将最后两行向下移动相应的距离
		currentRowIndex = hssfSheet.getLastRowNum() - 1; // 移到需要创建“注意事项”的位置
		hssfSheet.shiftRows(hssfSheet.getLastRowNum() - 1,
				hssfSheet.getLastRowNum(), attention_items.length);
		// 因为最后两行向下移动后，空出来的行不是对象，需要顺便也创建新行
		for (int j = 0; j < attention_items.length; j++) {
			hssfSheet.createRow(j + hssfSheet.getLastRowNum() - 1
					- attention_items.length);
		}

		for (String attention_item : attention_items) {
			// 先合并0-6列 - 注意事项
			currentCell = hssfSheet.getRow(currentRowIndex).createCell(0);
			hssfSheet.addMergedRegion(new CellRangeAddress(currentRowIndex,
					currentRowIndex, 0, 7));
			currentCell.setCellValue(attention_item);
			currentRowIndex++;
		}

		// 回到“乳化异常记录”的开始行
		currentRowIndex = hssfSheet.getLastRowNum() - 1
				- attention_items.length;
		// 合并“乳化异常记录”的单元格
		hssfSheet.getRow(currentRowIndex).createCell(9)
				.setCellValue(formulaDesc.getException_record());
		hssfSheet.addMergedRegion(new CellRangeAddress(currentRowIndex,
				currentRowIndex + attention_items.length - 1, 9, 13));
		cells.add(hssfSheet.getRow(currentRowIndex).getCell(9));

		// 理化指标
		currentRowIndex = attention_items.length + currentRowIndex;
		hssfSheet
				.getRow(currentRowIndex)
				.getCell(0)
				.setCellValue(
						hssfSheet.getRow(currentRowIndex).getCell(0)
								.getStringCellValue()
								+ formulaDesc.getPhysicochemical_target());

		currentRowIndex++;
		// 最后一行
//		hssfSheet.getRow(currentRowIndex).getCell(2).setCellValue(formulaDesc.getEngineer());
//		hssfSheet.getRow(currentRowIndex).getCell(4).setCellValue(formulaDesc.getMaterial_weigher());
//		hssfSheet.getRow(currentRowIndex).getCell(6).setCellValue(formulaDesc.getMaterial_checker());
//		hssfSheet.getRow(currentRowIndex).getCell(8).setCellValue(formulaDesc.getMaterial_distributor());
//		hssfSheet.getRow(currentRowIndex).getCell(10).setCellValue(formulaDesc.getMaterial_distributor());
		hssfSheet.getRow(currentRowIndex).getCell(0).setCellValue( "工程师:" +
				 formulaDesc.getEngineer() + "   称料:" + formulaDesc.getMaterial_weigher()
				 + "   核称:" + formulaDesc.getMaterial_checker() + "   配料员:" +
				 formulaDesc.getMaterial_distributor() + "   乳化主管:" +
				 formulaDesc.getSupervisor());
		cells.add(hssfSheet.getRow(currentRowIndex).getCell(0));

		// 最后，设置字体大小
		HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
		cellStyle.setWrapText(true); // 自动换行
		HSSFFont font = hssfWorkbook.createFont();
		font.setFontName("宋体"); // 设置字体为 - “宋体”
		font.setFontHeightInPoints((short) 10);// 设置字体大小
		cellStyle.setFont(font);
		for (Row row : hssfSheet) {
			for (Cell cell : row) {
				// 设置表格一些属性（字体，位置等）
				cell.setCellStyle(cellStyle);
			}
		}

		// 设置需要居中的单元格
		cellStyle = hssfWorkbook.createCellStyle();
		cellStyle.setWrapText(true); // 自动换行
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直 居中
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		font.setFontName("宋体"); // 设置字体为 - “宋体”
		font.setFontHeightInPoints((short) 10);// 设置字体大小
		cellStyle.setFont(font);
		for (Cell cell : cells) {
			cell.setCellStyle(cellStyle);
		}

		fis.close();// 释放模板资源
		return hssfWorkbook;
	}
	
	private void setCellContent(Object data, Cell contentCell){
		 HSSFCellStyle contextstyle =hssfWorkbook.createCellStyle();
		 Boolean isNum = false;//data是否为数值型
         Boolean isInteger=false;//data是否为整数
         Boolean isPercent=false;//data是否为百分数
         if (data != null || "".equals(data)) {
             //判断data是否为数值型
             isNum = data.toString().matches("^(-?\\d+)(\\.\\d+)?$");
             //判断data是否为整数（小数部分是否为0）
             isInteger=data.toString().matches("^[-\\+]?[\\d]*$");
             //判断data是否为百分数（是否包含“%”）
             isPercent=data.toString().contains("%");
         }

         //如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
         if (isNum && !isPercent) {
             HSSFDataFormat df = hssfWorkbook.createDataFormat(); // 此处设置数据格式
             if (isInteger) {
                 contextstyle.setDataFormat(df.getBuiltinFormat("#,#0"));//数据格式只显示整数
             }else{
                 contextstyle.setDataFormat(df.getBuiltinFormat("#,##0.00"));//保留两位小数点
             }                   
             // 设置单元格格式
             contentCell.setCellStyle(contextstyle);
             // 设置单元格内容为double类型
             contentCell.setCellValue(Double.parseDouble(data.toString()));
         } else {
             contentCell.setCellStyle(contextstyle);
             // 设置单元格内容为字符型
             contentCell.setCellValue(data.toString());
         }
	}

}
