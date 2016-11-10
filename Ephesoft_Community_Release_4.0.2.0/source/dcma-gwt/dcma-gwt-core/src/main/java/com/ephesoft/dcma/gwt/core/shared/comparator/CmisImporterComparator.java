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

package com.ephesoft.dcma.gwt.core.shared.comparator;

import java.util.Comparator;

import com.ephesoft.dcma.core.common.DomainProperty;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.core.shared.CmisConfigurationDTO;

/**
 * Compares two CmisConfigurationDTOs based on property defined in order object.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.shared.CmisConfigurationDTO
 */
public class CmisImporterComparator implements Comparator<Object> {

	/**
	 * Instance of {@link Order}.
	 */
	private final Order order;
	
	/**
	 * String constant for username.
	 */
	private static final String CMIS_IMPORTER_USERNAME = "userName";
	/**
	 * String constant for password.
	 */
	private static final String CMIS_IMPORTER_PASSWORD = "password";
	/**
	 * String constant for serverURL.
	 */
	private static final String CMIS_IMPORTER_SERVER_URL = "serverURL";
	/**
	 * String constant for repositoryID.
	 */
	private static final String CMIS_IMPORTER_REPOSITORYID = "repositoryID";
	/**
	 * String constant for folderName.
	 */
	private static final String CMIS_IMPORTER_FOLDER_NAME = "folderName";
	/**
	 * String constant for value.
	 */
	private static final String CMIS_IMPORTER_VALUE = "value";
	/**
	 * String constant for valueToUpdate.
	 */
	private static final String CMIS_IMPORTER_VALUE_TO_UPDATE = "valueToUpdate";
	/**
	 * String constant for fileExtension.
	 */
	private static final String CMIS_IMPORTER_FILE_EXTENSION = "fileExtension";
	/**
	 * String constant for cmisProperty.
	 */
	private static final String CMIS_IMPORTER_CMIS_PROPERTY = "cmisProperty";

	/**
	 * Constructor for Cmis Importer Comparator.
	 * 
	 * @param order {@link Order}
	 */
	public CmisImporterComparator(final Order order) {
		this.order = order;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final Object cmisConfigurationOne, final Object cmisConfigurationTwo) {
		int isEqualOrGreater = 0;
		final CmisConfigurationDTO cmisConfigurationDTOOne = (CmisConfigurationDTO) cmisConfigurationOne;
		final CmisConfigurationDTO cmisConfigurationDTOTwo = (CmisConfigurationDTO) cmisConfigurationTwo;
		final Boolean isAsc = order.isAscending();

		final String cmisConfigPropertyOne = getProperty(order.getSortProperty(), cmisConfigurationDTOOne);
		final String cmisConfigPropertyTwo = getProperty(order.getSortProperty(), cmisConfigurationDTOTwo);
		if (isAsc) {
			isEqualOrGreater = cmisConfigPropertyOne.compareTo(cmisConfigPropertyTwo);
		} else {
			isEqualOrGreater = cmisConfigPropertyTwo.compareTo(cmisConfigPropertyOne);
		}
		return isEqualOrGreater;
	}

	/**
	 * Getting the name of domain property to sort on.
	 * 
	 * @param domainProperty {@link DomainProperty}
	 * @param cmisConfigurationDTO {@link CmisConfigurationDTO}
	 * @return property {@link String}
	 */
	public String getProperty(final DomainProperty domainProperty, final CmisConfigurationDTO cmisConfigurationDTO) {
		String property = null;
		if (domainProperty.getProperty().equals(CMIS_IMPORTER_USERNAME)) {
			property = cmisConfigurationDTO.getUserName();
		} else if (domainProperty.getProperty().equals(CMIS_IMPORTER_PASSWORD)) {
			property = cmisConfigurationDTO.getPassword();
		} else if (domainProperty.getProperty().equals(CMIS_IMPORTER_SERVER_URL)) {
			property = cmisConfigurationDTO.getServerURL();
		} else if (domainProperty.getProperty().equals(CMIS_IMPORTER_REPOSITORYID)) {
			property = cmisConfigurationDTO.getRepositoryID();
		} else if (domainProperty.getProperty().equals(CMIS_IMPORTER_FOLDER_NAME)) {
			property = cmisConfigurationDTO.getFolderName();
		} else if (domainProperty.getProperty().equals(CMIS_IMPORTER_VALUE)) {
			property = cmisConfigurationDTO.getValue();
		} else if (domainProperty.getProperty().equals(CMIS_IMPORTER_VALUE_TO_UPDATE)) {
			property = cmisConfigurationDTO.getValueToUpdate();
		} else if (domainProperty.getProperty().equals(CMIS_IMPORTER_FILE_EXTENSION)) {
			property = cmisConfigurationDTO.getFileExtension();
		} else if (domainProperty.getProperty().equals(CMIS_IMPORTER_CMIS_PROPERTY)) {
			property = cmisConfigurationDTO.getCmisProperty();
		}
		return property;
	}

}
