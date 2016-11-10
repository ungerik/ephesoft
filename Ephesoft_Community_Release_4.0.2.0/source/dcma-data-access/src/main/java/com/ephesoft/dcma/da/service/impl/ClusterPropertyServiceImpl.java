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

package com.ephesoft.dcma.da.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.common.ClusterPropertyType;
import com.ephesoft.dcma.da.dao.ClusterPropertyDao;
import com.ephesoft.dcma.da.domain.ClusterProperty;
import com.ephesoft.dcma.da.service.ClusterPropertyService;
import com.ephesoft.dcma.da.service.ServiceStatusService;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * The <code>ClusterPropertyServiceImpl</code> is a class that implements methods defined by {@link ClusterPropertyService} to
 * implement business rules to get Ephesoft Cluster level properties.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see {@link ServiceStatusService} , {@link ClusterPropertyType}
 * 
 */

@Service
public class ClusterPropertyServiceImpl implements ClusterPropertyService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(ClusterPropertyServiceImpl.class);

	/**
	 * The clusterPropertyDao {@link ClusterPropertyDao} is used to perform dao operation needed to perform services.
	 */
	@Autowired
	private ClusterPropertyDao clusterPropertyDao;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void createClusterProperty(ClusterProperty property) {
		if (null == property) {
			LOGGER.info("NUll value of cluster property. Cann't do the createClusterProperty operation");
		} else {
			clusterPropertyDao.createClusterProperty(property);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void deleteClusterProperty(ClusterProperty property) {
		if (null == property) {
			LOGGER.info("NUll value of cluster property. Cann't do the deleteClusterProperty operation");
		} else {
			clusterPropertyDao.remove(property);
		}
	}

	/**
	 * API to update existing Cluster property into DB.
	 * 
	 * @param property
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void updateClusterProperty(ClusterProperty property) {
		if (null == property) {
			LOGGER.info("NUll value of cluster property. Cann't do the updaClusterProperty operation");
		} else {
			if (0 == property.getId()) {
				ClusterProperty propertyFromDb = this.getClusterPropertyValue(property.getPropertyName());
				if (null == propertyFromDb) {
					LOGGER.info("The input ClusterProperty is not able available in DB. Can not do the update operation. Please use save operation");
				}
				{
					propertyFromDb.setPropertyValue(property.getPropertyValue());
					clusterPropertyDao.saveOrUpdateProperty(propertyFromDb);
				}
			} else {
				clusterPropertyDao.saveOrUpdateProperty(property);
			}

		}
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
	public ClusterProperty getClusterPropertyValue(ClusterPropertyType propertyName) {
		ClusterProperty property = null;
		if (null == propertyName) {
			LOGGER.info("NUll value of  propertyName. Cann't do the getClusterPropertyValue operation");
		} else {
			property = clusterPropertyDao.getValueOfaProperty(propertyName);
		}

		return property;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
	public List<ClusterProperty> getAllClusterProperties() {

		return clusterPropertyDao.getAllPropertyList();
	}

}
