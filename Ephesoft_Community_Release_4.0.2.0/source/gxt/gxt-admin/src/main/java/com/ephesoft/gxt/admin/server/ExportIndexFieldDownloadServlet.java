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

package com.ephesoft.gxt.admin.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SerializationUtils;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.da.service.FieldTypeService;
import com.ephesoft.gxt.core.server.BatchClassUtil;
import com.ephesoft.gxt.core.server.DCMAHttpServlet;
import com.ephesoft.dcma.util.FileUtils;

/**
 * Servlet for exporting index field.
 * 
 * @author ephesoft
 * @version 1.0
 * 
 */
public class ExportIndexFieldDownloadServlet extends DCMAHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String SERIALIZATION_EXT = FileType.SER.getExtensionWithDot();
	private static final String ZIP_EXT = FileType.ZIP.getExtensionWithDot();

	@Override
	public final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		doGet(request, response);
	}

	/**
	 * This API is used to create directory for given path in parameter.
	 * 
	 * @param dirPath {@link String} to create directory.
	 * @return {@link ServletFileUpload} uploaded file.
	 */
	private File createDirectory(final String dirPath) {
		final File copiedtFolder = new File(dirPath);
		if (copiedtFolder.exists()) {
			copiedtFolder.delete();
		}
		copiedtFolder.mkdirs();
		return copiedtFolder;
	}

	/**
	 * This API is used to modify document type to make document identifier null.
	 * 
	 * @param documentType {@link FieldType}.
	 * @return {@link FieldType} modified document type.
	 */
	private FieldType modifyFieldTypeForExport(final FieldType fieldType) {
		BatchClassUtil.copyKVExtractionFields(fieldType);
		BatchClassUtil.copyRegex(fieldType);
		;
		return fieldType;
	}

	/**
	 * This API is used to process document types to export.
	 * 
	 * @param fieldTypeList {@link List<{@link FieldType}>} document type list to export.
	 * @param documentType {@link documentType} batch class for which document type is exporting.
	 * @param bsService {@link BatchSchemaService}.
	 * @param isImagemagickFd boolean to identify whether image classification samples to export.
	 * @param isSearchSampleFd boolean to identify whether search classification samples to export.
	 * @param resp {@link HttpServletResponse}
	 * @return
	 */
	private void processExportFieldTypes(final List<FieldType> fieldTypeList, final BatchSchemaService bsService,
			final HttpServletResponse resp, String zipFileName) throws IOException {
		final String exportSerFdPath = bsService.getBatchExportFolderLocation();

		final String parentFdPath = exportSerFdPath + File.separator + zipFileName;

		for (int docNum = 0; docNum < fieldTypeList.size(); docNum++) {
			FieldType fieldType = null;
			fieldType = fieldTypeList.get(docNum);
			createDirectory(parentFdPath);
			fieldType = modifyFieldTypeForExport(fieldType);

			final File serializedFile = new File(parentFdPath + File.separator + fieldType.getIdentifier() + SERIALIZATION_EXT);

			try {
				SerializationUtils.serialize(fieldType, new FileOutputStream(serializedFile));

			} catch (final FileNotFoundException e) {
				// Unable to read serializable file
				log.error("Error occurred while creating the serializable file." + e, e);
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error occurred while creating the serializable file.");

			} catch (final IOException e) {
				// Unable to create the temporary export file(s)/folder(s)
				log.error("Error occurred while creating the serializable file." + e, e);
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Error occurred while creating the serializable file.Please try again");
			}

		}
	}

	/**
	 * This API is used to process request to export the documents .
	 * 
	 * @param fieldTypeList {@link List<{@link FieldType}>} selected document type list to export.
	 * @param resp {@link HttpServletResponse}.
	 * @return
	 */
	private void process(final List<FieldType> fieldTypeList, final HttpServletResponse resp) throws IOException {
		final BatchSchemaService bSService = this.getSingleBeanOfType(BatchSchemaService.class);
		final String exportSerDirPath = bSService.getBatchExportFolderLocation();

		final String zipFileName = fieldTypeList.get(0).getDocType().getName() + "_" + "FieldTypes";

		final String copiedParentFolderPath = exportSerDirPath + File.separator + zipFileName;

		final File copiedFd = createDirectory(copiedParentFolderPath);

		processExportFieldTypes(fieldTypeList, bSService, resp, zipFileName);
		resp.setContentType("application/x-zip\r\n");
		resp.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + ZIP_EXT + "\"\r\n");
		ServletOutputStream out = null;
		ZipOutputStream zout = null;
		try {
			out = resp.getOutputStream();
			zout = new ZipOutputStream(out);
			FileUtils.zipDirectory(copiedParentFolderPath, zout, zipFileName);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (final IOException e) {
			// Unable to create the temporary export file(s)/folder(s)
			log.error("Error occurred while creating the zip file." + e, e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to export.Please try again.");
		} finally {
			// clean up code
			if (zout != null) {
				zout.close();
			}
			if (out != null) {
				out.flush();
			}
			FileUtils.deleteDirectoryAndContentsRecursive(copiedFd);
		}
	}

	public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {

		final FieldTypeService fieldTypeService = this.getSingleBeanOfType(FieldTypeService.class);
		final List<FieldType> fieldTypeList = new ArrayList<FieldType>();
		String listOfIdentifier = req.getParameter("identifier");
		for (String identifier : listOfIdentifier.split(";")) {
			if (null != identifier && !identifier.isEmpty()) {
				FieldType fieldType = fieldTypeService.getLoadedFieldTypeByIdentifer(identifier);
				
				fieldTypeList.add(fieldType);
			}
		}

		if (fieldTypeList.isEmpty()) {
			log.error("No Index Field selected to export.");
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No Index Field selected to export.");
		} else {
		}
		process(fieldTypeList, resp);
	}

}
