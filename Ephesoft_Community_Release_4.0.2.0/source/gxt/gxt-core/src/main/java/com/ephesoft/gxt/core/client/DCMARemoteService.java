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

package com.ephesoft.gxt.core.client;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.core.common.AuthenticationType;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.DirtyCell;
import com.ephesoft.gxt.core.shared.dto.InitializeMetaData;
import com.ephesoft.gxt.core.shared.exception.UIException;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Service interface for the required functionality of the UI.
 * 
 * @author Ephesoft
 * @version 3.0
 *
 */
public interface DCMARemoteService extends RemoteService {

	/**
	 * API to acquire Lock on a batch class given it's identifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @throws GWTException
	 */
	void acquireLock(String batchClassIdentifier) throws UIException;

	/**
	 * API to initiate Remote Services on the application.
	 * 
	 * @throws Exception
	 */
	void initRemoteService() throws Exception;

	/**
	 * API to logout from remote services on the application.
	 */
	String logout(String url);

	/**
	 * API to clean up.
	 */
	void cleanup();

	/**
	 * API to clean up the current user of the current batch
	 * 
	 * @param batchIdentifier
	 */
	void cleanUpCurrentBatch(String batchIdentifier);

	/**
	 * API to get User Name using the remote services on the application.
	 * 
	 * @return {@link String}
	 */
	String getUserName();

	/**
	 * API to get roles of the user using the remote services on the application.
	 * 
	 * @return Set<{@link String}>
	 */
	Set<String> getUserRoles();

	/**
	 * API to get locale of the user using the remote services on the application.
	 * 
	 * @return {@link String}
	 */
	String getLocale();

	/**
	 * API to check if reporting is enabled.
	 * 
	 * @return {@link Boolean}
	 */
	Boolean isReportingEnabled();

	/**
	 * API to get all user's common name along with their full name.
	 * 
	 * @return {@link Map}<{@link String}, {@link String}> All user's common name along with their full name.
	 */
	Map<String, String> getAllUser();

	/**
	 * API to get all user groups.
	 * 
	 * @return Set<{@link String}>
	 */
	Set<String> getAllGroups();

	/**
	 * API to get All BatchClass By User Roles.
	 * 
	 * @return Set<{@link String}>
	 */
	Set<String> getAllBatchClassByUserRoles();

	/**
	 * API to check if Upload Batch is Enabled
	 * 
	 * @return {@link Boolean}
	 */
	Boolean isUploadBatchEnabled();

	/**
	 * API to get BatchList Priority Filter.
	 * 
	 * @return Map<{@link BatchInstanceStatus}, {@link Integer}>
	 */
	Map<BatchInstanceStatus, Integer> getBatchListPriorityFilter();

	/**
	 * API to set BatchList Priority Filter.
	 * 
	 * @param reviewBatchListPriority {@link Integer}
	 * @param validateBatchListPriority {@link Integer}
	 */
	void setBatchListPriorityFilter(Integer reviewBatchListPriority, Integer validateBatchListPriority);

	/**
	 * API to check if Restart All Batch is Enabled
	 * 
	 * @return {@link Boolean}
	 */
	Boolean isRestartAllBatchEnabled();

	/**
	 * API to get the batch list table row count.
	 * 
	 * @return {@link Integer}
	 */
	Integer getBatchListTableRowCount();

	/**
	 * API to get the batch list screen tab for user.
	 * 
	 * @return {@link BatchInstanceStatus}
	 */
	BatchInstanceStatus getBatchListScreenTab(String userName);

	/**
	 * API to set the batch list screen tab for user.
	 * 
	 * @param userName {@link String}
	 * @param batchDTOStatus {@link BatchInstanceStatus}
	 */
	void setBatchListScreenTab(String userName, BatchInstanceStatus batchDTOStatus);

	/**
	 * API to disable the restart all button for current session.
	 * 
	 * @return
	 */
	void disableRestartAllButton();

	/**
	 * API to get current user of a batch.
	 * 
	 * @param batchInstanceIdentifier
	 * @return
	 */
	String getCurrentUser(String batchInstanceIdentifier);

	/**
	 * API to validate a regex expression or match input with regular expession.
	 * 
	 * @param regex {@link String} - regular expression pattern for matching.
	 */
	Boolean validateRegEx(String regex);

	/**
	 * API to get the help url from properties file
	 * 
	 * @return helpUrl
	 * @throws GWTException
	 */
	String getHelpUrl() throws UIException;

	/**
	 * API to get selected batch class from session.
	 * 
	 * @param asyncCallback
	 */
	public String getBatchClassInfoFromSession();

	/**
	 * API to set the selected batch class info into session.
	 * 
	 * @param batchClassInfo
	 */
	public void setBatchClassInfoFromSession(String batchClassInfo);

	/**
	 * API to get the text and link values for the footer.
	 * 
	 * @return {@link Map}< {@link String}, {@link String}>
	 */
	Map<String, String> getFooterProperties();

	/**
	 * The <code>getUserType</code> method is used to get the Ephesoft Cloud user type.
	 * 
	 * @return Ephesoft {@link Integer} Cloud user type
	 */
	public Integer getUserType();

	/**
	 * API to check whether the user is super admin or not.
	 * 
	 * @return {@link Boolean}
	 */
	Boolean isSuperAdmin();

	/**
	 * Gets the user selected theme.
	 * 
	 * @return {@link String}-the selected theme name is returned
	 */
	String getSelectedTheme();

	/**
	 * Reads the application.properties file and returns the value of the property whose name is being passed.
	 * 
	 * @param propertyName {@link String} Name of the property whose value is to be read.
	 * @return {@link String} The value of the property given.
	 */
	String getPropertiesValue(String propertyName);

	/**
	 * Reads the application.properties file for the given propertyName,find its value and returns the array of string separated by the
	 * given split pattern.
	 * 
	 * @param propertyName {@link String} Name of the property whose value is to be read.
	 * @param splitPattern {@link String} the parameter on which input string should be split.
	 * @return {@link String[]} The array of strings computed by splitting this string.
	 */
	String[] getPropertiesValue(String propertyName, String splitPattern);

	/**
	 * Returns the attribute from session.
	 * 
	 * @param attribute {@link String}- attribute whose value has to be fetched from the session
	 * @return {@link String}- value of attribute from the session
	 */
	String getAttributeFromSession(String attribute);

	/**
	 * Sets the attribute into session.
	 * 
	 * @param attribute {@link String}-attribute whose value has to be stored in the session
	 * @param value {@link String}- value of the attribute
	 */
	void setAttributeInSession(String attribute, String value);

	/**
	 * Gets the authentication type configured through web.xml.
	 * 
	 * @return {@link AuthenticationType} default is return if configured type is not the from the suggested list.
	 */
	public Integer getAuthenticationType();

	/**
	 * Gets the full name of the user.
	 * 
	 * @return {@link String} Full name of the currently logged in User.
	 */
	public String getUserFullName();

	/**
	 * Gets the full name of all the users provided in the set.
	 * 
	 * @param commonNamesOfAllUsers {@link Set}<{@link String}> Common names of filtered users.
	 * @return {@link Map}<{@link String}, {@link String}>
	 */
	public Map<String, String> getFullNameOfFilteredUsers(Set<String> commonNamesOfAllUsers);

	public Boolean validateRegExPattern(String regex, String value);

	/**
	 * Initialize meta data.
	 *
	 * @return the initialize meta data
	 */
	public InitializeMetaData initializeMetaData();

	public List<ConnectionsDTO> getAllConnectionsDTO();

	/**
	 * API to get the not allowed characters in batch class name from properties file
	 * 
	 * @return String. String of not allowed characters in batch class name.
	 * @throws GWTException
	 */
	String getNotAllowedCharactersInBCName() throws Exception;

	public Map<String, List<DirtyCell>> validateRegexPattern(final Map<String, List<DirtyCell>> dirtyCellsMap);
}
