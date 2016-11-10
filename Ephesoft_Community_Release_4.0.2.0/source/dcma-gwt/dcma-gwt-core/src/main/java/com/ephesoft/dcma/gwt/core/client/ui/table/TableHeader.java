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

package com.ephesoft.dcma.gwt.core.client.ui.table;

import java.util.LinkedList;

import com.ephesoft.dcma.core.common.DomainProperty;

public class TableHeader {

	private LinkedList<HeaderColumn> headerColumns = new LinkedList<HeaderColumn>();

	/**
	 * headerColumnWidth int.
	 */
	private static final int HEADER_COLUMN_WIDTH = 5;

	public void addHeaderColumn(final HeaderColumn column) {
		headerColumns.add(column);
	}

	/**
	 * Returns the header column based on the selection handler paased.
	 * 
	 * @param selectionHandler {@link SelectionHandlers} The type of selection handler.
	 * @return {@link LinkedList} <{@link HeaderColumn}> The list of header columns.
	 */
	public LinkedList<HeaderColumn> getHeaderColumns(final SelectionHandlers selectionHandler) {
		boolean columnToAdd = true;

		if (selectionHandler == SelectionHandlers.RADIOBUTTONS) {
			for (HeaderColumn column : headerColumns) {
				if (column.getName().equalsIgnoreCase("radio")) {
					columnToAdd = false;
				}
			}
			if (columnToAdd) {
				HeaderColumn radioButton = new HeaderColumn(0, "radio", HEADER_COLUMN_WIDTH);
				headerColumns.add(0, radioButton);
			}

		} else if (selectionHandler == SelectionHandlers.CHECKBOX) {
			for (HeaderColumn column : headerColumns) {
				if (column.getName().equalsIgnoreCase("checkBox")) {
					columnToAdd = false;
				}
			}
			if (columnToAdd) {
				HeaderColumn checkBox = new HeaderColumn(0, "checkBox", HEADER_COLUMN_WIDTH);
				headerColumns.add(0, checkBox);
			}
		}
		return headerColumns;
	}

	public HeaderColumn[] getHeaderColumns() {
		return headerColumns.toArray(new HeaderColumn[headerColumns.size()]);
	}

	public static class HeaderColumn {

		int index;
		String name;
		int width;
		boolean isSortable;
		boolean isPrimaryAsc = true;
		DomainProperty domainProperty;

		public HeaderColumn(int index, String name, int width) {
			this(index, name, width, false, null);
		}

		public HeaderColumn(int index, String name, int width, boolean isSortable, DomainProperty domainProperty) {
			this.index = index;
			this.name = name;
			this.width = width;
			this.isSortable = isSortable;
			this.domainProperty = domainProperty;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + index;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			HeaderColumn other = (HeaderColumn) obj;
			if (index != other.index)
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		public int getIndex() {
			return index;
		}

		public String getName() {
			return name;
		}

		public int getWidth() {
			return width;
		}

		public DomainProperty getDomainProperty() {
			return domainProperty;
		}

		public boolean isPrimaryAsc() {
			return isPrimaryAsc;
		}

		public void setPrimaryAsc(boolean isPrimaryAsc) {
			this.isPrimaryAsc = isPrimaryAsc;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setSortable(boolean isSortable) {
			this.isSortable = isSortable;
		}

		public boolean isSortable() {
			return isSortable;
		}
	}
}
