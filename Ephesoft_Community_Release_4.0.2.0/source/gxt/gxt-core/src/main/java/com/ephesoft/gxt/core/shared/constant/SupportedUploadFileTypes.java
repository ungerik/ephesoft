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

package com.ephesoft.gxt.core.shared.constant;

import com.ephesoft.gxt.core.shared.util.StringUtil;

/**
 * Enum to specify the list of supported file format of Office files which can be converted into PDF files using Open Office. The valid
 * extensions specified by the user in folder monitor property files are validated with this list and then conversion is done only on
 * verified file formats.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public enum SupportedUploadFileTypes {

	TIFF("TIFF"), TIF("TIF"), PDF("PDF"), ODT("ODT"), ODS("ODS"), ODP("ODP"), ODG("ODG"), ODF("ODF"), SXW("SXW"), SXI("SXI"), 
	SXC("SXC"), SXD("SXD"), HTML("HTML"), HTM("HTM"), XLS("XLS"), XLSX("XLSX"), PPT("PPT"), PPTX("PPTX"), DOC("DOC"),
	DOCX("DOCX"), RTF("RTF"), TXT("TXT"), WPD("WPD"), PSD("PSD"), BMP("BMP"), GIF("GIF"), JPEG("JPEG"), JPG("JPG"), PNG("PNG");

	/**
	 * Valid file extension.
	 */
	private String extension;

	/**
	 * Constructor.
	 */
	SupportedUploadFileTypes(final String extension) {
		this.extension = extension;
	}

	/**
	 * Gets the extension of a particular file format.
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Checks whether the specified extension is supported or not.
	 * 
	 * @param extention {@link String} File extension.
	 * @return true if the extension is supported. Else false.
	 */
	public static boolean isFileSupported(final String extention) {
		boolean validFormat = false;
		if (!StringUtil.isNullOrEmpty(extention)) {
			final SupportedUploadFileTypes[] allSupportedFormats = SupportedUploadFileTypes.values();
			for (final SupportedUploadFileTypes supportedFormat : allSupportedFormats) {
				if (extention.equalsIgnoreCase(supportedFormat.getExtension())) {
					validFormat = true;
					break;
				}
			}
		}
		return validFormat;
	}
}
