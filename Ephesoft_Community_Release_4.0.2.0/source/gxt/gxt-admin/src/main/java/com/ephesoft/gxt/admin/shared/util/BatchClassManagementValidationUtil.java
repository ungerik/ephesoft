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

package com.ephesoft.gxt.admin.shared.util;

import java.util.List;

import com.ephesoft.gxt.core.shared.dto.CmisConfigurationDTO;
import com.ephesoft.gxt.core.shared.dto.WebScannerConfigurationDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.core.shared.util.ValidationUtil;
import com.google.gwt.regexp.shared.RegExp;

/**
 * Utility class required during validation.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jan 13, 2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public final class BatchClassManagementValidationUtil {

	// Suppresses default constructor, ensuring non-instantiability.
	/**
	 * Instantiates a new batch class management validation util.
	 */
	private BatchClassManagementValidationUtil() {
	}

	/**
	 * Method to check whether cmis configuration mandatory fields empty.
	 * 
	 * @param cmisDTO the cmis dto {@link CmisConfigurationDTO}.
	 * @return true, if is cmis configuration mandatory fields empty.
	 */
	public static boolean isCmisConfigMandatoryFieldsEmpty(CmisConfigurationDTO cmisDTO) {
		return (StringUtil.isNullOrEmpty(cmisDTO.getServerURL()) || StringUtil.isNullOrEmpty(cmisDTO.getUserName())
				|| StringUtil.isNullOrEmpty(cmisDTO.getPassword()) || StringUtil.isNullOrEmpty(cmisDTO.getFolderName())
				|| StringUtil.isNullOrEmpty(cmisDTO.getCmisProperty()) || StringUtil.isNullOrEmpty(cmisDTO.getValue()) || StringUtil
					.isNullOrEmpty(cmisDTO.getValueToUpdate()));
	}

	/**
	 * Method to check whether cmis configuration duplicate.
	 * 
	 * @param cmisConfigList the cmis configuration list {@link List<{@link CmisConfigurationDTO}>}.
	 * @return true, if cmis configuration already exist.
	 */
	public static boolean isCmisConfigDuplicate(List<CmisConfigurationDTO> cmisConfigList) {
		boolean duplicateUsrFlg = false;
		if (!CollectionUtil.isEmpty(cmisConfigList)) {
			CmisConfigurationDTO cmisConfigurationDTO = null;
			CmisConfigurationDTO cmisConfigDTO = null;
			for (int i = 0; i < cmisConfigList.size(); i++) {
				cmisConfigurationDTO = cmisConfigList.get(i);
				for (int j = i + 1; j < cmisConfigList.size(); j++) {
					cmisConfigDTO = cmisConfigList.get(j);
					if (cmisConfigurationDTO.getUserName().equals(cmisConfigDTO.getUserName())
							&& cmisConfigurationDTO.getServerURL().equals(cmisConfigDTO.getServerURL())
							&& ((cmisConfigurationDTO.getRepositoryID() == null && cmisConfigDTO.getRepositoryID() == null) || (cmisConfigurationDTO
									.getRepositoryID() != null && cmisConfigDTO.getRepositoryID() != null && cmisConfigurationDTO
									.getRepositoryID().equals(cmisConfigDTO.getRepositoryID())))
							&& cmisConfigurationDTO.getFolderName().equals(cmisConfigDTO.getFolderName())
							&& cmisConfigurationDTO.getValue().equals(cmisConfigDTO.getValue())
							&& cmisConfigurationDTO.getValueToUpdate().equals(cmisConfigDTO.getValueToUpdate())) {
						duplicateUsrFlg = true;
						break;
					}
				}
			}
		}
		return duplicateUsrFlg;
	}

	/**
	 * Method to check scanner configuration child valid.
	 * 
	 * @param model the model {@link WebScannerConfigurationDTO}.
	 * @param childName the child name {@link String}.
	 * @param isClass the is class {link Class<?>}.
	 * @return true, if scanner configuration child vali.d
	 */
	public static boolean isScannerChildInValid(final WebScannerConfigurationDTO model, final String childName, final Class<?> isClass) {
		if (model != null) {
			final List<WebScannerConfigurationDTO> scannerDTOList = (List<WebScannerConfigurationDTO>) model.getChildren();
			for (final WebScannerConfigurationDTO scannerDTO : scannerDTOList) {
				if (childName.equals(scannerDTO.getName())
						&& (StringUtil.isNullOrEmpty(scannerDTO.getValue()) || !ValidationUtil.isValidNonNegativeNumericalValue(
								scannerDTO.getValue(), isClass))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Method to check whether scanner configuration mandatory fields empty.
	 * 
	 * @param scannerDTO the scanner configuration dto {@link WebScannerConfigurationDTO}.
	 * @return true, if scanner configuration mandatory fields empty.
	 */
	public static boolean isScannerConfigMandatoryFieldsEmpty(WebScannerConfigurationDTO scannerDTO) {
		boolean isEmpty = false;
		if (StringUtil.isNullOrEmpty(scannerDTO.getValue())) {
			isEmpty = true;
		} else {
			List<WebScannerConfigurationDTO> childList = (List<WebScannerConfigurationDTO>) scannerDTO.getChildren();
			for (WebScannerConfigurationDTO childDTO : childList) {
				if (StringUtil.isNullOrEmpty(childDTO.getValue())) {
					isEmpty = true;
					break;
				}
			}
		}
		return isEmpty;
	}

	/**
	 * Method to check whether scanner configuration numeric fields valid.
	 * 
	 * @param scannerList the scanner list {@link List<{@link WebScannerConfigurationDTO}>}.
	 * @return true, if scanner configuration numeric fields valid.
	 */
	public static boolean isScannerConfigNumericFieldsValid(List<WebScannerConfigurationDTO> scannerList) {
		boolean isValid = true;
		if (!CollectionUtil.isEmpty(scannerList)) {
			for (WebScannerConfigurationDTO scannerDTO : scannerList) {
				List<WebScannerConfigurationDTO> childList = (List<WebScannerConfigurationDTO>) scannerDTO.getChildren();
				for (WebScannerConfigurationDTO childDTO : childList) {
					if (childDTO.getDataType() != null && childDTO.getDataType().equals("Integer")
							&& !ValidationUtil.isValidNonNegativeNumericalValue(childDTO.getValue(), Integer.class)) {
						isValid = false;
						break;
					} else if (childDTO.getDataType() != null && childDTO.getDataType().equals("Double")
							&& !ValidationUtil.isValidNonNegativeNumericalValue(childDTO.getValue(), Double.class)) {
						isValid = false;
						break;
					}
				}
			}
		}
		return isValid;
	}

	/**
	 * Method to check whether scanner configuration duplicate.
	 * 
	 * @param scannerConfigList the scanner configuration list {@link List<{@link WebScannerConfigurationDTO}>}.
	 * @return true, if scanner configuration already exist.
	 */
	public static boolean isScannerProfileDuplicate(List<WebScannerConfigurationDTO> scannerConfigList) {
		boolean isDuplicate = false;
		if (!CollectionUtil.isEmpty(scannerConfigList)) {
			WebScannerConfigurationDTO scannerDTO = null;
			String profileName = null;
			for (int i = 0; i < scannerConfigList.size(); i++) {
				scannerDTO = scannerConfigList.get(i);
				profileName = scannerDTO.getValue();
				for (int j = i + 1; j < scannerConfigList.size(); j++) {
					WebScannerConfigurationDTO scannerConfDTO = scannerConfigList.get(j);
					if (profileName != null && profileName.equals(scannerConfDTO.getValue())) {
						isDuplicate = true;
						break;
					}
				}
			}
		}
		return isDuplicate;
	}
	
	public static boolean isRegexPatternValid(String regexPattern)
	{
		boolean isValidRegex = true;
		try {
			RegExp.compile(regexPattern);
		} catch (final Exception e) {
			isValidRegex = false;
		}
		return isValidRegex;
	}

}
