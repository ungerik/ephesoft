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

package com.ephesoft.gxt.core.shared.dto.propertyAccessors;

import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.BatchConstants;
import com.ephesoft.gxt.core.shared.dto.CustomValueProvider;
import com.ephesoft.gxt.core.shared.dto.EmailConfigurationDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.EmailImportConfigurationProperties.EmailConfigurationProperties;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.valueprovider.StringToIntegerValueProvider;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * This interface provides Property Accessors for {@link TableColumnExtractionRuleDTO}
 * 
 * @author Ephesoft
 * @version 1.0
 *
 */
public class ColumnExtractionRuleProperties {

	private static ColumnExtractionRuleProperties properties;

	private ModelKeyProvider<TableColumnExtractionRuleDTO> identifier;

	private ValueProvider<TableColumnExtractionRuleDTO, String> betweenLeft;

	private ValueProvider<TableColumnExtractionRuleDTO, String> betweenRight;

	private ValueProvider<TableColumnExtractionRuleDTO, String> columnPattern;

	private ValueProvider<TableColumnExtractionRuleDTO, String> columnHeaderPattern;

	private StringToIntegerValueProvider<TableColumnExtractionRuleDTO, Integer> columnStartCoordinate;

	private StringToIntegerValueProvider<TableColumnExtractionRuleDTO, Integer> columnEndCoordinate;

	private ValueProvider<TableColumnExtractionRuleDTO, String> columnCoordY0;
	
	private ValueProvider<TableColumnExtractionRuleDTO, String> columnCoordY1;
	
	private ValueProvider<TableColumnExtractionRuleDTO, String> extractedDataColumnName;
	
	private ValueProvider<TableColumnExtractionRuleDTO, String> orderNumber;
	
	private ValueProvider<TableColumnExtractionRuleDTO, String> columnName;
	
	private ValueProvider<TableColumnExtractionRuleDTO, Boolean> currency;

	private ValueProvider<TableColumnExtractionRuleDTO, Boolean> isDeleted;

	private ValueProvider<TableColumnExtractionRuleDTO, Boolean> isNew;

	private ValueProvider<TableColumnExtractionRuleDTO, Boolean> mandatory;
	
	private ValueProvider<TableColumnExtractionRuleDTO, Boolean> required;

	private ValueProvider<TableColumnExtractionRuleDTO, Boolean> selected;

	static {
		properties = new ColumnExtractionRuleProperties();
	}

	
	public static ColumnExtractionRuleProperties getProperties() {
		return properties;
	}

	
	public ModelKeyProvider<TableColumnExtractionRuleDTO> identifier() {
		return identifier;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, String> getBetweenLeft() {
		return betweenLeft;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, String> getBetweenRight() {
		return betweenRight;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, String> getColumnPattern() {
		return columnPattern;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, String> getColumnHeaderPattern() {
		return columnHeaderPattern;
	}

	
	public StringToIntegerValueProvider<TableColumnExtractionRuleDTO, Integer> getColumnStartCoordinate() {
		return columnStartCoordinate;
	}

	
	public StringToIntegerValueProvider<TableColumnExtractionRuleDTO, Integer> getColumnEndCoordinate() {
		return columnEndCoordinate;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, String> getColumnCoordY0() {
		return columnCoordY0;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, String> getColumnCoordY1() {
		return columnCoordY1;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, String> getExtractedDataColumnName() {
		return extractedDataColumnName;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, String> getOrderNumber() {
		return orderNumber;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, String> getColumnName() {
		return columnName;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, Boolean> getCurrency() {
		return currency;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, Boolean> getIsDeleted() {
		return isDeleted;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, Boolean> getIsNew() {
		return isNew;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, Boolean> getMandatory() {
		return mandatory;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, Boolean> getRequired() {
		return required;
	}

	
	public ValueProvider<TableColumnExtractionRuleDTO, Boolean> selected() {
		return selected;
	}

	private ColumnExtractionRuleProperties() {
		identifier = TableColumnExtractionRuleProperties.properties.identifier();
		selected = TableColumnExtractionRuleProperties.properties.selected();
		betweenLeft = TableColumnExtractionRuleProperties.properties.betweenLeft();
		betweenRight = TableColumnExtractionRuleProperties.properties.betweenRight();
		columnPattern = TableColumnExtractionRuleProperties.properties.columnPattern();
		columnHeaderPattern = TableColumnExtractionRuleProperties.properties.columnHeaderPattern();
		columnStartCoordinate = new StringToIntegerValueProvider<TableColumnExtractionRuleDTO, Integer>(TableColumnExtractionRuleProperties.properties.columnStartCoordinate());
		columnEndCoordinate = new StringToIntegerValueProvider<TableColumnExtractionRuleDTO, Integer>(TableColumnExtractionRuleProperties.properties.columnEndCoordinate());
		columnCoordY0 = TableColumnExtractionRuleProperties.properties.columnCoordY0();
		columnCoordY1 = TableColumnExtractionRuleProperties.properties.columnCoordY1();
		extractedDataColumnName = TableColumnExtractionRuleProperties.properties.extractedDataColumnName();
		orderNumber = TableColumnExtractionRuleProperties.properties.orderNumber();
		columnName = TableColumnExtractionRuleProperties.properties.columnName();
		isDeleted = TableColumnExtractionRuleProperties.properties.isDeleted();
		isNew = TableColumnExtractionRuleProperties.properties.isNew();
		currency = TableColumnExtractionRuleProperties.properties.currency();
		mandatory = TableColumnExtractionRuleProperties.properties.mandatory();
		required = TableColumnExtractionRuleProperties.properties.required();
	}

public interface TableColumnExtractionRuleProperties extends PropertyAccess<TableColumnExtractionRuleDTO> {
	
	public static TableColumnExtractionRuleProperties properties = GWT.create(TableColumnExtractionRuleProperties.class);

	ModelKeyProvider<TableColumnExtractionRuleDTO> identifier();

	ValueProvider<TableColumnExtractionRuleDTO, String> betweenLeft();

	ValueProvider<TableColumnExtractionRuleDTO, String> betweenRight();

	ValueProvider<TableColumnExtractionRuleDTO, String> columnPattern();

	ValueProvider<TableColumnExtractionRuleDTO, String> columnHeaderPattern();

	ValueProvider<TableColumnExtractionRuleDTO, String> columnStartCoordinate();

	ValueProvider<TableColumnExtractionRuleDTO, String> columnEndCoordinate();

	ValueProvider<TableColumnExtractionRuleDTO, String> columnCoordY0();

	ValueProvider<TableColumnExtractionRuleDTO, String> columnCoordY1();

	ValueProvider<TableColumnExtractionRuleDTO, String> extractedDataColumnName();

	ValueProvider<TableColumnExtractionRuleDTO, String> orderNumber();

	ValueProvider<TableColumnExtractionRuleDTO, String> columnName();

	ValueProvider<TableColumnExtractionRuleDTO, Boolean> isDeleted();

	ValueProvider<TableColumnExtractionRuleDTO, Boolean> isNew();

	ValueProvider<TableColumnExtractionRuleDTO, Boolean> currency();

	ValueProvider<TableColumnExtractionRuleDTO, Boolean> mandatory();

	ValueProvider<TableColumnExtractionRuleDTO, Boolean> required();
	
	ValueProvider<TableColumnExtractionRuleDTO, Boolean> selected();
}
}
