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
package com.kcp.platform.ui.form.ckfinder.connector.data;

import org.w3c.dom.Element;

import com.kcp.platform.ui.form.ckfinder.connector.utils.XMLCreator;

/**
 * Event data for {@link com.kcp.platform.ui.form.ckfinder.connector.configuration.Events.EventTypes#InitCommand} event.
 */
public class InitCommandEventArgs extends EventArgs {

	private XMLCreator xml;
	private Element rootElement;

	/**
	 * @return the rootElement
	 */
	public final Element getRootElement() {
		return rootElement;
	}

	/**
	 * @param rootElement the rootElement to set
	 */
	public final void setRootElement(final Element rootElement) {
		this.rootElement = rootElement;
	}

	/**
	 * @return the xml
	 */
	public final XMLCreator getXml() {
		return xml;
	}

	/**
	 * @param xml the XML to set
	 */
	public final void setXml(final XMLCreator xml) {
		this.xml = xml;
	}


}
