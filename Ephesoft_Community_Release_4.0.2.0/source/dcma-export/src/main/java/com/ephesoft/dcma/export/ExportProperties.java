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

package com.ephesoft.dcma.export;

import com.ephesoft.dcma.core.common.PluginProperty;

/**
 * Enum to carry the properties used in Copy_Batch_XML plugin.
 * 
 * @author Ephesoft
 * @version 3.0
 *
 */
public enum ExportProperties implements PluginProperty {

	/**
	 * The folder path to which all the exports needs to be copied.
	 */
	EXPORT_FOLDER("batch.export_to_folder"),
	/**
	 * Switch describing if batch has to be exported or not.
	 */
	EXPORT_TO_FOLDER_SWITCH("batch.export_to_folder_switch"),
	/**
	 * The name to be created for the folder in which exported artifacts will be copied.
	 */
	FOLDER_NAME("batch.folder_name"),
	/**
	 * The files that will be exported.
	 */
	FILE_NAME("batch.file_name"),
	/**
	 * The folder from where files need to be picked up.
	 */
	BATCH_XML_EXPORT_FOLDER("batch.batch_xml_export_folder"),
	//property for version 3.1 for adding the type of file to be exported.
	/**
	 * Property for identifying the type of files that will be exported along with batch xml. The properties will have following values: Tiff/PDF/Tiff and PDF.
	 */
	COPY_MULTIPAGE_FILE_TYPE("batch.copy_file_type"),
	/**
	 * Property for multipage export folder path 
	 */
	EXPORT_MULTIPAGE_FOLDER_PATH("batch.multipage_export_folder_path"),
	/**
	 * Property for multipage export file name 
	 */
	EXPORT_MULTIPAGE_FILE_NAME("batch.multipage_export_file_name");
	/**
	 * A string key to extract the property values.
	 */
	private String key;

	ExportProperties(final String key) {
		this.key = key;
	}

	@Override
	public String getPropertyKey() {
		return key;
	}
}
