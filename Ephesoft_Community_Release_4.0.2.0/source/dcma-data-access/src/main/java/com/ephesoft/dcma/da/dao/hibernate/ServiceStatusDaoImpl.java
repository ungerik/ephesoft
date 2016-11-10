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

package com.ephesoft.dcma.da.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.common.ServiceType;
import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.da.dao.ServiceStatusDao;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.domain.ServiceStatus;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * The <code>ServiceStatusDaoImpl</code> is the implementation of {@link ServiceStatusDao} which implements the dao service defined by
 * ServiceStatusDao.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see ServiceStatusDao
 * 
 */

@Repository
public class ServiceStatusDaoImpl extends HibernateDao<ServiceStatus> implements ServiceStatusDao {

	/**
	 * The SERVICE_TYPE is a string constant for property name used for service type inside {@link ServiceStatus}.
	 */
	private static final String SERVICE_TYPE = "serviceType";

	/**
	 * The SERVER_REGISTRY is a string constant for property name used for server registered inside {@link ServiceStatus}.
	 */
	private static final String SERVER_REGISTRY = "serverRegistry";

	/**
	 * LOGGER for the class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceStatusDaoImpl.class);

	@Override
	public List<ServiceStatus> getServiceStatusList(final ServiceType serviceType) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Getting status of service: ", serviceType.toString()));
		final DetachedCriteria detachedCriteria = criteria();
		detachedCriteria.add(Restrictions.eq(SERVICE_TYPE, serviceType));
		final List<ServiceStatus> serviceStatusList = find(detachedCriteria);
		if (!CollectionUtil.isEmpty(serviceStatusList)) {
			final ServerRegistry serverRegistry = serviceStatusList.get(0).getServerRegistry();
			LOGGER.debug(EphesoftStringUtil.concatenate("Status of service list", serviceType.toString(), "is under: ",
					serverRegistry.getIpAddress()));
		} else {
			LOGGER.debug(EphesoftStringUtil.concatenate("Status of service list", serviceType.toString(), "is not under any server"));
		}
		return serviceStatusList;
	}

	@Override
	public List<ServiceStatus> getServiceStatusListForServer(final ServerRegistry serverRegistry, final ServiceType serviceType) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Getting status of service: ", serviceType.toString(), "for Server: ",
				serverRegistry.getIpAddress()));
		final DetachedCriteria detachedCriteria = criteria();
		detachedCriteria.add(Restrictions.and(Restrictions.eq(SERVER_REGISTRY, serverRegistry),
				Restrictions.eq(SERVICE_TYPE, serviceType)));
		final List<ServiceStatus> serviceStatusList = find(detachedCriteria);
		if (!CollectionUtil.isEmpty(serviceStatusList)) {
			LOGGER.debug(EphesoftStringUtil.concatenate("Status of service list", serviceType.toString(), "is under: ",
					serverRegistry.getIpAddress()));
		} else {
			LOGGER.debug(EphesoftStringUtil.concatenate("Status of service list", serviceType.toString(), "is not under given server"));
		}
		return serviceStatusList;
	}

}
