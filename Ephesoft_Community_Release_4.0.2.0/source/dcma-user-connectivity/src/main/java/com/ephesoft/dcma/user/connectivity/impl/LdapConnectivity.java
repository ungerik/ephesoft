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

import java.util.HashSet;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.UserInformation;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.user.connectivity.UserConnectivity;
import com.ephesoft.dcma.user.connectivity.constant.UserConnectivityConstant;
import com.ephesoft.dcma.user.connectivity.exception.InvalidCredentials;
import com.ephesoft.dcma.user.connectivity.util.UserConnectivityUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * This class connect to the LDAP server and fetching the result from the LDAP directory server.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.user.connectivity.UserConnectivity
 * 
 */
public class LdapConnectivity implements UserConnectivity {

	private static final String ERROR_IN_CLOSING_LDAP_CONNECTION = "Error in closing LDAP connection";

	/**
	 * Used for handling logs.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(LdapConnectivity.class);

	/**
	 * This value get from the user.ldap_url in user-connectivity.properties file.
	 */
	private String ldapURL;

	/**
	 * This value get from the user.ldap_config in user-connectivity.properties file.
	 */
	private String ldapConfig;

	/**
	 * This value get from the user.ldap_domain_component_name in user-connectivity.properties file.
	 */
	private String ldapDomainName;

	/**
	 * This value get from the user.ldap_domain_component_organization in user-connectivity.properties file.
	 */
	private String ldapDomainOrganization;

	/**
	 * This value get from the user.ldap_user_name in user-connectivity.properties file.
	 */
	private String ldapUserName;

	/**
	 * This value get from the user.ldap_password in user-connectivity.properties file.
	 */
	private String ldapPassword;

	/**
	 * This value get from the user.ldap_user_base in user-connectivity.properties file.
	 */
	private String userBasePath;

	/**
	 * This value get from the user.ldap_group_base in user-connectivity.properties file.
	 */
	private String groupBasePath;
	/**
	 * This Value is fetched from attribute user.ldap_groupSearchAttributeFilter in user-connectivity.properties file.
	 */
	private String groupSearchAttribute;
	/**
	 * This Value is fetched from attribute user.ldap_userSearchAttributeFilter in user-connectivity.properties file.
	 */
	private String userSearchAttribute;

	/**
	 * @return Attribute Value on which Users are searched
	 */
	public String getUserSearchAttribute() {
		return userSearchAttribute;
	}

	/**
	 * @param userSearchAttribute is Value to be set for getting Users,on the basis of attribute specified.
	 */
	public void setUserSearchAttribute(final String userSearchAttribute) {
		this.userSearchAttribute = userSearchAttribute;
	}

	/**
	 * @return Attribute Value on which groups are searched
	 */
	public String getGroupSearchAttribute() {
		return groupSearchAttribute;
	}

	/**
	 * @param groupSearchAttribute is Value to be set for getting groups,on the basis of attribute specified.
	 */
	public void setGroupSearchAttribute(final String groupSearchAttribute) {
		this.groupSearchAttribute = groupSearchAttribute;
	}

	/**
	 * @return the ldapURL
	 */
	public String getLdapURL() {
		return ldapURL;
	}

	/**
	 * @param ldapURL the ldapURL to set
	 */
	public void setLdapURL(final String ldapURL) {
		this.ldapURL = ldapURL;
	}

	/**
	 * @return the ldapConfig
	 */
	public String getLdapConfig() {
		return ldapConfig;
	}

	/**
	 * @param ldapConfig the ldapConfig to set
	 */
	public void setLdapConfig(final String ldapConfig) {
		this.ldapConfig = ldapConfig;
	}

	/**
	 * @return the ldapDomainName
	 */

	public String getLdapDomainName() {
		return ldapDomainName;
	}

	/**
	 * @param ldapDomainName the ldapDomainName to set
	 */
	public void setLdapDomainName(final String ldapDomainName) {
		this.ldapDomainName = ldapDomainName;
	}

	/**
	 * @return the ldapDomainOrganization
	 */
	public String getLdapDomainOrganization() {
		return ldapDomainOrganization;
	}

	/**
	 * @param ldapDomainOrganization the ldapDomainOrganization to set
	 */
	public void setLdapDomainOrganization(final String ldapDomainOrganization) {
		this.ldapDomainOrganization = ldapDomainOrganization;
	}

	/**
	 * @return the ldapPassword
	 */
	public String getLdapPassword() {
		return ldapPassword;
	}

	/**
	 * @return the ldapUserName
	 */
	public String getLdapUserName() {
		return ldapUserName;
	}

	/**
	 * @param ldapPassword the ldapPassword to set
	 */
	public void setLdapPassword(final String ldapPassword) {
		this.ldapPassword = ldapPassword;
	}

	/**
	 * @param ldapUserName the ldapUserName to set
	 */
	public void setLdapUserName(final String ldapUserName) {
		this.ldapUserName = ldapUserName;
	}

	/**
	 * This method is used to connect the LDAP Directory and fetching the list of groups object from the LDAP.
	 * 
	 * @return Set<String> if connected and groups is found else return null
	 */
	@Override
	public Set<String> getAllGroups() {
		Set<String> allGroups = null;
		try {
			LOG.info("======Inside LDAPUserConnectivity======");
			LOG.info("Fetching all available groups from the LDAP");
			allGroups = this.fetchLDAPList(groupBasePath, UserConnectivityConstant.LDAP_GROUP_FILTER, groupSearchAttribute);
			LOG.info("Ending fetching list from LDAP");
		} catch (final Exception e) {
			LOG.error("Error in fetching all groups " + e.getMessage(), e);
		}
		return allGroups;
	}

	/**
	 * This method is used to connect to the LDAP directory and used to return the Set of string of result fetch in accordance of the
	 * argument passed to it.
	 * 
	 * @param {@link String} search parameter
	 * @param {@link String} filter value
	 * @param searchAttribure contains the property on which search is executed for the LDAP object.
	 * @return Set<String> if connected and result is found else return null
	 */

	private Set<String> fetchLDAPList(final String name, final String filterValue, final String searchAttribute) {

		Set<String> resultList = null;
		boolean isValid = true;

		isValid = isValidData();

		if (isValid) {
			DirContext dctx = getLDAPDirectoryContext();

			if (null != dctx) {
				LOG.info("Start Fetching result set from LDAP Directory");
				NamingEnumeration<?> results = null;
				results = getResultSet(name, dctx, filterValue, searchAttribute);

				if (results != null) {
					resultList = new HashSet<String>();
					resultSetValues(resultList, results, searchAttribute);
				} else {
					LOG.error("Results found from LDAP is  null or empty.");
				}

				try {
					if (dctx != null) {
						LOG.info("Closing directory context of LDAP");
						dctx.close();
					}
					if (results != null) {
						LOG.info("Closing result set of LDAP");
						results.close();
					}
				} catch (final NamingException ne) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				} catch (final Exception e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			} else {
				LOG.error("Invalid directory context of LDAP.");
			}
		}
		return resultList;
	}

	private DirContext getLDAPDirectoryContext() {
		final Hashtable<Object, Object> env = new Hashtable<Object, Object>(); // NOPMD. Required to work with Hashtable for LDAP.
		env.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfig);
		env.put(Context.PROVIDER_URL, ldapURL);
		env.put(Context.SECURITY_PRINCIPAL, ldapUserName);
		env.put(Context.SECURITY_CREDENTIALS, ((ldapPassword == null) ? "" : ldapPassword));

		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);
		} catch (final NamingException ne) {
			LOG.error(ne.getMessage(), ne);
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return dctx;
	}

	/**
	 * This method is used to get the result from the Ldap directory.
	 * 
	 * @param name the search directory name
	 * @param dctx directory context object
	 * @param filterValue filter value for fetching specific type of objects
	 * @param parameter contains values on which search is to be made
	 * @return
	 */
	private NamingEnumeration<?> getResultSet(final String name, final DirContext dctx, final String filterValue,
			final String searchAttribute) {
		final SearchControls searchControl = new SearchControls();

		// This code is Modified to make search attribute for LDAP groups and people configurable
		final String[] attributeFilter = new String[1];
		if (searchAttribute != null && !searchAttribute.isEmpty() && searchAttribute != UserConnectivityConstant.EMPTY_STRING) {
			attributeFilter[0] = searchAttribute;
		} else {
			attributeFilter[0] = UserConnectivityConstant.COMMON_NAME;
		}
		searchControl.setReturningAttributes(attributeFilter);
		searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);

		LOG.info("Added filter to LDAP :" + filterValue);
		NamingEnumeration<?> results = null;
		try {
			StringBuilder stringBuilder = new StringBuilder();
			if (!name.isEmpty()) {
				stringBuilder.append(name);
				stringBuilder.append(UserConnectivityConstant.COMMA_SYMBOL);
			}
			stringBuilder.append(UserConnectivityConstant.DOMAIN_COMPONENT);
			stringBuilder.append(UserConnectivityConstant.EQUAL_SYMBOL);
			stringBuilder.append(ldapDomainName);
			stringBuilder.append(UserConnectivityConstant.COMMA_SYMBOL);
			stringBuilder.append(UserConnectivityConstant.DOMAIN_COMPONENT);
			stringBuilder.append(UserConnectivityConstant.EQUAL_SYMBOL);
			stringBuilder.append(ldapDomainOrganization);
			final String paramName = stringBuilder.toString();

			LOG.info("Context Path for LDAP :" + paramName);
			results = dctx.search(paramName, filterValue, searchControl);
		} catch (final NamingException ne) {
			LOG.error(ne.getMessage(), ne);
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return results;
	}

	/**
	 * This method manipulates the result into string to be added in the resultset
	 * 
	 * @param resultList
	 * @param results
	 * @param searchAttribute It can value user or group on the basis of which value configured in user-connectivity.properties value
	 *            is fetched
	 */
	private void resultSetValues(final Set<String> resultList, final NamingEnumeration<?> results, final String searchAttribute) {
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
				} catch (final NamingException e) {
					LOG.error(e.getMessage(), e);
				}

				if (null != searchResult) {
					LOG.info("Group found of LDAP is :" + searchResult);
					final String searchResultString = searchResult.toString();
					final StringTokenizer stringTokenizer = new StringTokenizer(searchResultString,
							UserConnectivityConstant.COLON_SYMBOL + UserConnectivityConstant.CURLY_BRACE_START
									+ UserConnectivityConstant.CURLY_BRACE_END);

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
				} else {
					LOG.error("No groups found in the LDAP.");
				}
			}
		} catch (final NamingException ne) {
			LOG.error(ne.getMessage(), ne);
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
		if (null == ldapConfig || ldapConfig.isEmpty()) {
			LOG.error("ldapConfig not found.");
			if (check) {
				check = false;
			}
		}

		if (null == ldapURL || ldapURL.isEmpty()) {
			LOG.error("ldapUrl not found.");
			if (check) {
				check = false;
			}
		}

		if (null == ldapDomainName || ldapDomainName.isEmpty()) {
			LOG.error("ldapDomainName is null or empty.");
			if (check) {
				check = false;
			}
		}

		if (null == ldapDomainOrganization || ldapDomainOrganization.isEmpty()) {
			LOG.error("ldapDomainOrganization is null or empty.");
			if (check) {
				check = false;
			}
		}
		return check;
	}

	/**
	 * This method is used to connect the LDAP Directory and fetching the list of users object from the LDAP.
	 * 
	 * @return Set<String> if connected and users is found else return null
	 */
	@Override
	public Set<String> getAllUser() {
		Set<String> allUser = null;
		try {
			allUser = fetchLDAPList(userBasePath, UserConnectivityConstant.LDAP_USER_FILTER, userSearchAttribute);
		} catch (final Exception e) {
			LOG.error("Error in fetching all users " + e.getMessage(), e);
		}
		return allUser;
	}

	@Override
	public Set<String> getUserGroups(final String userName) {
		Set<String> userGroups = null;
		try {
			LOG.info(EphesoftStringUtil.concatenate("Fetching all available groups from the LDAP for user : ", userName));
			String userGroupFilter = createUserGroupFilter(userName);
			userGroups = this.fetchLDAPList(groupBasePath, userGroupFilter, groupSearchAttribute);
			LOG.info("Ending fetching list from LDAP");
		} catch (final Exception exception) {
			LOG.error(EphesoftStringUtil.concatenate("Error in fetching all groups for user : ", userName), exception);
		}
		return userGroups;
	}

	private String createUserGroupFilter(final String userName) {
		String userDomainFilter = getUserDN(userName);
		String userFilter = EphesoftStringUtil.concatenate(UserConnectivityConstant.UNIQUE_MEMBER,
				UserConnectivityConstant.EQUAL_SYMBOL, userDomainFilter);

		String userGroupFilter = EphesoftStringUtil.concatenate(UserConnectivityConstant.START_FILTER,
				UserConnectivityConstant.AND_SYMBOL, UserConnectivityConstant.START_FILTER,
				UserConnectivityConstant.LDAP_GROUP_FILTER, UserConnectivityConstant.END_FILTER,
				UserConnectivityConstant.START_FILTER, userFilter, UserConnectivityConstant.END_FILTER,
				UserConnectivityConstant.END_FILTER);

		return userGroupFilter;
	}

	@Override
	public void addGroup(final UserInformation userInfo) throws NamingException {
		final Hashtable<Object, Object> env = new Hashtable<Object, Object>(); // NOPMD. Required to work with Hashtable for LDAP.
		env.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfig);
		env.put(Context.PROVIDER_URL, ldapURL);
		env.put(Context.SECURITY_PRINCIPAL, ldapUserName);
		env.put(Context.SECURITY_CREDENTIALS, ((ldapPassword == null) ? "" : ldapPassword));

		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);

			// Create a container set of attributes
			final Attributes container = new BasicAttributes();

			// Create the objectclass to add
			final Attribute objClasses = new BasicAttribute(UserConnectivityConstant.OBJECT_CLASS);
			objClasses.add(UserConnectivityConstant.GROUP_OF_UNIQUE_NAMES);

			final String commonNameValue = userInfo.getCompanyName() + ICommonConstants.UNDERSCORE + userInfo.getEmail();

			// Assign the name and description to the group
			final Attribute commonName = new BasicAttribute(UserConnectivityConstant.COMMON_NAME, commonNameValue);
			final Attribute desc = new BasicAttribute(UserConnectivityConstant.DESCRIPTION, userInfo.getCompanyName());
			final Attribute uniqueMember = new BasicAttribute(UserConnectivityConstant.UNIQUE_MEMBER, getUserDN(userInfo.getEmail()));

			// Add these to the container
			container.put(objClasses);
			container.put(commonName);
			container.put(desc);
			container.put(uniqueMember);

			// Create the entry
			dctx.createSubcontext(getGroupDN(commonNameValue), container);
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
	}

	@Override
	public void addUser(final UserInformation userInformation) throws NamingException {
		final Hashtable<Object, Object> env = new Hashtable<Object, Object>(); // NOPMD. Required to work with Hashtable for LDAP.
		env.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfig);
		env.put(Context.PROVIDER_URL, ldapURL);
		env.put(Context.SECURITY_PRINCIPAL, ldapUserName);
		env.put(Context.SECURITY_CREDENTIALS, ((ldapPassword == null) ? "" : ldapPassword));
		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);
			// Create a container set of attributes
			final Attributes container = new BasicAttributes();

			// Create the objectclass to add
			final Attribute objClasses = new BasicAttribute(UserConnectivityConstant.OBJECT_CLASS);
			objClasses.add(UserConnectivityConstant.INET_ORG_PERSON);

			// Assign the username, first name, and last name
			final Attribute commonName = new BasicAttribute(UserConnectivityConstant.COMMON_NAME, userInformation.getEmail());
			final Attribute email = new BasicAttribute(UserConnectivityConstant.MAIL, userInformation.getEmail());
			final Attribute givenName = new BasicAttribute(UserConnectivityConstant.GIVEN_NAME, userInformation.getFirstName());
			final Attribute uid = new BasicAttribute(UserConnectivityConstant.UID, userInformation.getEmail());
			final Attribute surName = new BasicAttribute(UserConnectivityConstant.SUR_NAME, userInformation.getLastName());

			// Add password
			final Attribute userPassword = new BasicAttribute(UserConnectivityConstant.USER_PASSWORD, userInformation.getPassword());

			// Add these to the container
			container.put(objClasses);
			container.put(commonName);
			container.put(givenName);
			container.put(email);
			container.put(uid);
			container.put(surName);
			container.put(userPassword);

			// Create the entry
			dctx.createSubcontext(getUserDN(userInformation.getEmail()), container);
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
	}

	private String getUserDN(final String userName) {
		return new StringBuffer().append(UserConnectivityConstant.COMMON_NAME).append(UserConnectivityConstant.EQUAL_SYMBOL).append(
				userName).append(UserConnectivityConstant.COMMA_SYMBOL).append(userBasePath).append(
				UserConnectivityConstant.COMMA_SYMBOL).append(UserConnectivityConstant.DOMAIN_COMPONENT).append(
				UserConnectivityConstant.EQUAL_SYMBOL).append(ldapDomainName).append(UserConnectivityConstant.COMMA_SYMBOL).append(
				UserConnectivityConstant.DOMAIN_COMPONENT).append(UserConnectivityConstant.EQUAL_SYMBOL)
				.append(ldapDomainOrganization).toString();
	}

	private String getGroupDN(final String userName) {
		return new StringBuffer().append(UserConnectivityConstant.COMMON_NAME).append(UserConnectivityConstant.EQUAL_SYMBOL).append(
				userName).append(UserConnectivityConstant.COMMA_SYMBOL).append(groupBasePath).append(
				UserConnectivityConstant.COMMA_SYMBOL).append(UserConnectivityConstant.DOMAIN_COMPONENT).append(
				UserConnectivityConstant.EQUAL_SYMBOL).append(ldapDomainName).append(UserConnectivityConstant.COMMA_SYMBOL).append(
				UserConnectivityConstant.DOMAIN_COMPONENT).append(UserConnectivityConstant.EQUAL_SYMBOL)
				.append(ldapDomainOrganization).toString();
	}

	@Override
	public boolean checkUserExistence(final String userName) {
		boolean isUserExist = false;
		final Hashtable<Object, Object> env = new Hashtable<Object, Object>(); // NOPMD. Required to work with Hashtable for LDAP.
		env.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfig);
		env.put(Context.PROVIDER_URL, ldapURL);
		env.put(Context.SECURITY_PRINCIPAL, ldapUserName);
		env.put(Context.SECURITY_CREDENTIALS, ((ldapPassword == null) ? "" : ldapPassword));

		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);
			final NamingEnumeration<SearchResult> namingEnumeration = dctx.search(getUserDN(userName), null);

			if (namingEnumeration != null) {
				isUserExist = true;
			}
		} catch (final NamingException e) {
			LOG.info("User is not found in ldap for :" + userName);
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
		return isUserExist;
	}

	@Override
	public void deleteGroup(final String groupName) {
		final Hashtable<Object, Object> env = new Hashtable<Object, Object>(); // NOPMD. Required to work with Hashtable for LDAP.
		env.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfig);
		env.put(Context.PROVIDER_URL, ldapURL);
		env.put(Context.SECURITY_PRINCIPAL, ldapUserName);
		env.put(Context.SECURITY_CREDENTIALS, ((ldapPassword == null) ? "" : ldapPassword));

		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);
			dctx.destroySubcontext(getGroupDN(groupName));
		} catch (final NameNotFoundException e) {
			LOG.error("User doesn't exist in LDAP for deletion");
		} catch (final NamingException e) {
			LOG.error("Unable to create connection with LDAP :" + e.getMessage(), e);
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
	}

	@Override
	public void deleteUser(final String userName) {
		final Hashtable<Object, Object> env = new Hashtable<Object, Object>(); // NOPMD. Required to work with Hashtable for LDAP.
		env.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfig);
		env.put(Context.PROVIDER_URL, ldapURL);
		env.put(Context.SECURITY_PRINCIPAL, ldapUserName);
		env.put(Context.SECURITY_CREDENTIALS, ((ldapPassword == null) ? "" : ldapPassword));

		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);
			dctx.destroySubcontext(getUserDN(userName));
		} catch (final NameNotFoundException e) {
			LOG.error("User doesn't exist in LDAP for deletion");
		} catch (final NamingException e) {
			LOG.error("Unable to create connection with LDAP :" + e.getMessage(), e);
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
	}

	@Override
	public void verifyandmodifyUserPassword(final String userName, final String oldPassword, final String newPassword)
			throws NamingException {
		final Hashtable<Object, Object> env = new Hashtable<Object, Object>(); // NOPMD. Required to work with Hashtable for LDAP.
		env.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfig);
		env.put(Context.PROVIDER_URL, ldapURL);
		env.put(Context.SECURITY_PRINCIPAL, ldapUserName);
		env.put(Context.SECURITY_CREDENTIALS, ((ldapPassword == null) ? "" : ldapPassword));

		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);

			final Hashtable<Object, Object> env1 = new Hashtable<Object, Object>(); // NOPMD. Required to work with Hashtable for LDAP.
			env1.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfig);
			env1.put(Context.PROVIDER_URL, ldapURL);
			env1.put(Context.SECURITY_PRINCIPAL, getUserDN(userName));
			env1.put(Context.SECURITY_CREDENTIALS, ((oldPassword == null) ? "" : oldPassword));
			boolean isExist = true;
			DirContext localDctx = null;
			try {
				localDctx = new InitialDirContext(env1);
			} catch (final NamingException e) {
				isExist = false;
				throw new InvalidCredentials("Invalid username and password provided for verification", e);
			} finally {
				if (null != localDctx) {
					try {
						localDctx.close();
					} catch (final NamingException e) {
						LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
					}
				}
			}
			if (isExist) {
				final ModificationItem[] modificationItem = new ModificationItem[1];
				final Attribute attribute = new BasicAttribute(UserConnectivityConstant.USER_PASSWORD, newPassword);

				modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attribute);

				dctx.modifyAttributes(getUserDN(userName), modificationItem);
			}
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
	}

	@Override
	public void modifyUserPassword(final String userName, final String newPassword) throws NamingException {
		final Hashtable<Object, Object> env = new Hashtable<Object, Object>(); // NOPMD. Required to work with Hashtable for LDAP.
		env.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfig);
		env.put(Context.PROVIDER_URL, ldapURL);
		env.put(Context.SECURITY_PRINCIPAL, ldapUserName);
		env.put(Context.SECURITY_CREDENTIALS, ((ldapPassword == null) ? "" : ldapPassword));

		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);

			final ModificationItem[] modificationItem = new ModificationItem[1];
			final Attribute attribute = new BasicAttribute(UserConnectivityConstant.USER_PASSWORD, newPassword);

			modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attribute);

			dctx.modifyAttributes(getUserDN(userName), modificationItem);
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
	}

	/**
	 * @return the userBasePath
	 */
	public String getUserBasePath() {
		return userBasePath;
	}

	/**
	 * @param userBasePath the userBasePath to set
	 */
	public void setUserBasePath(final String userBasePath) {
		this.userBasePath = userBasePath;
	}

	/**
	 * @return the groupBasePath
	 */
	public String getGroupBasePath() {
		return groupBasePath;
	}

	/**
	 * @param groupBasePath the groupBasePath to set
	 */
	public void setGroupBasePath(final String groupBasePath) {
		this.groupBasePath = groupBasePath;
	}

	/**
	 * This method is used to connect to the LDAP Directory and fetch the full name and surname of the user from the LDAP.
	 * 
	 * @param commonName
	 * @return String if connected and user is found else return null
	 */
	@Override
	public String getUserFullName(final String userName) {
		String result = null;
		if (null != userName) {
			try {
				final DirContext dctx = getLDAPDirectoryContext();
				final SearchControls constraints = new SearchControls();
				constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
				final String[] attrIDs = UserConnectivityUtil.getUserSearchAttributes();
				LOG.info("User attributes added to constraints + " + attrIDs.toString());
				constraints.setReturningAttributes(attrIDs);

				final String paramConfig = getBaseContextPath();
				LOG.info(EphesoftStringUtil.concatenate("Context Path for LDAP :", paramConfig));
				final String paramName = EphesoftStringUtil.concatenate(userSearchAttribute, UserConnectivityConstant.EQUAL_SYMBOL,
						userName);
				LOG.info(EphesoftStringUtil.concatenate("User Name for LDAP :", paramName));

				result = UserConnectivityUtil.searchUser(dctx, constraints, paramName, paramConfig);
			} catch (final NoSuchElementException noSuchEleExp) {
				LOG.error(noSuchEleExp.getMessage(), noSuchEleExp);
			}
		}
		LOG.info(EphesoftStringUtil.concatenate("Full name for user ", userName, " is ", result));
		return result;
	}

	private String getBaseContextPath() {
		LOG.info("Fetching the LDAP base context path.");
		String baseContextPath;
		StringBuilder ldapContextPathBuilder = new StringBuilder();
		if (!EphesoftStringUtil.isNullOrEmpty(ldapDomainName)) {

			if (!EphesoftStringUtil.isNullOrEmpty(ldapContextPathBuilder.toString())) {
				ldapContextPathBuilder.append(UserConnectivityConstant.COMMA_SYMBOL);
			}
			ldapContextPathBuilder.append(UserConnectivityConstant.DOMAIN_COMPONENT);
			ldapContextPathBuilder.append(UserConnectivityConstant.EQUAL_SYMBOL);
			ldapContextPathBuilder.append(ldapDomainName);
		}
		if (!EphesoftStringUtil.isNullOrEmpty(ldapDomainOrganization)) {
			if (!EphesoftStringUtil.isNullOrEmpty(ldapContextPathBuilder.toString())) {
				ldapContextPathBuilder.append(UserConnectivityConstant.COMMA_SYMBOL);
			}
			ldapContextPathBuilder.append(UserConnectivityConstant.DOMAIN_COMPONENT);
			ldapContextPathBuilder.append(UserConnectivityConstant.EQUAL_SYMBOL);
			ldapContextPathBuilder.append(ldapDomainOrganization);
		}
		baseContextPath = ldapContextPathBuilder.toString();
		if (baseContextPath.isEmpty()) {
			LOG.error("Context path returned is empty.");
		} else {
			LOG.info("Base context path = " + baseContextPath);
		}
		return baseContextPath;
	}

	@Override
	public String getUserEmail(String userName) {
		LOG.info("Inside getUserEmail() in LdapConnectivity");
		String result = null;
		if (null != userName) {
			try {
				final DirContext dctx = getLDAPDirectoryContext();
				final SearchControls constraints = new SearchControls();
				constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
				final String[] attrIDs = new String[1];
				attrIDs[0] = UserConnectivityConstant.MAIL;
				LOG.info("User attributes added to constraints + " + attrIDs.toString());
				constraints.setReturningAttributes(attrIDs);

				final String paramConfig = getBaseContextPath();
				LOG.info(EphesoftStringUtil.concatenate("Context Path for LDAP :", paramConfig));
				final String paramName = EphesoftStringUtil.concatenate(userSearchAttribute, UserConnectivityConstant.EQUAL_SYMBOL,
						userName);
				LOG.info(EphesoftStringUtil.concatenate("User Name for LDAP :", paramName));
				LOG.info("dctx is null = " + (null == dctx) + " paramName = " + paramName + " paramConfig = " + paramConfig); 
				result = UserConnectivityUtil.searchUser(dctx, constraints, paramName, paramConfig);
			} catch (final NoSuchElementException noSuchEleExp) {
				LOG.error(noSuchEleExp.getMessage(), noSuchEleExp);
			}
		}
		LOG.error(EphesoftStringUtil.concatenate("Email for user ", userName, " is ", result));
		return result;
	}

}
