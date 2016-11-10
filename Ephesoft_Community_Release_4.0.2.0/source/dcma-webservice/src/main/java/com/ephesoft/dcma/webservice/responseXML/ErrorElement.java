/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2015 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.webservice.responseXML;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is used for creating the error body for the result of web service.
 * 
 * @author Ephesoft
 * @version 3.0
 * @see RootElement
 * 
 */
@XmlRootElement
public class ErrorElement {

	/**
	 * The custom code received for this exception.
	 */
	private int customCode;
	/**
	 * The cause of the exception received.
	 */
	private String cause;
	/**
	 * The url for details of this exception received.
	 */
	private String infoUrl;

	/**
	 * Getter for customCode.
	 * @return int
	 */
	public int getCustomCode() {
		return customCode;
	}

	/**
	 * Setter for customCode. 
	 * @param customCode int
	 */
	@XmlElement(name = "Custom_Code")
	public void setCustomCode(final int customCode) {
		this.customCode = customCode;
	}

	/**
	 * Getter for cause.
	 * @return {@link String}
	 */
	public String getCause() {
		return cause;
	}

	/**
	 * Setter for cause.
	 * @param cause {@link String}
	 */
	@XmlElement(name = "Cause")
	public void setCause(final String cause) {
		this.cause = cause;
	}

	/**
	 * Getter for infoUrl.
	 * @return {@link String}
	 */
	public String getInfoUrl() {
		return infoUrl;
	}

	/**
	 * Setter for infoUrl.
	 * @param infoUrl {@link String}
	 */
	@XmlElement(name = "More_Info")
	public void setInfoUrl(final String infoUrl) {
		this.infoUrl = infoUrl;
	}

	/**
	 * Parameterized constructor.
	 * @param customCode int
	 * @param cause {@link String}
	 * @param infoUrl {@link String}
	 */
	public ErrorElement(final int customCode, final String cause,
			final String infoUrl) {
		super();
		this.customCode = customCode;
		this.cause = cause;
		this.infoUrl = infoUrl;
	}

	/**
	 * Default constructor.
	 */
	public ErrorElement() {
		super();
	}

}
