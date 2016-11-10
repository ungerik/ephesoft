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

package com.ephesoft.dcma.workflows.util;

import java.util.List;

import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchInstance;

/**
 * This class represents the virtual object carrier for details of a batch for restart.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 27, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RestartingBatchDetails {

	/**
	 * {@link String} Batch instance identifier.
	 */
	private String batchInstanceIdentifier;

	/**
	 * {@link BatchInstance} Batch Instance object.
	 */
	private BatchInstance batchInstance;

	/**
	 * {@link List}<{@link BatchClassModule}> Batch class modules in order of execution in worklflow.
	 */
	private List<BatchClassModule> batchClassModulesInOrder;

	/**
	 * {@link String} Name of the module.
	 */
	private String moduleName;

	/**
	 * {@link BatchClassModule} Precursor batch class module.
	 */
	private BatchClassModule prescursorBatchClassModule;

	/**
	 * Gets batch instance identifier.
	 * 
	 * @return {@link String}
	 */
	public String getBatchInstanceIdentifier() {
		return batchInstanceIdentifier;
	}

	/**
	 * Sets batch instance identifier.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 */
	public void setBatchInstanceIdentifier(final String batchInstanceIdentifier) {
		this.batchInstanceIdentifier = batchInstanceIdentifier;
	}

	/**
	 * Gets batch instance.
	 * 
	 * @return {@link BatchInstance}
	 */
	public BatchInstance getBatchInstance() {
		return batchInstance;
	}

	/**
	 * Sets batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	public void setBatchInstance(final BatchInstance batchInstance) {
		this.batchInstance = batchInstance;
	}

	/**
	 * Gets batch class modules in order of execution in worklflow.
	 * 
	 * @return {@link List}<{@link BatchClassModule}>
	 */
	public List<BatchClassModule> getBatchClassModulesInOrder() {
		return batchClassModulesInOrder;
	}

	/**
	 * Sets batch class modules in order of execution in worklflow.
	 * 
	 * @param batchClassModulesInOrder {@link List}<{@link BatchClassModule}>
	 */
	public void setBatchClassModulesInOrder(final List<BatchClassModule> batchClassModulesInOrder) {
		this.batchClassModulesInOrder = batchClassModulesInOrder;
	}

	/**
	 * Gets module name.
	 * 
	 * @return {@link String}
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * Sets module name.
	 * 
	 * @param moduleName {@link String}
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * Gets precursor batch class module.
	 * 
	 * @return {@link BatchClassModule}
	 */
	public BatchClassModule getPrescursorBatchClassModule() {
		return prescursorBatchClassModule;
	}

	/**
	 * Sets precursor batch class module.
	 * 
	 * @param prescursorBatchClassModule {@link BatchClassModule}
	 */
	public void setPrescursorBatchClassModule(final BatchClassModule prescursorBatchClassModule) {
		this.prescursorBatchClassModule = prescursorBatchClassModule;
	}

}
