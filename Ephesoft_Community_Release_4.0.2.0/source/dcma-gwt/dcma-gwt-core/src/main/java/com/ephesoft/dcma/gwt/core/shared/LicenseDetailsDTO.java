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

package com.ephesoft.dcma.gwt.core.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * DTO for license details {@link LicenseDetails}.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.google.gwt.user.client.rpc.IsSerializable
 * 
 */
public class LicenseDetailsDTO implements IsSerializable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * issuedDate Date.
	 */
	private Date issuedDate;

	/**
	 * licenseStartDate Date.
	 */
	private Date licenseStartDate;

	/**
	 * licenseEndDate Date.
	 */
	private Date licenseEndDate;

	/**
	 * noOfCPU String.
	 */
	private int noOfCPU;

	/**
	 * deviceAddress String.
	 */
	private String deviceAddress;

	/**
	 * String constant for port number.
	 */
	private String portNumber;

	/**
	 * multiServerSwitch String.
	 */
	private String multiServerSwitch;

	/**
	 * licenseExpiryMessageDays int.
	 */
	private int licenseExpiryMessageDays;

	/**
	 * advanceReporting String.
	 */
	private String advanceReporting;

	/**
	 * advanceReporting String.
	 */
	private String nuanceSwitch;

	/**
	 * webServerSwitch String.
	 */
	private String webServerSwitch;

	/**
	 * noOfCores int.
	 */
	private int noOfCores;

	/**
	 * {@link String} Email address of the holder.
	 */
	private String emailAddress;

	/**
	 * {@link String} License type description.
	 */
	private String licenseType;

	/**
	 * {@link String} License type description.
	 */
	private long AnnualImageCount;
	
	/**
	 * String constant for issuers email.
	 */
	private String issuerEmail;

	/**
	 * To get the license issued date.
	 * 
	 * @return Date
	 */
	public Date getIssuedDate() {
		return issuedDate;
	}

	/**
	 * To set the license issued date.
	 * 
	 * @param issuedDate
	 */
	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	/**
	 * To get the license start date.
	 * 
	 * @return Date
	 */
	public Date getLicenseStartDate() {
		return licenseStartDate;
	}

	/**
	 * To set the license start date.
	 * 
	 * @param licenseStartDate
	 */
	public void setLicenseStartDate(Date licenseStartDate) {
		this.licenseStartDate = licenseStartDate;
	}

	/**
	 * To get the license end date.
	 * 
	 * @return Date
	 */
	public Date getLicenseEndDate() {
		return licenseEndDate;
	}

	/**
	 * To set the license end date.
	 * 
	 * @param licenseEndDate
	 */
	public void setLicenseEndDate(Date licenseEndDate) {
		this.licenseEndDate = licenseEndDate;
	}

	/**
	 * To get the no of CPU.
	 * 
	 * @return int
	 */
	public int getNoOfCPU() {
		return noOfCPU;
	}

	/**
	 * To set the no of CPU.
	 * 
	 * @param noOfCPU
	 */
	public void setNoOfCPU(int noOfCPU) {
		this.noOfCPU = noOfCPU;
	}

	/**
	 * To get the device address.
	 * 
	 * @return String
	 */
	public String getDeviceAddress() {
		return deviceAddress;
	}

	/**
	 * To set the device address.
	 * 
	 * @param deviceAddress
	 */
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	/**
	 * To get the port number.
	 * 
	 * @return String
	 */
	public String getPortNumber() {
		return portNumber;
	}

	/**
	 * To set the port number.
	 * 
	 * @param portNumber
	 */
	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	/**
	 * To get the multiserver switch.
	 * 
	 * @return String
	 */
	public String getMultiServerSwitch() {
		return multiServerSwitch;
	}

	/**
	 * To Set the multiserver switch.
	 * 
	 * @param multiServerSwitch
	 */
	public void setMultiServerSwitch(String multiServerSwitch) {
		this.multiServerSwitch = multiServerSwitch;
	}

	/**
	 * To get the license expiry message days.
	 * 
	 * @return int
	 */
	public int getLicenseExpiryMessageDays() {
		return licenseExpiryMessageDays;
	}

	/**
	 * To set the license expiry message days.
	 * 
	 * @param licenseExpiryMessageDays
	 */
	public void setLicenseExpiryMessageDays(int licenseExpiryMessageDays) {
		this.licenseExpiryMessageDays = licenseExpiryMessageDays;
	}

	/**
	 * To Get the advanced reporting.
	 * 
	 * @return String
	 */
	public String getAdvanceReporting() {
		return advanceReporting;
	}

	/**
	 * To set the advanced reporting.
	 * 
	 * @param advanceReporting
	 */
	public void setAdvanceReporting(String advanceReporting) {
		this.advanceReporting = advanceReporting;
	}

	/**
	 * To set the nuance switch.
	 * 
	 * @param nuanceSwitch
	 */
	public void setNuanceSwitch(String nuanceSwitch) {
		this.nuanceSwitch = nuanceSwitch;

	}

	/**
	 * To Get the nuance switch.
	 * 
	 * @return String
	 */
	public String getNuanceSwitch() {
		return nuanceSwitch;
	}

	/**
	 * To get the no of cores.
	 * 
	 * @return int
	 */
	public int getNoOfCores() {
		return noOfCores;
	}

	/**
	 * To set the no of cores.
	 * 
	 * @param noOfCores
	 */
	public void setNoOfCores(int noOfCores) {
		this.noOfCores = noOfCores;
	}

	/**
	 * To get the webServerSwitch.
	 * 
	 * @return String
	 */
	public String getWebServerSwitch() {
		return webServerSwitch;
	}

	/**
	 * To set the webServerSwitch.
	 * 
	 * @param webServerSwitch
	 */
	public void setWebServerSwitch(String webServerSwitch) {
		this.webServerSwitch = webServerSwitch;
	}

	/**
	 * 
	 * Gets the email address of the holder.
	 * 
	 * @return {@link String}
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Sets the email address of the holder.
	 * 
	 * @param emailAddress {@link String}
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * Gets the licence type description.
	 * 
	 * @return {@link String}
	 */
	public String getLicenseType() {
		return licenseType;
	}

	/**
	 * Sets the AnnualImageCount.
	 * 
	 * @param licenseType {@link String}
	 */
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	/**
	 * Gets the AnnualImageCount.
	 * 
	 * @return {@link String}
	 */
	public long getAnnualImageCount() {
		return AnnualImageCount;
	}

	/**
	 * Sets the license type description.
	 * 
	 * @param licenseType {@link String}
	 */
	public void setAnnualImageCount(long annualImageCount) {
		AnnualImageCount = annualImageCount;
	}
	
	public String getIssuerEmail() {
		return issuerEmail;
	}

	public void setIssuerEmail(String issuerEmail) {
		this.issuerEmail = issuerEmail;
	}
}
