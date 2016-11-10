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

package com.ephesoft.dcma.docassembler.classification.image.process;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Batch.DocumentClassificationTypes;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Page.PageLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.da.service.PageTypeService;
import com.ephesoft.dcma.docassembler.AdvancedDocumentAssembler;
import com.ephesoft.dcma.docassembler.DocumentAssembler;
import com.ephesoft.dcma.docassembler.constant.DocumentAssemblerConstants;
import com.ephesoft.dcma.docassembler.constant.PlaceHolder;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * This class will process every page present at document type Unknown. This will read all the pages one by one and basis of the image
 * it will create new documents and delete the current page from the document type Unknown.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.DocumentAssembler
 */
public class ImagePageProcess {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ImagePageProcess.class);

	/**
	 * pageTypeService PageTypeService.
	 */
	private PageTypeService pageTypeService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	private PluginPropertiesService pluginPropertiesService;

	private DocumentTypeService docTypeService;

	/**
	 * xmlDocuments List<DocumentType>.
	 */
	private List<Document> xmlDocuments;

	/**
	 * Batch instance ID.
	 */
	private String batchInstanceID;

	private String batchClassID;

	/**
	 * Reference of BatchSchemaService.
	 */
	private BatchSchemaService batchSchemaService;

	/**
	 * Reference of propertyMap.
	 */
	private Map<String, String> propertyMap;

	/**
	 * @return the propertyMap
	 */
	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}

	/**
	 * @param propertyMap the propertyMap to set
	 */
	public final void setPropertyMap(final Map<String, String> propertyMap) {
		this.propertyMap = propertyMap;
	}

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
	 * @return the pageTypeService
	 */
	public final PageTypeService getPageTypeService() {
		return pageTypeService;
	}

	/**
	 * @param pageTypeService the pageTypeService to set
	 */
	public final void setPageTypeService(final PageTypeService pageTypeService) {
		this.pageTypeService = pageTypeService;
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
	public final String getBatchInstanceID() {
		return batchInstanceID;
	}

	/**
	 * @param batchInstanceID String
	 */
	public final void setBatchInstanceID(final String batchInstanceID) {
		this.batchInstanceID = batchInstanceID;
	}

	public void setBatchClassID(String batchClassID) {
		this.batchClassID = batchClassID;
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
	public void setPluginPropertiesService(final PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	public void setDocTypeService(DocumentTypeService docTypeService) {
		this.docTypeService = docTypeService;
	}

	/**
	 * Confidence threshold for middle page.
	 */
	private float middlePageConfidenceThreshold = 0;
	/**
	 * Confidence threshold for last page.
	 */
	private float lastPageConfidenceThreshold = 0;
	/**
	 * Confidence threshold for first page.
	 */
	private float firstPageConfidenceThreshold = 0;

	/**
	 * This method will set the document type name and ConfidenceThreshold.
	 * 
	 * @param docType DocumentType
	 * @param pageTypeName String
	 */
	private void setDocTypeNameAndConfThreshold(final Document docType, final String pageTypeName) {

		final List<com.ephesoft.dcma.da.domain.DocumentType> docTypes = pluginPropertiesService.getDocTypeByPageTypeName(
				batchInstanceID, pageTypeName);

		String docTypeName = null;
		String docTypeDesc = null;
		float minConfidenceThreshold = 0;

		if (null == docTypes || docTypes.isEmpty()) {
			LOGGER.info("No Document Type found for the input page type name.");
		} else {
			final Iterator<com.ephesoft.dcma.da.domain.DocumentType> itr = docTypes.iterator();
			while (itr.hasNext()) {
				final com.ephesoft.dcma.da.domain.DocumentType docTypeDB = itr.next();
				docTypeName = docTypeDB.getName();
				docTypeDesc = docTypeDB.getDescription();
				minConfidenceThreshold = docTypeDB.getMinConfidenceThreshold();
				LOGGER.debug(EphesoftStringUtil.concatenate("DocumentType name : ", docTypeName, "  minConfidenceThreshold : ",
						minConfidenceThreshold));
				if (null != docTypeName) {
					break;
				}
			}
		}

		if (null == docTypeName) {
			final String errMsg = "DocumentType name is not found in the data base " + "for page type name: " + pageTypeName;
			LOGGER.info(errMsg);
		} else {
			docType.setType(docTypeName);
			docType.setDescription(docTypeDesc);
			final DecimalFormat twoDForm = new DecimalFormat("#.##");
			minConfidenceThreshold = Float.valueOf(twoDForm.format(minConfidenceThreshold));
			docType.setConfidenceThreshold(minConfidenceThreshold);
		}
	}

	/**
	 * To decide which algorithm is to be called for creating docs for pages.
	 * 
	 * @param insertAllDocument {@link List<Document>} list of all the current documents.
	 * @param docPageInfo {@link List<Document>} list of all the pages.
	 * @param isFromWebService boolean to check if the call is from a web service or proper batch functioning.
	 * @throws DCMAApplicationException in case of any exception occurred during processing of document creation.
	 */
	public void createDocForPages(List<Document> insertAllDocument, List<Page> docPageInfo, boolean isFromWebService)
			throws DCMAApplicationException {
		createDocForPagesOldAlgorithm(insertAllDocument, docPageInfo, isFromWebService);

	}

	public void createDocForPagesAdvancedAlgorithm(List<Document> insertAllDocument, List<Page> docPageInfo, boolean isFromWebService)
			throws DCMAApplicationException {
		StringBuilder errMsg;
		try {
			firstPageConfidenceThreshold = Float.parseFloat(getPropertyMap().get(
					DocumentAssemblerConstants.FIRST_PAGE_CONFIDENCE_THRESHOLD));
			middlePageConfidenceThreshold = Float.parseFloat(getPropertyMap().get(
					DocumentAssemblerConstants.MIDDLE_PAGE_CONFIDENCE_THRESHOLD));
			lastPageConfidenceThreshold = Float.parseFloat(getPropertyMap().get(
					DocumentAssemblerConstants.LAST_PAGE_CONFIDENCE_THRESHOLD));
		} catch (NumberFormatException e) {
			errMsg = new StringBuilder();
			errMsg.append("The value of confidence thresholds in the properties file is not a valid number. ");
			errMsg.append(e.getMessage());
			LOGGER.error(errMsg.toString(), e);
			throw new DCMAApplicationException(errMsg.toString(), e);
		}
		if (!isFromWebService && null == this.xmlDocuments) {
			throw new DCMAApplicationException("Unable to write pages for the document.");
		}
		try {
			performDocCreationForPages(insertAllDocument, docPageInfo, isFromWebService);
		} catch (Exception e) {
			errMsg = new StringBuilder();
			errMsg.append("Unable to write pages for the document. ");
			errMsg.append(e.getMessage());
			LOGGER.error(errMsg.toString(), e);
			throw new DCMAApplicationException(errMsg.toString(), e);
		}
	}

	/**
	 * Perform document creation for pages being processed.
	 * 
	 * @param insertAllDocument {@link List <Document>} created list of documents.
	 * @param docPageInfo {@link List <Page>} the list of pages in the batch being processed.
	 * @param isFromWebService boolean to check if this request is from web service or not.
	 * @throws DCMAApplicationException
	 */
	private void performDocCreationForPages(List<Document> insertAllDocument, List<Page> docPageInfo, boolean isFromWebService)
			throws DCMAApplicationException {
		StringBuilder errMsg;
		List<Integer> removeIndexList = new ArrayList<Integer>();
		Document currentDocument = null;
		DocField docPage = null;
		Long idGenerator = 0L;
		AdvancedDocumentAssembler docAssemblerProperties = new AdvancedDocumentAssembler();
		docAssemblerProperties.setFirst(true);
		docAssemblerProperties.setLast(false);
		String checkLastPage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE);
		String checkMiddlePage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE);
		String checkFirstPage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE);
		docAssemblerProperties.setIdGenerator(0L);
		for (int index = 0; index < docPageInfo.size(); index++) {
			docAssemblerProperties.setIndex(index);

			// The page being processed.
			Page pgType = docPageInfo.get(index);

			// The page level field for the current classification type is stored in it.
			DocField docFieldType = getPgLevelField(pgType);
			if (null == docFieldType) {
				errMsg = new StringBuilder();
				errMsg.append("Invalid format of page level fields. DocFieldType found for ");
				errMsg.append(getPropertyMap().get(DocumentAssemblerConstants.IMAGE_CLASSIFICATION));
				errMsg.append(" classification is null.");
				throw new DCMAApplicationException(errMsg.toString());
			}
			// The type of page ex: first, middle or last.
			String value = docFieldType.getValue();
			float confidenceScore = docFieldType.getConfidence();
			if (null == value) {
				errMsg = new StringBuilder();
				errMsg.append("Invalid format of page level fields. Value found for ");
				errMsg.append(getPropertyMap().get(DocumentAssemblerConstants.IMAGE_CLASSIFICATION));
				errMsg.append(" classification is null.");
				throw new DCMAApplicationException(errMsg.toString());
			}
			// check for zero confidence score value.
			// for zero value just leave the page to unknown type.
			// zero confidence pages.
			if (confidenceScore == 0) {
				idGenerator = docAssemblerProperties.getIdGenerator();
				idGenerator++;
				currentDocument = createNewDocument(insertAllDocument, idGenerator);
				mergeDocument(removeIndexList, currentDocument, index, pgType);
				currentDocument.setType(EphesoftProperty.UNKNOWN.getProperty());
				currentDocument.setDescription(EphesoftProperty.UNKNOWN.getProperty());
				docPage = docFieldType;
				docAssemblerProperties.setIdGenerator(idGenerator);
				// If the classification of the document is empty then set current docPage as null.
				if (EphesoftStringUtil.isNullOrEmpty(value)) {
					docAssemblerProperties.setDocPage(null);
				} else {
					docAssemblerProperties.setDocPage(docPage);
				}
				docAssemblerProperties.setCurrentDocument(currentDocument);
				continue;
			}
			// In case of first page scenario.
			if (value.contains(checkFirstPage)) {
				docAssemblerProperties = currentFirstPageCase(docAssemblerProperties, docFieldType, pgType, removeIndexList,
						insertAllDocument);
			}
			// In case of middle page scenario.
			else if (value.contains(checkMiddlePage)) {
				docAssemblerProperties = currentMiddlePageCase(docAssemblerProperties, docFieldType, pgType, removeIndexList,
						insertAllDocument);
			}
			// In case of last page scenario.
			else if (value.contains(checkLastPage)) {
				docAssemblerProperties = currentLastPageCase(docAssemblerProperties, docFieldType, pgType, removeIndexList,
						insertAllDocument);
			}
			// if no value is matched...............
			else {
				errMsg = new StringBuilder();
				errMsg.append("For page type value: ");
				errMsg.append(value);
				errMsg.append("  and page ID : ");
				errMsg.append(pgType.getIdentifier());
				errMsg.append(" , Data format is not correct in the batch.xml file. ");
				errMsg.append(checkFirstPage);
				errMsg.append(" , ");
				errMsg.append(checkMiddlePage);
				errMsg.append(" and ");
				errMsg.append(checkLastPage);
				errMsg.append(" any of the three are not present to the name <Value> tag.");
				LOGGER.info(errMsg.toString());
			}
		}
		if (isFromWebService) {
			updateBatchXMLAPI(insertAllDocument, isFromWebService);
		} else {
			// update the xml file.
			updateBatchXML(insertAllDocument, removeIndexList);
		}
	}

	/**
	 * Case handling if the current page is of middle page type.
	 * 
	 * @param {@link AdvancedDocumentAssembler} the properties object for processing.
	 * @param docFieldType {@link DocField} the current doc page.
	 * @param pgType {@link Page} the current page.
	 * @param removeIndexList {@link List <Integer>} the list of indexes that have been removed.
	 * @param insertAllDocument {@link List <Document>} the list of all the documents created.
	 * @return {@link AdvancedDocumentAssembler} the updated properties object.
	 */
	private AdvancedDocumentAssembler currentMiddlePageCase(AdvancedDocumentAssembler docAssemblerProperties, DocField docFieldType,
			Page pgType, List<Integer> removeIndexList, List<Document> insertAllDocument) {
		boolean merge = false;
		Long idGenerator = docAssemblerProperties.getIdGenerator();
		DocField docPage = docAssemblerProperties.getDocPage();
		int index = docAssemblerProperties.getIndex();
		Document currentDocument = docAssemblerProperties.getCurrentDocument();
		String checkLastPage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE);
		String checkMiddlePage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE);
		String checkFirstPage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE);
		if (null == docPage) {
			// create a new document, add the page into it and set the previous page as docFieldType.
			idGenerator++;
			currentDocument = createNewDocument(insertAllDocument, idGenerator);
			mergeDocument(removeIndexList, currentDocument, index, pgType);
			docPage = docFieldType;
			docAssemblerProperties.setLast(false);
			docAssemblerProperties.setFirst(false);
		} else {
			String docPageValue = docPage.getValue();
			float docPageConfidence = docPage.getConfidence();
			String classifiedPageType = getClassifiedPageType(docFieldType);
			String previousPageType = getClassifiedPageType(docPage);
			if (docPageValue.contains(checkMiddlePage)) {
				if (classifiedPageType.equalsIgnoreCase(previousPageType)) {
					if (docAssemblerProperties.isLast()) {
						// create a new document, add the page into it and set the previous page as docFieldType.
						idGenerator++;
						currentDocument = createNewDocument(insertAllDocument, idGenerator);
						mergeDocument(removeIndexList, currentDocument, index, pgType);
						docPage = docFieldType;
						docAssemblerProperties.setLast(false);
					} else {
						// both the pages are of type A_MIDDLE_PAGE and A_MIDDLE_PAGE
						mergeDocument(removeIndexList, currentDocument, index, pgType);
					}
				} else {
					// both are first page of type A_MIDDLE_PAGE and B_MIDDLE_PAGE
					// check for alternate values
					Field maxAlternate = getAlternateConfidenceForFirst(docFieldType, previousPageType);
					if (null != maxAlternate) {
						merge = mergeVerificationForMiddleAndLast(merge, docAssemblerProperties, docPageConfidence,
								middlePageConfidenceThreshold, lastPageConfidenceThreshold, checkMiddlePage, checkLastPage,
								maxAlternate);
					}
					if (merge) {
						mergeDocument(removeIndexList, currentDocument, index, pgType);
					} else {
						// if we do not have to merge then we should create a new document for this.......
						idGenerator++;
						currentDocument = createNewDocument(insertAllDocument, idGenerator);
						docAssemblerProperties.setFirst(false);
						docAssemblerProperties.setLast(false);
						mergeDocument(removeIndexList, currentDocument, index, pgType);
						docPage = docFieldType;
					}
				}
			} else {
				// in case of first page is currently open...
				if (docPageValue.contains(checkFirstPage)) {
					// if open page is a middle page and we encounter a first page........
					if (classifiedPageType.equalsIgnoreCase(previousPageType)) {
						if (docAssemblerProperties.isLast()) {
							idGenerator++;
							currentDocument = createNewDocument(insertAllDocument, idGenerator);
							docAssemblerProperties.setFirst(false);
							docAssemblerProperties.setLast(false);
							mergeDocument(removeIndexList, currentDocument, index, pgType);
							docPage = docFieldType;
						} else {
							mergeDocument(removeIndexList, currentDocument, index, pgType);
						}
					} else {
						// if both are different then we can check for merger.....
						Field maxAlternate = getAlternateConfidenceForFirst(docFieldType, previousPageType);
						if (null != maxAlternate) {
							merge = mergeVerificationForMiddleAndLast(merge, docAssemblerProperties, docPageConfidence,
									middlePageConfidenceThreshold, lastPageConfidenceThreshold, checkMiddlePage, checkLastPage,
									maxAlternate);
						}
						if (merge) {
							mergeDocument(removeIndexList, currentDocument, index, pgType);
						} else {
							// if we do not have to merge then we should create a new document for this.......
							idGenerator++;
							currentDocument = createNewDocument(insertAllDocument, idGenerator);
							docAssemblerProperties.setFirst(false);
							docAssemblerProperties.setLast(false);
							mergeDocument(removeIndexList, currentDocument, index, pgType);
							docPage = docFieldType;
						}
					}
				} else if (docPageValue.contains(checkLastPage)) {
					// if open page is a last page and then we encounter a first page...........
					if (classifiedPageType.equalsIgnoreCase(previousPageType)) {
						// if both are of same document type then have to create a new document............
						idGenerator++;
						currentDocument = createNewDocument(insertAllDocument, idGenerator);
						mergeDocument(removeIndexList, currentDocument, index, pgType);
						docPage = docFieldType;
						docAssemblerProperties.setLast(true);
					} else {
						// if both are different then we can check for merger.....
						Field maxAlternate = getAlternateConfidenceForLast(docFieldType, previousPageType);
						if (null != maxAlternate) {
							merge = mergeVerificationForLast(merge, docPageConfidence, lastPageConfidenceThreshold, maxAlternate);
						}
						docAssemblerProperties.setLast(true);
						if (merge) {
							mergeDocument(removeIndexList, currentDocument, index, pgType);
						} else {
							// if we do not have to merge then we should create a new document for this.......
							idGenerator++;
							currentDocument = createNewDocument(insertAllDocument, idGenerator);
							mergeDocument(removeIndexList, currentDocument, index, pgType);
							docPage = docFieldType;
						}
					}
				}
			}
		}
		populatePropertiesObject(docAssemblerProperties, idGenerator, docPage, index, currentDocument);
		return docAssemblerProperties;
	}

	/**
	 * Case handling if the current page is of last page type.
	 * 
	 * @param {@link AdvancedDocumentAssembler} the properties object for processing.
	 * @param docFieldType {@link DocField} the current doc page.
	 * @param currentDocument {@link Document} the current document being processed.
	 * @param pgType {@link Page} the current page.
	 * @param removeIndexList {@link List <Integer>} the list of indexes that have been removed.
	 * @param index int the current index of page.
	 * @param insertAllDocument {@link List <Document>} the list of all the documents created.
	 * @return {@link AdvancedDocumentAssembler} the updated properties object.
	 */
	private AdvancedDocumentAssembler currentLastPageCase(AdvancedDocumentAssembler docAssemblerProperties, DocField docFieldType,
			Page pgType, List<Integer> removeIndexList, List<Document> insertAllDocument) {
		boolean merge = false;
		Long idGenerator = docAssemblerProperties.getIdGenerator();
		DocField docPage = docAssemblerProperties.getDocPage();
		int index = docAssemblerProperties.getIndex();
		Document currentDocument = docAssemblerProperties.getCurrentDocument();
		String checkLastPage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE);
		docAssemblerProperties.setLast(true);
		if (null == docPage) {
			// create a new document, add the page into it and set the previous page as docFieldType.
			idGenerator++;
			currentDocument = createNewDocument(insertAllDocument, idGenerator);
			mergeDocument(removeIndexList, currentDocument, index, pgType);
			docPage = docFieldType;
		} else {
			String docPageValue = docPage.getValue();
			float docPageConfidence = docPage.getConfidence();
			String classifiedPageType = getClassifiedPageType(docFieldType);
			String previousPageType = getClassifiedPageType(docPage);
			if (docPageValue.contains(checkLastPage)) {
				if (classifiedPageType.equalsIgnoreCase(previousPageType)) {
					// both the pages are of type A_LAST_PAGE and A_LAST_PAGE
					mergeDocument(removeIndexList, currentDocument, index, pgType);
				} else {
					// both are first page of type A_LAST_PAGE and B_LAST_PAGE
					// check for alternate values
					Field maxAlternate = getAlternateConfidenceForLast(docFieldType, previousPageType);
					if (null != maxAlternate) {
						merge = mergeVerificationForLast(merge, docPageConfidence, lastPageConfidenceThreshold, maxAlternate);
					}
					if (merge) {
						mergeDocument(removeIndexList, currentDocument, index, pgType);
					} else {
						// if we do not have to merge then we should create a new document for this.......
						idGenerator++;
						currentDocument = createNewDocument(insertAllDocument, idGenerator);
						mergeDocument(removeIndexList, currentDocument, index, pgType);
						docPage = docFieldType;
					}
				}
			} else {
				// in case of middle or last page is currently open...
				// if open page is a first or middle page and we encounter a first page........
				if (classifiedPageType.equalsIgnoreCase(previousPageType)) {
					mergeDocument(removeIndexList, currentDocument, index, pgType);
				} else {
					// if both are different then we can check for merger.....
					Field maxAlternate = getAlternateConfidenceForLast(docFieldType, previousPageType);
					if (null != maxAlternate) {
						merge = mergeVerificationForLast(merge, docPageConfidence, lastPageConfidenceThreshold, maxAlternate);
					}
					if (merge) {
						mergeDocument(removeIndexList, currentDocument, index, pgType);
					} else {
						// if we do not have to merge then we should create a new document for this.......
						idGenerator++;
						currentDocument = createNewDocument(insertAllDocument, idGenerator);
						mergeDocument(removeIndexList, currentDocument, index, pgType);
						docPage = docFieldType;
					}
				}
			}
		}
		populatePropertiesObject(docAssemblerProperties, idGenerator, docPage, index, currentDocument);
		return docAssemblerProperties;
	}

	/**
	 * This method is used for extracting the classified name for the page provided.
	 * 
	 * @param docFieldType {@link DocField} the document level field for the current working page for classification.
	 * @return {@link String} the classified name of the doc field.
	 */
	private String getClassifiedPageType(final DocField docFieldType) {
		String value = docFieldType.getValue();
		int index = 0;
		if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE))) {
			index = value.indexOf(getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE));
		} else if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE))) {
			index = value.indexOf(getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE));
		} else if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE))) {
			index = value.indexOf(getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE));
		}
		String classificationName = value.substring(0, index);
		return classificationName;
	}

	/**
	 * Case handling if the current page is of first page type.
	 * 
	 * @param {@link AdvancedDocumentAssembler} the properties object for processing.
	 * @param docFieldType {@link DocField} the current doc page.
	 * @param pgType {@link Page} the current page.
	 * @param removeIndexList {@link List <Integer>} the list of indexes that have been removed.
	 * @param insertAllDocument {@link List <Document>} the list of all the documents created.
	 * @return {@link AdvancedDocumentAssembler} the updated properties object.
	 */
	private AdvancedDocumentAssembler currentFirstPageCase(AdvancedDocumentAssembler docAssemblerProperties, DocField docFieldType,
			Page pgType, List<Integer> removeIndexList, List<Document> insertAllDocument) {
		boolean merge = false;
		Long idGenerator = docAssemblerProperties.getIdGenerator();
		DocField docPage = docAssemblerProperties.getDocPage();
		int index = docAssemblerProperties.getIndex();
		Document currentDocument = docAssemblerProperties.getCurrentDocument();
		float confidenceScore = docFieldType.getConfidence();
		String checkLastPage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE);
		String checkMiddlePage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE);
		String checkFirstPage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE);
		if (null == docPage || confidenceScore >= firstPageConfidenceThreshold) {
			// create a new document, add the page into it and set the previous page as docFieldType.
			idGenerator++;
			currentDocument = createNewDocument(insertAllDocument, idGenerator);
			mergeDocument(removeIndexList, currentDocument, index, pgType);
			docPage = docFieldType;
			docAssemblerProperties.setFirst(true);
			docAssemblerProperties.setLast(false);
		} else {
			String docPageValue = docPage.getValue();
			float docPageConfidence = docPage.getConfidence();
			String classifiedPageType = getClassifiedPageType(docFieldType);
			String previousPageType = getClassifiedPageType(docPage);
			if (docPageValue.contains(checkFirstPage)) {
				if (classifiedPageType.equalsIgnoreCase(previousPageType)) {
					// both the pages are of type A_FIRST_PAGE and A_FIRST_PAGE
					// close the old document and add this page to the new document
					idGenerator++;
					currentDocument = createNewDocument(insertAllDocument, idGenerator);
					mergeDocument(removeIndexList, currentDocument, index, pgType);
					docPage = docFieldType;
					docAssemblerProperties.setFirst(true);
					docAssemblerProperties.setLast(false);
				} else {
					// if pages are first page of type A_FIRST_PAGE and B_FIRST_PAGE
					// check for alternate values
					Field maxAlternate = getAlternateConfidenceForFirst(docFieldType, previousPageType);
					if (null != maxAlternate) {
						merge = mergeVerificationForMiddleAndLast(merge, docAssemblerProperties, docPageConfidence,
								middlePageConfidenceThreshold, lastPageConfidenceThreshold, checkMiddlePage, checkLastPage,
								maxAlternate);
					}
					if (merge) {
						// we have to merge so simply put the page in this existing document.......
						mergeDocument(removeIndexList, currentDocument, index, pgType);
					} else {
						// if we do not have to merge then we should create a new document for this.......
						if (docAssemblerProperties.isFirst()) {
							idGenerator++;
							currentDocument = createNewDocument(insertAllDocument, idGenerator);
						}
						docAssemblerProperties.setFirst(true);
						docAssemblerProperties.setLast(false);
						mergeDocument(removeIndexList, currentDocument, index, pgType);
						docPage = docFieldType;
					}
				}
			} else {
				// in case of middle or last page is currently open...
				if (docPageValue.contains(checkMiddlePage)) {
					// set last as false cause open page is middle, so after merging also last is not set.
					docAssemblerProperties.setLast(false);
					// if open page is a middle page and we encounter a first page........
					if (classifiedPageType.equalsIgnoreCase(previousPageType)) {
						// if both are of same document type then create a new document as after middle page first can't be
						// added............
						idGenerator++;
						currentDocument = createNewDocument(insertAllDocument, idGenerator);
						mergeDocument(removeIndexList, currentDocument, index, pgType);
						docPage = docFieldType;
						docAssemblerProperties.setFirst(true);
					} else {
						// if both are different then we can check for merger.....
						Field maxAlternate = getAlternateConfidenceForFirst(docFieldType, previousPageType);
						if (null != maxAlternate) {
							merge = mergeVerificationForMiddleAndLast(merge, docAssemblerProperties, docPageConfidence,
									middlePageConfidenceThreshold, lastPageConfidenceThreshold, checkMiddlePage, checkLastPage,
									maxAlternate);
						}
						if (merge) {
							mergeDocument(removeIndexList, currentDocument, index, pgType);
						} else {
							// if we do not have to merge then we should create a new document for this.......
							idGenerator++;
							currentDocument = createNewDocument(insertAllDocument, idGenerator);
							docAssemblerProperties.setFirst(true);
							mergeDocument(removeIndexList, currentDocument, index, pgType);
							docPage = docFieldType;
						}
					}
				} else if (docPageValue.contains(checkLastPage)) {
					// if open page is a last page and then we encounter a first page...........
					if (classifiedPageType.equalsIgnoreCase(previousPageType)) {
						// if both are of same document type then create a new document............
						idGenerator++;
						currentDocument = createNewDocument(insertAllDocument, idGenerator);
						mergeDocument(removeIndexList, currentDocument, index, pgType);
						docPage = docFieldType;
						docAssemblerProperties.setFirst(true);
						docAssemblerProperties.setLast(false);
					} else {
						// if both are different then we can check for merger.....
						Field maxAlternate = getAlternateConfidenceForLast(docFieldType, previousPageType);
						if (null != maxAlternate) {
							merge = mergeVerificationForLast(merge, docPageConfidence, lastPageConfidenceThreshold, maxAlternate);
						}
						if (merge) {
							mergeDocument(removeIndexList, currentDocument, index, pgType);
							docAssemblerProperties.setLast(true);
						} else {
							// if we do not have to merge then we should create a new document for this.......
							if (docAssemblerProperties.isFirst()) {
								idGenerator++;
								currentDocument = createNewDocument(insertAllDocument, idGenerator);
							}
							docAssemblerProperties.setFirst(true);
							docAssemblerProperties.setLast(false);
							mergeDocument(removeIndexList, currentDocument, index, pgType);
							docPage = docFieldType;
						}
					}
				}
			}
		}
		populatePropertiesObject(docAssemblerProperties, idGenerator, docPage, index, currentDocument);
		return docAssemblerProperties;
	}

	/**
	 * Populating the properties object for advanced document assembler algorithm processing.
	 * 
	 * @param docAssemblerProperties {@link AdvancedDocumentAssembler} the current properties object.
	 * @param idGenerator {@link Long} the id.
	 * @param docPage {@link DocField} the current doc field being processed.
	 * @param index int the index of page being processed.
	 * @param currentDocument {@link Document} the current open document.
	 */
	private void populatePropertiesObject(AdvancedDocumentAssembler docAssemblerProperties, Long idGenerator, DocField docPage,
			int index, Document currentDocument) {
		docAssemblerProperties.setIdGenerator(idGenerator);
		docAssemblerProperties.setCurrentDocument(currentDocument);
		docAssemblerProperties.setDocPage(docPage);
		docAssemblerProperties.setIndex(index);
	}

	/**
	 * Merging verifications for middle and last page type with current open document and current page.
	 * 
	 * @param merge {@link Boolean} the condition flag for merging.
	 * @param docAssemblerProperties {@link Boolean} the properties for current condition.
	 * @param docPageConfidence float confidence of open document page.
	 * @param middlePageConfidenceThreshold float the threshold confidence level for middle page.
	 * @param lastPageConfidenceThreshold float the threshold confidence level for last page.
	 * @param checkMiddlePage {@link String} value tag for middle page type.
	 * @param checkLastPage {@link String} value tag for last page type.
	 * @param maxAlternate {@link Field} the maximum alternate value.
	 * @return boolean the flag for merging.
	 */
	private boolean mergeVerificationForMiddleAndLast(boolean merge, AdvancedDocumentAssembler docAssemblerProperties,
			float docPageConfidence, float middlePageConfidenceThreshold, float lastPageConfidenceThreshold, String checkMiddlePage,
			String checkLastPage, Field maxAlternate) {
		String maxAlternateValue = maxAlternate.getValue();
		float maxAlternateConfidence = maxAlternate.getConfidence();
		if (maxAlternateValue.contains(checkMiddlePage)) {
			if (docAssemblerProperties.isLast()) {
				merge = false;
			} else {
				merge = checkCompatibilityForMerging(maxAlternateConfidence, docPageConfidence, middlePageConfidenceThreshold);
			}
			docAssemblerProperties.setLast(false);
		} else if (maxAlternateValue.contains(checkLastPage)) {
			merge = checkCompatibilityForMerging(maxAlternateConfidence, docPageConfidence, lastPageConfidenceThreshold);
			docAssemblerProperties.setLast(true);
		}
		return merge;
	}

	/**
	 * Compatibility of the current open document page and the new page's alternate value for merging.
	 * 
	 * @param alternateConfidence float the confidence of the page's alternate value.
	 * @param pageConfidence float the confidence of the page in open document.
	 * @param threshold float the threshold for this comparison.
	 * @return boolean true if compatible for merging, false otherwise.
	 */
	private boolean checkCompatibilityForMerging(float alternateConfidence, float pageConfidence, float threshold) {
		boolean merge = false;
		if ((pageConfidence - alternateConfidence) < threshold) {
			merge = true;
		}
		return merge;
	}

	/**
	 * Merging verifications for last page type with current open document and current page.
	 * 
	 * @param merge {@link boolean} the condition flag for merging.
	 * @param docPageConfidence float confidence of open document page.
	 * @param lastPageConfidenceThreshold float the threshold confidence level for last page.
	 * @param maxAlternate {@link Field} the maximum alternate value.
	 * @return boolean the flag for merging.
	 */
	private boolean mergeVerificationForLast(boolean merge, float docPageConfidence, float lastPageConfidenceThreshold,
			Field maxAlternate) {
		if (null == maxAlternate) {
			merge = false;
		} else {
			float maxAlternateConfidence = maxAlternate.getConfidence();
			merge = checkCompatibilityForMerging(maxAlternateConfidence, docPageConfidence, lastPageConfidenceThreshold);
		}
		return merge;
	}

	/**
	 * Get the maximum confidence for the type of page passed from the alternate values from the doc field passed.
	 * 
	 * @param docFieldType {@link DocField} the docField for this current classification.
	 * @param previousPageType {@link String} The document type of the open document.
	 * @return {@link Field}
	 */
	private Field getAlternateConfidenceForLast(DocField docFieldType, String previousPageType) {
		Field maxAlternateValue = null;
		float highestConfidence = 0;
		String lastPage = previousPageType + getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE);
		AlternateValues alternateValues = docFieldType.getAlternateValues();
		if (alternateValues != null) {
			List<Field> alternateValueList = alternateValues.getAlternateValue();
			if (alternateValueList != null && alternateValueList.size() > 0) {
				for (int i = 0; i < alternateValueList.size(); i++) {
					if (i >= 5) {
						break;
					}
					Field alternateValue = alternateValueList.get(i);
					if (getPropertyMap().get(DocumentAssemblerConstants.IMAGE_CLASSIFICATION).equals(alternateValue.getName())) {
						if (alternateValue.getValue().equalsIgnoreCase(lastPage)) {
							if (highestConfidence < alternateValue.getConfidence()) {
								highestConfidence = alternateValue.getConfidence();
								maxAlternateValue = alternateValue;
							}
						}
					}
				}
			}
		}
		return maxAlternateValue;
	}

	/**
	 * Get the maximum confidence for the type of page passed from the alternate values from the doc field passed.
	 * 
	 * @param docFieldType {@link DocField} the docField for this current classification.
	 * @param previousPageType {@link String} The document type of the open document.
	 * @return {@link Field}
	 */
	private Field getAlternateConfidenceForFirst(DocField docFieldType, String previousPageType) {
		Field maxAlternateValue = null;
		float highestConfidence = 0;
		String middlePage = previousPageType + getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE);
		String lastPage = previousPageType + getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE);
		AlternateValues alternateValues = docFieldType.getAlternateValues();
		if (alternateValues != null) {
			List<Field> alternateValueList = alternateValues.getAlternateValue();
			if (alternateValueList != null && alternateValueList.size() > 0) {
				for (int i = 0; i < alternateValueList.size(); i++) {
					if (i >= 5) {
						break;
					}
					Field alternateValue = alternateValueList.get(i);
					if (getPropertyMap().get(DocumentAssemblerConstants.IMAGE_CLASSIFICATION).equals(alternateValue.getName())) {
						if (alternateValue.getValue().equalsIgnoreCase(middlePage)) {
							if (highestConfidence <= alternateValue.getConfidence()) {
								highestConfidence = alternateValue.getConfidence();
								maxAlternateValue = alternateValue;
							}
						} else if (alternateValue.getValue().equalsIgnoreCase(lastPage)) {
							if (highestConfidence < alternateValue.getConfidence()) {
								highestConfidence = alternateValue.getConfidence();
								maxAlternateValue = alternateValue;
							}
						}
					}
				}
			}
		}
		return maxAlternateValue;
	}

	/**
	 * Creating a new document.
	 * 
	 * @param insertAllDocument {@link List} list of all the documents already added.
	 * @param idGenerator {@link Long} the identifier for current document to be added.
	 * @return {@link Document} the new created document.
	 */
	private Document createNewDocument(List<Document> insertAllDocument, Long idGenerator) {
		Document currentDocument;
		currentDocument = new Document();
		Pages pages = new Pages();
		currentDocument.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + idGenerator);
		currentDocument.setConfidence(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
		currentDocument.setConfidenceThreshold(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
		currentDocument.setReviewed(Boolean.FALSE);
		currentDocument.setValid(Boolean.FALSE);
		currentDocument.setPages(pages);
		currentDocument.setDocumentDisplayInfo(BatchConstants.EMPTY);
		insertAllDocument.add(currentDocument);
		return currentDocument;
	}

	/**
	 * The current document to merge.
	 * 
	 * @param removeIndexList {@link List} the list of all the pages that have been already added.
	 * @param currentDocument {@link Document} the document being processed currently.
	 * @param index int the index of page to be added in removeIndexList.
	 * @param pgType {@link Page} the page being processed currently.
	 */
	private void mergeDocument(List<Integer> removeIndexList, Document currentDocument, int index, Page pgType) {
		currentDocument.getPages().getPage().add(pgType);
		removeIndexList.add(index);
	}

	/**
	 * This method will create new document for pages that was found in the batch.xml file for Unknown type document.
	 * 
	 * @param docPageInfo List<PageType>
	 * @throws DCMAApplicationException Check for input parameters, create new documents for page found in document type Unknown.
	 */
	public final void createDocForPagesOldAlgorithm(List<Document> insertAllDocument, final List<Page> docPageInfo,
			final boolean isFromWebService) throws DCMAApplicationException {

		String errMsg = null;
		if (!isFromWebService) {
			if (null == this.xmlDocuments) {
				throw new DCMAApplicationException("Unable to write pages for the document.");
			}
		}

		try {

			List<Integer> removeIndexList = new ArrayList<Integer>();
			Document document = null;
			Long idGenerator = 0L;

			boolean isLast = true;
			boolean isFirst = true;

			for (int index = 0; index < docPageInfo.size(); index++) {

				Page pgType = docPageInfo.get(index);
				DocField docFieldType = getPgLevelField(pgType);
				if (null == docFieldType) {
					errMsg = "Invalid format of page level fields. DocFieldType found for "
							+ getPropertyMap().get(DocumentAssemblerConstants.IMAGE_CLASSIFICATION) + " classification is null.";
					throw new DCMAApplicationException(errMsg);
				}

				String value = docFieldType.getValue();
				float confidenceScore = docFieldType.getConfidence();

				if (null == value) {
					errMsg = "Invalid format of page level fields. Value found for "
							+ getPropertyMap().get(DocumentAssemblerConstants.IMAGE_CLASSIFICATION) + " classification is null.";
					throw new DCMAApplicationException(errMsg);
				}

				// check for zero confidence score value
				// for zero value just leave the page to unknown type.

				if (confidenceScore == 0) {
					document = new Document();
					Pages pages = new Pages();
					idGenerator++;
					document.setConfidence(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
					document.setConfidenceThreshold(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
					document.setReviewed(Boolean.FALSE);
					document.setValid(Boolean.FALSE);
					document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + idGenerator);
					document.setPages(pages);
					document.setDocumentDisplayInfo(BatchConstants.EMPTY);
					insertAllDocument.add(document);
					document.getPages().getPage().add(pgType);
					document.setType(EphesoftProperty.UNKNOWN.getProperty());
					document.setDescription(EphesoftProperty.UNKNOWN.getProperty());
					removeIndexList.add(index);
					continue;
				}

				if (isLast) {
					document = new Document();
					document.setConfidence(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
					document.setConfidenceThreshold(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
					document.setReviewed(Boolean.FALSE);
					document.setValid(Boolean.FALSE);
					Pages pages = new Pages();
					idGenerator++;
					document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + idGenerator);
					document.setPages(pages);
					document.setDocumentDisplayInfo(BatchConstants.EMPTY);
					insertAllDocument.add(document);
					isLast = false;
					isFirst = false;
				}

				if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE))) {
					if (isFirst) {
						document = new Document();
						document.setConfidence(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
						document.setConfidenceThreshold(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
						document.setReviewed(Boolean.FALSE);
						document.setValid(Boolean.FALSE);
						Pages pages = new Pages();
						idGenerator++;
						document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + idGenerator);
						document.setPages(pages);
						document.setDocumentDisplayInfo(BatchConstants.EMPTY);
						insertAllDocument.add(document);
					}
					isFirst = true;
					document.getPages().getPage().add(pgType);
					removeIndexList.add(index);
				} else {
					if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE))) {
						document.getPages().getPage().add(pgType);
						removeIndexList.add(index);
						isFirst = true;
					} else {
						if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE))) {
							document.getPages().getPage().add(pgType);
							removeIndexList.add(index);
							isLast = true;
						} else {
							errMsg = "For page type value: " + value + "  and page ID : " + pgType.getIdentifier()
									+ " , Data format is not correct in the batch.xml file. "
									+ getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE) + " , "
									+ getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE) + " and "
									+ getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE)
									+ " any of the three are not present to the name <Value> tag.";
							LOGGER.error(errMsg);
						}
					}
				}
			}

			if (isFromWebService) {
				updateBatchXMLAPI(insertAllDocument, isFromWebService);
			} else {
				// update the xml file.
				updateBatchXML(insertAllDocument, removeIndexList);
			}
		} catch (Exception e) {
			errMsg = "Unable to write pages for the document. " + e.getMessage();
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg, e);
		}
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

		Batch batch = batchSchemaService.getBatch(batchInstanceID);

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

		// set the confidence score and document type name on the basis of
		// defined rules.
		setDocConfAndDocType(xmlDocuments, false);

		// setting the value to predefined document type if the confidence is more than the threshold value. If confidence is less then
		// set the value to unknown.
		String predefinedDocType = getPropertyMap().get(DocumentAssemblerConstants.PREDEFINED_DOCUMENT_TYPE).trim();
		try {
			Float confThreshold = Float.parseFloat(getPropertyMap().get(
					DocumentAssemblerConstants.PREDEFINED_DOCUMENT_CONFIDENCE_THRESHOLD));
			if (null != predefinedDocType && !predefinedDocType.isEmpty()) {
				DocumentType predefinedDocTypeDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService,
						predefinedDocType, batchInstanceID, false, null);
				if (null != predefinedDocTypeDesc) {
					for (Document doc : xmlDocuments) {
						if (!EphesoftProperty.UNKNOWN.getProperty().equalsIgnoreCase(doc.getType())
								&& doc.getConfidence() < confThreshold) {
							doc.setType(predefinedDocType);
							doc.setDescription(predefinedDocTypeDesc.getDescription());
						}
					}
				}
			}
		} catch (NumberFormatException noFormatException) {
			LOGGER.error("The value of predefined document type could not be set because the confidence threshold is incorrect.");
		}
		// merge the unknown documents on the basis of a check
		String mergeSwitch = getPropertyMap().get(DocumentAssemblerConstants.DA_MERGE_UNKNOWN_DOCUMENT_SWITCH);
		int xmlDocSize = xmlDocuments.size();
		if (mergeSwitch != null && mergeSwitch.equalsIgnoreCase(DocumentAssemblerConstants.DA_SWITCH_ON) && xmlDocSize > 1) {
			for (int i = 1; i < xmlDocuments.size();) {
				final Document document = xmlDocuments.get(i);
				String docType = document.getType();
				if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
					final Document prevDocument = xmlDocuments.get(i - 1);
					if (null != prevDocument.getType() && !prevDocument.getType().equals(EphesoftProperty.UNKNOWN.getProperty())) {
						prevDocument.getPages().getPage().addAll(document.getPages().getPage());
						xmlDocuments.remove(i);
					} else {
						i++;
					}
				} else {
					i++;
				}
			}
		}
		// resetting the list of identifiers.
		xmlDocuments = DocumentAssembler.setUpdatedListIdentifiers(xmlDocuments);

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
		// setting the batch level documentClassificationTypes.
		DocumentClassificationTypes documentClassificationTypes = new DocumentClassificationTypes();
		List<String> documentClassificationType = documentClassificationTypes.getDocumentClassificationType();
		documentClassificationType.add(getPropertyMap().get(DocumentAssemblerConstants.FACTORY_CLASSIFICATION));
		batch.setDocumentClassificationTypes(documentClassificationTypes);

		// Fetching the list of all the document types defined for a particular batch.
		List<String> documnetTypes = DocumentAssembler.getDocumentTypes(pluginPropertiesService, batchInstanceID);
		// Fetching the value of the delete document's first page switch.
		String deleteDocumentFirstPageSwitch = getPropertyMap().get(DocumentAssemblerConstants.SWITCH_DELETE_DOCUMENT_FIRST_PAGE);
		// Set the error message explicitly to blank to display the node in batch xml
		for (int i = 0; i < xmlDocuments.size(); i++) {
			final Document document = xmlDocuments.get(i);
			// If there is only one document type defined then set the type of document to the name of that document.
			if (documnetTypes != null && documnetTypes.size() == 1) {
				DocumentType documentDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, documnetTypes.get(0),
						batchInstanceID, false, null);
				document.setType(documnetTypes.get(0));
				document.setDescription(documentDesc.getDescription());
			}
			document.setErrorMessage("");
			// Deleting the document's first page if the switch is ON.
			if (DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(deleteDocumentFirstPageSwitch)) {
				DocumentAssembler.removeFirstPageOfDocument(document);
			}
		}
		// now write the state of the object to the xml file.
		batchSchemaService.updateBatch(batch);

		LOGGER.info("updateBatchXML done.");

	}

	/**
	 * Update Batch XML file.
	 * 
	 * @param insertAllDocument List<DocumentType>
	 * @throws DCMAApplicationException Check for input parameters, update the batch xml.
	 */
	private void updateBatchXMLAPI(List<Document> insertAllDocument, boolean isFromWebService) throws DCMAApplicationException {
		// set the confidence score and document type name on the basis of
		// defined rules.
		setDocConfAndDocType(insertAllDocument, isFromWebService);
		// Fetching the list of all the document types defined for a particular batch.
		List<DocumentType> documnetTypes = docTypeService.getDocTypeByBatchClassIdentifier(batchClassID, -1, -1);
		String predefinedDocType = getPropertyMap().get(DocumentAssemblerConstants.PREDEFINED_DOCUMENT_TYPE).trim();
		try {
			Float confThreshold = Float.parseFloat(getPropertyMap().get(
					DocumentAssemblerConstants.PREDEFINED_DOCUMENT_CONFIDENCE_THRESHOLD));
			if (null != predefinedDocType && !predefinedDocType.isEmpty()) {
				DocumentType predefinedDocTypeDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService,
						predefinedDocType, batchClassID, isFromWebService, docTypeService);
				if (null != predefinedDocTypeDesc) {
					for (Document doc : insertAllDocument) {
						if (!EphesoftProperty.UNKNOWN.getProperty().equalsIgnoreCase(doc.getType())
								&& doc.getConfidence() < confThreshold) {
							doc.setType(predefinedDocType);
							doc.setDescription(predefinedDocTypeDesc.getDescription());
						}
					}
				}
			}
		} catch (NumberFormatException noFormatException) {
			LOGGER.error("The value of predefined document type could not be set because the confidence threshold is incorrect.");
		}

		// merge the unknown documents on the basis of a check
		String mergeSwitch = getPropertyMap().get(DocumentAssemblerConstants.DA_MERGE_UNKNOWN_DOCUMENT_SWITCH);
		int xmlDocSize = insertAllDocument.size();
		if (mergeSwitch != null && mergeSwitch.equalsIgnoreCase(DocumentAssemblerConstants.DA_SWITCH_ON) && xmlDocSize > 1) {
			for (int i = 1; i < insertAllDocument.size();) {
				final Document document = insertAllDocument.get(i);
				String docType = document.getType();
				if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
					final Document prevDocument = insertAllDocument.get(i - 1);
					if (null != prevDocument.getType() && !prevDocument.getType().equals(EphesoftProperty.UNKNOWN.getProperty())) {
						prevDocument.getPages().getPage().addAll(document.getPages().getPage());
						insertAllDocument.remove(i);
					} else {
						i++;
					}
				} else {
					i++;
				}
			}
		}
		// resetting the list of identifiers.
		insertAllDocument = DocumentAssembler.setUpdatedListIdentifiers(insertAllDocument);

		// set default document type for unknown documents if the switch is on.
		String switchUnknownDocType = getPropertyMap().get(DocumentAssemblerConstants.SWITCH_UNKNOWN_PREDEFINED_DOCUMENT_TYPE);
		String unknownDocType = getPropertyMap().get(DocumentAssemblerConstants.UNKNOWN_PREDEFINED_DOCUMENT_TYPE).trim();
		if (null != switchUnknownDocType && DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(switchUnknownDocType)) {
			if (null != unknownDocType && !unknownDocType.isEmpty()) {
				DocumentType unknownDocTypeDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, unknownDocType,
						batchClassID, isFromWebService, docTypeService);
				if (null != unknownDocTypeDesc) {
					for (Document doc : insertAllDocument) {
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
		for (int i = 0; i < insertAllDocument.size(); i++) {
			final Document document = insertAllDocument.get(i);
			// If there is only one document type defined except UNKNOWN then set the type of document to the name of that document. By
			// default the list will return a document UNKNOWN also so default value for one document type in web service and Test
			// content classification is set to 2.
			if (documnetTypes != null && documnetTypes.size() == 2) {
				DocumentType documentDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, documnetTypes.get(1)
						.getName(), batchClassID, isFromWebService, docTypeService);
				document.setType(documnetTypes.get(1).getName());
				document.setDescription(documentDesc.getDescription());
			}
			// Deleting the document's first page if the switch is ON.
			if (DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(deleteDocumentFirstPageSwitch)) {
				DocumentAssembler.removeFirstPageOfDocument(document);
			}
		}
		LOGGER.info("updateBatchXML for web services done.");
	}

	/**
	 * This method will retrieve the page level field type docFieldType for any input page type for current classification name.
	 * 
	 * @param pgType PageType
	 * @return docFieldType DocFieldType
	 */
	private DocField getPgLevelField(final Page pgType) {

		DocField docFieldType = null;
		String name = null;
		if (null != pgType) {
			PageLevelFields pageLevelFields = pgType.getPageLevelFields();
			if (null != pageLevelFields) {
				List<DocField> pageLevelField = pageLevelFields.getPageLevelField();
				for (DocField docFdType : pageLevelField) {
					if (null != docFdType) {
						name = docFdType.getName();
						if (null != name && name.contains(getPropertyMap().get(DocumentAssemblerConstants.IMAGE_CLASSIFICATION))) {
							docFieldType = docFdType;
							break;
						}
					}
				}
			}
		}

		return docFieldType;

	}

	/**
	 * This method will set the confidence score of every document on the basis of average of all the page confidence score.
	 * 
	 * @param xmlDocuments List<DocumentType>
	 */
	@SuppressWarnings("unchecked")
	public void setDocConfAndDocType(final List<Document> xmlDocuments, boolean isFromWebService) {

		Map<String, Object[]> docConfidence = null;
		float confidenceScoreMax = 0.0f;
		String pageTypeName = null;
		float localSum = 0.0f;

		for (Document docType : xmlDocuments) {
			String type = docType.getType();
			if (null != type && type.equals(EphesoftProperty.UNKNOWN.getProperty())) {
				continue;
			}
			processDocument(docType, docConfidence, confidenceScoreMax, pageTypeName, localSum, isFromWebService);
			type = docType.getType();
			if (type == null || type.isEmpty()) {
				docType.setType(EphesoftProperty.UNKNOWN.getProperty());
				docType.setDescription(EphesoftProperty.UNKNOWN.getProperty());
			}
		}

	}

	public void setDocTypeNameAndConfThresholdAPI(final Document docType, final String pageTypeName) {

		List<com.ephesoft.dcma.da.domain.DocumentType> docTypes = docTypeService
				.getDocTypeByBatchClassIdentifier(batchClassID, -1, -1);

		String docTypeName = null, docTypeNameTemp = null;
		String docTypeDesc = null;
		float minConfidenceThreshold = 0;

		final Iterator<com.ephesoft.dcma.da.domain.DocumentType> itr = docTypes.iterator();
		while (itr.hasNext()) {
			final com.ephesoft.dcma.da.domain.DocumentType docTypeDB = itr.next();
			docTypeNameTemp = docTypeDB.getName();
			if (pageTypeName.contains(docTypeNameTemp)) {
				docTypeName = docTypeNameTemp;
				docTypeDesc = docTypeDB.getDescription();
				minConfidenceThreshold = docTypeDB.getMinConfidenceThreshold();
				LOGGER.debug(EphesoftStringUtil.concatenate("DocumentType name : ", docTypeName, "  minConfidenceThreshold : ",
						minConfidenceThreshold));
				break;
			}
		}

		if (null == docTypeName) {
			final String errMsg = "DocumentType name is not found in the data base " + "for page type name: " + pageTypeName;
			LOGGER.info(errMsg);
		} else {
			docType.setType(docTypeName);
			docType.setDescription(docTypeDesc);
			final DecimalFormat twoDForm = new DecimalFormat("#.##");
			minConfidenceThreshold = Float.valueOf(twoDForm.format(minConfidenceThreshold));
			docType.setConfidenceThreshold(minConfidenceThreshold);
		}
	}

	/**
	 * This method will apply the rule to calculate the confidence score.
	 * 
	 * @param checkTypeList List<String>
	 * @return multiplyingFactor float
	 */
	private float multiplyingFactor(final List<String> checkTypeList) {

		// fp + mp + lp = 1
		// fp = 0.50
		// lp = 0.50
		// mp = 0.25
		// fp + lp = 0.75
		// fp + mp = 0.50
		// mp + lp = 0.50

		float multiplyingFactor = 1.00f;
		float intialFactor = 1.00f;
		String checkFirstPage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE);
		String checkMiddlePage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE);
		String checkLastPage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE);
		int fmlPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_FML_PAGE));
		int fPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_F_PAGE));
		int mPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_M_PAGE));
		int lPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_L_PAGE));
		int fmPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_FM_PAGE));
		int flPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_FL_PAGE));
		int mlPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_ML_PAGE));

		if (null != checkTypeList) {

			// A = First_Page
			// B = Middle_Page
			// C = Last_Page
			// CBA
			// 101 = 5
			// 100 = 4
			// 111 = 7
			// 010 = 2

			int placeHolder = 0;

			if (checkTypeList.contains(checkFirstPage)) {
				placeHolder = placeHolder | PlaceHolder.FP.getValue();
			}

			if (checkTypeList.contains(checkMiddlePage)) {
				placeHolder = placeHolder | PlaceHolder.MP.getValue();
			}

			if (checkTypeList.contains(checkLastPage)) {
				placeHolder = placeHolder | PlaceHolder.LP.getValue();
			}

			switch (PlaceHolder.getPlaceHolder(placeHolder)) {
				case FP:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (fPage * intialFactor) / fmlPage;
					break;
				case MP:
					// multiplyingFactor = 0.25f;
					multiplyingFactor = (mPage * intialFactor) / fmlPage;
					break;
				case FMP:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (fmPage * intialFactor) / fmlPage;
					break;
				case LP:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (lPage * intialFactor) / fmlPage;
					break;
				case FLP:
					// multiplyingFactor = 0.75f;
					multiplyingFactor = (flPage * intialFactor) / fmlPage;
					break;
				case MLP:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (mlPage * intialFactor) / fmlPage;
					break;
				case FMLP:
					// multiplyingFactor = 1.00f;
					multiplyingFactor = (fmlPage * intialFactor) / fmlPage;
					break;
				default:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (fPage * intialFactor) / fmlPage;
					break;
			}
		}

		return multiplyingFactor;
	}

	/**
	 * This method will traverse the page level fields.
	 * 
	 * @param pageLevelFields PageLevelFields
	 * @param docConfidence Map<String, List<Float>>
	 */
	@SuppressWarnings("unchecked")
	private void traversePgFds(final PageLevelFields pageLevelFields, final Map<String, Object[]> docConfidence) {

		String name = null;
		String value = null;
		String checkType = null;

		List<DocField> pageLevelField = pageLevelFields.getPageLevelField();
		for (DocField docFdType : pageLevelField) {
			if (null != docFdType) {
				name = docFdType.getName();
				if (null != name && name.contains(getPropertyMap().get(DocumentAssemblerConstants.IMAGE_CLASSIFICATION))) {
					AlternateValues alternateValues = docFdType.getAlternateValues();
					if (null != alternateValues) {
						List<Field> alternateValue = alternateValues.getAlternateValue();
						value = docFdType.getValue();
						if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE))) {
							checkType = getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE);
						} else if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE))) {
							checkType = getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE);
						} else if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE))) {
							checkType = getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE);
						} else {
							LOGGER.info("In valid page level value tag.");
						}
						// if doc field type is empty then do nothing.
						if (null != checkType) {
							for (Field fdType : alternateValue) {
								String val = fdType.getValue();
								float confidence = fdType.getConfidence();
								if (null != val && val.contains(checkType)) {
									String[] arr = val.split(checkType);
									if (null != arr && arr.length > 0) {
										Object[] objArr = docConfidence.get(arr[0]);
										List<Float> conFloatList = null;
										List<String> checkTypeList = null;
										if (null == objArr) {
											objArr = new Object[2];
											conFloatList = new ArrayList<Float>();
											checkTypeList = new ArrayList<String>();
										} else {
											checkTypeList = (List<String>) objArr[0];
											conFloatList = (List<Float>) objArr[1];
										}
										checkTypeList.add(checkType);
										conFloatList.add(confidence);
										objArr[0] = checkTypeList;
										objArr[1] = conFloatList;
										docConfidence.put(arr[0], objArr);
									}
								}
							}
						}
					}
				}
			}
		}

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

		final Batch batch = batchSchemaService.getBatch(this.batchInstanceID);

		this.xmlDocuments = batch.getDocuments().getDocument();

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
	 * @param documentList
	 */
	public void reClassifyDocuments(final List<Document> documentList) {
		LOGGER.debug("Entering method reClassifyDocuments for " + batchInstanceID);
		if (documentList != null && !documentList.isEmpty()) {
			Map<String, Object[]> docConfidence = null;
			float confidenceScoreMax = 0.0f;
			String pageTypeName = null;
			float localSum = 0.0f;
			String docId = null;
			Long idGenerator = 0L;
			for (Document document : documentList) {
				idGenerator++;
				document.setDocumentDisplayInfo(BatchConstants.EMPTY);
				docId = EphesoftProperty.DOCUMENT.getProperty() + idGenerator;
				document.setIdentifier(docId);
				String docType = document.getType();
				LOGGER.debug("Processing document " + docId + " having type " + docType);
				if (docType == null || docType.isEmpty() || docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
					// If document type is null or empty, perform reclassification.
					processDocument(document, docConfidence, confidenceScoreMax, pageTypeName, localSum, false);
				} else {
					// If document has already been classified, set its confidence threshold.
					setConfThreshold(docType, document);
				}
				docType = document.getType();
				if (docType == null || docType.isEmpty()) {
					document.setType(EphesoftProperty.UNKNOWN.getProperty());
					document.setDescription(EphesoftProperty.UNKNOWN.getProperty());
				}
			}
		}
		LOGGER.debug("Exiting method reClassifyDocuments for " + batchInstanceID);
	}

	private void setConfThreshold(final String docTypeName, final Document document) {
		if (docTypeName != null && document != null) {
			com.ephesoft.dcma.da.domain.DocumentType docType = docTypeService.getDocTypeByBatchClassAndDocTypeName(batchClassID,
					docTypeName);
			if (docType != null) {
				float confThreshold = docType.getMinConfidenceThreshold();
				document.setConfidenceThreshold(confThreshold);
			}
		}
	}

	private void processDocument(final Document docType, Map<String, Object[]> docConfidence, float confidenceScoreMax,
			String pageTypeName, float localSum, boolean isFromWebService) {

		docConfidence = new HashMap<String, Object[]>();
		confidenceScoreMax = 0.0f;

		Pages pages = docType.getPages();
		if (null != pages) {
			List<Page> pageList = pages.getPage();
			for (Page pgType : pageList) {
				if (null != pgType) {
					PageLevelFields pageLevelFields = pgType.getPageLevelFields();
					if (null != pageLevelFields) {
						traversePgFds(pageLevelFields, docConfidence);
					}
				}
			}
		}
		if (docConfidence != null && !docConfidence.isEmpty()) {
			Set<String> set = docConfidence.keySet();
			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				Object[] objArr = docConfidence.get(key);
				List<Float> conFloatList = (List<Float>) objArr[1];
				localSum = 0.0f;
				for (Float f : conFloatList) {
					localSum += f;
				}
				if (confidenceScoreMax < localSum) {
					confidenceScoreMax = localSum;
					pageTypeName = key;
				}
			}

			Object[] objArr = docConfidence.get(pageTypeName);
			List<Float> conFloatList = (List<Float>) objArr[1];
			List<String> checkTypeList = (List<String>) objArr[0];

			float multiplyingFactor = 1.00f;
			try {
				multiplyingFactor = multiplyingFactor(checkTypeList);
			} catch (Exception e) {
				LOGGER.error("Invalid multiplyingFactor. " + e.getMessage());
			}

			float confidenceScore = (multiplyingFactor * confidenceScoreMax) / conFloatList.size();
			LOGGER.info("multiplyingFactor : " + multiplyingFactor + "  confidence score : " + confidenceScore
					+ " for document Type ID : " + docType.getIdentifier());

			DecimalFormat twoDForm = new DecimalFormat("#.##");
			confidenceScore = Float.valueOf(twoDForm.format(confidenceScore));

			docType.setConfidence(confidenceScore);
			if (null == checkTypeList || checkTypeList.isEmpty()) {
				StringBuilder stringBuilder = new StringBuilder("");
				stringBuilder.append(pageTypeName);
				stringBuilder.append(getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE));
				pageTypeName = stringBuilder.toString();
			} else {
				StringBuilder stringBuilder = new StringBuilder("");
				stringBuilder.append(pageTypeName);
				stringBuilder.append(checkTypeList.get(0));
				pageTypeName = stringBuilder.toString();
			}
			if (isFromWebService) {
				setDocTypeNameAndConfThresholdAPI(docType, pageTypeName);
			} else {
				setDocTypeNameAndConfThreshold(docType, pageTypeName);
			}
		}
	}

}
