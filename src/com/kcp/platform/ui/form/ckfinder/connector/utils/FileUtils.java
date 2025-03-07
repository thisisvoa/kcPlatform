/*
 * CKFinder
 * ========
 * http://ckfinder.com
 * Copyright (C) 2007-2012, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying or distribute this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 */

package com.kcp.platform.ui.form.ckfinder.connector.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.fileupload.FileItem;

import com.kcp.platform.ui.form.ckfinder.connector.ServletContextFactory;
import com.kcp.platform.ui.form.ckfinder.connector.configuration.Constants;
import com.kcp.platform.ui.form.ckfinder.connector.configuration.IConfiguration;
import com.kcp.platform.ui.form.ckfinder.connector.data.ResourceType;

import java.io.FileFilter;
import java.net.URLEncoder;
import java.util.Collections;

/**
 * Utils for files.
 *
 */
public class FileUtils {

	/**
	 * max read file buffer size.
	 */
	private static final int MAX_BUFFER_SIZE = 1024;


	private static final Map<String, String> UTF8_LOWER_ACCENTS
														= new HashMap<String, String>();
	private static final Map<String, String> UTF8_UPPER_ACCENTS
														= new HashMap<String, String>();


        private static final Map<String, String> encodingMap;
        static {
            Map<String, String> mapHelper = new HashMap<String, String>();
            mapHelper.put("%21", "!");
	    mapHelper.put("%27", "'");
	    mapHelper.put("%28", "(");
	    mapHelper.put("%29", ")");
	    mapHelper.put("%7E", "~");
	    mapHelper.put("[+]", "%20");
            encodingMap = Collections.unmodifiableMap(mapHelper);
        }

	/**
	 * Gets list of childeren folder or files for dir, according to searchDirs param.
	 * @param dir folder to search.
	 * @param searchDirs if true method return list of folders, otherwise list of files.
	 * @return list of files or subdirectores in selected directory
	 */
	public static List<String> findChildrensList(final File dir,
			final boolean searchDirs) {
		List<String> files = new ArrayList<String>();
		for (String subFiles : dir.list()) {
			File file = new File(dir + "/" + subFiles);
			if ((searchDirs && file.isDirectory())
					|| (!searchDirs && !file.isDirectory())) {
				files.add(file.getName());
			}
		}
		return files;
	}

	/**
	 * Gets file extension.
	 * @param fileName name of file.
	 * @return file extension
	 */
	public static String getFileExtension(final String fileName) {
		if (fileName == null
				|| fileName.lastIndexOf(".") == -1
				|| fileName.lastIndexOf(".") == fileName.length() - 1) {
			return null;
		}
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	/**
	 * Gets file name without it's extension.
	 * @param fileName name of file
	 * @return file extension
	 */
	public static String getFileNameWithoutExtension(final String fileName) {
		if (fileName == null
				|| fileName.lastIndexOf(".") == -1) {
			return null;
		}
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	/**
	 * Print file content to outputstream.
	 * @param file file to be printed.
	 * @param out outputstream.
	 * @throws IOException when io error occurs.
	 */
	public static void printFileContentToResponse(final File file,
			final OutputStream out) throws IOException {
		FileInputStream in = null;
		if (file.length() == 0) {
			return;
		}
		try {
			in = new FileInputStream(file);
			byte[] buf = null;
			if (file.length() < MAX_BUFFER_SIZE) {
				buf = new byte[(int) file.length()];
			} else {
				buf = new byte[MAX_BUFFER_SIZE];
			}
			
                        int numRead = 0;
                        while ((numRead = in.read(buf)) != -1) {
                            out.write(buf, 0, numRead);
                        }
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				throw e;
			}
		}
	}

	/**
	 *
	 * @param sourceFile source file
	 * @param destFile destenation file
	 * @param move if source file should be deleted.
	 * @param conf connector configuration
	 * @return true if file moved/copied correctly
	 * @throws IOException when IOerror occurs
	 */
	public static boolean copyFromSourceToDestFile(final File sourceFile,
												final File destFile,
												final boolean move,
												final IConfiguration conf)
												throws IOException {
		createPath(destFile, conf, true);
		InputStream in = new FileInputStream(sourceFile);
		OutputStream out = new FileOutputStream(destFile);
		byte[] buf = new byte[MAX_BUFFER_SIZE];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
		if (move) {
			sourceFile.delete();
		}
		return true;

	}

	/**
	 * gets full path of file.
	 * @param file path to a file in ckfinder
	 * @return full path
	 * @throws Exception when servlet context is null
	 */
	public static String getFullPath(final String file) throws Exception {
		if (checkFileParentExists(PathUtils.escape(file))) {
			return file;
		} else {
			return ServletContextFactory.getServletContext().getRealPath(file);
		}
	}


	/**
	 * check if parent of file in param exists and is directory.
	 * @param file file to check
	 * @return true if is directory and exists.
	 */
	private static boolean checkFileParentExists(final String file) {
		String fileName = PathUtils.removeSlashFromEnd(file);
		File dir = new File(fileName.substring(0, fileName.lastIndexOf("/")));
		return dir.exists() && dir.isDirectory();
	}

	/**
	 * Parse date with pattern yyyyMMddHHmm. Pattern is used in get file command
	 * response XML.
	 * @param file input file.
	 * @return parsed file modification date.
	 */
	public static String parseLastModifDate(final File file) {
		Date date = new Date(file.lastModified());
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		return dateFormat.format(date);
	}

	/**
	 * check if dirname matches configuration hidden folder regex.
	 * @param dirName dir name
	 * @param conf connector configuration
	 * @return true if matches.
	 */
	public static boolean checkIfDirIsHidden(final String dirName,
											 final IConfiguration conf) {
		if (dirName == null || dirName.equals("")) {
			return false;
		}
		String dir = PathUtils.removeSlashFromEnd(PathUtils.escape(dirName));
		Scanner sc = new Scanner(dir).useDelimiter("/");
		while (sc.hasNext()) {
			boolean check = Pattern.compile(getHiddenFileOrFolderRegex(
								conf.getHiddenFolders())).matcher(sc.next()).matches();
			if (check) {
				return true;
			}
		}
		return false;
	}

	/**
	 * check if filename matches configuration hidden file regex.
	 * @param fileName file name
	 * @param conf connector configuration
	 * @return true if matches.
	 */
	public static boolean checkIfFileIsHidden(final String fileName,
											  final IConfiguration conf) {
		return Pattern.compile(getHiddenFileOrFolderRegex(
									conf.getHiddenFiles())).matcher(fileName).matches();
	}

	/**
	 * get hidden folder regex pattern.
	 * @param hiddenList list of hidden file or files patterns.
	 * @return full folder regerx pattern
	 */
	private static String getHiddenFileOrFolderRegex(final List<String> hiddenList) {
		StringBuilder sb = new StringBuilder("(");
		for (String item : hiddenList) {
			if (sb.length() > 3) {
				sb.append("|");
			}

			item = item.replaceAll("\\.", "\\\\.");
			item = item.replaceAll("\\*", ".+");
			item = item.replaceAll("\\?", ".");

			sb.append("(");
			sb.append(item);
			sb.append(")");


		}
		sb.append(")+");
		return sb.toString();
	}

	/**
	 * deletes file or folder with all subfolders and subfiles.
	 * @param file file or directory to delete.
	 * @return true if all files are deleted.
	 */
	public static boolean delete(final File file) {

		if (file.isDirectory()) {
			for (String item : file.list()) {
				File subFile = new File(file.getAbsolutePath()
										+ File.separator + item);
				if (!delete(subFile)) {
					return false;
				}
			}
		}
		return file.delete();
	}

	/**
	 * check if file or folder name doesn't match invalid name.
	 * @param fileName file name
	 * @return true if file name is correct
	 */
	public static boolean checkFileName(final String fileName) {
		return !(fileName == null || fileName.equals("")
			|| fileName.charAt(fileName.length() - 1) == '.'
			|| fileName.contains("..")
			|| checkFolderNamePattern(fileName));
	}

	/**
	 * check if new folder name contains disallowed chars.
	 * @param fileName file name
	 * @return true if it does contain disallowed characters.
	 */
	private static boolean checkFolderNamePattern(final String fileName) {
		Pattern pattern = Pattern.compile(Constants.INVALID_FILE_NAME_REGEX);
		Matcher matcher = pattern.matcher(fileName);
		return matcher.find();
	}


	/**
	 * checks if file extension is on denied list or isn't on allowed list.
	 * @param fileName filename
	 * @param type resurece type
	 * @param conf connector configuraion
	 * @param renameReq for renaming file if file ext is double
	 * @return 0 if ok, 1 if not ok, 2 if rename reqired
	 */
	public static int checkFileExtension(final String fileName,
											 final ResourceType type,
											 final IConfiguration conf,
											 final boolean renameReq) {
		if (type == null || fileName == null) {
			return 1;
		}

		if (fileName.indexOf('.') == -1) {
			return 0;
		}

		if (conf.ckeckDoubleFileExtensions()) {
			StringTokenizer tokens = new StringTokenizer(fileName, ".");
			StringTokenizer tokens2 = new StringTokenizer(fileName, ".");
			String currToken = "";
			while (true) {

				if (tokens2.hasMoreTokens()) {
					currToken = tokens2.nextToken();
				} else if (!checkSingleExtension(currToken, type)) {
					return 1;
				} else {
					break;
				}
			}

			if (renameReq) {
				String cfileName = tokens.nextToken();
				while (tokens.hasMoreTokens()) {
					currToken = tokens.nextToken();
					if (tokens.hasMoreElements()) {
						cfileName = cfileName.concat(
								checkSingleExtension(currToken, type) ? "." : "_");
						cfileName = cfileName.concat(currToken);
					} else {
						cfileName.concat(".".concat(currToken));
					}
				}

				return 2;
			}
			return 0;
		} else {
			return checkSingleExtension(getFileExtension(fileName), type) ? 0 : 1;
		}
	}

	/**
	 * checks is file extension is on denied list or isn't on allowed list.
	 * @param fileExt file extension
	 * @param type file resource type
	 * @return 0 if isn't on denied and is on allowed, otherwise 1
	 */
	private static boolean checkSingleExtension(final String fileExt,
									 final ResourceType type) {
		Scanner scanner = new Scanner(type.getDeniedExtensions()).useDelimiter(",");
		while (scanner.hasNext()) {
			if (scanner.next().equalsIgnoreCase(fileExt)) {
				return false;
			}
		}
		Scanner scanner1 = new Scanner(type.getAllowedExtensions()).useDelimiter(",");
		while (scanner1.hasNext()) {
			if (scanner1.next().equalsIgnoreCase(fileExt)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * converts filename to connector encoding.
	 * @param fileName file name
	 * @param configuration connector configuration
	 * @return encoded file name
	 */
	public static String convertFromUriEncoding(final String fileName,
													final IConfiguration configuration) {
		try {
			return new String(fileName.getBytes(configuration.getUriEncoding()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return fileName;
		}
	}

	/**
	 * converts filename to ASCII.
	 * @param fileName file name
	 * @return encoded file name
	 */
	public static String convertToASCII(final String fileName) {
		String newFileName = fileName;
		fillLowerAccents();
		fillUpperAccents();
		for (String s : UTF8_LOWER_ACCENTS.keySet()) {
			newFileName = newFileName.replace(s, UTF8_LOWER_ACCENTS.get(s));
		}

		for (String s : UTF8_UPPER_ACCENTS.keySet()) {
			newFileName = newFileName.replace(s, UTF8_UPPER_ACCENTS.get(s));
		}
		return newFileName;
	}


	/**
	 * creates file and all above folders that doesn"t exists.
	 * @param file file to create.
	 * @param conf connector configuration
	 * @param isFile if it is path to folder.
	 * @throws IOException when io error occurs.
	 */
	public static void createPath(final File file, final IConfiguration conf,
								  final boolean isFile)
																	throws IOException {
		String path = file.getAbsolutePath();
		// on Linux first path char - "/" is removed by StringTokenizer
		StringTokenizer st = new StringTokenizer(path, File.separator);

		// if path include "/" as a first char of path we have to add it manually
		String checkPath = (path.indexOf(File.separator) == 0)
												? File.separator : "";
		checkPath += (String) st.nextElement();
		while (st.hasMoreElements()) {
			String string = (String) st.nextElement();
			checkPath = checkPath.concat(File.separator + string);
			if (!(string.equals(file.getName()) && isFile)) {
				File dir = new File(checkPath);
				if (!dir.exists()) {
					mkdir(dir, conf);
				}
			} else {
				file.createNewFile();
			}
		}
	}

	/**
	 * Creates dir.
	 * @param dir dir to create.
	 * @param configuration connector configuration
	 * @return create dir result.
	 */
	public static boolean mkdir(final File dir, final IConfiguration configuration) {
		return dir.mkdir();
	}

	/**
	 * check if file size isn't bigger then max size for type.
	 * @param type resource type
	 * @param fileSize file size
	 * @return true if file size isn't bigger then max size for type.
	 */
	public static boolean checkFileSize(final ResourceType type, final long fileSize) {
		return (type.getMaxSize() == null || type.getMaxSize() > fileSize);
	}

	/**
	 * check if file has html file extension.
	 * @param file file name
	 * @param configuration connector configuration
	 * @return true if has
	 */
	public static boolean checkIfFileIsHtmlFile(final String file,
												final IConfiguration configuration) {

		return configuration.getHTMLExtensions().contains(
													getFileExtension(file).toLowerCase());

	}

	/**
	 * Detect HTML in the first KB to prevent against potential security issue with
	 * IE/Safari/Opera file type auto detection bug.
	 * Returns true if file contain insecure HTML code at the beginning.
	 * @param item file upload item
	 * @return true if dedected.
	 * @throws IOException when io error occurs.
	 */
	public static boolean detectHtml(final FileItem item) throws IOException {
		byte[] buff = new byte[MAX_BUFFER_SIZE];
		InputStream is = null;
		try {
			is = item.getInputStream();
			is.read(buff, 0, MAX_BUFFER_SIZE);
			String content = new String(buff);
			content = content.toLowerCase().trim();

			if (Pattern.compile("<!DOCTYPE\\W+X?HTML.+",
								Pattern.CASE_INSENSITIVE
								| Pattern.DOTALL
								| Pattern.MULTILINE).matcher(content).matches()) {
				return true;
			}

			String[] tags = {"<body", "<head", "<html", "<img", "<pre",
							 "<script", "<table", "<title"};

			for (String tag : tags) {
				if (content.indexOf(tag) != -1) {
					return true;
				}
			}

			if (Pattern.compile("type\\s*=\\s*[\'\"]?\\s*(?:\\w*/)?(?:ecma|java)",
					Pattern.CASE_INSENSITIVE
					| Pattern.DOTALL
					| Pattern.MULTILINE).matcher(content).find()) {
				return true;
			}


			if (Pattern.compile(
					"(?:href|src|data)\\s*=\\s*[\'\"]?\\s*(?:ecma|java)script:",
					Pattern.CASE_INSENSITIVE
					| Pattern.DOTALL
					| Pattern.MULTILINE).matcher(content).find()) {
				return true;
			}


			if (Pattern.compile("url\\s*\\(\\s*[\'\"]?\\s*(?:ecma|java)script:",
					Pattern.CASE_INSENSITIVE
					| Pattern.DOTALL
					| Pattern.MULTILINE).matcher(content).find()) {
				return true;
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (is != null) {
				is.close();
			}
		}	
		return false;
	}

        /**
	 * Checks if folder has any subfolders but respects ACL and hideFolders
	 * setting from configuration.
	 * @param dirPath path to current folder.
         * @param dir current folder being checked. Represented by File object.
         * @param configuration configuration object.
         * @param resourceType name of resource type, folder is assignd to.
         * @param currentUserRole user role.
	 * @return true if there are any allowed and non-hidden subfolders.
	 */
        public static Boolean hasChildren( String dirPath, File dir, IConfiguration configuration, String resourceType, String currentUserRole )
        {
                FileFilter fileFilter = new FileFilter() {
                    public boolean accept(File file) {
                        return file.isDirectory();
                    }
                };
                File [] subDirsList =  dir.listFiles(fileFilter);

                if( subDirsList != null ){
                    for (int i = 0; i < subDirsList.length; i++)
                    {
                            String subDirName = subDirsList[i].getName();

                            if ( !FileUtils.checkIfDirIsHidden(subDirName, configuration) &&
                                    AccessControlUtil.getInstance(configuration).checkFolderACL(resourceType,
                                    dirPath+subDirName, currentUserRole, AccessControlUtil.CKFINDER_CONNECTOR_ACL_FOLDER_VIEW))
                            return true;
                    }
                }
                return false;
 	}

	/**
	 * rename file with double extension.
	 * @param fileName file name
	 * @return new file name with . replaced with _ (but not last)
	 */
	public static String renameFileWithBadExt(final String fileName) {
		if (fileName == null) {
			return null;
		}

		return getFileNameWithoutExtension(fileName)
								.replace('.', '_').concat("."
								+ FileUtils.getFileExtension(fileName));
	}

        public static String encodeURIComponent(final String fileName) throws UnsupportedEncodingException{
		String fileNameHelper = URLEncoder.encode(fileName, "utf-8");
		for (Map.Entry<String, String> entry : encodingMap.entrySet()){
			fileNameHelper = fileNameHelper.replaceAll(entry.getKey(), entry.getValue());
		}
		return fileNameHelper;
	}

        public static boolean checkFolderName( final String folderName, final IConfiguration configuration )
        {
            if ( ( configuration.isDisallowUnsafeCharacters() && 
                    ( folderName.contains(".") || folderName.contains(";") ) )
                    || FileUtils.checkFolderNamePattern(folderName) ){
                return false;
            }
            return true;
 	}

        public static boolean checkFileName( final String fileName, final IConfiguration configuration ){
             if ( ( configuration.isDisallowUnsafeCharacters() && fileName.contains(";")  )
                    || !FileUtils.checkFileName(fileName) )
                 return false;
             return true;
        }

        public static String backupWithBackSlash (final String fileName, final String toReplace){
            return fileName.replaceAll(toReplace, "\\\\"+toReplace);
        }

	/**
	 * fills data for upper accesnts map.
	 */
	private static void fillUpperAccents() {
		if (UTF8_UPPER_ACCENTS.size() == 0) {
			UTF8_UPPER_ACCENTS.put("À", "A");
			UTF8_UPPER_ACCENTS.put("Ô", "O");
			UTF8_UPPER_ACCENTS.put("Ď", "D");
			UTF8_UPPER_ACCENTS.put("Ḟ", "F");
			UTF8_UPPER_ACCENTS.put("Ë", "E");
			UTF8_UPPER_ACCENTS.put("Š", "S");
			UTF8_UPPER_ACCENTS.put("Ơ", "O");
			UTF8_UPPER_ACCENTS.put("Ă", "A");
			UTF8_UPPER_ACCENTS.put("Ř", "R");
			UTF8_UPPER_ACCENTS.put("Ț", "T");
			UTF8_UPPER_ACCENTS.put("Ň", "N");
			UTF8_UPPER_ACCENTS.put("Ā", "A");
			UTF8_UPPER_ACCENTS.put("Ķ", "K");
			UTF8_UPPER_ACCENTS.put("Ŝ", "S");
			UTF8_UPPER_ACCENTS.put("Ỳ", "Y");
			UTF8_UPPER_ACCENTS.put("Ņ", "N");
			UTF8_UPPER_ACCENTS.put("Ĺ", "L");
			UTF8_UPPER_ACCENTS.put("Ħ", "H");
			UTF8_UPPER_ACCENTS.put("Ṗ", "P");
			UTF8_UPPER_ACCENTS.put("Ó", "O");
			UTF8_UPPER_ACCENTS.put("Ú", "U");
			UTF8_UPPER_ACCENTS.put("Ě", "E");
			UTF8_UPPER_ACCENTS.put("É", "E");
			UTF8_UPPER_ACCENTS.put("Ç", "C");
			UTF8_UPPER_ACCENTS.put("Ẁ", "W");
			UTF8_UPPER_ACCENTS.put("Ċ", "C");
			UTF8_UPPER_ACCENTS.put("Õ", "O");
			UTF8_UPPER_ACCENTS.put("Ṡ", "S");
			UTF8_UPPER_ACCENTS.put("Ø", "O");
			UTF8_UPPER_ACCENTS.put("Ģ", "G");
			UTF8_UPPER_ACCENTS.put("Ŧ", "T");
			UTF8_UPPER_ACCENTS.put("Ș", "S");
			UTF8_UPPER_ACCENTS.put("Ė", "E");
			UTF8_UPPER_ACCENTS.put("Ĉ", "C");
			UTF8_UPPER_ACCENTS.put("Ś", "S");
			UTF8_UPPER_ACCENTS.put("Î", "I");
			UTF8_UPPER_ACCENTS.put("Ű", "U");
			UTF8_UPPER_ACCENTS.put("Ć", "C");
			UTF8_UPPER_ACCENTS.put("Ę", "E");
			UTF8_UPPER_ACCENTS.put("Ŵ", "W");
			UTF8_UPPER_ACCENTS.put("Ṫ", "T");
			UTF8_UPPER_ACCENTS.put("Ū", "U");
			UTF8_UPPER_ACCENTS.put("Č", "C");
			UTF8_UPPER_ACCENTS.put("Ö", "Oe");
			UTF8_UPPER_ACCENTS.put("È", "E");
			UTF8_UPPER_ACCENTS.put("Ŷ", "Y");
			UTF8_UPPER_ACCENTS.put("Ą", "A");
			UTF8_UPPER_ACCENTS.put("Ł", "L");
			UTF8_UPPER_ACCENTS.put("Ų", "U");
			UTF8_UPPER_ACCENTS.put("Ů", "U");
			UTF8_UPPER_ACCENTS.put("Ş", "S");
			UTF8_UPPER_ACCENTS.put("Ğ", "G");
			UTF8_UPPER_ACCENTS.put("Ļ", "L");
			UTF8_UPPER_ACCENTS.put("Ƒ", "F");
			UTF8_UPPER_ACCENTS.put("Ž", "Z");
			UTF8_UPPER_ACCENTS.put("Ẃ", "W");
			UTF8_UPPER_ACCENTS.put("Ḃ", "B");
			UTF8_UPPER_ACCENTS.put("Å", "A");
			UTF8_UPPER_ACCENTS.put("Ì", "I");
			UTF8_UPPER_ACCENTS.put("Ï", "I");
			UTF8_UPPER_ACCENTS.put("Ḋ", "D");
			UTF8_UPPER_ACCENTS.put("Ť", "T");
			UTF8_UPPER_ACCENTS.put("Ŗ", "R");
			UTF8_UPPER_ACCENTS.put("Ä", "Ae");
			UTF8_UPPER_ACCENTS.put("Í", "I");
			UTF8_UPPER_ACCENTS.put("Ŕ", "R");
			UTF8_UPPER_ACCENTS.put("Ê", "E");
			UTF8_UPPER_ACCENTS.put("Ü", "Ue");
			UTF8_UPPER_ACCENTS.put("Ò", "O");
			UTF8_UPPER_ACCENTS.put("Ē", "E");
			UTF8_UPPER_ACCENTS.put("Ñ", "N");
			UTF8_UPPER_ACCENTS.put("Ń", "N");
			UTF8_UPPER_ACCENTS.put("Ĥ", "H");
			UTF8_UPPER_ACCENTS.put("Ĝ", "G");
			UTF8_UPPER_ACCENTS.put("Đ", "D");
			UTF8_UPPER_ACCENTS.put("Ĵ", "J");
			UTF8_UPPER_ACCENTS.put("Ÿ", "Y");
			UTF8_UPPER_ACCENTS.put("Ũ", "U");
			UTF8_UPPER_ACCENTS.put("Ŭ", "U");
			UTF8_UPPER_ACCENTS.put("Ư", "U");
			UTF8_UPPER_ACCENTS.put("Ţ", "T");
			UTF8_UPPER_ACCENTS.put("Ý", "Y");
			UTF8_UPPER_ACCENTS.put("Ő", "O");
			UTF8_UPPER_ACCENTS.put("Ÿ", "Y");
			UTF8_UPPER_ACCENTS.put("Ũ", "U");
			UTF8_UPPER_ACCENTS.put("Ŭ", "U");
			UTF8_UPPER_ACCENTS.put("Ư", "U");
			UTF8_UPPER_ACCENTS.put("Ţ", "T");
			UTF8_UPPER_ACCENTS.put("Ý", "Y");
			UTF8_UPPER_ACCENTS.put("Ő", "O");
			UTF8_UPPER_ACCENTS.put("Â", "A");
			UTF8_UPPER_ACCENTS.put("Ľ", "L");
			UTF8_UPPER_ACCENTS.put("Ẅ", "W");
			UTF8_UPPER_ACCENTS.put("Ż", "Z");
			UTF8_UPPER_ACCENTS.put("Ī", "I");
			UTF8_UPPER_ACCENTS.put("Ã", "A");
			UTF8_UPPER_ACCENTS.put("Ġ", "G");
			UTF8_UPPER_ACCENTS.put("Ṁ", "M");
			UTF8_UPPER_ACCENTS.put("Ō", "O");
			UTF8_UPPER_ACCENTS.put("Ĩ", "I");
			UTF8_UPPER_ACCENTS.put("Ù", "U");
			UTF8_UPPER_ACCENTS.put("Į", "I");
			UTF8_UPPER_ACCENTS.put("Ź", "Z");
			UTF8_UPPER_ACCENTS.put("Á", "A");
			UTF8_UPPER_ACCENTS.put("Û", "U");
			UTF8_UPPER_ACCENTS.put("Þ", "Th");
			UTF8_UPPER_ACCENTS.put("Ð", "Dh");
			UTF8_UPPER_ACCENTS.put("Æ", "Ae");
			UTF8_UPPER_ACCENTS.put("Ĕ", "E");
		}
	}

	/**
	 * fills data for lower accesnts map.
	 */
	private static void fillLowerAccents() {
		if (UTF8_LOWER_ACCENTS.size() == 0) {
			UTF8_LOWER_ACCENTS.put("à", "a");
			UTF8_LOWER_ACCENTS.put("ô", "o");
			UTF8_LOWER_ACCENTS.put("ď", "d");
			UTF8_LOWER_ACCENTS.put("ḟ", "f");
			UTF8_LOWER_ACCENTS.put("ë", "e");
			UTF8_LOWER_ACCENTS.put("š", "s");
			UTF8_LOWER_ACCENTS.put("ơ", "o");
			UTF8_LOWER_ACCENTS.put("ß", "ss");
			UTF8_LOWER_ACCENTS.put("ă", "a");
			UTF8_LOWER_ACCENTS.put("ř", "r");
			UTF8_LOWER_ACCENTS.put("ț", "t");
			UTF8_LOWER_ACCENTS.put("ň", "n");
			UTF8_LOWER_ACCENTS.put("ā", "a");
			UTF8_LOWER_ACCENTS.put("ķ", "k");
			UTF8_LOWER_ACCENTS.put("ŝ", "s");
			UTF8_LOWER_ACCENTS.put("ỳ", "y");
			UTF8_LOWER_ACCENTS.put("ņ", "n");
			UTF8_LOWER_ACCENTS.put("ĺ", "l");
			UTF8_LOWER_ACCENTS.put("ħ", "h");
			UTF8_LOWER_ACCENTS.put("ṗ", "p");
			UTF8_LOWER_ACCENTS.put("ó", "o");
			UTF8_LOWER_ACCENTS.put("ú", "u");
			UTF8_LOWER_ACCENTS.put("ě", "e");
			UTF8_LOWER_ACCENTS.put("é", "e");
			UTF8_LOWER_ACCENTS.put("ç", "c");
			UTF8_LOWER_ACCENTS.put("ẁ", "w");
			UTF8_LOWER_ACCENTS.put("ċ", "c");
			UTF8_LOWER_ACCENTS.put("õ", "o");
			UTF8_LOWER_ACCENTS.put("ṡ", "s");
			UTF8_LOWER_ACCENTS.put("ø", "o");
			UTF8_LOWER_ACCENTS.put("ģ", "g");
			UTF8_LOWER_ACCENTS.put("ŧ", "t");
			UTF8_LOWER_ACCENTS.put("ș", "s");
			UTF8_LOWER_ACCENTS.put("ė", "e");
			UTF8_LOWER_ACCENTS.put("ĉ", "c");
			UTF8_LOWER_ACCENTS.put("ś", "s");
			UTF8_LOWER_ACCENTS.put("î", "i");
			UTF8_LOWER_ACCENTS.put("ű", "u");
			UTF8_LOWER_ACCENTS.put("ć", "c");
			UTF8_LOWER_ACCENTS.put("ę", "e");
			UTF8_LOWER_ACCENTS.put("ŵ", "w");
			UTF8_LOWER_ACCENTS.put("ṫ", "t");
			UTF8_LOWER_ACCENTS.put("ū", "u");
			UTF8_LOWER_ACCENTS.put("č", "c");
			UTF8_LOWER_ACCENTS.put("ö", "oe");
			UTF8_LOWER_ACCENTS.put("è", "e");
			UTF8_LOWER_ACCENTS.put("ŷ", "y");
			UTF8_LOWER_ACCENTS.put("ą", "a");
			UTF8_LOWER_ACCENTS.put("ł", "l");
			UTF8_LOWER_ACCENTS.put("ų", "u");
			UTF8_LOWER_ACCENTS.put("ů", "u");
			UTF8_LOWER_ACCENTS.put("ş", "s");
			UTF8_LOWER_ACCENTS.put("ğ", "g");
			UTF8_LOWER_ACCENTS.put("ļ", "l");
			UTF8_LOWER_ACCENTS.put("ƒ", "f");
			UTF8_LOWER_ACCENTS.put("ž", "z");
			UTF8_LOWER_ACCENTS.put("ẃ", "w");
			UTF8_LOWER_ACCENTS.put("ḃ", "b");
			UTF8_LOWER_ACCENTS.put("å", "a");
			UTF8_LOWER_ACCENTS.put("ì", "i");
			UTF8_LOWER_ACCENTS.put("ï", "i");
			UTF8_LOWER_ACCENTS.put("ḋ", "d");
			UTF8_LOWER_ACCENTS.put("ť", "t");
			UTF8_LOWER_ACCENTS.put("ŗ", "r");
			UTF8_LOWER_ACCENTS.put("ä", "ae");
			UTF8_LOWER_ACCENTS.put("í", "i");
			UTF8_LOWER_ACCENTS.put("ŕ", "r");
			UTF8_LOWER_ACCENTS.put("ê", "e");
			UTF8_LOWER_ACCENTS.put("ü", "ue");
			UTF8_LOWER_ACCENTS.put("ò", "o");
			UTF8_LOWER_ACCENTS.put("ē", "e");
			UTF8_LOWER_ACCENTS.put("ñ", "n");
			UTF8_LOWER_ACCENTS.put("ń", "n");
			UTF8_LOWER_ACCENTS.put("ĥ", "h");
			UTF8_LOWER_ACCENTS.put("ĝ", "g");
			UTF8_LOWER_ACCENTS.put("đ", "d");
			UTF8_LOWER_ACCENTS.put("ĵ", "j");
			UTF8_LOWER_ACCENTS.put("ÿ", "y");
			UTF8_LOWER_ACCENTS.put("ũ", "u");
			UTF8_LOWER_ACCENTS.put("ŭ", "u");
			UTF8_LOWER_ACCENTS.put("ư", "u");
			UTF8_LOWER_ACCENTS.put("ţ", "t");
			UTF8_LOWER_ACCENTS.put("ý", "y");
			UTF8_LOWER_ACCENTS.put("ő", "o");
			UTF8_LOWER_ACCENTS.put("â", "a");
			UTF8_LOWER_ACCENTS.put("ľ", "l");
			UTF8_LOWER_ACCENTS.put("ẅ", "w");
			UTF8_LOWER_ACCENTS.put("ż", "z");
			UTF8_LOWER_ACCENTS.put("ī", "i");
			UTF8_LOWER_ACCENTS.put("ã", "a");
			UTF8_LOWER_ACCENTS.put("ġ", "g");
			UTF8_LOWER_ACCENTS.put("ṁ", "m");
			UTF8_LOWER_ACCENTS.put("ō", "o");
			UTF8_LOWER_ACCENTS.put("ĩ", "i");
			UTF8_LOWER_ACCENTS.put("ù", "u");
			UTF8_LOWER_ACCENTS.put("į", "i");
			UTF8_LOWER_ACCENTS.put("ź", "z");
			UTF8_LOWER_ACCENTS.put("á", "a");
			UTF8_LOWER_ACCENTS.put("û", "u");
			UTF8_LOWER_ACCENTS.put("þ", "th");
			UTF8_LOWER_ACCENTS.put("ð", "dh");
			UTF8_LOWER_ACCENTS.put("æ", "ae");
			UTF8_LOWER_ACCENTS.put("µ", "u");
			UTF8_LOWER_ACCENTS.put("ĕ", "e");

		}
	}
}
