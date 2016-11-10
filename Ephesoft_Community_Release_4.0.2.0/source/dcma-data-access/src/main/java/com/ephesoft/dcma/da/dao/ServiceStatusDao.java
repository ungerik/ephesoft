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

package com.ephesoft.dcma.da.dao;

import java.util.List;

import com.ephesoft.dcma.core.common.ServiceType;
import com.ephesoft.dcma.core.dao.Dao;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.domain.ServiceStatus;

/**
 * The <code>ServiceStatusDao</code> interface provides operations to be performed on ServiceStatus table like CRUD operations,
 * checking which Ephesoft services is under which server.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see ServiceStatus
 * @see Dao
 * @see ServiceType
 * 
 */
public interface ServiceStatusDao extends Dao<ServiceStatus> {

	/**
	 * To get the service status list for the given service type, which should ideally return a list of only one element or an empty
	 * list. A service can be registered only under one server which is the desired functionality. This is used initially by the server
	 * to know whether given service responsibility is taken by the some other server or not.
	 * 
	 * @param serviceType {@link ServiceType} type of service type.
	 * @return {@link List}<{@link ServiceStatus}> list of service status.
	 */
	List<ServiceStatus> getServiceStatusList(ServiceType serviceType);

	/**
	 * To get the service status list for a given server and service type,which should ideally return a list of only one element or an
	 * empty list. A service can be registered only under one server which is the desired functionality. This is used to at the start
	 * of service to check whether it can go on or not.
	 * 
	 * @param serverRegistry {@link ServerRegistry} server for which service status is to be checked.
	 * @param serviceType {@link ServiceType} type of service to be checked.
	 * @return {@link List}<{@link ServiceStatus}> list of service status.
	 */
	List<ServiceStatus> getServiceStatusListForServer(ServerRegistry serverRegistry, ServiceType serviceType);

}
