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

package com.ephesoft.dcma.regexvalidation;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.RegexValidation;
import com.ephesoft.dcma.da.service.FieldTypeService;
import com.ephesoft.dcma.regexvalidation.constant.RegexValidationConstants;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * This class is used to validate the document level fields of document for whole batch. It will validate the DLF's on the basis of
 * regex patterns available in database. If the document level field is valid with data type and regex pattern then only document is
 * called as valid document otherwise it is taken as invalid document.
 * 
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.regexvalidation.service.RegexValidationServiceImpl
 */
@Component
public class RegexAutomatedValidation implements ICommonConstants {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RegexAutomatedValidation.class);

	/**
	 * fieldService FieldService.
	 */
	@Autowired
	private FieldTypeService fieldTypeService;

	/**
	 * Reference of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	private static final String optionListSeparator = ";";

	/**
	 * The <code>validateDLFields</code> method is used to validate the document level fields of document for whole batch. It will
	 * fetch all the record corresponding to document level fields from the database and on the basis of that pattern it will validate
	 * the document level field. If all the patterns satisfied with the document level field value then only that document will marked
	 * as valid otherwise it will marked as invalid document.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @throws DCMAApplicationException If any invalid state occur during the regex based validation process.
	 */
	public boolean validateDLFields(final String batchInstanceIdentifier) throws DCMAApplicationException {

		String errMsg = null;
		if (null == batchInstanceIdentifier) {
			errMsg = "Invalid batchInstanceId.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		if (null == fieldTypeService) {
			errMsg = "Invalid intialization of FieldService.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		LOGGER.info("batchInstanceIdentifier : " + batchInstanceIdentifier);

		final Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

		boolean isSuccessful = false;
		try {

			final List<Document> docTypeList = batch.getDocuments().getDocument();

			if (null == docTypeList) {
				LOGGER.info("In valid batch documents.");
			} else {
				final String ocrSwitch = getOcrConfidenceSwitchValue();
				LOGGER.debug(EphesoftStringUtil.concatenate("OCR switch: ", ocrSwitch));

				validateDLFields(docTypeList, batchInstanceIdentifier, ocrSwitch);

				// START.. add field option list if any....
				setAndValidateFieldValOptList(docTypeList, batchInstanceIdentifier);
				// END...

				// now write the state of the object to the xml file.
				batchSchemaService.updateBatch(batch);
				LOGGER.info("Updated the batch xml file with valid/invalid document state.");

				isSuccessful = true;
			}

		} catch (DCMAApplicationException e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}

		return isSuccessful;
	}

	/**
	 * This method is responsible for setting the field option list for document level fields.
	 * 
	 * @param docTypeList List<Document>
	 * @param batchInstanceIdentifier {@link String}
	 */
	private void setAndValidateFieldValOptList(final List<Document> docTypeList, final String batchInstanceIdentifier) {
		for (Document document : docTypeList) {
			if (null != document) {
				DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
				if (documentLevelFields != null) {
					List<DocField> docFieldList = documentLevelFields.getDocumentLevelField();
					List<FieldType> fieldTypeList = pluginPropertiesService.getFieldTypes(batchInstanceIdentifier, document.getType());
					if (fieldTypeList != null) {

						// if (docFieldList.isEmpty()) {
						// for (FieldType fieldType : fieldTypeList) {
						// if (null != fieldType) {
						// DocField docField = new DocField();
						// docField.setName(fieldType.getName());
						// docField.setFieldOrderNumber(fieldType.getFieldOrderNumber());
						// docField.setFieldValueOptionList(fieldType.getFieldOptionValueList());
						// docField.setConfidence(0f);
						// docField.setType(fieldType.getDataType().name());
						// docField.setValue("");
						// docFieldList.add(docField);
						// }
						// }
						// }

						for (DocField docField : docFieldList) {
							for (FieldType fieldType : fieldTypeList) {
								if (docField != null && fieldType != null) {
									String docFieldName = docField.getName();
									String fieldTypeName = fieldType.getName();
									if (null != docFieldName && docFieldName.equals(fieldTypeName)) {
										String fieldOptionValueList = fieldType.getFieldOptionValueList();
										if (!EphesoftStringUtil.isNullOrEmpty(fieldOptionValueList)
												&& EphesoftStringUtil.isNullOrEmpty(docField.getFieldValueOptionList())) {
											docField.setFieldValueOptionList(fieldOptionValueList);
										}
										String existingFieldValueOptionList = docField.getFieldValueOptionList();
										if (!EphesoftStringUtil.isNullOrEmpty(existingFieldValueOptionList)) {
											String value = docField.getValue();
											boolean isValueValid = checkFieldOptionList(value, existingFieldValueOptionList);
											// Add new value to the list if it
											// does not exist already.
											if (!isValueValid) {
												if (!EphesoftStringUtil.isNullOrEmpty(value)) {
													docField.setFieldValueOptionList(EphesoftStringUtil.concatenate(value,
															optionListSeparator, existingFieldValueOptionList));
												} else {
													docField.setFieldValueOptionList(existingFieldValueOptionList);
												}
											}
											// document.setValid(document.isValid()
											// && isValueValid);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean checkFieldOptionList(String data, String fieldValueOptionList) {
		boolean optionValueExists = false;
		// Find all existing values.
		String[] optionList = EphesoftStringUtil.splitString(fieldValueOptionList, optionListSeparator);
		if (null != optionList) {
			for (String valueOption : optionList) {
				if (valueOption.equals(data)) {
					// Value exists in the list.
					optionValueExists = true;
					break;
				}
			}
		}
		return optionValueExists;
	}

	/**
	 * The <code>validateDLFields</code> method is used to validate the document level fields. If all the patterns satisfied with the
	 * document level field value then only that document will marked as valid otherwise it will marked as invalid document.
	 * 
	 * @param xmlDocuments List<Document>
	 * @param batchInstanceIdentifier {@link String}
	 * @param batch {@link Batch}
	 * @throws DCMAApplicationException If any invalid state occur during the regex based validation process.
	 */
	private void validateDLFields(final List<Document> xmlDocuments, final String batchInstanceIdentifier, final String ocrSwitch)
			throws DCMAApplicationException {
		String errMsg = null;
		List<DocField> documentLevelField = null;
		String docTypeName = null;
		boolean regexValid = false;
		boolean ocrValid = true;
		boolean docValid = true;
		boolean isOCRCheckRequired = ON.equalsIgnoreCase(ocrSwitch);
		documentFor: for (Document document : xmlDocuments) {
			docTypeName = document.getType();
			ocrValid = true;
			docValid = true;
			LOGGER.info(EphesoftStringUtil.concatenate("docTypeName: ", docTypeName));

			DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();

			if (null == documentLevelFields) {
				document.setValid(true);
				// continue;
			} else {
				documentLevelField = documentLevelFields.getDocumentLevelField();
				if (null == documentLevelField || documentLevelField.isEmpty()) {
					LOGGER.info("Document level field is null or empty.");
					document.setValid(true);
					// continue;
				} else {
					final List<com.ephesoft.dcma.da.domain.FieldType> allFdTypes = fieldTypeService
							.getFdTypeAndRegexValidationByDocTypeName(docTypeName, batchInstanceIdentifier);

					if (null == allFdTypes || allFdTypes.isEmpty()) {
						errMsg = "No FieldType data found from data base for document type : " + docTypeName;
						LOGGER.info(errMsg);
						document.setValid(true);
						// continue;
					} else {

						LOGGER.info("FieldType data found from data base for document type : " + docTypeName);
						for (final DocField docField : documentLevelField) {
							float ocrConfidence = 0;
							final String value = docField.getValue();
							final String name = docField.getName();
							if (!EphesoftStringUtil.isNullOrEmpty(value) && isOCRCheckRequired) {
								ocrConfidence = getOcrConfidence(batchInstanceIdentifier, docField, document);
								LOGGER.debug(EphesoftStringUtil.concatenate("For docField: ", name, ", setting ocr confidence to ",
										ocrConfidence));
								docField.setOcrConfidence(ocrConfidence);

							}
							if (name == null) {
								LOGGER.info("Name is null for document level field.");
								continue;
							}

							for (com.ephesoft.dcma.da.domain.FieldType fdType : allFdTypes) {

								if (null == fdType) {
									LOGGER.info("field is null for database field type.");
									continue;
								}

								final String dbFdName = fdType.getName();

								if (null == dbFdName) {
									LOGGER.info("field name is null for database field type.");
									continue;
								}

								if (dbFdName.equals(name)) {
									final List<RegexValidation> regexValidationList = fdType.getRegexValidation();

									// If Doc level field value is invalid then continue to the next document and make that document
									// invalid otherwise set document to valid.
									if (!CollectionUtils.isEmpty(regexValidationList)) {
										regexValid = checkForDocFieldValue(value, regexValidationList);
									} else {
										LOGGER.info("Regex validation list is empty.");
										regexValid = true;
									}

									if (!EphesoftStringUtil.isNullOrEmpty(value) && isOCRCheckRequired && ocrConfidence != 0) {
										docField.setOcrConfidenceThreshold(fdType.getOcrConfidenceThreshold());
										float ocrConfidenceThreshold = docField.getOcrConfidenceThreshold();
										if (ocrConfidence < ocrConfidenceThreshold) {
											docField.setForceReview(true);
											ocrValid = false;
											LOGGER.info("Ocr confidence is less than threshold for Document type: " + docTypeName);
										}
									}

									if (docValid) {
										if (regexValid && ocrValid) {
											LOGGER.info("Setting document type as valid document. Document type: " + docTypeName);
											document.setValid(true);
											if (!isOCRCheckRequired) {
												continue documentFor;
											}
										} else {
											LOGGER.info(EphesoftStringUtil.concatenate(
													"Setting document type as invalid document. Document type: ", docTypeName));
											document.setValid(false);
											docValid = false;
										}
									}
									break;
								}
							}
						}
					}
				}
			}
			if (document.isValid()) {
				document.setValid(checkForInvalidDataTables(document));
			}
		}
	}

	private boolean checkForInvalidDataTables(Document document) {
		LOGGER.info("Checking for invalid datatables.");
		boolean isValidDoc = true;
		if (document.getDataTables() != null) {
			List<DataTable> dataTableList = document.getDataTables().getDataTable();
			for (DataTable dataTable : dataTableList) {
				LOGGER.debug("Datatable name : " + dataTable.getName());
				if (dataTable.getRows() != null) {
					isValidDoc = checkForInvalidRow(dataTable);
					if (!isValidDoc) {
						break;
					}
				}
			}
		}
		LOGGER.debug("Is datatable valid : " + isValidDoc);
		return isValidDoc;
	}

	public boolean checkForInvalidRow(final DataTable dataTable) {
		LOGGER.info("Checking for invalid rows in table.");
		boolean isValidDoc = true;
		List<Row> rowList = dataTable.getRows().getRow();

		// changes made for setting the document as invalid if a table fails to satisfy the rules defined for it.
		for (Row row : rowList) {
			if (row != null) {
				if (row.isIsRuleValid()) {
					if (row.getColumns() != null) {
						List<Column> columnList = row.getColumns().getColumn();
						for (Column column : columnList) {
							LOGGER.debug("Column data : " + column.getValue());
							if (!column.isValid()) {
								isValidDoc = false;
								LOGGER.debug("Column not valid.");
								break;
							}
						}
						if (!isValidDoc) {
							break;
						}
					}
				} else {
					isValidDoc = false;
					LOGGER.debug("Row is invalid.");
					break;
				}
			}
		}
		return isValidDoc;
	}

	/**
	 * The <code>checkValueText</code> method will check the valueText with typeText compatibility.
	 * 
	 * @param valueText {@link String}
	 * @param typeText {@link String}
	 * @return boolean true if pass the test otherwise false.
	 */
	// private boolean checkValueText(String valueText, String typeText) {
	//
	// boolean isValid = false;
	// if (null == valueText || RegexValidationConstants.EMPTY.equals(valueText)
	// || null == typeText
	// || RegexValidationConstants.EMPTY.equals(typeText)) {
	// LOGGER.error("Input value text or type text is null or empty.");
	// isValid = false;
	// } else {
	// if (typeText.equals(RegexValidationConstants.DATE)) {
	// SimpleDateFormat format = new
	// SimpleDateFormat(RegexValidationConstants.PATTERN);
	// try {
	// format.parse(valueText);
	// isValid = true;
	// } catch (Exception e) {
	// // the value couldn't be parsed by the pattern, return false.
	// LOGGER.error(e.getMessage(), e);
	// isValid = false;
	// }
	// } else {
	// if (typeText.equals(RegexValidationConstants.LONG)) {
	// try {
	// Long.parseLong(valueText);
	// isValid = true;
	// } catch (Exception e) {
	// // the value couldn't be parsed by the pattern, return false
	// LOGGER.error(e.getMessage(), e);
	// isValid = false;
	// }
	// } else {
	// if (typeText.equals(RegexValidationConstants.DOUBLE)) {
	// try {
	// Float.parseFloat(valueText);
	// isValid = true;
	// } catch (Exception e) {
	// // the value couldn't be parsed by the pattern, return false
	// LOGGER.error(e.getMessage(), e);
	// isValid = false;
	// }
	// } else {
	// if (typeText.equals(RegexValidationConstants.STRING)) {
	// isValid = true;
	// } else {
	// isValid = false;
	// }
	// }
	// }
	// }
	// }
	//
	// return isValid;
	// }

	/**
	 * The <code>findPattern</code> method will test the pattern on the input character sequence and return true if and only if it
	 * passes the test.
	 * 
	 * @param inputCharSequence {@link String}
	 * @param patternStr {@link String}
	 * @return boolean if it pass the pattern matching test.
	 * @throws DCMAApplicationException if any invalid state occur during pattern test process.
	 */
	private boolean findPattern(String inputCharSequence, String patternStr) throws DCMAApplicationException {

		String errMsg = null;
		CharSequence inputStr = inputCharSequence;
		boolean isFound = false;
		if (null == patternStr || RegexValidationConstants.EMPTY.equals(patternStr)) {
			errMsg = "Invalid input pattern sequence.";
			throw new DCMAApplicationException(errMsg);
		}
		if (null == inputStr) {
			errMsg = EphesoftStringUtil.concatenate("Invalid input character sequence.", inputStr);
			LOGGER.info(errMsg);
			inputStr = "";
		}

		// Compile and use regular
		// expression
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(inputStr);
		// boolean matchFound = matcher.find();
		whileLoop: while (matcher.find()) {
			// Get all groups for this match
			for (int i = 0; i <= matcher.groupCount();) {
				String groupStr = matcher.group(i);
				if (groupStr != null && groupStr.equals(inputStr)) {
					isFound = true;
					break whileLoop;
				} else {
					break whileLoop;
				}
			}
		}

		return isFound;
	}

	/**
	 * Matches the document level field value with the list of regex pattern based on the regex listing separator and if it is matched
	 * then this method returns <code>true</code> otherwise it returns <code>false</code>.
	 * 
	 * @param docFieldValue {@link String} The value of document level field.
	 * @param regexValidationList {@link List<{@link RegexValidation}> The list of regex patterns defined for that document level
	 *            field.
	 * @return <code>true</code> if the document level field is valid otherwise returns <code>false</code>
	 * @throws DCMAApplicationException {@link DCMAApplicationException} if any invalid state occur during pattern test process.
	 */
	private boolean checkForDocFieldValue(final String docFieldValue, final List<RegexValidation> regexValidationList)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (regexValidationList != null && !regexValidationList.isEmpty()) {
			for (RegexValidation regexValidation : regexValidationList) {
				if (regexValidation != null) {
					final String pattern = regexValidation.getPattern();
					isValid = findPattern(docFieldValue, pattern);
					if (isValid) {
						if (regexValidation.getFieldType() != null
								&& !RegexValidationConstants.AND_STRING.equalsIgnoreCase(regexValidation.getFieldType()
										.getRegexListingSeparator())) {
							break;
						}
					} else {
						if (regexValidation.getFieldType() != null
								&& RegexValidationConstants.AND_STRING.equalsIgnoreCase(regexValidation.getFieldType()
										.getRegexListingSeparator())) {
							break;
						}
					}
				}
			}
		}
		return isValid;
	}

	/**
	 * Returns the value of OCR confidence switch.
	 * 
	 * @return {@link String}
	 */
	private String getOcrConfidenceSwitchValue() {
		String ocrSwitch = ON;
		try {
			ocrSwitch = ApplicationConfigProperties.getApplicationConfigProperties().getProperty(
					RegexValidationConstants.OCR_CONFIDENCE_SWITCH);
		} catch (final IOException ioException) {
			LOGGER.error("Input ouput exception while reading the property: ocr_confidence_switch");
		}
		return ocrSwitch;
	}

	private float getOcrConfidence(final String batchInstanceIdentifier, final DocField docField, final Document document) {
		final List<Page> listOfPage = document.getPages().getPage();
		final String value = docField.getValue();
		final String docFieldPage = docField.getPage();
		float ocrConfidence = 0;
		boolean isFound = false;
		if (null != value) {
			for (final Page page : listOfPage) {
				if (page.getIdentifier().equals(docFieldPage)) {
					final String hocrFileName = page.getHocrFileName();
					if (null != hocrFileName) {
						final HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier, hocrFileName);
						if (null != hocrPages) {
							final List<HocrPage> hocrPageList = hocrPages.getHocrPage();
							final HocrPage hocrPage = hocrPageList.get(0);
							if (null != hocrPage) {
								final Spans spans = hocrPage.getSpans();
								if (null != spans) {
									final List<Span> mainSpanList = spans.getSpan();
									Coordinates spanCoordinates = null;
									for (final Span span : mainSpanList) {
										spanCoordinates = span.getCoordinates();
										final CoordinatesList coordinatesList = docField.getCoordinatesList();
										if (null != coordinatesList) {
											final List<Coordinates> coordinates = coordinatesList.getCoordinates();
											for (Coordinates coordinate : coordinates) {
												if (HocrUtil.isInsideZone(spanCoordinates, coordinate)) {
													String spanOcrConfidence = span.getOcrConfidence();
													if (!EphesoftStringUtil.isNullOrEmpty(spanOcrConfidence)) {
														try {
															ocrConfidence = Float.valueOf(spanOcrConfidence);
															isFound = true;
															break;
														} catch (NumberFormatException numberFormatException) {
															LOGGER.error("Error in parsing OCR confidence"
																	+ numberFormatException.getMessage());
														}
													}
												}
											}
										}
										if (isFound) {
											break;
										}
									}
								}
							}
						}
					}
					break;
				}
			}
		}

		return ocrConfidence;
	}

}
