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

package com.ephesoft.gxt.foldermanager.client;

import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.shared.dto.FileWrapper;
import com.ephesoft.gxt.core.shared.dto.FolderManagerDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface for Async Folder Manager Service.
 * @author Ephesoft
 * 
 *         <b>created on</b> 13-Apr-2015 <br/>
 * @version 1.0.0 <br/>
 *          $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public interface FolderManagerServiceAsync extends DCMARemoteServiceAsync {

	/**
	 * API to get the contents of a file/folder
	 * 
	 * @param wrapper {@link FileWrapper}
	 * @param callback {@link AsyncCallback}<{@link List}<{@link FileWrapper}>>
	 */
	void getContents(FileWrapper wrapper, AsyncCallback<List<FileWrapper>> callback);

	/**
	 * API to get the contents of a file/folder by file path
	 * 
	 * @param path {@link String}
	 * @param callback {@link AsyncCallback}<{@link List}<{@link FolderManagerDTO}>>
	 */
	void getContents(String path, AsyncCallback<List<FolderManagerDTO>> callback);

	/**
	 * API to delete a single file/folder by path
	 * 
	 * @param absolutePath {@link String}
	 * @param callback {@link AsyncCallback}<{@link Boolean}>
	 */
	void deleteFile(String absolutePath, AsyncCallback<Boolean> callback);

	/**
	 * API to rename a file/folder
	 * 
	 * @param absolutePath {@link String}
	 * @param newName {@link String}
	 * @param folderPath {@link String}
	 * @param callback {@link AsyncCallback}<{@link Boolean}
	 */
	void renameFile(String absolutePath, String newName, String folderPath, AsyncCallback<Boolean> callback);

	/**
	 * API to delete all the files contained by a path
	 * 
	 * @param absolutePaths {@link List}<{@link String}>
	 * @param callback {@link AsyncCallback}<{@link String}>
	 */
	void deleteFiles(List<String> absolutePaths, AsyncCallback<String> callback);

	/**
	 * API to fetch the parent folder for folder management
	 * 
	 * @param asyncCallback {@link AsyncCallback}<{@link String}>
	 */
	void getParentFolder(AsyncCallback<String> asyncCallback);

	/**
	 * API to perform copy operation for selected files
	 * 
	 * @param copyFilesList {@link List}<{@link String}>
	 * @param currentFolderPath {@link String}
	 * @param asyncCallback {@link AsyncCallback}<{@link List}<{@link String}>>
	 */
	void copyFiles(List<String> copyFilesList, String currentFolderPath, AsyncCallback<List<String>> asyncCallback);

	/**
	 * API to perform cut operation for selected files
	 * 
	 * @param cutFilesList {@link List}<{@link String}>
	 * @param currentFolderPath {@link String}
	 * @param asyncCallback {@link AsyncCallback}<{@link List}<{@link String}>>
	 */
	void cutFiles(List<String> cutFilesList, String currentFolderPath, AsyncCallback<List<String>> asyncCallback);

	/**
	 * API to fetch base http url
	 * 
	 * @param asyncCallback {@link AsyncCallback}<{@link String}>
	 */
	void getBaseHttpURL(AsyncCallback<String> asyncCallback);

	/**
	 * API to create a new folder at the specified folder path
	 * 
	 * @param folderPath {@link String}
	 * @param asyncCallback {@link AsyncCallback}<{@link String}>
	 */
	void createNewFolder(String folderPath, AsyncCallback<String> asyncCallback);

	/**
	 * API to get all the batch classes available to a user
	 * 
	 * @param asyncCallback {@link AsyncCallback}<{@link Map}<{@link String},{@link String}>>
	 */
	void getBatchClassNames(AsyncCallback<Map<String, String>> asyncCallback);

}
