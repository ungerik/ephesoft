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

package com.ephesoft.dcma.ftp.service;

import com.ephesoft.dcma.core.exception.FTPDataDownloadException;
import com.ephesoft.dcma.core.exception.FTPDataUploadException;

/**
 * This service is used to Upload/download data to/from an FTP Server.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.ftp.service.FTPServiceImpl
 */
public interface FTPService {

	/**
	 * This API uploads a given Source Directory onto the FTP Server.
	 * 
	 * @param sourceDirectoryPath {@link String} - Full Path of the directory to be copied.
	 * @param destDirName {@link String} - Name of the directory to be created on FTP Server.
	 * @param retryCounter int - Start with zero.
	 * @throws FTPDataUploadException if any exception occur in file uploading.
	 */
	void uploadDirectory(String sourceDirectoryPath, String destDirName, int retryCounter) throws FTPDataUploadException;

	/**
	 * API to download a particular directory from FTP Server.
	 * 
	 * @param sourceDirName {@link String} - Name of the source directory to be copied.
	 * @param destDirectoryPath {@link String} - Full path where directory need to be copied.
	 * @param retryCounter int- Start with zero. 
	 * @param isDeletedFTPServerSourceContent boolean - set true for deleted the ftp server content.
	 * @throws FTPDataDownloadException if any exception occurs in file downloading.
	 */
	void downloadDirectory(String sourceDirName, String destDirectoryPath, int retryCounter, boolean isDeletedFTPServerSourceContent) throws FTPDataDownloadException;

}
