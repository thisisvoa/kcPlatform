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

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Element;

import com.kcp.platform.ui.form.ckfinder.connector.configuration.Constants;
import com.kcp.platform.ui.form.ckfinder.connector.configuration.IConfiguration;
import com.kcp.platform.ui.form.ckfinder.connector.errors.ConnectorException;
import com.kcp.platform.ui.form.ckfinder.connector.utils.AccessControlUtil;
import com.kcp.platform.ui.form.ckfinder.connector.utils.FileUtils;

/**
 * Class to handle <code>RenameFile</code> command.
 */
public class RenameFileCommand extends XMLCommand implements IPostCommand{

	private String fileName;
	private String newFileName;
	private boolean renamed;
	private boolean addRenameNode;

	@Override
	protected void createXMLChildNodes(final int errorNum,
			final Element rootElement) throws ConnectorException {
		if (this.addRenameNode) {
			createRenamedFileNode(rootElement);
		}


	}

	/**
	 * create rename file XML node.
	 *
	 * @param rootElement
	 *           XML root node
	 */
	private void createRenamedFileNode(final Element rootElement) {
		Element element = creator.getDocument().createElement("RenamedFile");
		element.setAttribute("name", this.fileName);
		if (renamed) {
			element.setAttribute("newName", this.newFileName);
		}
		rootElement.appendChild(element);
	}

	/**
	 * gets data for XML and checks all validation.
	 *
	 * @return error code or 0 if it's correct.
	 */
	@Override
	protected int getDataForXml() {

		if (!AccessControlUtil.getInstance(configuration).checkFolderACL(
				this.type, this.currentFolder, this.userRole,
				AccessControlUtil.CKFINDER_CONNECTOR_ACL_FILE_RENAME)) {
			return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
		}

		if (configuration.forceASCII()) {
			this.newFileName = FileUtils.convertToASCII(this.newFileName);
		}

		if (this.fileName != null && !this.fileName.equals("")
				&& this.newFileName != null && !this.newFileName.equals("")) {
			this.addRenameNode = true;
		}

		int checkFileExt = FileUtils.checkFileExtension(this.newFileName,
				this.configuration.getTypes().get(this.type),
				this.configuration, true);
		if (checkFileExt == 1) {
			return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION;
		} else if (checkFileExt == 2) {
			this.newFileName = FileUtils.renameFileWithBadExt(this.newFileName);
		}

		if (!FileUtils.checkFileName(this.fileName)
				|| FileUtils.checkIfFileIsHidden(this.fileName,
						configuration)) {
			return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
		}

		if (!FileUtils.checkFileName(this.newFileName, configuration)
				|| FileUtils.checkIfFileIsHidden(this.newFileName,
						configuration)) {
			return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
		}

		if (FileUtils.checkFileExtension(this.fileName,
				this.configuration.getTypes().get(this.type),
				this.configuration, false) == 1) {
			return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
		}

		String dirPath = configuration.getTypes().get(this.type).getPath()
				+ this.currentFolder;

		File file = new File(dirPath, this.fileName);
		File newFile = new File(dirPath, this.newFileName);
		File dir = new File(dirPath);

		try {
			if (!file.exists()) {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND;
			}

			if (newFile.exists()) {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ALREADY_EXIST;
			}

			if (!dir.canWrite() || !file.canWrite()) {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
			}
			this.renamed = file.renameTo(newFile);
			if (this.renamed) {
				renameThumb();
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
			} else {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
			}
		} catch (SecurityException e) {
			if (configuration.isDebugMode()) {
				throw e;
			} else {
				return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
			}
		}


	}

	/**
	 * rename thumb file.
	 */
	private void renameThumb() {
		File thumbFile = new File(configuration.getThumbsPath()
				+ File.separator + type + this.currentFolder,
				this.fileName);
		File newThumbFile = new File(configuration.getThumbsPath()
				+ File.separator + type + this.currentFolder,
				this.newFileName);

		thumbFile.renameTo(newThumbFile);

	}

	@Override
	public void initParams(final HttpServletRequest request,
			final IConfiguration configuration, final Object... params)
			throws ConnectorException {
		super.initParams(request, configuration);
		this.fileName = getParameter(request, "fileName");
		this.newFileName = getParameter(request, "newFileName");
	}



}
