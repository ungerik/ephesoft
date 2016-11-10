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
 * Class that stores Uploaded Files information on client side.
 * 
 * @author Ephesoft
 */

public class UploadBatchDTO implements IsSerializable, Selectable {

	/**
	 * ID representing each file.
	 */
	private String id;

	/**
	 * Name of the file.
	 */
	private String fileName;

	/**
	 * Type of the file.
	 */
	private String type;

	/**
	 * Size of the file.
	 */
	private int size;

	/**
	 * Upload progress of a file.
	 */
	private double progress;

	/**
	 * Flag to represent file selection.
	 */
	private boolean selected;

	/**
	 * Constructor.
	 */
	public UploadBatchDTO() {
	}

	/**
	 * Gets the ID of a file.
	 * 
	 * @return the id {@link String}
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the ID of a file.
	 * 
	 * @param id {@link String} the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Sets the flag as true to depict file selection.
	 * 
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Gets the file name.
	 * 
	 * @return file name {@link String}
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name.
	 * 
	 * @param filename {@link String}
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets the file type.
	 * 
	 * @return file type {@link String}
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the file type.
	 * 
	 * @param file type {@link String}
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the file size.
	 * 
	 * @return file size {@link String}
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * Sets the file size.
	 * 
	 * @param file size {@link String}
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Gets the uploaded progress of a file.
	 * 
	 * @return progress.
	 */
	public double isProgress() {
		return progress;
	}

	/**
	 * Sets the uploaded progress of a file.
	 * 
	 * @param progress.
	 */
	public void setProgress(double progress) {
		this.progress = progress;
	}
	@Override
	public boolean isNew() {
		return false;
	}

}
