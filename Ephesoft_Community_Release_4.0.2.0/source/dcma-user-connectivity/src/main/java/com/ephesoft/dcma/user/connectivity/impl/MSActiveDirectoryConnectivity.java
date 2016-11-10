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

package com.ephesoft.dcma.user.connectivity.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.UserInformation;
import com.ephesoft.dcma.user.connectivity.UserConnectivity;
import com.ephesoft.dcma.user.connectivity.constant.UserConnectivityConstant;
import com.ephesoft.dcma.user.connectivity.util.UserConnectivityUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * This class connect to the Active Directory server and fetching the result from the Active Directory server.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.user.connectivity.UserConnectivity
 * 
 */
public class MSActiveDirectoryConnectivity implements UserConnectivity, UserConnectivityConstant {

	private static final String REFERRAL_FOLLOW = "follow";

	private static final String OPERATION_NOT_SUPPORTED_IN_MS_ACTIVE_DIRECTORY = "Operation not supported in MSActive Directory.";

	/**
	 * Used for handling logs.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MSActiveDirectoryConnectivity.class);

	/**
	 * This value get from the user.msactivedirectory_url in user-connectivity.properties file.
	 */
	private String msActiveDirectoryURL;

	/**
	 * This value get from the user.msactivedirectory_config in user-connectivity.properties file.
	 */
	private String msActiveDirectoryConfig;

	/**
	 * This value get from the user.msactivedirectory_container_name in user-connectivity.properties file.
	 */
	private String msActiveDirectoryContextPath;

	/**
	 * This value get from the user.msactivedirectory_domain_component_name in user-connectivity.properties file.
	 */
	private String msActiveDirectoryDomainName;

	/**
	 * This value get from the user.msactivedirectory_domain_component_organization in user-connectivity.properties file.
	 */
	private String msActiveDirectoryDomainOrganization;

	/**
	 * This value get from the user.msactivedirectory_user_name in user-connectivity.properties file.
	 */
	private String msActiveDirectoryUserName;

	/**
	 * This value get from the user.msactivedirectory_password in user-connectivity.properties file.
	 */
	private String msActiveDirectoryPassword;

	/**
	 * This value get from the user.msactivedirectory_group_search_filter in user-connectivity.properties file.
	 */
	private String msActiveDirectoryGroupSearchFilter;

	/**
	 * This Value is fetched from attribute user.msActiveDirectory_groupSearchAttributeFilter in user-connectivity.properties file.
	 */
	private String groupSearchAttribute;

	/**
	 * @return Attribute Value on which groups are searched
	 */

	public String getGroupSearchAttribute() {
		return groupSearchAttribute;
	}

	/**
	 * @param groupSearchAttribute is Value to be set for getting groups,on the basis of attribute specified.
	 */

	public void setGroupSearchAttribute(String groupSearchAttribute) {
		this.groupSearchAttribute = groupSearchAttribute;
	}

	/**
	 * @return Attribute Value on which Users are searched
	 */
	public String getUserSearchAttribute() {
		return userSearchAttribute;
	}

	/**
	 * @param userSearchAttribute is Value to be set for getting Users,on the basis of attribute specified.
	 */

	public void setUserSearchAttribute(String userSearchAttribute) {
		this.userSearchAttribute = userSearchAttribute;
	}

	/**
	 * This Value is fetched from attribute user.msActiveDirectory_userSearchAttributeFilter in user-connectivity.properties file.
	 */
	private String userSearchAttribute;

	/**
	 * @return the msActiveDirectoryUserName
	 */
	public String getMsActiveDirectoryUserName() {
		return msActiveDirectoryUserName;
	}

	/**
	 * @param msActiveDirectoryUserName the msActiveDirectoryUserName to set
	 */
	public void setMsActiveDirectoryUserName(final String msActiveDirectoryUserName) {
		this.msActiveDirectoryUserName = msActiveDirectoryUserName;
	}

	/**
	 * @return the msActiveDirectoryPassword
	 */
	public String getMsActiveDirectoryPassword() {
		return msActiveDirectoryPassword;
	}

	/**
	 * @param msActiveDirectoryPassword the msActiveDirectoryPassword to set
	 */
	public void setMsActiveDirectoryPassword(final String msActiveDirectoryPassword) {
		this.msActiveDirectoryPassword = msActiveDirectoryPassword;
	}

	/**
	 * @return the msActiveDirectoryURL
	 */
	public String getMsActiveDirectoryURL() {
		return msActiveDirectoryURL;
	}

	/**
	 * @param msActiveDirectoryURL the msActiveDirectoryURL to set
	 */
	public void setMsActiveDirectoryURL(final String msActiveDirectoryURL) {
		this.msActiveDirectoryURL = msActiveDirectoryURL;
	}

	/**
	 * @return the msActiveDirectoryConfig
	 */
	public String getMsActiveDirectoryConfig() {
		return msActiveDirectoryConfig;
	}

	/**
	 * @param msActiveDirectoryConfig the msActiveDirectoryConfig to set
	 */
	public void setMsActiveDirectoryConfig(final String msActiveDirectoryConfig) {
		this.msActiveDirectoryConfig = msActiveDirectoryConfig;
	}

	/**
	 * @return the msActiveDirectoryDomainName
	 */
	public String getMsActiveDirectoryDomainName() {
		return msActiveDirectoryDomainName;
	}

	/**
	 * @param msActiveDirectoryDomainName the msActiveDirectoryDomainName to set
	 */
	public void setMsActiveDirectoryDomainName(final String msActiveDirectoryDomainName) {
		this.msActiveDirectoryDomainName = msActiveDirectoryDomainName;
	}

	/**
	 * @return the msActiveDirectoryDomainOrganization
	 */
	public String getMsActiveDirectoryDomainOrganization() {
		return msActiveDirectoryDomainOrganization;
	}

	/**
	 * @param msActiveDirectoryDomainOrganization the msActiveDirectoryDomainOrganization to set
	 */
	public void setMsActiveDirectoryDomainOrganization(final String msActiveDirectoryDomainOrganization) {
		this.msActiveDirectoryDomainOrganization = msActiveDirectoryDomainOrganization;
	}

	/**
	 * @return the msActiveDirectoryContextPath
	 */
	public String getMsActiveDirectoryContextPath() {
		return msActiveDirectoryContextPath;
	}

	/**
	 * @param msActiveDirectoryContextPath the msActiveDirectoryContextPath to set
	 */
	public void setMsActiveDirectoryContextPath(String msActiveDirectoryContextPath) {
		this.msActiveDirectoryContextPath = msActiveDirectoryContextPath;
	}

	/**
	 * @return the msActiveDirectoryGroupSearchFilter
	 */
	public String getMsActiveDirectoryGroupSearchFilter() {
		return msActiveDirectoryGroupSearchFilter;
	}

	/**
	 * @param msActiveDirectoryGroupSearchFilter the msActiveDirectoryGroupSearchFilter to set
	 */
	public void setMsActiveDirectoryGroupSearchFilter(String msActiveDirectoryGroupSearchFilter) {
		this.msActiveDirectoryGroupSearchFilter = msActiveDirectoryGroupSearchFilter;
	}

	/**
	 * This method is used to connect the Active Directory and fetching the list of group object from the active directory.
	 * 
	 * @return Set<String> if connected and group is found else return null
	 */
	@Override
	public Set<String> getAllGroups() {
		Set<String> allGroups = null;
		try {
			LOG.info("======Inside MSActiveDirectoryConnectivity======");
			LOG.info("Fetching all available groups from the active directory");
			allGroups = this.fetchActiveDirectoryList(UserConnectivityConstant.MSACTIVEDIRECTORY_GROUP, groupSearchAttribute);
			LOG.info("Ending fetching list from Active Directory");
		} catch (Exception e) {
			LOG.error("Error in fetching all groups " + e.getMessage(), e);
		}
		return allGroups;
	}

	/**
	 * This method is used to connect to the active directory and used to return the Set of string of result fetch in accordance of the
	 * argument passed to it.
	 * 
	 * @param {@link String}
	 * @return Set<String> if connected and result is found else return null
	 */
	private Set<String> fetchActiveDirectoryList(final String name, final String searchAttribute) {
		Set<String> resultList = new HashSet<String>();

		boolean isValid = isValidData();

		if (isValid) {

			DirContext dctx = null;
			dctx = getActiveDirectoryContext();
			
			List<NamingEnumeration<?>> results = null;

			if (dctx != null) {
				LOG.info("Start Fetching result set from Active Directory");
				results = getResultSet(name, dctx, searchAttribute);

				if (results != null) {
					for (NamingEnumeration<?> resultContainer : results) {
						resultSetValues(resultList, resultContainer, searchAttribute);
					}
				} else {
					LOG.error("Results found from Active Directory is  null or empty.");
				}
				try {
					if (dctx != null) {
						LOG.info("Closing directory context of Active Directory");
						dctx.close();
					}
					if (results != null && !results.isEmpty()) {
						for (NamingEnumeration<?> resultContainer : results) {
							LOG.info("Closing result set of Active Directory");
							resultContainer.close();
						}
					}
				} catch (NamingException ne) {
					LOG.error(ne.getMessage(), ne);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			} else {
				LOG.error("Invalid directory context of Active Directory.");
			}
		}
		return resultList;
	}

	/**
	 * This method is used to create the connection to the directory server
	 * 
	 * @param environment
	 * @param directory
	 * @return
	 */
	private DirContext createDirectoryConnection(Hashtable<Object, Object> environment) { // NOPMD
		// Hashtable for Active Directory
		DirContext directory = null;
		try {
			directory = new InitialDirContext(environment);
		} catch (NamingException ne) {
			LOG.error(ne.getMessage(), ne);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return directory;
	}

	/**
	 * This method is used to get the result from the msActiveDirectory
	 * 
	 * @param name
	 * @param dctx
	 * @return
	 */
	private List<NamingEnumeration<?>> getResultSet(final String name, DirContext dctx, final String searchAttribute) {
		SearchControls searchControl = new SearchControls();

		// This code is Modified to make search attribute for MAS-Active directory groups and people configurable
		final String[] attributeFilter = new String[1];
		if (searchAttribute != null && !searchAttribute.isEmpty() && searchAttribute != UserConnectivityConstant.EMPTY_STRING) {
			attributeFilter[0] = searchAttribute;
		} else {
			attributeFilter[0] = UserConnectivityConstant.COMMON_NAME;
		}
		searchControl.setReturningAttributes(attributeFilter);
		searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String filter = UserConnectivityConstant.EMPTY_STRING;
		LOG.info("Added filter to Active Directory :" + filter);
		List<NamingEnumeration<?>> results = new ArrayList<NamingEnumeration<?>>();
		if (!msActiveDirectoryContextPath.trim().isEmpty()) {
			String[] msActiveDirectoryContextPathName = msActiveDirectoryContextPath.split(DOUBLE_SEMICOLON_DELIMITER);
			String[] msActiveDirectoryGroupFilter = msActiveDirectoryGroupSearchFilter.split(DOUBLE_SEMICOLON_DELIMITER);
			int numberOfContextPaths = msActiveDirectoryContextPathName.length;

			for (int index = 0; index < numberOfContextPaths; index++) {
				String msActiveDirectoryFullPath = msActiveDirectoryContextPathName[index];
				String filterData = UserConnectivityConstant.EMPTY_STRING;
				if (index < msActiveDirectoryGroupFilter.length) {
					filterData = msActiveDirectoryGroupFilter[index];
				}
				fetchResultsForSingleContextPath(name, dctx, searchControl, results, msActiveDirectoryFullPath, filterData);
			}
		} else {
			LOG.info("No seperate context path mentioned. So searching root node.");
			fetchResultsForSingleContextPath(name, dctx, searchControl, results, msActiveDirectoryContextPath,
					msActiveDirectoryGroupSearchFilter);
		}
		return results;
	}

	/**
	 * @param name
	 * @param dctx
	 * @param searchControl
	 * @param results
	 * @param msActiveDirectoryContextPath
	 * @param filterData
	 */
	private void fetchResultsForSingleContextPath(final String name, DirContext dctx, SearchControls searchControl,
			List<NamingEnumeration<?>> results, String msActiveDirectoryContextPath, String filterData) {
		String filter;
		filter = getGroupFilter(name, filterData);
		LOG.info("Filter added for " + msActiveDirectoryContextPath + " is:" + filter);
		String completeDirectoryContextPath = null;
		try {
			completeDirectoryContextPath = getCompleteDirectoryContextPath(msActiveDirectoryContextPath);
			LOG.info("Context Path for Active Directory :" + completeDirectoryContextPath);
			if (!completeDirectoryContextPath.isEmpty()) {
				results.add(dctx.search(completeDirectoryContextPath, filter, searchControl));
			} else {

				LOG.error("Context path is empty. Escaping search for this instance.");
			}
		} catch (NamingException ne) {
			LOG.error(ne.getMessage(), ne);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * This Method is added to check whether string Builder is empty or not
	 * 
	 * @param activeDirectoryPathBuilder
	 * @return true if string builder is not empty
	 */

	private static boolean checkStringLength(StringBuilder activeDirectoryPathBuilder) {
		return activeDirectoryPathBuilder != null && activeDirectoryPathBuilder.length() > 0;
	}

	/**
	 * @param msActiveDirectoryContextPath
	 * @return
	 */
	private String getCompleteDirectoryContextPath(String msActiveDirectoryContextPath) {
		String completeDirectoryContextPath;
		StringBuilder activeDirectoryPathBuilder = new StringBuilder();
		if (!EphesoftStringUtil.isNullOrEmpty(msActiveDirectoryContextPath)) {
			activeDirectoryPathBuilder.append(msActiveDirectoryContextPath);
		}
		if (!EphesoftStringUtil.isNullOrEmpty(msActiveDirectoryDomainName)) {

			if (checkStringLength(activeDirectoryPathBuilder)) {
				activeDirectoryPathBuilder.append(UserConnectivityConstant.COMMA_SYMBOL);
			}
			activeDirectoryPathBuilder.append(UserConnectivityConstant.DOMAIN_COMPONENT);
			activeDirectoryPathBuilder.append(UserConnectivityConstant.EQUAL_SYMBOL);
			activeDirectoryPathBuilder.append(msActiveDirectoryDomainName);
		}
		if (!EphesoftStringUtil.isNullOrEmpty(msActiveDirectoryDomainOrganization)) {
			if (checkStringLength(activeDirectoryPathBuilder)) {
				activeDirectoryPathBuilder.append(UserConnectivityConstant.COMMA_SYMBOL);
			}
			activeDirectoryPathBuilder.append(UserConnectivityConstant.DOMAIN_COMPONENT);
			activeDirectoryPathBuilder.append(UserConnectivityConstant.EQUAL_SYMBOL);
			activeDirectoryPathBuilder.append(msActiveDirectoryDomainOrganization);
		}
		completeDirectoryContextPath = activeDirectoryPathBuilder.toString();
		if (completeDirectoryContextPath.isEmpty()) {
			LOG.error("Context path returned is empty.");
		}
		return completeDirectoryContextPath;
	}

	/**
	 * This method provides the group filter to be added.
	 * 
	 * @param name
	 * @param filterString {@link String} filter String to be added.
	 * @return {@link String} filter added for group.
	 */
	private String getGroupFilter(final String name, final String filterString) {
		String filter;

		// Reverting back changes for adding filter for list of active directory users.
		if (name == UserConnectivityConstant.MSACTIVEDIRECTORY_GROUP && filterString != null && !filterString.trim().isEmpty()) {
			StringBuffer filterBuffer = new StringBuffer(UserConnectivityConstant.START_FILTER);
			filterBuffer.append(UserConnectivityConstant.AND_SYMBOL);
			filterBuffer.append(UserConnectivityConstant.START_FILTER);
			filterBuffer.append(UserConnectivityConstant.OBJECT_CLASS);
			filterBuffer.append(UserConnectivityConstant.MSACTIVEDIRECTORY_EQUAL_SYMBOL);
			filterBuffer.append(name);
			filterBuffer.append(UserConnectivityConstant.END_FILTER);
			filterBuffer.append(filterString);
			filterBuffer.append(UserConnectivityConstant.END_FILTER);
			filter = filterBuffer.toString();
		} else {
			StringBuffer filterBuffer = new StringBuffer(UserConnectivityConstant.START_FILTER);
			filterBuffer.append(UserConnectivityConstant.OBJECT_CLASS);
			filterBuffer.append(UserConnectivityConstant.MSACTIVEDIRECTORY_EQUAL_SYMBOL);
			filterBuffer.append(name);
			filterBuffer.append(UserConnectivityConstant.END_FILTER);
			filter = filterBuffer.toString();
		}
		return filter;
	}

	/**
	 * This method manipulates the result into string to be added in the resultset
	 * 
	 * @param resultList
	 * @param results
	 */
	private void resultSetValues(Set<String> resultList, NamingEnumeration<?> results, final String searchAttribute) {

		try {
			StringBuffer attributeTobeSearched = new StringBuffer();
			// Added to getProperty Attribute configured in
			// user-connectivity.properties
			// If no property is specified Common Name is returned
			if (searchAttribute != null && !searchAttribute.isEmpty()) {
				attributeTobeSearched.append(searchAttribute);
				attributeTobeSearched.append(UserConnectivityConstant.EQUAL_SYMBOL);
				attributeTobeSearched.append(searchAttribute);
			} else {
				attributeTobeSearched.append(UserConnectivityConstant.COMMON_NAME).append(UserConnectivityConstant.EQUAL_SYMBOL)
						.append(UserConnectivityConstant.COMMON_NAME);
			}

			while (results.hasMore()) {
				SearchResult searchResult = null;
				try {
					searchResult = (SearchResult) results.next();
				} catch (NamingException e) {
					LOG.error(e.getMessage(), e);
				}

				if (null != searchResult) {
					LOG.info("Group found of Active Directory is :" + searchResult);
					String result = searchResult.toString();
					if (null != result && !result.isEmpty()) {
						final StringTokenizer stringTokenizer = new StringTokenizer(result, UserConnectivityConstant.COLON_SYMBOL
								+ UserConnectivityConstant.CURLY_BRACE_START + UserConnectivityConstant.CURLY_BRACE_END);
						boolean isAttributeFound = false;
						while (stringTokenizer.hasMoreTokens()) {
							isAttributeFound = false;
							final String stringToken = stringTokenizer.nextToken();

							if (attributeTobeSearched.toString().equalsIgnoreCase(stringToken)) {
								isAttributeFound = true;
							}
							if (isAttributeFound && stringToken != null && stringTokenizer.hasMoreTokens()) {
								resultList.add(stringTokenizer.nextToken().substring(1));
							}
						}
					}
				} else {
					LOG.error("No groups found in the active directory.");
				}
			}
		} catch (NamingException ne) {
			LOG.error("No result found" + ne.getMessage(), ne);
		}
	}

	/**
	 * Check the user-connectivity.properties are valid or not
	 * 
	 * @param isValid
	 * @return true if valid else false
	 */
	private boolean isValidData() {
		boolean check = true;
		if (null == msActiveDirectoryConfig || msActiveDirectoryConfig.isEmpty()) {
			LOG.error("msactivedirectoryConfig not found.");
			if (check) {
				check = false;
			}
		}

		if (null == msActiveDirectoryURL || msActiveDirectoryURL.isEmpty()) {
			LOG.error("msactivedirectoryUrl not found.");
			if (check) {
				check = false;
			}
		}

		if (null == msActiveDirectoryDomainName) {
			LOG.error("msactivedirectoryDomainName is null or empty.");
			if (check) {
				check = false;
			}
		}

		if (null == msActiveDirectoryDomainOrganization) {
			LOG.error("msactivedirectoryDomainOrganization is null or empty.");
			if (check) {
				check = false;
			}
		}

		if (null == msActiveDirectoryContextPath) {
			LOG.error("msActiveDirectoryContainer is null.");
			if (check) {
				check = false;
			}
		}

		if (msActiveDirectoryDomainName.isEmpty() && msActiveDirectoryDomainOrganization.isEmpty()) {
			LOG
					.error("No domain component provided. Both user.msactivedirectory_domain_component_name and user.msactivedirectory_domain_component_organization both cannot be empty.");
			if (check) {
				check = false;
			}
		}
		if (null == msActiveDirectoryUserName || msActiveDirectoryUserName.isEmpty()) {
			LOG.error("msActiveDirectoryUserName is null or empty.");
			if (check) {
				check = false;
			}
		}

		if (null == msActiveDirectoryPassword || msActiveDirectoryPassword.isEmpty()) {
			LOG.error("msActiveDirectoryPassword is null or empty.");
			if (check) {
				check = false;
			}
		}
		return check;
	}

	/**
	 * This method is used to connect the Active Directory and fetching the list of user object from the active directory.
	 * 
	 * @return Set<String> if connected and user is found else return null
	 */
	@Override
	public Set<String> getAllUser() {
		Set<String> allUser = null;
		try {
			allUser = fetchActiveDirectoryList(UserConnectivityConstant.MSACTIVEDIRECTORY_USER, userSearchAttribute);
		} catch (Exception e) {
			LOG.error("Error in fetching all users " + e.getMessage(), e);
		}
		return allUser;
	}

	@Override
	public Set<String> getUserGroups(String userName) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_MS_ACTIVE_DIRECTORY);
	}

	@Override
	public void addGroup(UserInformation userInformation) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_MS_ACTIVE_DIRECTORY);
	}

	@Override
	public void addUser(UserInformation userInformation) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_MS_ACTIVE_DIRECTORY);
	}

	@Override
	public boolean checkUserExistence(String userName) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_MS_ACTIVE_DIRECTORY);
	}

	@Override
	public void deleteGroup(String groupName) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_MS_ACTIVE_DIRECTORY);
	}

	@Override
	public void deleteUser(String userName) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_MS_ACTIVE_DIRECTORY);
	}

	@Override
	public void modifyUserPassword(String userName, String newPassword) throws NamingException {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_MS_ACTIVE_DIRECTORY);
	}

	@Override
	public void verifyandmodifyUserPassword(String userName, String oldPassword, String newPassword) throws NamingException {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_MS_ACTIVE_DIRECTORY);
	}

	/**
	 * This method is used to connect to the LDAP Directory and fetch the full name and surname of the user from the LDAP.
	 * 
	 * @param commonName
	 * @return String if connected and user is found else return null.
	 */
	@Override
	public String getUserFullName(final String userName) {
		LOG.info("Getting full name for user " + userName);
		String result = null;
		if (null != userName) {
			DirContext dctx = null;
			// get directory context
			dctx = getActiveDirectoryContext();
			final SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

			// get user search attributes.
			final String[] attrIDs = UserConnectivityUtil.getUserSearchAttributes();
			LOG.info("User attributes added to constraints + " + attrIDs.toString());
			constraints.setReturningAttributes(attrIDs);

			final String paramName = EphesoftStringUtil.concatenate(userSearchAttribute, UserConnectivityConstant.EQUAL_SYMBOL,
					userName);

			if (!msActiveDirectoryContextPath.trim().isEmpty()) {
				// if context paths are mentioned
				String[] msActiveDirectoryContextPathName = msActiveDirectoryContextPath.split(DOUBLE_SEMICOLON_DELIMITER);

				for (String contextPath : msActiveDirectoryContextPathName) {
					result = searchUserOnContextPath(dctx, constraints, paramName, contextPath);
					if (!EphesoftStringUtil.isNullOrEmpty(result)) {
						// if the user is found in the current context path, do not search further.
						break;
					}
				}
			} else {
				// if no context paths are mentioned. Search to be done on domain components.
				result = searchUserOnContextPath(dctx, constraints, paramName, null);
			}
			LOG.info(EphesoftStringUtil.concatenate("Full name for user ", userName, " is ", result));
		}
		return result;
	}

	private String searchUserOnContextPath(DirContext dctx, final SearchControls constraints, final String paramName,
			String contextPath) {
		String result = "";
		LOG.info(EphesoftStringUtil.concatenate("User Name for MSAD :", paramName));

		// prepare complete path for the given context path.
		final String paramConfig = getCompleteDirectoryContextPath(contextPath);
		LOG.info(EphesoftStringUtil.concatenate("Context Path for MSAD :", paramConfig));
		result = UserConnectivityUtil.searchUser(dctx, constraints, paramName, paramConfig);
		return result;
	}

	private DirContext getActiveDirectoryContext() {
		final Hashtable<Object, Object> env = new Hashtable<Object, Object>(); // NOPMD. Required to work with Hashtable for
		// LDAP.
		env.put(Context.INITIAL_CONTEXT_FACTORY, msActiveDirectoryConfig);
		env.put(Context.PROVIDER_URL, msActiveDirectoryURL);
		env.put(Context.SECURITY_PRINCIPAL, msActiveDirectoryUserName);
		env.put(Context.SECURITY_CREDENTIALS, (msActiveDirectoryPassword));
		env.put(Context.REFERRAL, REFERRAL_FOLLOW);

		DirContext dctx = null;
		dctx = createDirectoryConnection(env);
		return dctx;

	}

	@Override
	public String getUserEmail(final String userName) {
		LOG.info("Getting full name for user " + userName);
		String result = null;
		if (null != userName) {
			DirContext dctx = null;
			// get directory context
			dctx = getActiveDirectoryContext();
			final SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

			final String[] attrIDs = new String[1];
			attrIDs[0] = UserConnectivityConstant.MAIL;
			LOG.info("User attributes added to constraints + " + attrIDs.toString());
			constraints.setReturningAttributes(attrIDs);

			final String paramName = EphesoftStringUtil.concatenate(userSearchAttribute, UserConnectivityConstant.EQUAL_SYMBOL,
					userName);

			if (!EphesoftStringUtil.isNullOrEmpty(msActiveDirectoryContextPath.trim())) {
				// if context paths are mentioned
				final String[] msActiveDirectoryContextPathName = msActiveDirectoryContextPath.split(DOUBLE_SEMICOLON_DELIMITER);

				for (final String contextPath : msActiveDirectoryContextPathName) {
					result = searchUserOnContextPath(dctx, constraints, paramName, contextPath);
					if (!EphesoftStringUtil.isNullOrEmpty(result)) {
						// if the user is found in the current context path, do not search further.
						break;
					}
				}
			} else {
				// if no context paths are mentioned. Search to be done on domain components.
				result = searchUserOnContextPath(dctx, constraints, paramName, null);
			}
			LOG.info(EphesoftStringUtil.concatenate("Email for user ", userName, " is ", result));
		}
		return result;
	}
}
