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

import com.ephesoft.gxt.core.client.DCMARemoteService;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.ConnectionsResult;
import com.ephesoft.gxt.core.shared.dto.ImportPoolDTO;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.core.shared.dto.RegexGroupDTO;
import com.ephesoft.gxt.core.shared.dto.RegexPatternDTO;
import com.ephesoft.gxt.core.shared.exception.UIException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service class to import/export regex pool, import/export table column pool, get list of regex groups and table column pool, get
 * license details.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Sep 6, 2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * 
 * @see com.ephesoft.dcma.gwt.theme.client.ThemeService
 */
@RemoteServiceRelativePath("systemConfigService")
public interface SystemConfigService extends DCMARemoteService {

	/**
	 * API to add new plugin given the new plugin name, plugin xml path and plugin jar path
	 * 
	 * @param pluginName {@link String}
	 * @param xmlSourcePath {@link String}
	 * @param jarSourcePath {@link String}
	 * @return {@link Boolean} if plugin is added successfully or not
	 * @throws UIException
	 */
	public Boolean addNewPlugin(String pluginName, String xmlSourcePath, String jarSourcePath) throws UIException;

	/**
	 * API to get the list of all plugins from the database
	 * 
	 * @return {@link List}< {@link PluginDetailsDTO}>
	 */
	public List<PluginDetailsDTO> getAllPluginDetailDTOs();

	/**
	 * API to update all the dirty plugins
	 * 
	 * @param allPluginDetailsDTO {@link List}< {@link PluginDetailsDTO}>
	 * @return {@link List}< {@link PluginDetailsDTO}>
	 */
	List<PluginDetailsDTO> updateAllPluginDetailsDTOs(List<PluginDetailsDTO> allPluginDetailsDTO);

	/**
	 * API to delete a plugin and its references.
	 * 
	 * @param pluginDetailsDTO {@link PluginDetailsDTO}
	 * @return {@link Boolean} whether the plugin could be deleted or not.
	 * @throws UIException
	 */
	Boolean deletePlugin(PluginDetailsDTO pluginDetailsDTO) throws UIException;

	/**
	 * Checks whether the application key is already generated or not
	 * 
	 * @return true if the key is already generated.
	 * @throws GWTException when the key cannot be generated.
	 */
	boolean isApplicationKeyGenerated() throws UIException;

	/**
	 * Generates the application level key using the key as received from the user. This Key might not be generated in the form as it
	 * is.
	 * 
	 * @param applicationKey {@link String} application key provided by the client which will be digested to generate the actual key
	 *            for encryption purpose
	 * @param callback {@link AsyncCallback} which makes the rpc call with the server.
	 */
	boolean generateApplicationLevelKey(final String applicationKey) throws UIException;

	// below method not used yet.
	/**
	 * Gets the list of all regex groups.
	 * 
	 * @return {@link Collection}<{@link RegexGroupDTO}>
	 */
	Collection<RegexGroupDTO> getRegexGroupDTOList();

	/**
	 * Imports new regex groups, which are not already present in database.
	 * 
	 * @param importRegexPoolDTO {@link ImportRegexPoolDTO}
	 * @return {@link Map}<{@link String}, {@link Boolean}>
	 */
	Map<String, Boolean> importRegexGroups(List<ImportPoolDTO> importRegexPoolDTO);

	/**
	 * Returns the license details like license expiry date, CPU license count, etc.
	 * 
	 * @return {@link Map}<{@link String}, {@link String}>: license details
	 */
	Map<String, String> getLicenseDetails();

	/**
	 * Stores all the updated connections DTOs in db and returns the updated list.
	 * 
	 * @param allConnectionsDTO {@link List}<{@link ConnectionsDTO}>
	 * @param all updated list of connections dtos. {@link List}<{@link ConnectionsDTO}>
	 * @return {@link List}<{@link ConnectionsDTO}>
	 */
	List<ConnectionsDTO> updateAllConnectionsDTOs(List<ConnectionsDTO> allConnectionsDTO);

	/**
	 * Tests DB connection using the provided parameters.
	 * 
	 * @param connectionURL {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param driverClass {@link String}
	 * @return {@link Boolean}
	 */
	boolean testConnection(String connectionURL, String userName, String password, String driverClass);

	/**
	 * Removes selected connection from DB.
	 * 
	 * @param selectedConnection {@link List}<{@link ConnectionsDTO}>
	 * @return {@link ConnectionsResult}
	 */
	ConnectionsResult removeSelectedConnection(List<ConnectionsDTO> selectedConnection);

	/**
	 * Finds all the matches inside the string for the given regex pattern and returns the list of matched indexes.
	 * 
	 * @param regex {@link String} The regex pattern generated.
	 * @param strToBeMatched {@link String} The string which is to be matched.
	 * @return {@link List}<{@link String}> The list of matched indexes.
	 * @throws Exception if any exception or error occur.
	 */
	List<String> findMatchedIndexesList(String regex, String strToBeMatched) throws UIException;

	/**
	 * used for getting the map of avaliable REgex groups from DB
	 * 
	 * @return {@link Map}<{@link String}, {@link RegexGroupDTO}>
	 */
	Map<String, RegexGroupDTO> getRegexGroupMap();

	/**
	 * used for updating the selected Regex Pattern Dto
	 * 
	 * @param editedRegexPattern {@link RegexPatternDTO}
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 * @return {@link integer} pattern ID
	 */
	int updateRegexPattern(final RegexPatternDTO editedRegexPattern, final RegexGroupDTO regexGroupDTO);

	/**
	 * used for updating the selected Regex Group Dto
	 * 
	 * @param editedRegexGroup {@link RegexGroupDTO}
	 * @return {@link integer} Group ID
	 */
	int updateRegexGroup(final RegexGroupDTO editedRegexGroup);

	/**
	 * delete the selected list of Regex Patterns from DB
	 * 
	 * @param regexPatternDTOList {@link List}<{@link RegexPatternDTO}>
	 * @return {@link boolean } success or failure status of deletion event
	 */
	boolean deleteRegexPatterns(final List<RegexPatternDTO> regexPatternDTOList);

	/**
	 * delete the selected list of Regex Groups from DB
	 * 
	 * @param regexGroupDTOList {@link List}<{@link RegexPatternDTO}>
	 * @return {@link boolean } success or failure status of deletion event
	 */
	boolean deleteRegexGroups(final List<RegexGroupDTO> regexGroupDTOList);

	/**
	 * insert the selected Regex Group into DB
	 * 
	 * @param regexGroup {@link RegexGroupDTO}
	 * @return {@link boolean } success or failure status of insertion event
	 */
	boolean insertRegexGroup(final RegexGroupDTO regexGroup);

	/**
	 * insert the selected Regex Pattern into DB
	 * 
	 * @param regexPattern {@link RegexPatternDTO}
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 * @return {@link boolean } success or failure status of insertion event
	 */
	boolean insertRegexPattern(final RegexPatternDTO regexPattern, final RegexGroupDTO regexGroupDTO);

	/**
	 * Tests the connection is always ON
	 * 
	 * @param databaseName {@link String}
	 * @param connectionURL {@link String}
	 * @return {@link boolean}
	 */
	boolean testMSSQLAlwaysONConnection(String databaseName, String connectionURL);

}
