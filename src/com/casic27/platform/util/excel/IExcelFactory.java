package com.casic27.platform.util.excel;

import java.io.File;
import java.io.InputStream;

public interface IExcelFactory {

	public ExcelReader createExcelReader();
	
	public ExcelReader createExcelReader(File file);
	
	public ExcelReader createExcelReader(InputStream is);
	
	public ExcelWriter createExcelWriter();
}
