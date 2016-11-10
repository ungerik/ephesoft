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

/**
 * 
 */
package com.ephesoft.gxt.admin.shared.constant;

/**
 * This class is contains the set of constants that will be shared between Client side and server side code. This class cannot be
 * extended or instantiated. The constants can be accessed directly.
 * 
 * <b>created on</b> 29-Jul-2013 <br/>
 * 
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public final class AdminSharedConstants {

	/**
	 * Private constructor to avoid instantiation of the class.
	 */
	private AdminSharedConstants() {
		// Do-Nothing
	}

	/**
	 * FILE_PATH, a {@link String} constant to represent the keyword for File path of the imported batch class on the local system.
	 */
	public static final String FILE_PATH = "filePath:";

	/**
	 * WORKFLOW_EXIST_IN_BATCH_CLASS, a {@link String} constant to represent keyword defining whether any other workflow exists with
	 * the same name in the system.
	 */
	public static final String WORKFLOW_EXIST_IN_BATCH_CLASS = "workflowExistInBatchClass:";

	/**
	 * WORKFLOW_EQUAL, a {@link String} constant to represent the keyword to define whether the workflow being imported is same as some
	 * other workflow in the system.
	 */
	public static final String WORKFLOW_EQUAL = "workflowEqual:";

	/**
	 * WORKFLOW_DEPLOYED, a {@link String} constant to represent the keyword defining whether any workflow with same name is already
	 * deployed or not.
	 */
	public static final String WORKFLOW_DEPLOYED = "workflowDeployed:";

	/**
	 * WORK_FLOW_NAME, a {@link String} constant to represent the keyword defining the name of the workflow extracted from the imported
	 * batch class.
	 */
	public static final String WORK_FLOW_NAME = "workFlowName:";

	/**
	 * WORK_FLOW_PRIORITY, a {@link String} constant to represent the keyword defining the priority of the workflow being imported.
	 */
	public static final String WORK_FLOW_PRIORITY = "workFlowPriority:";

	/**
	 * WORK_FLOW_DESC, a {@link String} constant to represent the keyword defining the description of the workflow being imported.
	 */
	public static final String WORK_FLOW_DESC = "workFlowDesc:";
	
	/**
	 * SCANNER_BIT_DEPTH_KEY, a {@link String} constant to represent the keyword defining the name for bit depth.
	 */
	public static final String SCANNER_BIT_DEPTH = "bit_depth";

	/**
	 * SCANNER_PAGE_MODE_KEY, a {@link String} constant to represent the keyword defining the name for blank page mode.
	 */
	public static final String SCANNER_PAGE_MODE = "blank_page_mode";

	/**
	 * SCANNER_PAGE_THRESHOLD_KEY, a {@link String} constant to represent the keyword defining the name for blank page threshold.
	 */
	public static final String SCANNER_PAGE_THRESHOLD = "blank_page_threshold";

	/**
	 * SCANNER_PAGE_CACHE_COUNT_KEY, a {@link String} constant to represent the keyword defining the name for page cache clear count.
	 */
	public static final String SCANNER_PAGE_CACHE_COUNT = "pages_cache_clear_coun";

	/**
	 * SCANNER_CURRENT_PIXEL_TYPE_KEY, a {@link String} constant to represent the keyword defining the key for current pixel type
	 * sample values.
	 */
	public static final String SCANNER_CURRENT_PIXEL_TYPE_KEY = "current_pixel_type";

	/**
	 * SCANNER_DPI_KEY, a {@link String} constant to represent the keyword defining the key for dpi sample values.
	 */
	public static final String SCANNER_DPI_KEY = "dpi";

	/**
	 * SCANNER_COLOR_KEY, a {@link String} constant to represent the keyword defining the key for color sample values.
	 */
	public static final String SCANNER_COLOR_KEY = "color";

	/**
	 * SCANNER_PAPER_SIZE_KEY, a {@link String} constant to represent the keyword defining the key for paper size sample values.
	 */
	public static final String SCANNER_PAPER_SIZE_KEY = "paper_size";

	/**
	 * SCANNER_BACK_PAGE_ROTATION_KEY, a {@link String} constant to represent the keyword defining the key for back page rotation
	 * multiple sample values.
	 */
	public static final String SCANNER_BACK_PAGE_ROTATION_KEY = "back_page_rotation_multiple";

	/**
	 * SCANNER_FRONT_PAGE_ROTATION_KEY, a {@link String} constant to represent the keyword defining the key for front page rotation
	 * multiple sample values.
	 */
	public static final String SCANNER_FRONT_PAGE_ROTATION_KEY = "front_page_rotation_multiple";
	
	/**
	 * PAGE_PROCESSING_SCRIPTING_PLUGIN_NAME, a {@link String} constant to represent the page processing scripting plugin name.
	 */
	public static final String PAGE_PROCESSING_SCRIPTING_PLUGIN_NAME = "ScriptPageProcessing";
	/**
	 * DOCUMENT_ASSEMBLER_SCRIPTING_PLUGIN_NAME, a {@link String} constant to represent the document assembler scripting plugin name.
	 */
	public static final String DOCUMENT_ASSEMBLER_SCRIPTING_PLUGIN_NAME = "ScriptDocumentAssembler";
	/**
	 * DOCUMENT_ASSEMBLER_SCRIPTING_PLUGIN_NAME, a {@link String} constant to represent the extraction scripting plugin name.
	 */
	public static final String EXTRACTION_SCRIPTING_PLUGIN_NAME = "ScriptExtraction";
	
}
