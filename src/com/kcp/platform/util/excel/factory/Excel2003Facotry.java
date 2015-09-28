package com.kcp.platform.util.excel.factory;

import java.io.File;
import java.io.InputStream;

import com.kcp.platform.util.excel.ExcelReader;
import com.kcp.platform.util.excel.ExcelWriter;
import com.kcp.platform.util.excel.IExcelFactory;
import com.kcp.platform.util.excel.reader.Excel2003Reader;
import com.kcp.platform.util.excel.writer.Excel2003Writer;

public class Excel2003Facotry implements IExcelFactory {

	@Override
	public ExcelReader createExcelReader() {
		return new Excel2003Reader();
	}

	@Override
	public ExcelReader createExcelReader(File file) {
		return new Excel2003Reader(file);
	}

	@Override
	public ExcelReader createExcelReader(InputStream is) {
		return new Excel2003Reader(is);
	}

	@Override
	public ExcelWriter createExcelWriter(){
		return new Excel2003Writer();
	}

}
