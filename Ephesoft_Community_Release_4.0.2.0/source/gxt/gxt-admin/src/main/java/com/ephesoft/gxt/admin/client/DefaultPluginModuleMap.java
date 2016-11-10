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

package com.ephesoft.gxt.admin.client;

/**
 * Maps the plugin name with a default module associated to the plugin. Each plugin of the workflow is assigned to a default module.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Feb 25, 2014 <br/>
 * @version $LastChangedDate:$<br/>
 *          $LastChangedRevision:$<br/>
 */
public enum DefaultPluginModuleMap {

	BARCODE_READER("BARCODE_READER", "Page Process"), CLEANUP("CLEANUP", "Export"), DOCUMENT_ASSEMBLER("DOCUMENT_ASSEMBLER",
			"Document Assembler"), SCRIPTING_PLUGIN("SCRIPTING_PLUGIN", "Page Process"), SEARCH_CLASSIFICATION(
			"SEARCH_CLASSIFICATION", "Page Process"), OCROPUS("OCROPUS", "Page Process"), KEY_VALUE_EXTRACTION("KEY_VALUE_EXTRACTION",
			"Extraction"), TESSERACT_HOCR("TESSERACT_HOCR", "Page Process"), KV_PAGE_PROCESS("KV_PAGE_PROCESS", "Page Process"),
	AUTOMATED_REGEX_VALIDATION("AUTOMATED_REGEX_VALIDATION", "Automated Validation"), COPY_BATCH_XML("COPY_BATCH_XML", "Extraction"),
	HTML_TO_XML("HTML_TO_XML", "Page Process"), CLASSIFY_IMAGES("CLASSIFY_IMAGES", "Page Process"), CREATE_DISPLAY_IMAGE(
			"CREATE_DISPLAY_IMAGE", "Page Process"), CREATE_OCR_INPUT("CREATE_OCR_INPUT", "Page Process"), CREATE_COMPARE_THUMBNAILS(
			"CREATE_COMPARE_THUMBNAILS", "Page Process"), CREATEMULTIPAGE_FILES("CREATEMULTIPAGE_FILES", "Export"),
	IMPORT_BATCH_FOLDER("IMPORT_BATCH_FOLDER", "Folder Import"), IMPORT_MULTIPAGE_FILES("IMPORT_MULTIPAGE_FILES", "Folder Import"),
	REVIEW_DOCUMENT("REVIEW_DOCUMENT", "Review Document"), TABLE_EXTRACTION("TABLE_EXTRACTION", "Extraction"), VALIDATE_DOCUMENT(
			"VALIDATE_DOCUMENT", "Extraction"), BARCODE_EXTRACTION("BARCODE_EXTRACTION", "Extraction"), REGULAR_REGEX_EXTRACTION(
			"REGULAR_REGEX_EXTRACTION", "Extraction"), TABBED_PDF("TABBED_PDF", "Extraction"), CSV_FILE_CREATION_PLUGIN(
			"CSV_FILE_CREATION_PLUGIN", "Extraction"), KEY_VALUE_LEARNING_PLUGIN("KEY_VALUE_LEARNING_PLUGIN", "Page Process"),
	EXTRACTION_SCRIPTING_PLUGIN("EXTRACTION_SCRIPTING_PLUGIN", "Extraction");

	/**
	 * {@link String} name of the plugin.
	 */
	private final String pluginName;

	/**
	 * {@link String} Default module name to which the plugin belongs.
	 */
	private final String defaultModuleName;

	/**
	 * Initializes with the pluginName and defaultModuleName.
	 * 
	 * @param pluginName {@link String} name of the plugin.
	 * @param defaultModuleName {@link String} moduleName to which plugin is mapped by default.
	 */
	private DefaultPluginModuleMap(final String pluginName, final String defaultModuleName) {
		this.defaultModuleName = defaultModuleName;
		this.pluginName = pluginName;
	}

	/**
	 * Returns the default module name assigned to the plugin.
	 * 
	 * @param pluginName {@link String} plugin Name whose default name is to be mapped.
	 * @return {@link String} Default module name corresponding to the plugin Name.
	 */
	public static String getModuleNameForPlugin(final String pluginName) {
		String moduleName = null;
		final DefaultPluginModuleMap[] pluginConstants = DefaultPluginModuleMap.values();

		for (final DefaultPluginModuleMap pluginProperty : pluginConstants) {
			if (pluginProperty.pluginName.equalsIgnoreCase(pluginName)) {
				moduleName = pluginProperty.defaultModuleName;
				break;
			}
		}
		return moduleName;
	}

}
