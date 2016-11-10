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

package com.ephesoft.gxt.rv.server;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DataTable.Rows;
import com.ephesoft.dcma.batch.schema.Direction;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.DataTables;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HeaderRow;
import com.ephesoft.dcma.batch.schema.HeaderRow.Columns;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.service.BatchInstancePluginPropertiesService;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.EphesoftUser;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ThreadPool;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.ManualStepHistoryInWorkflow;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.ManualStepHistoryService;
import com.ephesoft.dcma.da.service.TableInfoService;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.service.WorkflowService;
import com.ephesoft.dcma.workflows.service.engine.EngineService;
import com.ephesoft.gxt.core.server.BatchClassUtil;
import com.ephesoft.gxt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.gxt.core.server.cache.BatchCache;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.PointCoordinate;
import com.ephesoft.gxt.core.shared.exception.UIException;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.ReviewValidateService;
import com.ephesoft.gxt.rv.client.constant.ReviewValidateConstant;
import com.ephesoft.gxt.rv.client.constant.ValidateProperties;
import com.ephesoft.gxt.rv.client.util.ReviewValidateDataConveter;
import com.ephesoft.gxt.rv.shared.metadata.PluginPropertiesMetaData;
import com.ephesoft.gxt.rv.shared.metadata.ReviewValidateMetaData;

public class ReviewValidateServiceImpl extends DCMARemoteServiceServlet implements ReviewValidateService {

	private static final long serialVersionUID = 1L;
	private static final String PROPERTIES_FOLDER = "properties";
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(ReviewValidateServiceImpl.class);
	private static final String VALIDATE_DOCUMENT_PLUGIN = "VALIDATE_DOCUMENT";
	private static final String REVIEW_DOCUMENT_PLUGIN = "REVIEW_DOCUMENT";
	private static final String MODULE_ID_FOR_REVIEW = "5";

	private static final String MODULE_ID_FOR_VALIDATE = "6";

	@Override
	public ReviewValidateMetaData getReviewValidateMetaData(final String batchInstanceIdentifier) throws UIException {
		return getReviewValidateMetaData(batchInstanceIdentifier, true);
	}

	private ReviewValidateMetaData getReviewValidateMetaData(final String batchInstanceIdentifier, final boolean recordBatchTimings)
			throws UIException {
		LOGGER.trace(" Getting the review validate meta data.");
		ReviewValidateMetaData batchMetadata = null;
		Batch batch = null;
		if (!StringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
			batch = this.getBatch(batchInstanceIdentifier);
		} else {
			batch = this.getHighestPriortyBatch();
		}
		if (null != batch) {
			final String batchId = batch.getBatchInstanceIdentifier();
			LOGGER.info("Batch instance identifier is ", batchId);
			final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			final EphesoftUser ephesoftUser = EphesoftUser.NORMAL_USER;
			String userName = getUserName();
			final BatchInstance batchInstance = batchInstanceService.getBatchInstanceByUserRole(getUserRoles(), batchId, userName,
					ephesoftUser);
			if (null != batchInstance) {
				final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
				final PluginPropertiesService pluginPropertiesService = this.getBeanByName("batchInstancePluginPropertiesService",
						BatchInstancePluginPropertiesService.class);
				batchMetadata = ReviewValidateDataConveter.getBatchMetadata(batch, batchInstance, batchSchemaService,
						pluginPropertiesService);
				if (recordBatchTimings) {
					acquireLock(batchId);
					recordReviewOrValidateDuration(batchId, batchInstance.getStatus(), userName);
				}
			}
		}
		LOGGER.trace(" Meta data of the batch is returned.");
		return batchMetadata;
	}

	@Override
	public Document getDocument(final String batchInstanceIdentifier, final String docIndentifier) throws UIException {
		Document requiredDocument = null;
		if (!StringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
			final Batch batch = this.getBatch(batchInstanceIdentifier);
			if (null != batch) {
				requiredDocument = BatchSchemaUtil.getDocumentByIdentifier(batch, docIndentifier);
			}
		}
		return requiredDocument;
	}

	private Batch getBatch(final String batchInstanceIdentifier) throws UIException {
		LOGGER.info("Getting the batch.");
		Batch batch = BatchCache.get(batchInstanceIdentifier);
		if (null == batch) {
			LOGGER.info("Batch is not available in the cache");
			final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			batch = batchSchemaService.getBatchFromXML(batchInstanceIdentifier, true);
			acquireLock(batchInstanceIdentifier);
			BatchCache.put(batch);
		}
		return batch;
	}

	@Override
	public void signalWorkflow(final String batchInstanceIdentifier, final Map<String, Document> alteredDocumentsMap,
			final List<String> documentIdentifierList) throws UIException {
		if (!StringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
			mergeAndUpdateBatch(batchInstanceIdentifier, alteredDocumentsMap, documentIdentifierList, false, true);
			WorkflowService workflowService = this.getSingleBeanOfType(WorkflowService.class);
			try {
				invalidate(batchInstanceIdentifier);
				workflowService.signalWorkflow(batchInstanceIdentifier, getUserName());
				updateEndTimeAndCalculateDuration(batchInstanceIdentifier);
			} catch (DCMAApplicationException dcmaApplicationException) {
				LOGGER.error("The batch:", batchInstanceIdentifier, "could not be signalled from Review/Validate.",
						dcmaApplicationException);
				BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
				workflowService.handleErrorBatch(batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier),
						dcmaApplicationException, dcmaApplicationException.getMessage());
			}
		}
	}

	@Override
	public void saveBatch(final String batchInstanceIdentifier, final Map<String, Document> alteredDocumentsMap,
			final List<String> documentIdentifierList) throws UIException {
		this.mergeAndUpdateBatch(batchInstanceIdentifier, alteredDocumentsMap, documentIdentifierList, true, true);
	}

	private void mergeAndUpdateBatch(final String batchInstanceIdentifier, final Map<String, Document> alteredDocumentsMap,
			final List<String> documentIdentifierList, final boolean isAsync, final boolean doUpdateAtDAO) throws UIException {
		LOGGER.info("Merging and updating the batch");
		if (!StringUtil.isNullOrEmpty(batchInstanceIdentifier) && !CollectionUtil.isEmpty(alteredDocumentsMap)
				&& !CollectionUtil.isEmpty(documentIdentifierList)) {
			final Batch batch = this.getBatch(batchInstanceIdentifier);
			if (null != batch) {
				final Map<String, Document> batchDocumentsMap = BatchSchemaUtil.getDocuments(batch);
				if (!CollectionUtil.isEmpty(batchDocumentsMap)) {
					final List<Document> documentToSaveList = batch.getDocuments().getDocument();
					documentToSaveList.clear();
					for (final String documentIdentifier : documentIdentifierList) {
						if (!StringUtil.isNullOrEmpty(documentIdentifier)) {
							final Document documentToSave = getDocumentToSave(alteredDocumentsMap, batchDocumentsMap,
									documentIdentifier);
							documentToSaveList.add(documentToSave);
						}
					}
					if (doUpdateAtDAO) {
						this.updateBatch(batch, isAsync);
					}
				}
			}
		}
	}

	public Document getDocumentToSave(final Map<String, Document> alteredDocumentMap, final Map<String, Document> lastSavedDocument,
			final String documentIdentifier) {
		Document documentToSave = null;
		documentToSave = alteredDocumentMap.get(documentIdentifier);
		if (null == documentToSave) {
			documentToSave = lastSavedDocument.get(documentIdentifier);
		}
		return documentToSave;
	}

	private void updateBatch(final Batch batchToSave, final boolean isAsync) {
		if (null != batchToSave) {
			if (isAsync) {
				final Thread saveThread = new Thread(new Runnable() {

					@Override
					public void run() {
						accquireLockAndSaveBatch(batchToSave);
					}
				});
				saveThread.start();
			} else {
				accquireLockAndSaveBatch(batchToSave);
			}
		}
	}

	private void accquireLockAndSaveBatch(final Batch batchToSave) {
		final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		synchronized (batchToSave) {
			batchSchemaService.updateBatchXML(batchToSave);
		}
	}

	public void duplicatePage(final String batchInstanceIdentifier, final Map<String, Document> alteredDocumentsMap,
			final List<String> documentIdentifierList, final String documentIdentifier, final String duplicatePageIdentifier)
			throws UIException {
		if (!StringUtil.isNullOrEmpty(documentIdentifier) && !StringUtil.isNullOrEmpty(duplicatePageIdentifier)) {
			this.mergeAndUpdateBatch(batchInstanceIdentifier, alteredDocumentsMap, documentIdentifierList, false, true);
			this.duplicatePage(batchInstanceIdentifier, documentIdentifier, duplicatePageIdentifier);
			this.invalidate(batchInstanceIdentifier);
		}
	}

	private void duplicatePage(final String batchInstanceIdentifier, final String documentIdentifierIdentifier,
			final String duplicatePageIdentifier) throws UIException {
		BatchSchemaService batchSchemaService = null;
		try {
			batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			batchSchemaService.duplicatePageOfDocument(batchInstanceIdentifier, documentIdentifierIdentifier, duplicatePageIdentifier);
			BatchCache.put(this.getBatch(batchInstanceIdentifier));
		} catch (final DCMAApplicationException dcmaException) {
			LOGGER.error("Could not duplicate the page ", dcmaException);
			throw new UIException("Error in creating duplicate pages of the  document", dcmaException);
		}
	}

	private BatchInstance getBatchInstance(final String batchInstanceIdentifier) {
		BatchInstance batchInstance = null;
		if (!StringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
			final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
		}
		return batchInstance;
	}

	private ReviewValidateMetaData getBatchMetadata(final String batchInstanceIdentifier) throws UIException {
		ReviewValidateMetaData metaData = null;
		if (!StringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
			final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			final PluginPropertiesService pluginPropertiesService = this.getBeanByName("batchInstancePluginPropertiesService",
					BatchInstancePluginPropertiesService.class);
			metaData = ReviewValidateDataConveter.getBatchMetadata(this.getBatch(batchInstanceIdentifier),
					this.getBatchInstance(batchInstanceIdentifier), batchSchemaService, pluginPropertiesService);
		}
		return metaData;
	}

	public void recordReviewOrValidateDuration(final String batchInstanceId, final BatchInstanceStatus batchInstanceStatus,
			final String userName) {
		final Thread recordingThread = new Thread() {

			@Override
			public void run() {
				LOGGER.info("Recording the time for report generation");
				if (batchInstanceStatus == BatchInstanceStatus.READY_FOR_REVIEW
						|| batchInstanceStatus == BatchInstanceStatus.READY_FOR_VALIDATION) {
					final ManualStepHistoryInWorkflow manualStepHistoryInWorkflow = new ManualStepHistoryInWorkflow();
					manualStepHistoryInWorkflow.setUserName(userName);
					manualStepHistoryInWorkflow.setStartTime(new Date());
					manualStepHistoryInWorkflow.setEndTime(new Date(0L));
					manualStepHistoryInWorkflow.setBatchInstanceId(batchInstanceId);
					manualStepHistoryInWorkflow.setBatchInstanceStatus(batchInstanceStatus.name());
					final ManualStepHistoryService manualStepHistoryService = ReviewValidateServiceImpl.this
							.getSingleBeanOfType(ManualStepHistoryService.class);
					manualStepHistoryService.updateManualStepHistory(manualStepHistoryInWorkflow);
					LOGGER.trace("Time is recorded for reports.");
				}
			}
		};
		recordingThread.start();
	}

	public Batch getHighestPriortyBatch() throws UIException {
		Batch batch = null;
		final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		final EphesoftUser ephesoftUser = EphesoftUser.NORMAL_USER;
		final BatchInstance batchInstance = batchInstanceService.getHighestPriorityBatchInstance(getUserRoles(), ephesoftUser);
		if (batchInstance != null) {
			final String batchInstanceIdentifier = batchInstance.getIdentifier();
			batch = getBatch(batchInstanceIdentifier);
		}
		return batch;
	}

	@Override
	public DocumentTypeDTO getDocumentType(final String batchInstanceIdentifier, final String documentTypeName) {
		DocumentTypeDTO documentTypeDTO = null;
		if (!StringUtil.isNullOrEmpty(batchInstanceIdentifier) && !StringUtil.isNullOrEmpty(documentTypeName)) {
			final BatchClass loadedBatchClass = this.getBatchClassForBatchInstance(batchInstanceIdentifier);
			if (null != loadedBatchClass) {
				final DocumentType documentType = loadedBatchClass.getDocumentTypeByName(documentTypeName);
				if (null != documentType) {
					documentTypeDTO = BatchClassUtil.createDocumentTypeDTO(null, documentType);
				}
			}
		}
		return documentTypeDTO;
	}

	private BatchClass getBatchClassForBatchInstance(final String batchInstanceIdentifier) {
		BatchClass batchClass = null;
		if (!StringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
			final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			final BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
			if (null != batchInstance) {
				batchClass = batchInstance.getBatchClass();
			}
		}
		return batchClass;
	}

	@Override
	public List<Span> getHOCRContent(final PointCoordinate pointCoordinate1, final PointCoordinate pointCoordinate2,
			final String batchInstanceIdentifier, final String hocrFileName, final boolean rectangularCoordinateSet) {
		List<Span> spanSelectedList = null;
		boolean valid = true;
		if (batchInstanceIdentifier == null) {
			valid = false;
		}
		if (hocrFileName == null) {
			valid = false;
		}
		if (valid) {
			final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			final HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier, hocrFileName);
			if (hocrPages != null && hocrPages.getHocrPage() != null) {
				final List<HocrPage> hocrPageList = hocrPages.getHocrPage();
				if (!hocrPageList.isEmpty()) {
					final HocrPage hocrPage = hocrPageList.get(0);
					List<Span> spanList = new ArrayList<Span>();
					if (hocrPage.getSpans() != null) {
						spanList = hocrPage.getSpans().getSpan();
					}
					Integer firstSpanIndex = null;
					Integer lastSpanIndex = null;

					final Integer x0Coordinate = pointCoordinate1.getxCoordinate();
					final Integer y0Coordinate = pointCoordinate1.getyCoordinate();
					final Integer x1Coordinate = pointCoordinate2.getxCoordinate();
					final Integer y1Coordinate = pointCoordinate2.getyCoordinate();
					final List<Span> spanSortedList = getSortedList(spanList);
					if (!rectangularCoordinateSet) {

						int counter = 0;

						for (final Span span : spanSortedList) {
							final long spanX0 = span.getCoordinates().getX0().longValue();
							final long spanY0 = span.getCoordinates().getY0().longValue();
							final long spanX1 = span.getCoordinates().getX1().longValue();
							final long spanY1 = span.getCoordinates().getY1().longValue();
							if (spanX0 < x0Coordinate && spanX1 > x0Coordinate && spanY0 < y0Coordinate && spanY1 > y0Coordinate) {
								firstSpanIndex = counter;
							}
							if (spanX0 < x1Coordinate && spanX1 > x1Coordinate && spanY0 < y1Coordinate && spanY1 > y1Coordinate) {
								lastSpanIndex = counter;
							}
							if (firstSpanIndex != null && lastSpanIndex != null) {
								break;
							}
							counter++;
						}
						if (firstSpanIndex != null && lastSpanIndex != null) {
							counter = 0;
							for (final Span span : spanSortedList) {
								if ((counter >= firstSpanIndex && counter <= lastSpanIndex)
										|| (counter <= firstSpanIndex && counter >= lastSpanIndex)) {
									if (spanSelectedList == null) {
										spanSelectedList = new ArrayList<Span>();
									}
									spanSelectedList.add(span);
								}
								counter++;
							}
						}
					} else {
						boolean isValidSpan = false;
						final int defaultvalue = 20;
						int counter = 0;
						final StringBuffer valueStringBuffer = new StringBuffer();
						long currentYCoor = spanSortedList.get(0).getCoordinates().getY1().longValue();
						for (final Span span : spanSortedList) {
							isValidSpan = false;
							final long spanX0 = span.getCoordinates().getX0().longValue();
							final long spanY0 = span.getCoordinates().getY0().longValue();
							final long spanX1 = span.getCoordinates().getX1().longValue();
							final long spanY1 = span.getCoordinates().getY1().longValue();
							if ((spanY1 - currentYCoor) > defaultvalue) {
								currentYCoor = spanY1;
								if (spanSelectedList != null && spanSelectedList.size() > 0) {
									break;
								}
							}
							if (((spanX1 >= x0Coordinate && spanX1 <= x1Coordinate) || (spanX0 >= x0Coordinate && spanX0 <= x1Coordinate))
									&& ((spanY1 <= y1Coordinate && spanY1 >= y0Coordinate) || (spanY0 <= y1Coordinate && spanY0 >= y0Coordinate))) {
								isValidSpan = true;
							} else if (((x0Coordinate <= spanX0 && x1Coordinate >= spanX0) || (x0Coordinate >= spanX1 && x1Coordinate <= spanX1))
									&& ((y0Coordinate >= spanY0 && y0Coordinate <= spanY1) || (y1Coordinate >= spanY0 && y1Coordinate <= spanY1))
									|| ((y0Coordinate <= spanY0 && y1Coordinate >= spanY0) || (y0Coordinate >= spanY1 && y1Coordinate <= spanY1))
									&& ((x0Coordinate >= spanX0 && x0Coordinate <= spanX1) || (x1Coordinate >= spanX0 && x1Coordinate <= spanX1))) {
								isValidSpan = true;
							} else {
								if (((x0Coordinate > spanX0 && x0Coordinate < spanX1) || (x1Coordinate > spanX0 && x1Coordinate < spanX1))
										&& ((y0Coordinate > spanY0 && y0Coordinate < spanY1) || (y1Coordinate > spanY0 && y1Coordinate < spanY1))) {
									isValidSpan = true;
								}
							}
							if (isValidSpan) {
								if (counter != 0) {
									valueStringBuffer.append(' ');
								}
								valueStringBuffer.append(span.getValue());
								counter++;
							}

						}
						if (spanSelectedList == null) {
							spanSelectedList = new ArrayList<Span>();
						}
						final Span span = new Span();
						final Coordinates coordinates = new Coordinates();
						coordinates.setX0(BigInteger.valueOf(x0Coordinate));
						coordinates.setX1(BigInteger.valueOf(x1Coordinate));
						coordinates.setY0(BigInteger.valueOf(y0Coordinate));
						coordinates.setY1(BigInteger.valueOf(y1Coordinate));
						span.setCoordinates(coordinates);
						span.setValue(valueStringBuffer.toString());
						spanSelectedList.add(span);

					}
				}
			}
		}

		return spanSelectedList;
	}

	private List<Span> getSortedList(final List<Span> spanList) {
		final Set<Span> set = new TreeSet<Span>(new Comparator<Span>() {

			public int compare(final Span firstSpan, final Span secSpan) {
				BigInteger s1Y0 = firstSpan.getCoordinates().getY0();
				BigInteger s1Y1 = firstSpan.getCoordinates().getY1();
				final BigInteger s2Y0 = secSpan.getCoordinates().getY0();
				final BigInteger s2Y1 = secSpan.getCoordinates().getY1();
				final int halfOfSecSpan = (s2Y1.intValue() - s2Y0.intValue()) / 2;
				final int y1 = s2Y1.intValue() + halfOfSecSpan;
				final int y0 = s2Y0.intValue() - halfOfSecSpan;

				// following if else code is to handle abnormal(out of synch) value y0 or y1 coordinate of new span.
				if (isApproxEqual(s1Y0.intValue(), s2Y0.intValue()) && s1Y1.intValue() > y1) {
					s1Y1 = BigInteger.valueOf(y1);
					firstSpan.getCoordinates().setY1(s1Y1);
				} else if (isApproxEqual(s1Y1.intValue(), s2Y1.intValue()) && s1Y0.intValue() < y0) {
					s1Y0 = BigInteger.valueOf(y0);
					firstSpan.getCoordinates().setY0(s1Y0);
				}
				final BigInteger s1Y = s1Y1.add(s1Y0);
				final BigInteger s2Y = s2Y1.add(s2Y0);

				// calculating middle of old span.
				final int oldSpanMid = s2Y.intValue() / 2;
				int returnValue = 0;

				// if old span's y coordinate's middle lies within range of new span's y coordinates or not. if true, the two spans
				// belong to same line compare them further on their x coordinates, else they belong to two different lines.
				if (oldSpanMid >= s1Y0.intValue() && oldSpanMid <= s1Y1.intValue()) {
					final BigInteger s1X1 = firstSpan.getCoordinates().getX1();
					final BigInteger s2X1 = secSpan.getCoordinates().getX1();
					returnValue = s1X1.compareTo(s2X1);
				} else {
					returnValue = s1Y.compareTo(s2Y);
				}
				return returnValue;
			}
		});
		set.addAll(spanList);
		final List<Span> spanSortedList = new LinkedList<Span>();
		spanSortedList.addAll(set);

		// TODO add the clear method to remove all elements of set since it not
		// required after adding it to linked list.
		// set.clear();

		return spanSortedList;

	}

	/**
	 * Checks if two coordinates are nearly equal.
	 * 
	 * @param first int
	 * @param second int
	 * @return boolean
	 */
	private boolean isApproxEqual(final int first, final int second) {
		boolean result;
		int compare = first - second;
		if (compare < 0) {
			compare = -compare;
		}
		if (compare <= 5) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public List<Row> getTableData(final Map<Integer, Coordinates> columnVsCoordinates, final Document selectedDocument,
			final DataTable selectedDataTable, final String batchClassIdentifier, final String batchInstanceIdentifier,
			final String pageID, final String hocrFileName) {
		boolean valid = true;
		if (batchInstanceIdentifier == null) {
			valid = false;
		}
		if (valid && pageID == null) {
			valid = false;
		}

		final List<Row> rowList = new LinkedList<Row>();
		final List<Column> columnList = selectedDataTable.getHeaderRow().getColumns().getColumn();
		final String tableName = selectedDataTable.getName();
		if (valid) {
			boolean endPatternFound = false;
			final String tableEndPattern = getTableEndPattern(selectedDocument.getType(), tableName, batchClassIdentifier);
			final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			final Integer tableStartYCoordinate = getTableStartYCoordinate(columnVsCoordinates);
			endPatternFound = getTableRows(tableStartYCoordinate, batchSchemaService, batchInstanceIdentifier, tableEndPattern,
					columnVsCoordinates, columnList, pageID, rowList, hocrFileName);

			// following code supports multipage manual table extraction
			List<Page> pageList = null;
			if (!endPatternFound && selectedDocument.getPages() != null) {
				pageList = selectedDocument.getPages().getPage();
			}
			boolean currentPageFound = false;

			// iterate through all pages to fetch table data till end pattern not found.
			if (pageList != null) {
				for (final Page page : pageList) {
					if (endPatternFound) {
						break;
					}
					final String pageIdentifier = page.getIdentifier();
					if (!currentPageFound && pageIdentifier.equals(pageID)) {
						currentPageFound = true;
						continue;
					}
					if (!currentPageFound) {
						continue;
					}
					endPatternFound = getTableRows(null, batchSchemaService, batchInstanceIdentifier, tableEndPattern,
							columnVsCoordinates, columnList, pageIdentifier, rowList, page.getHocrFileName());
				}
			}
		}
		return rowList;
	}

	private String getTableEndPattern(final String documentTypeName, final String tableName, final String batchClassIdentifier) {
		String tableEndPattern = null;
		final TableInfoService tableInfoService = this.getSingleBeanOfType(TableInfoService.class);
		final List<TableInfo> tableInfoList = tableInfoService.getTableInfoByDocTypeName(documentTypeName, batchClassIdentifier);

		if (null != tableInfoList && !tableInfoList.isEmpty()) {
			for (final TableInfo tableInfo : tableInfoList) {
				if (null != tableInfo && tableInfo.getName().equalsIgnoreCase(tableName)) {
					tableEndPattern = tableInfo.getEndPattern();
					break;
				}
			}
		}
		return tableEndPattern;
	}

	private boolean getTableRows(final Integer tableStartYCoordinate, final BatchSchemaService batchSchemaService,
			final String batchInstanceIdentifier, final String tableEndPattern, final Map<Integer, Coordinates> columnVsCoordinates,
			final List<Column> columnList, final String pageID, final List<Row> rowList, final String hocrFileName) {
		boolean endPatternFound = false;
		List<Span> spanSortedList = null;
		final HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier, hocrFileName);
		if (hocrPages != null && hocrPages.getHocrPage() != null) {
			final List<HocrPage> hocrPageList = hocrPages.getHocrPage();
			if (!hocrPageList.isEmpty()) {
				final HocrPage hocrPage = hocrPageList.get(0);
				List<Span> spanList = new ArrayList<Span>();
				if (hocrPage.getSpans() != null) {
					spanList = hocrPage.getSpans().getSpan();
				}
				spanSortedList = getSortedList(spanList);
				endPatternFound = addColumnData(tableStartYCoordinate, spanSortedList, columnList, columnVsCoordinates,
						tableEndPattern, pageID, rowList);
			}
		}
		return endPatternFound;
	}

	private boolean addColumnData(final Integer tableStartYCoordinate, final List<Span> spanSortedList, final List<Column> columnList,
			final Map<Integer, Coordinates> columnVsCoordinates, final String tableEndPattern, final String pageID,
			final List<Row> rowList) {

		int counter = 1;
		long currentY0Coor = 0;
		long currentY1Coor = 0;

		final StringBuilder[] colValue = new StringBuilder[columnList.size()];
		setEmptyString(colValue);

		Coordinates rowCoordinates = new Coordinates();
		Row row = null;
		List<Column> tableRowColumns = null;
		boolean isValidSpan = false;
		Pattern pattern = null;
		Matcher matcher = null;

		if (tableEndPattern != null) {
			pattern = Pattern.compile(tableEndPattern);
		}
		boolean endPatternFound = false;
		for (final Span span : spanSortedList) {
			final String value = span.getValue();
			if (value == null || value.isEmpty()) {
				continue;
			}
			final long spanX0Coor = span.getCoordinates().getX0().longValue();
			final long spanY0Coor = span.getCoordinates().getY0().longValue();
			final long spanX1Coor = span.getCoordinates().getX1().longValue();
			final long spanY1Coor = span.getCoordinates().getY1().longValue();
			if (pattern != null) {
				matcher = pattern.matcher(value);
			}
			if (tableStartYCoordinate != null && spanY0Coor < tableStartYCoordinate && spanY1Coor < tableStartYCoordinate) {
				if (matcher != null && matcher.find()) {
					break;
				}
				continue;
			}
			if (counter == 1) {
				row = createNewRow(columnList);
				tableRowColumns = row.getColumns().getColumn();
				currentY0Coor = spanY0Coor;
				currentY1Coor = spanY1Coor;
				rowCoordinates.setX0(span.getCoordinates().getX0());
				rowCoordinates.setY0(span.getCoordinates().getY0());
				counter++;
			}

			// Algorithm for comparison: if old span's y coordinate's mid lies within range of new span's y coordinates or not.
			final long oldSpanMid = (currentY0Coor + currentY1Coor) / 2;
			if (!(oldSpanMid >= spanY0Coor && oldSpanMid <= spanY1Coor)) {
				isValidSpan = false;
				for (int i = 0; i < colValue.length; i++) {
					if (colValue[i] != null && !colValue[i].toString().trim().isEmpty()) {
						isValidSpan = true;
						break;
					}
				}
				if (isValidSpan) {
					for (final Integer colNo : columnVsCoordinates.keySet()) {
						tableRowColumns.get(colNo).setValue(colValue[colNo].toString().trim());
						tableRowColumns.get(colNo).setPage(pageID);
					}
					row.setRowCoordinates(rowCoordinates);
					rowList.add(row);
					row = createNewRow(columnList);
					tableRowColumns = row.getColumns().getColumn();
				}
				setEmptyString(colValue);
				currentY0Coor = spanY0Coor;
				currentY1Coor = spanY1Coor;
				rowCoordinates = new Coordinates();
				rowCoordinates.setX0(span.getCoordinates().getX0());
				rowCoordinates.setY0(span.getCoordinates().getY0());
			}

			rowCoordinates.setX1(span.getCoordinates().getX1());
			rowCoordinates.setY1(span.getCoordinates().getY1());

			for (final Integer colNo : columnVsCoordinates.keySet()) {
				final Coordinates coor = columnVsCoordinates.get(colNo);
				final long coorX0 = coor.getX0().longValue();
				final long coorX1 = coor.getX1().longValue();
				if ((spanX0Coor > coorX0 && spanX0Coor < coorX1) || (spanX1Coor > coorX0 && spanX1Coor < coorX1)
						|| (spanX0Coor < coorX0 && spanX1Coor > coorX1)) {
					tableRowColumns.get(colNo).getCoordinatesList().getCoordinates().add(span.getCoordinates());
					if (!colValue[colNo].toString().trim().isEmpty()) {
						colValue[colNo].append(CoreCommonConstant.SPACE);
					}
					colValue[colNo].append(value);
					break;
				}
			}
			if ((tableEndPattern != null && !tableEndPattern.isEmpty()) && matcher.find()) {
				endPatternFound = true;
				break;
			}
		}
		for (int i = 0; i < colValue.length; i++) {
			if (colValue[i] != null && !colValue[i].toString().trim().isEmpty()) {
				for (final Integer colNo : columnVsCoordinates.keySet()) {
					tableRowColumns.get(colNo).setValue(colValue[colNo].toString().trim());
					tableRowColumns.get(colNo).setPage(pageID);
				}
				row.setRowCoordinates(rowCoordinates);
				rowList.add(row);
				break;
			}
		}
		// setAlternateValues(rowList, columnList.size());
		return endPatternFound;
	}

	/**
	 * This method sets empty stringBuilder for all array elements.
	 * 
	 * @param colValue {@link StringBuilder[]}
	 */
	private void setEmptyString(final StringBuilder[] colValue) {
		for (int i = 0; i < colValue.length; i++) {
			colValue[i] = new StringBuilder(CoreCommonConstant.EMPTY_STRING);
		}
	}

	private Row createNewRow(final List<Column> columnList) {
		final Row row = new Row();
		row.setIsRuleValid(false);
		row.setMannualExtraction(false);

		Row.Columns columnsRow = row.getColumns();
		if (null == columnsRow) {
			columnsRow = new Row.Columns();
			row.setColumns(columnsRow);
		}
		final List<Column> columnRowList = columnsRow.getColumn();
		String columnName = null;
		Column referenceColumn = null;
		for (int count = 0; count < columnList.size(); count++) {
			final Column column = new Column();
			referenceColumn = columnList.get(count);

			// Column Names were not getting extracted in the rows generated with manual extraction
			columnName = referenceColumn == null ? null : referenceColumn.getName();
			column.setValid(false);
			column.setValidationRequired(false);
			column.setConfidence(0.0f);
			column.setForceReview(false);
			column.setOcrConfidence(0.0f);
			column.setOcrConfidenceThreshold(0.0f);
			column.setValid(false);
			column.setValidationRequired(false);
			column.setName(columnName);
			column.setValue(null);
			column.setConfidence(0.0f);
			column.setCoordinatesList(new CoordinatesList());
			column.setPage(null);
			column.setValid(true);
			column.setAlternateValues(new Column.AlternateValues());
			columnRowList.add(column);
		}
		return row;
	}

	private Integer getTableStartYCoordinate(final Map<Integer, Coordinates> columnVsCoordinates) {
		Integer minY = 0;
		int counter = 1;
		for (final Integer colNo : columnVsCoordinates.keySet()) {
			final Coordinates coor = columnVsCoordinates.get(colNo);
			if (counter++ == 1) {
				minY = coor.getY0().intValue();
			} else if (minY > coor.getY0().intValue()) {
				minY = coor.getY0().intValue();
			}
		}
		return minY;
	}

	@Override
	public Document getFdTypeByDocTypeName(final String batchInstanceIdentifier, final String docTypeName) {
		final PluginPropertiesService pluginPropertiesService = this.getBeanByName("batchInstancePluginPropertiesService",
				BatchInstancePluginPropertiesService.class);
		final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		final EphesoftUser userType = EphesoftUser.NORMAL_USER;
		final BatchInstance batchInstance = batchInstanceService.getBatchInstanceByUserRole(getUserRoles(), batchInstanceIdentifier,
				getUserName(), userType);

		final List<FieldType> listFieldTypes = pluginPropertiesService.getFieldTypes(batchInstanceIdentifier, docTypeName);
		final Document documentType = new Document();
		final DocumentLevelFields documentLevelFields = new DocumentLevelFields();
		final List<DocField> documentLevelField = documentLevelFields.getDocumentLevelField();
		final DocumentType selectedDocumentType = pluginPropertiesService.getDocumentTypeByName(batchInstanceIdentifier, docTypeName);
		for (final FieldType fieldType : listFieldTypes) {
			final DocField dDocFieldType = new DocField();
			dDocFieldType.setConfidence(0.0f);
			dDocFieldType.setOcrConfidence(0.0f);
			dDocFieldType.setOcrConfidenceThreshold(fieldType.getOcrConfidenceThreshold());
			dDocFieldType.setForceReview(false);
			dDocFieldType.setCoordinatesList(null);
			dDocFieldType.setAlternateValues(null);
			dDocFieldType.setCategory(fieldType.getCategoryName());
			dDocFieldType.setName(fieldType.getName());
			dDocFieldType.setFieldOrderNumber(fieldType.getFieldOrderNumber());
			dDocFieldType.setOverlayedImageFileName(null);
			dDocFieldType.setPage(null);
			dDocFieldType.setType(fieldType.getDataType().name());
			dDocFieldType.setValue(CoreCommonConstant.EMPTY_STRING);
			dDocFieldType.setFieldValueOptionList(fieldType.getFieldOptionValueList());
			dDocFieldType.setFieldValueChangeScript(fieldType.isFieldValueChangeScriptEnabled());
			dDocFieldType.setHidden(fieldType.isHidden());
			
			/*
			 * EPHE-8996 - Priority Issue: Sticky Fields not working in 4.0.2.0
			 * Read only parameters should be reflected when the document type changes.
			 */
			dDocFieldType.setReadOnly(fieldType.getIsReadOnly());
			
			documentLevelField.add(dDocFieldType);
		}
		if (BatchInstanceStatus.READY_FOR_VALIDATION == batchInstance.getStatus()) {
			final List<TableInfo> tableInfoList = selectedDocumentType.getTableInfos();
			if (tableInfoList != null && !tableInfoList.isEmpty()) {
				setDataTables(tableInfoList, documentType);
			}
		}
		documentType.setDocumentLevelFields(documentLevelFields);
		documentType.setConfidence(0.0f);
		documentType.setDescription(selectedDocumentType.getDescription());
		documentType.setConfidenceThreshold(selectedDocumentType.getMinConfidenceThreshold());
		documentType.setErrorMessage(CoreCommonConstant.EMPTY_STRING);
		return documentType;
	}

	/**
	 * Sets the data tables information of a document type.
	 * 
	 * @param tableInfoList {@link List} the list of tables
	 * @param documentType {@link Document} the document whose table information needs to be added
	 */
	private void setDataTables(final List<TableInfo> tableInfoList, final Document documentType) {
		final DataTables dataTables = new DataTables();
		final List<DataTable> tables = dataTables.getDataTable();
		for (final TableInfo currentTableInfo : tableInfoList) {
			final DataTable dataTable = new DataTable();
			final HeaderRow headerRow = new HeaderRow();
			final Columns columns = new Columns();
			final List<Column> columnList = columns.getColumn();
			final List<TableColumnsInfo> tableColumnList = currentTableInfo.getTableColumnsInfo();
			for (final TableColumnsInfo currentTableColumnsInfo : tableColumnList) {
				final Column column = new Column();
				column.setValid(false);
				column.setValidationRequired(false);
				column.setConfidence(0.0f);
				column.setForceReview(false);
				column.setOcrConfidence(0.0f);
				column.setOcrConfidenceThreshold(0.0f);
				column.setValid(false);
				column.setValidationRequired(false);
				column.setName(currentTableColumnsInfo.getColumnName());
				columnList.add(column);
			}
			headerRow.setColumns(columns);
			final Rows rows = new Rows();
			dataTable.setHeaderRow(headerRow);
			dataTable.setRows(rows);
			dataTable.setName(currentTableInfo.getName());
			tables.add(dataTable);
		}
		documentType.setDataTables(dataTables);
	}

	@Override
	public Span getHOCRContent(final PointCoordinate pointCoordinate, final String batchInstanceIdentifier, final String hocrFileName) {

		boolean valid = true;
		if (batchInstanceIdentifier == null) {
			valid = false;
		}
		if (hocrFileName == null) {
			valid = false;
		}
		Span selectedSpan = null;
		if (valid) {
			final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			final HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier, hocrFileName);
			if (hocrPages != null && hocrPages.getHocrPage() != null) {
				final List<HocrPage> hocrPageList = hocrPages.getHocrPage();
				if (!hocrPageList.isEmpty()) {
					final HocrPage hocrPage = hocrPageList.get(0);
					List<Span> spanList = new ArrayList<Span>();
					if (hocrPage.getSpans() != null) {
						spanList = hocrPage.getSpans().getSpan();
					}
					final Integer xCoordinate = pointCoordinate.getxCoordinate();
					final Integer yCoordinate = pointCoordinate.getyCoordinate();
					for (final Span span1 : spanList) {
						final long spanX0 = span1.getCoordinates().getX0().longValue();
						final long spanY0 = span1.getCoordinates().getY0().longValue();
						final long spanX1 = span1.getCoordinates().getX1().longValue();
						final long spanY1 = span1.getCoordinates().getY1().longValue();
						if (spanX0 < xCoordinate && spanX1 > xCoordinate && spanY0 < yCoordinate && spanY1 > yCoordinate) {
							selectedSpan = span1;
							break;
						}
					}
				}
			}
		}
		return selectedSpan;
	}

	@Override
	public Page rotateImage(final String batchInstanceIdentifier, final Map<String, Document> alteredDocumentsMap,
			final List<String> documentIdentifierList, final Page page, final String documentId) throws UIException {

		final ImageProcessService imageProcessService = this.getSingleBeanOfType(ImageProcessService.class);
		try {
			final Batch batch = this.getBatch(batchInstanceIdentifier);
			if (null != batch) {
				final String batchFolderName = batch.getBatchLocalPath() + File.separator + batch.getBatchInstanceIdentifier();
				final String displayFilePath = batchFolderName + File.separator + page.getDisplayFileName();
				final String thumbnailFilePath = batchFolderName + File.separator + page.getThumbnailFileName();
				final String inputTiffPath = batchFolderName + File.separator + page.getNewFileName();
				imageProcessService.rotateImage(displayFilePath);
				imageProcessService.rotateImage(thumbnailFilePath);
				imageProcessService.rotateImage(inputTiffPath);
				Direction direction = page.getDirection();
				if (null == direction) {
					direction = Direction.NORTH;
					page.setDirection(direction);
				}
				Direction newDirection = null;
				switch (direction) {
					case EAST:
						newDirection = Direction.SOUTH;
						break;
					case SOUTH:
						newDirection = Direction.WEST;
						break;
					case WEST:
						newDirection = Direction.NORTH;
						break;
					case NORTH:
						newDirection = Direction.EAST;
						break;
				}
				page.setDirection(newDirection);
				page.setIsRotated(true);
				final File outputFolder = new File(batchFolderName + File.separator + newDirection.toString());
				if (!outputFolder.exists()) {
					outputFolder.mkdir();
				}
				final File inputImage = new File(displayFilePath);
				final File inputThumbNailImage = new File(thumbnailFilePath);
				final File outputImage = new File(batchFolderName + File.separator + newDirection.toString() + File.separator
						+ page.getDisplayFileName());
				final File outputThumbNailImage = new File(batchFolderName + File.separator + newDirection.toString() + File.separator
						+ page.getThumbnailFileName());
				try {
					FileUtils.copyFile(inputImage, outputImage);
					FileUtils.copyFile(inputThumbNailImage, outputThumbNailImage);
				} catch (final Exception e) {
					LOGGER.error("Exception while rotating page ", e);
				}
			}
		} catch (final NumberFormatException e) {
			LOGGER.error(e);
		} catch (final DCMAException e) {
			LOGGER.error("Could not rotate page ", e);
		}
		this.saveBatch(batchInstanceIdentifier, alteredDocumentsMap, documentIdentifierList);
		return page;
	}

	@Override
	public PluginPropertiesMetaData getPluginConfigurations(final String batchInstanceIdentifier,
			final BatchInstanceStatus batchInstanceStatus) throws UIException {
		PluginPropertiesMetaData metaData = null;
		try {
			if (!StringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
				metaData = new PluginPropertiesMetaData();
				final PluginPropertiesService pluginPropertiesService = this.getBeanByName("batchInstancePluginPropertiesService",
						BatchInstancePluginPropertiesService.class);
				final ApplicationConfigProperties configProperties = ApplicationConfigProperties.getApplicationConfigProperties();
				final String updatePropertyValue = configProperties.getProperty(ReviewValidateConstant.UPDATE_BATCH_INTERVAL_PROPERTY);
				final String ctrlQEnabled = configProperties.getProperty(ReviewValidateConstant.CTRL_Q_ENABLED);
				if (StringUtil.isValidNumericValue(updatePropertyValue)) {
					metaData.setSaveInterval(Integer.parseInt(updatePropertyValue));
				}
				final boolean value = Boolean.valueOf(ctrlQEnabled);
				metaData.setDefaultSaveOperation(!value);
				final BatchPlugin batchPlugin = pluginPropertiesService.getPluginProperties(batchInstanceIdentifier,
						VALIDATE_DOCUMENT_PLUGIN);
				final String indexFieldSeparator = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
						VALIDATE_DOCUMENT_PLUGIN, ValidateProperties.INDEX_FIELD_VALUE_SEPARATOR);
				final String stickyIndexFieldSwitch = batchPlugin
						.getPluginConfigurationValue(ValidateProperties.STICKY_INDEX_FIELD_SWITCH);
				metaData.setFieldValueSeparator(indexFieldSeparator == null ? CoreCommonConstant.SPACE : indexFieldSeparator);
				metaData.setShowSuggestions(CoreCommonConstant.ON.equalsIgnoreCase(batchPlugin
						.getPluginConfigurationValue(ValidateProperties.SUGGESTION_BOX_SWITCH)));

				metaData.setShowTablesSuggestions(CoreCommonConstant.ON.equalsIgnoreCase(batchPlugin
						.getPluginConfigurationValue(ValidateProperties.TABLE_EXTARCTION_SUGGESTION_BOX_SWITCH)));
				if (stickyIndexFieldSwitch != null) {
					metaData.setStickyIndexFieldSwitch(CoreCommonConstant.ON.equalsIgnoreCase(stickyIndexFieldSwitch));
				}
			}
		} catch (final IOException ioException) {
			throw new UIException("Could not get Plugin Properties", ioException);
		}
		return metaData;
	}

	public Boolean moveBatchToFinishedState(final String batchIdentifier) {
		boolean isSuccessful = Boolean.FALSE;
		EngineService engineService = null;
		BatchInstanceService batchInstanceService = null;
		BatchInstance batchInstance = null;
		if (null != batchIdentifier && !batchIdentifier.isEmpty()) {
			LOGGER.debug(EphesoftStringUtil.concatenate("Moving batch to finished state for batch: ", batchIdentifier));
			engineService = this.getSingleBeanOfType(EngineService.class);
			batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			if (null != engineService) {
				try {
					isSuccessful = engineService.deleteBatchInstance(batchIdentifier);
					LOGGER.debug(EphesoftStringUtil.concatenate("Engine service deleted batch status: ", isSuccessful));
				} catch (DCMAApplicationException dcmaApplicationException) {
					LOGGER.error(EphesoftStringUtil.concatenate("Error in deleting batch processing data while moving ",
							"the batch to FINISHED state: ", dcmaApplicationException.getMessage()));
				}
				if (isSuccessful) {
					LOGGER.debug(EphesoftStringUtil.concatenate("Changing status to finish: ", batchIdentifier));
					batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchIdentifier);
					deleteBatchInstanceThread(batchIdentifier);
					System.gc(); // To be removed- Added since BI*.zip was not deleting
					removeFolders(batchInstance);
					batchInstanceService.updateBatchInstanceStatus(batchInstance, BatchInstanceStatus.FINISHED);
				}
			}
		}
		return isSuccessful;
	}

	/**
	 * Deletes batch instance thread of the given batch instance identifier form thread pool.
	 * 
	 * @param batchIdentifier {@link String} batch instance identifier.
	 */
	private void deleteBatchInstanceThread(String batchIdentifier) {
		BatchInstanceThread batchInstanceThread = null;
		if (null != batchIdentifier && !batchIdentifier.isEmpty()) {
			LOGGER.debug(EphesoftStringUtil.concatenate("Deleting thread of batch instance: ", batchIdentifier));
			batchInstanceThread = ThreadPool.getBatchInstanceThreadList(batchIdentifier);
			if (batchInstanceThread != null) {
				batchInstanceThread.remove();
				LOGGER.debug(EphesoftStringUtil.concatenate("Batch Instance: ", batchIdentifier, " thread deleted."));
			}
		}
	}

	/**
	 * Removes folders of batch instance from ephesoft-system-folder and it's corresponding serialised file form properties folder.
	 * 
	 * @param batchInstance {@link BatchInstance} batch instance.
	 * @return true if folder and file is deleted.
	 */
	private boolean removeFolders(BatchInstance batchInstance) {
		boolean isDeleted = false;
		if (null != batchInstance) {
			LOGGER.debug(EphesoftStringUtil.concatenate("Removing BI* folder and serialized file for batch: ",
					batchInstance.getIdentifier()));
			File systemFolderFile = new File(EphesoftStringUtil.concatenate(batchInstance.getLocalFolder(), File.separator,
					batchInstance.getIdentifier()));
			File propertiesFile = new File(EphesoftStringUtil.concatenate(batchInstance.getLocalFolder(), File.separator,
					PROPERTIES_FOLDER, File.separator, batchInstance.getIdentifier(), FileType.SER.getExtensionWithDot()));

			// Deleted BI* folder from ephesoft system folder
			if (null != systemFolderFile) {
				try {
					org.apache.commons.io.FileUtils.deleteDirectory(systemFolderFile);
					isDeleted = true;
				} catch (IOException e) {
					isDeleted = false;
				}
			}

			// Deletes BI*.ser file from Properties folder
			if (null != propertiesFile) {
				isDeleted &= propertiesFile.delete();
			}
			LOGGER.debug(EphesoftStringUtil.concatenate("Deletion Status: ", isDeleted));
		}
		return isDeleted;
	}

	@Override
	public void invalidate(String batchInstanceIdentifier) {
		BatchCache.invalidate(batchInstanceIdentifier);
	}

	private void updateEndTimeAndCalculateDuration(String batchInstanceId) {
		LOGGER.info("Inside updateEndTimeAndCalculateDuration of ReviewValidateDocServiceImpl.");
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
		if (batchInstance != null) {
			BatchInstanceStatus batchInstanceStatus = batchInstance.getStatus();

			// Checking the batch instance status, which may be 'RUNNING' or 'READY' after review or validation has been done
			// and based on the executed modules setting the batch instance status as READY_FOR_REVIEW' or 'READY_FOR_VALIDATION'
			// to store it in 'hist_manual_steps_in_workflow' table
			if (batchInstanceStatus.equals(BatchInstanceStatus.RUNNING) || batchInstanceStatus.equals(BatchInstanceStatus.READY)) {
				String executedModules = batchInstance.getExecutedModules();
				if (executedModules.contains(MODULE_ID_FOR_REVIEW) && !executedModules.contains(MODULE_ID_FOR_VALIDATE)) {
					batchInstanceStatus = BatchInstanceStatus.READY_FOR_REVIEW;
				} else {
					batchInstanceStatus = BatchInstanceStatus.READY_FOR_VALIDATION;
				}
			}
			ManualStepHistoryService manualStepHistoryService = this.getSingleBeanOfType(ManualStepHistoryService.class);
			manualStepHistoryService.updateEndTimeAndCalculateDuration(batchInstanceId, batchInstanceStatus.name(), getUserName());
		} else {
			LOGGER.error("Not able to update End Time. BatchInstance is null");
		}
	}

	@Override
	public void cleanUpCurrentBatch(final String batchIdentifier) {
		if (!StringUtil.isNullOrEmpty(batchIdentifier)) {
			super.cleanUpCurrentBatch(batchIdentifier);
			updateEndTimeAndCalculateDuration(batchIdentifier);
		}
	}
}
