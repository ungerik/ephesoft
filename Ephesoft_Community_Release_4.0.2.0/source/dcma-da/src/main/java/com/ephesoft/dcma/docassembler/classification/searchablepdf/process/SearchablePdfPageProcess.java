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

package com.ephesoft.dcma.docassembler.classification.searchablepdf.process;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.docassembler.DocumentAssembler;
import com.ephesoft.dcma.docassembler.constant.DocumentAssemblerConstants;

/**
 * This class will process the batch and modify the document type from UNKNWON to the first document type configured for batch class
 * configured for searchable pdf workflow.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.DocumentAssembler
 */
public class SearchablePdfPageProcess {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchablePdfPageProcess.class);

	/**
	 * Instance of PluginPropertiesService.
	 */
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Batch instance Identifier.
	 */
	private String batchInstanceID;

	/**
	 * Reference of BatchSchemaService.
	 */
	private BatchSchemaService batchSchemaService;

	/**
	 * Reference of propertyMap.
	 */
	private Map<String, String> propertyMap;

	/**
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @return the batchInstanceID
	 */
	public String getBatchInstanceID() {
		return batchInstanceID;
	}

	/**
	 * @param batchInstanceID the batchInstanceID to set
	 */
	public void setBatchInstanceID(String batchInstanceID) {
		this.batchInstanceID = batchInstanceID;
	}

	/**
	 * @param pluginPropertiesService the pluginPropertiesService to set
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * This method modifies the document type from UNKNWON to the first document type configured for batch class configured for
	 * searchable pdf workflow.
	 * 
	 * @throws DCMAApplicationException
	 */
	public void modifyDocumentType() throws DCMAApplicationException {

		DocumentType docType = getDocumentType();

		Batch batch = batchSchemaService.getBatch(batchInstanceID);
		List<Document> xmlDocuments = batch.getDocuments().getDocument();

		setDocumentType(docType, xmlDocuments);

		batchSchemaService.updateBatch(batch);
	}

	private void setDocumentType(DocumentType docType, List<Document> xmlDocuments) {
		// set default document type for unknown documents if the switch is on.
		String switchUnknownDocType = getPropertyMap().get(DocumentAssemblerConstants.SWITCH_UNKNOWN_PREDEFINED_DOCUMENT_TYPE);
		String unknownDocType = getPropertyMap().get(DocumentAssemblerConstants.UNKNOWN_PREDEFINED_DOCUMENT_TYPE).trim();
		if (null != switchUnknownDocType && DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(switchUnknownDocType)) {
			if (null != unknownDocType && !unknownDocType.isEmpty()) {
				DocumentType unknownDocTypeDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, unknownDocType,
						batchInstanceID, false, null);
				if (null != unknownDocTypeDesc) {
					for (Document doc : xmlDocuments) {
						if (EphesoftProperty.UNKNOWN.getProperty().equals(doc.getType())) {
							doc.setType(unknownDocType);
							doc.setDescription(unknownDocTypeDesc.getDescription());
						}
					}
				}
			}
		}
		// Fetching the value of the delete document's first page switch.
		String deleteDocumentFirstPageSwitch = getPropertyMap().get(DocumentAssemblerConstants.SWITCH_DELETE_DOCUMENT_FIRST_PAGE);
		for (Document document : xmlDocuments) {
			document.setType(docType.getName());
			document.setDescription(docType.getDescription());
			// Set the error message explicitly to blank to display the node in batch xml
			document.setErrorMessage(BatchConstants.EMPTY);
			// Deleting the document's first page based on the switch.
			if (DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(deleteDocumentFirstPageSwitch)) {
				DocumentAssembler.removeFirstPageOfDocument(document);
			}
		}
	}

	private DocumentType getDocumentType() throws DCMAApplicationException {
		List<DocumentType> allDocTypes = pluginPropertiesService.getDocumentTypes(batchInstanceID);
		DocumentType docType = null;
		if (allDocTypes != null && !allDocTypes.isEmpty()) {
			// Assumption : Only 1 document type exist for Searchable PDF workflow.
			docType = allDocTypes.get(0);
			if (docType == null) {
				LOGGER.error("Document type is NULL for batch instance : " + batchInstanceID);
				throw new DCMAApplicationException("Document type is NULL for batch instance : " + batchInstanceID);
			}
		} else {
			LOGGER.error("No Document type defined for batch instance : " + batchInstanceID);
			throw new DCMAApplicationException("No Document type defined for batch instance : " + batchInstanceID);
		}
		if (docType.getName() == null) {
			LOGGER.error("Document type name is NULL for batch instance : " + batchInstanceID);
			throw new DCMAApplicationException("Document type name is NULL for batch instance : " + batchInstanceID);
		}
		return docType;
	}

	/**
	 * @param documentList
	 * @throws DCMAApplicationException
	 */
	public void reClassifyDocuments(final List<Document> documentList) throws DCMAApplicationException {
		LOGGER.debug("Entering method reClassifyDocuments for " + batchInstanceID);
		if (documentList != null && !documentList.isEmpty()) {

			// Assuming there will only be one document type for searchable pdf classification, get the document type name from db.
			DocumentType docType = getDocumentType();

			// Generate the ids for document types.
			Long idGenerator = 0L;

			for (Document document : documentList) {
				idGenerator++;

				document.setType(docType.getName());
				document.setDescription(docType.getDescription());
				document.setDocumentDisplayInfo(BatchConstants.EMPTY);
				document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + idGenerator);

				// Set the error message explicitly to blank to display the node in batch xml
				document.setErrorMessage(BatchConstants.EMPTY);
			}
		}
		LOGGER.debug("Exiting method reClassifyDocuments for " + batchInstanceID);
	}

	public void setPropertyMap(Map<String, String> propertyMap) {
		this.propertyMap = propertyMap;
	}

	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}
}
