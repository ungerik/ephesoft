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

package com.ephesoft.dcma.lucene.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.lucene.LuceneEngine;
import com.ephesoft.dcma.lucene.LuceneProperties;
import com.ephesoft.dcma.util.BackUpFileService;
import com.ephesoft.dcma.util.EphesoftStringUtil;

public class SearchClassificationServiceImpl implements SearchClassificationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchClassificationServiceImpl.class);

	@Autowired
	private transient LuceneEngine luceneEngine;

	@PreProcess
	public void preProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) throws DCMAApplicationException {
		Assert.notNull(batchInstanceID);
		BackUpFileService.backUpBatch(batchInstanceID.getID());
	}

	@PostProcess
	public void postProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		BackUpFileService.copyBatchXML(batchInstanceID.getID(), pluginWorkflow);
	}

	@Override
	public void generateConfidenceScore(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			luceneEngine.generateConfidence(batchInstanceID.getID());
		} catch (Exception e) {
			LOGGER.error("Uncaught Exception in generateConfidence method " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	@Override
	public void generateConfidenceScoreAPI(final List<Document> xmlDocuments, final HocrPages hocrPages, final String workingDir,
			final Map<LuceneProperties, String> propertyMap, final String batchClassIdentifier) throws DCMAException {
		try {
			luceneEngine.generateConfidenceAPI(xmlDocuments, hocrPages, workingDir, propertyMap, batchClassIdentifier);
		} catch (Exception e) {
			LOGGER.error("Uncaught Exception in generateConfidence method " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	@Override
	public void learnSampleHOCR(final BatchClassID batchClassID, final boolean createIndex) throws DCMAException {
		try {
			luceneEngine.learnSampleHocrFiles(batchClassID.getID(), createIndex);
		} catch (Exception e) {
			LOGGER.error("Uncaught Exception in learnSampleHOCR method " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	@Override
	public void learnSampleHOCRForTesseract(final BatchClassID batchClassID, final boolean createIndex) throws DCMAException {
		try {
			luceneEngine.learnSampleHocrFilesForTesseract(batchClassID.getID(), createIndex);
		} catch (Exception e) {
			LOGGER.error("Uncaught Exception in learnSampleHOCR method " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, String> generateHOCRForKVExtractionTest(String imageFolder, String ocrEngineName, String batchClassIdentifer,
			File testImageFile, boolean isAdvancedKVExtraction) throws DCMAException {
		Map<String, String> imageToOCRFilePath = null;
		try {
			imageToOCRFilePath = luceneEngine.generateHOCRForKVExtractionTest(imageFolder, ocrEngineName, batchClassIdentifer,
					testImageFile, isAdvancedKVExtraction);
			luceneEngine.cleanUpIntrmediatePngs(imageFolder, !isAdvancedKVExtraction);
		} catch (Exception e) {
			LOGGER.error("Uncaught Exception in generateHOCRForKVExtractionTest method " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}

		return imageToOCRFilePath;
	}

	/**
	 * Performs learning for a batch class using the plugin selected for ocr'ing in the batch class configuration
	 * 
	 * @param batchClassID the batch class id
	 * @param createIndex specifies wheteher to create lucene indexes or not
	 * @throws DCMAException if an error occurs while creating the hocr file
	 * @throws IOException if an error occurs while creating the hocr file
	 */
	@Override
	public void learnSampleHOCRFilesUsingSelectedPlugin(final String batchClassID, final boolean createIndex) throws DCMAException,
			IOException {
		LOGGER.info("Entering learn sample hocr files using selected plugin...");
		if (null != batchClassID) {
			try {
				luceneEngine.learnSampleHOCRFilesUsingSelectedPlugin(batchClassID, createIndex);
			} catch (DCMAApplicationException dcmaApplicationException) {
				LOGGER.error(EphesoftStringUtil.concatenate("Uncaught exception in learn sample hocr files using selected plugin",
						dcmaApplicationException.getMessage()), dcmaApplicationException);
				throw new DCMAException(dcmaApplicationException.getMessage(), dcmaApplicationException);
			}
		} else {
			LOGGER.error("Batch class Id is null.");
			throw new DCMAException("Batch class id is null.");
		}
		LOGGER.info("Exiting learn sample hocr files using selected plugin...");
	}
}
