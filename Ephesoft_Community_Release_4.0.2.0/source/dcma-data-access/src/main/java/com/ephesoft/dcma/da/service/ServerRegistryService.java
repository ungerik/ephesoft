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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.domain.ServiceStatus;

/**
 * Interface to provide Services related to ServerRegistry.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 14-Apr-2015 <br/>
 * @version 1.0 <br/>
 *          $LastChangedDate: 2015-04-14 12:53:03 +0530 (Tue, 14 Apr 2015) $ <br/>
 *          $LastChangedRevision: 21604 $ <br/>
 */
public interface ServerRegistryService {

	/**
	 * An API to fetch a Server Registry.
	 * 
	 * @param identifier {@link Serializable}
	 * @return {@link ServerRegistry}
	 */
	ServerRegistry getServerRegistry(Serializable identifier);

	/**
	 * An API to fetch all Server Registry.
	 * 
	 * @return {@link List}<{@link ServerRegistry}> return the server registry list.
	 */
	List<ServerRegistry> getAllServerRegistry();

	/**
	 * An api to fetch all the Server Registry by ip address, port number and context.
	 * 
	 * @param ipAddress {@link String}
	 * @param portNumber {@link String}
	 * @param context {@link String}
	 * 
	 * @return {@link ServerRegistry} return the server registry.
	 */
	ServerRegistry getServerRegistry(String ipAddress, String portNumber, String context);

	/**
	 * API to save or update a Server Registry.
	 * 
	 * @param serverRegistry {@link ServerRegistry}
	 */
	void updateServerRegistry(ServerRegistry serverRegistry);

	/**
	 * API to create a new Server Registry.
	 * 
	 * @param serverRegistry {@link ServerRegistry}
	 */
	void createServerRegistry(ServerRegistry serverRegistry);

	/**
	 * API to remove an existing Server Registry.
	 * 
	 * @param serverRegistry {@link ServerRegistry}
	 */
	void removeServerRegistry(ServerRegistry serverRegistry);

	/**
	 * API to get the inactive servers.
	 * 
	 * @return {@link List}<{@link ServerRegistry}>
	 */
	List<ServerRegistry> getInactiveServers();

	/**
	 * Gets the services for the given server.
	 * 
	 * @param serverRegistryId {@link Long}
	 * @return {@link Set}<{@link ServiceStatus}>
	 */
	Set<ServiceStatus> getServicesForServer(Long serverRegistryId);

	/**
	 * API to get the active servers.
	 * 
	 * @return {@link List}<{@link ServerRegistry}>
	 */
	List<ServerRegistry> getActiveServers();

	/**
	 * API to get the active License servers.
	 * 
	 * @return {@link List}<{@link ServerRegistry}>
	 */
	List<ServerRegistry> getActiveLicenseServers();

	/**
	 * API to get the active servers those are designated to run folder monitor service.
	 * 
	 * @return {@link List}<{@link ServerRegistry}> of active servers
	 */
	List<ServerRegistry> getActiveFolderMoniterServers();

	/**
	 * API to get Active server with priority loaded into it for a current server.
	 * 
	 * @param currentServerId {@link Long}
	 * @param isSecure {@link Boolean}
	 * @return {@link Set}<{@link Integer}>
	 */
	List<Set<Integer>> getPriorityLoadedActiveServers(final long currentServerId, boolean isSecure);

	/**
	 * API to get Active servers with priority loaded into it.
	 * 
	 * @return {@link Map}<{@link Set}<{@link Integer}>,<{@link Integer}>>
	 */
	Map<Set<Integer>, Integer> getPriorityLoadedActiveServers();

	/**
	 * API to get the priority range set.
	 * 
	 * @return NumericSetOfPriorityRange {@link Set}<{@link Integer}>
	 */
	Set<Integer> getNumericSetOfPriorityRange();

	/**
	 * API to set the priority range numeric set.
	 * 
	 * @param numericSetOfPriorityRange {@link Set}<{@link Integer}>
	 */
	void setNumericSetOfPriorityRange(Set<Integer> numericSetOfPriorityRange);
}
