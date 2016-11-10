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

package com.ephesoft.gxt.systemconfig.client;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.ConnectionsResult;
import com.ephesoft.gxt.core.shared.dto.ImportPoolDTO;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.core.shared.dto.RegexGroupDTO;
import com.ephesoft.gxt.core.shared.dto.RegexPatternDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous service class to import/export regex pool, import/export table column pool, get list of regex groups and table column
 * pool, get license details.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 13-Apr-2015 <br/>
 * @version 1.0.0 <br/>
 *          $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see com.ephesoft.dcma.gwt.theme.client.ThemeServiceAsync
 */
public interface SystemConfigServiceAsync extends DCMARemoteServiceAsync {

	/**
	 * Adds New Plugin
	 * 
	 * @param pluginName {@link String}
	 * @param xmlSourcePath {@link String}
	 * @param jarSourcePath {@link String}
	 * @param pluginAdded {@link AsyncCallback}<{@link Boolean}>
	 */
	void addNewPlugin(String pluginName, String xmlSourcePath, String jarSourcePath, AsyncCallback<Boolean> pluginAdded);

	/**
	 * Gets all the Plugin details DTO's
	 * 
	 * @param callback {@link AsyncCallback}<{@link List}<{@link PluginDetailsDTO}>>
	 */
	void getAllPluginDetailDTOs(AsyncCallback<List<PluginDetailsDTO>> callback);

	/**
	 * Updates all Plugin Details DTO
	 * 
	 * @param allPluginDetailsDTO {@link List}<{@link PluginDetailsDTO}>
	 * @param callback {@link AsyncCallback}<{@link List}<{@link PluginDetailsDTO}>>
	 */
	void updateAllPluginDetailsDTOs(List<PluginDetailsDTO> allPluginDetailsDTO, AsyncCallback<List<PluginDetailsDTO>> callback);

	/**
	 * @param pluginDetailsDTO {@link PluginDetailsDTO}
	 * @param callback {@link AsyncCallback}<{@link Boolean}>
	 */
	void deletePlugin(PluginDetailsDTO pluginDetailsDTO, AsyncCallback<Boolean> callback);

	/**
	 * Checks whether the application key is already generated or not.
	 * 
	 * @param callback {@link AsyncCallback}<{@link Boolean}>
	 */
	void isApplicationKeyGenerated(final AsyncCallback<Boolean> callback);

	/**
	 * Generates the application level key using the key as received from the user. This Key might not be generated in the form as it
	 * is.
	 * 
	 * @param applicationKey {@link String} application key provided by the client which will be digested to generate the actual key
	 *            for encryption purpose
	 * @param callback {@link AsyncCallback} which makes the rpc call with the server.
	 */
	void generateApplicationLevelKey(final String applicationKey, final AsyncCallback<Boolean> callback);

	// Below method not used yet.
	/**
	 * Gets the list of all regex groups.
	 * 
	 * @param asyncCallback {@link AsyncCallback}<{@link Collection}<{@link RegexGroupDTO}>>
	 * 
	 */
	void getRegexGroupDTOList(AsyncCallback<Collection<RegexGroupDTO>> asyncCallback);

	/**
	 * Imports new regex groups, which are not already present in database.
	 * 
	 * @param importRegexPoolDTO {@link ImportRegexPoolDTO}
	 * @param asyncCallback {@link AsyncCallback}<{@link Map}<{@link String},{@link Boolean}>>
	 */
	void importRegexGroups(List<ImportPoolDTO> importRegexPoolDTO, AsyncCallback<Map<String, Boolean>> asyncCallback);

	/**
	 * Returns the license details like license expiry date, CPU license count, etc.
	 * 
	 * @param asyncCallback {@link AsyncCallback}<{@link Map}<{@link String},{@link String}>>
	 */
	void getLicenseDetails(AsyncCallback<Map<String, String>> asyncCallback);

	/**
	 * Stores all the updated connections DTOs in db and returns the updated list.
	 * 
	 * @param allConnectionsDTO {@link List}<{@link ConnectionsDTO}>
	 * @param callback {@link AsyncCallback}<{@link List}<{@link ConnectionsDTO}>>
	 */
	void updateAllConnectionsDTOs(List<ConnectionsDTO> allConnectionsDTO, AsyncCallback<List<ConnectionsDTO>> callback);

	/**
	 * Tests DB connection using the provided parameters.
	 * 
	 * @param connectionURL {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param driverClass {@link String}
	 * @param asyncCallback {@link AsyncCallback }<{@link Boolean}>
	 */
	void testConnection(String connectionURL, String userName, String password, String driverClass,
			AsyncCallback<Boolean> asyncCallback);

	/**
	 * Removes selected connection from DB.
	 * 
	 * @param listOfSelectedConnectionDTO {@link ConnectionsDTO}
	 * @param asyncCallback {@link AsyncCallback }<{@link ConnectionsResult}>
	 */
	void removeSelectedConnection(List<ConnectionsDTO> listOfSelectedConnectionDTO, AsyncCallback<ConnectionsResult> asyncCallback);

	/**
	 * Finds all the matches inside the string for the given regex pattern and returns the list of matched indexes.
	 * 
	 * @param regex {@link String} The regex pattern generated.
	 * @param strToBeMatched {@link String} The string which is to be matched.
	 * @param asyncCallback {@link AsyncCallback }<{@link List}<{@link String}>>
	 */
	void findMatchedIndexesList(String regex, String strToBeMatched, AsyncCallback<List<String>> asyncCallback);

	/**
	 * getter for getting the map of available RegexGroups from Db
	 * 
	 * @param asyncCallback {@link AsyncCallback}<{@link Map}<{@link String}, {@link RegexGroupDTO }>>
	 */
	void getRegexGroupMap(AsyncCallback<Map<String, RegexGroupDTO>> asyncCallback);

	/**
	 * update selected RegexGroupDTO into DB.
	 * 
	 * @param editedRegexGroup {@link RegexGroupDTO}
	 * @param asyncCallback {@link AsyncCallback}<{@link Integer}
	 */
	void updateRegexGroup(RegexGroupDTO editedRegexGroup, AsyncCallback<Integer> asyncCallback);

	/**
	 * update selected RegexPatternDTO into DB.
	 * 
	 * @param editedRegexPattern {@link RegexPatternDTO}
	 * @param asyncCallback {@link AsyncCallback}<{@link Integer}
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 */
	void updateRegexPattern(RegexPatternDTO editedRegexPattern, RegexGroupDTO regexGroupDTO, AsyncCallback<Integer> asyncCallback);

	/**
	 * Removes selected list of RegexPatternDTO from DB.
	 * 
	 * @param regexPatternDTOList {@link List}<{@link RegexPatternDTO}>
	 * @param asyncCallback {@link AsyncCallback}<{@link Boolean}
	 */
	void deleteRegexPatterns(List<RegexPatternDTO> regexPatternDTOList, AsyncCallback<Boolean> asyncCallback);

	/**
	 * Removes selected list of RegexGroupDTO from DB.
	 * 
	 * @param regexGroupDTOList {@link List}<{@link RegexGroupDTO}>
	 * @param asyncCallback {@link AsyncCallback}<{@link Boolean}
	 */
	void deleteRegexGroups(List<RegexGroupDTO> regexGroupDTOList, AsyncCallback<Boolean> asyncCallback);

	/**
	 * insert selected RegexGroupDTO into DB.
	 * 
	 * @param regexGroup {@link RegexGroupDTO}
	 * @param asyncCallback {@link AsyncCallback}<{@link Boolean}>
	 */
	void insertRegexGroup(RegexGroupDTO regexGroup, AsyncCallback<Boolean> asyncCallback);

	/**
	 * insert selected RegexPatternDTO into DB.
	 * 
	 * @param regexPattern {@link RegexPatternDTO}
	 * @param asyncCallback {@link AsyncCallback}<{@link Boolean}
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 */
	void insertRegexPattern(RegexPatternDTO regexPattern, RegexGroupDTO regexGroupDTO, AsyncCallback<Boolean> asyncCallback);

	/**
	 * Tests MSSQL always ON connection
	 * 
	 * @param databaseName {@link String}
	 * @param connectionURL {@link String}
	 * @param asyncCallback {@link AsyncCallback}<{@link Boolean}>
	 */
	void testMSSQLAlwaysONConnection(String databaseName, String connectionURL, AsyncCallback<Boolean> asyncCallback);
}
