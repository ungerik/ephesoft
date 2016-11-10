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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * DTO for license holder.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.google.gwt.user.client.rpc.IsSerializable
 * 
 */
public class LicenseHolderDTO implements IsSerializable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * commonName String.
	 */
	private String commonName;

	/**
	 * localityName String.
	 */
	private String localityName;

	/**
	 * stateName String.
	 */
	private String stateName;

	/**
	 * organizationName String.
	 */
	private String organizationName;

	/**
	 * organizationUnitName String.
	 */
	private String organizationUnitName;

	/**
	 * countryName String.
	 */
	private String countryName;

	/**
	 * address String.
	 */
	private String address;

	/**
	 * domainComponent String.
	 */
	private String domainComponent;

	/**
	 * resellerName String.
	 */
	private String resellerName;
	
	/**
	 * serverType String.
	 */
	private String serverType;
	
	/**
	 * username String
	 */
	private String username;

	/**
	 * To get the common name.
	 * 
	 * @return String
	 */
	public String getCommonName() {
		return commonName;
	}

	/**
	 * To set the common name.
	 * 
	 * @param commonName
	 */
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	/**
	 * To get the locality name.
	 * 
	 * @return String
	 */
	public String getLocalityName() {
		return localityName;
	}

	/**
	 * To set the locality name.
	 * 
	 * @param localityName
	 */
	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}

	/**
	 * To get the state name.
	 * 
	 * @return String
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * To set the state name.
	 * 
	 * @param stateName
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/**
	 * To get the organization name.
	 * 
	 * @return String
	 */
	public String getOrganizationName() {
		return organizationName;
	}

	/**
	 * To set the organization name.
	 * 
	 * @param organizationName
	 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	/**
	 * To get the organization unit name.
	 * 
	 * @return String
	 */
	public String getOrganizationUnitName() {
		return organizationUnitName;
	}

	/**
	 * To set the organization unit name.
	 * 
	 * @param organizationUnitName
	 */
	public void setOrganizationUnitName(String organizationUnitName) {
		this.organizationUnitName = organizationUnitName;
	}

	/**
	 * To get the country name.
	 * 
	 * @return String
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * To set the country name.
	 * 
	 * @param countryName
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * To get the address.
	 * 
	 * @return String
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * To set the address.
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * To get the domain component.
	 * 
	 * @return String
	 */
	public String getDomainComponent() {
		return domainComponent;
	}

	/**
	 * To set the domain component.
	 * 
	 * @param domainComponent
	 */
	public void setDomainComponent(String domainComponent) {
		this.domainComponent = domainComponent;
	}

	/**
	 * To get the resellerName.
	 * 
	 * @return String
	 */
	public String getResellerName() {
		return resellerName;
	}

	/**
	 * To set the resellerName.
	 * 
	 * @param domainComponent
	 */

	public void setResellerName(String resellerName) {
		this.resellerName = resellerName;
	}
	
	/**
	 * To get the server type.
	 * 
	 * @return String
	 */
	public String getServerType() {
		return serverType;
	}

	/**
	 * To set the server type.
	 * 
	 * @param serverType
	 */
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	
	/**
	 * To get the username.
	 * 
	 * @return String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * To set the username.
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
