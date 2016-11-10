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

package com.ephesoft.dcma.heartbeat;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.service.ServerHeartBeatMonitor;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.service.ServerRegistryService;

/**
 * This class is responsible for watching all the servers registered with the common database pool. If any of the server is down or not
 * active this service will mark that registry entry as in active such that other servers can pick the entry corresponding to this shut
 * down server.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.heartbeat.service.HeartBeatServiceImpl
 * 
 */
public final class HeartBeat {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeat.class);

	/**
	 * Reference of ServerRegistryService.
	 */
	@Autowired
	private ServerRegistryService serverRegistryService;

	/**
	 * Reference of {@link ServerHeartBeatMonitor}.
	 */
	@Autowired
	private ServerHeartBeatMonitor serverHeartBeatMonitor;

	/**
	 * This method will check the health of all the servers registered to the common data base pool.
	 */
	public void heartBeatHealth() {

		List<ServerRegistry> serverRegistries = getServerRegistries();
		if (CollectionUtils.isEmpty(serverRegistries)) {
			LOGGER.info("No server registry found.");
		} else {
			for (ServerRegistry serverRegistry : serverRegistries) {
				if (serverRegistry != null) {
					updateServerActiveStatus(serverRegistry);
				}
			}
		}

	}

	private List<ServerRegistry> getServerRegistries() {
		if (null == serverRegistryService) {
			throw new DCMABusinessException("Not able to find ServerRegistryService service of the system.");
		}
		List<ServerRegistry> serverRegistries = serverRegistryService.getAllServerRegistry();
		return serverRegistries;
	}

	// /**
	// * This method will check the health of all the servers registered to the common data base pool.
	// */
	// public void heartBeatHealth(long currentServerId) {
	// List<ServerRegistry> serverRegistries = getServerRegistries();
	// if (null == serverRegistries || serverRegistries.isEmpty()) {
	// LOGGER.info("No server registry found.");
	// } else {
	// for (ServerRegistry serverRegistry : serverRegistries) {
	// if (null != serverRegistry && currentServerId != serverRegistry.getId()) {
	// updateServerActiveStatus(serverRegistry);
	// }
	// }
	// }
	//
	// }

	private void updateServerActiveStatus(ServerRegistry serverRegistry) {
		if (null != serverRegistry) {
			boolean isActive = serverHeartBeatMonitor.getServerActiveStatus(serverRegistry.getIpAddress(), serverRegistry.getPort(),
					serverRegistry.getAppContext(), EphesoftContext.getCurrent().isSecure());
			if (serverRegistry.isActive() != isActive) {
				serverRegistry.setActive(isActive);
				serverRegistryService.updateServerRegistry(serverRegistry);
			}
		}
	}

}
