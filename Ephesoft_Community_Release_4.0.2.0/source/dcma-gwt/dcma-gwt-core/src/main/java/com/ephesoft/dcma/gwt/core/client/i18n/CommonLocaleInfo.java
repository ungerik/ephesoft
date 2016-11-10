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

package com.ephesoft.dcma.gwt.core.client.i18n;

import java.util.ArrayList;
import java.util.List;

public class CommonLocaleInfo extends LocaleInfo {

	private CommonLocaleInfo(String locale) {
		super(locale, "commonConstants", null);
		availableConstants.add(LocaleCommonConstants.header_label_hi);
		availableConstants.add(LocaleCommonConstants.header_label_signOut);
		availableConstants.add(LocaleCommonConstants.header_label_help);
		availableConstants.add(LocaleCommonConstants.msg_mask_wait);
		availableConstants.add(LocaleCommonConstants.exc_consumerAmountIsNotPositive);
		availableConstants.add(LocaleCommonConstants.exc_consumerTypeIsNull);
		availableConstants.add(LocaleCommonConstants.exc_cpu_count_voilation);
		availableConstants.add(LocaleCommonConstants.exc_holderIsNull);
		availableConstants.add(LocaleCommonConstants.exc_invalidSubject);
		availableConstants.add(LocaleCommonConstants.exc_issuedIsNull);
		availableConstants.add(LocaleCommonConstants.exc_issuerIsNull);
		availableConstants.add(LocaleCommonConstants.exc_licenseHasExpired);
		availableConstants.add(LocaleCommonConstants.exc_licenseIsNotYetValid);
		availableConstants.add(LocaleCommonConstants.exc_system_date_voilation);
		availableConstants.add(LocaleCommonConstants.exc_system_mac_address_voilation);
		availableConstants.add(LocaleCommonConstants.title_confirmation_ok);
		availableConstants.add(LocaleCommonConstants.title_confirmation_cancel);
		availableConstants.add(LocaleCommonConstants.dialog_box_title);
		availableConstants.add(LocaleCommonConstants.exc_ephesoft_license_cpu_limit_exhausted);
		availableConstants.add(LocaleCommonConstants.exc_ephesoft_license_server_error);
		availableConstants.add(LocaleCommonConstants.title_go_to_page);
		availableConstants.add(LocaleCommonConstants.title_previous);
		availableConstants.add(LocaleCommonConstants.title_next);
		availableConstants.add(LocaleCommonConstants.title_displaying);
		availableConstants.add(LocaleCommonConstants.title_confirmation_save);
		availableConstants.add(LocaleCommonConstants.title_confirmation_discard);
		availableConstants.add(LocaleCommonConstants.help_url_error_msg);
		availableConstants.add(LocaleCommonConstants.ERROR_TITLE);
		availableConstants.add(LocaleCommonConstants.up_record);
		availableConstants.add(LocaleCommonConstants.down_record);
		availableConstants.add(LocaleCommonConstants.BROWSE);
		availableConstants.add(LocaleCommonConstants.SERVER_DISCONNECTED);
		availableConstants.add(LocaleCommonConstants.BATCH_CLASS_MANAGEMENT_CONSTANT);
		availableConstants.add(LocaleCommonConstants.BATCH_INSTANCE_MANAGEMENT_CONSTANT);
		availableConstants.add(LocaleCommonConstants.WORKFLOW_MANAGEMENT_CONSTANT);
		availableConstants.add(LocaleCommonConstants.FOLDER_MANAGEMENT_CONSTANT);
		availableConstants.add(LocaleCommonConstants.REPORTS_CONSTANT);
		availableConstants.add(LocaleCommonConstants.SYSTEM_CONFIG_MANAGEMENT_CONSTANT);
		availableConstants.add(LocaleCommonConstants.EXC_EPHESOFT_LICENSE_INVALID_LICENSE_SERVER_DOWN);
		availableConstants.add(LocaleCommonConstants.SUCCESS_TITLE);
		availableConstants.add(LocaleCommonConstants.SORT_ON_ID);
		availableConstants.add(LocaleCommonConstants.SORT_ON_DESCRIPTION);
		availableConstants.add(LocaleCommonConstants.CHANGE_THEME);
		availableConstants.add(LocaleCommonConstants.REMOVE);
		availableConstants.add(LocaleCommonConstants.ADD);
		availableConstants.add(LocaleCommonConstants.UP);
		availableConstants.add(LocaleCommonConstants.DOWN);
		// For 3.1 Image Magnification
		availableConstants.add(LocaleCommonConstants.ACTIVATE_MAGNIFICATION_MSG);
		availableConstants.add(LocaleCommonConstants.DEACTIVATE_MAGNIFICATION_MSG);
		availableConstants.add(LocaleCommonConstants.ERROR_IN_GETTING_USERNAME);
	}

	public static CommonLocaleInfo get(String locale) {
		return new CommonLocaleInfo(locale);
	}

	private List<String> availableConstants = new ArrayList<String>(3);

	public boolean isExist(String key) {
		return availableConstants.contains(key);
	}
}
