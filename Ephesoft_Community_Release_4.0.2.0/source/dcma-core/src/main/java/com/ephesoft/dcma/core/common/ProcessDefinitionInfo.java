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

package com.ephesoft.dcma.core.common;

import com.ephesoft.dcma.core.component.ICommonConstants;

/**
 * This class represents object that carries information for custom workflow creation for Batch class and modules
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class ProcessDefinitionInfo {

	/**
	 * {@link String}
	 */
	private String subProcessName;

	/**
	 * boolean
	 */
	private boolean isScriptingPlugin;

	/**
	 * {@link String}
	 */
	private String scriptingFileName;

	/**
	 * {@link String}
	 */
	private String backUpFileName;

	/**
	 * {@link String}
	 */
	private String subProcessKey;

	/**
	 * Single Argument Constructor
	 * 
	 * @param subProcessName {@link String}
	 */
	public ProcessDefinitionInfo(String subProcessName) {
		this(subProcessName, false, ICommonConstants.EMPTY_STRING, ICommonConstants.EMPTY_STRING, subProcessName);
	}

	/**
	 * Sets the Sub Process Key
	 * 
	 * @return {@link String}
	 */
	public String getSubProcessKey() {
		return subProcessKey;
	}

	/**
	 * Sets the Sub-Process Name
	 * 
	 * @param subProcessName {@link String}
	 */
	public void setSubProcessName(String subProcessName) {
		this.subProcessName = subProcessName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subProcessName == null) ? 0 : subProcessName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		boolean isEqual = true;
		if (this == obj) {
			isEqual = true;
		} else {
			if (obj == null) {
				isEqual = false;
			} else {
				if (getClass() != obj.getClass()) {
					isEqual = false;
				} else {
					ProcessDefinitionInfo other = (ProcessDefinitionInfo) obj;
					if (subProcessName == null) {
						if (other.subProcessName != null)
							isEqual = false;
					} else if (!subProcessName.equals(other.subProcessName))
						isEqual = false;
				}
			}
		}
		return isEqual;
	}

	/**
	 * 
	 * @param subProcessName {@link String}
	 * @param isScriptingPlugin boolean
	 * @param scriptingFileName {@link String}
	 * @param backUpFileName {@link String}
	 * @param subProcessKey {@link String}
	 */
	public ProcessDefinitionInfo(String subProcessName, boolean isScriptingPlugin, String scriptingFileName, String backUpFileName,
			String subProcessKey) {
		this.subProcessName = subProcessName;
		this.isScriptingPlugin = isScriptingPlugin;
		this.scriptingFileName = scriptingFileName;
		this.backUpFileName = backUpFileName;
		this.subProcessKey = subProcessKey;
	}

	/**
	 * Fetches Scripting File Name
	 * 
	 * @return {@link String}
	 */
	public String getScriptingFileName() {
		return scriptingFileName;
	}

	/**
	 * Fetches Backup File Name
	 * 
	 * @return {@link String}
	 */
	public String getBackUpFileName() {
		return backUpFileName;
	}

	/**
	 * Fetches Sub-Process Name
	 * 
	 * @return {@link String}
	 */
	public String getSubProcessName() {
		return subProcessName;
	}

	/**
	 * Flag to determine whether Instance is a Scripting Plugin
	 * 
	 * @return boolean
	 */
	public boolean isScriptingPlugin() {
		return isScriptingPlugin;
	}

}
