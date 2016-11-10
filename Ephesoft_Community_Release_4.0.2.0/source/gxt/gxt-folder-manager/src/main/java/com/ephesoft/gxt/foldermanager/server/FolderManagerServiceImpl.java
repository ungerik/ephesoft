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

package com.ephesoft.gxt.foldermanager.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.gxt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.FileWrapper;
import com.ephesoft.gxt.core.shared.dto.FolderManagerDTO;
import com.ephesoft.gxt.core.shared.exception.UIException;
import com.ephesoft.gxt.foldermanager.client.FolderManagerService;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementConstants;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementMessages;

public class FolderManagerServiceImpl extends DCMARemoteServiceServlet implements FolderManagerService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * batchClassList- List to store batch class identifiers.
	 */
	private List<BatchClass> batchClassList = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(FolderManagerServiceImpl.class);

	private static Date folderDate(long dateLong) {
		Date date = new Date(dateLong);
		return date;
	}

	@Override
	public List<FileWrapper> getContents(FileWrapper file) throws UIException {
		List<FileWrapper> fileWrapperList = null;
		File fsFile = new File(file.getPath());
		if (!fsFile.exists()) {
			LOGGER.info("File selected is not present in the system:" + fsFile.getAbsolutePath());
			throw new UIException(FolderManagementConstants.ERROR_TYPE_1);
		}
		if (fsFile.isDirectory()) {
			fileWrapperList = this.buildFilesList(fsFile.listFiles());
		}
		return fileWrapperList;
	}

	private List<FileWrapper> buildFilesList(File[] files) {
		List<FileWrapper> result = new ArrayList<FileWrapper>();
		Set<String> hiddenFolderPaths = null;
		if (!isSuperAdmin()) {
			hiddenFolderPaths = getSetOfHiddenFolderPaths();
		}
		for (int i = 0; i < files.length; i++) {
			if (hiddenFolderPaths == null || hiddenFolderPaths.isEmpty() || !hiddenFolderPaths.contains(files[i].getAbsolutePath())) {
				FileWrapper fileWrapper = new FileWrapper(files[i].getAbsolutePath(), files[i].getName(),
						folderDate(files[i].lastModified()), files[i].lastModified());
				if (files[i].isFile()) {
					long fileSize = files[i].length();
					if (fileSize != 0) {
						float size = fileSize;
						fileWrapper.setFileSize(size / 1024);
					}
				}
				if (files[i].isDirectory()) {
					File[] allFiles = files[i].listFiles();
					fileWrapper.setIsDirectory();
					boolean isSubfolderContained = false;
					for (File file : allFiles) {
						if (file.isDirectory()) {
							isSubfolderContained = true;
							break;
						}
					}
					fileWrapper.setSubFolderContained(isSubfolderContained);
				}
				result.add(fileWrapper);
			}
		}

		return result;
	}

	// private String fetchNumberOfFiles(File[] allFiles) {
	// String noOfFiles = null;
	// int files = 0;
	// int directory = 0;
	// if (allFiles.length == 0) {
	// noOfFiles = "Empty Folder";
	// } else {
	// for (File file : allFiles) {
	// if (file.isFile()) {
	// files++;
	// } else {
	// if (file.isDirectory()) {
	// directory++;
	// }
	// }
	// }
	// noOfFiles = files + "  files" + "  " + directory + "  folders";
	// }
	// return noOfFiles;
	//
	// }

	/**
	 * To get collection of folder paths that should be hidden for normal user.
	 * 
	 * @return Set<String>
	 */
	private Set<String> getSetOfHiddenFolderPaths() {
		final List<String> hiddenFolderNames = getListOfHiddenFolderNames();
		final Set<String> hiddenFolderPaths = new HashSet<String>();
		final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		final String baseFolderPath = batchSchemaService.getBaseFolderLocation();
		String identifier;
		String path;
		for (String folderName : hiddenFolderNames) {
			for (BatchClass batchClass : batchClassList) {
				identifier = batchClass.getIdentifier();
				path = baseFolderPath + "\\" + identifier + "\\" + folderName;
				hiddenFolderPaths.add(path);
			}
		}

		return hiddenFolderPaths;
	}

	/**
	 * To get list of folder names that should be hidden for normal user.
	 * 
	 * @return List<String>
	 */
	private List<String> getListOfHiddenFolderNames() {
		final List<String> hiddenFolderNames = new ArrayList<String>();
		String folderNames = FolderManagementConstants.EMPTY_STRING;
		try {
			folderNames = ApplicationConfigProperties.getApplicationConfigProperties().getProperty(
					FolderManagementConstants.HIDDEN_FOLDERS_NAMES);
		} catch (IOException e) {
			LOGGER.error("Input ouput exception while reading the property: hidden_folders_names");
		}

		if (folderNames != null) {
			final StringTokenizer stringTokenizer = new StringTokenizer(folderNames, FolderManagementConstants.DELIMITER);
			while (stringTokenizer.hasMoreTokens()) {
				hiddenFolderNames.add(stringTokenizer.nextToken());
			}
		} else {
			LOGGER.error("Property: hidden_folders_names is missing from application.properties file");
		}
		return hiddenFolderNames;
	}

	@Override
	public List<FolderManagerDTO> getContents(String file) throws UIException {
		List<FileWrapper> result = this.getContents(new FileWrapper(file));
		List<FolderManagerDTO> folderManagerDTOsList = convertFileWrapperToFolderManagerDTO(result);
		return folderManagerDTOsList;
	}

	private List<FolderManagerDTO> convertFileWrapperToFolderManagerDTO(List<FileWrapper> folderContents) {
		List<FolderManagerDTO> folderManagerDTOsList = new ArrayList<FolderManagerDTO>();
		for (FileWrapper folder : folderContents) {
			FolderManagerDTO dto = new FolderManagerDTO();
			dto.setPath(folder.getPath());
			dto.setFileName(folder.getName());
			dto.setModifiedAt(folder.getModified());
			dto.setKind(folder.getKind());
			dto.setId(RandomIdGenerator.getIdentifier());
			dto.setSubFolderContained(folder.isSubFolderContained());
			dto.setSize(folder.getFileSize());
			folderManagerDTOsList.add(dto);
		}

		return folderManagerDTOsList;
	}

	@Override
	public Boolean deleteFile(String absoluteName) {
		boolean deleteSuccessfull = true;
		File file = new File(absoluteName);
		try {
			if (file.isDirectory()) {
				FileUtils.deleteDirectory(file);
			} else {
				deleteSuccessfull = file.delete();
			}
		} catch (IOException e) {
			deleteSuccessfull = false;
		}

		return deleteSuccessfull;
	}

	@Override
	public Boolean renameFile(String oldName, String newName, String folderPath) {
		File orignalFile = new File(folderPath + File.separator + oldName);
		File renamedFile = new File(folderPath + File.separator + newName);
		if (renamedFile.exists()) {
			return false;
		}
		return orignalFile.renameTo(renamedFile);
	}

	@Override
	public String deleteFiles(List<String> absolutePaths) {
		StringBuffer returnStringBuffer = new StringBuffer();
		for (String absolutePath : absolutePaths) {
			if (!deleteFile(absolutePath)) {
				returnStringBuffer.append(absolutePath + FolderManagementConstants.SEMI_COLON);
			}
		}
		return returnStringBuffer.toString();
	}

	@Override
	public String getParentFolder() {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		return batchSchemaService.getBaseFolderLocation();
	}

	@Override
	public List<String> copyFiles(List<String> copyFilesList, String currentFolderPath) throws UIException {
		List<String> resultList = new ArrayList<String>();
		for (String filePath : copyFilesList) {
			try {
				File srcFile = new File(filePath);
				if (srcFile.exists()) {
					String fileName = srcFile.getName();
					File copyFile = new File(currentFolderPath + File.separator + fileName);
					if (copyFile.exists()) {
						if (copyFile.equals(srcFile)) {
							int counter = 0;
							while (copyFile.exists()) {
								String newFileName;
								if (counter == 0) {
									newFileName = FolderManagementConstants.COPY + FolderManagementConstants.SPACE
											+ FolderManagementConstants.OF + FolderManagementConstants.SPACE + fileName;
								} else {
									newFileName = FolderManagementConstants.COPY + FolderManagementConstants.SPACE
											+ FolderManagementConstants.OPENING_BRACKET + counter
											+ FolderManagementConstants.CLOSING_BRACKET + FolderManagementConstants.SPACE
											+ FolderManagementConstants.OF + FolderManagementConstants.SPACE + fileName;
								}
								counter++;
								copyFile = new File(currentFolderPath + File.separator + newFileName);
							}
						} else {
							resultList.add(fileName);
						}
					}
					if (srcFile.isFile()) {
						FileUtils.copyFile(srcFile, copyFile, false);
					} else {
						FileUtils.forceMkdir(copyFile);
						FileUtils.copyDirectory(srcFile, copyFile, false);
					}
				} else {
					resultList.add(srcFile.getName());
				}
			} catch (IOException e) {
				throw new UIException(
						FolderManagementMessages.EXCEPTION_OCCURRED_WHILE_COPY_PASTE_OPERATION_COULD_NOT_COMPLETE_OPERATION);
			}
		}
		return resultList;
	}

	@Override
	public List<String> cutFiles(List<String> cutFilesList, String currentFolderPath) throws UIException {
		List<String> resultList = new ArrayList<String>();
		for (String filePath : cutFilesList) {
			File srcFile = new File(filePath);
			String fileName = srcFile.getName();
			if (srcFile.exists()) {
				try {
					String newPathName = currentFolderPath + File.separator + srcFile.getName();
					File newFile = new File(newPathName);
					if (!newFile.exists()) {
						if (srcFile.isFile()) {
							FileUtils.moveFile(srcFile, newFile);
						} else {
							FileUtils.moveDirectory(srcFile, newFile);
						}
					} else {
						resultList.add(fileName);
						// throw new UIException(FolderManagementMessages.CANNOT_COMPLETE_CUT_PASTE_OPERATION_AS_THE_FILE_FOLDER
						// + fileName + FolderManagementMessages.ALREADY_EXISTS);
					}
				} catch (IOException e) {
					throw new UIException(
							FolderManagementMessages.EXCEPTION_OCCURRED_WHILE_CUT_PASTE_OPERATION_COULD_NOT_COMPLETE_OPERATION);
				}
			} else {
				resultList.add(srcFile.getName());
				// throw new UIException(FolderManagementMessages.EXCEPTION_OCCURRED_WHILE_CUT_PASTE_OPERATION_FILE_NOT_FOUND
				// + FolderManagementConstants.QUOTES + srcFile.getName() + FolderManagementConstants.QUOTES);
			}
		}
		return resultList;
	}

	@Override
	public String getBaseHttpURL() {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		return batchSchemaService.getBaseHttpURL();
	}

	@Override
	public String createNewFolder(String folderPath) throws UIException {
		String folderName = FolderManagementConstants.NEW_FOLDER;
		File folder = new File(folderPath + File.separator + folderName);
		int counter = 0;
		while (folder.exists()) {
			String newFolderName = folderName + FolderManagementConstants.OPENING_BRACKET + ++counter
					+ FolderManagementConstants.CLOSING_BRACKET;
			folder = new File(folderPath + File.separator + newFolderName);
		}
		String name = folder.getName();
		try {
			FileUtils.forceMkdir(folder);
		} catch (IOException e) {
			throw new UIException(FolderManagementMessages.PROBLEM_OCCURRED_WHILE_CREATING_THE_FOLDER + name);
		}
		return name;
	}

	@Override
	public Map<String, String> getBatchClassNames() {
		Map<String, String> list = new LinkedHashMap<String, String>();
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		Set<String> allGroups = getUserRoles();
		if (isSuperAdmin()) {
			String parentFolder = getParentFolder();
			File file = new File(parentFolder);
			String name = file.getName();
			list.put(name, null);
		}
		if (null != allGroups) {
			batchClassList = batchClassService.getAllBatchClassesByUserRoles(allGroups);
			if (null != batchClassList) {
				for (BatchClass batchClass : batchClassList) {
					String identifier = batchClass.getIdentifier();
					String description = batchClass.getDescription();
					if (description.length() > 30) {
						description = description.substring(0, 30);
					}
					list.put(identifier + " - " + description, identifier);
				}
			}
		}
		return list;
	}

}
