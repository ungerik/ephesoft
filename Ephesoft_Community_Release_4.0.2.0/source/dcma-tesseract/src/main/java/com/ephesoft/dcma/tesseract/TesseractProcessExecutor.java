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

package com.ephesoft.dcma.tesseract;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.core.TesseractVersionProperty;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.EphesoftProcessExecutor;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.FileNameFormatter;
import com.ephesoft.dcma.util.OSUtil;

/**
 * Class to execute and a control a tesseract process. This class provides proper retry mechanism if process fails to execute and wait
 * time property which defines maximum time to wait for a process to execute completely. If process can't execute within wait time then
 * process will be terminated and process will be re run by executor.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jun 20, 2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class TesseractProcessExecutor implements ICommonConstants {

	final private static String TESSERACT_BASE_PATH_NOT_CONFIGURED = "Tesseract Base path not configured.";
	private final static String TESSERACT_EXECUTOR_PATH = "TESSERACT_EXECUTOR_PATH";

	/**
	 * Constant for environment variable TESSERACT_HOME which contains the path to tesseract ocr engine
	 */
	private final static String TESSERACT_OCR_PATH = "TESSERACT_HOME";

	final private String fileName;
	final private Batch batch;
	final private String batchInstanceID;
	final private List<String> cmdList;
	final private String actualFolderLocation;
	final private String cmdLanguage;
	final private BatchInstanceThread thread;
	private String targetHOCR;
	final private String tesseractVersion;
	final private String colorSwitch;
	final private String unixCmd;
	final private String windowsCmd;
	final private String overwriteHOCR;
	final private String cmdParams;
	final private String outputFolderLocation;

	private static final Logger LOGGER = LoggerFactory.getLogger(TesseractProcessExecutor.class);

	public TesseractProcessExecutor(String fileName, Batch batch, String batchInstanceID, String actualFolderLocation,
			String cmdLanguage, BatchInstanceThread thread, String tesseractVersion, String colorSwitch, String windowsCmd,
			String unixCmd, String overwriteHOCR, String cmdParams) throws DCMAApplicationException {
		this.fileName = fileName;
		this.batch = batch;
		this.batchInstanceID = batchInstanceID;
		this.cmdList = new ArrayList<String>();
		this.actualFolderLocation = actualFolderLocation;
		this.cmdLanguage = cmdLanguage;
		this.thread = thread;
		this.tesseractVersion = tesseractVersion;
		this.colorSwitch = colorSwitch;
		this.windowsCmd = windowsCmd;
		this.unixCmd = unixCmd;
		this.overwriteHOCR = overwriteHOCR;
		this.cmdParams = cmdParams;
		this.outputFolderLocation = actualFolderLocation;
		run();
	}

	public TesseractProcessExecutor(String fileName, String actualFolderLocation, String cmdLanguage, BatchInstanceThread thread,
			String tesseractVersion, String colorSwitch, String windowsCmd, String unixCmd, String cmdParams,
			String outputFolderLocation) throws DCMAApplicationException {
		this.fileName = fileName;
		this.batch = null;
		this.batchInstanceID = null;
		this.cmdList = new ArrayList<String>();
		this.actualFolderLocation = actualFolderLocation;
		this.cmdLanguage = cmdLanguage;
		this.thread = thread;
		this.tesseractVersion = tesseractVersion;
		this.colorSwitch = colorSwitch;
		this.windowsCmd = windowsCmd;
		this.unixCmd = unixCmd;
		this.overwriteHOCR = "true";
		this.cmdParams = cmdParams;
		this.outputFolderLocation = outputFolderLocation;
		createOCR();
	}

	public final void run() throws DCMAApplicationException {
		List<String> cmdListLocal = new ArrayList<String>();
		cmdListLocal.addAll(this.cmdList);
		String tesseractBasePath = getTesseractBasePath();

		// process each image file to generate HOCR files
		String targetDirectoryName = fileName.substring(0, fileName.indexOf('.'));
		String targetHOCR = "";
		String oldFileName = "";
		String ocrInputFileName = "";
		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		String pageId = "";
		for (int i = 0; i < xmlDocuments.size(); i++) {
			Document document = xmlDocuments.get(i);
			List<Page> listOfPages = document.getPages().getPage();
			for (int j = 0; j < listOfPages.size(); j++) {
				Page page = listOfPages.get(j);
				String sImageFile;
				if ("ON".equals(colorSwitch)) {
					sImageFile = page.getOCRInputFileName();
				} else {
					sImageFile = page.getNewFileName();
				}
				if (fileName.equalsIgnoreCase(sImageFile)) {
					oldFileName = page.getOldFileName();
					ocrInputFileName = page.getOCRInputFileName();
					pageId = page.getIdentifier();
				}
			}
		}
		try {
			FileNameFormatter fileFormatter = new FileNameFormatter();
			if (tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
				/*
				 * # Changed due to Tesseract 3.0: # Sending an empty string in the extension part which Tesseract automatically does #
				 * to the name we give to the file
				 */
				targetHOCR = fileFormatter.getHocrFileName(String.valueOf(batchInstanceID), oldFileName, fileName, ocrInputFileName,
						"", pageId, false);
			} else {
				targetHOCR = fileFormatter.getHocrFileName(String.valueOf(batchInstanceID), oldFileName, fileName, ocrInputFileName,
						FileType.HTML.getExtensionWithDot(), pageId, false);
			}
		} catch (Exception e1) {
			LOGGER.error("Exception retrieving the name of HOCR file" + e1.getMessage(), e1);
			throw new DCMAApplicationException("Exception retrieving the name of HOCR file" + e1.getMessage(), e1);
		}
		LOGGER.info("image file name is: " + fileName);
		LOGGER.info("Target directory name is: " + targetDirectoryName);
		LOGGER.info("Target HOCR name is: " + targetHOCR);

		boolean isHOCRExists = false;

		LOGGER.info("Overwrite HOCR is :" + this.overwriteHOCR);

		if (!"true".equalsIgnoreCase(this.overwriteHOCR)) {
			File fPageFolder = new File(actualFolderLocation);
			String[] listOfHtmlFiles = fPageFolder.list(new CustomFileFilter(false, FileType.HTML.getExtensionWithDot()));
			isHOCRExists = checkHocrExists(targetHOCR, listOfHtmlFiles);
		}

		LOGGER.info("Is HOCR existing for page" + pageId + "is" + isHOCRExists);

		if (!isHOCRExists) {
			try {
				if (tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
					creatingTesseractVersion3Command(cmdListLocal, targetHOCR, cmdParams);
				} else {
					creatingTesseractVersion2Command(cmdListLocal, targetHOCR);
				}
				String[] cmds = new String[cmdListLocal.size()];
				for (int i = 0; i < cmdListLocal.size(); i++) {
					if (cmdListLocal.get(i).contains("cmd")) {
						LOGGER.info("inside cmd");
						cmds[i] = cmdListLocal.get(i);
					} else if (cmdListLocal.get(i).contains("/c")) {
						LOGGER.info("inside /c");
						cmds[i] = cmdListLocal.get(i);
					} else {
						LOGGER.info("inside Tesseract");
						cmds[i] = cmdListLocal.get(i);
					}
				}
				LOGGER.info("command formed is :");
				for (String cmd : cmds) {
					LOGGER.info(cmd);
				}
				LOGGER.info("command formed Ends.");
				if (OSUtil.isUnix()) {
					// Replacing ProcessExecutor with EphesoftProcessExecutor
					thread.add(new EphesoftProcessExecutor(cmds, new File(tesseractBasePath), ApplicationConfigProperties
							.getWaitTimeProperty(TESSERACT_WAIT_TIME_PROPERTY)));
				} else if (OSUtil.isWindows()) {
					// Replacing ProcessExecutor with EphesoftProcessExecutor
					thread.add(new EphesoftProcessExecutor(cmds, ApplicationConfigProperties
							.getWaitTimeProperty(TESSERACT_WAIT_TIME_PROPERTY)));
				}
			} catch (Exception e) {
				LOGGER.error("Exception while generating HOCR for image" + fileName + e.getMessage());
				throw new DCMAApplicationException("Exception while generating HOCR for image" + fileName + e.getMessage(), e);
			}
		}
		if (tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
			// Appending the .html extension to the file(since Tesseract 3.0 appends it automatically)
			this.targetHOCR = targetHOCR + FileType.HTML.getExtensionWithDot();
		} else {
			this.targetHOCR = targetHOCR;
		}

	}

	public final void createOCR() throws DCMAApplicationException {
		List<String> cmdListLocal = new ArrayList<String>();
		cmdListLocal.addAll(this.cmdList);
		String tesseractBasePath = getTesseractBasePath();
		if (tesseractBasePath == null) {
			LOGGER.error(TESSERACT_BASE_PATH_NOT_CONFIGURED);
			throw new DCMAApplicationException(TESSERACT_BASE_PATH_NOT_CONFIGURED);
		}

		String targetHOCR = "";
		targetHOCR = fileName.substring(0, fileName.lastIndexOf("."));

		LOGGER.info("image file name is: " + fileName);
		LOGGER.info("Target HOCR name is: " + targetHOCR);

		LOGGER.info("Overwrite HOCR is :" + this.overwriteHOCR);

		try {
			if (tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
				creatingTesseractVersion3Command(cmdListLocal, targetHOCR, cmdParams);
			} else {
				creatingTesseractVersion2Command(cmdListLocal, targetHOCR);
			}
			String[] cmds = new String[cmdListLocal.size()];
			for (int i = 0; i < cmdListLocal.size(); i++) {
				if (cmdListLocal.get(i).contains("cmd")) {
					LOGGER.info("inside cmd");
					cmds[i] = cmdListLocal.get(i);
				} else if (cmdListLocal.get(i).contains("/c")) {
					LOGGER.info("inside /c");
					cmds[i] = cmdListLocal.get(i);
				} else {
					LOGGER.info("inside Tesseract");
					cmds[i] = cmdListLocal.get(i);
				}
			}
			LOGGER.info("command formed is :");
			for (String cmd : cmds) {
				LOGGER.info(cmd);
			}
			LOGGER.info("command formed Ends.");
			if (OSUtil.isUnix()) {
				// Replacing ProcessExecutor with EphesoftProcessExecutor
				thread.add(new EphesoftProcessExecutor(cmds, new File(tesseractBasePath), ApplicationConfigProperties
						.getWaitTimeProperty(TESSERACT_WAIT_TIME_PROPERTY)));
			} else if (OSUtil.isWindows()) {
				// Replacing ProcessExecutor with EphesoftProcessExecutor
				thread.add(new EphesoftProcessExecutor(cmds, ApplicationConfigProperties
						.getWaitTimeProperty(TESSERACT_WAIT_TIME_PROPERTY)));
			}
		} catch (Exception e) {
			LOGGER.error("Exception while generating HOCR for image" + fileName + e.getMessage());
			throw new DCMAApplicationException("Exception while generating HOCR for image" + fileName + e.getMessage(), e);
		}

		if (tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
			// Appending the .html extension to the file(since Tesseract 3.0 appends it automatically)
			this.targetHOCR = targetHOCR + FileType.HTML.getExtensionWithDot();
		} else {
			this.targetHOCR = targetHOCR;
		}

	}

	private void creatingTesseractVersion2Command(List<String> cmdListLocal, String targetHOCR) throws DCMAApplicationException {
		cmdListLocal.add("\"" + System.getenv(TESSERACT_EXECUTOR_PATH) + File.separator + "TesseractExecutor.exe" + "\"");
		cmdListLocal.add("\"" + getTesseractBasePath() + File.separator + windowsCmd + "\"");
		cmdListLocal.add("\"" + actualFolderLocation + File.separator + fileName + "\"");
		cmdListLocal.add(cmdLanguage);
		cmdListLocal.add(">");
		cmdListLocal.add("\"" + outputFolderLocation + File.separator + targetHOCR + "\"");
	}

	private void creatingTesseractVersion3Command(List<String> cmdListLocal, String targetHOCR, String cmdParams)
			throws DCMAApplicationException {
		if (OSUtil.isWindows()) {
			cmdListLocal.add("\"" + System.getenv(TESSERACT_EXECUTOR_PATH) + File.separator + "TesseractExecutor.exe" + "\"");
			cmdListLocal.add("\"" + getTesseractBasePath() + File.separator + windowsCmd + "\"");
			cmdListLocal.add("\"" + actualFolderLocation + File.separator + fileName + "\"");
			cmdListLocal.add("\"" + outputFolderLocation + File.separator + targetHOCR + "\"");
			cmdListLocal.add("\"-l");
			cmdListLocal.add(cmdLanguage);
			if (cmdParams != null && !cmdParams.trim().isEmpty()) {
				String cmdParamsArray[] = cmdParams.split(" ");
				for (String param : cmdParamsArray) {
					cmdListLocal.add(param);
				}
			}
			cmdListLocal.add("\"");
			cmdListLocal.add("+" + "\"" + getTesseractBasePath() + File.separator + "hocr.txt\"");
		} else {
			cmdListLocal.add(unixCmd);
			cmdListLocal.add(actualFolderLocation + File.separator + fileName);
			cmdListLocal.add(outputFolderLocation + File.separator + targetHOCR);
			cmdListLocal.add("-l");
			cmdListLocal.add(cmdLanguage);
			if (cmdParams != null && !cmdParams.trim().isEmpty()) {
				String cmdParamsArray[] = cmdParams.split(" ");
				for (String param : cmdParamsArray) {
					cmdListLocal.add(param);
				}
			}
			cmdListLocal.add("+hocr.txt");
		}
	}

	private String getTesseractBasePath() throws DCMAApplicationException {

		// Getting the tesseract ocr directory from environment variables.
		String tesseractBasePath = System.getenv(TESSERACT_OCR_PATH);
		if (tesseractBasePath == null) {
			LOGGER.error(TESSERACT_BASE_PATH_NOT_CONFIGURED);
			throw new DCMAApplicationException(TESSERACT_BASE_PATH_NOT_CONFIGURED);
		}
		return tesseractBasePath;
	}

	public String getTargetHOCR() {
		return targetHOCR;
	}

	public String getFileName() {
		return fileName;
	}

	public String getActualFolderLocation() {
		return actualFolderLocation;
	}

	private boolean checkHocrExists(String hocrName, final String[] listOfFiles) {
		boolean returnValue = false;
		String localHOCRFileName = hocrName + FileType.HTML.getExtensionWithDot();
		if (listOfFiles != null && listOfFiles.length > 0 && localHOCRFileName != null) {
			for (String eachFile : listOfFiles) {
				if (eachFile.equalsIgnoreCase(localHOCRFileName)) {
					returnValue = true;
				}
			}
		}
		return returnValue;
	}
}
