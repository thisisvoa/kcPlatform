package com.kcp.platform.util.excel.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.util.CollectionUtils;

import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.util.excel.ExcelWriter;

public class Excel2007Writer extends ExcelWriter {

	@Override
	public Cell createCell(Row row, int colIndex, String content) {
		XSSFCell result = (XSSFCell) row.createCell((short) colIndex);
		result.setCellValue(new XSSFRichTextString(content));
		return result;
	}

	/**
	 * 创建单元格的样式
	 * 
	 * @param workbook
	 *            工作表
	 * @param font
	 *            字代
	 * @param alignment
	 *            水平对齐 如：CellStyle.ALIGN_CENTER
	 * @param verticalAlignment
	 *            垂直对齐 如：CellStyle.VERTICAL_CENTER
	 * @param Border
	 *            边框 如：XSSFCellStyle.BORDER_THIN
	 * @param foregroundColor
	 *            前置背景色 如：CellStyle.BORDER_THIN
	 * @param fillPattern
	 *            填充模式 如：
	 * @return
	 */
	public CellStyle createCellStyle(Workbook workbook, Font font,
			short alignment, short verticalAlignment, short Border,
			short foregroundColor, short fillPattern) {
		XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setAlignment(alignment);
		cellStyle.setVerticalAlignment(verticalAlignment);
		cellStyle.setBorderRight(Border);
		cellStyle.setFillForegroundColor(foregroundColor);
		cellStyle.setFillPattern(fillPattern);
		return cellStyle;
	}
	
	@Override
	public Workbook writeWorkbook(Workbook workbook, String sheetTitle,
			List<List<String>> tableDataList,int headerNum) { 
        if(workbook == null)
            workbook = new XSSFWorkbook();       
        int exportRecordNum = tableDataList.size();   //导出的总记录数
        int tableStartRowNum = headerNum > 0 ? headerNum : 0; //表体数据开始行数
        int sheetNum = exportRecordNum/(CommonConst.EXCEL_MAX_EXPORT_NUM - headerNum) + 1;  //工作表的页数                        
        List<XSSFSheet> sheetList = new ArrayList<XSSFSheet>();        
        if(StringUtils.isEmpty(sheetTitle))
            sheetTitle = "sheet";        
        for(int i=0; i<sheetNum; i++){            
            // 在Excel工作簿中建一工作表
            String sTitle = sheetTitle + (i+1);
            XSSFSheet sheet = (XSSFSheet)workbook.createSheet(sTitle);                  
                      sheet.setSelected(true);  //设置工作薄为选中
                      sheet.setAutobreaks(true);
                      sheet.setPrintGridlines(true);                      
            sheetList.add(sheet);
        }
        /*************************输出表头**********************************/
        if(headerNum > 0){
            for(XSSFSheet sheet : sheetList){            
                XSSFRow headRow = sheet.createRow(0);
                        headRow.setHeightInPoints(20);
                for(int i = 0; i < headerNum; i++){
                	List<String> headerRowDataList = tableDataList.get(i);
                	for(int j = 0; j < headerRowDataList.size(); j++){
                		XSSFCellStyle cellStyle = (XSSFCellStyle)createDefHeaderCellStyle(workbook);//默认表头样式
                        createCell(headRow, j, headerRowDataList.get(j), cellStyle);
                	}
                }        
                //固定表头
                sheet.createFreezePane(0, 1);
            }  
        }     
        /***********************输出表体内容**************************/
        //设置列样式
        XSSFCellStyle columnStyle = (XSSFCellStyle)workbook.createCellStyle();
                      columnStyle.setFillBackgroundColor(HSSFColor.GREEN.index);
                      columnStyle.setWrapText(true);       
        if(tableDataList.size() > (tableStartRowNum + 1)){            
            for(int i = tableStartRowNum; i < exportRecordNum; i++){
                List<String> rowDataList = tableDataList.get(i);
                XSSFRow row = null;               
                int currentSheet = i/CommonConst.EXCEL_MAX_EXPORT_NUM;  //当前工作表的页数  
                XSSFSheet sheet = sheetList.get(currentSheet);
                int rowIndex = i-CommonConst.EXCEL_MAX_EXPORT_NUM*currentSheet;              
                row = sheet.createRow(rowIndex);                               
                for(int colIndex = 0, colLength = rowDataList.size(); colIndex < colLength; colIndex++){
                    createCell(row, colIndex, rowDataList.get(colIndex));
                }
            }
        }       
        //调整列的宽度(取第一列为基准)
        for(XSSFSheet sheet : sheetList){            
            for(int i = 0; i< tableDataList.get(0).size(); i++){
                sheet.autoSizeColumn((short)i);                
                sheet.setColumnWidth((short)i, (short)(sheet.getColumnWidth((short)i)+1000));
            }
        }       
        return workbook;    
	}
	
	/**
	 * 向workbook中写入多数据源的数据（多个sheet的数据来源不同）//====后期增加
	 * 
	 * @param workbook
	 * @param mutilSheetMap   key为sheet名称sheetTitle， value为表体数据tableDataList
	 * @return
	 */
	@Override
	public Workbook writeWorkbookMutilDatasoure(Workbook workbook,
			Map<String, List<List<String>>> mutilSheetMap,int headerNum) {

		if(workbook == null)
            workbook = new XSSFWorkbook(); 
		if(!CollectionUtils.isEmpty(mutilSheetMap)){
			for(String sheetTitle : mutilSheetMap.keySet()){
				List<List<String>> tableDataList = mutilSheetMap.get(sheetTitle);
				writeWorkbook(workbook, sheetTitle, tableDataList, 0);//默认无表头				
			}			
		}
		return workbook;
	
	}

}
