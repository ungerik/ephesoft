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

package com.ephesoft.dcma.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.transform.stream.StreamResult;

/**
 * Creates the stream with the file and stores the reference to the {@link File} with which the stream is created. Java doesnot support
 * extracting the name of the file from the Stream. Its a wrapper to the {@link StreamResult} which stores the reference to the file
 * with which the stream is created.
 * 
 * @author Ephesoft.
 * @version 1.0.
 */
public class FileStreamResult extends StreamResult {

	/**
	 * Reference to the {@link File} with which the stream is actually created.
	 */
	private File streamedFile;

	/**
	 * Creates a {@link FileStreamResult} with the <code>streamedFile</code>. Also stores the reference to the file with which the
	 * reference was created.
	 * 
	 * @param streamedFile {@link File} which which the stream was created.
	 * @throws FileNotFoundException When the file which the stream needs to be created doesnot exist.
	 */
	public FileStreamResult(File streamedFile, final OutputStream outputStream) throws FileNotFoundException {
		super(outputStream);
		this.streamedFile = streamedFile;
	}

	/**
	 * @return the streamedFile {@link File} with which the stream was actually created.
	 */
	public File getStreamedFile() {
		return streamedFile;
	}

}
