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

import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InitializeMetaData implements IsSerializable {

	/** The current user. */
	private String currentUser;

	/** True if operating system is linux. */
	private boolean operatingSystemLinux;

	/** The operating system windows. */
	private boolean operatingSystemWindows;

	/** The locale. */
	private String locale;

	private boolean isSuperAdmin;

	/** The user roles. */
	private Set<String> userRoles;

	/** The super admin groups. */
	private Set<String> superAdminGroups;

	/** All Groups */
	private Set<String> allGroups;
	
	private String footerLabel;
	
	private String ephesoftURL;

    private int documentDisplayProperty;
	/**
	 * Gets the current user.
	 * 
	 * @return the current user
	 */
	public String getCurrentUser() {
		return currentUser;
	}

	
	/**
	 * @return the isSuperAdmin
	 */
	public int getDocumentDisplayProperty() {
		return documentDisplayProperty;
	}
	public void setDocumentDisplayProperty(int documentDisplayProperty) {
		this.documentDisplayProperty = documentDisplayProperty;
	}
	
	/*
	* @return the isSuperAdmin
	 */
	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}



	
	/**
	 * @param isSuperAdmin the isSuperAdmin to set
	 */
	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}



	/**
	 * Sets the current user.
	 * 
	 * @param currentUser the new current user
	 */
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * Checks if is operating system linux.
	 * 
	 * @return true, if is operating system linux
	 */
	public boolean isOperatingSystemLinux() {
		return operatingSystemLinux;
	}

	/**
	 * Sets the operating system linux.
	 * 
	 * @param operatingSystemLinux the new operating system linux
	 */
	public void setOperatingSystemLinux(boolean operatingSystemLinux) {
		this.operatingSystemLinux = operatingSystemLinux;
	}

	/**
	 * Gets the locale.
	 * 
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Sets the locale.
	 * 
	 * @param locale the new locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * Gets the user roles.
	 * 
	 * @return the user roles
	 */
	public Set<String> getUserRoles() {
		return userRoles;
	}

	/**
	 * Sets the user roles.
	 * 
	 * @param userRoles the new user roles
	 */
	public void setUserRoles(Set<String> userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * Checks if is operating system windows.
	 * 
	 * @return true, if is operating system windows
	 */
	public boolean isOperatingSystemWindows() {
		return operatingSystemWindows;
	}

	/**
	 * Sets the operating system windows.
	 * 
	 * @param operatingSystemWindows the new operating system windows
	 */
	public void setOperatingSystemWindows(boolean operatingSystemWindows) {
		this.operatingSystemWindows = operatingSystemWindows;
	}

	/**
	 * Gets the super admin groups.
	 * 
	 * @return the super admin groups
	 */
	public Set<String> getSuperAdminGroups() {
		return superAdminGroups;
	}

	/**
	 * Sets the super admin groups.
	 * 
	 * @param superAdminGroups the new super admin groups
	 */
	public void setSuperAdminGroups(Set<String> superAdminGroups) {
		this.superAdminGroups = superAdminGroups;
	}

	/**
	 * Gets the all groups.
	 *
	 * @return the all groups
	 */
	public Set<String> getAllGroups() {
		return allGroups;
	}

	/**
	 * Sets the all groups.
	 *
	 * @param allGroups the new all groups
	 */
	public void setAllGroups(Set<String> allGroups) {
		this.allGroups = allGroups;
	}


	public String getFooterLabel() {
		return footerLabel;
	}


	public void setFooterLabel(String footerLabel) {
		this.footerLabel = footerLabel;
	}


	public String getEphesoftURL() {
		return ephesoftURL;
	}


	public void setEphesoftURL(String ephesoftURL) {
		this.ephesoftURL = ephesoftURL;
	}
	
	
	

}
