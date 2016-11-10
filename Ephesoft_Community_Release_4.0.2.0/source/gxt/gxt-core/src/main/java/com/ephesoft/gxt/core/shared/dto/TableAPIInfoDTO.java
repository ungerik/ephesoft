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
 * Represents the DTO for the table API extractrion technique info used for extracting the data for tables.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 09-Jan-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see IsSerializable
 * @see TableInfoDTO
 */
public class TableAPIInfoDTO implements IsSerializable {

	/**
	 * tableInfoDTO {@link TableInfoDTO} holds the object for the table info DTO.
	 */
	private TableInfoDTO tableInfoDTO;

	/**
	 * tableAPI {@link String} holds the data for table extraction API defined for a table.
	 */
	private String tableAPI;

	/**
	 * identifier {@link String} holds the id for the table API object.
	 */
	private String identifier;

	/**
	 * {@code isDeleted} holds the information whether the table API DTO object is deleted or not.If table API DTO object is deleted
	 * then it is set to {@code true}, otherwise it is set to {@code false}.
	 */
	private boolean isDeleted;

	/**
	 * {@code isNew} holds the information whether the table API DTO object is new or not.If table API DTO object is new then it is set
	 * to {@code true}, otherwise it is set to {@code false}.
	 */
	private boolean isNew;

	/**
	 * @return the tableInfoDTO
	 */
	public TableInfoDTO getTableInfoDTO() {
		return tableInfoDTO;
	}

	/**
	 * @param tableInfoDTO the tableInfoDTO to set
	 */
	public void setTableInfoDTO(final TableInfoDTO tableInfoDTO) {
		this.tableInfoDTO = tableInfoDTO;
	}

	/**
	 * @return the tableAPI
	 */
	public String getTableAPI() {
		return tableAPI;
	}

	/**
	 * @param tableAPI the tableAPI to set
	 */
	public void setTableAPI(final String tableAPI) {
		this.tableAPI = tableAPI;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the isDeleted
	 */
	public boolean isDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setDeleted(final boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the isNew
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * @param isNew the isNew to set
	 */
	public void setNew(final boolean isNew) {
		this.isNew = isNew;
	}

}
