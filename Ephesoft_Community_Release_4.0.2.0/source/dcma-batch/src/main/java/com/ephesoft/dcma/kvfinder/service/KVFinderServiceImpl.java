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

package com.ephesoft.dcma.kvfinder.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.common.PatternMatcherUtil;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.KVExtractZone;
import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.kvfinder.KVFinderConstants;
import com.ephesoft.dcma.kvfinder.LocationFinder;
import com.ephesoft.dcma.kvfinder.data.CustomList;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.KeyValueFieldCarrier;
import com.ephesoft.dcma.kvfinder.data.KeyValueFieldCarrier.KeyValueProperties;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.ZonalKeyParameters;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.TIFFUtil;

public class KVFinderServiceImpl implements KVFinderService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(KVFinderServiceImpl.class);

	/**
	 * Confidence score.
	 */
	private String confidenceScore;

	/**
	 * Confidence score.
	 * 
	 * @return the confidenceScore
	 */
	public final String getConfidenceScore() {
		return confidenceScore;
	}

	/**
	 * Setter for confidence score.
	 * 
	 * @param confidenceScore the confidenceScore to set
	 */
	public final void setConfidenceScore(final String confidenceScore) {
		this.confidenceScore = confidenceScore;
	}

	public final List<OutputDataCarrier> findKeyValue(final List<InputDataCarrier> inputDataCarrierList, final HocrPage hocrPage,
			final Map<String, KeyValueFieldCarrier> fieldTypeKVMap, final KeyValueFieldCarrier keyValueFieldCarrier,
			final int maxResults, final String inputFilePath) throws DCMAException {

		LOGGER.info("Key value search for hocr page.");

		if (inputDataCarrierList == null || null == hocrPage) {
			LOGGER.error("Invalid InputDataCarrier. inputDataCarrier is null.");
			throw new DCMAException("Invalid inputDataCarrier.");
		}
		final CustomList outputDataCarrierList = new CustomList(maxResults);
		final Spans spans = hocrPage.getSpans();
		List<OutputDataCarrier> extractedValueList = null;

		if (null != spans) {
			final String pageID = hocrPage.getPageID();
			LOGGER.info("page id: " + pageID);
			final LocationFinder locationFinder = new LocationFinder();
			final List<LineDataCarrier> lineDataCarrierList = HocrUtil.getLineDataCarrierList(spans, pageID);

			int[] imageDimension = getImageDimensions(inputFilePath);

			locationFinder.setConfidenceScore(getConfidenceScore());
			for (InputDataCarrier inputDataCarrier : inputDataCarrierList) {
				boolean useExistingKey = inputDataCarrier.isUseExistingField();
				final String keyPattern = inputDataCarrier.getKeyPattern();
				LOGGER.info("Key pattern : " + keyPattern);
				LOGGER.info("Use Existing key : " + useExistingKey);

				if (LOGGER.isInfoEnabled()) {
					for (LineDataCarrier lineDataCarrier : lineDataCarrierList) {
						final String lineRowData = lineDataCarrier.getLineRowData();
						LOGGER.info(lineRowData);
					}
				}
				if (useExistingKey) {
					LOGGER.info("Extracting data using existing field as key. Field name : " + keyPattern);
					extractKVUsingExistingField(inputDataCarrier, locationFinder, outputDataCarrierList, fieldTypeKVMap, keyPattern,
							pageID, lineDataCarrierList, maxResults, keyValueFieldCarrier);
				} else {
					extractKey(keyValueFieldCarrier, maxResults, outputDataCarrierList, pageID, locationFinder, lineDataCarrierList,
							inputDataCarrier, keyPattern);
				}
				extractedValueList = applyExtractZoneFilter(outputDataCarrierList, inputDataCarrier.getExtractZone(), imageDimension);
			}
		}
		return extractedValueList;
	}

	/**
	 * Gets the dimension of image in an array. Element at 0th index is width, whereas one at 1st index is height.
	 * 
	 * @param inputFilePath {@link String} absolute path for file whose dimension is to be calculated.
	 * @return Returns the dimension of image in an array. Element at 0th index is width, whereas one at 1st index is height.
	 */
	private int[] getImageDimensions(final String inputFilePath) {
		LOGGER.debug("Entering method getImageDimensions....");
		int[] imageDimension = null;
		try {
			if (!EphesoftStringUtil.isNullOrEmpty(inputFilePath) && FileUtils.isFileExists(inputFilePath)) {
				imageDimension = TIFFUtil.getImageDimensions(inputFilePath);
			} else {
				LOGGER.debug(EphesoftStringUtil.concatenate("Image not found = ", inputFilePath));
			}
		} catch (Exception ioException) {
			LOGGER.error(EphesoftStringUtil.concatenate("Error occurred while reading file dimensions for ", inputFilePath),
					ioException);
		}
		LOGGER.debug("Exiting method getImageDimensions....");
		return imageDimension;
	}

	private List<OutputDataCarrier> applyExtractZoneFilter(final CustomList outputDataCarrierList, final KVExtractZone extractZone,
			final int[] imageDimension) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Entering method applyExtractZoneFilter for extract zone....", extractZone));
		List<OutputDataCarrier> finalExtractedDataList = null;
		List<OutputDataCarrier> extractedDataList = outputDataCarrierList.getList();

		// Filter out the extracted values by zone. Filtration will be done only if extract zone is specified and we have image
		// dimensions based on which zone will be defined.
		if (null != extractZone && KVExtractZone.ALL != extractZone && null != imageDimension) {
			int imageWidth = imageDimension[0];
			int imageHeight = imageDimension[1];

			Span extractedDataSpan = null;
			Coordinates dataCoordinates = null;

			finalExtractedDataList = new ArrayList<OutputDataCarrier>(extractedDataList.size());

			Coordinates zoneCooridnates = null;

			for (OutputDataCarrier extractedData : extractedDataList) {
				extractedDataSpan = extractedData.getSpan();
				if (extractedDataSpan != null) {
					dataCoordinates = extractedDataSpan.getCoordinates();

					zoneCooridnates = getExtractZoneCoordinates(extractZone, imageWidth, imageHeight);

					// Add extracted data to the final list only if lies/touches the zone coordinates.
					if (HocrUtil.isInsideZone(dataCoordinates, zoneCooridnates)) {
						LOGGER.debug(EphesoftStringUtil.concatenate("Adding extracted data == ", extractedData.getValue()));
						finalExtractedDataList.add(extractedData);
					}
				}
			}
		} else {
			finalExtractedDataList = extractedDataList;
		}
		LOGGER.debug("Exiting method applyExtractZoneFilter....");
		return finalExtractedDataList;
	}

	private Coordinates getExtractZoneCoordinates(final KVExtractZone extractZone, int imageWidth, int imageHeight) {
		Coordinates zoneCooridnates;
		zoneCooridnates = new Coordinates();

		switch (extractZone) {

			case TOP:
				zoneCooridnates.setX0(BigInteger.valueOf(0));
				zoneCooridnates.setY0(BigInteger.valueOf(0));
				zoneCooridnates.setX1(BigInteger.valueOf(imageWidth));
				zoneCooridnates.setY1(BigInteger.valueOf(imageHeight / 3));
				break;

			case MIDDLE:
				zoneCooridnates.setX0(BigInteger.valueOf(0));
				zoneCooridnates.setY0(BigInteger.valueOf(imageHeight / 3));
				zoneCooridnates.setX1(BigInteger.valueOf(imageWidth));
				zoneCooridnates.setY1(BigInteger.valueOf(imageHeight * 2 / 3));
				break;

			case BOTTOM:
				zoneCooridnates.setX0(BigInteger.valueOf(0));
				zoneCooridnates.setY0(BigInteger.valueOf(imageHeight * 2 / 3));
				zoneCooridnates.setX1(BigInteger.valueOf(imageWidth));
				zoneCooridnates.setY1(BigInteger.valueOf(imageHeight));
				break;

			case LEFT:
				zoneCooridnates.setX0(BigInteger.valueOf(0));
				zoneCooridnates.setY0(BigInteger.valueOf(0));
				zoneCooridnates.setX1(BigInteger.valueOf(imageWidth / 2));
				zoneCooridnates.setY1(BigInteger.valueOf(imageHeight));
				break;

			case RIGHT:
				zoneCooridnates.setX0(BigInteger.valueOf(imageWidth / 3));
				zoneCooridnates.setY0(BigInteger.valueOf(0));
				zoneCooridnates.setX1(BigInteger.valueOf(imageWidth));
				zoneCooridnates.setY1(BigInteger.valueOf(imageHeight));
				break;

			default:
				break;
		}
		return zoneCooridnates;
	}

	private void extractKey(final KeyValueFieldCarrier keyValueFieldCarrier, final int maxResults,
			final CustomList outputDataCarrierList, final String pageID, final LocationFinder locationFinder,
			final List<LineDataCarrier> lineDataCarrierList, InputDataCarrier inputDataCarrier, final String keyPattern) {
		LOGGER.info("Inside method extractKey...");
		if (null != keyPattern) {
			boolean isZonalKV = isZonalKVExtraction(inputDataCarrier);
			LOGGER.info("isZonalKV : "+isZonalKV);
			List<ZonalKeyParameters> foundKeyList = new ArrayList<ZonalKeyParameters>();
			for (int currentLineIndex = 0; currentLineIndex < lineDataCarrierList.size(); currentLineIndex++) {
				LineDataCarrier lineDataCarrier = lineDataCarrierList.get(currentLineIndex);
				try {
					List<OutputDataCarrier> dataCarrierList = null;

					// Getting the key fuzziness threshold value.
					final Float keyFuzziness = inputDataCarrier.getKeyFuzziness();

					// If key fuzziness if null then apply regex pattern technique for finding key and if key fuzziness threshold value
					// is defined for key pattern then use fuzzy matching technique.
					if (null == keyFuzziness || keyFuzziness == 0.0) {
						dataCarrierList = PatternMatcherUtil.findPattern(lineDataCarrier, keyPattern, getConfidenceScore(),
								inputDataCarrier.getWeightValue());
					} else {
						dataCarrierList = PatternMatcherUtil.findFuzzyPattern(lineDataCarrier, keyPattern, keyFuzziness);
					}
					if (null != dataCarrierList) {
						for (OutputDataCarrier dataCarrier : dataCarrierList) {
							String foundValue = dataCarrier.getValue();
							LOGGER.info("Key found : " + foundValue);
							Span span = dataCarrier.getSpan();
							if (null == foundValue || foundValue.isEmpty() || null == span) {
								continue;
							}

							String[] foundValArr = foundValue.split(KVFinderConstants.SPACE);
							List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
							coordinatesList.add(span.getCoordinates());
							Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
							Span rightSpan = null;
							if (null != spanIndex && null != foundValArr && foundValArr.length > 1) {
								for (int p = 0; p < foundValArr.length - 1; p++) {
									rightSpan = lineDataCarrier.getRightSpan(spanIndex);
									if (null != rightSpan) {
										coordinatesList.add(rightSpan.getCoordinates());
									}
									spanIndex = spanIndex + 1;
								}
							}
							Coordinates keyCoordinate = HocrUtil.getRectangleCoordinates(coordinatesList);
							if (!isZonalKV) {
								extractValue(keyCoordinate, lineDataCarrierList, inputDataCarrier, keyValueFieldCarrier, maxResults,
										outputDataCarrierList, locationFinder, currentLineIndex, pageID);
								//Test Adv KV Modification for Key pattern and Coordinates.
								if(outputDataCarrierList!=null && !outputDataCarrierList.isEmpty()){
									LOGGER.info("outputDataCarrierList size"+outputDataCarrierList.size());
									for(OutputDataCarrier carrier: outputDataCarrierList){
										LOGGER.info("Entering key for loop");
										if(carrier!=null){
											LOGGER.info("Setting key coordinates");
											carrier.setKey(foundValue);
											carrier.setKeySpan(span);
										}
									}
								}
								//End
							} else {
								ZonalKeyParameters zonalKeyParameters = new ZonalKeyParameters(currentLineIndex, keyCoordinate,
										keyPattern, foundValue);
								double distanceFromZone = HocrUtil.calculateDistanceFromZone(keyCoordinate, inputDataCarrier
										.getKeyRectangleCoordinates());
								zonalKeyParameters.setDistanceFromZone(distanceFromZone);
								foundKeyList.add(zonalKeyParameters);
							}
						}
					}
				} catch (Exception e) {
					LOGGER.error("Error while extracting key value data." + e.getMessage(), e);
				}
			}
			if (isZonalKV) {
				Collections.sort(foundKeyList, new CustomComparator());
				LOGGER.info("inside if loop of isZonalKV");
				for (ZonalKeyParameters zonalKeyParameters : foundKeyList) {
					if (zonalKeyParameters != null) {
						Coordinates keyCoordinates = zonalKeyParameters.getKeyCoordinates();
						int currentLineIndex = zonalKeyParameters.getLineIndex();
						/*
						 * if (isFirstKey) { isFirstKey = false; setOffset(keyCoordinates,
						 * inputDataCarrier.getKeyRectangleCoordinates(), inputDataCarrier); }
						 */
						try {
							LOGGER.info("Entering into extractValue from isZonalKV loop");
							extractValue(keyCoordinates, lineDataCarrierList, inputDataCarrier, keyValueFieldCarrier, maxResults,
									outputDataCarrierList, locationFinder, currentLineIndex, pageID);
							//Test Adv KV Modification for Key pattern and Coordinates.
							if(outputDataCarrierList!=null && !outputDataCarrierList.isEmpty()){
								LOGGER.info("outputDataCarrierList size from if(isZonalKV) loop"+outputDataCarrierList.size());
								for(OutputDataCarrier carrier: outputDataCarrierList){
									LOGGER.info("Entering key for loop");
									if(carrier!=null){
										LOGGER.info("Setting key coordinates"+zonalKeyParameters.getKey());
										if(carrier.getKey()==null){
											LOGGER.info("carrier.getKey()==null");
											carrier.setKey(zonalKeyParameters.getKey());
											Span span = new Span();
											span.setCoordinates(keyCoordinates);
											carrier.setKeySpan(span);
										}
										
									}
								}
							}
							//End
							
						} catch (DCMAApplicationException e) {
							LOGGER.error("Error while extracting key value data." + e.getMessage(), e);
						}
					}
				}
			}
		}
		LOGGER.info("Exiting method extractKey.....");
	}

	private void extractValue(final Coordinates keyCoordinate, final List<LineDataCarrier> lineDataCarrierList,
			final InputDataCarrier inputDataCarrier, final KeyValueFieldCarrier keyValueFieldCarrier, final int maxResults,
			final CustomList outputDataCarrierList, final LocationFinder locationFinder, final int currentLineIndex,
			final String pageID) throws DCMAApplicationException {
		LOGGER.info("Entering extractValue");
		CustomList outDataList = performValueExtraction(maxResults, locationFinder, outputDataCarrierList, inputDataCarrier, lineDataCarrierList,
				currentLineIndex, keyCoordinate);
//		List<OutputDataCarrier> outDataList = outputDataCarrierList.getList();
		List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
		for (OutputDataCarrier outDataCarrier : outDataList.getList()) {
			coordinatesList.add(outDataCarrier.getSpan().getCoordinates());
		}
		Coordinates recCoord = HocrUtil.getRectangleCoordinates(coordinatesList);
		if (keyValueFieldCarrier != null) {
			KeyValueProperties keyValueProperties = keyValueFieldCarrier.new KeyValueProperties();
			keyValueProperties.setValueCoordinates(recCoord);
			//Adv KV Button Customization: Setting Key Coordinates
			keyValueProperties.setKeyCoordinates(keyCoordinate);
			keyValueFieldCarrier.addKeyValueDataToPage(pageID, keyValueProperties);
		}
		LOGGER.info("Exiting extractValue");
	}

	private void extractKVUsingExistingField(final InputDataCarrier inputDataCarrier, final LocationFinder locationFinder,
			final CustomList outputDataCarrierList, final Map<String, KeyValueFieldCarrier> fieldTypeKVMap, String keyPattern,
			final String pageID, final List<LineDataCarrier> lineDataCarrierList, final int maxResults,
			final KeyValueFieldCarrier keyValueFieldCarrier) {
		LOGGER.info("Entering method extractKVUsingExistingField .....");
		try {
			if (keyPattern != null && !keyPattern.isEmpty() && fieldTypeKVMap != null && !fieldTypeKVMap.isEmpty()) {
				LOGGER.info("Getting KeyValueFieldCarrier for field type : " + keyPattern);
				KeyValueFieldCarrier keyPatternCarrier = fieldTypeKVMap.get(keyPattern);
				if (keyPatternCarrier != null && keyPatternCarrier.getKeyValuePropertiesForPage(pageID) != null) {
					LOGGER.info("Getting KeyValueProperties list for page id : " + pageID);
					List<KeyValueProperties> keyValuePropertiesList = keyPatternCarrier.getKeyValuePropertiesForPage(pageID);
					for (KeyValueProperties keyValueProperties : keyValuePropertiesList) {
						LOGGER.info("Getting KeyCoordinates for page id : " + pageID);
						Coordinates keyCoordinates = keyValueProperties.getValueCoordinates();
						for (int currentLineIndex = 0; currentLineIndex < lineDataCarrierList.size(); currentLineIndex++) {
							LineDataCarrier lineDataCarrier = lineDataCarrierList.get(currentLineIndex);
							if (HocrUtil.isValidRowForZone(lineDataCarrier.getRowCoordinates(), keyCoordinates)) {
								CustomList outDataCarrierList = performValueExtraction(maxResults, locationFinder, outputDataCarrierList, inputDataCarrier,
										lineDataCarrierList, currentLineIndex, keyCoordinates);
								String key="";
								//Modified for storing key Coordinates
								List<Span> spans=lineDataCarrier.getSpanList();
								for(Span span:spans){
									Coordinates spanCoordinate=span.getCoordinates();
									if(spanCoordinate.getX0().equals(keyCoordinates.getX0()) && spanCoordinate.getX1().equals(keyCoordinates.getX1()) && spanCoordinate.getY0().equals(keyCoordinates.getY0()) && spanCoordinate.getY1().equals(keyCoordinates.getY1())){
										key=span.getValue();
										break;
									}
								}
								//End
								List<OutputDataCarrier> outDataList = outDataCarrierList.getList();
								List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
								for (OutputDataCarrier outDataCarrier : outDataList) {
									coordinatesList.add(outDataCarrier.getSpan().getCoordinates());
									//Modified for storing key Coordinates
									Span span = new Span();
									span.setCoordinates(keyCoordinates);
									outDataCarrier.setKeySpan(span);
									outDataCarrier.setKey(key);
									//End
								}
								Coordinates recCoord = HocrUtil.getRectangleCoordinates(coordinatesList);
								if (keyValueFieldCarrier != null) {
									KeyValueProperties newKeyValueProperties = keyValueFieldCarrier.new KeyValueProperties();
									newKeyValueProperties.setValueCoordinates(recCoord);
									keyValueFieldCarrier.addKeyValueDataToPage(pageID, newKeyValueProperties);
								}
							}
						}
					}
				}
			}
		} catch (DCMAApplicationException dcmae) {
			LOGGER.error("Error while extracting key value field using existing field. Field : " + dcmae.getMessage(), dcmae);
		}
		LOGGER.info("Exiting method extractKVUsingExistingField .....");
	}

	private CustomList performValueExtraction(final int maxResults, final LocationFinder locationFinder,
			final CustomList outputDataCarrierList, InputDataCarrier inputDataCarrier,
			final List<LineDataCarrier> lineDataCarrierList, int currentLineIndex, Coordinates keyCoordinate)
			throws DCMAApplicationException {
		LOGGER.info("Entering performValueExtraction");
		final CustomList outputDataCarrierListInner = new CustomList(maxResults);
		valueExtraction(inputDataCarrier, outputDataCarrierListInner, lineDataCarrierList, currentLineIndex, locationFinder,
				keyCoordinate);
		for (OutputDataCarrier outputDataCarrier : outputDataCarrierListInner) {
			if (outputDataCarrier != null) {

				// Bug id #6875: Making confidence to be upto 2 decimal places only.
				float confidence = outputDataCarrier.getConfidence();
				outputDataCarrier.setConfidence(Math.round(confidence * 100) / 100f);
				outputDataCarrierList.add(outputDataCarrier);
			}
		}
		LOGGER.info("Exiting performValueExtraction");
		return outputDataCarrierListInner;
	}

	/**
	 * Value based extraction.
	 * 
	 * @param inputDataCarrier {@link InputDataCarrier}
	 * @param outputDataCarrierList CustomList
	 * @param lineDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param locationFinder {@link LocationFinder}
	 * @throws DCMAApplicationException
	 */
	private void valueExtraction(final InputDataCarrier inputDataCarrier, final CustomList outputDataCarrierList,
			final List<LineDataCarrier> lineDataCarrierList, final int currentLineIndex, final LocationFinder locationFinder,
			final Coordinates keyCoordinate) throws DCMAApplicationException {

		final LocationType locationType = inputDataCarrier.getLocationType();
		LOGGER.info("inside valueExtraction " + locationType);
		if (isZonalKVExtraction(inputDataCarrier)) {
			LOGGER.info("Inside isZonalKVExtraction");
			locationFinder.extractValueFromZone(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
					keyCoordinate);
		} else if (null != locationType) {

			switch (locationType) {

				case TOP:
					locationFinder.topLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
					break;

				case TOP_LEFT:
					locationFinder.topLeftLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);

					break;

				case TOP_RIGHT:
					locationFinder.topRightLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
					break;

				case RIGHT:
					locationFinder.rightLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
					break;

				case LEFT:
					locationFinder.leftLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
					break;

				case BOTTOM:
					locationFinder.bottomLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);

					break;

				case BOTTOM_LEFT:
					locationFinder.bottomLeftLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
					break;

				case BOTTOM_RIGHT:
					locationFinder.bottomRightLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
					break;

				default:
					LOGGER.info("***********  Default case found. In valid case.");
					break;
			}
		}

	}

	private boolean isZonalKVExtraction(InputDataCarrier inputDataCarrier) {
		boolean isZonalKV = false;
		if (inputDataCarrier.getLength() != null && inputDataCarrier.getWidth() != null
				&& (inputDataCarrier.getLength() > 0 || inputDataCarrier.getWidth() > 0)) {
			isZonalKV = true;
		}
		return isZonalKV;
	}

	class CustomComparator implements Comparator<ZonalKeyParameters> {

		@Override
		public int compare(final ZonalKeyParameters zonalKeyParameter1, final ZonalKeyParameters zonalKeyParameter2) {
			int compare = 0;
			if (zonalKeyParameter1 != null && zonalKeyParameter2 != null) {
				double distanceFromZone1 = zonalKeyParameter1.getDistanceFromZone();
				double distanceFromZone2 = zonalKeyParameter2.getDistanceFromZone();
				if (distanceFromZone2 > distanceFromZone1) {
					compare = -1;
				} else if (distanceFromZone2 < distanceFromZone1) {
					compare = 1;
				}
			}
			return compare;
		}
	}

}
