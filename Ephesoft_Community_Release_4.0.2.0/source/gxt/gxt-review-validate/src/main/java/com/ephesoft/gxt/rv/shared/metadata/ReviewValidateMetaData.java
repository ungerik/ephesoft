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

package com.ephesoft.gxt.rv.shared.metadata;

import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.core.shared.metadata.DocumentMetaData;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ReviewValidateMetaData implements IsSerializable {

	private String baseHTTPUrl;

	private BatchInstanceStatus batchInstanceStatus;

	private Map<String, DocumentMetaData> documentMetadata;

	private String batchInstanceIdentifier;

	private String batchInstanceName;

	private String batchInstancePriority;

	private List<String> documentTypeNamesList;

	private String batchClassIdentifier;

	private int zoomCount;

	public String getResuorceAbsoluteURL(final String resourceName) {
		return this.baseHTTPUrl + "/" + resourceName;
	}

	public String getBatchClassIdentifier() {
		return batchClassIdentifier;
	}

	public void setBatchClassIdentifier(String batchClassIdentifier) {
		this.batchClassIdentifier = batchClassIdentifier;
	}

	/**
	 * @param baseHTTPUrl the baseHTTPUrl to set
	 */
	public void setBaseHTTPUrl(final String baseHTTPUrl) {
		this.baseHTTPUrl = baseHTTPUrl;
	}

	/**
	 * @return the batchInstanceStatus
	 */
	public BatchInstanceStatus getBatchInstanceStatus() {
		return batchInstanceStatus;
	}

	/**
	 * @param batchInstanceStatus the batchInstanceStatus to set
	 */
	public void setBatchInstanceStatus(final BatchInstanceStatus batchInstanceStatus) {
		this.batchInstanceStatus = batchInstanceStatus;
	}

	/**
	 * @return the documentIdentifiers
	 */
	public Map<String, DocumentMetaData> getDocumentMetadata() {
		return documentMetadata;
	}

	/**
	 * @param documentIdentifiers the documentIdentifiers to set
	 */
	public void setDocumentMetadata(final Map<String, DocumentMetaData> documentIdentifiers) {
		this.documentMetadata = documentIdentifiers;
	}

	/**
	 * @return the baseHTTPUrl
	 */
	public String getBaseHTTPUrl() {
		return baseHTTPUrl;
	}

	/**
	 * @return the batchInstanceIdentifier
	 */
	public String getBatchInstanceIdentifier() {
		return batchInstanceIdentifier;
	}

	/**
	 * @param batchInstanceIdentifier the batchInstanceIdentifier to set
	 */
	public void setBatchInstanceIdentifier(final String batchInstanceIdentifier) {
		this.batchInstanceIdentifier = batchInstanceIdentifier;
	}

	/**
	 * @return the batchInstanceName
	 */
	public String getBatchInstanceName() {
		return batchInstanceName;
	}

	/**
	 * @param batchInstanceName the batchInstanceName to set
	 */
	public void setBatchInstanceName(final String batchInstanceName) {
		this.batchInstanceName = batchInstanceName;
	}

	/**
	 * @return the documentTypeNamesList
	 */
	public List<String> getDocumentTypeNamesList() {
		return documentTypeNamesList;
	}

	/**
	 * @param documentTypeNamesList the documentTypeNamesList to set
	 */
	public void setDocumentTypeNamesList(final List<String> documentTypeNamesList) {
		this.documentTypeNamesList = documentTypeNamesList;
	}

	/**
	 * @return the batchInstancePriority
	 */
	public String getBatchInstancePriority() {
		return batchInstancePriority;
	}

	/**
	 * @param batchInstancePriority the batchInstancePriority to set
	 */
	public void setBatchInstancePriority(final String batchInstancePriority) {
		this.batchInstancePriority = batchInstancePriority;
	}

	public int getZoomCount() {
		return zoomCount;
	}

	public void setZoomCount(int zoomCount) {
		this.zoomCount = zoomCount;
	}
}
