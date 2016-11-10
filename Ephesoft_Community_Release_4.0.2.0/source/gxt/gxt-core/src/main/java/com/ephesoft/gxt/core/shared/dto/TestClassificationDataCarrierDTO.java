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

public class TestClassificationDataCarrierDTO implements IsSerializable, Selectable {

	private Float pageConfidence;
	private String docType;
	private String docIdentifier;
	private String classificationType;
	private String pageID;
	private String pageClassificationValue;
	private Float documentConfidence;
	private String pageName;
	private boolean validated;
	private String learnedFileName;
	private boolean selected;
	private boolean isNew;

	public Float getPageConfidence() {
		return pageConfidence;
	}

	public void setPageConfidence(Float confidence) {
		this.pageConfidence = confidence;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocIdentifier() {
		return docIdentifier;
	}

	public void setDocIdentifier(String docIdentifier) {
		this.docIdentifier = docIdentifier;
	}

	public String getClassificationType() {
		return classificationType;
	}

	public void setClassificationType(String classificationType) {
		this.classificationType = classificationType;
	}

	public String getPageID() {
		return pageID;
	}

	public void setPageID(String pageID) {
		this.pageID = pageID;
	}

	public String getPageClassificationValue() {
		return pageClassificationValue;
	}

	public void setPageClassificationValue(String pageClassificationValue) {
		this.pageClassificationValue = pageClassificationValue;
	}

	public void setDocumentConfidence(Float documentConfidence) {
		this.documentConfidence = documentConfidence;
	}

	public Float getDocumentConfidence() {
		return documentConfidence;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setLearnedFileName(String learnedFileName) {
		this.learnedFileName = learnedFileName;
	}

	public String getLearnedFileName() {
		return learnedFileName;
	}

	@Override
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isNew() {
		return this.isNew;
	}
}
