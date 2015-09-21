package com.casic27.platform.util.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class ExcelReader {
	protected Workbook wb = null;// book [includes sheet]
    
	protected Sheet sheet = null;//工作表
    
	protected Row row = null;//行对象
    
	protected int sheetNum = 0;//第sheetnum个工作表
    
	protected int rowNum = 0;//第rowNum个工作表
    
	protected InputStream fis = null;
    
	public Sheet getSheet() {
		return sheet;
	}

	protected File file = null;
    
    public ExcelReader() {
   
    }
    
    public Workbook getWb() {
		return wb;
	}

	public InputStream getFis() {
		return fis;
	}

	public File getFile() {
		return file;
	}

	public ExcelReader(File file) {
        this.file = file;
    }
    
    public ExcelReader(InputStream is) {
        this.fis = is;
    }
    public ExcelReader(Workbook workbook) {
        this.wb = workbook;
    }
    
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
    
    public void setSheetNum(int sheetNum) {
        this.sheetNum = sheetNum;
    }
    
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 读取excel文件获得HSSFWorkbook对象
     * 
     * @throws IOException
     */
    public abstract void open() throws IOException;
    
    /**
     * 指定工作表、行、列下的内容
     * 
     * @param sheetNum
     * @param rowNum
     * @param cellNum
     * @return String
     */ 
    public abstract String readStringExcelCell(int sheetNum, int rowNum, int cellNum);
    
    /**
     * 指定所有工作表中的内容内容(获取前limitCout行数据，limitCout <= 0时，获取全部数据)
     * 
     * @param limitCout 
     * @return String
     */ 
	public List<String[]> readExcelDataList(int limitRowCount) {
		List<String[]> dataList = new ArrayList<String[]>();
		int sheetCount =  getSheetCount() ;		
		for(int i = 0; i < sheetCount; i++){
			sheetNum = i;
			int rowCount = limitRowCount > 0 && limitRowCount < (getRowCount(sheetNum)+1) ? limitRowCount :(getRowCount(sheetNum)+1);
			for(int j = 0; j < rowCount; j++){
				String[] rowData = readExcelLine(j);
				if(rowData != null)
					dataList.add(rowData);
			}
		}
		return dataList;
	} 
	
	
    /**
     * 返回sheet表数目
     * 
     * @return
     */
    public int getSheetCount() { 
        int sheetCount = -1;
        sheetCount = wb.getNumberOfSheets();
        return sheetCount;
    }

    /**
     * sheetNum下的记录行数
     * 
     * @return
     */ 
    public int getRowCount() { 
        if (wb == null)
            System.out.println("=============>WorkBook为空");       
        Sheet sheet = wb.getSheetAt(this.sheetNum);
        int rowCount = -1;
        rowCount = sheet.getLastRowNum();
        return rowCount;
    }
    
    /**
     * 读取指定sheetNum的rowCount
     * 
     * @param sheetNum
     * @return
     */
    public int getRowCount(int sheetNum) {
        Sheet sheet = wb.getSheetAt(sheetNum);
        int rowCount = -1;
        rowCount = sheet.getLastRowNum()+1;
        return rowCount; 
    }
    
    /**
     * 得到指定行的内容
     * 
     * @param lineNum
     * @return
     */
    public String[] readExcelLine(int lineNum) { 
        return readExcelLine(this.sheetNum, lineNum);
    }

    /**
     * 指定工作表和行数的内容
     * 
     * @param sheetNum
     * @param lineNum
     * @return String[]
     */ 
    public String[] readExcelLine(int sheetNum, int lineNum) { 
        if (sheetNum < 0 || lineNum < 0)
            return null;       
        String[] strExcelLine = null;       
        try {
            sheet = wb.getSheetAt(sheetNum);
            row = sheet.getRow(lineNum);            
            if(row == null)
                return null;            
            int cellCount = row.getLastCellNum();
            strExcelLine = new String[cellCount + 1];
            for (int i = 0; i <= cellCount; i++) {
                strExcelLine[i] = readStringExcelCell(lineNum, i);
            }
        }catch (Exception e) {
            e.printStackTrace(); 
        }        
        return strExcelLine;
    }

    /**
     * 读取指定列的内容
     * 
     * @param cellNum
     * @return
     */
    public String readStringExcelCell(int cellNum) { 
        return readStringExcelCell(this.rowNum, cellNum);
    }

    /**
     * 指定行和列编号的内容
     * 
     * @param rowNum
     * @param cellNum
     * @return
     */
    public String readStringExcelCell(int rowNum, int cellNum) { 
        return readStringExcelCell(this.sheetNum, rowNum, cellNum);
    }

}
