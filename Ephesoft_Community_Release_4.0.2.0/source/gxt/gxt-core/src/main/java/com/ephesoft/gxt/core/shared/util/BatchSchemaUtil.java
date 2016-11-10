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

package com.ephesoft.gxt.core.shared.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DataTable.Rows;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.DataTables;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HeaderRow;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.Row.Columns;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.core.client.constant.CategoryType;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.StickyDocumentDataDTO;
import com.ephesoft.gxt.core.shared.metadata.DocumentMetaData;

public final class BatchSchemaUtil {

	private static final String DOCUMENT = "DOC";
	private static final int RADIX_BASE = 10;

	private static int ROW_MERGING_FACTOR = 2;

	public static Map<String, DocumentMetaData> getDocumentMetaDataMap(final Batch batch, final BatchInstanceStatus status) {
		Map<String, DocumentMetaData> batchMetadataMap = null;
		if (null != batch) {
			final Documents documents = batch.getDocuments();
			if (null != documents) {
				final List<Document> documentList = documents.getDocument();
				if (!CollectionUtil.isEmpty(documentList)) {
					batchMetadataMap = new LinkedHashMap<String, DocumentMetaData>();
					DocumentMetaData metaData = null;
					for (final Document document : documentList) {
						if (null != document) {
							metaData = new DocumentMetaData();
							final boolean reviewedDocument = document.isReviewed();
							if (status == BatchInstanceStatus.READY_FOR_REVIEW) {
								metaData.setValid(reviewedDocument);
							} else {
								metaData.setValid(document.isReviewed() && document.isValid());
							}
							metaData.setDocumentName(document.getType());
							batchMetadataMap.put(document.getIdentifier(), metaData);
						}
					}
				}
			}
		}
		return batchMetadataMap;
	}

	public static List<Document> getDocumentList(final Batch batch) {
		List<Document> batchDocumentList = null;
		if (null != batch) {
			final Documents batchDocuments = batch.getDocuments();
			batchDocumentList = batchDocuments == null ? null : batchDocuments.getDocument();

		}
		return batchDocumentList;
	}

	public static Document getDocumentByIdentifier(final Batch batch, final String documentIdentifier) {
		Document requiredDocument = null;
		if (!StringUtil.isNullOrEmpty(documentIdentifier) && null != batch) {
			final List<Document> documentList = getDocumentList(batch);
			if (!CollectionUtil.isEmpty(documentList)) {
				for (final Document document : documentList) {
					if (null != document && documentIdentifier.equals(document.getIdentifier())) {
						requiredDocument = document;
						break;
					}
				}
			}
		}
		return requiredDocument;
	}

	public static List<String> getThumbnails(final Document document) {
		List<String> thumbnailNamesList = null;
		if (null != document) {
			final Pages documentPages = document.getPages();
			if (null != documentPages) {
				final List<Page> documentPageList = documentPages.getPage();
				thumbnailNamesList = new ArrayList<String>();
				for (final Page page : documentPageList) {
					if (null != page) {
						thumbnailNamesList.add(page.getThumbnailFileName());
					}
				}
			}
		}
		return thumbnailNamesList;
	}

	public static void reorderPages(final Document document, final List<String> thumbnailNamesList) {
		if (!CollectionUtil.isEmpty(thumbnailNamesList) && null != document) {
			final Map<String, Page> pageMap = getThumbnailPageMap(document);
			final List<Page> newThumbnailPageList = document.getPages().getPage();
			newThumbnailPageList.clear();
			Page thumbnailPage = null;
			for (final String thumbnailName : thumbnailNamesList) {
				thumbnailPage = pageMap.get(thumbnailName);
				if (thumbnailPage != null) {
					newThumbnailPageList.add(thumbnailPage);
				}
			}
		}
	}

	private static Map<String, Page> getThumbnailPageMap(final Document document) {
		Map<String, Page> pageMap = null;
		if (document != null) {
			final Pages documentPages = document.getPages();
			if (null != documentPages) {
				final List<Page> pageList = documentPages.getPage();
				if (!CollectionUtil.isEmpty(pageList)) {
					pageMap = new HashMap<String, Page>(pageList.size());
					for (final Page documentPage : pageList) {
						pageMap.put(documentPage.getThumbnailFileName(), documentPage);
					}
				}
			}
		}
		return pageMap;
	}

	public static String getDisplayImage(final String thumbnailImage, final Document document) {
		String displayFileName = null;
		final Page thumbnailPage = getPage(thumbnailImage, document);
		if (null != thumbnailPage) {
			displayFileName = thumbnailPage.getDisplayFileName();
		}
		return displayFileName;
	}

	public static Page getPage(final String thumbnailImage, final Document document) {
		Page thumbnailPage = null;
		final Map<String, Page> thumbnailPageMap = getThumbnailPageMap(document);
		if (null != thumbnailPageMap) {
			thumbnailPage = thumbnailPageMap.get(thumbnailImage);
		}
		return thumbnailPage;
	}

	public static void removePage(final Document document, final Page pageToRemove) {
		if (null != document && null != pageToRemove) {
			final Pages documentPages = document.getPages();
			if (null != documentPages) {
				final List<Page> pageList = documentPages.getPage();
				pageList.remove(pageToRemove);
			}
		}
	}

	public static void addPage(final Document document, final Page pageToAdd) {
		if (null != document && null != pageToAdd) {
			Pages pages = document.getPages();
			if (null == pages) {
				pages = new Pages();
				document.setPages(pages);
			}
			pages.getPage().add(pageToAdd);
		}
	}

	public static Document splitDocument(final Document docTypeToSplit, final String pageID, final boolean isDocTableFieldEnabled,
			final boolean isTableFieldEnabled, final List<String> documentIdentifiers) {
		Document newDocType = null;
		if (null != docTypeToSplit && !StringUtil.isNullOrEmpty(pageID) && !CollectionUtil.isEmpty(documentIdentifiers)) {
			final Integer pageIndex = getPageTypeIndex(docTypeToSplit, pageID);
			final Pages pages = docTypeToSplit.getPages();
			final List<Page> pageTypeList = pages.getPage();
			final DataTables docDataTables = docTypeToSplit.getDataTables();
			// List<DataTable> dataTables = docDataTables.getDataTable();
			// create new document.
			final String newDocID = getNewDocumentTypeID(documentIdentifiers);
			newDocType = new Document();
			newDocType.setIdentifier(DOCUMENT + newDocID);

			newDocType.setDescription(docTypeToSplit.getDescription());
			newDocType.setType(docTypeToSplit.getType());
			newDocType.setConfidence(docTypeToSplit.getConfidence());
			newDocType.setMultiPagePdfFile(docTypeToSplit.getMultiPagePdfFile());
			newDocType.setMultiPageTiffFile(docTypeToSplit.getMultiPageTiffFile());
			newDocType.setValid(docTypeToSplit.isValid());
			newDocType.setReviewed(docTypeToSplit.isReviewed());
			newDocType.setErrorMessage(CoreCommonConstant.EMPTY_STRING);
			newDocType.setDocumentDisplayInfo(CoreCommonConstant.EMPTY_STRING);
			// hack to hide Multiple RV nodes.
			// newDocType.setPreviousType(docType.getPreviousType());
			final Pages newPages = new Pages();
			final List<Page> listOfNewPages = newPages.getPage();
			final List<String> listOfNewPageIdentifiers = new ArrayList<String>();
			// create the new document for split pages.
			for (int index = pageIndex; index < pageTypeList.size(); index++) {
				final Page pgType = pageTypeList.get(index);
				listOfNewPages.add(pgType);
				listOfNewPageIdentifiers.add(pgType.getIdentifier());
			}
			newDocType.setPages(newPages);
			// remove the pages from the older document.
			for (int index = pageTypeList.size() - 1; index >= pageIndex; index--) {
				pageTypeList.remove(index);
			}
			// newDocType.setDataTables(docDataTables);
			if (isTableFieldEnabled) {
				// newDocType.setDataTables(docDataTables);
				final DataTables updatedDataTables = updateDataTables(docDataTables, listOfNewPageIdentifiers);
				// DataTables updatedDataTables = new DataTables();
				newDocType.setDataTables(updatedDataTables);
			} else {
				updateDataTables(docDataTables, listOfNewPageIdentifiers);
				newDocType.setDataTables(null);
			}
			final DocumentLevelFields documentLevelFields = docTypeToSplit.getDocumentLevelFields();
			if (documentLevelFields != null) {
				final List<DocField> docFields = documentLevelFields.getDocumentLevelField();
				final DocumentLevelFields documentLevelFieldsForNewDoc = new DocumentLevelFields();
				final List<DocField> docFieldsForNewDoc = documentLevelFieldsForNewDoc.getDocumentLevelField();
				if (docFields != null) {
					for (final DocField docField : docFields) {
						final DocField docFieldForNewDoc = new DocField();
						docFieldForNewDoc.setConfidence(0.0f);
						docFieldForNewDoc.setFieldOrderNumber(0);
						docFieldForNewDoc.setForceReview(false);
						docFieldForNewDoc.setOcrConfidence(0.0f);
						docFieldForNewDoc.setOcrConfidenceThreshold(0.0f);
						docFieldForNewDoc.setConfidence(docField.getConfidence());
						docFieldForNewDoc.setFieldOrderNumber(docField.getFieldOrderNumber());
						docFieldForNewDoc.setFieldValueOptionList(docField.getFieldValueOptionList());
						docFieldForNewDoc.setName(docField.getName());
						docFieldForNewDoc.setType(docField.getType());
						if (null != docField.getCategory()) {
							docFieldForNewDoc.setCategory(docField.getCategory());
						} else {
							// docFieldForNewDoc.setCategory("Uncategorised");
							docFieldForNewDoc.setCategory(CategoryType.GROUP_1.getCategoryName());
						}
						docFieldForNewDoc.setOcrConfidence(docField.getOcrConfidence());
						docFieldForNewDoc.setOcrConfidenceThreshold(docField.getOcrConfidenceThreshold());
						final float ocrConfidence = docFieldForNewDoc.getOcrConfidence();
						final float ocrConfidenceThreshold = docFieldForNewDoc.getOcrConfidenceThreshold() ;
						if (ocrConfidence < ocrConfidenceThreshold) {
							docFieldForNewDoc.setForceReview(true);
						}
						if (isDocTableFieldEnabled) {
							docFieldForNewDoc.setValue(docField.getValue());
						} else {
							docFieldForNewDoc.setValue(CoreCommonConstant.EMPTY_STRING);
						}
						final boolean docFieldChanged = docField.isFieldValueChangeScript() ;
						docFieldForNewDoc.setFieldValueChangeScript(docFieldChanged);
						if (listOfNewPageIdentifiers.contains(docField.getPage())) {
							// docFieldForNewDoc.setCoordinatesList(docField.getCoordinatesList());
							docFieldForNewDoc.setOverlayedImageFileName(docField.getOverlayedImageFileName());
							docFieldForNewDoc.setCoordinatesList(null);
							// docFieldForNewDoc.setPage(docField.getPage());
							docFieldForNewDoc.setPage(null);
							// docField.setPage(pageTypeList.get(0).getIdentifier());
							docField.setPage(null);
							docField.setCoordinatesList(null);
							docField.setOverlayedImageFileName(null);
						} else {
							docFieldForNewDoc.setCoordinatesList(null);
							docFieldForNewDoc.setOverlayedImageFileName(null);
							docFieldForNewDoc.setPage(null);
							// if (docField.getPage() != null &&
							// !docField.getPage().isEmpty()) {
							// docFieldForNewDoc.setPage(listOfNewPages.get(0).getIdentifier());
							// } else {
							// docFieldForNewDoc.setPage(docField.getPage());
							// }
						}
						final AlternateValues alternateValuesForNewDoc = new AlternateValues();
						final List<Field> fieldsForNewDoc = alternateValuesForNewDoc.getAlternateValue();
						final AlternateValues alternateValues = docField.getAlternateValues();
						if (alternateValues != null) {
							createAlternateValues(pageTypeList, listOfNewPages, listOfNewPageIdentifiers, fieldsForNewDoc,
									alternateValues);
						}
						docFieldForNewDoc.setAlternateValues(alternateValuesForNewDoc);
						docFieldsForNewDoc.add(docFieldForNewDoc);
					}
				}
				newDocType.setDocumentLevelFields(documentLevelFieldsForNewDoc);
			} else {
				newDocType.setDocumentLevelFields(null);
			}
		}
		return newDocType;
	}

	private static DataTables updateDataTables(final DataTables docDataTables, final List<String> listOfNewPageIdentifiers) {
		final DataTables updatedDatatables = new DataTables();
		final List<DataTable> updatedDataTableList = updatedDatatables.getDataTable();
		if (docDataTables != null && !docDataTables.getDataTable().isEmpty()) {
			for (final DataTable dataTable : docDataTables.getDataTable()) {
				final DataTable updatedDataTable = new DataTable();
				updatedDataTable.setName(dataTable.getName());
				final HeaderRow headerRow = dataTable.getHeaderRow();
				updatedDataTable.setHeaderRow(headerRow);
				final Rows rows = dataTable.getRows();
				final Rows updatedRows = new Rows();
				updatedDataTable.setRows(updatedRows);
				if (rows != null && !rows.getRow().isEmpty()) {
					updateRows(updatedRows, rows, listOfNewPageIdentifiers);
				}
				updatedDataTableList.add(updatedDataTable);
			}
		}
		return updatedDatatables;
	}

	private static void updateRows(final Rows updatedRows, final Rows rows, final List<String> listOfNewPageIdentifiers) {
		final List<Row> updatedRowList = updatedRows.getRow();
		final List<Row> deleteRowList = new ArrayList<Row>();
		// boolean isFirstColumn = true;
		// Field.CoordinatesList firstColumnCordinates = null;

		// Row status: 0 for both, 1 for first document, 2 for second document.
		int rowStatus = 0;
		for (final Row row : rows.getRow()) {
			rowStatus = 0;
			final Row updatedRow = new Row();
			updatedRow.setIsRuleValid(row.isIsRuleValid());
			updatedRow.setMannualExtraction(row.isIsRuleValid());
			final Columns columns = row.getColumns();
			if (columns != null && !columns.getColumn().isEmpty()) {
				final Columns updatedColumns = new Columns();
				updatedRow.setRowCoordinates(row.getRowCoordinates());
				updatedRow.setColumns(updatedColumns);
				for (final Column column : columns.getColumn()) {
					final String currentColumnPageId = column.getPage();
					if (!StringUtil.isNullOrEmpty(currentColumnPageId)) {
						if (listOfNewPageIdentifiers.contains(currentColumnPageId)) {
							rowStatus = 2;
						} else {
							rowStatus = 1;
						}
						break;
					}
				}
				for (final Column column : columns.getColumn()) {

					final Column updatedColumn = new Column();
					updatedColumn.setValid(false);
					updatedColumn.setValidationRequired(false);
					updatedColumn.setValid(false);
					updatedColumn.setValidationRequired(false);
					updatedColumn.setConfidence(0.0f);
					updatedColumn.setForceReview(false);
					updatedColumn.setOcrConfidence(0.0f);
					updatedColumn.setName(column.getName());
					updatedColumn.setOcrConfidenceThreshold(0.0f);
					final Float confidence = column.getConfidence();
					updatedColumn.setConfidence(confidence == null ? 0 : confidence);
					updatedColumn.setValid(column.isValid());
					updatedColumn.setFieldOrderNumber(column.getFieldOrderNumber());
					// if (isFirstColumn) {
					// firstColumnCordinates = column.getCoordinatesList();
					// isFirstColumn = false;
					// }
					final String pageId = column.getPage();
					updatedColumn.setValue(column.getValue());
					if (listOfNewPageIdentifiers.contains(pageId)) {
						updatedColumn.setPage(pageId);
						updatedColumn.setCoordinatesList(column.getCoordinatesList());
						column.setPage(null);
						column.setCoordinatesList(null);
					} else {
						updatedColumn.setPage(null);
						updatedColumn.setCoordinatesList(null);
					}
					final com.ephesoft.dcma.batch.schema.Column.AlternateValues alternateValues = updatedColumn.getAlternateValues();
					if (alternateValues != null && !alternateValues.getAlternateValue().isEmpty()) {
						final com.ephesoft.dcma.batch.schema.Column.AlternateValues updatedAlternateValues = new com.ephesoft.dcma.batch.schema.Column.AlternateValues();
						updatedColumn.setAlternateValues(updatedAlternateValues);
						for (final Field field : alternateValues.getAlternateValue()) {
							final Field updatedAlternateValue = new Field();
							updatedAlternateValue.setForceReview(Boolean.FALSE);
							updatedAlternateValue.setConfidence(field.getConfidence());
							updatedAlternateValue.setName(field.getName());
							final String fieldPageId = field.getPage();
							updatedAlternateValue.setValue(field.getValue());
							if (listOfNewPageIdentifiers.contains(fieldPageId)) {
								updatedAlternateValue.setPage(fieldPageId);
								updatedAlternateValue.setCoordinatesList(field.getCoordinatesList());
							} else {
								updatedAlternateValue.setPage(null);
								updatedAlternateValue.setCoordinatesList(null);
							}
							updatedAlternateValues.getAlternateValue().add(updatedAlternateValue);
						}
					}
					updatedColumns.getColumn().add(updatedColumn);
				}
			}
			if (rowStatus == 0) {
				updatedRowList.add(updatedRow);
			} else if (rowStatus == 2) {
				deleteRowList.add(row);
				updatedRowList.add(updatedRow);
			}
		}
		rows.getRow().removeAll(deleteRowList);
	}

	private static String getNewDocumentTypeID(final List<String> docTypesIDList) {
		Long maxDocumentTypeID = 1L;
		for (String docTypeID : docTypesIDList) {
			if (null != docTypeID) {
				docTypeID = docTypeID.replaceAll(DOCUMENT, CoreCommonConstant.EMPTY_STRING);
				final Long curPageIDLg = Long.parseLong(docTypeID, RADIX_BASE);
				if (curPageIDLg > maxDocumentTypeID) {
					maxDocumentTypeID = curPageIDLg;
				}
			}
		}
		final String maxDocStr = Long.toString(maxDocumentTypeID + 1L);
		return maxDocStr;
	}

	public static int getPageTypeIndex(final Document document, final String pageID) {
		int pageIndex = 0;
		if (null != document && !StringUtil.isNullOrEmpty(pageID)) {
			final Pages pages = document.getPages();
			final List<Page> pageTypeList = pages.getPage();
			for (final Page pageType : pageTypeList) {
				final String curPageID = pageType.getIdentifier();
				if (curPageID.equals(pageID)) {
					break;
				}
				pageIndex++;
			}
		}
		return pageIndex;
	}

	private static void createAlternateValues(final List<Page> pageTypeList, final List<Page> listOfNewPages,
			final List<String> listOfNewPageIdentifiers, final List<Field> fieldsForNewDoc, final AlternateValues alternateValues) {
		final List<Field> alternateValueFields = alternateValues.getAlternateValue();
		if (alternateValueFields != null) {
			for (final Field field : alternateValueFields) {
				final Field fieldForNewDoc = new Field();
				fieldForNewDoc.setForceReview(Boolean.FALSE);
				fieldForNewDoc.setConfidence(field.getConfidence());
				fieldForNewDoc.setFieldOrderNumber(field.getFieldOrderNumber());
				fieldForNewDoc.setFieldValueOptionList(field.getFieldValueOptionList());
				fieldForNewDoc.setName(field.getName());
				fieldForNewDoc.setType(field.getType());
				fieldForNewDoc.setValue(field.getValue());
				if (listOfNewPageIdentifiers.contains(field.getPage())) {
					fieldForNewDoc.setCoordinatesList(field.getCoordinatesList());
					fieldForNewDoc.setOverlayedImageFileName(field.getOverlayedImageFileName());
					fieldForNewDoc.setPage(field.getPage());
					field.setCoordinatesList(null);
					field.setOverlayedImageFileName(null);
					field.setPage(pageTypeList.get(0).getIdentifier());
				} else {
					fieldForNewDoc.setCoordinatesList(null);
					fieldForNewDoc.setOverlayedImageFileName(null);
					if (field.getPage() != null && !field.getPage().isEmpty()) {
						fieldForNewDoc.setPage(listOfNewPages.get(0).getIdentifier());
					} else {
						fieldForNewDoc.setPage(field.getPage());
					}
				}
				fieldsForNewDoc.add(fieldForNewDoc);
			}
		}
	}

	public static Map<String, Document> getDocuments(final Batch batch) {
		Map<String, Document> documentsMap = null;
		if (null != batch) {
			final Documents batchDocuments = batch.getDocuments();
			if (null != batchDocuments) {
				final List<Document> documentsList = batchDocuments.getDocument();
				documentsMap = new HashMap<String, Document>();
				for (final Document document : documentsList) {
					if (null != document) {
						documentsMap.put(document.getIdentifier(), document);
					}
				}
			}
		}
		return documentsMap;
	}

	public static int getPageCount(final Document document) {
		int pageCount = 0;
		if (null != document) {
			final Pages documentPages = document.getPages();
			if (null != documentPages) {
				pageCount = documentPages.getPage().size();
			}
		}
		return pageCount;
	}

	public static boolean removePage(final Document document, final String pageIdentifierToDelete) {
		boolean isDeleted = false;
		if (!StringUtil.isNullOrEmpty(pageIdentifierToDelete)) {
			final List<Page> documentPageList = getDocumentPageList(document);
			if (!CollectionUtil.isEmpty(documentPageList)) {
				final int totalElements = documentPageList.size();
				Page currentPage = null;
				for (int index = 0; index < totalElements; index++) {
					currentPage = documentPageList.get(index);
					if (currentPage != null && pageIdentifierToDelete.equalsIgnoreCase(currentPage.getIdentifier())) {
						documentPageList.remove(index);
						isDeleted = true;
						break;
					}
				}
			}
		}
		return isDeleted;
	}

	public static List<Page> getDocumentPageList(final Document document) {
		List<Page> documentPageList = null;
		if (document != null) {
			final Pages documentPages = document.getPages();
			if (null != documentPages) {
				documentPageList = documentPages.getPage();
			}
		}
		return documentPageList;
	}

	public static Map<String, List<DocField>> getDLFCategoryMap(final Document document) {
		Map<String, List<DocField>> categoryMap = null;
		if (null != document) {
			categoryMap = new HashMap<String, List<DocField>>();
			final DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
			if (null != documentLevelFields) {
				final List<DocField> docFieldList = documentLevelFields.getDocumentLevelField();
				for (final DocField documentField : docFieldList) {
					addCategory(documentField, categoryMap);
				}
			}
		}
		return categoryMap;
	}

	public static int getNonHiddenFieldsCount(final List<DocField> fieldsList) {
		int nonHiddenFields = 0;
		if (!CollectionUtil.isEmpty(fieldsList)) {
			for (DocField field : fieldsList) {
				if (null != field && !field.isHidden()) {
					nonHiddenFields++;
				}
			}
		}
		return nonHiddenFields;
	}

	public static void addCategory(final DocField documentField, final Map<String, List<DocField>> categoryMap) {
		if (null != documentField) {
			String dlfCategory = documentField.getCategory();
			dlfCategory = dlfCategory == null ? CategoryType.GROUP_1.getCategoryName()/* "Uncategorised" */: dlfCategory;
			List<DocField> categoryList = categoryMap.get(dlfCategory);
			if (null == categoryList) {
				categoryList = new LinkedList<DocField>();
				categoryMap.put(dlfCategory, categoryList);
			}
			categoryList.add(documentField);
		}
	}

	public static Page getPage(final Document document, final int index) {
		Page page = null;
		if (null != document && index >= 0) {
			final Pages documentPages = document.getPages();
			if (null != documentPages) {
				final List<Page> pageList = documentPages.getPage();
				final int totalPages = pageList.size();
				if (totalPages > 0 && totalPages > index) {
					page = pageList.get(index);
				}
			}
		}
		return page;
	}

	public static Column getColumn(final Row row, final String colName) {
		Column searchedColumn = null;
		if (null != row && !StringUtil.isNullOrEmpty(colName)) {
			final Columns tableColumns = row.getColumns();
			if (null != tableColumns) {
				final List<Column> columnsList = tableColumns.getColumn();
				for (final Column column : columnsList) {
					if (null != column && colName.equalsIgnoreCase(column.getName())) {
						searchedColumn = column;
						break;
					}
				}
			}
		}
		return searchedColumn;
	}

	public static List<Column> getHeaderColumn(final DataTable dataTable) {
		List<Column> columnList = null;
		if (null != dataTable) {
			final HeaderRow headerRow = dataTable.getHeaderRow();
			final com.ephesoft.dcma.batch.schema.HeaderRow.Columns headerRowColumns = headerRow.getColumns();
			columnList = headerRowColumns == null ? null : headerRowColumns.getColumn();
		}
		return columnList;
	}

	public static int getDataTableCount(final Document document) {
		int dataTablesCount = 0;
		if (null != document) {
			final DataTables dataTables = document.getDataTables();
			dataTablesCount = null == dataTables ? 0 : dataTables.getDataTable().size();
		}
		return dataTablesCount;
	}

	public static Map<String, String> getColumnValues(final Row row) {
		Map<String, String> columnValuesMap = null;
		if (null != row) {
			columnValuesMap = new HashMap<String, String>();
			final Columns columns = row.getColumns();
			if (null != columns) {
				final List<Column> columnList = columns.getColumn();
				for (final Column column : columnList) {
					if (null != column) {
						final String columnName = column.getName();
						if (!StringUtil.isNullOrEmpty(columnName)) {
							columnValuesMap.put(columnName.toUpperCase(), column.getValue());
						}
					}
				}
			}
		}
		return columnValuesMap;
	}

	public static Row getNearestRow(final List<Row> rowListToSearch, final Row rowToCompare) {
		Row nearestRow = null;
		int minimumDistance = Integer.MAX_VALUE;
		if (null != rowToCompare && !CollectionUtil.isEmpty(rowListToSearch)) {
			for (final Row currentRow : rowListToSearch) {
				final int verticalDistance = getVerticalDistance(rowToCompare, currentRow);
				if (verticalDistance != CoreCommonConstant.INVALID_ROW_DISTANCE && verticalDistance < minimumDistance) {
					minimumDistance = verticalDistance;
					nearestRow = currentRow;
				}
			}
		}
		return nearestRow;
	}

	public static boolean canMerge(final Row source, final Row target) {
		boolean canMerge = false;
		if (null != source && null != target) {
			final int verticalDistance = getVerticalDistance(source, target);
			final int sourceRowHeight = getRowHeight(source);
			final int targetRowHeight = getRowHeight(target);
			final int maximumHeight = Math.max(sourceRowHeight, targetRowHeight);
			canMerge = verticalDistance < ROW_MERGING_FACTOR * maximumHeight;
		}
		return canMerge;
	}

	public static int getRowHeight(final Row sourceRow) {
		int rowHeight = -1;
		if (null != sourceRow) {
			final Coordinates sourceRowCoordinates = sourceRow.getRowCoordinates();
			if (null != sourceRowCoordinates) {
				final BigInteger sourceRowTopCoordinate = sourceRowCoordinates.getY0();
				final BigInteger sourceRowBottomCoordinate = sourceRowCoordinates.getY1();
				if (null != sourceRowTopCoordinate && null != sourceRowBottomCoordinate) {
					rowHeight = Math.abs(sourceRowTopCoordinate.subtract(sourceRowBottomCoordinate).intValue());
				}
			}
		}
		return rowHeight;
	}

	private static String getPageIdentifier(final Row sourceRow) {
		String rowPageIdentifier = null;
		if (sourceRow != null) {
			final Columns columns = sourceRow.getColumns();
			if (null != columns) {
				final List<Column> columnList = columns.getColumn();
				for (final Column column : columnList) {
					if (null != column) {
						rowPageIdentifier = column.getPage();
						if (!StringUtil.isNullOrEmpty(rowPageIdentifier)) {
							break;
						}
					}
				}
			}
		}
		return rowPageIdentifier;
	}

	private static boolean isOnSamePage(final Row sourceRow, final Row targetRow) {
		final String sourceRowPageIdentifier = getPageIdentifier(sourceRow);
		final String targetRowPageIdentifier = getPageIdentifier(targetRow);
		return (!StringUtil.isNullOrEmpty(sourceRowPageIdentifier))
				&& sourceRowPageIdentifier.equalsIgnoreCase(targetRowPageIdentifier) ? true : false;
	}

	public static int getVerticalDistance(final Row sourceRow, final Row targetRow) {
		int distance = CoreCommonConstant.INVALID_ROW_DISTANCE;
		final boolean onSamePage = isOnSamePage(sourceRow, targetRow);
		if (null != sourceRow && null != targetRow && onSamePage) {
			final Coordinates sourceRowCoordinates = sourceRow.getRowCoordinates();
			final Coordinates targetRowCoordinates = targetRow.getRowCoordinates();
			if (null != sourceRowCoordinates && null != targetRowCoordinates) {
				final BigInteger sourceRowTopCoordinate = sourceRowCoordinates.getY0();
				final BigInteger sourceRowBottomCoordinate = sourceRowCoordinates.getY1();
				final BigInteger targetRowTopCoordinate = targetRowCoordinates.getY0();
				final BigInteger targetRowBottomCoordinate = targetRowCoordinates.getY1();
				if (null != sourceRowTopCoordinate && null != sourceRowBottomCoordinate && null != targetRowTopCoordinate
						&& null != targetRowBottomCoordinate) {
					final int sourceCenter = (sourceRowBottomCoordinate.add(sourceRowTopCoordinate).intValue()) / 2;
					final int targetCenter = (targetRowBottomCoordinate.add(targetRowTopCoordinate).intValue()) / 2;
					distance = Math.abs(sourceCenter - targetCenter);
				}
			}
		}
		return distance;
	}

	public static Field getAlternateValue(final DocField field, final String value) {
		Field alternateValue = null;
		if (null != field && !StringUtil.isNullOrEmpty(value)) {
			final AlternateValues alternateValues = field.getAlternateValues();
			if (null != alternateValues) {
				final List<Field> alternateValueList = alternateValues.getAlternateValue();
				for (final Field currentValue : alternateValueList) {
					if (null != currentValue && value.equals(currentValue.getValue())) {
						alternateValue = currentValue;
						break;
					}
				}
			}
		}
		return alternateValue;
	}

	public static DocField getDocFieldByName(final Document document, String name) {
		DocField namedDocField = null;
		if (!StringUtil.isNullOrEmpty(name) && null != document) {
			DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
			if (null != documentLevelFields) {
				List<DocField> documentLevelFieldList = documentLevelFields.getDocumentLevelField();
				for (final DocField docField : documentLevelFieldList) {
					if (null != docField && name.equalsIgnoreCase(docField.getName())) {
						namedDocField = docField;
						break;
					}
				}
			}
		}
		return namedDocField;
	}

	public static CoordinatesList cloneCordinates(CoordinatesList coordinateListToClone) {
		CoordinatesList clonedObject = null;
		if (null != coordinateListToClone) {
			clonedObject = new CoordinatesList();
			final List<Coordinates> newCoordinatesList = clonedObject.getCoordinates();
			final List<Coordinates> listOfCoordinates = coordinateListToClone.getCoordinates();
			Coordinates newCoordinate;
			for (final Coordinates coordinateValue : listOfCoordinates) {
				if (null != coordinateValue) {
					newCoordinate = new Coordinates();
					newCoordinate.setX0(coordinateValue.getX0());
					newCoordinate.setX1(coordinateValue.getX1());
					newCoordinate.setY0(coordinateValue.getY0());
					newCoordinate.setY1(coordinateValue.getY1());
					newCoordinatesList.add(newCoordinate);
				}
			}
		}
		return clonedObject;
	}

	public static Field getAlternateValue(final Column column, final String value) {
		final Field requiredField = null;
		if (null != column && !StringUtil.isNullOrEmpty(value)) {
			final com.ephesoft.dcma.batch.schema.Column.AlternateValues columnAlternateValues = column.getAlternateValues();
			if (null != columnAlternateValues) {
				final List<com.ephesoft.dcma.batch.schema.Field> alternateValuesList = columnAlternateValues.getAlternateValue();
				if (!CollectionUtil.isEmpty(alternateValuesList)) {
					for (com.ephesoft.dcma.batch.schema.Field field : alternateValuesList) {
						if (null != field && value.equals(field.getValue())) {
							field = requiredField;
							break;
						}
					}
				}
			}
		}
		return requiredField;
	}

	public static void updateIndexFields(final DocumentLevelFields documentLevelFields,
			final StickyDocumentDataDTO stickyDocumentDataDTO) {
		if (documentLevelFields != null) {
			final List<DocField> docLevelFieldList = documentLevelFields.getDocumentLevelField();
			if (!CollectionUtil.isEmpty(docLevelFieldList)) {
				DocField existingDocField;
				String docFieldName;
				final List<DocField> updatedDocFieldList = new ArrayList<DocField>();
				for (final DocField docField : docLevelFieldList) {
					docFieldName = docField.getName().toUpperCase();
					existingDocField = stickyDocumentDataDTO.getDocField(docFieldName);
					if (existingDocField == null) {
						stickyDocumentDataDTO.addDocField(docFieldName, docField);
						updatedDocFieldList.add(docField);
					} else {
						setExistingFieldProps(existingDocField, docField);
						updatedDocFieldList.add(existingDocField);
					}
				}
				docLevelFieldList.clear();
				docLevelFieldList.addAll(updatedDocFieldList);
			}
		}
	}

	private static void setExistingFieldProps(final DocField existingDocField, final DocField docField) {
		if (existingDocField != null && docField != null) {
			existingDocField.setType(docField.getType());
			existingDocField.setFieldValueChangeScript(docField.isFieldValueChangeScript());
			existingDocField.setScriptEnabled(docField.isScriptEnabled());
			existingDocField.setFieldOrderNumber(docField.getFieldOrderNumber());
			existingDocField.setReadOnly(docField.isReadOnly());
			existingDocField.setHidden(docField.isHidden());
			existingDocField.setCategory(docField.getCategory());
			existingDocField.setOcrConfidenceThreshold(docField.getOcrConfidenceThreshold());
		}
	}
}
