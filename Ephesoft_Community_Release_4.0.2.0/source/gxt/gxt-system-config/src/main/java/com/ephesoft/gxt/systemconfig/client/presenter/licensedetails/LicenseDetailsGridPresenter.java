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

package com.ephesoft.gxt.systemconfig.client.presenter.licensedetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.LicenseDetailsDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigInlinePresenter;
import com.ephesoft.gxt.systemconfig.client.view.licensedetails.LicenseDetailsGridView;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LicenseDetailsGridPresenter extends SystemConfigInlinePresenter<LicenseDetailsGridView> {

	public LicenseDetailsGridPresenter(SystemConfigController controller, LicenseDetailsGridView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
		if (CollectionUtil.isEmpty(controller.getLicenseDetailsDTOList())) {
			getLicenseDetails();
		} else {
			view.setDataAndLoadGrid(controller.getLicenseDetailsDTOList());
		}
	}

	private void getLicenseDetails() {
		if (CollectionUtil.isEmpty(controller.getLicenseDetailsDTOList())) {
			controller.getRpcService().getLicenseDetails(new AsyncCallback<Map<String, String>>() {

				@Override
				public void onFailure(final Throwable caught) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_GET_LICENSE_DETAILS), DialogIcon.ERROR);
				}

				@Override
				public void onSuccess(final Map<String, String> licenseDetails) {
					if (null != licenseDetails) {
						List<LicenseDetailsDTO> licenseDetailsDTOList = convertLicenseRecordsIntoDto(licenseDetails);
						if (null != licenseDetailsDTOList && !licenseDetailsDTOList.isEmpty()) {
							controller.setLicenseDetailsDTOList(licenseDetailsDTOList);
							view.setDataAndLoadGrid(controller.getLicenseDetailsDTOList());
						}
					} else {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_GET_LICENSE_DETAILS), DialogIcon.ERROR);
					}
				}
			});
		}

	}

	@Override
	public void injectEvents(EventBus eventBus) {

	}

	private List<LicenseDetailsDTO> convertLicenseRecordsIntoDto(Map<String, String> licenseDetails) {
		List<LicenseDetailsDTO> licenseDetailsDTOList = new ArrayList<LicenseDetailsDTO>();
		for (final Entry<String, String> entry : licenseDetails.entrySet()) {
			LicenseDetailsDTO licenseDetailsDTO = new LicenseDetailsDTO();
			licenseDetailsDTO.setPropertyName(entry.getKey());
			if (entry.getValue().equalsIgnoreCase("on") || entry.getValue().equalsIgnoreCase("off")) {
				licenseDetailsDTO.setPropertyValue(entry.getValue().toUpperCase());
			}
			if (SystemConfigConstants.ANNUAL_IMAGE_COUNT_PROPERTY_NAME.equalsIgnoreCase(entry.getKey())
					&& (entry.getValue().equals("-1") || "null".equals(entry.getValue()))) {
				licenseDetailsDTO.setPropertyValue(LocaleDictionary
						.getConstantValue(SystemConfigConstants.ANNUAL_IMAGE_COUNT_PROPERTY_VALUE));
			} else {
				licenseDetailsDTO.setPropertyValue(entry.getValue());
			}
			licenseDetailsDTOList.add(licenseDetailsDTO);
		}
		return licenseDetailsDTOList;
	}

}
