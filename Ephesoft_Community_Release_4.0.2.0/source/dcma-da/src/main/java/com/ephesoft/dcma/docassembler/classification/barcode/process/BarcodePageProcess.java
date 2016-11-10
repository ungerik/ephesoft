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

package com.ephesoft.dcma.docassembler.classification.barcode.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Batch.DocumentClassificationTypes;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Page.PageLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.docassembler.DocumentAssembler;
import com.ephesoft.dcma.docassembler.constant.DocumentAssemblerConstants;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * This class will process every page present at document type Unknown. This will read all the pages one by one and basis of the
 * barcode it will create new documents and delete the current page from the document type Unknown.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.DocumentAssembler
 */
public class BarcodePageProcess {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BarcodePageProcess.class);

	/**
	 * docTypeService DocumentTypeService.
	 */
	private DocumentTypeService docTypeService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * xmlDocuments List<DocumentType>.
	 */
	private List<Document> xmlDocuments;

	/**
	 * barcode Classification.
	 */
	private String barcodeClassification;

	/**
	 * barcode Confidence score.
	 */
	private String barcodeConfidence;

	/**
	 * Batch instance ID.
	 */
	private String batchInstanceIdentifier;

	/**
	 * Batch Class ID.
	 */
	private String batchClassIdentifier;
	/**
	 * Reference of BatchSchemaService.
	 */
	private BatchSchemaService batchSchemaService;

	/**
	 * Name of the factory classification.
	 */
	private String factoryClassification;

	/**
	 * Reference of propertyMap.
	 */
	private Map<String, String> propertyMap;

	/**
	 * @return the batchSchemaService
	 */
	public final BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public final void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return the docTypeService
	 */
	public final DocumentTypeService getDocTypeService() {
		return docTypeService;
	}

	/**
	 * @param docTypeService the docTypeService to set
	 */
	public final void setDocTypeService(final DocumentTypeService docTypeService) {
		this.docTypeService = docTypeService;
	}

	/**
	 * @return List<DocumentType>
	 */
	public final List<Document> getXmlDocuments() {
		return xmlDocuments;
	}

	/**
	 * @param xmlDocuments List<DocumentType>
	 */
	public final void setXmlDocuments(final List<Document> xmlDocuments) {
		this.xmlDocuments = xmlDocuments;
	}

	/**
	 * @return batchInstanceID
	 */
	public final String getBatchInstanceIdentifier() {
		return this.batchInstanceIdentifier;
	}

	/**
	 * @param batchInstanceID Long
	 */
	public final void setBatchInstanceIdentifier(String batchInstanceIdentifier) {
		this.batchInstanceIdentifier = batchInstanceIdentifier;
	}

	/**
	 * @param batchClassIdentifier.
	 */
	public void setBatchClassIdentifier(String batchClassIdentifier) {
		this.batchClassIdentifier = batchClassIdentifier;
	}

	/**
	 * @return batch class identifier.
	 */
	public String getBatchClassIdentifier() {
		return batchClassIdentifier;
	}

	/**
	 * @return the barcodeClassification
	 */
	public final String getBarcodeClassification() {
		return barcodeClassification;
	}

	/**
	 * @param barcodeClassification the barcodeClassification to set
	 */
	public final void setBarcodeClassification(final String barcodeClassification) {
		this.barcodeClassification = barcodeClassification;
	}

	/**
	 * @return the barcodeConfidence
	 */
	public final String getBarcodeConfidence() {
		return barcodeConfidence;
	}

	/**
	 * @param barcodeConfidence the barcodeConfidence to set
	 */
	public final void setBarcodeConfidence(final String barcodeConfidence) {
		this.barcodeConfidence = barcodeConfidence;
	}

	/**
	 * @return the factoryClassification
	 */
	public String getFactoryClassification() {
		return factoryClassification;
	}

	/**
	 * @param factoryClassification the factoryClassification to set
	 */
	public void setFactoryClassification(String factoryClassification) {
		this.factoryClassification = factoryClassification;
	}

	/**
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService the pluginPropertiesService to set
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * This method return the document type name for the page type name input.
	 * 
	 * @param pageTypeName String
	 * @return {@link DocumentType} document type which is present in data base table document_type for the corresponding page type
	 *         name.
	 */
	public final DocumentType getDocType(final String pageTypeName) {

		DocumentType docTypeDB = null;
		if (pageTypeName != null) {
			List<DocumentType> docTypeList = pluginPropertiesService.getDocTypeByPageTypeName(batchInstanceIdentifier, pageTypeName);

			if (null == docTypeList || docTypeList.isEmpty()) {
				LOGGER.error("No document type name found for the page type name : " + pageTypeName);
			} else {
				DocumentType docType = docTypeList.get(0);
				docTypeDB = docType;
			}
		}
		return docTypeDB;
	}

	/**
	 * This method return the document type name for the page type name input from the batch class id.
	 * 
	 * @param pageTypeName String
	 * @return String docTypeName which is present in data base table document_type for the corresponding page type name.
	 */
	public final DocumentType getDocTypeNameAPI(final String batchClassID, final String pageTypeName) {

		DocumentType docType = null;

		List<com.ephesoft.dcma.da.domain.DocumentType> docTypeList = docTypeService.getDocTypeByBatchClassIdentifier(batchClassID, -1,
				-1);

		String docTypeNameTemp = "";
		if (null == docTypeList || docTypeList.isEmpty()) {
			LOGGER.error("No document type name found for the page type name : " + pageTypeName);
		} else {

			final Iterator<com.ephesoft.dcma.da.domain.DocumentType> itr = docTypeList.iterator();
			while (itr.hasNext()) {
				final com.ephesoft.dcma.da.domain.DocumentType docTypeDB = itr.next();
				docTypeNameTemp = docTypeDB.getName();
				if (pageTypeName.contains(docTypeNameTemp)) {
					docType = docTypeDB;
					LOGGER.debug("DocumentType name : " + docTypeDB.getName());
					break;
				}
			}
		}
		return docType;
	}

	/**
	 * This method will create new document for pages that was found in the batch.xml file for Unknown type document.
	 * 
	 * @param insertAllDocument {@link List <{@link Document} .The list of all the documents.
	 * @param docPageInfo List<Page> {@link List <{@link Page} .The list of all the pages.
	 * @param isFromWebService boolean. true if this method is called from web service else returns false.
	 * @throws {@link DCMAApplicationException} Check for input parameters, create new documents for page found in document type
	 *         Unknown.
	 */
	public final void createDocForPages(final List<Document> insertAllDocument, final List<Page> docPageInfo,
			final boolean isFromWebService) throws DCMAApplicationException {

		String errMsg = null;

		if (!isFromWebService && null == this.xmlDocuments) {
			throw new DCMAApplicationException("Unable to write pages for the document.");
		}

		int confidenceValue = 0;

		try {
			confidenceValue = Integer.parseInt(getBarcodeConfidence());
		} catch (NumberFormatException nfe) {
			errMsg = EphesoftStringUtil.concatenate("Invalid integer for barcode confidence score in properties file.",
					nfe.getMessage());
			LOGGER.error(errMsg);
			// Setting the default value for the confidence in case if there is number format exception occurs.
			confidenceValue = 0;
		}

		try {
			String previousValue = DocumentAssemblerConstants.EMPTY;

			Document document = null;
			Long idGenerator = 0L;
			List<Integer> removeIndexList = new ArrayList<Integer>();

			for (int index = 0; index < docPageInfo.size(); index++) {

				Page pgType = docPageInfo.get(index);
				String value = getPgLevelFdValue(pgType);

				if (null == value) {
					errMsg = EphesoftStringUtil.concatenate("Invalid format of page level fields. Value found for ",
							barcodeClassification, " classification is null.");
					throw new DCMAApplicationException(errMsg);
				}

				if (value.equals(DocumentAssemblerConstants.EMPTY)) {
					// This if is to check the first continuous empty names for
					// all the pages. do not do any thing if the first name is
					// empty.
					if (previousValue.equals(DocumentAssemblerConstants.EMPTY)) {
						continue;
					} else {
						document.getPages().getPage().add(pgType);
						removeIndexList.add(index);
					}
				} else {
					previousValue = value;
					DocumentType docTypeDB = null;
					if (isFromWebService) {
						docTypeDB = getDocTypeNameAPI(batchClassIdentifier, previousValue);
					} else {
						docTypeDB = getDocType(previousValue);
					}
					if (docTypeDB == null || docTypeDB.getName() == null) {
						errMsg = EphesoftStringUtil.concatenate(
								"DocumentType name is not found in the data base for the page type name : ", previousValue);
						LOGGER.info(errMsg);
						document = new Document();
						document.setType(EphesoftProperty.UNKNOWN.getProperty());
						document.setConfidence(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
						document.setConfidenceThreshold(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
						document.setReviewed(Boolean.FALSE);
						document.setValid(Boolean.FALSE);
					} else {
						document = new Document();
						document.setType(docTypeDB.getName());
						document.setDescription(docTypeDB.getDescription());
						float minConfThreshold = 0;
						// if the call is from web service or test classification functionality.
						if (isFromWebService) {
							minConfThreshold = getMinConfThresholdAPI(batchClassIdentifier, previousValue);
						} else {
							minConfThreshold = getMinConfThreshold(previousValue);
						}
						document.setReviewed(Boolean.FALSE);
						document.setValid(Boolean.FALSE);
						document.setConfidenceThreshold(minConfThreshold);
						LOGGER.info(EphesoftStringUtil.concatenate("Page confidence value is : ", confidenceValue));
						document.setConfidence(Float.valueOf(confidenceValue));
					}
					idGenerator++;
					document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + idGenerator);
					document.setDocumentDisplayInfo(BatchConstants.EMPTY);
					Pages pages = new Pages();
					List<Page> listOfPages = pages.getPage();
					listOfPages.add(pgType);
					document.setPages(pages);
					insertAllDocument.add(document);
					removeIndexList.add(index);
				}
			}

			if (isFromWebService) {
				updateBatchXMLAPI(insertAllDocument, docPageInfo, removeIndexList);
			} else {
				// update the xml file.
				updateBatchXML(insertAllDocument, removeIndexList);
			}
		} catch (Exception e) {
			errMsg = EphesoftStringUtil.concatenate("Unable to write pages for the document. ", e.getMessage());
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg, e);
		}
	}

	/**
	 * This method return the document minimum confidence threshold for the page type name input.
	 * 
	 * @param pageTypeName String
	 * @return float minConfThreshold which is present in data base table document_type for the corresponding page type name.
	 */
	private float getMinConfThreshold(final String pageTypeName) {
		float minConfThreshold = 0;

		List<com.ephesoft.dcma.da.domain.DocumentType> docTypeList = pluginPropertiesService.getDocTypeByPageTypeName(
				batchInstanceIdentifier, pageTypeName);

		if (null == docTypeList || docTypeList.isEmpty()) {
			LOGGER.error("No document type name found for the page type name : " + pageTypeName);
		} else {
			com.ephesoft.dcma.da.domain.DocumentType docType = docTypeList.get(0);
			minConfThreshold = docType.getMinConfidenceThreshold();
		}

		return minConfThreshold;
	}

	/**
	 * Returns the document minimum confidence threshold for the page type name input. This method is to cater the web service and test
	 * classification calls.
	 * 
	 * @param pageTypeName {@link String} the name of the page classified.
	 * @param batchClassID {@link String} the identifier for the batch class in which processing has to be done.
	 * @return float minConfThreshold which is present in data base table document_type for the corresponding page type name.
	 */
	private final float getMinConfThresholdAPI(final String batchClassID, final String pageTypeName) {
		float minConfThreshold = 0;
		List<com.ephesoft.dcma.da.domain.DocumentType> docTypeList = docTypeService.getDocTypeByBatchClassIdentifier(batchClassID, -1,
				-1);
		if (null == docTypeList || docTypeList.isEmpty()) {
			LOGGER.error(EphesoftStringUtil.concatenate("No document type name found for the page type name : ", pageTypeName));
		} else {
			String docTypeNameTemp = DocumentAssemblerConstants.EMPTY;
			final Iterator<com.ephesoft.dcma.da.domain.DocumentType> itr = docTypeList.iterator();
			while (itr.hasNext()) {
				final com.ephesoft.dcma.da.domain.DocumentType docTypeDB = itr.next();
				docTypeNameTemp = docTypeDB.getName();
				if (!EphesoftStringUtil.isNullOrEmpty(pageTypeName) && pageTypeName.indexOf(docTypeNameTemp) != -1) {
					minConfThreshold = docTypeDB.getMinConfidenceThreshold();
					break;
				}
			}
		}
		return minConfThreshold;
	}

	/**
	 * Update Batch XML file.
	 * 
	 * @param insertAllDocument List<DocumentType>
	 * @param removeIndexList List<Integer>
	 * @throws DCMAApplicationException Check for input parameters, update the batch xml.
	 */
	private void updateBatchXML(final List<Document> insertAllDocument, final List<Integer> removeIndexList)
			throws DCMAApplicationException {

		String errMsg = null;

		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		if (null != xmlDocuments && null != insertAllDocument && !insertAllDocument.isEmpty()) {
			xmlDocuments.addAll(insertAllDocument);
		}

		List<Page> docPageInfo = null;

		int indexDocType = -1;

		for (int i = 0; i < xmlDocuments.size(); i++) {
			final Document document = xmlDocuments.get(i);
			String docType = document.getType();
			if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
				docPageInfo = document.getPages().getPage();
				indexDocType = i;
				break;
			}
		}

		if (!removeIndexList.isEmpty()) {
			for (int index = removeIndexList.size() - 1; index >= 0; index--) {
				int rIndex = removeIndexList.get(index);
				if (rIndex < docPageInfo.size()) {
					docPageInfo.remove(rIndex);
				} else {
					errMsg = "Invalid argument for removal of pages for document type : " + EphesoftProperty.UNKNOWN.getProperty();
					LOGGER.error(errMsg);
					throw new DCMAApplicationException(errMsg);
				}
			}
		}

		// Delete the document type "Unknown" if no more pages are present.
		if (indexDocType != -1 && null != docPageInfo && docPageInfo.isEmpty()) {
			xmlDocuments.remove(indexDocType);
		}

		// setting the batch level documentClassificationTypes.
		DocumentClassificationTypes documentClassificationTypes = new DocumentClassificationTypes();
		List<String> documentClassificationType = documentClassificationTypes.getDocumentClassificationType();
		documentClassificationType.add(getFactoryClassification());
		batch.setDocumentClassificationTypes(documentClassificationTypes);

		// Fetching the list of all the document types defined for a particular batch.
		List<String> documnetTypes = DocumentAssembler.getDocumentTypes(pluginPropertiesService, batchInstanceIdentifier);

		// the regex verification of UNKNOWN documents only for barcode classification.
		String regexDocType = getPropertyMap().get(DocumentAssemblerConstants.REGEX_DOCUMENT_TYPE);
		String regex = getPropertyMap().get(DocumentAssemblerConstants.REGEX_VALUE);
		String switchRegexClassification = getPropertyMap().get(DocumentAssemblerConstants.SWITCH_REGEX_CLASSIFICATION).trim();
		if (DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(switchRegexClassification)) {
			if (null != regexDocType && !regexDocType.isEmpty()) {
				DocumentType regexDocument = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, regexDocType,
						batchInstanceIdentifier, false, null);
				if (null != regexDocument) {
					DocumentAssembler.kvClassification(xmlDocuments, regex, regexDocument);
				}
			}
		}

		// set default document type for unknown documents if the switch is on.
		String switchUnknownDocType = getPropertyMap().get(DocumentAssemblerConstants.SWITCH_UNKNOWN_PREDEFINED_DOCUMENT_TYPE);
		String unknownDocType = getPropertyMap().get(DocumentAssemblerConstants.UNKNOWN_PREDEFINED_DOCUMENT_TYPE).trim();
		if (null != switchUnknownDocType && DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(switchUnknownDocType)) {
			if (null != unknownDocType && !unknownDocType.isEmpty()) {
				DocumentType unknownDocTypeDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, unknownDocType,
						batchInstanceIdentifier, false, null);
				if (null != unknownDocTypeDesc) {
					for (Document doc : xmlDocuments) {
						if (EphesoftProperty.UNKNOWN.getProperty().equals(doc.getType())) {
							doc.setType(unknownDocType);
							doc.setDescription(unknownDocTypeDesc.getDescription());
							doc.setConfidenceThreshold(unknownDocTypeDesc.getMinConfidenceThreshold());
						}
					}
				}
			}
		}
		// Fetching the value of the delete document's first page switch.
		String deleteDocumentFirstPageSwitch = getPropertyMap().get(DocumentAssemblerConstants.SWITCH_DELETE_DOCUMENT_FIRST_PAGE);
		// Set the error message explicitly to blank to display the node in batch xml.
		for (int i = 0; i < xmlDocuments.size(); i++) {
			final Document document = xmlDocuments.get(i);
			// If there is only one document type defined then set the type of document to the name of that document.
			if (documnetTypes != null && documnetTypes.size() == 1) {
				DocumentType documentDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, documnetTypes.get(0),
						batchInstanceIdentifier, false, null);
				document.setType(documnetTypes.get(0));
				document.setDescription(documentDesc.getDescription());
			}
			document.setErrorMessage("");
			// Deleting the document's first page if the switch is ON.
			if (DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(deleteDocumentFirstPageSwitch)) {
				DocumentAssembler.removeFirstPageOfDocument(document);
			}
		}
		insertAllDocument.clear();
		insertAllDocument.addAll(xmlDocuments);
		// now write the state of the object to the xml file.
		batchSchemaService.updateBatch(batch);

		LOGGER.info("updateBatchXML done.");

	}

	/**
	 * Update Batch XML file.
	 * 
	 * @param insertAllDocument List<DocumentType>
	 * @param docPageInfo
	 * @param docPageInfo List<Integer>
	 * @param removeIndexList
	 * @throws DCMAApplicationException Check for input parameters, update the batch xml.
	 */
	private void updateBatchXMLAPI(final List<Document> insertAllDocument, List<Page> docPageInfo, List<Integer> removeIndexList)
			throws DCMAApplicationException {
		String errMsg;
		if (insertAllDocument.isEmpty()) {
			Document document = new Document();
			document.setConfidence(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
			document.setConfidenceThreshold(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
			document.setReviewed(Boolean.FALSE);
			document.setValid(Boolean.FALSE);
			document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + 0);
			document.setType(EphesoftProperty.UNKNOWN.getProperty());
			document.setDocumentDisplayInfo(BatchConstants.EMPTY);
			Pages pages = new Pages();
			List<Page> listOfPages = pages.getPage();
			listOfPages.addAll(docPageInfo);
			document.setPages(pages);
			insertAllDocument.add(document);
		} else {
			if (!removeIndexList.isEmpty()) {
				for (int index = removeIndexList.size() - 1; index >= 0; index--) {
					int rIndex = removeIndexList.get(index);
					if (rIndex < docPageInfo.size()) {
						docPageInfo.remove(rIndex);
					} else {
						errMsg = "Invalid argument for removal of pages for document type : " + EphesoftProperty.UNKNOWN.getProperty();
						LOGGER.error(errMsg);
						throw new DCMAApplicationException(errMsg);
					}
				}
			}
			if (!docPageInfo.isEmpty()) {
				Document document = new Document();
				document.setConfidence(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
				document.setConfidenceThreshold(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
				document.setReviewed(Boolean.FALSE);
				document.setValid(Boolean.FALSE);
				document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + 0);
				document.setType(EphesoftProperty.UNKNOWN.getProperty());
				document.setDocumentDisplayInfo(BatchConstants.EMPTY);
				Pages pages = new Pages();
				List<Page> listOfPages = pages.getPage();
				listOfPages.addAll(docPageInfo);
				document.setPages(pages);
				insertAllDocument.add(0, document);
			}
		}
		setXmlDocuments(insertAllDocument);
		// setting the batch level documentClassificationTypes.
		DocumentClassificationTypes documentClassificationTypes = new DocumentClassificationTypes();
		List<String> documentClassificationType = documentClassificationTypes.getDocumentClassificationType();
		documentClassificationType.add(getFactoryClassification());

		// Fetching the list of all the document types defined for a particular batch.
		List<DocumentType> documnetTypes = docTypeService.getDocTypeByBatchClassIdentifier(batchClassIdentifier, -1, -1);
		// the regex verification of UNKNOWN documents only for barcode classification.
		String regexDocType = getPropertyMap().get(DocumentAssemblerConstants.REGEX_DOCUMENT_TYPE);
		String regex = getPropertyMap().get(DocumentAssemblerConstants.REGEX_VALUE);
		String switchRegexClassification = getPropertyMap().get(DocumentAssemblerConstants.SWITCH_REGEX_CLASSIFICATION).trim();
		if (DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(switchRegexClassification)) {
			if (null != regexDocType && !regexDocType.isEmpty()) {
				DocumentType regexDocument = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, regexDocType,
						batchClassIdentifier, true, docTypeService);
				if (null != regexDocument) {
					DocumentAssembler.kvClassification(xmlDocuments, regex, regexDocument);
				}
			}
		}

		// set default document type for unknown documents if the switch is on.
		String switchUnknownDocType = getPropertyMap().get(DocumentAssemblerConstants.SWITCH_UNKNOWN_PREDEFINED_DOCUMENT_TYPE);
		String unknownDocType = getPropertyMap().get(DocumentAssemblerConstants.UNKNOWN_PREDEFINED_DOCUMENT_TYPE).trim();
		if (null != switchUnknownDocType && DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(switchUnknownDocType)) {
			if (null != unknownDocType && !unknownDocType.isEmpty()) {
				DocumentType unknownDocTypeDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, unknownDocType,
						batchClassIdentifier, true, docTypeService);
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
		// Set the error message explicitly to blank to display the node in batch xml
		for (int i = 0; i < xmlDocuments.size(); i++) {
			final Document document = xmlDocuments.get(i);
			// If there is only one document type defined except UNKNOWN then set the type of document to the name of that document. By
			// default the list will return a document UNKNOWN also so default value for one document type in web service and Test
			// content classification is set to 2.
			if (documnetTypes != null && documnetTypes.size() == 2) {
				DocumentType documentDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, documnetTypes.get(1)
						.getName(), batchClassIdentifier, true, docTypeService);
				document.setType(documnetTypes.get(1).getName());
				document.setDescription(documentDesc.getDescription());
			}
			document.setErrorMessage("");
			// Deleting the document's first page if the switch is ON.
			if (DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(deleteDocumentFirstPageSwitch)) {
				DocumentAssembler.removeFirstPageOfDocument(document);
			}
		}
		LOGGER.info("updateBatchXML for web services done.");
	}

	/**
	 * This method will retrieve the page level field type value for any input page type for current classification name.
	 * 
	 * @param pgType PageType
	 * @return value String
	 */
	private String getPgLevelFdValue(final Page pgType) {

		String value = null;
		String name = null;
		if (null != pgType) {
			PageLevelFields pageLevelFields = pgType.getPageLevelFields();
			if (null != pageLevelFields) {
				List<DocField> pageLevelField = pageLevelFields.getPageLevelField();
				if (null != pageLevelFields) {
					for (DocField docFdType : pageLevelField) {
						if (null != docFdType) {
							name = docFdType.getName();
							if (null != name && name.contains(getBarcodeClassification())) {
								value = docFdType.getValue();
							}
						}
					}
				}
			}
		}

		return value;

	}

	/**
	 * This method will read all the pages of the document for document type Unknown.
	 * 
	 * @return docPageInfo List<PageType>
	 * @throws DCMAApplicationException Check for input parameters and read all pages of the document.
	 */
	public final List<Page> readAllPages() throws DCMAApplicationException {
		LOGGER.info("Reading the document for Document Assembler.");
		List<Page> docPageInfo = null;

		final Batch pasrsedXMLFile = batchSchemaService.getBatch(this.batchInstanceIdentifier);

		this.xmlDocuments = pasrsedXMLFile.getDocuments().getDocument();

		for (int i = 0; i < this.xmlDocuments.size(); i++) {
			final Document document = this.xmlDocuments.get(i);
			String docType = document.getType();
			if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
				docPageInfo = document.getPages().getPage();
				break;
			}
		}

		return docPageInfo;
	}

	/**
	 * This API will reclassify the document
	 * 
	 * @param documentList
	 * @throws DCMAApplicationException
	 */
	public void reClassifyDocuments(final List<Document> documentList) throws DCMAApplicationException {
		LOGGER.debug("Entering method reClassifyDocuments for " + batchInstanceIdentifier);
		if (documentList != null && !documentList.isEmpty()) {

			// Assuming there will only be one document type for searchable pdf classification, get the document type name from db.

			// Generate the ids for document types.
			Long idGenerator = 0L;
			String pageTypeName = null;
			String docTypeName = null;
			String docTypeDesc = null;
			DocumentType docTypeDB = null;
			for (Document document : documentList) {
				idGenerator++;
				docTypeName = document.getType();
				if (docTypeName == null || docTypeName.isEmpty()) {
					List<Page> pageList = document.getPages().getPage();

					for (Page page : pageList) {
						pageTypeName = getPgLevelFdValue(page);
						docTypeDB = getDocType(pageTypeName);
						docTypeName = docTypeDB.getName();
						docTypeDesc = docTypeDB.getDescription();
						LOGGER.debug("Page Type Name = " + pageTypeName);
						LOGGER.debug("Document Type Name = " + docTypeName);
						if (docTypeName != null) {
							break;
						}
					}
					if (docTypeName != null) {
						document.setType(docTypeName);
						document.setDescription(docTypeDesc);
					} else {
						document.setType(EphesoftProperty.UNKNOWN.getProperty());
						document.setDescription(EphesoftProperty.UNKNOWN.getProperty());
					}
					document.setDocumentDisplayInfo(BatchConstants.EMPTY);
					String docId = EphesoftProperty.DOCUMENT.getProperty() + idGenerator;
					document.setIdentifier(docId);

					// Set the error message explicitly to blank to display the node in batch xml
					document.setErrorMessage(BatchConstants.EMPTY);
					LOGGER.debug("Created document = " + docId);
				} else {
					setConfThreshold(docTypeName, document);
				}
			}
			LOGGER.debug("Exiting method reClassifyDocuments for " + batchInstanceIdentifier);
		}
	}

	private void setConfThreshold(final String docTypeName, final Document document) {
		LOGGER.debug("Entering method setConfThreshold for " + batchInstanceIdentifier);
		if (docTypeName != null && document != null) {
			com.ephesoft.dcma.da.domain.DocumentType docType = docTypeService.getDocTypeByBatchClassAndDocTypeName(
					batchClassIdentifier, docTypeName);
			if (docType != null) {
				float confThreshold = docType.getMinConfidenceThreshold();
				document.setConfidenceThreshold(confThreshold);
			}
		}
		LOGGER.debug("Exiting method setConfThreshold for " + batchInstanceIdentifier);
	}

	public void setPropertyMap(Map<String, String> propertyMap) {
		this.propertyMap = propertyMap;
	}

	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}

}
