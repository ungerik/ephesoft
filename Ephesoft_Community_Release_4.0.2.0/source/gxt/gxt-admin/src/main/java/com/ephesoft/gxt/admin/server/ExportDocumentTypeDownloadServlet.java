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
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.gxt.core.server.BatchClassUtil;
import com.ephesoft.gxt.core.server.DCMAHttpServlet;
import com.ephesoft.dcma.util.FileUtils;

/**
 * Servlet for exporting document types.
 * 
 * @author ephesoft
 * @version 1.0
 * 
 */
public class ExportDocumentTypeDownloadServlet extends DCMAHttpServlet {

	private static final String EMPTY_VALUE = "";

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
	 * This API is used to get sub directories list.
	 * 
	 * @param dirPath {@link String}.
	 * @return {@link String[]} sub directories list.
	 */
	private String[] getSubDirectoriesList(final String dirPath) {
		String[] dirList = null;
		final File classificationDir = new File(dirPath);
		if (classificationDir.exists() && classificationDir.isDirectory()) {

			dirList = classificationDir.list();
			Arrays.sort(dirList);
		}
		return dirList;
	}

	/**
	 * This API is used to modify document type to make document identifier null.
	 * 
	 * @param documentType {@link DocumentType}.
	 * @return {@link DocumentType} modified document type.
	 */
	private DocumentType modifyDocumentTypeToExport(final DocumentType documentType) {
		BatchClassUtil.copyPageTypes(documentType);
		BatchClassUtil.copyFieldTypes(documentType);
		BatchClassUtil.copyTableInfo(documentType);
		BatchClassUtil.copyFunctionKeys(documentType);
		documentType.setFirstPageProjectFileName(EMPTY_VALUE);
		documentType.setSecondPageProjectFileName(EMPTY_VALUE);
		documentType.setThirdPageProjectFileName(EMPTY_VALUE);
		documentType.setFourthPageProjectFileName(EMPTY_VALUE);
		return documentType;
	}

	/**
	 * This API is used to process document types to export.
	 * 
	 * @param docTypeList {@link List<{@link DocumentType}>} document type list to export.
	 * @param batchClass {@link BatchClass} batch class for which document type is exporting.
	 * @param bsService {@link BatchSchemaService}.
	 * @param isImagemagickFd boolean to identify whether image classification samples to export.
	 * @param isSearchSampleFd boolean to identify whether search classification samples to export.
	 * @param resp {@link HttpServletResponse}
	 * @return
	 */
	private void processDocumentTypes(final List<DocumentType> docTypeList, final BatchClass batchClass,
			final BatchSchemaService bsService, final boolean isImagemagickFd, final boolean isSearchSampleFd,
			final HttpServletResponse resp) throws IOException {
		String[] imageMagickFdList = null;
		String[] searchFdList = null;
		final String imageMagickFdPath = bsService.getBaseSampleFDLock() + File.separator + batchClass.getIdentifier()
				+ File.separator + bsService.getImagemagickBaseFolderName();
		final String searchFdPath = bsService.getBaseSampleFDLock() + File.separator + batchClass.getIdentifier() + File.separator
				+ bsService.getSearchSampleName();
		imageMagickFdList = getSubDirectoriesList(imageMagickFdPath);
		searchFdList = getSubDirectoriesList(searchFdPath);
		final String exportSerFdPath = bsService.getBatchExportFolderLocation();

		final String zipFileName = batchClass.getIdentifier() + "_" + "DocumentTypes";
		final String parentFdPath = exportSerFdPath + File.separator + zipFileName;

		for (int docNum = 0; docNum < docTypeList.size(); docNum++) {
			DocumentType documentType = null;
			documentType = docTypeList.get(docNum);
			final Calendar cal = Calendar.getInstance();

			final SimpleDateFormat formatter = new SimpleDateFormat("MMddyy");
			final String formattedDate = formatter.format(new Date());
			final String documentFolderName = documentType.getIdentifier() + "_" + formattedDate + "_" + cal.get(Calendar.HOUR_OF_DAY)
					+ cal.get(Calendar.SECOND);
			final String tempFdLoc = parentFdPath + File.separator + documentFolderName;
			createDirectory(tempFdLoc);

			documentType = modifyDocumentTypeToExport(documentType);

			final File serializedFile = new File(tempFdLoc + File.separator + documentType.getIdentifier() + SERIALIZATION_EXT);

			try {
				SerializationUtils.serialize(documentType, new FileOutputStream(serializedFile));

				if (isImagemagickFd) {
					final String imFdPath = parentFdPath + File.separator + documentFolderName + File.separator
							+ bsService.getImagemagickBaseFolderName();
					copyDocumentType(imageMagickFdList, documentType, imageMagickFdPath, imFdPath);
				}

				if (isSearchSampleFd) {
					final String ssFolderPath = parentFdPath + File.separator + documentFolderName + File.separator
							+ bsService.getSearchSampleName();
					copyDocumentType(searchFdList, documentType, searchFdPath, ssFolderPath);
				}

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
	 * This API is used to check and copy selected document types .
	 * 
	 * @param DirList {@link String []} directories list.
	 * @param documentType {@link DocumentType} selected document type.
	 * @param srcPath {@link String} source path.
	 * @param destPath {@link String} destination path.
	 * @return
	 */
	private void copyDocumentType(final String[] DirList, final DocumentType documentType, final String srcPath, final String destPath)
			throws IOException {

		final File classificationDir = new File(srcPath);
		for (int i = 0; i < DirList.length; i++) {
			if (FilenameUtils.getName(DirList[i]).equalsIgnoreCase(documentType.getName())) {
				final File destFolder = new File(destPath);

				if (!destFolder.exists()) {
					destFolder.mkdirs();
				}
				FileUtils.copyDirectoryWithContents(new File(classificationDir, DirList[i]), new File(destFolder, DirList[i]));
			}

		}

	}

	/**
	 * This API is used to process request to export the documents .
	 * 
	 * @param docTypeList {@link List<{@link DocumentType}>} selected document type list to export.
	 * @param resp {@link HttpServletResponse}.
	 * @return
	 */
	private void process(final List<DocumentType> docTypeList, final HttpServletResponse resp) throws IOException {

		final BatchSchemaService bSService = this.getSingleBeanOfType(BatchSchemaService.class);
		final String exportSerDirPath = bSService.getBatchExportFolderLocation();
		final BatchClass batchClass = docTypeList.get(0).getBatchClass();

		final String zipFileName = batchClass.getIdentifier() + "_" + "DocumentTypes";
		final String copiedParentFolderPath = exportSerDirPath + File.separator + zipFileName;
		final File copiedFd = createDirectory(copiedParentFolderPath);
		// final boolean isIMFdSelected = checkBaseDirectorySelection("image-classification-sample",
		// bSService.getImagemagickBaseFolderName());
		// final boolean isSSFdSelected = checkBaseDirectorySelection("lucene-search-classification-sample",
		// bSService.getSearchSampleName());
		final boolean isIMFdSelected = true;
		final boolean isSSFdSelected = true;
		processDocumentTypes(docTypeList, batchClass, bSService, isIMFdSelected, isSSFdSelected, resp);
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

		final DocumentTypeService docTypeService = this.getSingleBeanOfType(DocumentTypeService.class);
		final List<DocumentType> docTypeList = new ArrayList<DocumentType>();
		String listOfIdentifier = req.getParameter("identifier");
		for (String identifier : listOfIdentifier.split(";")) {
			if (null != identifier && !identifier.isEmpty()) {
				DocumentType docType = docTypeService.getDocTypeByIdentifier(identifier);
				docTypeList.add(docType);
			}
		}

		if (docTypeList.isEmpty()) {
			log.error("No Document Type selected to export.");
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No Document Type selected to export.");
		} else {
			process(docTypeList, resp);
		}
	}

}
