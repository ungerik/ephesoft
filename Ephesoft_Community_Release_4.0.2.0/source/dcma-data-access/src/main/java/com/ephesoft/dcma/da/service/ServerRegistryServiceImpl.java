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

package com.ephesoft.dcma.da.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.jcajce.provider.asymmetric.EC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.service.ServerHeartBeatMonitor;
import com.ephesoft.dcma.da.dao.ServerRegistryDao;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.domain.ServiceStatus;

@Service
public class ServerRegistryServiceImpl implements ServerRegistryService {

	@Autowired
	private ServerRegistryDao serverRegistryDao;

	/**
	 * Reference of {@link ServerHeartBeatMonitor}.
	 */
	@Autowired
	private ServerHeartBeatMonitor serverHeartBeatMonitor;

	/**
	 * Set of priority of batches supported for execution by a server. {@link Set}<{@link Integer}>
	 */
	private Set<Integer> numericSetOfPriorityRange;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void createServerRegistry(ServerRegistry serverRegistry) {
		serverRegistryDao.createServerRegistry(serverRegistry);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ServerRegistry> getAllServerRegistry() {
		List<ServerRegistry> serverRegistries = null;
		serverRegistries = serverRegistryDao.getAllServerRegistry();
		return serverRegistries;
	}

	@Override
	@Transactional(readOnly = true)
	public ServerRegistry getServerRegistry(Serializable identifier) {
		return serverRegistryDao.getServerRegistry(identifier);
	}

	@Override
	@Transactional(readOnly = true)
	public ServerRegistry getServerRegistry(String ipAddress, String portNumber, String context) {
		return serverRegistryDao.getServerRegistry(ipAddress, portNumber, context);
	}

	@Override
	@Transactional(readOnly = false)
	public void removeServerRegistry(ServerRegistry serverRegistry) {
		serverRegistryDao.removeServerRegistry(serverRegistry);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void updateServerRegistry(ServerRegistry serverRegistry) {
		serverRegistryDao.updateServerRegistry(serverRegistry);
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
	public List<ServerRegistry> getInactiveServers() {
		return serverRegistryDao.getInactiveServers();
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Set<ServiceStatus> getServicesForServer(Long serverRegistryId) {
		Set<ServiceStatus> serviceStatusSet = null;
		if (null != serverRegistryId) {
			ServerRegistry serverRegistry = serverRegistryDao.getServerRegistry(serverRegistryId);
			if (null != serverRegistry) {
				serviceStatusSet = serverRegistry.getServiceStatusSet();
				if (null != serviceStatusSet) {
					Iterator<ServiceStatus> iter = serviceStatusSet.iterator();
					while (iter.hasNext()) {
						iter.next().getServiceType(); // this iteration is added to load serviceStatus eagerly.
					}
				}
			}
		}
		return serviceStatusSet;
	}

	/**
	 * API to get the active servers.
	 * 
	 * @return List<{@link ServerRegistry}>
	 */
	@Override
	@Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
	public List<ServerRegistry> getActiveServers() {
		return serverRegistryDao.getActiveServers();
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
	public List<Set<Integer>> getPriorityLoadedActiveServers(final long currentServerId, final boolean isSecure) {
		List<Set<Integer>> listOfServerBatchPrioritySet = null;
		List<ServerRegistry> listOfActiveServers = serverRegistryDao.getAllServerRegistry();
		if (CollectionUtils.isNotEmpty(listOfActiveServers)) {
			for (final ServerRegistry serverRegistry : listOfActiveServers) {
				if (null != serverRegistry && serverRegistry.getId() != currentServerId) {
					boolean isActive = serverHeartBeatMonitor.getServerActiveStatus(serverRegistry.getIpAddress(),
							serverRegistry.getPort(), serverRegistry.getAppContext(), isSecure);
					if (isActive) {
						String setOfBatchPriority = serverRegistry.getPriorityRange();
						String[] arrayOfPriority = setOfBatchPriority.split(ICommonConstants.OR_REGEX);
						if (ArrayUtils.isNotEmpty(arrayOfPriority)) {
							Set<Integer> arrayOfPriorityInteger = getArrayOfPriorityInteger(arrayOfPriority);
							if (null == listOfServerBatchPrioritySet) {
								listOfServerBatchPrioritySet = new ArrayList<Set<Integer>>();
							}
							listOfServerBatchPrioritySet.add(arrayOfPriorityInteger);
						}
					}
				}
			}
		}
		return listOfServerBatchPrioritySet;
	}

	private Set<Integer> getArrayOfPriorityInteger(final String[] arrayOfPriority) {
		Set<Integer> arrayOfPriorityInteger = new HashSet<Integer>(arrayOfPriority.length);
		for (int index = 0; index < arrayOfPriority.length; index++) {
			arrayOfPriorityInteger.add(Integer.valueOf(arrayOfPriority[index]));
		}
		return arrayOfPriorityInteger;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
	public Map<Set<Integer>, Integer> getPriorityLoadedActiveServers() {
		Map<Set<Integer>, Integer> listOfServerBatchPrioritySet = null;
		List<ServerRegistry> listOfActiveServers = serverRegistryDao.getActiveServers();
		if (CollectionUtils.isNotEmpty(listOfActiveServers)) {
			for (final ServerRegistry serverRegistry : listOfActiveServers) {
				String setOfBatchPriority = serverRegistry.getPriorityRange();
				String[] arrayOfPriority = setOfBatchPriority.split(ICommonConstants.OR_REGEX);
				if (ArrayUtils.isNotEmpty(arrayOfPriority)) {
					Set<Integer> arrayOfPriorityInteger = getArrayOfPriorityInteger(arrayOfPriority);
					if (null == listOfServerBatchPrioritySet) {
						listOfServerBatchPrioritySet = new HashMap<Set<Integer>, Integer>();
						listOfServerBatchPrioritySet.put(arrayOfPriorityInteger, serverRegistry.getExecutionCapacity());
					} else {
						Integer executionCapacity = listOfServerBatchPrioritySet.get(arrayOfPriorityInteger);
						if (null == executionCapacity) {
							listOfServerBatchPrioritySet.put(arrayOfPriorityInteger, serverRegistry.getExecutionCapacity());
						} else {
							listOfServerBatchPrioritySet.put(arrayOfPriorityInteger, serverRegistry.getExecutionCapacity()
									+ executionCapacity);
						}
					}
				}
			}
		}
		return listOfServerBatchPrioritySet;
	}

	/**
	 * API to get the active servers.
	 * 
	 * @return List<{@link ServerRegistry}>
	 */
	@Override
	@Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
	public List<ServerRegistry> getActiveLicenseServers() {
		return serverRegistryDao.getActiveLicenseServers();

	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
	public List<ServerRegistry> getActiveFolderMoniterServers() {
		return serverRegistryDao.getActiveFolderMoniterServers();
	}

	@Override
	public Set<Integer> getNumericSetOfPriorityRange() {
		return numericSetOfPriorityRange;
	}

	@Override
	public void setNumericSetOfPriorityRange(Set<Integer> numericSetOfPriorityRange) {
		this.numericSetOfPriorityRange = numericSetOfPriorityRange;
	}
}
