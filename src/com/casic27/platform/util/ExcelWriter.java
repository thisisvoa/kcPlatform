/**
 * @(#)com.casic27.platform.util.ExcelReader.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Author： 宗斌(zongbin@casic27.com)
 *<br> Date：Jun 11, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */

package com.casic27.platform.util;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.casic27.platform.sys.constants.CommonConst;

/**
 * Excel导出
 */
public class ExcelWriter {
    
    /**
     * 导出excel对外接口
     * 
     * @param exportFileName   导出后的文件名 如：hr.xls
     * @param title  标题  如：2005年人力资源统计报表
     * @param tableDataList 数据表数据  如：new String[][]{{"表头1","表头2"},{"aaa","bbb"}}
     * @param response
     * @throws Exception 
     */
    public static void exportExcelData(String exportFileName,
                                       String title, 
                                       List<List<String>> tableDataList,
                                       HttpServletResponse response) 
    throws Exception {
        
        HSSFWorkbook workbook = writeWorkbook(null, title, tableDataList); 
        
        exportExcelData(workbook, exportFileName, response);        
    }
    
    /**
     * 向workbook中写入数据
     * 
     * @param workbook
     * @param sheetTitle
     * @param tableDataList 其中第一行为标题行，其余的行为数据行
     * @return
     */
    public static HSSFWorkbook writeWorkbook(HSSFWorkbook workbook, String sheetTitle, List<List<String>> tableDataList){
        
        if(workbook == null)
            workbook = new HSSFWorkbook();
        
        int exportRecordNum = tableDataList.size();   //导出的记录数
        int sheetNum = exportRecordNum/CommonConst.EXCEL_MAX_EXPORT_NUM + 1;  //工作表的页数           
        List<String> headerRowDataList = tableDataList.get(0);        
        List<HSSFSheet> sheetList = new ArrayList<HSSFSheet>();        
        if(StringUtils.isEmpty(sheetTitle))
            sheetTitle = "sheet";        
        for(int i=0; i<sheetNum; i++){            
            // 在Excel工作簿中建一工作表
            String sTitle = sheetTitle + (i+1);
            HSSFSheet sheet = workbook.createSheet(sTitle);                  
                      sheet.setSelected(true);  //设置工作薄为选中
                      sheet.setAutobreaks(true);
                      sheet.setPrintGridlines(true);                      
            sheetList.add(sheet);
        }       
        /*************************输出表头**********************************/
        for(HSSFSheet sheet : sheetList){
            
            HSSFRow headRow = sheet.createRow(0);
                    headRow.setHeightInPoints(20);
                    
            for(int i=0; i<headerRowDataList.size(); i++){
                ExcelWriter.createCell(headRow, i, headerRowDataList.get(i), createHeaderCellStyle(workbook));
            }
            
            //固定表头
            sheet.createFreezePane(0, 1);
        }
        
        /***********************输出表体内容**************************/

        //设置列样式
        HSSFCellStyle columnStyle = workbook.createCellStyle();
                      columnStyle.setFillBackgroundColor(HSSFColor.GREEN.index);
                      columnStyle.setWrapText(true);
        
        if(tableDataList.size() > 1){            
            for(int i=1; i<exportRecordNum; i++){
                List<String> rowDataList = tableDataList.get(i);
                HSSFRow row = null;
                
                int currentSheet = i/CommonConst.EXCEL_MAX_EXPORT_NUM;  //当前工作表的页数  
                HSSFSheet sheet = sheetList.get(currentSheet);
                int rowIndex = i-CommonConst.EXCEL_MAX_EXPORT_NUM*currentSheet;
                
                row = sheet.createRow(rowIndex);
                                
                for(int colIndex=0, colLength=rowDataList.size(); colIndex<colLength; colIndex++){
                    ExcelWriter.createCell(row, colIndex, rowDataList.get(colIndex));
                }
            }
        }       
        //调整列的宽度
        for(HSSFSheet sheet : sheetList){            
            for(int i=0; i<headerRowDataList.size(); i++){
                sheet.autoSizeColumn((short)i);                
                sheet.setColumnWidth((short)i, (short)(sheet.getColumnWidth((short)i)+1000));
            }
        }       
        return workbook;
    }
    
    /**
     * 根据创建好的workbook对象，向响应端输出EXCEL
     * 
     * @param workbook
     * @param exportFileName
     * @param response
     * @throws Exception
     */
    public static void exportExcelData(HSSFWorkbook workbook, 
                                       String exportFileName, 
                                       HttpServletResponse response) 
    throws Exception {
        
        response.reset();
        
        StringBuffer headerSB = new StringBuffer();
                     headerSB.append("attachment; filename=");
                     headerSB.append(exportFileName);
                
        response.setHeader("Content-Disposition", new String(headerSB.toString().getBytes("GBK"), "ISO-8859-1"));
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        
        //获得输出流
        OutputStream out = null;
        //输出
        try {
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (out != null) {
                out.close();
            }
        }
    }
    
    /**
     * 创建一个单元格
     * 
     * @param row
     * @param colIndex
     * @param content
     * @return
     */
    public static HSSFCell createCell(HSSFRow row, int colIndex, String content){

        HSSFCell result = row.createCell((short)colIndex);
                 result.setCellValue(new HSSFRichTextString(content));
                 
        return result;
    }
    
    /**
     * 创建一个单元格
     * 
     * @param row
     * @param colIndex
     * @param content
     * @param titleStyle
     * @return
     */
    public static HSSFCell createCell(HSSFRow row, int colIndex, String content, HSSFCellStyle titleStyle) {
        
        HSSFCell result = createCell(row, colIndex, content);
        
        if (titleStyle != null)
            result.setCellStyle(titleStyle);
        
        return result;
    }
    
    /**
     * 创建表头标题单元格的样式
     * 
     * @param workbook
     * @return
     */
    public static HSSFCellStyle createHeaderCellStyle(HSSFWorkbook workbook) {
        //设置表头标题样式
        HSSFFont headerFont = workbook.createFont();
                 headerFont.setFontHeightInPoints((short) 14);
                 headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle result = workbook.createCellStyle();
                      result.setFont(headerFont);
                      result.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                      result.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                      result.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
                      result.setBorderRight(HSSFCellStyle.BORDER_THIN);
                      result.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);                     
        return result;
    }
}
