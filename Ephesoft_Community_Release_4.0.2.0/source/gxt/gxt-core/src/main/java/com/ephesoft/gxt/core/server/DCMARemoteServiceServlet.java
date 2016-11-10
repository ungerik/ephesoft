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

package com.ephesoft.gxt.core.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import  com.ephesoft.dcma.util.logger.EphesoftLogger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.core.common.AuthenticationType;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.UserType;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.Connections;
import com.ephesoft.dcma.da.service.BatchClassGroupsService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceGroupsService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.ConnectionsService;
import com.ephesoft.dcma.da.service.ManualStepHistoryService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.ApplicationContextUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.OSUtil;
import com.ephesoft.gxt.core.client.DCMARemoteService;
import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.server.security.AuthenticatorFactory;
import com.ephesoft.gxt.core.server.security.AuthorizerFactory;
import com.ephesoft.gxt.core.server.security.service.AuthenticationService;
import com.ephesoft.gxt.core.server.security.service.AuthorizationService;
import com.ephesoft.gxt.core.server.util.RequestUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.constant.Switch;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.DirtyCell;
import com.ephesoft.gxt.core.shared.dto.InitializeMetaData;
import com.ephesoft.gxt.core.shared.exception.UIException;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.log.UserLogger;
import com.ephesoft.gxt.log.constant.Screen;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class DCMARemoteServiceServlet extends RemoteServiceServlet implements DCMARemoteService {

	/**
	 * LOCALE_LANGUAGE_PROPERTY {@link String}.
	 */
	private static final String LOCALE_LANGUAGE_PROPERTY = "locale_language";

	private static final String SPACE = " ";

	private static final String REVIEW = "Review";

	private static final String REVIEW_BATCH_LIST_PRIORITY_FILTER = "reviewBatchListPriorityFilter";

	private static final String VALIDATE_BATCH_LIST_PRIORITY_FILTER = "validateBatchListPriorityFilter";

	private static final String RESTART_ALL_STATUS = "restartAllStatus";

	private static final String COULD_NOT_CLEAR_CURRENT_USER_ERROR_MSG = "Could not clear current user for batch id: ";

	private static final long serialVersionUID = 1L;

	private static final String SELECTED_BATCH_CLASS = "selectedBatchClass";

	private static final String HELP_URL_LINK = "help_url";

	private static final String footerText = "Powered by Ephesoft";

	private static final String footerLink = "http://www.ephesoft.com";

	private static EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(DCMARemoteServiceServlet.class);
	/**
	 * The USER_TYPE {@link String} is a constant for Ephesoft Cloud user type key.
	 */
	private static final String USER_TYPE = "user_type";

	/**
	 * String constant to get languages set in the request header.
	 */
	private static final String ACCEPT_LANGUAGE = "Accept-Language";

	/**
	 * String constant for comma.
	 */
	private static final String COMMA = ",";

	/**
	 * String constant for semicolon.
	 */
	private static final String SEMICOLON = ";";

	/**
	 * String constant for empty string.
	 */
	private static final String EMPTY_STRING = "";

	/**
	 * String constant for post fix value for english language.
	 */
	private static final String POSTFIX_FOR_ENGLISH_LOCALE = "en";

	/**
	 * String constant for post fix values of all the supported languages, separated by semicolon.
	 */
	private static final String POSTFIX_FOR_LANGUAGES = "en;tr;fr;es";

    private static final String DOCUMENT_DISPLAY_PROPERTY = "document.display_property";
	/**
	 * int constant for the number of characters in the postfix value of the locale languages.
	 */
	private static final int NUMBERS_OF_CHARACTERS_IN_POSTFIX = 2;

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Authentication type configured through web.xml for the Ephesoft Application.
	 */
	private final AuthenticationType authenticationType = EphesoftContext.getCurrent().getAuthenticationType();

	/**
	 * To hold instance of authentication service {@link AuthenticationService} configured through web.xml.
	 */
	private final AuthenticationService authenticationService = AuthenticatorFactory.getAuthenticator(authenticationType);

	/**
	 * To hold instance of authorisation service {@link AuthorizationService} configured through web.xml.
	 */
	private final AuthorizationService authorizationService = AuthorizerFactory.getAuthorizer(authenticationType);

	@Override
	public void initRemoteService() throws Exception {
		this.getThreadLocalRequest().getSession().removeAttribute(CoreCommonConstant.ALL_GROUPS);
		this.getThreadLocalRequest().getSession().removeAttribute(CoreCommonConstant.ALL_USERS);
		this.getThreadLocalRequest().getSession().removeAttribute(CoreCommonConstant.IS_SUPER_ADMIN);
	}

	@Override
	public String getBatchClassInfoFromSession() {

		// Changes done to fetch session data using util
		return RequestUtil.getSessionAttribute(this.getThreadLocalRequest(), SELECTED_BATCH_CLASS);
	}

	@Override
	public void setBatchClassInfoFromSession(final String batchClassInfo) {

		// Changes done to fetch session data using util
		RequestUtil.setSessionAttribute(this.getThreadLocalRequest(), SELECTED_BATCH_CLASS, batchClassInfo);
	}

	protected <T> T getSingleBeanOfType(final Class<T> type) throws NoSuchBeanDefinitionException {
		final WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		return ApplicationContextUtil.getSingleBeanOfType(ctx, type);
	}

	protected <T> T getBeanByName(final String name, final Class<T> type) throws NoSuchBeanDefinitionException {
		final WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		return ApplicationContextUtil.getBeanFromContext(ctx, name, type);
	}

	@Override
	public String getUserName() {

		// Common service added for authorisation and authentication
		final String userName = authorizationService.getUserName(this.getThreadLocalRequest());
		return (null == userName) ? CoreCommonConstant.EMPTY_STRING : userName;
	}

	@Override
	public Set<String> getUserRoles() {
		// Common service added for authorisation and authentication
		return authorizationService.getUserRoles(this.getThreadLocalRequest());
	}

	public Boolean isSuperAdmin() {

		// Common service added for authorisation and authentication
		return authorizationService.isSuperAdmin(this.getThreadLocalRequest());

	}

	public Set<String> getAllSuperAdminGroup() {

		// Common service added for authorisation and authentication
		return authorizationService.getAllSuperAdminGroup(this.getThreadLocalRequest());
	}

	@Override
	public final void doGet(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			processPost(request, response);
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
		} catch (final ServletException e) {
			log.error(e.getMessage(), e);
		} catch (final SerializationException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void acquireLock(final String batchInstanceIdentifier) throws UIException {
		final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		try {
			final String userName = getUserName();

			// 3126: Log batch owner name in separate file
			UserLogger.info(Screen.RV.getScreenName(), ":: User: ", userName, " trying to acquire lock on batch instance: ",
					batchInstanceIdentifier);
			final BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
			final String batchClassIdentifier = batchInstance.getBatchClass().getIdentifier();
			final Set<String> batchClassIdentifiers = getAllBatchClassByUserRoles();
			final Set<String> batchInstanceIdentifiers = getAllBatchInstanceByUserRoles();
			if (batchClassIdentifiers.contains(batchClassIdentifier)) {
				batchInstanceService.acquireBatchInstance(batchInstanceIdentifier, userName);
			} else if (batchInstanceIdentifiers.contains(batchInstanceIdentifier)) {
				batchInstanceService.acquireBatchInstance(batchInstanceIdentifier, userName);
			}

			// 3126: Log batch owner name in separate file
			UserLogger.info(Screen.RV.getScreenName(), ":: Batch Instance: ", batchInstanceIdentifier, " acquired by: ", userName);
		} catch (final BatchAlreadyLockedException batchAlreadyLockedException) {
			log.error(batchAlreadyLockedException.getMessage(), batchAlreadyLockedException);
			throw new UIException(batchAlreadyLockedException.getMessage());
		} catch (Exception lockAcquireException) {
			log.error(EphesoftStringUtil.concatenate(
					"Some exception occur while trying to acquire lock on BatchInstance with message: ",
					lockAcquireException.getMessage()), lockAcquireException);
			throw new UIException(lockAcquireException.getMessage());
		}
	}

	@Override
	public void cleanup() {
		final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		batchInstanceService.unlockAllBatchInstancesForCurrentUser(getUserName());
	}

	@Override
	public void cleanUpCurrentBatch(final String batchIdentifier) {
		final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);

		final BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchIdentifier);
		if (batchInstance != null) {
			final String currentUser = batchInstance.getCurrentUser();
			if (currentUser != null && currentUser.equalsIgnoreCase(getUserName())) {
				batchInstanceService.unlockCurrentBatchInstance(batchIdentifier);
			}
		} else {
			log.error(COULD_NOT_CLEAR_CURRENT_USER_ERROR_MSG + batchIdentifier);
		}
	}

	@Override
	public String logout(String httpUrl) {
		String logoutURL = null;
		cleanup();
		calculateUserReviewValidationTime(httpUrl);

		// Common service added for authorisation and authentication
		logoutURL = authenticationService.logout(this.getThreadLocalRequest());
		return logoutURL;
	}

	/**
	 * API to calculate the actual user review and validation time of batch instance
	 * 
	 * @param httpUrl
	 * @throws NoSuchBeanDefinitionException
	 */
	private void calculateUserReviewValidationTime(final String httpUrl) throws NoSuchBeanDefinitionException {
		String batchInstanceId = null;
		if (httpUrl.contains(REVIEW) && httpUrl.contains("?")) {
			final int index = httpUrl.lastIndexOf(SPACE);
			batchInstanceId = httpUrl.substring(index + 1);
			if (!StringUtil.isNullOrEmpty(batchInstanceId)) {
				final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
				final BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
				if (null != batchInstance) {
					final ManualStepHistoryService manualStepHistoryService = this.getSingleBeanOfType(ManualStepHistoryService.class);
					manualStepHistoryService.updateEndTimeAndCalculateDuration(batchInstanceId, batchInstance.getStatus().name(),
							getUserName());
				}
			}
		}
	}

	@Override
	public Boolean isReportingEnabled() {
		boolean isReportingEnabled = false;
		try {
			final ApplicationConfigProperties configProperties = ApplicationConfigProperties.getApplicationConfigProperties();
			isReportingEnabled = Boolean.parseBoolean(configProperties.getProperty("enable.reporting"));
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
			isReportingEnabled = false;
		}
		return isReportingEnabled;
	}

	@Override
	public Boolean isUploadBatchEnabled() {
		boolean isUploadBatchEnabled = false;
		try {
			final ApplicationConfigProperties configProperties = ApplicationConfigProperties.getApplicationConfigProperties();
			isUploadBatchEnabled = Boolean.parseBoolean(configProperties.getProperty("enable.uploadBatch"));
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
			isUploadBatchEnabled = false;
		}
		return isUploadBatchEnabled;
	}

	public Set<String> getAllGroups() {

		// Common service added for authorisation and authentication
		return authorizationService.getAllGroups(this.getThreadLocalRequest());
	}

	public Map<String, String> getAllUser() {

		// Common service added for authorization and authentication
		final Set<String> allUsersCommonName = authorizationService.getAllUser(this.getThreadLocalRequest());
		Map<String, String> mapFullNamewithCommonName = null;
		if (allUsersCommonName == null) {
			log.info("Set of common Names is empty. Returning Null.");
		} else {
			final int displayUserFullName = getFullNameReqPropValue();

			// Getting Full Name of Users if switch is on.
			if (displayUserFullName == Switch.ON.getState()) {
				mapFullNamewithCommonName = authorizationService.getFullNameofAllUsers(allUsersCommonName);
			} else {
				mapFullNamewithCommonName = new HashMap<String, String>(allUsersCommonName.size());
				for (final String userName : allUsersCommonName) {
					mapFullNamewithCommonName.put(userName, null);
				}
			}
		}
		return mapFullNamewithCommonName;
	}

	public Set<String> getAllBatchClassByUserRoles() {
		final BatchClassGroupsService batchClassGroupsService = this.getSingleBeanOfType(BatchClassGroupsService.class);
		final boolean isSuperAdmin = isSuperAdmin().booleanValue();
		final Set<String> userRoles = getUserRoles();
		Set<String> batchClassIdentifiersSet = new HashSet<String>();
		if (!isSuperAdmin) {
			final Set<String> batchClassIdentifiers = batchClassGroupsService.getBatchClassIdentifierForUserRoles(userRoles);
			if (batchClassIdentifiers != null) {
				batchClassIdentifiersSet = batchClassIdentifiers;
			}
		} else {
			final BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
			final List<String> allBatchClassIdentifiers = batchClassService.getAllBatchClassIdentifier();
			if (allBatchClassIdentifiers != null && allBatchClassIdentifiers.size() > 0) {
				for (final String batchClassIdentifier : allBatchClassIdentifiers) {
					batchClassIdentifiersSet.add(batchClassIdentifier);
				}
			}
		}
		return batchClassIdentifiersSet;
	}

	private Set<String> getAllBatchInstanceByUserRoles() {
		final BatchInstanceGroupsService batchInstanceGroupsService = this.getSingleBeanOfType(BatchInstanceGroupsService.class);
		final Set<String> userRoles = getUserRoles();
		return batchInstanceGroupsService.getBatchInstanceIdentifierForUserRoles(userRoles);
	}

	@Override
	public Map<BatchInstanceStatus, Integer> getBatchListPriorityFilter() {
		final Map<BatchInstanceStatus, Integer> priorityFilter = new HashMap<BatchInstanceStatus, Integer>(2);
		final Object reviewBatchListPriorityFilter = this.getThreadLocalRequest().getSession()
				.getAttribute(REVIEW_BATCH_LIST_PRIORITY_FILTER);
		final Object validateBatchListPriorityFilter = this.getThreadLocalRequest().getSession()
				.getAttribute(VALIDATE_BATCH_LIST_PRIORITY_FILTER);
		if (reviewBatchListPriorityFilter != null) {
			priorityFilter.put(BatchInstanceStatus.READY_FOR_REVIEW, (Integer) reviewBatchListPriorityFilter);
		}
		if (validateBatchListPriorityFilter != null) {
			priorityFilter.put(BatchInstanceStatus.READY_FOR_VALIDATION, (Integer) validateBatchListPriorityFilter);
		}
		return priorityFilter;
	}

	@Override
	public void setBatchListPriorityFilter(final Integer reviewBatchListPriority, final Integer validateBatchListPriority) {
		this.getThreadLocalRequest().getSession().setAttribute(REVIEW_BATCH_LIST_PRIORITY_FILTER, reviewBatchListPriority);
		this.getThreadLocalRequest().getSession().setAttribute(VALIDATE_BATCH_LIST_PRIORITY_FILTER, validateBatchListPriority);
	}

	@Override
	public Boolean isRestartAllBatchEnabled() {
		boolean isRestartAllBatchEnabled = false;
		final Object restartAllStatus = this.getThreadLocalRequest().getSession().getAttribute(RESTART_ALL_STATUS);
		try {
			if (null == restartAllStatus) {
				final ApplicationConfigProperties configProperties = ApplicationConfigProperties.getApplicationConfigProperties();
				isRestartAllBatchEnabled = Boolean.parseBoolean(configProperties.getProperty("enable.restart_all_batch"));
				this.getThreadLocalRequest().getSession().setAttribute(RESTART_ALL_STATUS, isRestartAllBatchEnabled);
			} else {
				isRestartAllBatchEnabled = (Boolean) restartAllStatus;
			}
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
			isRestartAllBatchEnabled = false;
		}
		return isRestartAllBatchEnabled;
	}

	@Override
	public Integer getBatchListTableRowCount() {
		Integer rowCount = null;
		try {
			final ApplicationConfigProperties configProperties = ApplicationConfigProperties.getApplicationConfigProperties();
			rowCount = Integer.parseInt(configProperties.getProperty("batchlist.table_row_count"));
		} catch (final Exception e) {
			log.info("batchlist.table_row_count is invalid or not exist in application.properties.Using default value: 15");
		}
		return rowCount;

	}

	@Override
	public BatchInstanceStatus getBatchListScreenTab(final String userName) {
		final BatchInstanceStatus batchListScreenTab = (BatchInstanceStatus) this.getThreadLocalRequest().getSession()
				.getAttribute(userName);
		return batchListScreenTab;
	}

	@Override
	public void setBatchListScreenTab(final String userName, final BatchInstanceStatus batchDTOStatus) {
		this.getThreadLocalRequest().getSession().setAttribute(userName, batchDTOStatus);
	}

	@Override
	public void disableRestartAllButton() {
		this.getThreadLocalRequest().getSession().setAttribute(RESTART_ALL_STATUS, Boolean.FALSE);
	}

	@Override
	public String getCurrentUser(final String batchInstanceIdentifier) {
		final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		final BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
		return batchInstance.getCurrentUser();
	}

	@Override
	public Boolean validateRegEx(final String regex) {
		boolean isValidRegExp = true;
		try {
			Pattern.compile(regex);
		} catch (final Exception e) {
			isValidRegExp = false;
		}
		return isValidRegExp;
	}

	@Override
	public Boolean validateRegExPattern(final String regex, final String value) {
		boolean isValidRegExp = true;
		try {
			Pattern pattern = Pattern.compile(regex);
			if (!pattern.matcher(value).matches())
				isValidRegExp = false;
		} catch (final Exception e) {
			isValidRegExp = false;
		}
		return isValidRegExp;
	}

	/**
	 * API to get the help url from properties file
	 * 
	 * @return
	 * @throws GWTException
	 */
	@Override
	public String getHelpUrl() throws UIException {
		String url = null;
		try {
			final ApplicationConfigProperties prop = ApplicationConfigProperties.getApplicationConfigProperties();
			url = prop.getProperty(HELP_URL_LINK);
			if (url == null) {
				log.error("Unable to read the help url from properties file");
				throw new UIException("Unable to get help url from properties file.");
			}
		} catch (final IOException ioe) {
			log.error("Unable to read the help url from properties file .Exception thrown is:" + ioe.getMessage(), ioe);
			throw new UIException("Unable to get help url from properties file.");
		}
		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.dcma.gwt.core.client.DCMARemoteService#getFooterProperties()
	 */
	@Override
	public Map<String, String> getFooterProperties() {

		final Map<String, String> footerProperties = new HashMap<String, String>();

		footerProperties.put(CoreCommonConstant.FOOTER_TEXT_KEY, footerText);
		footerProperties.put(CoreCommonConstant.FOOTER_LINK_KEY, footerLink);

		return footerProperties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.dcma.gwt.core.client.DCMARemoteService#getUserType()
	 */
	public Integer getUserType() {
		Integer userType = null;
		try {
			userType = Integer.parseInt(ApplicationConfigProperties.getApplicationConfigProperties().getProperty(USER_TYPE).trim());
		} catch (final NumberFormatException numberFormatException) {
			userType = UserType.OTHERS.getUserType();
			log.error("user type property is in wrong format in property file");
		} catch (final IOException ioException) {
			userType = UserType.OTHERS.getUserType();
			log.error("user type property is missing from property file");
		}
		return userType;
	}

	/**
	 * Gets the selected theme for the user from theme.properties file in folder theme in SharedFolders.
	 * 
	 * @return {@link String}-the selected theme name is returned
	 */
	@Override
	public String getSelectedTheme() {
		final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		final String pathToSharedFolder = batchSchemaService.getBaseFolderLocation();
		final String pathToThemeFolder = EphesoftStringUtil.concatenate(pathToSharedFolder, File.separator,
				LocaleCommonConstants.THEME_FOLDERNAME);
		final File themeFolder = new File(pathToThemeFolder);
		if (!themeFolder.exists()) {
			themeFolder.mkdir();
		}
		final String propFilePath = EphesoftStringUtil.concatenate(pathToThemeFolder, File.separator,
				LocaleCommonConstants.THEME_PROPERTY_FILENAME);

		String themeSelected = null;
		Properties properties = null;

		final File propertiesFile = new File(propFilePath);
		if (null != propertiesFile) {
			if (!propertiesFile.exists()) {
				try {
					propertiesFile.createNewFile();
				} catch (final IOException ioException) {
					log.error(EphesoftStringUtil.concatenate("Error in creating theme.properties file: ", ioException.getMessage()));
				}
			} else {
				final String userName = this.getUserName();
				if (null != userName) {
					try {
						properties = new Properties();
						properties.load(new FileInputStream(propFilePath));
						themeSelected = properties.getProperty(userName.toLowerCase());
					} catch (final FileNotFoundException fileNotFoundException) {
						log.error(EphesoftStringUtil.concatenate("File theme.properties not found: ",
								fileNotFoundException.getMessage()));
					} catch (final IOException ioException) {
						log.error(EphesoftStringUtil.concatenate("Error in loading theme.properties file: ", ioException.getMessage()));
					}
				} else {
					log.error("Username is null while setting theme.");
				}
			}
		}
		log.debug(EphesoftStringUtil.concatenate("Selected theme: ", themeSelected));
		return themeSelected;
	}

	/**
	 * Gets the list of languages set in the request parameter.
	 * 
	 * @param request HttpServletRequest- instance of HttpServletRequest.
	 * @return List<Locale>- list of languages set in the request parameter.
	 */
	private static List<Locale> getLanguageFromBrowser(final HttpServletRequest request) {
		String languages = null;
		List<Locale> normalizedLanguages = null;

		if (null != request) {
			languages = request.getHeader(ACCEPT_LANGUAGE);

			if (null != languages) {
				normalizedLanguages = new ArrayList<Locale>(languages.length());
				final String requestLanguages[] = EphesoftStringUtil.splitString(languages, COMMA);

				if (null != requestLanguages) {
					for (final String reqLang : requestLanguages) {
						String language = reqLang;

						if (null != language) {
							language = language.trim();
							if (language.length() == 0) {
								language = null;
							} else {

								/*
								 * To get the substring consisting of first 2 characters from the language set in the request parameter
								 * which will be used as post fix value for that language.
								 */
								if (NUMBERS_OF_CHARACTERS_IN_POSTFIX < language.length()) {
									language = language.substring(0, NUMBERS_OF_CHARACTERS_IN_POSTFIX);
								}
							}
						}
						normalizedLanguages.add(new Locale(language));
					}
				}
			}
		}

		return normalizedLanguages;
	}

	/**
	 * Returns the post fix value of the selected locale language.
	 * 
	 * @return String- post fix value of the selected locale language.
	 */
	@Override
	public String getLocale() {
		String locale = null;

		List<String> localeLanguage = null;
		final String languages = getLocaleLanguagePostFix();

		if (null != languages) {
			localeLanguage = new ArrayList<String>(languages.length());
			final String postFixValues[] = EphesoftStringUtil.splitString(languages, SEMICOLON);

			if (null != postFixValues) {
				for (final String postFix : postFixValues) {
					localeLanguage.add(postFix);
				}

				locale = getLanguageFromBrowser(getThreadLocalRequest()).get(0).toString();

				if (!localeLanguage.contains(locale)) {
					locale = POSTFIX_FOR_ENGLISH_LOCALE;
				}
				if (locale.equalsIgnoreCase(POSTFIX_FOR_ENGLISH_LOCALE)) {
					locale = EMPTY_STRING;
				}
			}
		}

		if (null == locale) {
			locale = EMPTY_STRING;
		}

		log.info("Locale: " + locale);

		return locale;
	}

	/**
	 * Returns the value of "locale_language" property from application.properties.
	 * 
	 * @return String- postFix values for all the supported languages, separated by semicolon.
	 */
	private String getLocaleLanguagePostFix() {
		String languages = null;
		try {
			languages = ApplicationConfigProperties.getApplicationConfigProperties().getProperty(LOCALE_LANGUAGE_PROPERTY);
		} catch (final IOException ioException) {
			log.error("Input ouput exception while reading the property: locale_language");
			languages = POSTFIX_FOR_LANGUAGES;
		}
		return languages;
	}

	public String getPropertiesValue(final String propertyName) {
		String propertyValue = null;
		try {
			final ApplicationConfigProperties configProperties = ApplicationConfigProperties.getApplicationConfigProperties();
			propertyValue = configProperties.getProperty(propertyName);
		} catch (final IOException ioException) {
			log.error(ioException.getMessage(), ioException);
		}
		return propertyValue;
	}

	public String[] getPropertiesValue(final String propertyName, final String splitPattern) {
		String[] resultArray = null;
		final String propertyValue = getPropertiesValue(propertyName);
		if (!EphesoftStringUtil.isNullOrEmpty(propertyValue)) {
			resultArray = EphesoftStringUtil.splitString(propertyValue, splitPattern);
		}
		return resultArray;
	}

	/**
	 * Returns the attribute from session. Returns null in case attribute passed is null.
	 * 
	 * @param attribute {@link String}- attribute whose value has to be fetched from the session
	 * @return {@link String}- value of attribute from the session
	 */
	@Override
	public String getAttributeFromSession(final String attribute) {
		if (null != attribute) {
			return (String) this.getThreadLocalRequest().getSession().getAttribute(attribute);
		}
		return null;
	}

	/**
	 * Sets the attribute into session.
	 * 
	 * @param attribute {@link String}-attribute whose value has to be stored in the session
	 * @param value {@link String}- value of the attribute
	 */
	@Override
	public void setAttributeInSession(final String attribute, final String value) {
		if (null != attribute) {
			this.getThreadLocalRequest().getSession().setAttribute(attribute, value);
		}
	}

	public Integer getAuthenticationType() {
		Integer tempAuthenticationType = 0;
		if (null != authenticationType) {
			tempAuthenticationType = authenticationType.getAuthenticationType();
		}
		return tempAuthenticationType;
	}

	@Override
	public String getUserFullName() {
		int displayUserFullName = getFullNameReqPropValue();

		// Common service added for authorisation and authentication
		String userName = authorizationService.getUserName(this.getThreadLocalRequest());

		// Reading switch value for displaying user's full name.
		if (displayUserFullName == Switch.ON.getState()) {
			userName = authorizationService.getUserFullName(this.getThreadLocalRequest());
		}
		if (null == userName) {
			return CoreCommonConstant.EMPTY_STRING;
		} else {
			return userName;
		}
	}

	/**
	 * Gets the property value for the user's full name required switch.
	 * 
	 * @return Integer value. 1 if the switch is on and 0 if its off.
	 */
	private int getFullNameReqPropValue() {
		int displayUserFullName = Switch.OFF.getState();
		try {
			final Properties userConnectivity = ApplicationConfigProperties.getApplicationConfigProperties().getAllProperties(
					EphesoftStringUtil.concatenate(CoreCommonConstant.USER_CONNECTIVITY_PROPERTIES_FOLDER, File.separator,
							CoreCommonConstant.USER_CONNECTIVITY_PROPERTIES_FILE));
			if (null != userConnectivity) {
				final String displayFullName = userConnectivity.getProperty(CoreCommonConstant.USERFULLNAME_REQUIRED_PROP);
				if (StringUtil.isNullOrEmpty(displayFullName)) {
					displayUserFullName = Switch.OFF.getState();
				} else {
					displayUserFullName = Integer.parseInt(displayFullName);
				}
			}
		} catch (final IOException ioException) {
			log.error(EphesoftStringUtil.concatenate("Error occured in getting properties.", ioException.getMessage()));
		} catch (final NumberFormatException numFormatException) {
			log.error(EphesoftStringUtil.concatenate("Error occured while parsing value from property file.",
					numFormatException.getMessage()));
		}
		return displayUserFullName;
	}

	@Override
	public Map<String, String> getFullNameOfFilteredUsers(final Set<String> commonNamesOfAllUsers) {
		Map<String, String> mapFullNamewithCommonName = null;
		if (null == commonNamesOfAllUsers) {
			log.info("Set of common names is empty. Returning null.");
		} else {
			final int displayUserFullName = getFullNameReqPropValue();

			// Getting Full Name of Users if switch is on.
			if (displayUserFullName == Switch.ON.getState()) {
				mapFullNamewithCommonName = authorizationService.getFullNameofAllUsers(commonNamesOfAllUsers);
			} else {
				mapFullNamewithCommonName = new HashMap<String, String>(commonNamesOfAllUsers.size());
				for (final String userName : commonNamesOfAllUsers) {
					mapFullNamewithCommonName.put(userName, null);
				}
			}
		}
		return mapFullNamewithCommonName;
	}

	@Override
	public InitializeMetaData initializeMetaData() {
		InitializeMetaData metaData = new InitializeMetaData();
		// Currently only initializes operating system.
		metaData.setOperatingSystemLinux(OSUtil.isUnix());
		metaData.setOperatingSystemWindows(OSUtil.isWindows());
		metaData.setSuperAdmin(isSuperAdmin());

		// sets Current user.
		metaData.setCurrentUser(getUserName());

		// Sets All Super Admin Groups
		metaData.setSuperAdminGroups(getAllSuperAdminGroup());

		// Sets All Groups
		metaData.setAllGroups(getAllGroups());

		metaData.setFooterLabel(footerText);
		metaData.setEphesoftURL(footerLink);
		int documentDisplayProperty = 0;
		try {
			documentDisplayProperty = Integer.parseInt(ApplicationConfigProperties.getApplicationConfigProperties().getProperty(DOCUMENT_DISPLAY_PROPERTY));
			metaData.setDocumentDisplayProperty(documentDisplayProperty);
		} catch (NumberFormatException numberFormatException) {
			LOGGER.error(numberFormatException, "Exception occured while fetching document display property.");
		} catch (IOException ioException) {
			LOGGER.error(ioException, "Exception occured while fetching document display property.");
		}

		this.getThreadLocalRequest().getSession().removeAttribute(CoreCommonConstant.IS_SUPER_ADMIN);
		return metaData;
	}

	@Override
	public List<ConnectionsDTO> getAllConnectionsDTO() {
		List<ConnectionsDTO> allConnectionsDTO = new ArrayList<ConnectionsDTO>();
		ConnectionsService pluginService = this.getSingleBeanOfType(ConnectionsService.class);
		List<Connections> allConnections = pluginService.getAllConnectionsExcludingDeleted();
		for (Connections connection : allConnections) {
			allConnectionsDTO.add(BatchClassUtil.createConnectionDTO(connection));
		}
		return allConnectionsDTO;
	}

	@Override
	public String getNotAllowedCharactersInBCName() throws UIException {
		String notAllowedBCCharacters = null;
		try {
			final ApplicationConfigProperties configProperties = ApplicationConfigProperties.getApplicationConfigProperties();
			notAllowedBCCharacters = configProperties.getProperty("batch_class_invalid_char_list");
			if (notAllowedBCCharacters == null) {
				log.error("Unable to read the allowed characters in batch class name  from properties file.");
				throw new UIException("Unable to read the allowed characters in batch class name from properties file.");
			}
		} catch (final IOException ioe) {
			log.error(
					"Unable to read the allowed characters in batch class name  from properties file.Exception thrown is:"
							+ ioe.getMessage(), ioe);
			throw new UIException("Unable to read the allowed characters in batch class name  from properties file.");
		}
		return notAllowedBCCharacters;

	}

	public Map<String, List<DirtyCell>> validateRegexPattern(final Map<String, List<DirtyCell>> dirtyCellsMap) {
		Map<String, List<DirtyCell>> invalidRegexPattern = new HashMap<String, List<DirtyCell>>();
		if (!CollectionUtil.isEmpty(dirtyCellsMap)) {
			for (Entry<String, List<DirtyCell>> entry : dirtyCellsMap.entrySet()) {
				String key = entry.getKey();
				LinkedList<DirtyCell> invalidDirtyCellList = new LinkedList<DirtyCell>();
				invalidRegexPattern.put(key, invalidDirtyCellList);
				List<DirtyCell> patternListToCheck = entry.getValue();
				if (!CollectionUtil.isEmpty(patternListToCheck)) {
					for (DirtyCell invalidCell : patternListToCheck) {
						if (null != invalidCell) {
							String invalidCellValue = invalidCell.getValue();
							invalidCellValue = invalidCellValue == null ? CoreCommonConstant.EMPTY_STRING : invalidCellValue;
							try {
								Pattern.compile(invalidCellValue);
							} catch (final Exception exception) {
								invalidDirtyCellList.add(invalidCell);
							}
						}
					}
				}
			}
		}
		return invalidRegexPattern;
	}
}
