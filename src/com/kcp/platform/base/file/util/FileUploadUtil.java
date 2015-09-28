package com.kcp.platform.base.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import com.kcp.platform.base.file.exception.FileUploadException;
import com.kcp.platform.sys.context.ApplicationContext;
import com.kcp.platform.util.StringUtils;

public class FileUploadUtil {
	/**
     * 取得上传文件的保存路径，相对于<FILE_SERVER_URL >/<UPLOAD_FILE_ROOT>而言
     * 
     * @return
     */
    public static String getSaveRelativePath(String saveFileName){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        
        return year + "/" + month + "/" + saveFileName;
    }
    
	/**
	 * 根据给定的文件名取得该文件的类型
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileType(String fileName) {

		int pointPos = fileName.lastIndexOf(".");

		if (pointPos == -1)
			return "";

		return fileName.substring(pointPos + 1, fileName.length());
	}

	/**
	 * 删除文件或目录
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {

		if (file.exists() && file.isDirectory()) {
			File[] subFileArray = file.listFiles();
			for (File subFile : subFileArray) {
				deleteFile(subFile);
			}
		}

		file.delete();
	}

	/**
	 * 将输入流保存成文件
	 * 
	 * @param file
	 *            保存的文件
	 * @param in
	 *            输入流
	 * @return
	 */
	public static boolean saveFile(File file, InputStream in) throws FileUploadException {
		boolean result = false;
		if (file == null || in == null)
			return result;
		FileOutputStream out = null;
		try {
			// 如果指定文件的目录不存在,则创建之.
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			out = new FileOutputStream(file);
			byte[] c = new byte[1024];
			int slen;
			while ((slen = in.read(c)) > 0) {
				out.write(c, 0, slen);
			}
			result = true;
		} catch (Exception e) {
			throw new FileUploadException("文件保存失败!",e);
		} finally {
			if (out != null) {
				try {
					out.close();//解压完成后注意关闭输出流对象
				} catch (IOException e) {
					throw new FileUploadException("文件保存失败!",e);
				} 
			}
		}
		return result;
	}
	
	/**
     * 取得响应头文件的文件类型
     * @param request
     * @param fileType
     * @return
     */
    public static String getResponseContentType(HttpServletRequest request, String fileType) 
    {
        String result = "APPLICATION/OCTET-STREAM";

        if (request == null || StringUtils.isEmpty(fileType))
            return result;

        fileType = fileType.trim().toLowerCase();

        try
        {
            Properties properties = new Properties();;
            InputStream fileInputStream = new FileInputStream(ApplicationContext.getInstance().getWebRoot() + "/BM_FILE_TYPE.properties");
            properties.load(fileInputStream);
            String outPutType = (String) properties.get(fileType);
            if (StringUtils.isNotEmpty(outPutType)) {
                result = outPutType;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
    /**
	 * 将zip文件输出至outputStream输出流中
	 * @param despath
	 * @param outputStream
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static boolean zipFileToOS(String despath, OutputStream outputStream)throws IOException{
		boolean result = false;
		java.util.zip.ZipFile inFile = null;
		ZipOutputStream out = null;
		try {
			inFile = new java.util.zip.ZipFile(new File(despath));
			out = new java.util.zip.ZipOutputStream(outputStream);
			Enumeration entriesIter = inFile.entries();
			while (entriesIter.hasMoreElements())
			{
				java.util.zip.ZipEntry entry = (java.util.zip.ZipEntry)entriesIter.nextElement();
				InputStream incoming = inFile.getInputStream(entry);
				byte[] data = new byte[1024 * 16];
				out.putNextEntry(new java.util.zip.ZipEntry(entry.getName()));
				int readCount;
				while ((readCount = incoming.read(data, 0, data.length))!=-1){
					out.write(data, 0, readCount);
				}
				out.closeEntry();
			}
			result = true;
		} catch (ZipException e) {
			result = false;
			e.printStackTrace();
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		} finally{
			if(inFile!=null){
				inFile.close();
			}
			if(out!=null){
				out.close();
			}
		}
		return result;
	}
	
	/**
	 * 将一般文件输出至outputStream输出流中
	 * @param despath
	 * @param outputStream
	 * @throws IOException
	 */
	public static boolean fileToOS(String despath, OutputStream outputStream)throws IOException{
		boolean result = false;
		File file = new File(despath);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                outputStream.write(buffer);
            }
            outputStream.flush();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                is.close();
        }
        
        return result;
	}
	
    public static boolean isZip(String fileType){
		if("zip".equalsIgnoreCase(fileType)||"docx".equalsIgnoreCase(fileType)
				||"xlsx".equalsIgnoreCase(fileType)||"pptx".equalsIgnoreCase(fileType)){
			return true;
		}
		return false;
	}
    
}
