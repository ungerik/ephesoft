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

package com.ephesoft.dcma.util;

/**
 * The class specifies the information required to upload data on FTP server.
 * @author Ephesoft
 * @version 3.0
 *
 */
public class FTPInformation {
	
	/**
	 *   ftpServerURL.
	 */
	private String ftpServerURL;

	/**
	 *   ftpUserName.
	 */
	private String ftpUsername;

	/**
	 *   ftpPassword.
	 */
	private String ftpPassword;

	/**
	 *   uploadBaseDir.
	 */
	private String uploadBaseDir;

	/**
	 *   numberOfRetries.
	 */
	private int numberOfRetries;

	/**
	 *   ftpDataTimeOut.
	 */
	private String ftpDataTimeOut;
	
	/**
	 * sourceDirectoryPath.
	 */
	private String sourceDirectoryPath;
	
	/**
	 * destDirName.
	 */
	private String destDirName;
	
	/**
	 * numberOfRetryCounter.
	 */
	private int numberOfRetryCounter;

	public FTPInformation(final String ftpServerURL, final String ftpUsername, final String ftpPassword, final String uploadBaseDir, final int numberOfRetries,
			final String ftpDataTimeOut, final String sourceDirectoryPath, final String destDirName, final int numberOfRetryCounter) {
		super();
		this.ftpServerURL = ftpServerURL;
		this.ftpUsername = ftpUsername;
		this.ftpPassword = ftpPassword;
		this.uploadBaseDir = uploadBaseDir;
		this.numberOfRetries = numberOfRetries;
		this.ftpDataTimeOut = ftpDataTimeOut;
		this.sourceDirectoryPath = sourceDirectoryPath;
		this.destDirName = destDirName;
		this.numberOfRetryCounter = numberOfRetryCounter;
	}

	
	/**
	 * Getter for ftpServerURL.
	 * 
	 * @return the ftpServerURL.
	 */
	public String getFtpServerURL() {
		return ftpServerURL;
	}

	
	/**
	 * Getter for ftpUsername.
	 * 
	 * @return the ftpUsername.
	 */
	public String getFtpUsername() {
		return ftpUsername;
	}

	
	/**
	 * Getter for ftpPassword.
	 * 
	 * @return the ftpPassword.
	 */
	public String getFtpPassword() {
		return ftpPassword;
	}

	
	/**
	 * Getter for uploadBaseDir.
	 * 
	 * @return the uploadBaseDir.
	 */
	public String getUploadBaseDir() {
		return uploadBaseDir;
	}

	
	/**
	 * Getter for numberOfRetries.
	 * 
	 * @return the numberOfRetries.
	 */
	public int getNumberOfRetries() {
		return numberOfRetries;
	}

	
	/**
	 * Getter for ftpDataTimeOut.
	 * 
	 * @return the ftpDataTimeOut.
	 */
	public String getFtpDataTimeOut() {
		return ftpDataTimeOut;
	}

	
	/**
	 * Getter for sourceDirectoryPath.
	 * 
	 * @return the sourceDirectoryPath.
	 */
	public String getSourceDirectoryPath() {
		return sourceDirectoryPath;
	}

	
	/**
	 * Getter for destDirName.
	 * 
	 * @return the destDirName.
	 */
	public String getDestDirName() {
		return destDirName;
	}

	
	/**
	 * Getter for numberOfRetryCounter.
	 * 
	 * @return the numberOfRetryCounter.
	 */
	public int getNumberOfRetryCounter() {
		return numberOfRetryCounter;
	}

	
}
