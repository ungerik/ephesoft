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

package com.ephesoft.dcma.core.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This interface is to provide the service to unzip a .zip file.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 13-Apr-2015 <br/>
 * @version 1.0 <br/>
 *          $LastChangedDate: 2015-04-14 12:46:10 +0530 (Tue, 14 Apr 2015) $ <br/>
 *          $LastChangedRevision: 21597 $ <br/>
 */
public interface ZipService {

	/**
	 * This API unzips files in specified destination directory. Destination directory is created if it doesn't exist. If destination
	 * directory passed is null, zip will be extracted in its parent directory.
	 * 
	 * @param zipFile {@link File} Zip File to be unzipped.
	 * @param destinationDir {@link String} Directory where file is to be unzipped.
	 * @throws IOException {@link IOException}
	 * @throws FileNotFoundException {@link FileNotFoundException}
	 */
	void unzipFiles(File zipFile, String destinationDir) throws FileNotFoundException, IOException;
}
