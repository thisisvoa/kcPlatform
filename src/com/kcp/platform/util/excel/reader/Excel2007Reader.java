package com.kcp.platform.util.excel.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.kcp.platform.util.excel.ExcelReader;
import com.kcp.platform.util.excel.factory.Excel2003Facotry;
import com.kcp.platform.util.excel.factory.Excel2007Factory;

public class Excel2007Reader extends ExcelReader {

	public Excel2007Reader(){
		super();
	}
	
	public Excel2007Reader(File file){
		super(file);
	}
	
    public Excel2007Reader(InputStream is) {
        this.fis = is;
    }
    
    public Excel2007Reader(Workbook workbook) {
        this.wb = (XSSFWorkbook)workbook;
    }
	@Override
	public void open() throws IOException {
		if(wb != null){
			wb = (XSSFWorkbook)wb;
		}else{
			if(fis == null)
	            fis = new FileInputStream(file);
	        try {
//	        	可以直接传流参数，但是推荐使用OPCPackage容器打开 
				wb = new XSSFWorkbook(OPCPackage.open(fis));
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			}
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
	        	sheet = (XSSFSheet) wb.getSheetAt(sheetNum); 
	            row = (XSSFRow)sheet.getRow(rowNum); 
	            XSSFCell cell = (XSSFCell) row.getCell(cellNum);                 
	            if (cell != null) {
	                switch (cell.getCellType()) {
                    case XSSFCell.CELL_TYPE_FORMULA://公式类型
                    	String temp = String.valueOf(cell.getNumericCellValue());
						if(temp != null && "NAN".equals(temp)){
							strExcelCell = cell.getRichStringCellValue().toString();
						}else{
							strExcelCell = temp;
						}
                        break;
                    case XSSFCell.CELL_TYPE_NUMERIC: //数值型
                    	if(HSSFDateUtil.isCellDateFormatted(cell)){
                    		strExcelCell = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
                    	}else{
                    		strExcelCell = df.format(cell.getNumericCellValue());
                    	}
                        break;
                    case XSSFCell.CELL_TYPE_STRING://字符串型
                        strExcelCell = cell.getRichStringCellValue().getString();
                        break;
                    case XSSFCell.CELL_TYPE_BLANK://空值
                        strExcelCell = "";
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN://布尔类型
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
	        File file = new File("E:\\批量比对碰撞 - 功能设计.xlsx");
	        ExcelReader readExcel = new Excel2007Factory().createExcelReader(file);
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
