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

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.core.client.ui.widget.property.ModelFormatter;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.user.client.ui.FlexTable;

public class CellGrid<T> extends FlexTable {

	private final List<String> headersList;

	private final ModelFormatter<T> modelFormatter;
	
	

	public CellGrid(final List<String> headersList, final ModelFormatter<T> modelFormatter) {
		if (null == modelFormatter) {
			throw new UnsupportedOperationException("Model formatter must not be null for initializing cell grid.");
		}
		this.headersList = headersList == null ? new ArrayList<String>() : headersList;
		this.modelFormatter = modelFormatter;
	}

	public void addRow(final List<T> dataToAdd) {
		if (!CollectionUtil.isEmpty(dataToAdd)) {
			String headerKey;
			String value;
			SuggestionCell suggestionCell;
			final int row = getRowCount();
			for (final T model : dataToAdd) {
				headerKey = modelFormatter.getKey(model);
				final int index = headersList.indexOf(headerKey);
				if (index != -1) {
					value = modelFormatter.getValue(model);
					suggestionCell = new SuggestionCell(value);
					this.setWidget(row, index, suggestionCell);
				}
			}
		}
	}

	public static class SuggestionCell extends ComboBox {

		public SuggestionCell(final String value) {
			setValue(value, true, true);
			addStyleName("gridCell");
		}
	}
}
