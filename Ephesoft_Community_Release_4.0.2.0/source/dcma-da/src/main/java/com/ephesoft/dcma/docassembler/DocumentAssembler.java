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

package com.ephesoft.dcma.docassembler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Page.PageLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.da.service.PageTypeService;
import com.ephesoft.dcma.docassembler.classification.DocumentClassification;
import com.ephesoft.dcma.docassembler.classification.automatic.AutomaticClassification;
import com.ephesoft.dcma.docassembler.classification.barcode.BarcodeClassification;
import com.ephesoft.dcma.docassembler.classification.engine.SearchClassification;
import com.ephesoft.dcma.docassembler.classification.image.ImageClassification;
import com.ephesoft.dcma.docassembler.constant.DocumentAssemblerConstants;
import com.ephesoft.dcma.docassembler.factory.DocumentClassificationFactory;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * This class retrieves a proper classification (Barcode, Image and Search classification) based on factory classification specified in
 * resources.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.service.DocumentAssemblerServiceImpl
 * @see com.ephesoft.dcma.docassembler.classification.DocumentClassification
 */
@Component
public class DocumentAssembler {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentAssembler.class);

	/**
	 * barcode Classification.
	 */
	private String barcodeClassification;

	/**
	 * lucene Classification.
	 */
	private String luceneClassification;

	/**
	 * image Classification.
	 */
	private String imageClassification;

	/**
	 * automatic Classification.
	 */
	private String automaticClassification;

	/**
	 * automatic Classification. inclusion list
	 */
	private String automaticIncludeList;

	/**
	 * check first page.
	 */
	private String checkFirstPage;

	/**
	 * check first page.
	 */
	private String checkMiddlePage;

	/**
	 * check first page.
	 */
	private String checkLastPage;

	/**
	 * Reference of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Reference of PageTypeService.
	 */
	@Autowired
	private PageTypeService pageTypeService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService bcPluginPropertiesService;

	/**
	 * docTypeService DocumentTypeService.
	 */
	@Autowired
	private DocumentTypeService docTypeService;

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
	 * @return the luceneClassification
	 */
	public final String getLuceneClassification() {
		return luceneClassification;
	}

	/**
	 * @param luceneClassification the luceneClassification to set
	 */
	public final void setLuceneClassification(final String luceneClassification) {
		this.luceneClassification = luceneClassification;
	}

	/**
	 * @return the imageClassification
	 */
	public final String getImageClassification() {
		return imageClassification;
	}

	/**
	 * @param imageClassification the imageClassification to set
	 */
	public final void setImageClassification(final String imageClassification) {
		this.imageClassification = imageClassification;
	}

	/**
	 * @return the automaticClassification inclusion list
	 */

	public String getAutomaticIncludeList() {
		return automaticIncludeList;
	}

	/**
	 * @param automaticIncludeList the automaticClassification inclusion list to set
	 */
	public void setAutomaticIncludeList(String automaticIncludeList) {
		this.automaticIncludeList = automaticIncludeList;
	}

	/**
	 * @return the automaticClassification
	 */

	public String getAutomaticClassification() {
		return automaticClassification;
	}

	/**
	 * @param automaticClassification the automaticClassification to set
	 */

	public void setAutomaticClassification(String automaticClassification) {
		this.automaticClassification = automaticClassification;
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
	 * @return the checkFirstPage
	 */
	public final String getCheckFirstPage() {
		return checkFirstPage;
	}

	/**
	 * @param checkFirstPage the checkFirstPage to set
	 */
	public final void setCheckFirstPage(final String checkFirstPage) {
		this.checkFirstPage = checkFirstPage;
	}

	/**
	 * @return the checkMiddlePage
	 */
	public final String getCheckMiddlePage() {
		return checkMiddlePage;
	}

	/**
	 * @param checkMiddlePage the checkMiddlePage to set
	 */
	public final void setCheckMiddlePage(final String checkMiddlePage) {
		this.checkMiddlePage = checkMiddlePage;
	}

	/**
	 * @return the checkLastPage
	 */
	public final String getCheckLastPage() {
		return checkLastPage;
	}

	/**
	 * @param checkLastPage the checkLastPage to set
	 */
	public final void setCheckLastPage(final String checkLastPage) {
		this.checkLastPage = checkLastPage;
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
	 * This method will create new documents for all the pages of the document type Unknown.
	 * 
	 * @param batchInstanceID String
	 * @throws DCMAApplicationException Check for input parameters, read all pages of document and create document for every page.
	 */
	public final void createDocument(final String batchInstanceID) throws DCMAApplicationException {

		// Check all the input parameters and fields loaded from property file.
		checkInputParams(batchInstanceID);

		// TODO read this from work flow.
		String factoryClassification = pluginPropertiesService.getPropertyValue(batchInstanceID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_FACTORY_CLASS);

		DocumentClassification documentClassification = DocumentClassificationFactory.getDocumentClassification(factoryClassification);

		// TODO read this from work flow.

		documentClassification.processUnclassifiedPages(this, batchInstanceID, pluginPropertiesService);

	}

	/**
	 * This method will create new documents for all the pages of the document type Unknown.
	 * 
	 * @param batchInstanceID String
	 * @throws DCMAApplicationException Check for input parameters, read all pages of document and create document for every page.
	 */
	public final List<Document> createDocumentAPI(final DocumentClassificationFactory classType, final String batchClassID,
			final List<Page> docPageInfo) throws DCMAApplicationException {
		List<Document> doc = null;
		DocumentClassification documentClassification = DocumentClassificationFactory.getDocumentClassification(classType
				.getNameClassification());
		if (classType.compareTo(DocumentClassificationFactory.IMAGE) == 0) {
			ImageClassification imgClassification = (ImageClassification) documentClassification;
			doc = imgClassification.processUnclassifiedPagesAPI(docPageInfo, this, batchClassID, bcPluginPropertiesService);
		} else if (classType.compareTo(DocumentClassificationFactory.SEARCHCLASSIFICATION) == 0) {
			SearchClassification searchClassification = (SearchClassification) documentClassification;
			doc = searchClassification.processUnclassifiedPagesAPI(docPageInfo, this, batchClassID, bcPluginPropertiesService);
		} else if (classType.compareTo(DocumentClassificationFactory.BARCODE) == 0) {
			BarcodeClassification barcodeClassification = (BarcodeClassification) documentClassification;
			doc = barcodeClassification.processUnclassifiedPagesAPI(docPageInfo, this, batchClassID, bcPluginPropertiesService);
		} else if (classType.compareTo(DocumentClassificationFactory.AUTOMATIC) == 0) {
			AutomaticClassification automaticClassification = (AutomaticClassification) documentClassification;
			doc = automaticClassification.processUnclassifiedPagesAPI(docPageInfo, this, batchClassID, bcPluginPropertiesService);
		}
		return doc;
	}

	/**
	 * This method will check all the input parameters for document assembly.
	 * 
	 * @param batchInstanceID String
	 * @throws DCMAApplicationException Check input parameters.
	 */
	private void checkInputParams(final String batchInstanceID) throws DCMAApplicationException {

		String errorMsg = null;

		LOGGER.info("Checking all the fields loaded from peroperty files.");

		// check for batch instance ID.
		if (null == batchInstanceID || "".equals(batchInstanceID)) {
			errorMsg = "Invalid argument batchInstanceID.";
			LOGGER.error(errorMsg);
			throw new DCMAApplicationException(errorMsg);
		}

		LOGGER.info("Input batch instance ID for Document Assembler is " + batchInstanceID);

		// check for barcode Classification name.
		if (null == this.barcodeClassification || "".equals(this.barcodeClassification)) {
			errorMsg = "Invalid initalization of barcodeClassification with properties file.";
			LOGGER.error(errorMsg);
			throw new DCMAApplicationException(errorMsg);
		}

		// check for lucene Classification name.
		if (null == this.luceneClassification || "".equals(this.luceneClassification)) {
			errorMsg = "Invalid initalization of luceneClassification with properties file.";
			LOGGER.error(errorMsg);
			throw new DCMAApplicationException(errorMsg);
		}

		// check for image Classification name.
		if (null == this.imageClassification || "".equals(this.imageClassification)) {
			errorMsg = "Invalid initalization of imageClassification with properties file.";
			LOGGER.error(errorMsg);
			throw new DCMAApplicationException(errorMsg);
		}

		// check for automatic Classification name.
		if (null == this.automaticClassification || "".equals(this.automaticClassification)) {
			errorMsg = "Invalid initalization of automaticClassification with properties file.";
			LOGGER.error(errorMsg);
			throw new DCMAApplicationException(errorMsg);
		}
	}

	public void reCreateDocumentXml(String batchInstanceIdentifer, final String batchClassIdentifier, List<Document> documentList)
			throws DCMAApplicationException {
		// Check all the input parameters and fields loaded from property file.
		LOGGER.debug("Entering method reCreateDocumentXml for " + batchInstanceIdentifer);
		checkInputParams(batchInstanceIdentifer);

		String factoryClassification = pluginPropertiesService.getPropertyValue(batchInstanceIdentifer,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_FACTORY_CLASS);

		// Getting the classification to be used for reclassification of documents.
		DocumentClassification documentClassification = DocumentClassificationFactory.getDocumentClassification(factoryClassification);

		documentClassification.processDocumentTypes(this, batchInstanceIdentifer, batchClassIdentifier, documentList);

		LOGGER.debug("Exiting method reCreateDocumentXml for " + batchInstanceIdentifer);
	}

	/**
	 * Fetches the list of all the document types defined for a particular batch.
	 * 
	 * @param pluginPropertiesService {@link PluginPropertiesService} The instance of pluginPropertiesService needed for fetching the
	 *            list of all the document types for a particular batch.
	 * @param batchInstanceIdentifier {@link String} The batch Instance identifier.
	 * @return {@link List <{@link String}> The list of document types.
	 * 
	 */
	public static List<String> getDocumentTypes(final PluginPropertiesService pluginPropertiesService,
			final String batchInstanceIdentifier) {
		List<String> documnetTypes = null;
		if (batchInstanceIdentifier != null) {
			List<DocumentType> batchDocumentList = pluginPropertiesService.getDocumentTypes(batchInstanceIdentifier);
			if (batchDocumentList != null && !batchDocumentList.isEmpty()) {
				documnetTypes = new ArrayList<String>();
				for (DocumentType documentType : batchDocumentList) {
					if (documentType != null && !EphesoftProperty.UNKNOWN.getProperty().equalsIgnoreCase(documentType.getName())) {
						documnetTypes.add(documentType.getName());
					}
				}
			}
		}
		return documnetTypes;
	}

	/**
	 * Removes the first page of the document if the document contains more than one page.If document contains only single page then it
	 * will nor remove that page.
	 * 
	 * @param document {@link Document} The instance of Document.
	 */
	public static void removeFirstPageOfDocument(final Document document) {
		if (document != null && document.getPages() != null && document.getPages().getPage() != null
				&& document.getPages().getPage().size() > 1) {
			document.getPages().getPage().remove(0);
		}
	}

	/**
	 * This api returns the document type if present of provided document name.
	 * 
	 * @param pluginPropertiesService {@link PluginPropertiesService} the service of plugin properties.
	 * @param docTypeName {@link String} the name of document type name.
	 * @param batchInstanceIdentifier {@link String} the batch instance identifier.
	 * @return {@link DocumentType} the document of provided name.
	 */
	public static DocumentType getDescriptionForDocument(final PluginPropertiesService pluginPropertiesService,
			final String docTypeName, String batchInstanceIdentifier, boolean isFromWebService,
			final DocumentTypeService docTypeService) {
		DocumentType document = null;
		if (batchInstanceIdentifier != null) {
			List<DocumentType> batchDocumentList = null;
			// If call is from web service then get the document list from the doc type service as the batch instance identifier is
			// null in this case.
			if (isFromWebService) {
				batchDocumentList = docTypeService.getDocTypeByBatchClassIdentifier(batchInstanceIdentifier, -1, -1);
			} else {
				batchDocumentList = pluginPropertiesService.getDocumentTypes(batchInstanceIdentifier);
			}
			if (batchDocumentList != null && !batchDocumentList.isEmpty()) {
				for (DocumentType documentType : batchDocumentList) {
					if (documentType != null && !EphesoftProperty.UNKNOWN.getProperty().equalsIgnoreCase(documentType.getName())
							&& docTypeName.equalsIgnoreCase(documentType.getName())) {
						document = documentType;
						break;
					}
				}
			}
		}
		return document;
	}

	public static void kvClassification(final List<Document> xmlDocumentList, final String regex,
			final DocumentType documentType) {
		List<Document> updatedDocumentList = new ArrayList<Document>();
		for (int index = 0; index < xmlDocumentList.size(); index++) {
			Document documentInstance = xmlDocumentList.get(index);
			if (null != documentInstance) {
				if (documentInstance.getType().equalsIgnoreCase(EphesoftProperty.UNKNOWN.getProperty())) {
					List<Page> breakPagesList = getDocBreakPages(documentInstance, regex);
					if (breakPagesList == null || breakPagesList.isEmpty()) {
						updatedDocumentList.add(documentInstance);
					} else {
						List<Page> allPageList = documentInstance.getPages().getPage();
						List<Page> addPages = new ArrayList<Page>();
						for (Page currentPage : allPageList) {
							if (breakPagesList.contains(currentPage)) {
								if (addPages.size() > 0) {
									Document emptyDocument = new Document();
									Pages pages = new Pages();
									emptyDocument.setConfidence(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
									emptyDocument.setConfidenceThreshold(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
									emptyDocument.setReviewed(Boolean.FALSE);
									emptyDocument.setValid(Boolean.FALSE);
									pages.getPage().addAll(addPages);
									emptyDocument.setPages(pages);
									emptyDocument.setType(documentType.getName());
									emptyDocument.setDescription(documentType.getDescription());
									emptyDocument = populateDocumentCopy(documentInstance, emptyDocument);
									updatedDocumentList.add(emptyDocument);
									addPages.clear();
									pages.getPage().clear();
								}
							}
							addPages.add(currentPage);
						}
						if (addPages.size() > 0) {
							Document emptyDocument = new Document();
							Pages pages = new Pages();
							pages.getPage().addAll(addPages);
							emptyDocument.setConfidence(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
							emptyDocument.setConfidenceThreshold(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
							emptyDocument.setReviewed(Boolean.FALSE);
							emptyDocument.setValid(Boolean.FALSE);
							emptyDocument.setPages(pages);
							emptyDocument.setType(documentType.getName());
							emptyDocument.setDescription(documentType.getDescription());
							emptyDocument = populateDocumentCopy(documentInstance, emptyDocument);
							updatedDocumentList.add(emptyDocument);
						}
					}
				} else {
					updatedDocumentList.add(documentInstance);
				}
			}
		}
		// Correct the identifiers for updated list of documents.
		setUpdatedListIdentifiers(updatedDocumentList);
		xmlDocumentList.clear();
		xmlDocumentList.addAll(updatedDocumentList);
	}

	private static List<Page> getDocBreakPages(Document documentInstance, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher;
		List<Page> breakPagesList = new ArrayList<Page>();
		Pages pages = documentInstance.getPages();
		if (null != pages) {
			List<Page> pageList = pages.getPage();
			if (null != pageList && pageList.size() > 0) {
				for (Page page : pageList) {
					PageLevelFields pageLevelFields = page.getPageLevelFields();
					if (null != pageLevelFields) {
						List<DocField> fieldList = pageLevelFields.getPageLevelField();
						if (null != fieldList && fieldList.size() > 0) {
							for (DocField fieldType : fieldList) {
								if (null != fieldType) {
									String fieldTypeName = fieldType.getName();
									if (null != fieldTypeName
											&& !fieldTypeName.isEmpty()
											&& (DocumentAssemblerConstants.BARCODE_TYPE_NAME.equalsIgnoreCase(fieldTypeName) || !(DocumentAssemblerConstants.SEARCH_CLASSIFICATION_TYPE_NAME
													.equalsIgnoreCase(fieldTypeName) || DocumentAssemblerConstants.IMAGE_CLASSIFICATION_TYPE_NAME
													.equalsIgnoreCase(fieldTypeName)))) {
										String value = fieldType.getValue();
										matcher = pattern.matcher(value);
										if ((!value.isEmpty()) && matcher.find()) {
											breakPagesList.add(page);
											break;
										}

									}
								}
							}
						}
					}
				}
			}
		}
		return breakPagesList;
	}

	private static Document populateDocumentCopy(Document documentInstance, Document emptyDocument) {
		emptyDocument.setConfidence(documentInstance.getConfidence());
		emptyDocument.setConfidenceThreshold(documentInstance.getConfidenceThreshold());
		emptyDocument.setValid(documentInstance.isValid());
		emptyDocument.setReviewed(documentInstance.isReviewed());
		return emptyDocument;
	}

	public static List<Document> setUpdatedListIdentifiers(List<Document> updatedDocumentList) {
		int index = 1;
		String docConstant = "DOC";
		for (Document document : updatedDocumentList) {
			document.setIdentifier(EphesoftStringUtil.concatenate(docConstant, index));
			index++;
		}
		return updatedDocumentList;
	}

}
