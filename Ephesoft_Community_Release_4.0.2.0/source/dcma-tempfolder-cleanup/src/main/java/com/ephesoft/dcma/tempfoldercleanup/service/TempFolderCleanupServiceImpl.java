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

package com.ephesoft.dcma.tempfoldercleanup.service;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.tempfoldercleanup.constant.TempFolderCleanupConstants;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;

public class TempFolderCleanupServiceImpl implements TempFolderCleanupService {

	/** The batch schema service. */
	@Autowired
	private BatchSchemaService bsService;

	/**
	 * Constant for age limit for filtering the files
	 */
	private int fileAge;

	public void setfileAge(int fileAge) {
		this.fileAge = fileAge;
	}

	/**
	 * Constant for folder names where files are to be deleted
	 */
	private String folders;

	public void setFolders(String folders) {
		this.folders = folders;
	}

	private final static Logger LOGGER = LoggerFactory.getLogger(TempFolderCleanupServiceImpl.class);

	@Override
	public void initiateTempFolderDeletion() {
		LOGGER.info("Initiate deletion of files from the temporary directories");
		String sharedFolderPath = bsService.getBaseFolderLocation();
		if (fileAge >= TempFolderCleanupConstants.MIN_CLEANUP_DURATION) {
			if (EphesoftStringUtil.isNullOrEmpty(folders)) {
				LOGGER.info(TempFolderCleanupConstants.EMPTY_PARAMETER_MESSAGE);
			}
			else
			{
				long cutoff = System.currentTimeMillis() - (fileAge * TempFolderCleanupConstants.MINUITES * TempFolderCleanupConstants.MINUITES * TempFolderCleanupConstants.MILLISECONDS);
				String folderArray[] = EphesoftStringUtil.splitString(folders, TempFolderCleanupConstants.SEMI_COLON);
				for (String folder : folderArray) {
					if (!folder.isEmpty()) {
						File file = new File(EphesoftStringUtil.concatenate(sharedFolderPath, File.separator, folder));
						if (file.exists()) {
							FileUtils.deleteAgedFile(file, cutoff, true);
						}
					}
				}
			}
		} else {
			LOGGER.info(TempFolderCleanupConstants.CLEANUP_ERROR_MESSAGE);
		}
	}


}
