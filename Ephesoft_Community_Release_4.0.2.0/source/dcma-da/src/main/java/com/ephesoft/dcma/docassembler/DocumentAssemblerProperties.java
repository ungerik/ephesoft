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

package com.ephesoft.dcma.docassembler;

import com.ephesoft.dcma.core.common.PluginProperty;


public enum DocumentAssemblerProperties implements PluginProperty {
	
	DA_BARCODE_CONFIDENCE("da.barcode_confidence"), 
	DA_RULE_FP_MP_LP("da.rule_fp_mp_lp"), 
	DA_RULE_FP("da.rule_fp"), 
	DA_RULE_MP("da.rule_mp"), 
	DA_RULE_LP("da.rule_lp"), 
	DA_RULE_FP_LP("da.rule_fp_lp"), 
	DA_RULE_FP_MP("da.rule_fp_mp"), 
	DA_RULE_MP_LP("da.rule_mp_lp"), 
	DA_FACTORY_CLASS("da.factory_classification"),
	DA_MERGE_UNKNOWN_DOCUMENT_SWITCH("da.merge_unknown_document_switch"),
	/**
	 * int constant for middle page confidence threshold.
	 */
	DA_MIDDLE_PAGE_CONFIDENCE_THRESHOLD("da.middle_page_confidence_threshold"),

	/**
	 * int constant for last page confidence threshold.
	 */
	DA_LAST_PAGE_CONFIDENCE_THRESHOLD("da.last_page_confidence_threshold"),

	/**
	 * int constant for first page confidence threshold.
	 */
	DA_FIRST_PAGE_CONFIDENCE_THRESHOLD("da.first_page_confidence_threshold"),
	/**
	 * String constant for da predefined document type.
	 */
	DA_PREDEFINED_DOCUMENT_TYPE("da.predefined_document_type"),
	/**
	 * int constant for da predefined document confidence threshold.
	 */
	DA_PREDEFINED_DOCUMENT_CONFIDENCE_THRESHOLD("da.predefined_document_confidence_threshold"),
	/**
	 * String constant for advanced algorithm.
	 */
	DA_ADVANCED_ALGORITHM("da.da_advanced_algorithm"),
	/**
	 * String constant for unknown predefined document type.
	 */
	DA_UNKNOWN_PREDEFINED_DOCUMENT_TYPE("da.unknown_predefined_document_type"),
	/**
	 * String constant for unknown predefined document type change switch.
	 */
	DA_SWITCH_UNKNOWN_PREDEFINED_DOCUMENT_TYPE("da.switch_unknown_predefined_document_type"),
	
	/**
	 * Represents the property for DA delete document first page switch.
	 */
	DA_DELETE_DOCUMENT_FIRST_PAGE_SWITCH("da.delete_document_first_page_switch"),
	/**
	 * Represents the property for DA regex classification document type.
	 */
	DA_DOCUMENT_TYPE_REGEX_CLASSIFICATION("da.document_type_regex_classification"),
	/**
	 * Represents the property for DA regex classification pattern.
	 */
	DA_REGEX_CLASSIFICATION_PATTERN("da.regex_classification_pattern"),
	
	/**
	 * String constant for switch regex classification.
	 */
	DA_SWITCH_REGEX_CLASSIFICATION("da.switch_regex_classification");
	
	String key;
	
	DocumentAssemblerProperties(String key) {
		this.key = key;
	}

	@Override
	public String getPropertyKey() {
		return key;
	}
}
