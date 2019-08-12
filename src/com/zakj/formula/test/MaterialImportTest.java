package com.zakj.formula.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.utils.MaterialExcelParseHelper;
import com.zakj.formula.utils.POIUtils;

public class MaterialImportTest {
	
	@Test
	public void test() throws Exception{
		File file = new File("C:\\Users\\Administrator\\Desktop\\原料导入模板.xls");
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
		
		MaterialExcelParseHelper helper = new MaterialExcelParseHelper();
		for (Row row : hssfSheet) {
			System.out.println();
			for (Cell cell : row) {
				System.out.println(POIUtils.getHSSFCellStringValue((HSSFCell) cell));
//				helper.parseXls(cell.getRowIndex(), 
//						cell.getColumnIndex(), POIUtils.getHSSFCellStringValue((HSSFCell) cell));
			}
		}
		
		for ( MaterialBean material : helper.getmList()){
			System.out.println(material);
		}
		
	}

}
