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

package com.ephesoft.dcma.batch.status.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.ephesoft.dcma.core.common.ServiceType;

/**
 * The <code>ResumeServiceEvent</code> is an application event to notify application that some services to be resumed on the current
 * server.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see ApplicationEvent
 * @see ServiceType
 * 
 */
public class ResumeServiceEvent extends ApplicationEvent {

	/**
	 * Generated serial version ID of class ResumeServiceEvent.
	 */
	private static final long serialVersionUID = 5529477786567639085L;

	/**
	 * The serviceTypeList {@link List} is the list of service which needs to be resumed.
	 */
	private final List<ServiceType> serviceTypeList;

	/**
	 * Gets the instance of {@link ResumeServiceEvent} and initialises the list of services to be resumed.
	 * 
	 * @param source source of event generation.
	 * @param serviceTypeList {@link List} list of services to be resumed.
	 */
	public ResumeServiceEvent(final Object source, final List<ServiceType> serviceTypeList) {
		super(source);
		this.serviceTypeList = serviceTypeList;
	}

	/**
	 * Returns the list of services to be resumed which is used by event respective service handlers to check if that service is in the
	 * list so that service can be resumed.
	 * 
	 * @return {@link List} list of services to be resumed.
	 */
	public List<ServiceType> getServiceType() {
		return serviceTypeList;
	}

}
