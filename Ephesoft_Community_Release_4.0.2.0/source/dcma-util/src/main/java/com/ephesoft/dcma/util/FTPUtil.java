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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.util.exception.FTPDataUploadException;
import com.ephesoft.dcma.util.FTPInformation;

/**
 * This class is a utility file consisting of various APIs related to different functions that can be performed on a FTP server.
 * 
 * @author Ephesoft
 * @version 3.0
 * 
 */
public class FTPUtil {

	/**
	 * Initializing.
	 */
	private static final String VAR_SOURCE_DIR = "Source direcotry is null";

	/**
	 * Initializing.
	 */
	private static final String VAR_SOURCE_DES = "Destination direcotry is null";

	/**
	 * Initializing log.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FTPUtil.class);

	/** Constant for directory separator. */
	private static final String DIRECTORY_SEPARATOR = "\\\\";

	/**
	 * API for creating connection to ftp server.
	 * 
	 * @param client {@link FTPClient} the ftp client instance
	 * @throws SocketException generate if any error occurs while making the connection.
	 * @throws IOException generate if any error occur while making the connection.
	 */
	public static void createConnection(final FTPClient client, final String ftpServerURL, final String ftpUsername,
			final String ftpPassword, final String ftpDataTimeOut) throws SocketException, IOException {
		client.connect(ftpServerURL);
		client.login(ftpUsername, ftpPassword);
		try {
			int ftpDataTimeOutLocal = Integer.parseInt(ftpDataTimeOut);
			client.setDataTimeout(ftpDataTimeOutLocal);
		} catch (NumberFormatException e) {
			LOGGER.error(EphesoftStringUtil.concatenate("Error occuring in converting ftpDataTimeOut :", e.getMessage(), e));
		}
	}

	/**
	 * API for uploading a directory on FTP server. Uploading any file requires uploading following a directory structure.
	 * 
	 * @param sourceDirectoryPath the directory to be uploaded
	 * @param destDirName the path on ftp where directory will be uploaded
	 * @param numberOfRetryCounter number of attempts to be made for uploading the directory
	 * @throws FTPDataUploadException if an error occurs while uploading the file
	 * @throws IOException if an error occurs while making the ftp connection
	 * @throws SocketException if an error occurs while making the ftp connection
	 */
	public static void uploadDirectory(final FTPInformation ftpInformation, boolean deleteExistingFTPData)
			throws FTPDataUploadException, SocketException, IOException {
		boolean isValid = true;
		if (ftpInformation.getSourceDirectoryPath() == null) {
			isValid = false;
			LOGGER.error(VAR_SOURCE_DIR);
			throw new FTPDataUploadException(VAR_SOURCE_DIR);
		}
		if (ftpInformation.getDestDirName() == null) {
			isValid = false;
			LOGGER.error(VAR_SOURCE_DES);
			throw new FTPDataUploadException(VAR_SOURCE_DES);
		}
		if (isValid) {
			FTPClient client = new FTPClient();
			String destDirName = ftpInformation.getDestDirName();
			String destinationDirectory = ftpInformation.getUploadBaseDir();
			if (destDirName != null && !destDirName.isEmpty()) {
				String uploadBaseDir = ftpInformation.getUploadBaseDir();
				if (uploadBaseDir != null && !uploadBaseDir.isEmpty()) {
					destinationDirectory = EphesoftStringUtil.concatenate(uploadBaseDir, File.separator, destDirName);
				} else {
					destinationDirectory = destDirName;
				}

			}
			FileInputStream fis = null;
			try {
				createConnection(client, ftpInformation.getFtpServerURL(), ftpInformation.getFtpUsername(),
						ftpInformation.getFtpPassword(), ftpInformation.getFtpDataTimeOut());

				int reply = client.getReplyCode();
				if (FTPReply.isPositiveCompletion(reply)) {
					LOGGER.info("Starting File Upload...");
				} else {
					LOGGER.info("Invalid Connection to FTP server. Disconnecting Client");
					client.disconnect();
					isValid = false;
					throw new FTPDataUploadException("Invalid Connection to FTP server. Disconnecting Client");
				}
				if (isValid) {

					// code changed for keeping the control connection busy.
					client.setControlKeepAliveTimeout(300);
					client.setFileType(FTP.BINARY_FILE_TYPE);
					createFtpDirectoryTree(client, destinationDirectory);
					// client.makeDirectory(destinationDirectory);
					if (deleteExistingFTPData) {
						deleteExistingFTPData(client, destinationDirectory, deleteExistingFTPData);
					}
					File file = new File(ftpInformation.getSourceDirectoryPath());
					if (file.isDirectory()) {
						String[] fileList = file.list();
						for (String fileName : fileList) {
							String inputFile = EphesoftStringUtil.concatenate(ftpInformation.getSourceDirectoryPath(), File.separator,
									fileName);
							File checkFile = new File(inputFile);
							if (checkFile.isFile()) {
								LOGGER.info(EphesoftStringUtil.concatenate("Transferring file :", fileName));
								fis = new FileInputStream(inputFile);
								try {
									client.storeFile(fileName, fis);
								} catch (IOException e) {
									int retryCounter = ftpInformation.getNumberOfRetryCounter();
									LOGGER.info(EphesoftStringUtil.concatenate("Retrying upload Attempt#-", (retryCounter + 1)));
									if (retryCounter < ftpInformation.getNumberOfRetries()) {
										retryCounter = retryCounter + 1;
										uploadDirectory(ftpInformation, deleteExistingFTPData);
									} else {
										LOGGER.error("Error in uploading the file to FTP server");
									}
								} finally {
									try {
										if (fis != null) {
											fis.close();
										}
									} catch (IOException e) {
										LOGGER.info(EphesoftStringUtil.concatenate("Could not close stream for file.", inputFile));
									}
								}
							}
						}
					}
				}
			} catch (FileNotFoundException e) {
				LOGGER.error("File does not exist..");
			} finally {
				try {
					client.disconnect();
				} catch (IOException e) {
					LOGGER.error("Disconnecting from FTP server....", e);
				}
			}
		}
	}

	/**
	 * API to create an arbitrary directory hierarchy on the remote ftp server
	 * 
	 * @param client {@link FTPClient} the ftp client instance.
	 * @param dirTree the directory tree only delimited with / chars.
	 * @throws IOException if a [problem occurs while making the directory or traversing down the directory structure.
	 */
	private static void createFtpDirectoryTree(final FTPClient client, final String dirTree) throws IOException {

		boolean dirExists = true;

		/*
		 * tokenize the string and attempt to change into each directory level. If you cannot, then start creating. Needs to be updated
		 * with a proper regex for being the system independent working. NOTE: Cannot use File.seperator here as for windows its value
		 * is "\", which is a special character in terms of regex.
		 */
		String[] directories = dirTree.split(DIRECTORY_SEPARATOR);
		for (String dir : directories) {
			if (!dir.isEmpty()) {
				if (dirExists) {
					dirExists = client.changeWorkingDirectory(dir);
				}
				if (!dirExists) {
					if (!client.makeDirectory(dir)) {
						throw new IOException(EphesoftStringUtil.concatenate("Unable to create remote directory :", dir, "\t error :",
								client.getReplyString()));
					}
					if (!client.changeWorkingDirectory(dir)) {
						throw new IOException(EphesoftStringUtil.concatenate("Unable to change into newly created remote directory :",
								dir, "\t  error :", client.getReplyString()));
					}
				}
			}
		}
	}

	/**
	 * API for deleting the data existing in ftp directory on the ftp server.
	 * 
	 * @param client {@link FTPClient}
	 * @param ftpDirectory {@link String}
	 * @param isSourceFolderDeleted
	 */
	public static void deleteExistingFTPData(final FTPClient client, final String ftpDirectory, final boolean isSourceFolderDeleted) {
		if (client != null && ftpDirectory != null) {
			try {
				FTPFile[] ftpFileList = client.listFiles(ftpDirectory);
				for (FTPFile ftpFile : ftpFileList) {
					client.deleteFile(EphesoftStringUtil.concatenate(ftpDirectory, File.separator, ftpFile.getName()));
				}
				if (isSourceFolderDeleted) {
					client.removeDirectory(ftpDirectory);
				}
			} catch (IOException e) {
				LOGGER.error(EphesoftStringUtil.concatenate("Error in deleting existing file on ftp server ", e.getMessage(), e));
			}
		}
	}

	/**
	 * API to download files from an arbitrary directory hierarchy on the remote ftp server
	 * 
	 * @param client {@link FTPClient}
	 * @param ftpDirectory{{@link String} the directory tree only delimited with / chars. No file name!
	 * @param destDirectoryPath {@link String} the path where file will be downloaded.
	 * @throws Exception
	 */
	public static void retrieveFiles(final FTPClient client, final String ftpDirectory, final String destDirectoryPath)
			throws IOException {
		boolean dirExists = true;
		String[] directories = ftpDirectory.split(DIRECTORY_SEPARATOR);
		for (String dir : directories) {
			if (!dir.isEmpty()) {
				if (dirExists) {
					dirExists = client.changeWorkingDirectory(dir);
				}
			}
		}
		FTPFile[] fileList = client.listFiles();
		if (fileList != null && fileList.length > 0) {
			String outputFilePath;
			FileOutputStream fileOutputStream;
			for (FTPFile file : fileList) {
				outputFilePath = EphesoftStringUtil.concatenate(destDirectoryPath, File.separator, file.getName());
				fileOutputStream = new FileOutputStream(outputFilePath);
				client.retrieveFile(file.getName(), fileOutputStream);
				fileOutputStream.close();
			}
		}
	}

}
