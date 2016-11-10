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

package com.ephesoft.gxt.core.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ImportDocumentTypeDTO implements IsSerializable {

	private int priority;

	private float minConfidenceThreshold;

	private String name;

	private String description;

	/**
	 */
	private String firstPageProjectFileName;

	/**
	 */
	private String secondPageProjectFileName;

	/**
	 */
	private String thirdPageProjectFileName;

	/**
	 */
	private String fourthPageProjectFileName;

	private boolean isHidden;

	public ImportDocumentTypeDTO() {
		super();
		this.name = "";
		this.description = "";
		this.isHidden = false;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public float getMinConfidenceThreshold() {
		return minConfidenceThreshold;
	}

	public void setMinConfidenceThreshold(float minConfidenceThreshold) {
		this.minConfidenceThreshold = minConfidenceThreshold;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the firstPageProjectFileName
	 */
	public String getFirstPageProjectFileName() {
		return firstPageProjectFileName;
	}

	/**
	 * @param rspProjectFileName the firstPageProjectFileName to set
	 */
	public void setFirstPageProjectFileName(String rspProjectFileName) {
		this.firstPageProjectFileName = rspProjectFileName;
	}

	/**
	 * @return the secondPageProjectFileName
	 */
	public String getSecondPageProjectFileName() {
		return secondPageProjectFileName;
	}

	/**
	 * @param rspProjectFileName the secondPageProjectFileName to set
	 */
	public void setSecondPageProjectFileName(String rspProjectFileName) {
		this.secondPageProjectFileName = rspProjectFileName;
	}

	/**
	 * @return the thirdPageProjectFileName
	 */
	public String getThirdPageProjectFileName() {
		return thirdPageProjectFileName;
	}

	/**
	 * @param rspProjectFileName the thirdPageProjectFileName to set
	 */
	public void setThirdPageProjectFileName(String rspProjectFileName) {
		this.thirdPageProjectFileName = rspProjectFileName;
	}

	/**
	 * @return the fourthPageProjectFileName
	 */
	public String getFourthPageProjectFileName() {
		return fourthPageProjectFileName;
	}

	/**
	 * @param rspProjectFileName the fourthPageProjectFileName to set
	 */
	public void setFourthPageProjectFileName(String rspProjectFileName) {
		this.fourthPageProjectFileName = rspProjectFileName;
	}

}
