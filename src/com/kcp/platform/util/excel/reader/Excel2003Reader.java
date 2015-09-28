package com.kcp.platform.util.excel.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;

import com.kcp.platform.util.excel.ExcelReader;
import com.kcp.platform.util.excel.factory.Excel2003Facotry;

public class Excel2003Reader extends ExcelReader {

    public Excel2003Reader() {
    	   super();
    }
    
    public Excel2003Reader(File file) {
        super(file);
    }
    
    public Excel2003Reader(InputStream is) {
        this.fis = is;
    }
    
    public Excel2003Reader(Workbook workbook) {
        this.wb = (HSSFWorkbook)workbook;
    }
    
	@Override
	public void open() throws IOException {
		if(wb != null){
			wb = (HSSFWorkbook)wb;
		}else{
			if(fis == null)
	            fis = new FileInputStream(file);
	        wb = new HSSFWorkbook(new POIFSFileSystem(fis));
	        fis.close();
		}
	}

	@Override
	public String readStringExcelCell(int sheetNum, int rowNum, int cellNum) {
		 if (sheetNum < 0 || rowNum < 0)
	            return "";      
		 DecimalFormat df = new DecimalFormat("#");
	        String strExcelCell = "";       
	        try {
	        	sheet = (HSSFSheet) wb.getSheetAt(sheetNum); 
	            row = (HSSFRow)sheet.getRow(rowNum); 
	            HSSFCell cell = (HSSFCell) row.getCell(cellNum);                 
	            if (cell != null) {
	                switch (cell.getCellType()) {
	                    case HSSFCell.CELL_TYPE_FORMULA://公式类型
	                    	String temp = String.valueOf(cell.getNumericCellValue());
							if(temp != null && "NAN".equals(temp)){
								strExcelCell = cell.getRichStringCellValue().toString();
							}else{
								strExcelCell = temp;
							}
	                        break;
	                    case HSSFCell.CELL_TYPE_NUMERIC: //数值型
	                    	if(HSSFDateUtil.isCellDateFormatted(cell)){
	                    		strExcelCell = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
	                    	}else{
	                    		strExcelCell = df.format(cell.getNumericCellValue());
	                    	}
	                        break;
	                    case HSSFCell.CELL_TYPE_STRING://字符串型
	                        strExcelCell = cell.getRichStringCellValue().getString();
	                        break;
	                    case HSSFCell.CELL_TYPE_BLANK://空值
	                        strExcelCell = "";
	                        break;
	                    case HSSFCell.CELL_TYPE_BOOLEAN://布尔类型
	                    	strExcelCell = Boolean.toString(cell.getBooleanCellValue());
	                    	break;
	                    case XSSFCell.CELL_TYPE_ERROR://故障
	                    	strExcelCell = "";
	                    	break;
	                    default:
	                        strExcelCell = "";
	                        break;
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace(); 
	        }        
	        return strExcelCell; 
	}
	
	 public static void main(String args[]) {         
	        File file = new File("E:\\批量比对碰撞 - 功能设计.xls");
	        ExcelReader readExcel = new Excel2003Facotry().createExcelReader(file);       
	        try {
	            readExcel.open();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }        
	        readExcel.setSheetNum(0); // 设置读取索引为0的工作表        
	        // 总行数
	        int count = readExcel.getRowCount();
	        for (int i = 1; i <= count; i++) {
	            String[] rows = readExcel.readExcelLine(i);
	            for (int j = 0; j < rows.length; j++) {
	                System.out.print(rows[j] + " ");
	            }           
	            System.out.print("\n");
	        }
	    }



}
