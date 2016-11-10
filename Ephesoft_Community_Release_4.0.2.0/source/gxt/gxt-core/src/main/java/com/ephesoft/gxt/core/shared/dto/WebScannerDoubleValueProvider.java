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

import java.util.List;

import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.sencha.gxt.core.client.ValueProvider;

/**
 * This ValueProvider is used for Web Scanner Profile.
 * 
 * @author Ephesoft
 * @version 1.0
 * @param <T> the generic type
 * @param <V> the value type
 * @see com.ephesoft.gxt.core.shared.dto.WebScannerValueProvider
 */
public class WebScannerDoubleValueProvider<T extends WebScannerConfigurationDTO, V extends Comparable<Double>> implements
		ValueProvider<T, Double> {

	/** The class name. */
	private final Class<V> className;

	/** The header name. */
	public String headerName;

	/**
	 * Instantiates a new web scanner value provider.
	 * 
	 * @param headerName the header name
	 * @param className the class name
	 */
	public WebScannerDoubleValueProvider(String headerName, Class<V> className) {
		super();
		this.headerName = headerName;
		this.className = className;
	}

	@Override
	public Double getValue(T object) {
		// TODO Auto-generated method stub
		Double value = null;
		WebScannerConfigurationDTO scannerDTO = (WebScannerConfigurationDTO) object;
		if (!headerName.equalsIgnoreCase(scannerDTO.getDescription())) {
			List<WebScannerConfigurationDTO> childList = (List<WebScannerConfigurationDTO>) scannerDTO.getChildren();
			if (!CollectionUtil.isEmpty(childList)) {
				for (WebScannerConfigurationDTO webScannerDTO : childList) {
					if (headerName.equalsIgnoreCase(webScannerDTO.getDescription())) {
						if (className == Double.class) {
							value = (webScannerDTO.getValue() != null ? Double.parseDouble(webScannerDTO.getValue()) : null);
						}
					}
				}
			}
		}
		return value;
	}

	@Override
	public void setValue(T object, Double value) {
		// TODO Auto-generated method stub
		WebScannerConfigurationDTO scannerDTO = (WebScannerConfigurationDTO) object;
		if (!headerName.equalsIgnoreCase(scannerDTO.getDescription())) {
			List<WebScannerConfigurationDTO> childList = (List<WebScannerConfigurationDTO>) scannerDTO.getChildren();
			if (!CollectionUtil.isEmpty(childList)) {
				for (WebScannerConfigurationDTO webScannerDTO : childList) {
					if (headerName.equalsIgnoreCase(webScannerDTO.getDescription())) {
						if (value != null) {
							if (value != null && value instanceof Double) {
								webScannerDTO.setValue(String.valueOf(value));
							}
						} else {
							webScannerDTO.setValue(null);
						}
					}
				}
			}
		}
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}
}
