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
package com.kcp.platform.ui.form.ckfinder.connector.handlers.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Element;

import com.kcp.platform.ui.form.ckfinder.connector.configuration.Constants;
import com.kcp.platform.ui.form.ckfinder.connector.configuration.IConfiguration;
import com.kcp.platform.ui.form.ckfinder.connector.data.FilePostParam;
import com.kcp.platform.ui.form.ckfinder.connector.errors.ConnectorException;
import com.kcp.platform.ui.form.ckfinder.connector.utils.AccessControlUtil;
import com.kcp.platform.ui.form.ckfinder.connector.utils.FileUtils;

/**
 * Class to handle <code>MoveFiles</code> command.
 */
public class MoveFilesCommand extends XMLCommand implements IPostCommand{


	private List<FilePostParam> files;
	private int filesMoved;
	private int movedAll;
	private boolean addMoveNode;

	@Override
	protected void createXMLChildNodes(final int errorNum, final Element rootElement)
															throws ConnectorException {
		if (creator.hasErrors()) {
			Element errorsNode = creator.getDocument().createElement("Errors");
			creator.addErrors(errorsNode);
			rootElement.appendChild(errorsNode);
		}

		if (addMoveNode) {
			createMoveFielsNode(rootElement);
		}
	}

	/**
	 * creates move file XML node.
	 *
	 * @param rootElement
	 *           XML root element.
	 */
	private void createMoveFielsNode(final Element rootElement) {
		Element element = creator.getDocument().createElement("MoveFiles");
		element.setAttribute("moved", String.valueOf(this.filesMoved));
		element.setAttribute("movedTotal",
				String.valueOf(this.movedAll + this.filesMoved));
		rootElement.appendChild(element);
	}

	@Override
	protected int getDataForXml() {

		if (!AccessControlUtil.getInstance(configuration).checkFolderACL(
				this.type,
				this.currentFolder,
				this.userRole,
				AccessControlUtil.CKFINDER_CONNECTOR_ACL_FILE_RENAME
						| AccessControlUtil.CKFINDER_CONNECTOR_ACL_FILE_DELETE
						| AccessControlUtil.CKFINDER_CONNECTOR_ACL_FILE_UPLOAD)) {
			return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
		}

		try {
			return moveFiles();
		} catch (Exception e) {
			this.exception = e;
		}
		//this code should never be reached
		return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNKNOWN;

	}

	/**
	 * move files.
	 *
	 * @return error code.
	 * @throws IOException when io error in debug mode occurs
	 */
	private int moveFiles() throws IOException {
		this.filesMoved = 0;
		this.addMoveNode = false;
		for (FilePostParam file : files) {

			if (!FileUtils.checkFileName(file.getName())) {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
			}
			if (Pattern.compile(Constants.INVALID_PATH_REGEX).matcher(
					file.getFolder()).find()) {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
			}

			if (configuration.getTypes().get(file.getType()) == null) {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
			}

			if (file.getFolder() == null || file.getFolder().equals("")) {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
			}
			if (FileUtils.checkFileExtension(file.getName(),
					this.configuration.getTypes().get(this.type),
					this.configuration,
					false) == 1) {
				creator.appendErrorNodeChild(
						Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION,
						file.getName(), file.getFolder(), file.getType());
				continue;
			}

			if (!this.type.equals(file.getType())) {
				if (FileUtils.checkFileExtension(file.getName(),
						this.configuration.getTypes().get(file.getType()),
						this.configuration,
						false) == 1) {
					creator.appendErrorNodeChild(
							Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION,
							file.getName(), file.getFolder(), file.getType());
					continue;
				}
			}

			if (FileUtils.checkIfFileIsHidden(file.getName(),
					this.configuration)) {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
			}

			if (FileUtils.checkIfDirIsHidden(file.getFolder(), this.configuration)) {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
			}

			if (!AccessControlUtil.getInstance(this.configuration)
					.checkFolderACL(file.getType(), file.getFolder(), this.userRole,
							AccessControlUtil.CKFINDER_CONNECTOR_ACL_FILE_VIEW)) {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;

			}
			File sourceFile = new File(configuration
										.getTypes().get(file.getType()).getPath()
								+ file.getFolder(), file.getName());
			File destFile = new File(configuration
										.getTypes().get(this.type).getPath()
								+ this.currentFolder, file.getName());

            File sourceThumb = new File(configuration
										.getThumbsPath()+ File.separator + file.getType()
								+ file.getFolder() + file.getName());
			try {
				if (!sourceFile.exists() || !sourceFile.isFile()) {
					creator.appendErrorNodeChild(
							Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND,
							file.getName(), file.getFolder(), file.getType());
					continue;
				}
				if (!this.type.equals(file.getType())) {
					Long maxSize = configuration.getTypes().get(this.type).getMaxSize();
					if (maxSize != null && maxSize < sourceFile.length()) {
						creator.appendErrorNodeChild(
							Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG,
							file.getName(), file.getFolder(), file.getType());
						continue;
					}
				}
				if (sourceFile.equals(destFile)) {
					creator.appendErrorNodeChild(
							Constants.Errors.
									CKFINDER_CONNECTOR_ERROR_SOURCE_AND_TARGET_PATH_EQUAL,
							file.getName(), file.getFolder(), file.getType());
					continue;
				} else if (destFile.exists()) {
					if (file.getOptions() != null
							&& file.getOptions().indexOf("overwrite") != -1) {
						if (!handleOverwrite(sourceFile, destFile)) {
							creator.appendErrorNodeChild(
								Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED,
								file.getName(), file.getFolder(),
								file.getType());
							continue;
						} else {
							this.filesMoved++;
                            FileUtils.delete(sourceThumb);
						}
					} else if (file.getOptions() != null
							&& file.getOptions().indexOf("autorename") != -1) {
						if (!handleAutoRename(sourceFile, destFile)) {
							creator.appendErrorNodeChild(
								Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED,
								file.getName(), file.getFolder(),
								file.getType());
							continue;
						} else {
							this.filesMoved++;
                            FileUtils.delete(sourceThumb);
						}
					} else {
						creator.appendErrorNodeChild(
								Constants.Errors.CKFINDER_CONNECTOR_ERROR_ALREADY_EXIST,
								file.getName(), file.getFolder(), file.getType());
						continue;
					}
				} else {
					if (FileUtils.copyFromSourceToDestFile(sourceFile, destFile,
															true, configuration)) {
						this.filesMoved++;
						moveThumb(file);
					}
				}
			} catch (SecurityException e) {
				if (configuration.isDebugMode()) {
					throw e;
				} else {
					creator.appendErrorNodeChild(
							Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED,
								file.getName(), file.getFolder(), file.getType());
					continue;
				}
			} catch (IOException e) {
				if (configuration.isDebugMode()) {
					throw e;
				} else {
					creator.appendErrorNodeChild(
							Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED,
								file.getName(), file.getFolder(), file.getType());
					continue;
				}
			}

		}
		this.addMoveNode = true;
		if (creator.hasErrors()) {
			return Constants.Errors.CKFINDER_CONNECTOR_ERROR_MOVE_FAILED;
		} else {
			return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
		}
	}

	/**
	 * Handles autorename option.
	 * @param sourceFile source file to move from.
	 * @param destFile destenation file to move to.
	 * @return true if moved correctly
	 * @throws IOException when ioerror occurs
	 */
	private boolean handleAutoRename(final File sourceFile, final File destFile)
																	throws IOException {
		int counter = 1;
		File newDestFile = destFile;
		while (true) {
			String filePath = FileUtils.getFileNameWithoutExtension(destFile
					.getAbsolutePath())
					+ "("
					+ counter
					+ ")."
					+ FileUtils.getFileExtension(destFile.getName());
			newDestFile = new File(filePath);
			if (!newDestFile.exists()) {
				// can't be in one if=, becouse when error in
				// copy file occurs then it will be infinity loop
				return (FileUtils.copyFromSourceToDestFile(sourceFile,
						newDestFile, true, configuration));
			} else {
				counter++;
			}
		}
	}

	/**
	 * Handles overwrite option.
	 * @param sourceFile source file to move from.
	 * @param destFile destenation file to move to.
	 * @return true if moved correctly
	 * @throws IOException when ioerror occurs
	 */
	private boolean handleOverwrite(final File sourceFile, final File destFile)
																	throws IOException {
		return FileUtils.delete(destFile)
				&& FileUtils.copyFromSourceToDestFile(sourceFile, destFile,
														true, configuration);
	}

	/**
	 * move thumb file.
	 *
	 * @param file
	 *            file to move.
	 * @throws IOException when ioerror occurs
	 */
	private void moveThumb(final FilePostParam file) throws IOException {
		File sourceThumbFile = new File(configuration.getThumbsPath()
													+ File.separator + file.getType()
													+ file.getFolder() + file.getName());
		File destThumbFile = new File(configuration.getThumbsPath()
													+ File.separator + type
													+ this.currentFolder
													+ file.getName());

		FileUtils
				.copyFromSourceToDestFile(sourceThumbFile, destThumbFile,
											true, configuration);

	}

	@Override
	public void initParams(final HttpServletRequest request,
			final IConfiguration configuration, final Object... params)
			throws ConnectorException {
		super.initParams(request, configuration);
		this.files = new ArrayList<FilePostParam>();
		this.movedAll = (request.getParameter("moved") != null) ? Integer
				.valueOf(request.getParameter("moved")) : 0;

		getFilesListFromRequest(request);

	}

	/**
	 * get file list to copy from request.
	 *
	 * @param request
	 *            request
	 */
	private void getFilesListFromRequest(final HttpServletRequest request) {
		int i = 0;
		while (true) {
			String paramName = "files[" + i + "][name]";
			if (request.getParameter(paramName) != null) {
				FilePostParam file = new FilePostParam();
				file.setName(getParameter(request, paramName));
				file.setFolder(getParameter(request, "files[" + i + "][folder]"));
				file.setOptions(getParameter(request, "files[" + i
						+ "][options]"));
				file.setType(getParameter(request, "files[" + i + "][type]"));
				files.add(file);
			} else {
				break;
			}
			i++;
		}
	}

}
