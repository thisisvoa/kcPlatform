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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Element;

import com.kcp.platform.ui.form.ckfinder.connector.configuration.Constants;
import com.kcp.platform.ui.form.ckfinder.connector.configuration.Events.EventTypes;
import com.kcp.platform.ui.form.ckfinder.connector.data.InitCommandEventArgs;
import com.kcp.platform.ui.form.ckfinder.connector.data.PluginInfo;
import com.kcp.platform.ui.form.ckfinder.connector.data.ResourceType;
import com.kcp.platform.ui.form.ckfinder.connector.errors.ConnectorException;
import com.kcp.platform.ui.form.ckfinder.connector.utils.AccessControlUtil;
import com.kcp.platform.ui.form.ckfinder.connector.utils.FileUtils;
import com.kcp.platform.ui.form.ckfinder.connector.utils.PathUtils;

import java.util.ArrayList;

/**
 * Class to handle <code>Init</code> command.
 */
public class InitCommand extends XMLCommand {


	/**
	 * chars taken to license key.
	 */
	private static final int[] LICENSE_CHARS = {11, 0, 8, 12, 26, 2, 3, 25, 1};

	private static final int LICENSE_CHAR_NR = 5;

	private static final int HASH = 0xFF;

	private static final int LICENSE_KEY_LENGTH = 34;

	private String type;


	/**
	 * method from super class - not used in this command.
	 * @return 0
	 */
	@Override
	protected int getDataForXml() {
		return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
	}

	@Override
	protected void createXMLChildNodes(final int errorNum,
									   final Element rootElement)
															throws ConnectorException {
		if (errorNum == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE) {
			createConnectorData(rootElement);
			try {
				createResouceTypesData(rootElement);
			} catch (Exception e) {
				if (configuration.isDebugMode()) {
					throw new ConnectorException(e);
				}
			}
			createPluginsData(rootElement);
		}
	}


	/**
	 * Creates connector node in XML.
	 * @param rootElement root element in XML
	 */
	private void createConnectorData(final Element rootElement) {
		// connector info
		Element element = creator.getDocument().createElement("ConnectorInfo");
		element.setAttribute("enabled", String.valueOf(configuration.enabled()));
		element.setAttribute("s", getLicenseName());
		element.setAttribute("c",
							createLicenseKey(configuration.getLicenseKey()));
		element.setAttribute("thumbsEnabled", String.valueOf(
												configuration.getThumbsEnabled()));
		element.setAttribute("uploadCheckImages", configuration.checkSizeAfterScaling() ? "false" : "true");
		if (configuration.getThumbsEnabled())
		{
			element.setAttribute("thumbsUrl", configuration.getThumbsURL());
			element.setAttribute("thumbsDirectAccess", String.valueOf(
												configuration.getThumbsDirectAccess()));
			element.setAttribute("thumbsWidth", String.valueOf(configuration.getMaxThumbWidth()));
			element.setAttribute("thumbsHeight", String.valueOf(configuration.getMaxThumbHeight()));
		}
		element.setAttribute("imgWidth", String.valueOf(configuration.getImgWidth()));
		element.setAttribute("imgHeight", String.valueOf(configuration.getImgHeight()));
		if (configuration.getPlugins().size() > 0) {
			element.setAttribute("plugins", getPlugins());
		}
		rootElement.appendChild(element);
	}

	/**
	 * gets plugins names.
	 * @return plugins names.
	 */
	private String getPlugins() {
		StringBuffer sb = new StringBuffer();
		boolean first = false;
		for (PluginInfo item : configuration.getPlugins()) {
			if (item.isEnabled()) {
				if (first) {
					sb.append(",");
				}
				sb.append(item.getName());
				first = true;
			}
		}
		return sb.toString();
	}

	/**
	 * checks license key.
	 * @return license name if key is ok, or empty string if not.
	 */
	private String getLicenseName() {
		if (validateLicenseKey(configuration.getLicenseKey())) {
			int index = Constants.CKFINDER_CHARS.indexOf(configuration.getLicenseKey().charAt(0))
														% LICENSE_CHAR_NR;
			if (index == 1 || index == 4) {
				return configuration.getLicenseName();
			}
		}
		return "";
	}

	/**
	 * Creates license key from key in configuration.
	 * @param licenseKey license key from configuration
	 * @return hashed license key
	 */
	private String createLicenseKey(final String licenseKey) {
		if (validateLicenseKey(licenseKey)) {
			StringBuilder sb = new StringBuilder();
			for (int i : LICENSE_CHARS) {
				sb.append(licenseKey.charAt(i));
			}
			return sb.toString();
		}
		return "";
	}

	/**
	 * validates license key lenght.
	 * @param licenseKey config license key
	 * @return true if has correnct length
	 */
	private boolean validateLicenseKey(final String licenseKey) {
		return licenseKey != null && licenseKey.length() == LICENSE_KEY_LENGTH;
	}

	/**
	 * Creates plugins node in XML.
	 * @param rootElement root element in XML
	 * @throws ConnectorException when error in event handler occurs.
	 */
	public void createPluginsData(final Element rootElement) throws ConnectorException {
		Element element = creator.getDocument().createElement("PluginsInfo");
		rootElement.appendChild(element);
		InitCommandEventArgs args = new InitCommandEventArgs();
		args.setXml(this.creator);
		args.setRootElement(rootElement);
		if (configuration.getEvents() != null) {
			configuration.getEvents().run(EventTypes.InitCommand, args, configuration);
		}

	}

	/**
	 * Creates plugins node in XML.
	 * @param rootElement root element in XML
	 * @throws Exception when error occurs
	 */
	private void createResouceTypesData(final Element rootElement) throws Exception {
		//resurcetypes
		Element element = creator.getDocument().createElement("ResourceTypes");
		rootElement.appendChild(element);

                List<String> types = null;
                if(super.type != null && !super.type.equals("")){
                    types = new ArrayList<String>();
                    types.add(super.type);
                }else
                     types = getTypes();

		for (String key : types) {
			ResourceType resourceType = configuration.getTypes().get(key);
			if (((this.type == null || this.type.equals(key)) && resourceType != null)
				&& AccessControlUtil.getInstance(this.configuration)
												.checkFolderACL(key, "/", this.userRole,
								AccessControlUtil.CKFINDER_CONNECTOR_ACL_FOLDER_VIEW)) {

				Element childElement = creator.getDocument().
												createElement("ResourceType");
				childElement.setAttribute("name", resourceType.getName());
				childElement.setAttribute("acl", String.valueOf(
						AccessControlUtil.getInstance(configuration)
											.checkACLForRole(key, "/", this.userRole)));
				childElement.setAttribute("hash", randomHash(
														resourceType.getPath()));
				childElement
						.setAttribute(
								"allowedExtensions",
								resourceType.getAllowedExtensions());
				childElement
						.setAttribute(
								"deniedExtensions",
								resourceType.getDeniedExtensions());
				childElement.setAttribute("url", resourceType.getUrl() + "/");
				Long maxSize = resourceType.getMaxSize();
				childElement.setAttribute("maxSize", (maxSize != null && maxSize > 0) ? maxSize.toString() : "0");
				childElement.setAttribute("hasChildren",
                                                FileUtils.hasChildren("/", new File(PathUtils.escape(resourceType.getPath())),
                                                configuration, resourceType.getName(), this.userRole).toString());
				element.appendChild(childElement);
			}
		}
	}

	/**
	 * gets list of types names.
	 * @return list of types names.
	 */
	private List<String> getTypes() {
		if (configuration.getDefaultResourceTypes().size() > 0) {
			return configuration.getDefaultResourceTypes();
		} else {
			return configuration.getResourceTypesOrder();
		}
	}

	/**
	 * Gets hash for folders in XML response to avoid cached responses.
	 * @param folder folder
	 * @return hash value
	 * @throws Exception when error occurs and debug mode is on
	 */
	private String randomHash(final String folder) throws Exception {

		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			try {
				algorithm.update(folder.getBytes("UTF8"));
			} catch (UnsupportedEncodingException e) {
				if (configuration.isDebugMode()) {
					throw e;
				}
				algorithm.update(folder.getBytes());
			}
			byte[] messageDigest = algorithm.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				hexString.append(Integer.toHexString(HASH & messageDigest[i]));
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			if (configuration.isDebugMode()) {
				throw e;
			}
			return "";
		}
	}

	@Override
	protected boolean mustAddCurrentFolderNode() {
		return false;
	}

	@Override
	protected void getCurrentFolderParam(final HttpServletRequest request) {
		this.currentFolder = null;
	}


}
