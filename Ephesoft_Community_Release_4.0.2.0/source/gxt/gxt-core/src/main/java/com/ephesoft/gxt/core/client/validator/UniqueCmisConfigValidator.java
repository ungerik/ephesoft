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

package com.ephesoft.gxt.core.client.validator;

import java.util.List;

import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.shared.dto.CmisConfigurationDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.sencha.gxt.core.client.ValueProvider;

/**
 * This Validator is used to validate uniqueness of cmis configuration.
 * 
 * @author Ephesoft
 * @version 1.0
 * @param <T> the generic type
 * @see com.ephesoft.gxt.core.client.validator.UniqueCmisConfigValidator
 */
public class UniqueCmisConfigValidator<T extends CmisConfigurationDTO> implements Validator<CmisConfigurationDTO> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.validator.Validator#validate(java.util.List, java.lang.Object,
	 * com.sencha.gxt.core.client.ValueProvider)
	 */
	@Override
	public boolean validate(List<CmisConfigurationDTO> listOfModels, CmisConfigurationDTO model,
			ValueProvider<CmisConfigurationDTO, ?> valueProvider) {
		boolean valid = true;
		if (null != listOfModels && !listOfModels.isEmpty() && null != model && null != valueProvider) {
			if (isCmisConfigDuplicate(listOfModels, model)) {
				valid = false;
			}
		} else {
			valid = false;
		}
		return valid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.validator.Validator#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {
		return LocaleDictionary.getConstantValue(LocaleCommonConstants.TOOLTIP_UNIQUE_CMIS_CONFIGURATION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.validator.Validator#getSeverity()
	 */
	@Override
	public Severity getSeverity() {
		return Severity.MEDIUM;
	}

	/**
	 * Checks if is cmis config duplicate.
	 * 
	 * @param cmisConfigList the cmis config list
	 * @param model the model
	 * @return true, if is cmis config duplicate
	 */
	private boolean isCmisConfigDuplicate(List<CmisConfigurationDTO> cmisConfigList, CmisConfigurationDTO model) {
		boolean duplicateUsrFlg = false;
		if (!CollectionUtil.isEmpty(cmisConfigList)) {
			CmisConfigurationDTO cmisConfigDTO = null;
			if (model != null) {
				for (int i = 0; i < cmisConfigList.size(); i++) {
					cmisConfigDTO = cmisConfigList.get(i);
					if (model != cmisConfigDTO
							&& ((model.getUserName() == null && cmisConfigDTO.getUserName() == null) || (model.getUserName() != null && model.getUserName().trim().equals(
									cmisConfigDTO.getUserName())))
							&& ((model.getServerURL() == null && cmisConfigDTO.getServerURL() == null) || (model.getServerURL() != null && model.getServerURL().trim().equals(
									cmisConfigDTO.getServerURL())))
							&& ((model.getRepositoryID() == null && cmisConfigDTO.getRepositoryID() == null) || (model
									.getRepositoryID() != null && cmisConfigDTO.getRepositoryID() != null && model.getRepositoryID().trim()
									.equals(cmisConfigDTO.getRepositoryID())))
							&& ((model.getFolderName() == null && cmisConfigDTO.getFolderName() == null) || (model.getFolderName() != null && model.getFolderName().trim()
									.equals(cmisConfigDTO.getFolderName())))
							&& ((model.getCmisProperty() == null && cmisConfigDTO.getCmisProperty() == null) || (model.getCmisProperty() != null && model
									.getCmisProperty().trim().equals(cmisConfigDTO.getCmisProperty())))
							&& ((model.getValue() == null && cmisConfigDTO.getValue() == null) || (model.getValue() != null && model.getValue().trim().equals(
									cmisConfigDTO.getValue())))
							&& ((model.getValueToUpdate() == null && cmisConfigDTO.getValueToUpdate() == null) || (model.getValueToUpdate() != null && model
									.getValueToUpdate().trim().equals(cmisConfigDTO.getValueToUpdate())))) {
						duplicateUsrFlg = true;
						break;
					}
				}
			}
		}
		return duplicateUsrFlg;
	}

}
