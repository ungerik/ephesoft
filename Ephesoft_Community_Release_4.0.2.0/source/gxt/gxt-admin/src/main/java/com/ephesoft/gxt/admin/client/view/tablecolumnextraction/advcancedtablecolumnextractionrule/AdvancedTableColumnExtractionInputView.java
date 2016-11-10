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

package com.ephesoft.gxt.admin.client.view.tablecolumnextraction.advcancedtablecolumnextractionrule;

import java.util.List;

import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.presenter.tablecolumnextraction.advancedtablecolumnextractionrule.AdvancedTableColumnExtractionInputPresenter;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.ui.widget.MandatoryLabel;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.FileType;
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

public class AdvancedTableColumnExtractionInputView extends
		AdvancedTableColumnExtractionInlineView<AdvancedTableColumnExtractionInputPresenter> {

	interface Binder extends UiBinder<Widget, AdvancedTableColumnExtractionInputView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	@UiField
	protected FramedPanel advExtrRuleFramedPanel;
	
	@UiField
	protected VerticalLayoutContainer advTableColumnExtractionInputPanel;

	@UiField
	protected FieldLabel tableColumnLabel;
	@UiField
	protected ComboBox tableColumnValue;
	@UiField
	protected FieldLabel imageNameLabel;
	@UiField
	protected Label imageName;
	@UiField
	protected MandatoryLabel startCoordinateLabel;
	@UiField
	protected Label startCoordinate;
	@UiField
	protected MandatoryLabel endCoordinateLabel;
	@UiField
	protected Label endCoordinate;

	public AdvancedTableColumnExtractionInputView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		setTextAndCSS();
		setWidgetIDs();
		addComboBoxHandlers();
		this.tableColumnValue.setEditable(false);
	}

	private void setTextAndCSS() {
		tableColumnLabel.setText("Choose Table Column");
		imageNameLabel.setText("Image Name");
		startCoordinateLabel.setLabelText("Start Coordinate");
		endCoordinateLabel.setLabelText("End Coordinate");

		advExtrRuleFramedPanel.addStyleName("advFramedPanel");
		advTableColumnExtractionInputPanel.addStyleName("advTCERInputPanel");
		tableColumnLabel.addStyleName("advTCERInputlabel");
		imageNameLabel.addStyleName("advTCERInputlabel");
		startCoordinateLabel.addStyleName("advTCERInputlabel");
		endCoordinateLabel.addStyleName("advTCERInputlabel");

		tableColumnValue.addStyleName("advTCERInputField");
		imageName.addStyleName("advTCERInputField");
		startCoordinate.addStyleName("advTCERInputField");
		endCoordinate.addStyleName("advTCERInputField");

	}

	private void setWidgetIDs() {
		WidgetUtil.setID(advTableColumnExtractionInputPanel, "advTableColumnExtractionInputPanel");
		WidgetUtil.setID(tableColumnLabel, "tableColumnLabel");
		WidgetUtil.setID(tableColumnValue, "tableColumnValue");
		WidgetUtil.setID(imageNameLabel, "imageNameLabel");
		WidgetUtil.setID(imageName, "imageName");
		WidgetUtil.setID(startCoordinateLabel, "startCoordinateLabel");
		WidgetUtil.setID(startCoordinate, "startCoordinate");
		WidgetUtil.setID(endCoordinateLabel, "endCoordinateLabel");
		WidgetUtil.setID(endCoordinate, "endCoordinate");
	}

	private void addComboBoxHandlers() {
		this.tableColumnValue.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				int index = tableColumnValue.getSelectedIndex();
				if (-1 != index) {
					presenter.setSelectedTableColumnExtractionRuleDTO(tableColumnValue.getStore().get(index));
				}
			}
		});
	}

	public void setColStartCoord(String columnStartCoordinate) {
		startCoordinate.setText(columnStartCoordinate);
	}

	public void setColEndCoord(String columnEndCoordinate) {
		endCoordinate.setText(columnEndCoordinate);
	}

	public void setImageName(String imageName) {
		if(imageName.equals(AdminConstants.EMPTY_STRING)){
			//Do Nothing
		}
		else if(imageName.contains(FileType.PDF.getExtension())){
			imageName=getActualFileName(imageName, FileType.PDF.getExtensionWithDot());
		}else{
			imageName=getActualFileName(imageName, FileType.TIF.getExtensionWithDot());
		}
		this.imageName.setText(imageName);
	}
	
	String getActualFileName(String str, String append){
		int index=str.indexOf(CoreCommonConstants.DOT);
		String appendedFileName=str.substring(0, index);
		appendedFileName=appendedFileName.concat(append);
		return appendedFileName;
	}
	
	
	public void setTableColumnExtractionRuleList(final TableExtractionRuleDTO tableExtractionRuleDTO) {
		if (tableExtractionRuleDTO != null) {
			List<TableColumnExtractionRuleDTO> tableColumnInfoList = tableExtractionRuleDTO.getTableColumnExtractionRuleDTOs(false);
			if (!CollectionUtil.isEmpty(tableColumnInfoList)) {
				this.tableColumnValue.getStore().clear();
				presenter.clearColumnDTOMap();
				for (TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO : tableColumnInfoList) {
					String columnName = tableColumnExtractionRuleDTO.getColumnName();
					String columnIdentifier = tableColumnExtractionRuleDTO.getIdentifier();
					if (columnName != null && !columnName.isEmpty() && columnIdentifier != null && !columnIdentifier.isEmpty()) {
						presenter.addToDtoMap(columnName, tableColumnExtractionRuleDTO);
						this.tableColumnValue.getStore().add(columnName);
					}
				}
			}
		}
	}

	public void setSelectedTableColumn(String selColumnName) {
		final int itemCount = this.tableColumnValue.getStore().size();
		if (selColumnName != null && !selColumnName.isEmpty() && itemCount != 0) {
			int index = tableColumnValue.getStore().indexOf(selColumnName);
			if (-1 != index) {
//				int currentSelectedIndex = this.tableColumnValue.getSelectedIndex();
//				if (index == currentSelectedIndex) {
//					ValueChangeEvent.fire(tableColumnValue, selColumnName);
//				}
				this.tableColumnValue.setValue(selColumnName, true);
			}
		}
	}

	public void fireSelectedColumnValueChangeEvent() {
		int currentSelectedIndex = this.tableColumnValue.getSelectedIndex();
		ValueChangeEvent.fire(tableColumnValue, this.tableColumnValue.getStore().get(currentSelectedIndex));
	}

	public String getSelectedTableColumn() {
		return this.tableColumnValue.getStore().get(this.tableColumnValue.getSelectedIndex());
	}
	public void setHeadingText(String heading){
		if(!StringUtil.isNullOrEmpty(heading) && null!=advExtrRuleFramedPanel){
			advExtrRuleFramedPanel.setHeadingText(heading);
			advExtrRuleFramedPanel.getHeader().setToolTip(heading);
		}
	}
}
