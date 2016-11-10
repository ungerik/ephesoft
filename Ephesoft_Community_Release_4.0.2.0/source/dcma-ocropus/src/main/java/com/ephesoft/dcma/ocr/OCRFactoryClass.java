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

package com.ephesoft.dcma.ocr;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.service.OCRService;
import com.ephesoft.dcma.ocr.factory.OCRFactory;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * Defines methods for performing ocr operation on files.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 17-Apr-2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Service
public class OCRFactoryClass {

	@Autowired
	private OCRFactory ocrFactory;

	@Autowired
	private OCRUtil ocrUtil;

	/**
	 * Creates the hocr for a file using the plugin selected for ocr'ing in the batch class.
	 * 
	 * @param batchClassIdentifier the batch class identifier.
	 * @param inputFolderLocation folder containing the images
	 * @param imageFileName image file to be used for ocr'ing
	 * @param outputFolderLocation folder where hocr will be generated
	 * @throws DCMAException if an error occurs while creating the hocr file
	 * @throws DCMAApplicationException if an error occurs while creating the hocr file
	 */
	public void createOCRForFile(final String batchClassIdentifier, final String inputFolderLocation, final String imageFileName,
			final String outputFolderLocation) throws DCMAException, DCMAApplicationException {
		String ocrPluginName = ocrUtil.getFirstOnOCRPlugin(batchClassIdentifier);
		if (!EphesoftStringUtil.isNullOrEmpty(ocrPluginName)) {
			OCRService ocrService = ocrFactory.getOCRService(ocrPluginName);
			ocrService.createOCRforFile(batchClassIdentifier, inputFolderLocation, imageFileName, outputFolderLocation);
		} else {
			throw new DCMAApplicationException(EphesoftStringUtil.concatenate("No ocr plugin configured for ocr'ing for batch class : ", batchClassIdentifier));
		}
	}

	/**
	 * Generates hocr files for all the images present inside a folder.
	 * 
	 * @param folderPath path of folder containing images
	 * @param batchClassIdentifier batch class identifier
	 * @throws DCMAApplicationException if an error occurs while creating the hocr file
	 * @throws IOException if an error occurs while creating the hocr file
	 */
	public void generateHOCRFilesForFolder(final String folderPath, final String batchClassIdentifier)
			throws DCMAApplicationException, IOException {
		String ocrPluginName = ocrUtil.getFirstOnOCRPlugin(batchClassIdentifier);
		if (!EphesoftStringUtil.isNullOrEmpty(ocrPluginName)) {
			OCRService ocrService = ocrFactory.getOCRService(ocrPluginName);
			ocrService.generateHOCRFilesForFolder(folderPath, batchClassIdentifier);
		} else {
			throw new DCMAApplicationException(EphesoftStringUtil.concatenate("No ocr plugin configured for ocr'ing for batch class : ", batchClassIdentifier));
		}
	}
}
