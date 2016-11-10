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

/**
 * Class that stores a record's information on client side.
 * 
 * @author Ephesoft
 */

public class BatchInstanceDTO implements IsSerializable, Selectable {

	/**
	 * The priority of this batch.
	 */
	private int priority;

	/**
	 * The unique identifier associated with each batch.
	 */
	private String batchIdentifier;

	/**
	 * Name of the batch.
	 */
	private String batchName;

	/**
	 * Name of the batch class to which this batch belongs to.
	 */
	private String batchClassName;

	/**
	 * Date and time when this batch was uploaded.
	 */
	private String uploadedOn;

	/**
	 * Date and time when this batch was imported.
	 */
	private String importedOn;

	/**
	 * No of documents present in this batch.
	 */
	private String noOfDocuments;

	/**
	 * Review status of the batch.
	 */
	private String reviewStatus;

	/**
	 * Validation status of this batch.
	 */
	private String validationStatus;

	/**
	 * Total number of pages in the batch.
	 */
	private String noOfPages;

	/**
	 * Status of the batch. Running, new, ready for review etc.
	 */
	private String status;

	/**
	 * Current user who has locked the batch.
	 */
	private String currentUser;

	/**
	 * Variable for isRemote.
	 */
	private boolean isRemote;

	private long id;

	private boolean selected;

	/**
	 * The path of UNC folder for the batch.
	 */
	private String uncSubFolderPath;

	private String errorCause;

	/**
	 * Date and Time the Batch Instance was last modified. {@link String}
	 */
	private String lastModified;

	/**
	 * {@link String} User Defined Data Column
	 */
	private String customColumn1;
	
	/**
	 * {@link String} User Defined Data Column
	 */
	private String customColumn2;
	
	/**
	 * {@link String} User Defined Data Column
	 */
	private String customColumn3;
	
	/**
	 * {@link String} User Defined Data Column
	 */
	private String customColumn4;

	/**
	 * {@link String} Description of batch.
	 */
	private String batchDescription;

	/**
	 * @return the errorCause
	 */
	public String getErrorCause() {
		return errorCause;
	}

	/**
	 * @param errorCause
	 *            the errorCause to set
	 */
	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Variable for executedModules.
	 */
	private String executedModules;

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(final int priority) {
		this.priority = priority;
	}

	public String getBatchIdentifier() {
		return batchIdentifier;
	}

	public void setBatchIdentifier(final String batchIdentifier) {
		this.batchIdentifier = batchIdentifier;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(final String batchName) {
		this.batchName = batchName;
	}

	public String getUploadedOn() {
		return uploadedOn;
	}

	public void setUploadedOn(final String uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	public String getImportedOn() {
		return importedOn;
	}

	public void setImportedOn(final String importedOn) {
		this.importedOn = importedOn;
	}

	public String getNoOfDocuments() {
		return noOfDocuments;
	}

	public void setNoOfDocuments(final String noOfDocuments) {
		this.noOfDocuments = noOfDocuments;
	}

	public String getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(final String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public String getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(final String validationStatus) {
		this.validationStatus = validationStatus;
	}

	public String getNoOfPages() {
		return noOfPages;
	}

	public void setNoOfPages(final String noOfPages) {
		this.noOfPages = noOfPages;
	}

	public String getBatchClassName() {
		return batchClassName;
	}

	public void setBatchClassName(final String batchClassName) {
		this.batchClassName = batchClassName;
	}

	public boolean isRemote() {
		return isRemote;
	}

	public void setRemote(final boolean isRemote) {
		this.isRemote = isRemote;
	}

	// private RemoteBatchInstanceDTO remoteBatchInstanceDTO;
	//
	// public RemoteBatchInstanceDTO getRemoteBatchInstanceDTO() {
	// return remoteBatchInstanceDTO;
	// }
	//
	// public void setRemoteBatchInstanceDTO(RemoteBatchInstanceDTO
	// remoteBatchInstanceDTO) {
	// this.remoteBatchInstanceDTO = remoteBatchInstanceDTO;
	// }

	public String getExecutedModules() {
		return executedModules;
	}

	public void setExecutedModules(String executedModules) {
		this.executedModules = executedModules;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return this.selected;
	}

	/**
	 * @param uncSubFolderPath
	 *            the uncSubFolderPath to set
	 */
	public void setUncSubFolderPath(String uncSubFolderPath) {
		this.uncSubFolderPath = uncSubFolderPath;
	}

	/**
	 * @return the uncSubFolderPath
	 */
	public String getUncSubFolderPath() {
		return uncSubFolderPath;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isNew() {
		return false;
	}

	public String getIdentifier() {
		return batchIdentifier;
	}

	/**
	 * @return {@link String} the lastModified
	 */
	public String getLastModified() {
		return lastModified;
	}

	/**
	 * @param {@link String} lastModified the lastModified to set
	 */
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	
	/**
	 * @return the customColumn1
	 */
	public String getCustomColumn1() {
		return customColumn1;
	}

	
	/**
	 * @param customColumn1 the customColumn1 to set
	 */
	public void setCustomColumn1(final String customColumn1) {
		this.customColumn1 = customColumn1;
	}

	
	/**
	 * @return the customColumn2
	 */
	public String getCustomColumn2() {
		return customColumn2;
	}

	
	/**
	 * @param customColumn2 the customColumn2 to set
	 */
	public void setCustomColumn2(final String customColumn2) {
		this.customColumn2 = customColumn2;
	}

	
	/**
	 * @return the customColumn3
	 */
	public String getCustomColumn3() {
		return customColumn3;
	}

	
	/**
	 * @param customColumn3 the customColumn3 to set
	 */
	public void setCustomColumn3(final String customColumn3) {
		this.customColumn3 = customColumn3;
	}

	
	/**
	 * @return the customColumn4
	 */
	public String getCustomColumn4() {
		return customColumn4;
	}

	
	/**
	 * @param customColumn4 the customColumn4 to set
	 */
	public void setCustomColumn4(final String customColumn4) {
		this.customColumn4 = customColumn4;
	}

	/**
	 * Gets the description of the batch.
	 * 
	 * @return {@link String} batch description.
	 */
	public String getBatchDescription() {
		return batchDescription;
	}

	/**
	 * Sets the description of the batch.
	 * 
	 * @param batchDescription {@link String}
	 */
	public void setBatchDescription(String batchDescription) {
		this.batchDescription = batchDescription;
	}
}
