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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.exception.FTPDataDownloadException;
import com.ephesoft.dcma.core.exception.FTPDataUploadException;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FTPInformation;
import com.ephesoft.dcma.util.FTPUtil;

/**
 * This service is used to Upload/download data to/from an FTP Server.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.ftp.service.FTPService
 */
public class FTPServiceImpl implements FTPService {

	/**
	 * Initializing log.
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(FTPServiceImpl.class);

	/**
	 * Initializing.
	 */
	private final static String var_source_dir = "Source direcotry is null";

	/**
	 * Initializing.
	 */
	private final static String var_source_des = "Destination direcotry is null";

	/**
	 * Initializing ftpServerURL.
	 */
	private String ftpServerURL;

	/**
	 * Initializing ftpUserName.
	 */
	private String ftpUsername;

	/**
	 * Initializing ftpPassword.
	 */
	private String ftpPassword;

	/**
	 * Initializing uploadBaseDir.
	 */
	private String uploadBaseDir;

	/**
	 * Initializing numberOfRetries.
	 */
	private int numberOfRetries;

	/**
	 * Initializing ftpDataTimeOut.
	 */
	private String ftpDataTimeOut;

	/**
	 * @param ftpServerURL the ftpServerURL to set
	 */
	public final void setFtpServerURL(String ftpServerURL) {
		this.ftpServerURL = ftpServerURL;
	}

	/**
	 * @param ftpUsername the ftpUsername to set
	 */
	public final void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	/**
	 * @param ftpPassword the ftpPassword to set
	 */
	public final void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	/**
	 * @param uploadBaseDir the uploadBaseDir to set
	 */
	public final void setUploadBaseDir(String uploadBaseDir) {
		this.uploadBaseDir = uploadBaseDir;
	}

	/**
	 * @param numberOfRetries the numberOfRetries to set
	 */
	public final void setNumberOfRetries(int numberOfRetries) {
		this.numberOfRetries = numberOfRetries;
	}

	/**
	 * @param ftpDataTimeOut the ftpDataTimeOut to set
	 */
	public void setFtpDataTimeOut(String ftpDataTimeOut) {
		this.ftpDataTimeOut = ftpDataTimeOut;
	}

	/**
	 * API to download a particular directory from FTP Server.
	 * 
	 * @param sourceDirName {@link String} - Name of the source directory to be copied.
	 * @param destDirectoryPath {@link String} - Full path where directory need to be copied.
	 * @param retryCounter - Start with zero.
	 * @param isDeletedFTPServerSourceContent - set true for deleted the ftp server content.
	 * @throws FTPDataDownloadException if any error occurs while downloading the file.
	 */
	@Override
	public void downloadDirectory(final String sourceDirName, final String destDirectoryPath, final int numberOfRetryCounter,
			boolean isDeletedFTPServerSourceContent) throws FTPDataDownloadException {
		boolean isValid = true;
		if (sourceDirName == null) {
			isValid = false;
			LOGGER.error(var_source_dir);
			throw new FTPDataDownloadException(var_source_dir);
		}
		if (destDirectoryPath == null) {
			isValid = false;
			LOGGER.error(var_source_des);
			throw new FTPDataDownloadException(var_source_des);
		}
		if (isValid) {
			FTPClient client = new FTPClient();
			String outputFileName = null;
			File file = new File(destDirectoryPath);
			if (!file.exists()) {
				file.mkdir();
			}
			try {
				createConnection(client);
				int reply = client.getReplyCode();
				if (FTPReply.isPositiveCompletion(reply)) {
					LOGGER.info("Starting File Download...");
				} else {
					LOGGER.error("Invalid Connection to FTP server. Disconnecting from FTP server....");
					client.disconnect();
					isValid = false;
					throw new FTPDataDownloadException("Invalid Connection to FTP server. Disconnecting from FTP server....");
				}
				if (isValid) {
					client.setFileType(FTP.BINARY_FILE_TYPE);
					String ftpDirectory = EphesoftStringUtil.concatenate(uploadBaseDir, File.separator, sourceDirName);
					LOGGER.info("Downloading files from FTP server");
					try {
						FTPUtil.retrieveFiles(client, ftpDirectory, destDirectoryPath);
					} catch (IOException e) {
						int retryCounter = numberOfRetryCounter;
						LOGGER.info("Retrying download Attempt#-" + (retryCounter + 1));
						if (retryCounter < numberOfRetries) {
							retryCounter = retryCounter + 1;
							downloadDirectory(sourceDirName, destDirectoryPath, retryCounter, isDeletedFTPServerSourceContent);
						} else {
							LOGGER.error("Error in getting file from FTP server :" + outputFileName);
						}
					}
					if (isDeletedFTPServerSourceContent) {
						FTPUtil.deleteExistingFTPData(client, ftpDirectory, true);
					}
				}
			} catch (FileNotFoundException e) {
				LOGGER.error("Error in generating output Stream for file :" + outputFileName);
			} catch (SocketException e) {
				LOGGER.error("Could not connect to FTP Server-" + ftpServerURL + e);
			} catch (IOException e) {
				LOGGER.error("Could not connect to FTP Server-" + ftpServerURL + e);
			} finally {
				try {
					client.disconnect();
				} catch (IOException e) {
					LOGGER.error("Error in disconnecting from FTP server." + e);
				}
			}
		}
	}

	/**
	 * This API uploads a given Source Directory onto the FTP Server.
	 * 
	 * @param sourceDirectoryPath {@link String} - Full Path of the directory to be copied.
	 * @param destDirName {@link String} - Name of the directory to be created on FTP Server.
	 * @param retryCounter - Start with zero.
	 * @throws FTPDataUploadException if any exception occur in file uploading.
	 */
	@Override
	public void uploadDirectory(final String sourceDirectoryPath, final String destDirName, final int numberOfRetryCounter)
			throws FTPDataUploadException {
		try {
			FTPInformation ftpInformation = new FTPInformation(ftpServerURL, ftpUsername, ftpPassword, uploadBaseDir, numberOfRetries,
					ftpDataTimeOut, sourceDirectoryPath, destDirName, numberOfRetryCounter);
			FTPUtil.uploadDirectory(ftpInformation, true);
		} catch (com.ephesoft.dcma.util.exception.FTPDataUploadException e) {
			throw new FTPDataUploadException("Exception in uploading directory", e);
		} catch (SocketException e) {
			throw new FTPDataUploadException("Exception in making connection with ftp server", e);
		} catch (IOException e) {
			throw new FTPDataUploadException("IOException in method uploadDirectory", e);
		}
	}

	/**
	 * API for creating connection to ftp server.
	 * 
	 * @param client {@link FTPClient} the ftp client instance
	 * @throws SocketException if any error occur while making the connection.
	 * @throws IOException generate if any error occur while making the connection
	 */
	private void createConnection(FTPClient client) throws SocketException, IOException {
		client.connect(ftpServerURL);
		client.login(ftpUsername, ftpPassword);
		try {
			int ftpDataTimeOutLocal = Integer.parseInt(ftpDataTimeOut);
			client.setDataTimeout(ftpDataTimeOutLocal);
		} catch (NumberFormatException e) {
			LOGGER.error("Error occuring in converting ftpDataTimeOut :" + e.getMessage(), e);
		}
	}

}
