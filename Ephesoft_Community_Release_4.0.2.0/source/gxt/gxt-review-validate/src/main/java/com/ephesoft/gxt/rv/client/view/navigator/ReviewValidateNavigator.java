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

package com.ephesoft.gxt.rv.client.view.navigator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.core.client.DCMAEntryPoint;
import com.ephesoft.gxt.core.client.DragDropFlowPanel.DragImage;
import com.ephesoft.gxt.core.client.ui.resource.Location;
import com.ephesoft.gxt.core.client.ui.widget.property.Validatable;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.PointCoordinate;
import com.ephesoft.gxt.core.shared.dto.StickyDocumentDataDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.metadata.DocumentMetaData;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.FieldSelectionChangeEvent;
import com.ephesoft.gxt.rv.client.event.PluginPropertiesLoadEvent;
import com.ephesoft.gxt.rv.client.widget.DataTableGrid;
import com.ephesoft.gxt.rv.shared.metadata.PluginPropertiesMetaData;
import com.ephesoft.gxt.rv.shared.metadata.ReviewValidateMetaData;
import com.google.gwt.user.client.Timer;

public class ReviewValidateNavigator {

	private static String baseHttpURL;
	private static String currentBatchInstanceIdentifier;
	private static BatchInstanceStatus batchInstanceStatus;
	private static String batchInstanceName;
	private static String batchInstancePrority;
	private static List<String> documentIdentifiersList;
	private static Map<String, Document> loadedDocumentsMap;
	private static Document currentDocument;
	private static Map<String, DocumentMetaData> documentMetadataMap;
	private static boolean advanceDocumentOpenSwitch = true;
	private static List<String> documentTypeList;
	private static Page currentPage;
	private static Map<String, Document> alteredDocumentMap;
	private static boolean defaultSaveOperation;
	private static int saveInterval;
	private static int currentCounter = 1;
	private static Map<String, DocumentTypeDTO> documentTypeMap;
	private static DocField currentSelectedDocField;
	private static Validatable lastSelectedWidget;
	private static Field currentField;
	private static Column selectedColumn;
	private static DataTableGrid currentGrid;
	private static String batchClassIdentifier;
	private static int zoomCount;
	private static DragImage currentSelectedThumbnail;
	private static String insertionScriptTableName;
	private static int rowIndex;
	private static String indexFieldSeparator;
	private static boolean isPageSelectionEnable = true;
	private static boolean isStickyIndexFieldEnable;
	private static boolean isShowSuggestions = false;
	private static boolean isShowTablesSuggestions = false;
	/**
	 * Map holds the sticky index fields details against the document names.
	 */
	private static Map<String, StickyDocumentDataDTO> stickyDocumentMap = null;

	public static final void create(final ReviewValidateMetaData reviewValidateMetadata) {
		if (null != reviewValidateMetadata) {
			performNewBatchLoadOperation(reviewValidateMetadata);
			baseHttpURL = reviewValidateMetadata.getBaseHTTPUrl();
			batchInstanceStatus = reviewValidateMetadata.getBatchInstanceStatus();
			documentMetadataMap = reviewValidateMetadata.getDocumentMetadata();
			createDocIdentifiersList(documentMetadataMap);
			batchClassIdentifier = reviewValidateMetadata.getBatchClassIdentifier();
			loadedDocumentsMap = null;
			batchInstanceName = reviewValidateMetadata.getBatchInstanceName();
			batchInstancePrority = reviewValidateMetadata.getBatchInstancePriority();
			documentTypeList = reviewValidateMetadata.getDocumentTypeNamesList();
			alteredDocumentMap = new HashMap<String, Document>();
			currentBatchInstanceIdentifier = reviewValidateMetadata.getBatchInstanceIdentifier();
			formRVURL();
			documentTypeMap = new HashMap<String, DocumentTypeDTO>();
			zoomCount = reviewValidateMetadata.getZoomCount();
			stickyDocumentMap = new HashMap<String, StickyDocumentDataDTO>();
		} else {
			throw new UnsupportedOperationException("Cannot create Navigator without any batch metadata.");
		}
	}

	private static void formRVURL() {
		if (!StringUtil.isNullOrEmpty(currentBatchInstanceIdentifier)) {
			String baseURL = DCMAEntryPoint.BASE_URL;
			String reviewValidateURL = StringUtil.concatenate(baseURL, CoreCommonConstant.URL_SEPARATOR,
					CoreCommonConstant.REVIEW_VALIDATE_RESOURCE_NAME, "?batch_id=", currentBatchInstanceIdentifier);
			Location.assignURlWitoutReload(reviewValidateURL);
		}
	}

	private static void performNewBatchLoadOperation(final ReviewValidateMetaData metaData) {
		final String batchInstanceIdentifier = metaData.getBatchInstanceIdentifier();
		if (!StringUtil.isNullOrEmpty(batchInstanceIdentifier)
				&& !batchInstanceIdentifier.equalsIgnoreCase(ReviewValidateNavigator.currentBatchInstanceIdentifier)) {
			//Fix for Client Issue : EPHE-8996 - Priority Issue: Sticky Fields not working in 4.0.2.0
			Timer timer = new Timer() {

				@Override
				public void run() {
					ReviewValidateEventBus.fireEvent(new PluginPropertiesLoadEvent(batchInstanceIdentifier, metaData
							.getBatchInstanceStatus()));
				}
			};
			timer.schedule(20);
		}
	}

	public static void merge(final PluginPropertiesMetaData pluginPropertiesMetadata) {
		if (null != pluginPropertiesMetadata) {
			saveInterval = pluginPropertiesMetadata.getSaveInterval();
			defaultSaveOperation = pluginPropertiesMetadata.isDefaultSaveOperation();
			indexFieldSeparator = pluginPropertiesMetadata.getFieldValueSeparator();
			isStickyIndexFieldEnable = pluginPropertiesMetadata.getStickyIndexFieldSwitch();
			isShowSuggestions = pluginPropertiesMetadata.isShowSuggestions();
			isShowTablesSuggestions = pluginPropertiesMetadata.isShowTablesSuggestions();
		}
	}

	private static final void createDocIdentifiersList(final Map<String, DocumentMetaData> documentMap) {
		if (!CollectionUtil.isEmpty(documentMap)) {
			documentIdentifiersList = new LinkedList<String>();
			final Set<String> docIdentifiersSet = documentMap.keySet();
			for (final String documentIdentifier : docIdentifiersSet) {
				documentIdentifiersList.add(documentIdentifier);
			}
		}
	}

	/**
	 * @return the batchInstanceStatus
	 */
	public static BatchInstanceStatus getBatchInstanceStatus() {
		return batchInstanceStatus;
	}

	/**
	 * @return the documentIdentifiers
	 */
	public static List<String> getDocumentIdentifiersList() {
		return documentIdentifiersList;
	}

	/**
	 * @return the currentDocument
	 */
	public static Document getCurrentDocument() {
		return currentDocument;
	}

	/**
	 * @param currentDocument the currentDocument to set
	 */
	public static void setCurrentDocument(final Document currentDocument) {
		ReviewValidateNavigator.currentDocument = currentDocument;
	}

	/**
	 * @return the currentBatchInstanceIdentifier
	 */
	public static String getCurrentBatchInstanceIdentifier() {
		return currentBatchInstanceIdentifier;
	}

	public static String getBatchClassIdentifier() {
		return batchClassIdentifier;
	}

	/**
	 * @return the baseHttpURL
	 */
	public static String getBaseHttpURL() {
		return baseHttpURL;
	}

	public static String getAbsoluteURL(final String batchInstanceResourceName) {
		return baseHttpURL + "/" + batchInstanceResourceName;
	}

	public static void updateDocumentOnLoad(final Document documentoAdd) {
		updateDocumentOnLoad(documentoAdd, false);

	}

	public static void updateDocumentOnLoad(final Document documentToAdd, final boolean override) {
		if (null != documentToAdd) {
			loadedDocumentsMap = loadedDocumentsMap == null ? new HashMap<String, Document>() : loadedDocumentsMap;
			documentIdentifiersList = documentIdentifiersList == null ? new LinkedList<String>() : documentIdentifiersList;
			final String docIdentifier = documentToAdd.getIdentifier();
			if (!StringUtil.isNullOrEmpty(docIdentifier) && (override || loadedDocumentsMap.get(docIdentifier) == null)) {
				loadedDocumentsMap.put(docIdentifier, documentToAdd);
			}
		}
	}

	public static String getDisplayImageURL(final Document document, final String thumbnailImageURL) {
		String displayImageName = null;
		if (null != document && !StringUtil.isNullOrEmpty(thumbnailImageURL)) {
			final int index = thumbnailImageURL.lastIndexOf('/');
			if (index != -1) {
				final String thumbnailName = thumbnailImageURL.substring(index + 1);
				displayImageName = BatchSchemaUtil.getDisplayImage(thumbnailName, document);
			}
		}
		return getAbsoluteURL(displayImageName);
	}

	public static Column getSelectedColumn() {
		return selectedColumn;
	}

	public static Page getPage(final Document document, final String thumbnailImageURL) {
		Page bindedPage = null;
		if (null != document && !StringUtil.isNullOrEmpty(thumbnailImageURL)) {
			final int index = thumbnailImageURL.lastIndexOf('/');
			if (index != -1) {
				final String thumbnailName = thumbnailImageURL.substring(index + 1);
				bindedPage = BatchSchemaUtil.getPage(thumbnailName, document);
			}
		}
		return bindedPage;
	}

	public static void reorderPages(final Document document, final List<String> thumbnailURLList) {
		if (null != document && !CollectionUtil.isEmpty(thumbnailURLList)) {
			final Pages documentPages = document.getPages();
			if (null != documentPages) {
				final List<Page> documentPageList = documentPages.getPage();
				final List<Page> newDocumentPageList = new ArrayList<Page>();
				for (final String thumbnailURL : thumbnailURLList) {
					final Page traversedPage = getPage(document, thumbnailURL);
					if (null != traversedPage) {
						newDocumentPageList.add(traversedPage);
					}
				}
				documentPageList.clear();
				documentPageList.addAll(newDocumentPageList);
			}
		}
	}

	/**
	 * @return the batchInstanceName
	 */
	public static String getBatchInstanceName() {
		return batchInstanceName;
	}

	public static int getBeforeInsertionRowIndex() {
		return rowIndex;
	}

	public static void setBeforeInsertionScripRowIndex(int rowIndex) {
		ReviewValidateNavigator.rowIndex = rowIndex;
	}

	/**
	 * @return the documentTypeList
	 */
	public static List<String> getDocumentTypeList() {
		return documentTypeList;
	}

	/**
	 * @return the batchInstancePrority
	 */
	public static String getBatchInstancePrority() {
		return batchInstancePrority;
	}

	public static String getDefaultDocumentIdentifierToOpen() {
		String defaultDocumentIdentifier = null;
		if (!CollectionUtil.isEmpty(documentIdentifiersList)) {
			if (advanceDocumentOpenSwitch) {
				defaultDocumentIdentifier = getDefaultInvalidDocumentIdentifier();
			}
			defaultDocumentIdentifier = defaultDocumentIdentifier == null ? (currentDocument == null ? documentIdentifiersList.get(0)
					: currentDocument.getIdentifier()) : defaultDocumentIdentifier;
		}
		return defaultDocumentIdentifier;
	}

	private static String getDefaultInvalidDocumentIdentifier() {
		String defaultDocumentIdentifier = null;
		if (!CollectionUtil.isEmpty(documentMetadataMap)) {
			String currentDocumentIdentifier = null;
			if (currentDocument != null) {
				currentDocumentIdentifier = currentDocument.getIdentifier();
			}
			defaultDocumentIdentifier = getFirstInvalidDocument(currentDocumentIdentifier);
		}
		return defaultDocumentIdentifier;
	}

	private static String getFirstInvalidDocument(final String documentIdentifier) {
		String firstInvalidDocumentIdentifier = null;
		int documentIndex = getDocumentIndex(documentIdentifier);
		if (!CollectionUtil.isEmpty(documentIdentifiersList)) {
			final int totalDocuments = documentIdentifiersList.size();
			documentIndex = documentIndex == -1 ? 0 : documentIndex;
			DocumentMetaData traversedDocumentMetadata;
			for (int currentDocumentIndex = 0; currentDocumentIndex < totalDocuments; currentDocumentIndex++) {
				final String docIdentifier = documentIdentifiersList.get((documentIndex % totalDocuments));
				traversedDocumentMetadata = documentMetadataMap.get(docIdentifier);
				if (null != traversedDocumentMetadata && !traversedDocumentMetadata.isValid()) {
					firstInvalidDocumentIdentifier = docIdentifier;
					break;
				}
				documentIndex++;
			}
		}
		return firstInvalidDocumentIdentifier;
	}

	public static DataTableGrid getCurrentGrid() {
		return currentGrid;
	}

	public static void setCurrentPage(final Page currentPage) {
		ReviewValidateNavigator.currentPage = currentPage;
	}

	/**
	 * @return the currentPage
	 */
	public static Page getCurrentPage() {
		return currentPage;
	}

	public static boolean addDocument(final Document documentToAdd, final Document afterDocument) {
		boolean documentAdded = false;
		if (null != documentToAdd && null != afterDocument) {
			final int documentIndex = getDocumentIndex(afterDocument.getIdentifier());
			if (documentIndex != -1) {
				final String documentIdentifier = documentToAdd.getIdentifier();
				documentIdentifiersList.add(documentIndex + 1, documentIdentifier);
				loadedDocumentsMap.put(documentIdentifier, documentToAdd);
				final DocumentMetaData documentMetadata = new DocumentMetaData();
				documentMetadata.setValid(documentToAdd.isValid());
				documentMetadataMap.put(documentIdentifier, documentMetadata);
				documentAdded = true;
			}
		}
		return documentAdded;
	}

	public static int getDocumentIndex(final String documentIdentifier) {
		int docIndex = -1;
		boolean isFound = false;
		if (!CollectionUtil.isEmpty(documentIdentifiersList) && !StringUtil.isNullOrEmpty(documentIdentifier)) {
			for (final String currentIdentifier : documentIdentifiersList) {
				docIndex++;
				if (documentIdentifier.equalsIgnoreCase(currentIdentifier)) {
					isFound = true;
					break;
				}
			}
		}
		return isFound ? docIndex : -1;
	}

	public static void addAlteredDocument(final Document documentToAdd) {
		if (null != documentToAdd) {
			alteredDocumentMap.put(documentToAdd.getIdentifier(), documentToAdd);
		}
	}

	public static void resetAlteredDocuments() {
		alteredDocumentMap.clear();
	}

	/**
	 * @return the alteredDocumentMap
	 */
	public static Map<String, Document> getAlteredDocumentMap() {
		return alteredDocumentMap;
	}

	public static int getAlteredDocumentCount() {
		return alteredDocumentMap.size();
	}

	public static String getDocumentName(final String documentIdentifier) {
		String documentName = null;
		if (null != documentMetadataMap && !StringUtil.isNullOrEmpty(documentIdentifier)) {
			final DocumentMetaData documentMetadata = documentMetadataMap.get(documentIdentifier);
			documentName = documentMetadata == null ? null : documentMetadata.getDocumentName();
		}
		return documentName;
	}

	public static Document getLoadedDocumentByIdentifier(final String documentIdentifier) {
		Document loadedDocument = null;
		if (!StringUtil.isNullOrEmpty(documentIdentifier)) {
			loadedDocument = loadedDocumentsMap.get(documentIdentifier);
		}
		return loadedDocument;
	}

	public static void removeDocument(final String documentIdentifier) {
		alteredDocumentMap.remove(documentIdentifier);
		documentIdentifiersList.remove(documentIdentifier);
		documentMetadataMap.remove(documentIdentifier);
	}

	public static boolean isDocumentValid(final String documentIdentifier) {
		final DocumentMetaData docMetadata = documentMetadataMap.get(documentIdentifier);
		return docMetadata == null ? false : docMetadata.isValid();
	}

	public static boolean validateBatch(final Document updatedDocument) {
		if (null != updatedDocument) {
			final String updatedDocumentIdentifier = updatedDocument.getIdentifier();
			final boolean documentReviewed = updatedDocument.isReviewed();
			if (batchInstanceStatus == BatchInstanceStatus.READY_FOR_REVIEW) {
				validateDocument(updatedDocumentIdentifier, documentReviewed);
			}

			if (batchInstanceStatus == BatchInstanceStatus.READY_FOR_VALIDATION) {
				final boolean documentValidated = updatedDocument.isValid();
				validateDocument(updatedDocumentIdentifier, documentReviewed && documentValidated);
			}
		}
		return isBatchValid();
	}

	public static String getInsertionScriptTableName() {
		return insertionScriptTableName;
	}

	public static void setInsertionScriptTableName(String insertionScriptTableName) {
		ReviewValidateNavigator.insertionScriptTableName = insertionScriptTableName;
	}

	private static boolean isBatchValid() {
		boolean isValid = true;
		final Set<Entry<String, DocumentMetaData>> metaDataEntrySet = documentMetadataMap.entrySet();
		for (final Entry<String, DocumentMetaData> metadataEntry : metaDataEntrySet) {
			if (null != metadataEntry) {
				final DocumentMetaData documentMetadata = metadataEntry.getValue();
				if (null != documentMetadata && !documentMetadata.isValid()) {
					isValid = false;
					break;
				}
			}
		}
		return isValid;
	}

	public static void validateDocument(final String documentIdentifier, final boolean isValid) {
		if (!StringUtil.isNullOrEmpty(documentIdentifier)) {
			final DocumentMetaData documentMetadata = documentMetadataMap.get(documentIdentifier);
			if (null != documentMetadata) {
				documentMetadata.setValid(isValid);
			}
		}
	}

	public static boolean isValidDocumentType(final String documentType, final boolean isUnknownValid) {
		boolean isValid = false;
		if (!CollectionUtil.isEmpty(documentTypeList) && !StringUtil.isNullOrEmpty(documentType)) {
			for (final String validDocumentType : documentTypeList) {
				if (documentType.equalsIgnoreCase(validDocumentType)
						&& (isUnknownValid || !"Unknown".equalsIgnoreCase(validDocumentType))) {
					isValid = true;
					break;
				}
			}
		}
		return isValid;
	}

	public static boolean isValidDocumentType(final String documentType) {
		return isValidDocumentType(documentType, false);
	}

	public static boolean doReviewValidateSave(final boolean ignoreDefaultReviewSwitch) {
		boolean requiresSave = defaultSaveOperation;
		if (!requiresSave || ignoreDefaultReviewSwitch) {
			requiresSave = currentCounter == saveInterval;
			currentCounter = ((currentCounter) % saveInterval) + 1;
		}
		return requiresSave;
	}

	public static void addDocumentType(final DocumentTypeDTO documentTypeDTO) {
		if (null != documentTypeDTO) {
			documentTypeMap.put(documentTypeDTO.getName().toUpperCase(), documentTypeDTO);
		}
	}

	public static DocumentTypeDTO getDocumentTypeByName(final String name) {
		DocumentTypeDTO loadedDocType = null;
		if (null != documentTypeMap && !StringUtil.isNullOrEmpty(name)) {
			loadedDocType = documentTypeMap.get(name.toUpperCase());
		}
		return loadedDocType;
	}

	public static DocumentTypeDTO getDocumentTypeForDocument(final Document document) {
		DocumentTypeDTO loadedDocType = null;
		if (null != document) {
			loadedDocType = getDocumentTypeByName(document.getType());
		}
		return loadedDocType;
	}

	public static DocField getCurrentSelectedDocField() {
		return currentSelectedDocField;
	}

	public static void setCurrentSelectedDocField(final DocField currentSelectedDocField) {
		setCurrentSelectedDocField(currentSelectedDocField, true);
	}

	public static void setCurrentSelectedDocField(final DocField newSelectedDocField, final boolean fireEvent) {
		if (fireEvent && null != ReviewValidateNavigator.currentSelectedDocField && null != newSelectedDocField
				&& newSelectedDocField != currentSelectedDocField) {
			String currentSelectedFieldName = ReviewValidateNavigator.currentSelectedDocField.getName();
			if (!StringUtil.isNullOrEmpty(currentSelectedFieldName)
					&& !currentSelectedFieldName.equalsIgnoreCase(newSelectedDocField.getName())) {
				ReviewValidateEventBus.fireEvent(new FieldSelectionChangeEvent(ReviewValidateNavigator.currentSelectedDocField));
			}
		}
		ReviewValidateNavigator.currentField = newSelectedDocField;
		ReviewValidateNavigator.currentSelectedDocField = newSelectedDocField;
	}

	public static void setCurrentGrid(final DataTableGrid currentGrid) {
		ReviewValidateNavigator.currentGrid = currentGrid;
	}

	public static void setCurrentSelectedColumnField(final Column selectedColumn) {
		currentField = selectedColumn;
		ReviewValidateNavigator.selectedColumn = selectedColumn;
		ReviewValidateNavigator.lastSelectedWidget = null;
	}

	public static String getNextDocumentIdentifier(final String docIdentifier) {
		String nextDocumentIdentifier = null;
		if (!StringUtil.isNullOrEmpty(docIdentifier) && !CollectionUtil.isEmpty(documentIdentifiersList)) {
			final int index = documentIdentifiersList.indexOf(docIdentifier);
			final int totalElements = documentIdentifiersList.size();
			nextDocumentIdentifier = documentIdentifiersList.get((index + 1) % totalElements);
		}
		return nextDocumentIdentifier;
	}

	public static String getPreviousDocumentIdentifier(final String docIdentifier) {
		String nextDocumentIdentifier = null;
		if (!StringUtil.isNullOrEmpty(docIdentifier) && !CollectionUtil.isEmpty(documentIdentifiersList)) {
			int index = documentIdentifiersList.indexOf(docIdentifier);
			final int totalElements = documentIdentifiersList.size();
			index = index <= 0 ? totalElements : index;
			nextDocumentIdentifier = documentIdentifiersList.get((index - 1) % totalElements);
		}
		return nextDocumentIdentifier;
	}

	public static String getNextPageIdentifier(final Document document, final Page currentPage) {
		final List<Page> pageList = BatchSchemaUtil.getDocumentPageList(document);
		String pageIdentifier = null;
		if (!CollectionUtil.isEmpty(pageList) && null != currentPage) {
			final int pageIndex = pageList.indexOf(currentPage);
			final int totalPages = pageList.size();
			final Page nextPage = pageList.get((pageIndex + 1) % totalPages);
			pageIdentifier = nextPage == null ? null : nextPage.getIdentifier();
		}
		return pageIdentifier;
	}

	public static String getPreviousPageIdentifier(final Document document, final Page currentPage) {
		final List<Page> pageList = BatchSchemaUtil.getDocumentPageList(document);
		String pageIdentifier = null;
		if (!CollectionUtil.isEmpty(pageList) && null != currentPage) {
			int pageIndex = pageList.indexOf(currentPage);
			final int totalPages = pageList.size();
			pageIndex = pageIndex <= 0 ? totalPages : pageIndex;
			final Page previousPage = pageList.get((pageIndex - 1) % totalPages);
			pageIdentifier = previousPage == null ? null : previousPage.getIdentifier();
		}
		return pageIdentifier;
	}

	public static Validatable getLastSelectedWidget() {
		return lastSelectedWidget;
	}

	public static void setLastSelectedDLFWidget(final Validatable lastSelectedWidget) {
		ReviewValidateNavigator.lastSelectedWidget = lastSelectedWidget;
	}

	public static Field getCurrentField() {
		return currentField;
	}

	public static void addCoordinateToCurrentField(final PointCoordinate startCoordinate, final PointCoordinate endCoordinate,
			final boolean removePreviousSpans) {
		if (null != currentField && null != startCoordinate && null != endCoordinate) {
			final String pageIdentfier = currentField.getPage();
			final String currentPageIdentifier = currentPage == null ? null : currentPage.getIdentifier();
			if (null != currentPageIdentifier) {
				CoordinatesList coordinateList = currentField.getCoordinatesList();
				if (null == coordinateList) {
					coordinateList = new CoordinatesList();
					currentField.setCoordinatesList(coordinateList);
				}
				final List<Coordinates> listOfCoordinates = coordinateList.getCoordinates();
				if (removePreviousSpans || !currentPageIdentifier.equalsIgnoreCase(pageIdentfier)) {
					listOfCoordinates.clear();
				}
				currentField.setPage(currentPageIdentifier);
				final Coordinates coordinate = new Coordinates();
				coordinate.setX0(new BigInteger(startCoordinate.getxCoordinate().toString()));
				coordinate.setY0(new BigInteger(startCoordinate.getyCoordinate().toString()));
				coordinate.setX1(new BigInteger(endCoordinate.getxCoordinate().toString()));
				coordinate.setY1(new BigInteger(endCoordinate.getyCoordinate().toString()));
				listOfCoordinates.add(coordinate);
			}
		}
	}

	public static TableInfoDTO getTableByName(final DocumentTypeDTO documentTypeDTO, final String tableName) {
		TableInfoDTO tableInfo = null;
		if (null != documentTypeDTO && !StringUtil.isNullOrEmpty(tableName)) {
			tableInfo = documentTypeDTO.getTableInfoByName(tableName);
		}
		return tableInfo;
	}

	public static FieldTypeDTO getField(final DocField bindedField, final DocumentTypeDTO documentType) {
		FieldTypeDTO fieldType = null;
		if (null != documentType && null != bindedField) {
			final String fieldName = bindedField.getName();
			if (!StringUtil.isNullOrEmpty(fieldName)) {
				fieldType = documentType.getFieldTypeByName(fieldName);
			}
		}
		return fieldType;
	}

	public static DataTable getCurrentTable() {
		final DataTableGrid currentGrid = ReviewValidateNavigator.getCurrentGrid();
		DataTable table = null;
		if (null != currentGrid) {
			table = currentGrid.getBindedTable();
		}
		return table;
	}

	public static TableColumnInfoDTO getCurrentTableColumn(final Column column) {
		final DataTable table = getCurrentTable();
		TableColumnInfoDTO tableColumn = null;
		if (null != column && null != table) {
			final String tableName = table.getName();
			final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			final DocumentTypeDTO documentType = ReviewValidateNavigator.getDocumentTypeForDocument(currentDocument);
			final TableInfoDTO tableInfo = ReviewValidateNavigator.getTableByName(documentType, tableName);
			if (null != tableInfo) {
				tableColumn = tableInfo.getTableColumnInfoDTOByName(column.getName());
			}
		}
		return tableColumn;
	}

	public static String getThumbnailName(final String thumbnailImageURL) {
		String thumbnailName = null;
		if (!StringUtil.isNullOrEmpty(thumbnailImageURL)) {
			final int index = thumbnailImageURL.lastIndexOf('/');
			if (index != -1) {
				thumbnailName = thumbnailImageURL.substring(index + 1);
			}
		}
		return thumbnailName;
	}

	public static Document getDocument(final String thumbnailName) {
		Document documentToFind = null;
		if (!StringUtil.isNullOrEmpty(thumbnailName)) {
			final Set<Entry<String, Document>> entrySet = loadedDocumentsMap.entrySet();
			Document traversedDocument;
			Page page;
			for (final Entry<String, Document> mapEntry : entrySet) {
				traversedDocument = mapEntry.getValue();
				page = BatchSchemaUtil.getPage(thumbnailName, traversedDocument);
				if (null != page) {
					documentToFind = traversedDocument;
					break;
				}
			}
		}
		return documentToFind;
	}

	/**
	 * Gets the index fields with respect to the given document.
	 * 
	 * @param documentIdentifier {@link String} batch instance document identifier for which index fields are to be fetched.
	 * @return Returns {@link StickyDocumentData} for the passed batch document.
	 */
	public static StickyDocumentDataDTO getStickyDocumentData(final String documentIdentifier, final Document changedDocument) {
		StickyDocumentDataDTO stickyDocumentData = stickyDocumentMap.get(documentIdentifier);
		if (stickyDocumentData == null && null != changedDocument) {
			stickyDocumentData = new StickyDocumentDataDTO();
			DocumentLevelFields documentLevelFields = changedDocument.getDocumentLevelFields();
			if (null != documentLevelFields) {
				List<DocField> documentLevelFieldList = documentLevelFields.getDocumentLevelField();
				for (DocField field : documentLevelFieldList) {
					if (null != field) {
						String name = field.getName();
						if (null != name) {
							stickyDocumentData.addDocField(name.toUpperCase(), field);
						}
					}
				}
			}
			stickyDocumentMap.put(documentIdentifier, stickyDocumentData);
		}
		return stickyDocumentData;
	}

	public static void getStickyIndexFields(String identifier) {

	}

	public static int getZoomCount() {
		return zoomCount;
	}

	public static DragImage getCurrentSelectedThumbnail() {
		return currentSelectedThumbnail;
	}

	public static void setCurrentSelectedThumbnail(final DragImage currentSelectedThumbnail) {
		ReviewValidateNavigator.currentSelectedThumbnail = currentSelectedThumbnail;
	}

	public static int getTotalDocument() {
		return documentIdentifiersList == null ? 0 : documentIdentifiersList.size();
	}

	public static String getIndexFieldSeparator() {
		return indexFieldSeparator;
	}

	/**
	 * @return the isPageSelectionEnable
	 */
	public static boolean isPageSelectionEnable() {
		return isPageSelectionEnable;
	}

	/**
	 * @param isPageSelectionEnable the isPageSelectionEnable to set
	 */
	public static void setPageSelectionEnable(boolean isPageSelectionEnable) {
		ReviewValidateNavigator.isPageSelectionEnable = isPageSelectionEnable;
	}

	public static boolean isStickyIndexFieldEnable() {
		return isStickyIndexFieldEnable;
	}

	public static void setStickyIndexFieldEnable(boolean isStickyIndexFieldEnable) {
		ReviewValidateNavigator.isStickyIndexFieldEnable = isStickyIndexFieldEnable;
	}

	public static boolean isShowSuggestions() {
		return isShowSuggestions;
	}

	public static boolean isShowTablesSuggestions() {
		return isShowTablesSuggestions;
	}

}
