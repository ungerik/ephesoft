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

package com.ephesoft.dcma.workflows.util;

import java.util.Date;
import java.util.Set;

/**
 * This class represents the virtual object carrier for details of the executing server.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 27, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class ServerDetails {

	/**
	 * int Max lock time for a job by a server in miliseconds.
	 */
	private int lockTimeInMillis;

	/**
	 * {@link String} Context Path of the server.
	 */
	private String serverContextPath;

	/**
	 * {@link String} Context Path of the server.
	 */
	private String serverIPAddress;

	/**
	 * {@link Date} Last date when server status was modified.
	 */
	private Date lastServerModified;

	/**
	 * {@link Set}<{@link Integer}> set of batch priorities.
	 */
	private Set<Integer> setOfPriority;

	public Set<Integer> getSetOfPriority() {
		return setOfPriority;
	}

	public void setSetOfPriority(Set<Integer> setOfPriority) {
		this.setOfPriority = setOfPriority;
	}

	/**
	 * Gets max lock time for a job by a server in miliseconds.
	 * 
	 * @return int
	 */
	public int getLockTimeInMillis() {
		return lockTimeInMillis;
	}

	/**
	 * Sets max lock time for a job by a server in miliseconds.
	 * 
	 * @param lockTimeInMillis int
	 */
	public void setLockTimeInMillis(final int lockTimeInMillis) {
		this.lockTimeInMillis = lockTimeInMillis;
	}

	/**
	 * Gets context Path of the server.
	 * 
	 * @return {@link String}
	 */
	public String getServerContextPath() {
		return serverContextPath;
	}

	/**
	 * Sets context Path of the server.
	 * 
	 * @param serverContextPath {@link String}
	 */
	public void setServerContextPath(final String serverContextPath) {
		this.serverContextPath = serverContextPath;
	}

	/**
	 * Gets last date when server status was modified.
	 * 
	 * @return {@link Date}
	 */
	public Date getLastServerModified() {
		return lastServerModified;
	}

	/**
	 * Sets last date when server status was modified.
	 * 
	 * @param lastServerModified {@link Date}
	 */
	public void setLastServerModified(final Date lastServerModified) {
		this.lastServerModified = lastServerModified;
	}

	public String getServerIPAddress() {
		return serverIPAddress;
	}

	public void setServerIPAddress(String serverIPAddress) {
		this.serverIPAddress = serverIPAddress;
	}
}
