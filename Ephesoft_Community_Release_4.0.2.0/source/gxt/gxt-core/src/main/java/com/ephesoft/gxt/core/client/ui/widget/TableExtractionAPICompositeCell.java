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

import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.TableExtractionAPIModel;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.TextCell;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;

public class TableExtractionAPICompositeCell<T extends TableExtractionAPIModel> {

	private CompositeCell<T> compositeCell;

	public CompositeCell<T> getCompositeCell() {
		return compositeCell;
	}

	public CompositeCell<T> createCompositeCell() {

		final ListStore<String> andOrOpearators = new ListStore<String>(new ModelKeyProvider<String>() {

			@Override
			public String getKey(String item) {

				if (StringUtil.isNullOrEmpty(item)) {
					item = CoreCommonConstant.OR_OPERATOR;
				}
				return item;
			}
		});
		andOrOpearators.add(CoreCommonConstant.OR_OPERATOR);
		andOrOpearators.add(CoreCommonConstant.AND_OPERATOR);

		final HasCell<T, String> coordHeaderCombo = new HasCell<T, String>() {

			public ComboBoxCell<String> getCell() {
				ComboBoxCell<String> coordHeaderComboCell = new ComboBoxCell<String>(andOrOpearators, new LabelProvider<String>() {

					@Override
					public String getLabel(String item) {
						if (StringUtil.isNullOrEmpty(item)) {
							item = CoreCommonConstant.OR_OPERATOR;
						}
						return item;
					}
				});
				coordHeaderComboCell.setTriggerAction(TriggerAction.ALL);
				coordHeaderComboCell.setForceSelection(true);
				coordHeaderComboCell.setWidth(60);
				return coordHeaderComboCell;
			}

			public FieldUpdater<T, String> getFieldUpdater() {
				return new FieldUpdater<T, String>() {

					@Override
					public void update(int index, T object, String value) {
						// TODO Auto-generated method stub
						if (object != null && value != null) {
							object.setCoordinateHeaderComb(value);
							;
						}
					}
				};
			}

			public String getValue(T object) {
				if (object != null && !StringUtil.isNullOrEmpty(object.getCoordinateHeaderComb())) {
					return object.getCoordinateHeaderComb();
				}
				return CoreCommonConstant.OR_OPERATOR;
			}
		};

		final HasCell<T, Boolean> colCoordCell = new HasCell<T, Boolean>() {

			public CheckBoxCell getCell() {
				return new CheckBoxCell();
			}

			public FieldUpdater<T, Boolean> getFieldUpdater() {
				return new FieldUpdater<T, Boolean>() {

					@Override
					public void update(int index, T object, Boolean value) {
						// TODO Auto-generated method stub
						if (object != null && value != null) {
							object.setColumnCoordinates(value);
						}
					}
				};
			}

			public Boolean getValue(T object) {
				boolean chkValue = false;
				if (object != null) {
					chkValue = object.isColumnCoordinates();
				}
				return chkValue;
			}
		};

		HasCell<T, String> colCoordLabel = new HasCell<T, String>() {

			public Cell<String> getCell() {
				return new TextCell();
			}

			public FieldUpdater<T, String> getFieldUpdater() {
				return null;
			}

			public String getValue(T object) {
				return LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_COORDINATES);
			}
		};

		final HasCell<T, Boolean> colHeaderCell = new HasCell<T, Boolean>() {

			public CheckBoxCell getCell() {
				return new CheckBoxCell();
			}

			public FieldUpdater<T, Boolean> getFieldUpdater() {
				return new FieldUpdater<T, Boolean>() {

					@Override
					public void update(int index, T object, Boolean value) {
						// TODO Auto-generated method stub
						if (object != null && value != null) {
							object.setColumnHeader(value);
						}
					}
				};
			}

			public Boolean getValue(T object) {
				if (object != null) {
					return object.isColumnHeader();
				}
				return false;
			}
		};

		HasCell<T, String> colHeaderLabel = new HasCell<T, String>() {

			public Cell<String> getCell() {
				return new TextCell();
			}

			public FieldUpdater<T, String> getFieldUpdater() {
				return null;
			}

			public String getValue(T object) {
				return LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_HEADER);
			}
		};

		final HasCell<T, String> headerRegexCombo = new HasCell<T, String>() {

			public ComboBoxCell<String> getCell() {
				ComboBoxCell<String> headerRegexComboCell = new ComboBoxCell<String>(andOrOpearators, new LabelProvider<String>() {

					@Override
					public String getLabel(String item) {
						if (StringUtil.isNullOrEmpty(item)) {
							item = CoreCommonConstant.OR_OPERATOR;
						}
						return item;
					}
				});
				headerRegexComboCell.setTriggerAction(TriggerAction.ALL);
				headerRegexComboCell.setForceSelection(true);
				headerRegexComboCell.setWidth(60);
				return headerRegexComboCell;
			}

			public FieldUpdater<T, String> getFieldUpdater() {
				return new FieldUpdater<T, String>() {

					@Override
					public void update(int index, T object, String value) {
						// TODO Auto-generated method stub
						if (object != null && value != null) {
							object.setHeaderRegexComb(value);
						}
					}
				};
			}

			public String getValue(T object) {
				if (object != null && !StringUtil.isNullOrEmpty(object.getHeaderRegexComb())) {
					return object.getHeaderRegexComb();
				}
				return CoreCommonConstant.OR_OPERATOR;
			}
		};

		HasCell<T, Boolean> regexExtCell = new HasCell<T, Boolean>() {

			public CheckBoxCell getCell() {
				CheckBoxCell checkBox = new CheckBoxCell();
				return checkBox;
			}

			public FieldUpdater<T, Boolean> getFieldUpdater() {
				return new FieldUpdater<T, Boolean>() {

					@Override
					public void update(int index, TableExtractionAPIModel object, Boolean value) {
						// TODO Auto-generated method stub
						if (object != null && value != null) {
							object.setRegexExtraction(value);
						}
					}
				};
			}

			public Boolean getValue(T object) {
				if (object != null) {
					return object.isRegexExtraction();
				}
				return false;
			}
		};

		HasCell<T, String> regexExtLabel = new HasCell<T, String>() {

			public Cell<String> getCell() {
				return new TextCell();
			}

			public FieldUpdater<T, String> getFieldUpdater() {
				return null;
			}

			public String getValue(T object) {
				return LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_REGEX_EXTRACTION);
			}
		};

		List<HasCell<T, ?>> cells = new ArrayList<HasCell<T, ?>>();
		cells.add(colCoordCell);
		cells.add(colCoordLabel);
		cells.add(coordHeaderCombo);
		cells.add(colHeaderCell);
		cells.add(colHeaderLabel);
		cells.add(headerRegexCombo);
		cells.add(regexExtCell);
		cells.add(regexExtLabel);
		CompositeCell<T> compositeCell = new CompositeCell<T>(cells);
		return compositeCell;
	}

}
