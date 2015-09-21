package com.casic27.platform.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.casic27.platform.util.excel.factory.Excel2003Facotry;
import com.casic27.platform.util.excel.factory.Excel2007Factory;

public class ExcelUtils {

	public static String EXCEL2003 = "EXCEL2003";
	public static String EXCEL2007 = "EXCEL2007";
	public static String EXCEL2010 = "EXCEL2010";

	/**
	 * 创建excelReader，包含excel文件类型判断（1、对未知版本的EXCEL文件通过POI提供的方法判断文件头来获取版本信息。2、
	 * 若第一种不能完全满足要求，可以通过判断Workbook类型获取版本信息） 注：该方法主要满足excel未知类型和版本兼容的问题
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static ExcelReader createCommonExcelReaderByFileName(String fileName)
			throws IOException, InvalidFormatException {
		File file = new File(fileName);
		InputStream fis = new FileInputStream(file);
		return createCommonExcelReaderByStream(fis);
	}

	/**
	 * 创建excelReader，包含excel文件类型判断（1、对未知版本的EXCEL文件通过POI提供的方法判断文件头来获取版本信息。2、
	 * 若第一种不能完全满足要求，可以通过判断Workbook类型获取版本信息） 注：该方法主要满足excel未知类型和版本兼容的问题
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static ExcelReader createCommonExcelReaderByStream(InputStream fis)
			throws IOException, InvalidFormatException {
		// 首先判断流是否支持mark和reset方法，最后两个if分支中的方法才能支持
		if (!fis.markSupported()) {
			// 还原流信息
			fis = new PushbackInputStream(fis, 8);
		}
		// EXCEL2003使用的是微软的文件系统
		if (POIFSFileSystem.hasPOIFSHeader(fis)) {
			IExcelFactory iExcelFactory = new Excel2003Facotry();
			ExcelReader reader = iExcelFactory.createExcelReader(fis);
			reader.open();
			Workbook wb = reader.getWb();
			if (wb instanceof HSSFWorkbook) {
				return reader;
			}
			return null;
		}
		// EXCEL2007使用的是OOM文件格式
		else if (POIXMLDocument.hasOOXMLHeader(fis)) {
			// 可以直接传流参数，但是推荐使用OPCPackage容器打开
			IExcelFactory iExcelFactory = new Excel2007Factory();
			ExcelReader reader = iExcelFactory.createExcelReader(fis);
			reader.open();
			Workbook wb = reader.getWb();
			if (wb instanceof XSSFWorkbook) {
				return reader;
			}
			return null;
		} else {
			return null;// 不能解析的excel版本
			// throw new IOException("不能解析的excel版本");
		}
	}

	public static void expExcelDataByExcelVersion(String excelVersion,
			String exportFileName, String sheetTitle,
			List<List<String>> tableDataList, HttpServletResponse response)
			throws Exception {
		IExcelFactory iExcelFactory = null;
		if (excelVersion.equals(EXCEL2003) || StringUtils.isEmpty(excelVersion)) {
			iExcelFactory = new Excel2003Facotry();					
		} else if (excelVersion.equals(EXCEL2007)) {
			iExcelFactory = new Excel2007Factory();	
		} else if (excelVersion.equals(EXCEL2010)) {
			//==后期扩展
		} else {
			iExcelFactory = new Excel2003Facotry();	
		}
		ExcelWriter excelWriter = iExcelFactory.createExcelWriter();	
		excelWriter.exportExcelData(exportFileName, sheetTitle,
				tableDataList, response);

	}

	public static void main(String args[]) throws InvalidFormatException,
			FileNotFoundException, IOException {
		File file = new File("E:\\1.xlsx");
		ExcelReader readExcel = ExcelUtils
				.createCommonExcelReaderByStream(new FileInputStream(file));
		try {
			readExcel.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		readExcel.setSheetNum(0); // 设置读取索引为0的工作表
		// 总行数
		int count = readExcel.getRowCount();
		int count1 = readExcel.getSheet().getPhysicalNumberOfRows();
		for (int i = 0; i <= count; i++) {
			String[] rows = readExcel.readExcelLine(i);
			for (int j = 0; j < rows.length; j++) {
				System.out.print(rows[j] + " ");
			}
			System.out.print("\n");
		}
	}
}
