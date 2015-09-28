package com.kcp.platform.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;

public class FileUtil {
	/**
	 * 写文件
	 * @param fileName
	 * @param content
	 */
	public static void writeFile(String fileName, String content) {
		writeFile(fileName, content, "utf-8");
	}
	
	/**
	 * 写文件
	 * @param fileName
	 * @param content
	 * @param charset
	 */
	public static void writeFile(String fileName, String content, String charset) {
		try {
			createFolder(fileName, true);
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), charset));

			out.write(content);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 写文件
	 * @param fileName
	 * @param is
	 * @throws IOException
	 */
	public static void writeFile(String fileName, InputStream is)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		byte[] bs = new byte[512];
		int n = 0;
		while ((n = is.read(bs)) != -1) {
			fos.write(bs, 0, n);
		}
		is.close();
		fos.close();
	}
	/**
	 * 读文件
	 * @param fileName
	 * @return
	 */
	public static String readFile(String fileName) {
		try {
			File file = new File(fileName);
			String charset = getCharset(file);
			StringBuffer sb = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName), charset));
			String str;
			while ((str = in.readLine()) != null) {
				sb.append(str + "\r\n");
			}
			in.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 获取文件编码
	 * @param file
	 * @return
	 */
	public static String getCharset(File file) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));

			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1)
				return charset;
			if ((first3Bytes[0] == -1) && (first3Bytes[1] == -2)) {
				charset = "UTF-16LE";
				checked = true;
			} else if ((first3Bytes[0] == -2) && (first3Bytes[1] == -1)) {
				charset = "UTF-16BE";
				checked = true;
			} else if ((first3Bytes[0] == -17) && (first3Bytes[1] == -69)
					&& (first3Bytes[2] == -65)) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();

			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc++;
					if (read < 240) {
						if ((128 > read) || (read > 191)) {
							if ((192 <= read) && (read <= 223)) {
								read = bis.read();
								if ((128 > read) || (read > 191)) {
									break;
								}

							} else if ((224 <= read) && (read <= 239)) {
								read = bis.read();
								if ((128 <= read) && (read <= 191)) {
									read = bis.read();
									if ((128 <= read) && (read <= 191)) {
										charset = "UTF-8";
									}
								}
							}
						}
					}
				}

			}

			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}
	/**
	 * 输入流转化为二进制字节
	 * @param is
	 * @return
	 */
	public static byte[] readByte(InputStream is) {
		try {
			byte[] r = new byte[is.available()];
			is.read(r);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 文件读取为二进制
	 * @param fileName
	 * @return
	 */
	public static byte[] readByte(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			byte[] r = new byte[fis.available()];
			fis.read(r);
			fis.close();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 二进制写入文件
	 * @param fileName
	 * @param b
	 * @return
	 */
	public static boolean writeByte(String fileName, byte[] b) {
		try {
			BufferedOutputStream fos = new BufferedOutputStream(
					new FileOutputStream(fileName));

			fos.write(b);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 删除文件夹
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
	/**
	 * 序列化到文件
	 * @param obj
	 * @param fileName
	 */
	public static void serializeToFile(Object obj, String fileName) {
		try {
			ObjectOutput out = new ObjectOutputStream(new FileOutputStream(
					fileName));

			out.writeObject(obj);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 文件中的对象反序列化
	 * @param fileName
	 * @return
	 */
	public static Object deserializeFromFile(String fileName) {
		try {
			File file = new File(fileName);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					file));

			Object obj = in.readObject();
			in.close();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 数据流转化为字符串
	 * @param input
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream input, String charset)
			throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(input,
				charset));

		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line + "\n");
		}
		return buffer.toString();
	}
	/**
	 * 数据流转化为字符串
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream input)
			throws IOException {
		return inputStream2String(input, "utf-8");
	}
	/**
	 * 获取路径下的文件列表
	 * @param path
	 * @return
	 */
	public static File[] getFiles(String path) {
		File file = new File(path);
		return file.listFiles();
	}
	
	/**
	 * 创建文件夹
	 * @param path
	 */
	public static void createFolderFile(String path) {
		createFolder(path, true);
	}
	
	/**
	 * 创建文件夹
	 * @param path
	 * @param isFile
	 */
	public static void createFolder(String path, boolean isFile) {
		if (isFile) {
			path = path.substring(0, path.lastIndexOf(File.separator));
		}
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
	}
	
	/**
	 * 创建文件夹
	 * @param dirstr
	 * @param name
	 */
	public void createFolder(String dirstr, String name) {
		dirstr = StringUtils.trimSufffix(dirstr, File.separator)
				+ File.separator + name;

		File dir = new File(dirstr + name);
		dir.mkdir();
	}
	
	/**
	 * 重命名文件夹
	 * @param path
	 * @param newName
	 */
	public static void renameFolder(String path, String newName) {
		File file = new File(path);
		if (file.exists())
			file.renameTo(new File(newName));
	}
	/**
	 * 获取文件夹下的文件夹
	 * @param dir
	 * @return
	 */
	public static ArrayList<File> getDiretoryOnly(File dir) {
		ArrayList dirs = new ArrayList();
		if ((dir != null) && (dir.exists()) && (dir.isDirectory())) {
			File[] files = dir.listFiles(new FileFilter() {
				public boolean accept(File file) {
					if (file.isDirectory())
						return true;
					return false;
				}
			});
			for (int i = 0; i < files.length; i++) {
				dirs.add(files[i]);
			}
		}
		return dirs;
	}
	/**
	 * 获取文件夹下的文件
	 * @param dir
	 * @return
	 */
	public ArrayList<File> getFileOnly(File dir) {
		ArrayList dirs = new ArrayList();
		File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				if (file.isFile())
					return true;
				return false;
			}
		});
		for (int i = 0; i < files.length; i++) {
			dirs.add(files[i]);
		}
		return dirs;
	}
	/**
	 * 删除文件
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		File file = new File(path);
		return file.delete();
	}
	
	/**
	 * 复制文件
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean copyFile(String from, String to) {
		File fromFile = new File(from);
		File toFile = new File(to);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(fromFile);
			fos = new FileOutputStream(toFile);

			byte[] buf = new byte[4096];
			int bytesRead;
			while ((bytesRead = fis.read(buf)) != -1) {
				fos.write(buf, 0, bytesRead);
			}
			fos.flush();
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 备份文件
	 * @param filePath
	 */
	public static void backupFile(String filePath) {
		String backupName = filePath + ".bak";
		File file = new File(backupName);
		if (file.exists()) {
			file.delete();
		}
		copyFile(filePath, backupName);
	}
	
	/**
	 * 获取文件扩展名
	 * @param file
	 * @return
	 */
	public static String getFileExt(File file) {
		if (file.isFile()) {
			return getFileExt(file.getName());
		}
		return "";
	}
	/**
	 * 获取文件扩展名
	 * @param fileName
	 * @return
	 */
	public static String getFileExt(String fileName) {
		int pos = fileName.lastIndexOf(".");
		if (pos > -1) {
			return fileName.substring(pos + 1).toLowerCase();
		}
		return "";
	}
	
	/**
	 * 复制文件夹
	 * @param fromDir
	 * @param toDir
	 * @throws IOException
	 */
	public static void copyDir(String fromDir, String toDir) throws IOException {
		new File(toDir).mkdirs();
		File[] file = new File(fromDir).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				String fromFile = file[i].getAbsolutePath();
				String toFile = toDir + "/" + file[i].getName();

				copyFile(fromFile, toFile);
			}
			if (file[i].isDirectory())
				copyDirectiory(fromDir + "/" + file[i].getName(), toDir + "/"
						+ file[i].getName());
		}
	}

	private static void copyDirectiory(String fromDir, String toDir)
			throws IOException {
		new File(toDir).mkdirs();
		File[] file = new File(fromDir).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				String fromName = file[i].getAbsolutePath();
				String toFile = toDir + "/" + file[i].getName();
				copyFile(fromName, toFile);
			}
			if (file[i].isDirectory())
				copyDirectiory(fromDir + "/" + file[i].getName(), toDir + "/"
						+ file[i].getName());
		}
	}
	/**
	 * 获取文件大小
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileSize(File file) throws IOException {
		if (file.isFile()) {
			FileInputStream fis = new FileInputStream(file);
			int size = fis.available();
			fis.close();
			return getSize(size);
		}
		return "";
	}
	/**
	 * 文件大小转化为可读模式
	 * @param size
	 * @return
	 */
	public static String getSize(double size) {
		DecimalFormat df = new DecimalFormat("0.00");
		if (size > 1048576.0D) {
			double ss = size / 1048576.0D;
			return df.format(ss) + " M";
		}
		if (size > 1024.0D) {
			double ss = size / 1024.0D;
			return df.format(ss) + " KB";
		}
		return size + " bytes";
	}
	
	/**
	 * 下载文件 
	 * @param response
	 * @param fullPath
	 * @param fileName
	 * @throws IOException
	 */
	public static void downLoadFile(HttpServletResponse response,
			String fullPath, String fileName) throws IOException {
		OutputStream outp = response.getOutputStream();
		File file = new File(fullPath);
		if (file.exists()) {
			response.setContentType("APPLICATION/OCTET-STREAM");
			String filedisplay = URLEncoder.encode(fileName, "UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ filedisplay);
			FileInputStream in = null;
			try {
				outp = response.getOutputStream();
				in = new FileInputStream(fullPath);
				byte[] b = new byte[1024];
				int i = 0;
				while ((i = in.read(b)) > 0) {
					outp.write(b, 0, i);
				}
				outp.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					in.close();
					in = null;
				}
				if (outp != null) {
					outp.close();
					outp = null;
					response.flushBuffer();
				}
			}
		} else {
			outp.write("文件不存在!".getBytes("utf-8"));
		}
	}
	
	/**
	 * 获取父文件夹
	 * @param baseDir
	 * @param currentFile
	 * @return
	 */
	public static String getParentDir(String baseDir, String currentFile) {
		File f = new File(currentFile);
		String parentPath = f.getParent();
		String path = parentPath.replace(baseDir, "");
		return path.replace(File.separator, "/");
	}
	/**
	 * class目录路径
	 * @return
	 */
	public static String getClassesPath() {
		String templatePath = Thread.currentThread().getContextClassLoader()
				.getResource("").toString();
		if (templatePath.startsWith("file:/")) {
			templatePath = templatePath.replaceFirst("file:/", "");
		}
		templatePath = templatePath.replace("/", File.separator);
		return templatePath;
	}
	/**
	 * 获取Class跟目录
	 * @return
	 */
	public static String getRootPath() {
		String rootPath = getClassesPath();
		String toReplace = "WEB-INF" + File.separator + "classes"
				+ File.separator;
		rootPath = rootPath.replace(toReplace, "");
		return rootPath;
	}
}
