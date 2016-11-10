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

package com.ephesoft.dcma.core.common;

/**
 * Enum that defines additional optional parameters which a user can store while exporting the data.
 * 
 * @author Ephesoft.
 * 
 */
public enum OptionalExportParameters {

	BATCH_CLASS_ID("BC-ID"), BATCH_INSTANCE_ID("BI-ID"), DOCUMENT_TYPE_NAME("DT-NAME"), BATCH_LEVEL_FIELDS("BLF"), DOCUMENT_TYPE_ID(
			"DT-ID"), BATCH_INSTANCE_NAME("BI-NAME"), BATCH_CLASS_NAME("BC-NAME"), BATCH_CLASS_DESCRIPTION("BC-DESCRIPTION"), BATCH_INSTANCE_DESCRIPTION("BI-DESCRIPTION"),
	/**
	 * Constant to denote multipage tiff.
	 */
	MULTIPAGE_TIFF_PATH("MP-TIFF"),

	/**
	 * Constant to denote multipage pdf.
	 */
	MULTIPAGE_PDF_PATH("MP-PDF");

	/**
	 * Argument which identifies the export parameter;
	 */
	private String parameterName;

	/**
	 * Constructor that initializes the parameters;
	 * 
	 * @param paramterName {@link String} Argument which identifies export the parameter
	 */
	private OptionalExportParameters(final String paramterName) {
		this.parameterName = paramterName;
	}

	/**
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * Gets the optional parameter with its parameter name.
	 * 
	 * @param parameterName {@link String} parameter name which maps the input as an optional parameter=
	 * @return {@link OptionalExportParameters} with the name same as optionalParameter.
	 */
	public static OptionalExportParameters getOptionalExportParameter(final String parameterName) {
		final OptionalExportParameters[] exportParameters = OptionalExportParameters.values();
		OptionalExportParameters parameter = null;
		if (parameterName != null) {
			final String trimmedParameterName = parameterName.trim();
			for (final OptionalExportParameters enumParam : exportParameters) {
				if (trimmedParameterName.equalsIgnoreCase(enumParam.getParameterName())) {
					parameter = enumParam;
					break;
				}
			}
		}
		return parameter;
	}
}
