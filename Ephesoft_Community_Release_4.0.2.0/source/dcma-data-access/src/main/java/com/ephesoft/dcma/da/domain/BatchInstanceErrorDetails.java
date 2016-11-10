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

package com.ephesoft.dcma.da.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * This class is a identity class for keeping error details for a batch that went into error.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see javax.persistence.Table.
 */
@Entity
@Table(name = "batch_instance_error_details")
public class BatchInstanceErrorDetails extends AbstractChangeableEntity {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * identifier {@link String}.
	 */
	@Column(name = "identifier", unique = true)
	private String identifier;

	/**
	 * {@link String} Last executed plugin name.
	 */
	@Column(name = "error_plugin_name")
	private String lastPluginName;

	/**
	 * {@link String} Last executed module name.
	 * 
	 */
	@Column(name = "error_module_name")
	private String lastModuleName;

	/**
	 * {@link String} Error message.
	 */
	@Column(name = "error_message", length=1000)
	private String errorMessage;

	/**
	 * Gets last executed plugin name.
	 * 
	 * @return {@link String}
	 */
	public String getLastPluginName() {
		return lastPluginName;
	}

	/**
	 * Sets last executed plugin name.
	 * 
	 * @param errorPluginName {@link String}
	 */
	public void setLastPluginName(final String errorPluginName) {
		this.lastPluginName = errorPluginName;
	}

	/**
	 * Gets last executed module name.
	 * 
	 * @return {@link String}
	 */
	public String getLastModuleName() {
		return lastModuleName;
	}

	/**
	 * Sets last executed module name.
	 * 
	 * @param errorModuleName {@link String}
	 */
	public void setLastModuleName(final String errorModuleName) {
		this.lastModuleName = errorModuleName;
	}

	/**
	 * Gets the identifier.
	 * 
	 * @return {@link String}.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the identifier.
	 * 
	 * @param identifier {@link String}.
	 */
	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Gets the error message.
	 * 
	 * @return {@link String}.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets the error message.
	 * 
	 * @param errorMessage {@link String}
	 */
	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
