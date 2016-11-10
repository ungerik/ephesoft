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

package com.ephesoft.gxt.core.client.i18n;

import java.util.ArrayList;
import java.util.List;

/**
 * CommonLocaleInfo holds the list of all the constant and messages which are used all over screens.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see LocaleInfo
 * 
 */
public class CommonLocaleInfo extends LocaleInfo {

	private CommonLocaleInfo(String locale) {
		super(locale, "commonConstants", null);
		// example to add a key for constant text with its locale value in locale.js.
		availableConstants.add(LocaleCommonConstants.header_label_hi);
		availableConstants.add(LocaleCommonConstants.SUCCESS_TITLE);
		availableConstants.add(LocaleCommonConstants.ERROR_TITLE);
		availableConstants.add(LocaleCommonConstants.WORKFLOW_MANAGEMENT_CONSTANT);
		availableConstants.add(CoreCommonConstants.ERROR_FETCHING_DATA);
		availableConstants.add(LocaleCommonConstants.ACTIVATE_MAGNIFICATION_MSG);
		availableConstants.add(LocaleCommonConstants.DEACTIVATE_MAGNIFICATION_MSG);
		availableConstants.add(LocaleCommonConstants.ERROR_IN_GETTING_USERNAME);
		availableConstants.add(LocaleCommonConstants.SORT_ON_ID);
		availableConstants.add(LocaleCommonConstants.SORT_ON_DESCRIPTION);
		availableConstants.add(LocaleCommonConstants.exc_ephesoft_license_server_error);
		availableConstants.add(LocaleCommonConstants.exc_ephesoft_license_cpu_limit_exhausted);
		availableConstants.add(CoreCommonConstants.UPLOAD_BATCH_GRID_FILE_NAME);
		availableConstants.add(CoreCommonConstants.UPLOAD_BATCH_GRID_PROGRESS);
		availableConstants.add(CoreCommonConstants.UPLOAD_BATCH_GRID_FILE_TYPE);
		availableConstants.add(CoreCommonConstants.UPLOAD_BATCH_GRID_FILE_SIZE);

		availableConstants.add(CoreCommonConstants.LABEL_TABLE_COLUMN_PRIORITY);
		availableConstants.add(CoreCommonConstants.LABEL_TABLE_COLUMN_BATCHID);
		availableConstants.add(CoreCommonConstants.LABEL_TABLE_COLUMN_BATCHCLASSNAME);
		availableConstants.add(CoreCommonConstants.LABEL_TABLE_COLUMN_BATCHNAME);
		availableConstants.add(CoreCommonConstants.LABEL_TABLE_COLUMN_BATCHIMPORTEDON);
		availableConstants.add(CoreCommonConstants.LABEL_TABLE_COLUMN_BATCHUPDATEDON);
		availableConstants.add(CoreCommonConstants.LABEL_TABLE_COLUMN_BATCHSTATUS);

		availableConstants.add(CoreCommonConstants.PLEASE_WAIT);

		availableConstants.add(LocaleCommonConstants.TOOLTIP_INCORRECT_RULE);
		availableConstants.add(LocaleCommonConstants.TOOLTIP_PORT_NUMBER_RANGE);
		availableConstants.add(LocaleCommonConstants.TOOLTIP_REGEX_NOT_VALID);
		availableConstants.add(LocaleCommonConstants.TOOLTIP_TABLE_EXTRACTION_API);
		availableConstants.add(LocaleCommonConstants.TOOLTIP_UNIQUE_CMIS_CONFIGURATION);
		availableConstants.add(LocaleCommonConstants.TOOLTIP_UNIQUE_VALUE);
		availableConstants.add(LocaleCommonConstants.TOOLTIP_VALUE_NONNEGATIVE);
		availableConstants.add(LocaleCommonConstants.TOOLTIP_VALUE_NOT_EMPTY);
		availableConstants.add(CoreCommonConstants.NO_RECORDS_FOUND);
		availableConstants.add(CoreCommonConstants.LABEL_COLUMN_COORDINATES);
		availableConstants.add(CoreCommonConstants.LABEL_COLUMN_HEADER);
		availableConstants.add(CoreCommonConstants.LABEL_REGEX_EXTRACTION);
		availableConstants.add(CoreCommonConstants.VALIDATE_GRID_ERR_MSG);
		availableConstants.add(CoreCommonConstants.FM_NAME_COLUMN_HEADER);
		availableConstants.add(CoreCommonConstants.FM_SELECT_COLUMN_HEADER);
		availableConstants.add(CoreCommonConstants.FM_MODIFIED_COLUMN_HEADER);
		availableConstants.add(CoreCommonConstants.FM_ICON_COLUMN_HEADER);
		availableConstants.add(CoreCommonConstants.FM_SIZE_COLUMN_HEADER);
		availableConstants.add(CoreCommonConstants.FM_FILE_TYPE_COLUMN_HEADER);
		availableConstants.add(CoreCommonConstants.FM_PROGRESS_COLUMN_HEADER);
		availableConstants.add(LocaleCommonConstants.INFO_TITLE);
		availableConstants.add(CoreCommonConstants.LICENSE_GRID_PROPERTY_NAME_HEADER);
		availableConstants.add(CoreCommonConstants.LICENSE_GRID_PROPERTY_VALUE_HEADER);
		availableConstants.add(CoreCommonConstants.PLUGIN_GRID_DEPENDENCY_HEADER);
		availableConstants.add(CoreCommonConstants.PLUGIN_GRID_DEPENDENCY_TYPE_HEADER);
		availableConstants.add(CoreCommonConstants.CM_CONNECTION_NAME_HEADER);
		availableConstants.add(CoreCommonConstants.CM_CONNECTION_DESCRIPTION_HEADER);
		availableConstants.add(CoreCommonConstants.REGEX_PATTERN_COLUMN_HEADER);
		availableConstants.add(CoreCommonConstants.REGEX_PATTERN_DESCRIPTION_COLUMN_HEADER);
		availableConstants.add(CoreCommonConstants.REGEX_GROUP_NAME_HEADER);

		availableConstants.add(CoreCommonConstants.REGEX_VALIDATION_GRID_PATTERN);
		availableConstants.add(CoreCommonConstants.REGEX_PATTERN_BUTTON_GRID);
		availableConstants.add(CoreCommonConstants.REGEX_PATTERN_BUTTON_GRID_DECRIPTION_HEADER);
		availableConstants.add(CoreCommonConstants.REGEX_GROUP_BUTTON_GRID);

		availableConstants.add(CoreCommonConstants.COLUMN_CONFIG_IDENTIFIER);
		availableConstants.add(CoreCommonConstants.COLUMN_CONFIG_NAME);
		availableConstants.add(CoreCommonConstants.COLUMN_CONFIG_DESCRIPTION);
		availableConstants.add(CoreCommonConstants.COLUMN_CONFIG_PRIORITY);
		availableConstants.add(CoreCommonConstants.COLUMN_CONFIG_HIDDEN);
		availableConstants.add(CoreCommonConstants.COLUMN_CONFIG_FIELD_NAME);
		availableConstants.add(CoreCommonConstants.COLUMN_CONFIG_KEY);
		availableConstants.add(CoreCommonConstants.COLUMN_CONFIG_VALUE);
		availableConstants.add(CoreCommonConstants.COLUMN_CONFIG_PATTERN);

		availableConstants.add(CoreCommonConstants.ADVANCE_KV_CONFIDENCE);
		availableConstants.add(CoreCommonConstants.ADVANCE_KV_KEY_COORDINATES);
		availableConstants.add(CoreCommonConstants.ADVANCE_KV_VALUE_COORDINATES);
		availableConstants.add(CoreCommonConstants.ADVANCE_KV_COLORCODE);

		availableConstants.add(CoreCommonConstants.BCC_UNC_PATH);
		availableConstants.add(CoreCommonConstants.BCC_VERSION);
		availableConstants.add(CoreCommonConstants.BCC_CURRENT_USER);
		availableConstants.add(CoreCommonConstants.BCC_ENCRYPTION);
		availableConstants.add(CoreCommonConstants.BCC_ROLES);

		availableConstants.add(CoreCommonConstants.DT_MINIMUM_CONFIDENCE_THRESHOLD);
		availableConstants.add(CoreCommonConstants.DT_FIRST_PAGE);
		availableConstants.add(CoreCommonConstants.DT_SECOND_PAGE);
		availableConstants.add(CoreCommonConstants.DT_THIRD_PAGE);
		availableConstants.add(CoreCommonConstants.DT_LASTGREATERTHAN3_PAGE);

		availableConstants.add(CoreCommonConstants.INDEX_FIELD_DATA_TYPE);
		availableConstants.add(CoreCommonConstants.INDEX_FIELD_OCR_THRESHOLD);
		availableConstants.add(CoreCommonConstants.INDEX_FIELD_TYPE);
		availableConstants.add(CoreCommonConstants.INDEX_FIELD_CATEGORY);
		availableConstants.add(CoreCommonConstants.INDEX_FIELD_ORDER);
		availableConstants.add(CoreCommonConstants.INDEX_FIELD_SAMPLE_VALUE);
		availableConstants.add(CoreCommonConstants.INDEX_FIELD_OPTION_VALUES_LIST);
		availableConstants.add(CoreCommonConstants.INDEX_FIELD_BARCODE);

		availableConstants.add(CoreCommonConstants.DB_EXPORT_DB_TABLE_NAME);
		availableConstants.add(CoreCommonConstants.DB_EXPORT_DB_COLUMN_NAME);

		availableConstants.add(CoreCommonConstants.KV_NO_OF_WORDS);
		availableConstants.add(CoreCommonConstants.KV_FUZINESS);
		availableConstants.add(CoreCommonConstants.KV_LOCATION);
		availableConstants.add(CoreCommonConstants.KV_WEIGHT);

		availableConstants.add(CoreCommonConstants.FUZZY_DB_TABLE_COLUMN_NAME);
		availableConstants.add(CoreCommonConstants.FUZZY_DB_IS_SEARCHABLE);

		availableConstants.add(CoreCommonConstants.BIM_COLUMN_CONFIG_BATCH_NAME);
		availableConstants.add(CoreCommonConstants.BIM_COLUMN_CONFIG_STATUS);
		availableConstants.add(CoreCommonConstants.BIM_COLUMN_CONFIG_PRIORITY);
		availableConstants.add(CoreCommonConstants.BIM_COLUMN_CONFIG_BATCH_CLASS_NAME);
		availableConstants.add(CoreCommonConstants.BIM_COLUMN_CONFIG_CURRENT_USER);
		availableConstants.add(CoreCommonConstants.BIM_COLUMN_CONFIG_IMPORTED_ON);
		availableConstants.add(CoreCommonConstants.BIM_COLUMN_CONFIG_LAST_MODIFIED);
		availableConstants.add(LocaleCommonConstants.CONFIRMATION);
		availableConstants.add(LocaleCommonConstants.SELECT_FILES);
		availableConstants.add(LocaleCommonConstants.OR_LABEL);
		availableConstants.add(LocaleCommonConstants.CLICK_HERE_TO_UPLOAD);
		availableConstants.add(LocaleCommonConstants.RESET);
		availableConstants.add(LocaleCommonConstants.SHOWING);
		availableConstants.add(LocaleCommonConstants.ACCEPT_VALUES_BETWEEN_10_TO_50);
		availableConstants.add(LocaleCommonConstants.RECORDS_PER_PAGE);
		availableConstants.add(LocaleCommonConstants.DISPLAYING_ZERO_OF_ZERO);
		availableConstants.add(LocaleCommonConstants.LOG_OUT);
		availableConstants.add(LocaleCommonConstants.MOVE_RIGHT);
		availableConstants.add(LocaleCommonConstants.MOVE_LEFT);
		availableConstants.add(LocaleCommonConstants.DRAG_AND_DROP_FILES_HERE);
		availableConstants.add(LocaleCommonConstants.DISPLAYING);
		availableConstants.add(LocaleCommonConstants.PAGE);

		availableConstants.add(LocaleCommonConstants.TERABYTE);
		availableConstants.add(LocaleCommonConstants.GIGABYTE);
		availableConstants.add(LocaleCommonConstants.MEGABYTE);
		availableConstants.add(LocaleCommonConstants.KILOBYTE);
		availableConstants.add(LocaleCommonConstants.BYTE);

		availableConstants.add(CoreCommonConstants.CLASSIFICATION_TYPE);
		availableConstants.add(CoreCommonConstants.DOCUMENT_TYPE);
		availableConstants.add(CoreCommonConstants.DOCUMENT_ID);
		availableConstants.add(CoreCommonConstants.DOCUMENT_CONFIDENCE);
		availableConstants.add(CoreCommonConstants.PAGE_NAME);
		availableConstants.add(CoreCommonConstants.PAGE_ID);
		availableConstants.add(CoreCommonConstants.PAGE_CLASSIFICATION_VALUE);
		availableConstants.add(CoreCommonConstants.PAGE_CONFIDENCE);
		availableConstants.add(CoreCommonConstants.CLASSIFICATION_SAMPLE);

		availableConstants.add(CoreCommonConstants.SELECT_LABEL);
		availableConstants.add(CoreCommonConstants.FILE_NAME);
		availableConstants.add(CoreCommonConstants.PAGE_NUMBER);
		availableConstants.add(CoreCommonConstants.PAGE_TYPE);
		availableConstants.add(CoreCommonConstants.IMAGE_CLASSIFICATION);
		availableConstants.add(CoreCommonConstants.LUCENE_LEARNING);

		availableConstants.add(CoreCommonConstants.YES);
		availableConstants.add(CoreCommonConstants.OK);
		availableConstants.add(CoreCommonConstants.NO);
		availableConstants.add(CoreCommonConstants.CLOSE);
		availableConstants.add(CoreCommonConstants.CANCEL);
		availableConstants.add(LocaleCommonConstants.DOC_LIMIT_MESSAGE);

	}

	public static CommonLocaleInfo get(String locale) {
		return new CommonLocaleInfo(locale);
	}

	/**
	 * availableConstants {@link List <String>} the list of all the constants which are available over all screens.
	 */
	private List<String> availableConstants = new ArrayList<String>(3);

	/**
	 * Checks if the key passed to it is contained in the declared available constants.
	 * 
	 * @param key {@link String} the key for the text to be searched.
	 * @return boolean true if the key is present in declared common locale, false otherwise.
	 */
	public boolean isExist(String key) {
		return availableConstants.contains(key);
	}
}
