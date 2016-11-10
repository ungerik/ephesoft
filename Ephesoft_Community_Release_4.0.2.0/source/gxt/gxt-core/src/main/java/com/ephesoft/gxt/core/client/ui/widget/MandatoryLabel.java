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

package com.ephesoft.gxt.core.client.ui.widget;

import com.ephesoft.gxt.core.client.ui.widget.constant.StyleConstant;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.HTML;

/**
 * A widget that represents a Mandatory field . The <code>MandatoryLabel</code> class provides us a custom label widget to represent
 * the mandatory fields.
 * 
 * It concates a read colored star after the label text to indicate that corresponding field is mandatory.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class MandatoryLabel extends HTML {

	/**
	 * Constant {@link String} for specifying label separator and its appearance.
	 */
	private static final String MANDATORY_FIELD_SEPERATOR = StringUtil.concatenate(StyleConstant.FIELD_SEPERATOR,
			StyleConstant.HTML_FONT_COLOR_TAG, StyleConstant.MANDATORY_STAR, StyleConstant.HTML_END_FONT_TAG);

	/**
	 * Constructor.
	 */
	public MandatoryLabel(){
		super();
	}
	
	public void setLabelText(final String value){
		this.setHTML(value.concat(MANDATORY_FIELD_SEPERATOR));
	}
}
