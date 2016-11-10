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

package com.ephesoft.dcma.gwt.core.client;

import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DCMARemoteServiceAsync {

	/**
	 * API to acquire Lock on a batch class given it's identifier asynchronously.
	 * 
	 * @param batchIdentifier
	 * @param callback
	 */
	void acquireLock(String batchIdentifier, AsyncCallback<Void> callback);

	/**
	 * API to initiate Remote Services on the application asynchronously.
	 * 
	 * @param callback
	 */
	void initRemoteService(AsyncCallback<Void> callback);

	/**
	 * API to clean up asynchronously.
	 * 
	 * @param callback
	 */
	void cleanup(AsyncCallback<Void> callback);

	/**
	 * API to clean up the current user for the current batch
	 * 
	 * @param batchIdentifier
	 * @param callback
	 */
	void cleanUpCurrentBatch(String batchIdentifier, AsyncCallback<Void> callback);

	/**
	 * API to get User Name using the remote services on the application asynchronously.
	 * 
	 * @param callback
	 */
	void getUserName(AsyncCallback<String> callback);

	/**
	 * API to logout from remote services on the application asynchronously.
	 * 
	 * @param callback
	 * @param url
	 */
	void logout(String url, AsyncCallback<String> callback);

	/**
	 * API to get locale of the user using the remote services on the application asynchronously.
	 * 
	 * @param callback
	 */
	void getLocale(AsyncCallback<String> callback);

	/**
	 * API to get roles of the user using the remote services on the application asynchronously.
	 * 
	 * @param callback
	 */
	void getUserRoles(AsyncCallback<Set<String>> callback);

	/**
	 * API to check if reporting is enabled asynchronously.
	 * 
	 * @param callback
	 */
	void isReportingEnabled(AsyncCallback<Boolean> callback);

	/**
	 * API to get all user's along with their full name asynchronously.
	 * 
	 * @param callback
	 */
	void getAllUser(AsyncCallback<Map<String, String>> callback);

	/**
	 * API to get all user groups asynchronously.
	 * 
	 * @param callback
	 */
	void getAllGroups(AsyncCallback<Set<String>> callback);

	/**
	 * 
	 * API to get All BatchClass By User Roles asynchronously.
	 * 
	 * @param callback
	 */
	void getAllBatchClassByUserRoles(AsyncCallback<Set<String>> callback);

	/**
	 * API to check if Upload Batch is Enabled asynchronously.
	 * 
	 * @param callback
	 */
	void isUploadBatchEnabled(AsyncCallback<Boolean> callback);

	/**
	 * API to get BatchList Priority Filter asynchronously.
	 * 
	 * @param callback
	 */
	void getBatchListPriorityFilter(AsyncCallback<Map<BatchInstanceStatus, Integer>> callback);

	/**
	 * API to set BatchList Priority Filter asynchronously.
	 * 
	 * @param reviewBatchListPriority
	 * @param validateBatchListPriority
	 * @param callback
	 */
	void setBatchListPriorityFilter(Integer reviewBatchListPriority, Integer validateBatchListPriority, AsyncCallback<Void> callback);

	/**
	 * API to check if Restart All Batch is Enabled asynchronously.
	 * 
	 * @param callback
	 */
	void isRestartAllBatchEnabled(AsyncCallback<Boolean> callback);

	/**
	 * API to get the Batch List table row count asynchronously.
	 * 
	 * @param callback
	 */
	void getBatchListTableRowCount(AsyncCallback<Integer> callback);

	/**
	 * API to get the batch list screen tab asynchronously.
	 * 
	 * @param callback
	 */
	void getBatchListScreenTab(String userName, AsyncCallback<BatchInstanceStatus> callback);

	/**
	 * API to set the batch list screen tab asynchronously.
	 * 
	 * @param userName
	 * @param batchDTOStatus
	 * @param callback
	 */
	void setBatchListScreenTab(String userName, BatchInstanceStatus batchDTOStatus, AsyncCallback<Void> callback);

	/**
	 * API to disable the restart all button for current session.
	 * 
	 * @return
	 */
	void disableRestartAllButton(AsyncCallback<Void> callback);

	/**
	 * API to get current user of a batch.
	 * 
	 * @param batchInstanceIdentifier
	 * @param callback
	 */
	void getCurrentUser(String batchInstanceIdentifier, AsyncCallback<String> callback);

	/**
	 * API to validate a regex expression or match input with regular expession.
	 * 
	 * @param regex {@link String} - regular expression pattern for matching.
	 * @param callback {@link AsyncCallback<{@link boolean}>}.
	 */
	void validateRegEx(String regex, AsyncCallback<Boolean> callback);

	/**
	 * API to get the help url from properties file.
	 * 
	 * @return
	 * @throws GWTException
	 */
	void getHelpUrl(AsyncCallback<String> callback);

	/**
	 * API to get selected batch class from session.
	 * 
	 * @param asyncCallback
	 */
	public void getBatchClassInfoFromSession(AsyncCallback<String> asyncCallback);

	/**
	 * API to set the selected batch class info into session.
	 * 
	 * @param batchClassInfo
	 * @param callback
	 */
	public void setBatchClassInfoFromSession(String batchClassInfo, AsyncCallback<Void> callback);

	/**
	 * API to get the text and link values for the UI footer.
	 * 
	 * @return {@link Map}< {@link String}, {@link String}>
	 */
	void getFooterProperties(AsyncCallback<Map<String, String>> asyncCallback);

	/**
	 * The <code>getUserType</code> method is used to get the Ephesoft Cloud user type.
	 * 
	 * @param callback {@link AsyncCallback < {@link Integer} .
	 */
	void getUserType(AsyncCallback<Integer> callback);

	/**
	 * API to check whether the user is super admin or not.
	 * 
	 * @return {@link Boolean}
	 */
	void isSuperAdmin(AsyncCallback<Boolean> asyncCallback);

	/**
	 * Gets the user selected theme.
	 * 
	 * @param callback{@link AsyncCallback <{@link String}>}
	 */
	void getSelectedTheme(AsyncCallback<String> callback);

	/**
	 * Reads the application.properties file and returns the value of the property whose name is being passed.
	 * 
	 * @param propertyName {@link String} Name of the property whose value is to be read.
	 * @param callback{@link AsyncCallback <{@link String}>}.
	 */
	void getPropertiesValue(String propertyName, AsyncCallback<String> callback);

	/**
	 * Reads the application.properties file for the given propertyName,find its value and returns the array of string separated by the
	 * given split pattern.
	 * 
	 * @param propertyName {@link String} Name of the property whose value is to be read.
	 * @param splitPattern {@link String} the parameter on which input string should be split.
	 * @param callback{@link AsyncCallback <{@link String}>}.
	 */
	void getPropertiesValue(String propertyName, String splitPattern, AsyncCallback<String[]> callback);

	/**
	 * Returns the attribute from session.
	 * 
	 * @param attribute {@link String}- attribute whose value has to be fetched from the session
	 * @return {@link String}- value of attribute from the session
	 */
	void getAttributeFromSession(String attribute, AsyncCallback<String> asyncCallback);

	/**
	 * Sets the attribute into session.
	 * 
	 * @param attribute {@link String}-attribute whose value has to be stored in the session
	 * @param value {@link String}- value of the attribute
	 */
	void setAttributeInSession(String attribute, String value, AsyncCallback<Void> callback);

	/**
	 * Gets the authentication type configured through web.xml.
	 * 
	 * @param callback {@link AsyncCallback} instance for asynchronous call to server.
	 */
	void getAuthenticationType(AsyncCallback<Integer> callback);

	/**
	 * Gets the full name of the user.
	 * 
	 * @param callback {@link AsyncCallback}<{@link String}> instance for asynchronous call to server to get full name of the currently
	 *            logged in user.
	 */
	void getUserFullName(AsyncCallback<String> callback);

	/**
	 * Gets all the filtered users along with their full name asynchronously.
	 * 
	 * @param commonNamesOfAllUsers {@link Set}<{@link String}> Common names of filtered users.
	 * @param callback {@link AsyncCallback}<{@link Map}<{@link String},{@link String}>>
	 */
	void getFullNameOfFilteredUsers(Set<String> commonNamesOfAllUsers, AsyncCallback<Map<String, String>> callback);

	// Changes made to solve: (EPHESOFT-12858) : Validation check missing for batch class name.
	/**
	 * API to get not allowed characters in batch class from properties file.
	 * 
	 * @param asyncCallback
	 */
	void getNotAllowedCharactersInBCName(AsyncCallback<String> callback);

	/**
	 * Gets the email of the user.
	 * 
	 * @param callback {@link AsyncCallback}<{@link String}> instance for asynchronous call to server to get email of the currently
	 *            logged in user.
	 */
	void getUserEmail(AsyncCallback<String> callback);

}
