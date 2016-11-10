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

package com.ephesoft.gxt.systemconfig.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.SerializationUtils;
import org.hibernate.StatelessSession;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ephesoft.dcma.batch.encryption.service.EncryptionKeyService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.PluginProcessCreationInfo;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao;
import com.ephesoft.dcma.core.hibernate.util.HibernateDaoUtil;
import com.ephesoft.dcma.da.dao.EncryptionKeyMetadataDao;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.Connections;
import com.ephesoft.dcma.da.domain.Dependency;
import com.ephesoft.dcma.da.domain.EncryptionKeyMetaData;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.PluginConfig;
import com.ephesoft.dcma.da.domain.PluginConfigSampleValue;
import com.ephesoft.dcma.da.domain.RegexGroup;
import com.ephesoft.dcma.da.domain.RegexPattern;
import com.ephesoft.dcma.da.property.DependencyTypeProperty;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.BatchClassPluginService;
import com.ephesoft.dcma.da.service.ConnectionsService;
import com.ephesoft.dcma.da.service.PluginConfigService;
import com.ephesoft.dcma.da.service.PluginService;
import com.ephesoft.dcma.da.service.RegexGroupService;
import com.ephesoft.dcma.da.service.RegexPatternService;
import com.ephesoft.dcma.encryption.util.EphesoftEncryptionUtil;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.XMLUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.service.DeploymentService;
import com.ephesoft.dcma.workflows.service.PluginWorkflowCreationService;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.server.BatchClassUtil;
import com.ephesoft.gxt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.gxt.core.server.util.RegexUtil;
import com.ephesoft.gxt.core.shared.constant.BatchConstants;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.ConnectionsResult;
import com.ephesoft.gxt.core.shared.dto.ImportPoolDTO;
import com.ephesoft.gxt.core.shared.dto.PluginConfigXmlDTO;
import com.ephesoft.gxt.core.shared.dto.PluginDependencyXmlDTO;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.core.shared.dto.PluginXmlDTO;
import com.ephesoft.gxt.core.shared.dto.RegexGroupDTO;
import com.ephesoft.gxt.core.shared.dto.RegexPatternDTO;
import com.ephesoft.gxt.core.shared.dto.Results;
import com.ephesoft.gxt.core.shared.exception.UIException;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.systemconfig.client.SystemConfigService;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.widget.property.ConnectionType;
import com.ephesoft.gxt.systemconfig.shared.SystemConfigSharedConstants;

/**
 * Service class to import/export regex pool, import/export table column pool, get list of regex groups and table column pool, get
 * license details.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Sep 9, 2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * 
 * @see com.ephesoft.dcma.gwt.systemConfig.client.SystemConfigService
 */
public class SystemConfigServiceImpl extends DCMARemoteServiceServlet implements SystemConfigService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * String constant for .ser extension.
	 */
	private static final String SERIALIZATION_EXT = FileType.SER.getExtensionWithDot();

	/**
	 * Keyword to detect OCR core property name.
	 */
	private static final String OCR_KEYWORD = "ocring";

	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(SystemConfigServiceImpl.class);

	@Override
	public Boolean addNewPlugin(String newPluginName, String xmlSourcePath, String jarSourcePath) throws UIException {

		LOGGER.info("Saving the new plugin with following details:\n Plugin Name:\t" + newPluginName + "\n Plugin Xml Path:\t"
				+ xmlSourcePath + "\n Plugin Jar path:\t" + jarSourcePath);
		PluginXmlDTO pluginXmlDTO = null;
		boolean pluginAdded = false;
		// Parse the data from xmlSourcePath file
		XPathFactory xFactory = new org.apache.xpath.jaxp.XPathFactoryImpl();
		XPath xpath = xFactory.newXPath();
		org.w3c.dom.Document pluginXmlDoc = null;

		try {
			pluginXmlDoc = XMLUtil.createDocumentFrom(FileUtils.getInputStreamFromZip(newPluginName, xmlSourcePath));
		} catch (Exception e) {
			String errorMsg = "Invalid xml content. Please try again.";
			LOGGER.error(errorMsg + e.getMessage(), e);
			throw new UIException(errorMsg);
		}

		if (pluginXmlDoc != null) {

			// correct syntax
			NodeList pluginNodeList = null;
			try {
				pluginNodeList = (NodeList) xpath.evaluate(SystemConfigSharedConstants.PLUGIN_EXPR, pluginXmlDoc,
						XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				String errorMsg = SystemConfigSharedConstants.INVALID_XML_CONTENT_MESSAGE;
				LOGGER.error(errorMsg + e.getMessage(), e);
				throw new UIException(errorMsg);
			}
			if (pluginNodeList != null && pluginNodeList.getLength() == 1) {
				LOGGER.info("Reading the Xml contents");
				String backUpFileName = SystemConfigSharedConstants.EMPTY_STRING;
				String jarName = SystemConfigSharedConstants.EMPTY_STRING;
				String methodName = SystemConfigSharedConstants.EMPTY_STRING;
				String description = SystemConfigSharedConstants.EMPTY_STRING;
				String pluginName = SystemConfigSharedConstants.EMPTY_STRING;
				String workflowName = SystemConfigSharedConstants.EMPTY_STRING;
				String scriptFileName = SystemConfigSharedConstants.EMPTY_STRING;
				String serviceName = SystemConfigSharedConstants.EMPTY_STRING;
				String pluginApplicationContextPath = SystemConfigSharedConstants.EMPTY_STRING;
				boolean isScriptingPlugin = false;
				boolean overrideExisting = false;
				try {
					backUpFileName = (String) xpath.evaluate(SystemConfigSharedConstants.BACK_UP_FILE_NAME_EXPR,
							pluginNodeList.item(0), XPathConstants.STRING);
					jarName = (String) xpath.evaluate(SystemConfigSharedConstants.JAR_NAME_EXPR, pluginNodeList.item(0),
							XPathConstants.STRING);
					methodName = (String) xpath.evaluate(SystemConfigSharedConstants.METHOD_NAME_EXPR, pluginNodeList.item(0),
							XPathConstants.STRING);
					description = (String) xpath.evaluate(SystemConfigSharedConstants.PLUGIN_DESC_EXPR, pluginNodeList.item(0),
							XPathConstants.STRING);
					pluginName = (String) xpath.evaluate(SystemConfigSharedConstants.PLUGIN_NAME_EXPR, pluginNodeList.item(0),
							XPathConstants.STRING);
					workflowName = (String) xpath.evaluate(SystemConfigSharedConstants.PLUGIN_WORKFLOW_NAME_EXPR,
							pluginNodeList.item(0), XPathConstants.STRING);
					scriptFileName = (String) xpath.evaluate(SystemConfigSharedConstants.SCRIPT_FILE_NAME_EXPR,
							pluginNodeList.item(0), XPathConstants.STRING);
					serviceName = (String) xpath.evaluate(SystemConfigSharedConstants.SERVICE_NAME_EXPR, pluginNodeList.item(0),
							XPathConstants.STRING);
					isScriptingPlugin = Boolean.parseBoolean((String) xpath.evaluate(
							SystemConfigSharedConstants.IS_SCRIPT_PLUGIN_EXPR, pluginNodeList.item(0), XPathConstants.STRING));
					pluginApplicationContextPath = (String) xpath.evaluate(SystemConfigSharedConstants.APPLICATION_CONTEXT_PATH,
							pluginNodeList.item(0), XPathConstants.STRING);
					overrideExisting = Boolean.parseBoolean((String) xpath.evaluate(SystemConfigSharedConstants.OVERRIDE_EXISTING,
							pluginNodeList.item(0), XPathConstants.STRING));
				} catch (Exception e) {
					String errorMsg = "Error in xml content. A mandatory tag is missing or invalid.";
					LOGGER.error(errorMsg + e.getMessage(), e);
					throw new UIException(errorMsg);
				}

				LOGGER.info("Back Up File Name: " + backUpFileName);
				LOGGER.info("Jar Name" + jarName);
				LOGGER.info("Method Name" + methodName);
				LOGGER.info("Description: " + description);
				LOGGER.info("Name: " + pluginName);
				LOGGER.info("Workflow Name" + workflowName);
				LOGGER.info("Script file Name" + scriptFileName);
				LOGGER.info("Service Name" + serviceName);
				LOGGER.info("Is scripting Plugin:" + isScriptingPlugin);
				LOGGER.info("Plugin application context path: " + pluginApplicationContextPath);
				if (!backUpFileName.isEmpty() && !jarName.isEmpty() && !methodName.isEmpty() && !description.isEmpty()
						&& !pluginName.isEmpty() && !workflowName.isEmpty() && !serviceName.isEmpty()
						&& !pluginApplicationContextPath.isEmpty()) {

					if (isScriptingPlugin && scriptFileName.isEmpty()) {
						String errorMsg = "Error in xml content. A mandatory field is missing.";
						LOGGER.error(errorMsg);
						throw new UIException(errorMsg);
					}
					pluginXmlDTO = setPluginInfo(backUpFileName, jarName, methodName, description, pluginName, workflowName,
							scriptFileName, serviceName, pluginApplicationContextPath, isScriptingPlugin, overrideExisting);

					extractPluginConfigs(pluginXmlDTO, xpath, pluginNodeList);

					extractPluginDependenciesFromXml(pluginXmlDTO, xpath, pluginNodeList);

					boolean pluginAlreadyExists = !checkIfPluginExists(pluginName);

					saveOrUpdatePluginToDB(pluginXmlDTO);
					createAndDeployPluginProcessDefinition(pluginXmlDTO);
					copyJarToLib(newPluginName, jarSourcePath);

					if (pluginAlreadyExists) {
						addPathToApplicationContext(pluginXmlDTO.getApplicationContextPath());
					}
					pluginAdded = true;
					LOGGER.info("Plugin added successfully.");
				} else {
					String errorMsg = SystemConfigSharedConstants.INVALID_XML_CONTENT_MESSAGE;
					LOGGER.error(errorMsg);
					throw new UIException(errorMsg);
				}
			} else {
				String errorMsg = "Invalid xml content. Number of plugins expected is one.";
				LOGGER.error(errorMsg);
				throw new UIException(errorMsg);
			}
		}

		return pluginAdded;
	}

	/**
	 * @param pluginXmlDTO
	 * @param backUpFileName
	 * @param jarName
	 * @param methodName
	 * @param description
	 * @param pluginNAme
	 * @param workflowName
	 * @param scriptFileName
	 * @param serviceName
	 * @param pluginApplicationContextPath
	 * @param isScriptingPlugin
	 * @param overrideExisting
	 */
	private PluginXmlDTO setPluginInfo(String backUpFileName, String jarName, String methodName, String description,
			String pluginNAme, String workflowName, String scriptFileName, String serviceName, String pluginApplicationContextPath,
			boolean isScriptingPlugin, boolean overrideExisting) {
		PluginXmlDTO pluginXmlDTO = new PluginXmlDTO();
		pluginXmlDTO.setBackUpFileName(backUpFileName);
		pluginXmlDTO.setJarName(jarName);
		pluginXmlDTO.setMethodName(methodName);
		pluginXmlDTO.setPluginDesc(description);
		pluginXmlDTO.setPluginName(pluginNAme);
		pluginXmlDTO.setPluginWorkflowName(workflowName);
		if (isScriptingPlugin) {
			pluginXmlDTO.setScriptFileName(scriptFileName);
		}
		pluginXmlDTO.setServiceName(serviceName);
		pluginXmlDTO.setIsScriptPlugin(isScriptingPlugin);
		pluginXmlDTO.setApplicationContextPath(pluginApplicationContextPath);
		pluginXmlDTO.setOverrideExisting(overrideExisting);

		return pluginXmlDTO;
	}

	/**
	 * @param pluginXmlDTO
	 * @param xpath
	 * @param pluginNodeList
	 * @throws
	 */
	private void extractPluginDependenciesFromXml(PluginXmlDTO pluginXmlDTO, XPath xpath, NodeList pluginNodeList) throws UIException {
		NodeList pluginDependenciesNode;
		try {
			pluginDependenciesNode = (NodeList) xpath.evaluate(SystemConfigSharedConstants.DEPENDENCIES_LIST_DEPENDENCY,
					pluginNodeList.item(0), XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			String errorMsg = "Invalid xml content. A mandatory field is missing.";
			LOGGER.error(errorMsg, e);
			throw new UIException(errorMsg);
		}
		LOGGER.info("Extracting Dependencies from xml:");

		List<PluginDependencyXmlDTO> pluginDependencyXmlDTOs = new ArrayList<PluginDependencyXmlDTO>(0);
		int numberOfDependencies = pluginDependenciesNode.getLength();
		LOGGER.info(numberOfDependencies + " dependencies found");
		for (int index = 0; index < numberOfDependencies; index++) {
			PluginDependencyXmlDTO pluginDependencyXmlDTO = new PluginDependencyXmlDTO();
			LOGGER.info("Plugin Dependency " + index + ":");
			String dependencyType = SystemConfigSharedConstants.EMPTY_STRING;
			String dependencyValue = SystemConfigSharedConstants.EMPTY_STRING;
			String operation = SystemConfigSharedConstants.EMPTY_STRING;
			try {
				dependencyType = (String) xpath.evaluate(SystemConfigSharedConstants.PLUGIN_DEPENDENCY_TYPE,
						pluginDependenciesNode.item(index), XPathConstants.STRING);
				dependencyValue = (String) xpath.evaluate(SystemConfigSharedConstants.PLUGIN_DEPENDENCY_VALUE,
						pluginDependenciesNode.item(index), XPathConstants.STRING);
				operation = (String) xpath.evaluate(SystemConfigSharedConstants.OPERATION, pluginDependenciesNode.item(index),
						XPathConstants.STRING);
			} catch (XPathExpressionException e) {
				String errorMsg = "Error in xml content. A mandatory field is missing.";
				LOGGER.error(errorMsg, e);
				throw new UIException(errorMsg);
			}

			if (!dependencyType.isEmpty() && !dependencyValue.isEmpty()) {
				LOGGER.info("Type: " + dependencyType);
				LOGGER.info("Value: " + dependencyValue);
				pluginDependencyXmlDTO.setPluginDependencyType(dependencyType);

				pluginDependencyXmlDTO.setPluginDependencyValue(dependencyValue);

				pluginDependencyXmlDTO.setOperation(operation);

				pluginDependencyXmlDTOs.add(pluginDependencyXmlDTO);
			}
		}
		pluginXmlDTO.setDependencyXmlDTOs(pluginDependencyXmlDTOs);
	}

	/**
	 * @param pluginXmlDTO
	 * @param xpath
	 * @param pluginPropertyNode
	 * @throws
	 */
	private void extractPluginConfigs(PluginXmlDTO pluginXmlDTO, XPath xpath, NodeList pluginNodeList) throws UIException {
		try {
			NodeList pluginPropertyNode = (NodeList) xpath.evaluate(SystemConfigSharedConstants.PLUGIN_PROPERTY_EXPR,
					pluginNodeList.item(0), XPathConstants.NODESET);
			LOGGER.info("Extracting plugin Configs from the xml");
			List<PluginConfigXmlDTO> pluginConfigXmlDTOs = new ArrayList<PluginConfigXmlDTO>(0);
			int numberOfPluginConfigs = pluginPropertyNode.getLength();

			LOGGER.info(numberOfPluginConfigs + " plugin configs found: ");
			for (int index = 0; index < numberOfPluginConfigs; index++) {
				LOGGER.info("Plugin config " + index + ": ");
				boolean isMandetory;
				boolean isMultiValue;
				String configName = SystemConfigSharedConstants.EMPTY_STRING;
				String propertyType = SystemConfigSharedConstants.EMPTY_STRING;
				String propertyDescription = SystemConfigSharedConstants.EMPTY_STRING;
				String operation = SystemConfigSharedConstants.EMPTY_STRING;

				isMandetory = Boolean.parseBoolean((String) xpath.evaluate(
						SystemConfigSharedConstants.PLUGIN_PROPERTY_IS_MANDETORY_EXPR, pluginPropertyNode.item(index),
						XPathConstants.STRING));
				isMultiValue = Boolean.parseBoolean((String) xpath.evaluate(
						SystemConfigSharedConstants.PLUGIN_PROPERTY_IS_MULTI_VALUES_EXPR, pluginPropertyNode.item(index),
						XPathConstants.STRING));
				configName = (String) xpath.evaluate(SystemConfigSharedConstants.PLUGIN_PROPERTY_NAME_EXPR,
						pluginPropertyNode.item(index), XPathConstants.STRING);
				propertyType = (String) xpath.evaluate(SystemConfigSharedConstants.PLUGIN_PROPERTY_TYPE_EXPR,
						pluginPropertyNode.item(index), XPathConstants.STRING);
				propertyDescription = (String) xpath.evaluate(SystemConfigSharedConstants.PLUGIN_PROPERTY_DESC_EXPR,
						pluginPropertyNode.item(index), XPathConstants.STRING);
				operation = (String) xpath.evaluate(SystemConfigSharedConstants.OPERATION, pluginPropertyNode.item(index),
						XPathConstants.STRING);

				LOGGER.info("Extracting values for config: " + index);
				LOGGER.info("Is Mandatory" + isMandetory);
				LOGGER.info("Is Multivalue" + isMultiValue);
				LOGGER.info("Config Name" + configName);
				LOGGER.info("Property Type" + propertyType);
				LOGGER.info("Property Description" + propertyDescription);
				if (!configName.isEmpty() && !propertyType.isEmpty() && !propertyDescription.isEmpty()) {
					PluginConfigXmlDTO pluginConfigXmlDTO = new PluginConfigXmlDTO();
					pluginConfigXmlDTO.setPluginPropertyIsMandatory(isMandetory);
					pluginConfigXmlDTO.setPluginPropertyIsMultiValues(isMultiValue);
					pluginConfigXmlDTO.setPluginPropertyName(configName);
					pluginConfigXmlDTO.setPluginPropertyType(propertyType);
					pluginConfigXmlDTO.setPluginPropertyDesc(propertyDescription);
					pluginConfigXmlDTO.setOperation(operation);
					NodeList sampleValuesNode = (NodeList) xpath.evaluate(
							SystemConfigSharedConstants.PLUGIN_PROPERTY_SAMPLE_VALUES_EXPR, pluginPropertyNode.item(index),
							XPathConstants.NODESET);
					List<String> sampleValuesList = new ArrayList<String>(0);
					LOGGER.info("Extracting sample values: ");
					int numberOfSampleValues = sampleValuesNode.getLength();

					LOGGER.info(numberOfSampleValues + " sample values found");
					for (int sampleValueIndex = 0; sampleValueIndex < numberOfSampleValues; sampleValueIndex++) {

						String sampleValue = (String) xpath.evaluate(SystemConfigSharedConstants.PLUGIN_PROPERTY_SAMPLE_VALUE_EXPR,
								sampleValuesNode.item(sampleValueIndex), XPathConstants.STRING);
						LOGGER.info("Sample value " + sampleValueIndex + " :" + sampleValue);
						sampleValuesList.add(sampleValue);
					}
					pluginConfigXmlDTO.setPluginPropertySampleValues(sampleValuesList);
					pluginConfigXmlDTOs.add(pluginConfigXmlDTO);
				} else {
					String errorMsg = SystemConfigSharedConstants.INVALID_XML_CONTENT_MESSAGE;
					LOGGER.error(errorMsg);
					throw new UIException(errorMsg);
				}
			}
			pluginXmlDTO.setConfigXmlDTOs(pluginConfigXmlDTOs);
		} catch (XPathExpressionException e) {
			String errorMsg = SystemConfigSharedConstants.INVALID_XML_CONTENT_MESSAGE;
			LOGGER.error(errorMsg, e);
			throw new UIException(errorMsg);
		}
	}

	/**
	 * @param jarSourcePath
	 * @param jarSourcePath2
	 * @throws
	 */
	private void copyJarToLib(String pluginName, String jarSourcePath) throws UIException {

		InputStream inputStream;
		try {
			inputStream = FileUtils.getInputStreamFromZip(pluginName, jarSourcePath);
		} catch (Exception e) {
			String errorMsg = " Jar file is either not present or corrupted.";
			LOGGER.error(errorMsg, e);
			throw new UIException(errorMsg);
		}
		FileOutputStream fileOutputStream = null;

		LOGGER.info("JAR file source path: " + jarSourcePath);
		StringBuffer destFilePath = new StringBuffer(System.getenv(SystemConfigSharedConstants.DCMA_HOME));
		String destinationFilePathString = destFilePath.toString();
		LOGGER.info("DCMA HOME: " + destinationFilePathString);
		// File srcFile = new File(jarSourcePath);
		destFilePath.append(File.separator);
		destFilePath.append(SystemConfigSharedConstants.WEB_INF);
		destFilePath.append(File.separator);
		destFilePath.append(SystemConfigSharedConstants.LIB);
		destFilePath.append(File.separator);
		destFilePath.append(jarSourcePath);
		destinationFilePathString = destFilePath.toString();
		LOGGER.info("Copying the JAR File in source path: " + jarSourcePath + " to " + destinationFilePathString);
		File destFile = new File(destinationFilePathString);
		try {
			fileOutputStream = new FileOutputStream(destFile);
		} catch (FileNotFoundException e) {
			String errorMsg = " The jar file could not be copied.";
			LOGGER.error(errorMsg, e);
			throw new UIException(errorMsg);
		}

		try {
			byte[] buf = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0) {
				fileOutputStream.write(buf, 0, len);
			}
			inputStream.close();
			fileOutputStream.close();
		} catch (FileNotFoundException fnfe) {
			String errorMsg = fnfe.getMessage() + " File not found";
			LOGGER.error(errorMsg, fnfe);
			throw new UIException(errorMsg);
		} catch (IOException ioe) {
			String errorMsg = ioe.getMessage() + " Error copying file";
			LOGGER.error(errorMsg, ioe);
			throw new UIException(errorMsg);
		}

	}

	/**
	 * @param pluginXmlDTO
	 * @throws IOException
	 */
	private void createAndDeployPluginProcessDefinition(PluginXmlDTO pluginXmlDTO) throws UIException {
		PluginWorkflowCreationService pluginWorkflowCreationService = this.getSingleBeanOfType(PluginWorkflowCreationService.class);
		DeploymentService deploymentService = this.getSingleBeanOfType(DeploymentService.class);

		File workflowDirectory = null;

		String newWorkflowBasePath = SystemConfigSharedConstants.EMPTY_STRING;

		try {
			Properties allProperties = ApplicationConfigProperties.getApplicationConfigProperties().getAllProperties(
					SystemConfigSharedConstants.META_INF_DCMA_WORKFLOWS_PROPERTIES);
			newWorkflowBasePath = allProperties.getProperty(SystemConfigSharedConstants.NEW_WORKFLOWS_BASE_PATH);
		} catch (IOException e) {
			LOGGER.error("Error reading workflow base path from the properties file.");
		}
		workflowDirectory = new File(newWorkflowBasePath);
		LOGGER.info("Workflow directory: " + workflowDirectory.getAbsolutePath());
		if (!workflowDirectory.exists()) {
			workflowDirectory.mkdir();
		}
		File newPluginDirectory = new File(workflowDirectory.getAbsolutePath() + File.separator + SystemConfigSharedConstants.PLUGINS
				+ File.separator + pluginXmlDTO.getPluginWorkflowName());
		LOGGER.info(EphesoftStringUtil.concatenate("Creating the plugins process definition directory + ",
				newPluginDirectory.getAbsolutePath(), " if not existing"));
		newPluginDirectory.mkdirs();

		StringBuffer pluginProcessDefinitionPath = new StringBuffer();
		pluginProcessDefinitionPath.append(workflowDirectory.getPath());
		pluginProcessDefinitionPath.append(File.separator);
		pluginProcessDefinitionPath.append(SystemConfigSharedConstants.PLUGINS);
		pluginProcessDefinitionPath.append(File.separator);
		pluginProcessDefinitionPath.append(pluginXmlDTO.getPluginWorkflowName());
		List<PluginProcessCreationInfo> pluginProcessCreationInfos = new ArrayList<PluginProcessCreationInfo>(0);
		LOGGER.info("Converting XML info into required form for creating process definition.");
		PluginProcessCreationInfo pluginProcessCreationInfo = new PluginProcessCreationInfo(pluginXmlDTO.getPluginWorkflowName(),
				pluginXmlDTO.getIsScriptPlugin(), pluginXmlDTO.getServiceName(), pluginXmlDTO.getMethodName());
		pluginProcessCreationInfos.add(pluginProcessCreationInfo);
		LOGGER.info("Creating process definition for plugin");
		try {
			String processDefinitionPath = pluginWorkflowCreationService.writeProcessDefinitions(newPluginDirectory.getPath(),
					pluginXmlDTO.getPluginWorkflowName(), pluginProcessCreationInfos);
			LOGGER.info("process definition created at " + processDefinitionPath);
			pluginProcessDefinitionPath.append(processDefinitionPath);
			LOGGER.info("Deploying process definition for plugin");
			deploymentService.deploy(pluginProcessDefinitionPath.toString());
		} catch (Exception exception) {
			String errorMsg = exception.getMessage() + " Error deploying plugin";
			LOGGER.error(errorMsg, exception);
			throw new UIException(errorMsg);
		}

	}

	private boolean checkIfPluginExists(String pluginName) {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		Plugin pluginCheck = pluginService.getPluginPropertiesForPluginName(pluginName);

		boolean pluginExists = !(pluginCheck == null);

		return pluginExists;
	}

	private void saveOrUpdatePluginToDB(PluginXmlDTO pluginXmlDTO) throws UIException {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);

		String pluginName = pluginXmlDTO.getPluginName();

		boolean pluginExists = checkIfPluginExists(pluginName);

		if (!pluginExists) {
			// If plugin does not exists, create one always
			LOGGER.info(pluginName + " plugin does not exists, so creating one.");

			LOGGER.info("Preparing plugin object from the XMl content");
			Plugin newPlugin = new Plugin();
			try {
				newPlugin = preparePluginObject(pluginXmlDTO);
			} catch (Exception e) {
				String errorMsg = " Data in xml is invalid or corrupted";
				LOGGER.error(errorMsg, e);
				throw new UIException(errorMsg);
			}
			LOGGER.info("Preparing plugin config object from the XMl content");
			List<PluginConfig> pluginConfigs = preparePluginConfigObjects(pluginXmlDTO);
			// Create new Plugin
			LOGGER.info("Creating new plugin: " + newPlugin.getPluginName());
			// Plugin does not exists and need to be created
			pluginService.createNewPlugin(newPlugin);
			for (PluginConfig newPluginConfig : pluginConfigs) {
				// Associate new plugin with new Plugin configs
				newPluginConfig.setPlugin(newPlugin);

				PluginConfig pluginConfigCheck = pluginConfigService.getPluginConfigByName(newPluginConfig.getName());
				if (pluginConfigCheck == null) {
					// save new Plugin configs
					LOGGER.info("Creating new plugin config: " + newPluginConfig.getName());
					pluginConfigService.createNewPluginConfig(newPluginConfig);
				} else {
					String errorMsg = "Error saving new plugin config. A plugin config with same name already exists.";
					LOGGER.error(errorMsg);
					throw new UIException(errorMsg);
				}
			}
		} else {
			LOGGER.info(pluginName + " plugin already exists.");
			if (pluginXmlDTO.getOverrideExisting()) {

				LOGGER.info(SystemConfigSharedConstants.OVERRIDE_EXISTING + " tag is true, so Overriding the plugin.");
				Plugin pluginCheck = pluginService.getPluginPropertiesForPluginName(pluginName);
				if (pluginCheck != null) {
					updateExistingPlugin(pluginCheck, pluginXmlDTO);
				}
				LOGGER.info("Plugin contents merged, now updating in db.");
				pluginService.mergePlugin(pluginCheck);
			} else {
				LOGGER.info(SystemConfigSharedConstants.OVERRIDE_EXISTING + " tag is false, so not Overriding the plugin.");
				String errorMsg = " A plugin with same name already exists.";
				LOGGER.error(errorMsg);
				throw new UIException(errorMsg);
			}
		}

	}

	private void updateExistingPlugin(Plugin pluginCheck, PluginXmlDTO pluginXmlDTO) throws UIException {

		LOGGER.info("Updating plugin properties for plugin name: " + pluginCheck.getPluginName());

		String pluginDesc = pluginXmlDTO.getPluginDesc();
		LOGGER.info("Plugin description: " + pluginDesc);
		pluginCheck.setDescription(pluginDesc);

		String scriptFileName = pluginXmlDTO.getScriptFileName();
		LOGGER.info("Plugin Script Name: " + scriptFileName);
		pluginCheck.setScriptName(scriptFileName);

		updateRevisionNumber(pluginCheck);
		LOGGER.info("Plugin revision number: " + pluginCheck.getVersion());

		String pluginWorkflowName = pluginXmlDTO.getPluginWorkflowName();
		LOGGER.info("Plugin Workflow Name: " + pluginWorkflowName);
		pluginCheck.setWorkflowName(pluginWorkflowName);

		updatePluginConfig(pluginCheck, pluginXmlDTO);

		updatePluginDependencies(pluginCheck, pluginXmlDTO);

	}

	private void updatePluginDependencies(Plugin pluginCheck, PluginXmlDTO pluginXmlDTO) throws UIException {
		try {
			List<Dependency> dependencyList = getDependencyFromXml(pluginXmlDTO, pluginCheck);
			pluginCheck.getDependencies().clear();

			LOGGER.info("Updating new Dependencies for plugin name: " + pluginCheck.getPluginName());
			pluginCheck.getDependencies().addAll(dependencyList);
		} catch (Exception e) {
			String errorMsg = "Error: " + "Invalid Dependencies name.";
			LOGGER.error(errorMsg, e);
			throw new UIException(errorMsg);
		}
	}

	private void updatePluginConfig(Plugin pluginCheck, PluginXmlDTO pluginXmlDTO) {

		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);
		for (PluginConfigXmlDTO pluginConfigXmlDTO : pluginXmlDTO.getConfigXmlDTOs()) {

			String pluginConfigOperation = pluginConfigXmlDTO.getOperation();
			PluginConfig newPluginConfig = setPluginConfigObject(pluginConfigXmlDTO);

			PluginConfig existingPluginConfig = pluginConfigService.getPluginConfigByName(newPluginConfig.getName());

			boolean pluginConfigAlreadyExists = false;
			if (existingPluginConfig != null) {
				pluginConfigAlreadyExists = true;
			}

			if (pluginConfigOperation.equalsIgnoreCase(SystemConfigSharedConstants.ADD_OPERATION) || pluginConfigOperation.isEmpty()) {
				LOGGER.info("Adding new Plugin config with name: " + newPluginConfig.getName());
				if (pluginConfigAlreadyExists) {
					LOGGER.error("Plugin config you are trying to add, already exists. Please try different name.");
				} else {
					createNewPluginConfig(pluginCheck, newPluginConfig);
				}
			} else if (pluginConfigOperation.equalsIgnoreCase(SystemConfigSharedConstants.UPDATE_OPERATION)) {
				LOGGER.info("Updating Plugin config with name: " + newPluginConfig.getName());
				if (pluginConfigAlreadyExists) {
					// update existing
					updateExistingPluginConfig(pluginCheck, newPluginConfig, existingPluginConfig);
				} else {
					// Create new one
					LOGGER.info(newPluginConfig.getName() + " plugin does not exists, creating new one.");
					createNewPluginConfig(pluginCheck, newPluginConfig);
				}
			} else if (pluginConfigOperation.equalsIgnoreCase(SystemConfigSharedConstants.DELETE_OPERATION)) {
				LOGGER.info("Deleting Plugin config with name: " + newPluginConfig.getName());
				if (pluginConfigAlreadyExists) {
					// before removing the plugin config, update the references
					removePluginConfig(existingPluginConfig);

				} else {
					LOGGER.error("Plugin config you are trying to delete, does not exist. Please make sure you enter the correct name.");
				}
			}

		}
	}

	/**
	 * @param pluginCheck
	 * @param pluginConfigService
	 * @param newPluginConfig
	 * @param existingPluginConfig
	 */
	private void updateExistingPluginConfig(Plugin pluginCheck, PluginConfig newPluginConfig, PluginConfig existingPluginConfig) {
		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);
		LOGGER.info("Merge new properties for plugin config: " + newPluginConfig.getName());
		mergePluginConfig(existingPluginConfig, newPluginConfig);
		existingPluginConfig.setPlugin(pluginCheck);
		pluginConfigService.updatePluginConfig(existingPluginConfig);

		updateBatchClassPluginConfigs(existingPluginConfig);
	}

	/**
	 * @param batchClassPluginConfigService
	 * @param existingPluginConfig
	 */
	private void removePluginConfig(PluginConfig existingPluginConfig) {
		BatchClassPluginConfigService batchClassPluginConfigService = this.getSingleBeanOfType(BatchClassPluginConfigService.class);
		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);

		String pluginConfigName = existingPluginConfig.getName();
		LOGGER.info("Removing all the instances of batch class plugin configs for plugin config: " + pluginConfigName);
		List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPluginConfigService
				.getBatchClassPluginConfigForPluginConfigId(existingPluginConfig.getId());
		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			LOGGER.info("Removing batch class plugin config with id: " + batchClassPluginConfig.getId());
			batchClassPluginConfigService.removeBatchClassPluginConfig(batchClassPluginConfig);
		}
		LOGGER.info("Removing the plugin config with name: " + pluginConfigName);
		pluginConfigService.removePluginConfig(existingPluginConfig);
	}

	/**
	 * @param pluginCheck
	 * @param pluginConfigService
	 * @param newPluginConfig
	 * @return
	 */
	private void createNewPluginConfig(Plugin pluginCheck, PluginConfig newPluginConfig) {
		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);
		LOGGER.info("Creating new plugin config with name: " + newPluginConfig.getName());
		PluginConfig existingPluginConfig;
		existingPluginConfig = newPluginConfig;
		existingPluginConfig.setPlugin(pluginCheck);
		pluginConfigService.createNewPluginConfig(existingPluginConfig);
		addBatchClassPluginConfig(existingPluginConfig);
	}

	private void updateBatchClassPluginConfigs(PluginConfig existingPluginConfig) {
		BatchClassPluginConfigService batchClassPluginConfigService = this.getSingleBeanOfType(BatchClassPluginConfigService.class);
		String pluginConfigName = existingPluginConfig.getName();

		LOGGER.info("Updating all the instances of batch class plugin configs for plugin config: " + pluginConfigName);
		List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPluginConfigService
				.getBatchClassPluginConfigForPluginConfigId(existingPluginConfig.getId());

		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			batchClassPluginConfig.setPluginConfig(existingPluginConfig);
			setDefaultValueForNewConfig(batchClassPluginConfig);
		}
	}

	private void addBatchClassPluginConfig(PluginConfig existingPluginConfig) {
		BatchClassPluginService batchClassPluginService = this.getSingleBeanOfType(BatchClassPluginService.class);

		String pluginConfigName = existingPluginConfig.getName();

		long pluginId = existingPluginConfig.getPlugin().getId();
		LOGGER.info("Adding new instances of batch class plugin configs for plugin config: " + pluginConfigName);
		List<BatchClassPlugin> batchClassPlugins = batchClassPluginService.getBatchClassPluginForPluginId(pluginId);

		for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
			BatchClassPluginConfig batchClassPluginConfig = new BatchClassPluginConfig();
			LOGGER.info("Creating new Batch class plugin config for plugin id: " + pluginId + " in batch class plugin with id: "
					+ batchClassPlugin.getId());
			batchClassPluginConfig.setPluginConfig(existingPluginConfig);
			setDefaultValueForNewConfig(batchClassPluginConfig);
			batchClassPlugin.addBatchClassPluginConfig(batchClassPluginConfig);
			LOGGER.info("Updating Batch class plugin.");
			batchClassPluginService.updateBatchClassPlugin(batchClassPlugin);
		}

	}

	/**
	 * @param pluginConfig
	 * @param batchClassPluginConfig
	 */
	private void setDefaultValueForNewConfig(BatchClassPluginConfig batchClassPluginConfig) {
		LOGGER.info("Setting default value for batch class plugin configs.");
		DataType pluginConfigDataType = batchClassPluginConfig.getPluginConfig().getDataType();
		LOGGER.info("Plugin config is of type: " + pluginConfigDataType.name());
		if (pluginConfigDataType == DataType.BOOLEAN) {
			LOGGER.info("Plugin config is boolean data type, so default value will be " + BatchConstants.YES);
			batchClassPluginConfig.setValue(BatchConstants.YES);
		} else {
			boolean isMandatory = batchClassPluginConfig.getPluginConfig().isMandatory();
			if (pluginConfigDataType == DataType.STRING && isMandatory) {
				List<String> sampleValues = batchClassPluginConfig.getSampleValue();
				if (sampleValues == null || sampleValues.isEmpty()) {
					LOGGER.info("Plugin config is String data type with no sample values, so default value will be"
							+ BatchConstants.DEFAULT);
					batchClassPluginConfig.setValue(BatchConstants.DEFAULT);
				} else if (sampleValues.size() > 0) {
					String defaultSampleValue = sampleValues.get(0);
					LOGGER.info("Plugin config is String data type with sample values as " + sampleValues.toArray().toString()
							+ ", so default value will be" + defaultSampleValue);
					batchClassPluginConfig.setValue(defaultSampleValue);
				}
			} else if (pluginConfigDataType == DataType.INTEGER && isMandatory) {
				LOGGER.info("Plugin config is Integer data type, so default value will be" + BatchConstants.ZERO);
				batchClassPluginConfig.setValue(BatchConstants.ZERO);
			}
		}
	}

	private void mergePluginConfig(PluginConfig existingPluginConfig, PluginConfig newPluginConfig) {
		LOGGER.info("Merge plugin config with name: " + existingPluginConfig.getName());

		DataType dataType = newPluginConfig.getDataType();
		LOGGER.info("Plugin Config Data type: " + dataType);
		existingPluginConfig.setDataType(dataType);

		String description = newPluginConfig.getDescription();
		LOGGER.info("Plugin Config description: " + description);
		existingPluginConfig.setDescription(description);

		boolean mandatory = newPluginConfig.isMandatory();
		LOGGER.info("Plugin Config is Mandatory: " + mandatory);
		existingPluginConfig.setMandatory(mandatory);

		Boolean multiValue = newPluginConfig.isMultiValue();
		LOGGER.info("Plugin Config is Multi Value: " + multiValue);
		existingPluginConfig.setMultiValue(multiValue);

		LOGGER.info("Clearing existing sample values " + existingPluginConfig.getSampleValue().toArray().toString()
				+ " for existing plugin config");
		existingPluginConfig.getPluginConfigSampleValues().clear();

		for (PluginConfigSampleValue pluginConfigSampleValue : newPluginConfig.getPluginConfigSampleValues()) {
			LOGGER.info("Adding new sample values " + pluginConfigSampleValue.getSampleValue() + " for Plugin config: "
					+ existingPluginConfig.getName());
			pluginConfigSampleValue.setPluginConfig(null);
			existingPluginConfig.getPluginConfigSampleValues().add(pluginConfigSampleValue);
		}
	}

	public void updateRevisionNumber(Plugin pluginCheck) {
		LOGGER.info("Updating Plugin revision number for plugin : " + pluginCheck.getPluginName());

		String version = pluginCheck.getVersion();
		if (null != version) {

			String newVersion = version;
			if (version.contains(".")) {
				int lastIndex = version.lastIndexOf('.');
				String preFix = version.substring(0, lastIndex);
				String postFix = version.substring(lastIndex + 1);
				int revNumber = Integer.parseInt(postFix);
				revNumber++;
				newVersion = preFix + "." + revNumber;
			} else {
				int revNumber = Integer.parseInt(version);
				revNumber++;
				newVersion = String.valueOf(revNumber);
			}

			LOGGER.info("Updating Plugin revision number to " + newVersion + " for plugin : " + pluginCheck.getPluginName());
			pluginCheck.setVersion(newVersion);
		}
	}

	private List<PluginConfig> preparePluginConfigObjects(PluginXmlDTO pluginXmlDTO) {
		List<PluginConfig> pluginConfigs = new ArrayList<PluginConfig>(0);

		LOGGER.info("Preparing plugin config objects.");
		for (PluginConfigXmlDTO pluginConfigXmlDTO : pluginXmlDTO.getConfigXmlDTOs()) {
			PluginConfig pluginConfig = setPluginConfigObject(pluginConfigXmlDTO);
			pluginConfigs.add(pluginConfig);
		}
		return pluginConfigs;
	}

	/**
	 * @param pluginConfigXmlDTO
	 * @return
	 */
	private PluginConfig setPluginConfigObject(PluginConfigXmlDTO pluginConfigXmlDTO) {
		PluginConfig pluginConfig = new PluginConfig();
		pluginConfig.setDescription(pluginConfigXmlDTO.getPluginPropertyDesc());
		pluginConfig.setMandatory(pluginConfigXmlDTO.getPluginPropertyIsMandatory());
		pluginConfig.setMultiValue(pluginConfigXmlDTO.getPluginPropertyIsMultiValues());
		pluginConfig.setName(pluginConfigXmlDTO.getPluginPropertyName());
		pluginConfig.setDataType(DataType.getDataType(pluginConfigXmlDTO.getPluginPropertyType()));

		List<PluginConfigSampleValue> pluginConfigSampleValues = new ArrayList<PluginConfigSampleValue>();
		for (String sampleValue : pluginConfigXmlDTO.getPluginPropertySampleValues()) {
			PluginConfigSampleValue pluginConfigSampleValue = new PluginConfigSampleValue();
			pluginConfigSampleValue.setPluginConfig(pluginConfig);
			pluginConfigSampleValue.setSamplevalue(sampleValue);

			pluginConfigSampleValues.add(pluginConfigSampleValue);
		}
		pluginConfig.setPluginConfigSampleValues(pluginConfigSampleValues);
		return pluginConfig;
	}

	private Plugin preparePluginObject(PluginXmlDTO pluginXmlDTO) throws UIException {
		Plugin plugin = new Plugin();

		plugin.setPluginName(pluginXmlDTO.getPluginName());
		plugin.setWorkflowName(pluginXmlDTO.getPluginWorkflowName());
		plugin.setDescription(pluginXmlDTO.getPluginDesc());
		if (pluginXmlDTO.getIsScriptPlugin()) {
			plugin.setScriptName(pluginXmlDTO.getScriptFileName());
		}
		plugin.setVersion(SystemConfigSharedConstants.VERSION);
		List<Dependency> dependenciesList = plugin.getDependencies();

		if (dependenciesList == null) {
			dependenciesList = new ArrayList<Dependency>();
		}

		dependenciesList = getDependencyFromXml(pluginXmlDTO, plugin);
		plugin.setDependencies(dependenciesList);
		return plugin;
	}

	/**
	 * @param pluginXmlDTO
	 * @param plugin
	 * @param dependenciesList
	 * @throws UIException
	 */
	private List<Dependency> getDependencyFromXml(PluginXmlDTO pluginXmlDTO, Plugin plugin) throws UIException {
		List<Dependency> dependenciesList = new ArrayList<Dependency>();
		for (PluginDependencyXmlDTO dependencyXmlDTO : pluginXmlDTO.getDependencyXmlDTOs()) {
			Dependency dependency = null;
			dependency = prepareDependency(plugin, dependencyXmlDTO);
			if (dependency != null) {
				dependenciesList.add(dependency);
			}
		}
		return dependenciesList;
	}

	/**
	 * @param plugin
	 * @param dependencyXmlDTO
	 * @param dependency
	 * @return
	 * @throws UIException
	 */
	private Dependency prepareDependency(Plugin plugin, PluginDependencyXmlDTO dependencyXmlDTO) throws UIException {
		Dependency dependency = new Dependency();
		if (validateDependenciesNameList(dependencyXmlDTO.getPluginDependencyValue())) {
			dependency = new Dependency();
			final DependencyTypeProperty dependencyType = BatchClassUtil.getDependencyTypePropertyFromValue(dependencyXmlDTO
					.getPluginDependencyType());
			if (dependencyType != null) {
				dependency.setDependencyType(dependencyType);
				dependency.setDependencies(getDependenciesIdentifierString(dependencyXmlDTO.getPluginDependencyValue()));
				dependency.setId(0);
				dependency.setPlugin(plugin);
			} else {
				String errorMsg = "Error: " + "Invalid Dependency type \"" + dependencyType + "\".";
				LOGGER.error(errorMsg);
				throw new UIException(errorMsg);
			}

		} else {
			String errorMsg = "Error: " + "Invalid Dependencies name.";
			LOGGER.error(errorMsg);
			throw new UIException(errorMsg);
		}
		return dependency;
	}

	private String getDependenciesIdentifierString(String dependencies) {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);

		LOGGER.info("Converting dependencies name to ID's");
		String[] andDependencies = dependencies.split(SystemConfigSharedConstants.AND);
		StringBuffer andDependenciesNameAsString = new StringBuffer();

		for (String andDependency : andDependencies) {
			if (!andDependenciesNameAsString.toString().isEmpty()) {
				andDependenciesNameAsString.append(SystemConfigSharedConstants.AND);
			}
			String[] orDependencies = andDependency.split(SystemConfigSharedConstants.OR_CONSTANT);
			StringBuffer orDependenciesNameAsString = new StringBuffer();

			for (String dependencyName : orDependencies) {
				if (!orDependenciesNameAsString.toString().isEmpty()) {
					orDependenciesNameAsString.append(SystemConfigSharedConstants.OR_CONSTANT);
				}
				String dependencyId = String.valueOf(pluginService.getPluginPropertiesForPluginName(dependencyName).getId());
				LOGGER.info("Dependency Id for Dependency Name: " + dependencyName + " : " + dependencyId);
				orDependenciesNameAsString.append(dependencyId);

			}
			andDependenciesNameAsString.append(orDependenciesNameAsString);

			orDependenciesNameAsString = new StringBuffer();
		}

		return andDependenciesNameAsString.toString();
	}

	private boolean validateDependenciesNameList(String pluginDependencyValue) {
		boolean isValidated = false;
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);

		LOGGER.info("Validating dependencies name.");
		String[] andDependencies = pluginDependencyValue.split(SystemConfigSharedConstants.AND);

		for (String andDependency : andDependencies) {

			String[] orDependencies = andDependency.split(SystemConfigSharedConstants.OR_CONSTANT);

			for (String dependencyName : orDependencies) {
				if (pluginService.getPluginPropertiesForPluginName(dependencyName) == null) {
					isValidated = false;
					String errorMsg = "No dependency found with name " + dependencyName;
					LOGGER.error(errorMsg);
					break;
				} else {
					isValidated = true;
				}
			}

		}
		return isValidated;
	}

	@Override
	public List<PluginDetailsDTO> getAllPluginDetailDTOs() {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		List<PluginDetailsDTO> pluginDetailsDTOs = new ArrayList<PluginDetailsDTO>(0);
		LOGGER.info("Getting all plugin details from DB");
		List<Plugin> allPluginsNames = pluginService.getAllPlugins();
		for (Plugin plugin : allPluginsNames) {
			LOGGER.info("Preparing plugin object for " + plugin.getPluginName() + " plugin");
			pluginDetailsDTOs.add(BatchClassUtil.createPluginDetailsDTO(plugin, pluginService));
		}
		return pluginDetailsDTOs;
	}

	private PluginDetailsDTO updatePlugin(PluginDetailsDTO pluginDetailsDTO) {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		String pluginIdentifier = pluginDetailsDTO.getIdentifier();
		LOGGER.info("Updating the plugin object with id: " + pluginIdentifier);

		try {
			Long pluginId = Long.valueOf(pluginIdentifier);
			Plugin plugin = pluginService.getPluginPropertiesForPluginId(pluginId);
			LOGGER.info("Merging the changes in plugin.");
			BatchClassUtil.mergePluginFromDTO(plugin, pluginDetailsDTO, pluginService);
			LOGGER.info("updating plugin");
			pluginService.mergePlugin(plugin);
			pluginDetailsDTO = BatchClassUtil.createPluginDetailsDTO(plugin, pluginService);
		} catch (NumberFormatException e) {
			LOGGER.error("Error converting number " + e.getMessage(), e);
		}
		return pluginDetailsDTO;
	}

	@Override
	public List<PluginDetailsDTO> updateAllPluginDetailsDTOs(List<PluginDetailsDTO> allPluginDetailsDTO) {
		List<PluginDetailsDTO> allUpdatedPluginDetailsDTO = new ArrayList<PluginDetailsDTO>(0);
		LOGGER.info("Updating all plugins");
		for (PluginDetailsDTO pluginDetailsDTO : allPluginDetailsDTO) {
			if (pluginDetailsDTO.isDirty()) {
				LOGGER.info(pluginDetailsDTO.getPluginName() + " plugin will be updated");
				final PluginDetailsDTO updatedPluginDetailsDto = updatePlugin(pluginDetailsDTO);
				if (updatedPluginDetailsDto != null) {
					allUpdatedPluginDetailsDTO.add(updatedPluginDetailsDto);
				}
			} else {
				LOGGER.info(pluginDetailsDTO.getPluginName() + " plugin does not need update");
				allUpdatedPluginDetailsDTO.add(pluginDetailsDTO);
			}
		}
		return allUpdatedPluginDetailsDTO;
	}

	private void addPathToApplicationContext(String pluginApplicationContextPath) throws UIException {
		StringBuffer applicationContextFilePathBuffer = new StringBuffer(System.getenv(SystemConfigSharedConstants.DCMA_HOME));
		applicationContextFilePathBuffer.append(File.separator);
		applicationContextFilePathBuffer.append(SystemConfigSharedConstants.APPLICATION_CONTEXT_PATH_XML);
		String applicationContextFilePath = applicationContextFilePathBuffer.toString();
		File applicationContextFile = new File(applicationContextFilePath);
		LOGGER.info("Making entry for " + pluginApplicationContextPath + " in the application context file at: "
				+ applicationContextFilePath);
		try {
			Document xmlDocument = XMLUtil.createDocumentFrom(applicationContextFile);

			NodeList beanTags = xmlDocument.getElementsByTagName(SystemConfigSharedConstants.BEANS_TAG);
			if (beanTags != null) {
				String searchTag = SystemConfigSharedConstants.BEANS_TAG;
				// Get the 1st bean node from
				Node beanNode = getFirstNodeOfType(beanTags, searchTag);

				Node importNodesClone = null;

				NodeList beanChildNodes = beanNode.getChildNodes();
				searchTag = SystemConfigSharedConstants.IMPORT_TAG;
				importNodesClone = getFirstNodeOfType(beanChildNodes, searchTag);
				Node cloneNode = importNodesClone.cloneNode(true);
				cloneNode.getAttributes().getNamedItem(SystemConfigSharedConstants.RESOURCE)
						.setTextContent(SystemConfigSharedConstants.CLASSPATH_META_INF + pluginApplicationContextPath);
				beanNode.insertBefore(cloneNode, importNodesClone);

				Source source = new DOMSource(xmlDocument);

				File batchXmlFile = new File(applicationContextFilePath);

				Result result = new StreamResult(batchXmlFile);

				Transformer xformer = null;
				try {
					xformer = TransformerFactory.newInstance().newTransformer();
					xformer.setOutputProperty(OutputKeys.INDENT, SystemConfigSharedConstants.YES);
					xformer.setOutputProperty(SystemConfigSharedConstants.XML_INDENT_AMOUNT, String.valueOf(2));

				} catch (TransformerConfigurationException e) {
					String errorMsg = SystemConfigSharedConstants.APPLICATION_CONTEXT_ENTRY_ERROR_MESSAGE;
					LOGGER.error(errorMsg + e.getMessage(), e);
					throw new UIException(errorMsg);
				} catch (TransformerFactoryConfigurationError e) {
					String errorMsg = SystemConfigSharedConstants.APPLICATION_CONTEXT_ENTRY_ERROR_MESSAGE;
					LOGGER.error(errorMsg + e.getMessage(), e);
					throw new UIException(errorMsg);
				}
				try {
					xformer.transform(source, result);
				} catch (TransformerException e) {
					String errorMsg = SystemConfigSharedConstants.APPLICATION_CONTEXT_ENTRY_ERROR_MESSAGE;
					LOGGER.error(errorMsg + e.getMessage(), e);
					throw new UIException(errorMsg);
				}
				LOGGER.info("Application Context Entry made successfully.");
			}
		} catch (Exception e) {
			String errorMsg = SystemConfigSharedConstants.APPLICATION_CONTEXT_ENTRY_ERROR_MESSAGE;
			LOGGER.error(errorMsg + e.getMessage());
			throw new UIException(errorMsg);
		}
	}

	/**
	 * 
	 * @param beanChildNodes
	 * @param searchTag
	 * @return
	 */
	private Node getFirstNodeOfType(NodeList beanChildNodes, String searchTag) {
		Node importNodesClone;
		int index = 0;
		importNodesClone = beanChildNodes.item(index);
		while (!importNodesClone.getNodeName().equalsIgnoreCase(searchTag)) {
			index++;
			importNodesClone = beanChildNodes.item(index);
		}
		return importNodesClone;
	}

	@Override
	public Boolean deletePlugin(final PluginDetailsDTO pluginDetailsDTO) throws UIException {
		Boolean isPluginDeleted = false;
		BatchClassPluginService batchClassPluginService = this.getSingleBeanOfType(BatchClassPluginService.class);
		final String pluginIdentifier = pluginDetailsDTO.getIdentifier();
		final String pluginName = pluginDetailsDTO.getPluginName();
		LOGGER.info("Attempting to Delete plugin with id : " + pluginIdentifier + " name: " + pluginName);
		final long pluginId = Long.parseLong(pluginIdentifier);
		final List<BatchClassPlugin> batchClassPlugins = batchClassPluginService.getBatchClassPluginForPluginId(pluginId);

		if (batchClassPlugins == null || batchClassPlugins.isEmpty()) {
			LOGGER.info("No Batch Class is using this plugin. So delete is allowed.");
			deletePluginFromDb(pluginId);
			isPluginDeleted = true;
		} else {
			String errorMessage = SystemConfigSharedConstants.PLUGIN_IN_USE_MESSAGE;
			LOGGER.error(errorMessage);
			throw new UIException(errorMessage);
		}
		if (isPluginDeleted) {
			LOGGER.info(pluginName + " plugin deleted successfully.");

		} else {
			LOGGER.info(pluginName + " plugin NOT deleted.");
		}

		return isPluginDeleted;
	}

	private void deletePluginFromDb(final long pluginId) {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		Plugin plugin = pluginService.getPluginPropertiesForPluginId(pluginId);
		LOGGER.info("Deleting Plugin with id : " + pluginId);
		pluginService.removePluginAndReferences(plugin, true);

	}

	@Override
	public boolean isApplicationKeyGenerated() throws UIException {
		final EncryptionKeyMetadataDao encryptionKeyMetadataDao = this.getSingleBeanOfType(EncryptionKeyMetadataDao.class);
		final EncryptionKeyMetaData applicationKeyMetaData = encryptionKeyMetadataDao.getApplicationKeyMetadata();
		final boolean isApplicationKeyGenerated = applicationKeyMetaData == null ? true : applicationKeyMetaData.isKeyGenerated();
		return isApplicationKeyGenerated;
	}

	@Override
	public boolean generateApplicationLevelKey(String applicationKey) throws UIException {
		boolean keyGenerated = false;
		try {
			if (!EphesoftStringUtil.isNullOrEmpty(applicationKey)) {
				final EncryptionKeyService encryptionKeyService = this.getSingleBeanOfType(EncryptionKeyService.class);
				encryptionKeyService.generateApplicationKey(applicationKey.getBytes(SystemConfigConstants.UTF_8_ENCODER));
				keyGenerated = true;
			}
		} catch (final Exception exception) {
			LOGGER.error(exception.getMessage());
			throw new UIException("Error occured in generating application level key ");
		}
		return keyGenerated;
	}

	/**
	 * Returns the list of all regex groups.
	 * 
	 * @return Collection<RegexGroupDTO>- returns list of all the regex groups present in database.
	 */
	@Override
	public Collection<RegexGroupDTO> getRegexGroupDTOList() {
		// final BatchClassDTO batchClassDTO = new BatchClassDTO();
		// //final RegexPoolService regexPoolService = this.getSingleBeanOfType(RegexPoolService.class);
		// //final Map<String, RegexGroupDTO> regexGroupDTOMap = getRegexGroupMap();
		// final Map<String, RegexGroupDTO> regexGroupDTOMap = regexPoolService.getRegexGroupMap();
		// batchClassDTO.setRegexGroupMap(regexGroupDTOMap);
		// final Collection<RegexGroupDTO> regexGroupDTOList = batchClassDTO.getRegexGroups();
		// return regexGroupDTOList;
		return null;
	}

	/**
	 * Imports new regex groups, which are not already present in database. Returns Map containing all imported file names with
	 * corresponding value , depending on the success of import.
	 * 
	 * @param importPoolDTOs {@link ImportRegexPoolDTO}- list of imported regex pool DTO.
	 * @return Map<String, Boolean>
	 */
	@Override
	public Map<String, Boolean> importRegexGroups(List<ImportPoolDTO> importPoolDTOs) {
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
		if (!CollectionUtil.isEmpty(importPoolDTOs)) {
			for (ImportPoolDTO importRegexPoolDTO : importPoolDTOs) {
				if (null != importRegexPoolDTO) {
					String serializableFilePath = FileUtils.getFileNameOfTypeFromFolder(importRegexPoolDTO.getZipFileLocation(),
							SERIALIZATION_EXT);
					InputStream serializableFileStream = null;
					try {
						if (!StringUtil.isNullOrEmpty(serializableFilePath)) {
							serializableFileStream = new FileInputStream(serializableFilePath);
							Object deserializedObject = SerializationUtils.deserialize(serializableFileStream);
							if (deserializedObject instanceof List<?>) {
								boolean instanceOfRegexGroup = true;
								List<?> deserializedList = (List<?>) deserializedObject;
								for (Object object : deserializedList) {
									if (!(object instanceof RegexGroup)) {
										instanceOfRegexGroup = false;
										break;
									}
								}
								if (instanceOfRegexGroup) {

									// List of imported regex groups
									@SuppressWarnings("unchecked")
									final List<RegexGroup> importRegexGroupList = (List<RegexGroup>) deserializedObject;
									insertRegexGroups(importRegexGroupList);
									resultMap.put(importRegexPoolDTO.getZipFileName(), true);
								} else {
									resultMap.put(importRegexPoolDTO.getZipFileName(), false);
								}
							} else {
								resultMap.put(importRegexPoolDTO.getZipFileName(), false);
							}
						} else {
							resultMap.put(importRegexPoolDTO.getZipFileName(), false);
						}
					} catch (final FileNotFoundException fileNotFoundException) {
						LOGGER.error(fileNotFoundException, "Error while importing regex groups: ", fileNotFoundException.getMessage());
						resultMap.put(importRegexPoolDTO.getZipFileName(), false);
					} catch (final DataAccessException dataAccessException) {
						LOGGER.error(dataAccessException, "Error while importing regex groups: ", dataAccessException.getMessage());
						resultMap.put(importRegexPoolDTO.getZipFileName(), false);
					}
				}
			}
		}
		return resultMap;
	}

	/**
	 * Inserts all regex groups for persistence in database. Transaction roll backs if any one insertion fails.
	 * 
	 * @param importRegexGroupList {@link List<{@link RegexGroup}>}
	 */
	@Transactional
	private void insertRegexGroups(final List<RegexGroup> importRegexGroupList) {
		if (!CollectionUtil.isEmpty(importRegexGroupList)) {
			final RegexGroupService regexGroupService = this.getSingleBeanOfType(RegexGroupService.class);
			List<RegexGroup> existingRegexGroupList = regexGroupService.getRegexGroups();
			for (final RegexGroup regexGroup : importRegexGroupList) {

				// If regex group does not exist in database, only then it is imported.Otherwise if a new pattern is added to regex
				// group then it is added to existing group.
				RegexGroup alreadyPresentRegexGroup = RegexUtil.getRegexGroupFromList(existingRegexGroupList, regexGroup);
				if (null != existingRegexGroupList && null != alreadyPresentRegexGroup) {
					boolean isPatternAddedToRegexGroup = false;
					for (RegexPattern patternToImport : regexGroup.getRegexPatterns()) {
						boolean isregexPatternAlreadyPresent = alreadyPresentRegexGroup.isRegexPatternExist(patternToImport);
						if (!isregexPatternAlreadyPresent) {
							LOGGER.info(EphesoftStringUtil.concatenate("Adding Regex pattern : ", patternToImport.getPattern(),
									" in regex group :", alreadyPresentRegexGroup.getName()));
							alreadyPresentRegexGroup.addRegexPattern(patternToImport);
							isPatternAddedToRegexGroup = true;
						}
					}
					if (isPatternAddedToRegexGroup) {
						LOGGER.info(EphesoftStringUtil.concatenate("Updating regex group :", alreadyPresentRegexGroup.getName()));
						regexGroupService.updateRegexGroup(alreadyPresentRegexGroup);
					}
				} else {
					regexGroupService.insertRegexGroup(regexGroup);
					LOGGER.info("Regex group: ", regexGroup.getName(), " imported.");
				}
			}
		}
	}

	/**
	 * Returns the license details like license expiry date, CPU license count, etc.
	 * 
	 * @return {@link Map}<{@link String}, {@link String}>: license details
	 */
	@Override
	public Map<String, String> getLicenseDetails() {
		String licenseInfoString = null;
		Map<String, String> licenseDetailMap = null;

		LOGGER.debug("License details: ", licenseInfoString);

		final String[] licenseDetails = EphesoftStringUtil.splitString(licenseInfoString, CoreCommonConstant.COMMA);

		if (null != licenseDetails) {
			int indexOfFirstColon = 0;
			String licensePropertyName = null;
			String licensePropertyValue = null;
			licenseDetailMap = new LinkedHashMap<String, String>(licenseDetails.length);
			for (final String licenseDetail : licenseDetails) {
				indexOfFirstColon = licenseDetail.indexOf(CoreCommonConstant.COLON);
				if ((indexOfFirstColon > 0) && (indexOfFirstColon < licenseDetail.length())) {
					licensePropertyName = licenseDetail.substring(0, indexOfFirstColon - 1);
					licensePropertyValue = licenseDetail.substring(indexOfFirstColon + CoreCommonConstant.TWO);

					LOGGER.debug("License Property Name: ", licensePropertyName, "and License Property Value: ", licensePropertyValue);

					if (CoreCommonConstant.EXPIRY_DATE_LICENSE_PROPERTY_NAME.equalsIgnoreCase(licensePropertyName)) {
						final DateFormat formatter = new SimpleDateFormat(CoreCommonConstant.EXTENDED_DATE_FORMAT);
						formatter.setTimeZone(TimeZone.getTimeZone(CoreCommonConstant.GMT));
						try {
							final Date date = formatter.parse(licensePropertyValue);
							licensePropertyValue = date.toGMTString();
						} catch (final ParseException parseException) {
							LOGGER.error(parseException, "Error while parsing the date", parseException.getMessage());
						}
					}

					if (CoreCommonConstants.ON.equals(licensePropertyValue) || CoreCommonConstants.OFF.equals(licensePropertyValue)) {
						licensePropertyValue = licensePropertyValue.toUpperCase();
					}

					if (CoreCommonConstant.MUTLISERVER_SWITCH_LICENSE_PROPERTY_NAME.equalsIgnoreCase(licensePropertyName)) {
						licensePropertyName = CoreCommonConstant.MUTLISERVER_SWITCH;
					}

					// Fix for Client Ticket #3340 changed name of advance reporting switch label.
					if (CoreCommonConstant.ADVANCE_REPORTING_SWITCH_PROPERTY_NAME.equalsIgnoreCase(licensePropertyName)) {
						licensePropertyName = CoreCommonConstant.ADVANCED_REPORTING_SWITCH;
					}

					if (CoreCommonConstant.EXPIRY_DATE_MESSAGE_LICENSE_PROPERTY_NAME.equalsIgnoreCase(licensePropertyName)) {
						licensePropertyName = CoreCommonConstant.LICENSE_EXPIRATION_DISPLAY_MESSGAE;
						final String[] licensePropertyTokens = EphesoftStringUtil.splitString(licensePropertyValue,
								CoreCommonConstant.SPACE);
						if (null != licensePropertyTokens) {
							licensePropertyValue = EphesoftStringUtil.concatenate(licensePropertyTokens[0], CoreCommonConstant.SPACE,
									CoreCommonConstant.DAYS);
						}

					}

					licenseDetailMap.put(licensePropertyName, licensePropertyValue);
				}
			}

			// Removing license failOver value into License detail UI
			// ClusterPropertyService clusterPropertyService =
			// this.getSingleBeanOfType(ClusterPropertyService.class);
			// licensePropertyName =
			// SystemConfigConstants.LICENSE_SERVER_FO_HEADER;
			//
			// ClusterProperty failOverProperty = clusterPropertyService
			// .getClusterPropertyValue(ClusterPropertyType.LICENSE_FAILOVER_MECHANISM);
			// if (null != failOverProperty) {
			// String switchStatus = failOverProperty.getPropertyValue();
			// if (!EphesoftStringUtil.isNullOrEmpty(switchStatus)) {
			// if (CoreCommonConstants.ON.equalsIgnoreCase(switchStatus)) {
			// licensePropertyValue = CoreCommonConstants.ON.toUpperCase();
			// } else {
			// licensePropertyValue = CoreCommonConstants.OFF.toUpperCase();
			// }
			// }
			// }
			// licenseDetailMap.put(licensePropertyName, licensePropertyValue);
		}
		return licenseDetailMap;
	}

	@Override
	public boolean testConnection(String connectionURL, String userName, String password, String driverClass) {
		boolean connectionSuccessful = false;
		if (!StringUtil.isNullOrEmpty(connectionURL) || !StringUtil.isNullOrEmpty(userName) || !StringUtil.isNullOrEmpty(password)
				|| !StringUtil.isNullOrEmpty(driverClass)) {
			try {
				final DynamicHibernateDao dao = HibernateDaoUtil.testHibernateDaoConnection(driverClass, connectionURL, userName,
						password);
				try {
					StatelessSession session = dao.getStatelessSession();
					connectionSuccessful = true;
					session.close();
				} catch (DCMAException dcmaException) {
					// TODO Auto-generated catch block
					connectionSuccessful = false;
					LOGGER.error(StringUtil.concatenate("Unable to establish connection.", dcmaException.getMessage()));
				} finally {
					dao.closeSession();
				}

			} catch (DCMAException dcmaException) {
				// TODO Auto-generated catch block
				LOGGER.info(StringUtil.concatenate("Unable to connect to Database.", dcmaException.getMessage()));
			}
		}
		return connectionSuccessful;
	}

	@Override
	public boolean testMSSQLAlwaysONConnection(String databaseName, String connectionURL) {
		boolean connectionSuccessful = false;
		if (!StringUtil.isNullOrEmpty(databaseName) && !StringUtil.isNullOrEmpty(connectionURL)) {
			java.sql.Connection conn = null;
			try {
				Class.forName(ConnectionType.MSSQL_ALWAYSON.getDriver());
				conn = DriverManager.getConnection(connectionURL);
				if (conn != null) {
					connectionSuccessful = true;
				}
			} catch (SQLException sqlException) {
				log.error("Unable to make connection" + sqlException.getMessage());
			} catch (ClassNotFoundException classNotFoundException) {
				// TODO Auto-generated catch block
				log.error("Unable to make connection" + classNotFoundException.getMessage());
			} finally {
				try {
					if (conn != null) {
						conn.close();

					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return connectionSuccessful;
	}

	@Override
	public List<ConnectionsDTO> updateAllConnectionsDTOs(List<ConnectionsDTO> allConnectionsDTO) {
		List<ConnectionsDTO> allUpdatedConnectionsDTO = new ArrayList<ConnectionsDTO>(0);
		LOGGER.info("Updating all plugins");
		for (ConnectionsDTO connectionDTO : allConnectionsDTO) {
			if (connectionDTO.isDirty()) {
				LOGGER.info(connectionDTO.getConnectionName() + " connection will be updated");
				final ConnectionsDTO updatedConnectionDto = updateConnection(connectionDTO);
				if (updatedConnectionDto != null) {
					allUpdatedConnectionsDTO.add(updatedConnectionDto);
				}
			} else if (connectionDTO.isNew()) {
				LOGGER.info(connectionDTO.getConnectionName() + " connection will be created");
				final ConnectionsDTO updatedConnectionDto = createNewConnection(connectionDTO);
				if (updatedConnectionDto != null) {
					allUpdatedConnectionsDTO.add(updatedConnectionDto);
				}
			} else {
				LOGGER.info(connectionDTO.getConnectionName() + " connection does not need update");
				allUpdatedConnectionsDTO.add(connectionDTO);
			}
		}
		return allUpdatedConnectionsDTO;
	}

	private ConnectionsDTO updateConnection(ConnectionsDTO connectionDTO) {
		ConnectionsService connectionsService = this.getSingleBeanOfType(ConnectionsService.class);
		String connectionsIdentifier = connectionDTO.getIdentifier();
		LOGGER.info("Updating the connection object with id: " + connectionsIdentifier);

		try {
			Long connectionId = Long.valueOf(connectionsIdentifier);
			Connections connection = connectionsService.getConnectionForId(connectionId);
			LOGGER.info("Merging the changes in connection.");
			String encryptPassword = null;
			if (!EphesoftEncryptionUtil.isPasswordStringEncrypted(connectionDTO.getPassword())) {
				encryptPassword = EphesoftEncryptionUtil.getEncryptedPasswordString(connectionDTO.getPassword());
				connectionDTO.setPassword(encryptPassword);
			} else {
				encryptPassword = connectionDTO.getPassword();
			}
			BatchClassUtil.mergeConnectionFromDTO(connection, connectionDTO);
			LOGGER.info("updating connection");
			connectionsService.mergeConnection(connection);
			connectionDTO = BatchClassUtil.createConnectionDTO(connection);
			connectionDTO.setDecryptedPassword(EphesoftEncryptionUtil.getDecryptedPasswordString(encryptPassword));
		} catch (NumberFormatException e) {
			LOGGER.error("Error converting number " + e.getMessage(), e);
		}
		return connectionDTO;
	}

	private ConnectionsDTO createNewConnection(ConnectionsDTO connectionDTO) {
		ConnectionsService connectionsService = this.getSingleBeanOfType(ConnectionsService.class);
		String connectionsIdentifier = connectionDTO.getIdentifier();
		LOGGER.info("Updating the connection object with id: " + connectionsIdentifier);

		try {
			Connections connection = new Connections();
			LOGGER.info("Merging the changes in connection.");
			String encryptPassword = EphesoftEncryptionUtil.getEncryptedPasswordString(connectionDTO.getPassword());
			connectionDTO.setPassword(encryptPassword);
			BatchClassUtil.mergeConnectionFromDTO(connection, connectionDTO);
			LOGGER.info("updating connection");
			connectionsService.mergeConnection(connection);
			connectionDTO = BatchClassUtil.createConnectionDTO(connection);
			connectionDTO.setDecryptedPassword(EphesoftEncryptionUtil.getDecryptedPasswordString(encryptPassword));
		} catch (NumberFormatException e) {
			LOGGER.error("Error converting number " + e.getMessage(), e);
		}
		return connectionDTO;
	}

	@Override
	public ConnectionsResult removeSelectedConnection(List<ConnectionsDTO> selectedConnection) {
		Results returnValue = Results.FAILURE;
		int failedDeletions = 0;
		int totalDeletions = 0;
		ConnectionsService connectionsService = this.getSingleBeanOfType(ConnectionsService.class);
		List<ConnectionsDTO> allConnections = null;
		ConnectionsResult connectionsResult = new ConnectionsResult();
		try {
			if (!CollectionUtil.isEmpty(selectedConnection)) {
				totalDeletions = selectedConnection.size();
				for (ConnectionsDTO connectionDTO : selectedConnection) {
					final String identifier = connectionDTO.getIdentifier();
					if (!StringUtil.isNullOrEmpty(identifier)) {
						long id = Long.valueOf(connectionDTO.getIdentifier());
						Connections connection = connectionsService.getConnectionForId(id);
						if (null != connection) {
							boolean isConnectionInUse = connectionsService.isConnectionInUse(connection.getId());
							if (!isConnectionInUse) {
								connection.setDeleted(true);
								connectionsService.mergeConnection(connection);
							} else {
								failedDeletions++;
							}
						}
					}
				}
			}
			if (failedDeletions == totalDeletions) {
				returnValue = Results.FAILURE;
			} else if (failedDeletions == 0) {
				returnValue = Results.SUCCESSFUL;
			} else {
				returnValue = Results.PARTIAL_SUCCESS;
			}
			allConnections = getAllConnectionsDTO();
			connectionsResult.setAllConnections(allConnections);
			connectionsResult.setConnectionResult(returnValue);
		} catch (NumberFormatException noFormatException) {
			LOGGER.error(StringUtil.concatenate("Error converting id into long ", noFormatException.getMessage()));
		}
		return connectionsResult;
	}

	/**
	 * Finds all the matches inside the string for the given regex pattern and returns the list of matched indexes.
	 * 
	 * @param regex {@link String} The regex pattern generated.
	 * @param strToBeMatched {@link String} The string which is to be matched.
	 * @return {@link List<{@link String}> The list of matched indexes.
	 * @throws Exception if any exception or error occur.
	 */
	@Override
	public List<String> findMatchedIndexesList(final String regex, final String strToBeMatched) throws UIException {
		List<String> matchedIndexList = null;
		try {
			if (!StringUtil.isNullOrEmpty(regex) && strToBeMatched != null) {
				matchedIndexList = new ArrayList<String>();
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(strToBeMatched);
				while (matcher.find()) {
					matchedIndexList.add(String.valueOf(matcher.start()));
					matchedIndexList.add(String.valueOf(matcher.end()));
				}
			}
		} catch (final PatternSyntaxException patternSyntaxException) {
			throw new UIException("Invalid Regex Pattern.");
		}
		return matchedIndexList;
	}

	/**
	 * used for getting the map of avaliable REgex groups from DB
	 * 
	 * @return {@link Map<String, RegexGroupDTO>}
	 */
	@Override
	public Map<String, RegexGroupDTO> getRegexGroupMap() {
		Map<String, RegexGroupDTO> regexGroupDTOMap = new LinkedHashMap<String, RegexGroupDTO>();
		List<RegexGroupDTO> listOfGroupDTOs = getRegexGroupList();
		for (RegexGroupDTO groupDTO : listOfGroupDTOs) {
			if (null != groupDTO) {
				regexGroupDTOMap.put(groupDTO.getIdentifier(), groupDTO);
			}
		}
		return regexGroupDTOMap;

	}

	/**
	 * used for getting the list of avaliable REgex groups from DB
	 * 
	 * @return {@link list<RegexGroupDTO>}
	 */
	private List<RegexGroupDTO> getRegexGroupList() {
		List<RegexGroupDTO> regexGroupDTOList = null;
		RegexGroupService regexGroupService = this.getSingleBeanOfType(RegexGroupService.class);
		final List<RegexGroup> regexGroupList = regexGroupService.getRegexGroups();
		if (regexGroupList == null) {
			LOGGER.error("Regex group List selection failed.");
		} else {
			regexGroupDTOList = new ArrayList<RegexGroupDTO>(regexGroupList.size());
			for (RegexGroup regexGroup : regexGroupList) {
				if (regexGroup != null) {
					final RegexGroupDTO regexGroupDTO = RegexUtil.convertRegexGroupToRegexGroupDTO(regexGroup);
					if (regexGroupDTO == null) {
						continue;
					}
					regexGroupDTOList.add(regexGroupDTO);
				}
			}
		}
		return regexGroupDTOList;
	}

	/**
	 * used for updating the selected Regex Pattern Dto
	 * 
	 * @param editedRegexPattern {@link RegexPatternDTO}
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 * @return {@link integer} pattern ID
	 */
	@Override
	public int updateRegexPattern(final RegexPatternDTO editedRegexPattern, final RegexGroupDTO regexGroupDTO) {
		int result = 0;
		if (null != editedRegexPattern) {
			RegexPatternService regexPatternService = this.getSingleBeanOfType(RegexPatternService.class);
			if (null != regexPatternService) {
				RegexGroup regexGroup = RegexUtil.convertRegexGroupDTOToRegexGroup(regexGroupDTO);
				RegexPattern regexPattern = RegexUtil.convertRegexPatternDTOToRegexPattern(editedRegexPattern, regexGroup);
				regexPatternService.updateRegexPattern(regexPattern);

				result = (int) regexPattern.getId();

			} else {
				LOGGER.error("Regex Pattern service is null ");
			}
		} else {
			LOGGER.error("Regex Pattern to be modified is null ");
		}

		return result;
	}

	/**
	 * delete the selected Regex Pattern from DB
	 * 
	 * @param regexPatternDTO {@link RegexPatternDTO}
	 * @return {@link boolean } success or failure status of deletion event
	 */
	public boolean deleteRegexPattern(final RegexPatternDTO regexPattern) {
		boolean result = false;
		if (null != regexPattern) {
			RegexPatternService regexPatternService = this.getSingleBeanOfType(RegexPatternService.class);

			if (null != regexPatternService) {
				result = regexPatternService.removeRegexPattern(Long.parseLong(regexPattern.getIdentifier()));

				if (!result) {
					LOGGER.error(regexPattern.getPattern() + "is not removed");
				}
			} else {
				LOGGER.error("Regex Pattern service is null ");
			}

		} else {
			LOGGER.error("Regex Pattern To Be Deleted is null");
		}

		return result;
	}

	/**
	 * delete the selected list of Regex Patterns from DB
	 * 
	 * @param regexPatternDTOList {@link List<RegexPatternDTO>}
	 * @return {@link boolean } success or failure status of deletion event
	 */
	@Override
	public boolean deleteRegexPatterns(final List<RegexPatternDTO> regexPatternDTOList) {
		boolean result = false;

		if (null != regexPatternDTOList) {
			for (RegexPatternDTO regexPattern : regexPatternDTOList) {
				result = this.deleteRegexPattern(regexPattern);
			}
		} else {
			LOGGER.error("Regex Patterns List to be deleted is null");
		}

		if (!result) {
			LOGGER.error("All Regex Patterns are not removed");
		}

		return result;
	}

	/**
	 * used for updating the selected Regex Group Dto
	 * 
	 * @param editedRegexGroup {@link RegexGroupDTO}
	 * @return {@link integer} Group ID
	 */
	@Override
	public int updateRegexGroup(final RegexGroupDTO editedRegexGroup) {
		int result = 0;
		if (null != editedRegexGroup) {
			RegexGroupService regexGroupService = this.getSingleBeanOfType(RegexGroupService.class);
			if (null != regexGroupService) {
				RegexGroup regexGroup = RegexUtil.convertRegexGroupDTOToRegexGroup(editedRegexGroup);

				regexGroupService.updateRegexGroup(regexGroup);
				result = (int) regexGroup.getId();

			} else {
				LOGGER.error("Regex Group service is null ");
			}
		} else {
			LOGGER.error("Regex Group to be modified is null ");
		}

		return result;
	}

	/**
	 * delete the selected Regex Group from DB
	 * 
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 * @return {@link boolean } success or failure status of deletion event
	 */
	public boolean deleteRegexGroup(final RegexGroupDTO regexGroup) {
		boolean result = false;
		if (null != regexGroup) {
			RegexGroupService regexGroupService = this.getSingleBeanOfType(RegexGroupService.class);

			if (null != regexGroupService) {
				result = regexGroupService.removeRegexGroup(Long.parseLong(regexGroup.getIdentifier()));

				if (!result) {
					LOGGER.error(regexGroup.getName() + "is not removed");
				}
			} else {
				LOGGER.error("Regex Group service is null ");
			}

		} else {
			LOGGER.error("Regex Group To Be Deleted is null");
		}

		return result;
	}

	/**
	 * delete the selected list of Regex Groups from DB
	 * 
	 * @param regexGroupDTOList {@link List<RegexGroupDTO>}
	 * @return {@link boolean } success or failure status of deletion event
	 */
	@Override
	public boolean deleteRegexGroups(final List<RegexGroupDTO> regexGroupDTOList) {
		boolean result = false;

		if (null != regexGroupDTOList) {
			for (RegexGroupDTO regexGroup : regexGroupDTOList) {
				result = this.deleteRegexGroup(regexGroup);
			}
		} else {
			LOGGER.error("Regex Groups List to be deleted is null");
		}

		if (!result) {
			LOGGER.error("All Regex Groups are not removed");
		}

		return result;
	}

	/**
	 * insert the selected Regex Group into DB
	 * 
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 * @return {@link boolean } success or failure status of insertion event
	 */
	@Override
	public boolean insertRegexGroup(final RegexGroupDTO regexGroupDTO) {
		boolean result = false;
		if (null != regexGroupDTO) {
			RegexGroupService regexGroupService = this.getSingleBeanOfType(RegexGroupService.class);

			if (null != regexGroupService) {
				RegexGroup regexGroup = RegexUtil.convertRegexGroupDTOToRegexGroup(regexGroupDTO);
				if (null != regexGroup) {
					result = regexGroupService.insertRegexGroup(regexGroup);

					if (!result) {
						LOGGER.error(regexGroupDTO.getName() + "is not inserted");
					}
				} else {
					LOGGER.error("Error occured while conerting to Regex Group from Regex Group DTO ");
				}

			} else {
				LOGGER.error("Regex Group service is null ");
			}

		} else {
			LOGGER.error("Regex Group To Be inserted is null");
		}

		return result;
	}

	/**
	 * insert the selected Regex Pattern into DB
	 * 
	 * @param regexPatternDTO {@link RegexPatternDTO}
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 * @return {@link boolean } success or failure status of insertion event
	 */
	@Override
	public boolean insertRegexPattern(final RegexPatternDTO regexPatternDTO, final RegexGroupDTO regexGroupDTO) {
		boolean result = false;
		if (null != regexPatternDTO) {
			RegexPatternService regexPatternService = this.getSingleBeanOfType(RegexPatternService.class);

			if (null != regexPatternService) {
				RegexGroup regexGroup = RegexUtil.convertRegexGroupDTOToRegexGroup(regexGroupDTO);
				RegexPattern regexPattern = RegexUtil.convertRegexPatternDTOToRegexPattern(regexPatternDTO, regexGroup);
				if (null != regexPattern) {
					result = regexPatternService.insertRegexPattern(regexPattern);

					if (!result) {
						LOGGER.error(regexPatternDTO.getPattern() + "is not inserted");
					}
				} else {
					LOGGER.error("Error occured while conerting to Regex Pattern from Regex Pattern DTO ");
				}

			} else {
				LOGGER.error("Regex Pattern service is null ");
			}

		} else {
			LOGGER.error("Regex Pattern To Be inserted is null");
		}

		return result;
	}
}
