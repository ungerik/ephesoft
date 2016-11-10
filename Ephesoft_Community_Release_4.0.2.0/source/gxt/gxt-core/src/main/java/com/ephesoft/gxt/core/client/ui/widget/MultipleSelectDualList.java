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

import java.util.List;

import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.factory.DualListAttributeFactory;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.DualListField;

/**
 * The dual list to provide the functionality of selecting or de-selecting multiple items from a list and arranging their order also.
 * 
 * @author Ephesoft
 * @param <M> the model type
 * @param <T> the type displayed in the list view
 */
public class MultipleSelectDualList<M, T> extends DualListField<M, T> {

	public MultipleSelectDualList(List<M> listOfData, final PropertyAccessModel propertyAccessModel) {
		super(MultipleSelectDualList.<M, T> createListStore(propertyAccessModel, listOfData), MultipleSelectDualList
				.<M, T> createListStore(propertyAccessModel), DualListAttributeFactory.<M, T> getValueProvider(propertyAccessModel),
				(Cell<T>) new TextCell());
	}

	public MultipleSelectDualList(List<M> listOfData, final PropertyAccessModel propertyAccessModel, final DualListFieldAppearance appearance) {
		super(MultipleSelectDualList.<M, T> createListStore(propertyAccessModel, listOfData), MultipleSelectDualList
				.<M, T> createListStore(propertyAccessModel), DualListAttributeFactory.<M, T> getValueProvider(propertyAccessModel),
				(Cell<T>) new TextCell(), appearance);
	}
	
	private static <M, T> ListStore<M> createListStore(final PropertyAccessModel propertyAccessModel, List<M> listOfData) {
		ListStore<M> listStore = null;
		final ModelKeyProvider<M> keyProvider = DualListAttributeFactory.<M, T> getKeyProvider(propertyAccessModel);
		if (null != keyProvider) {
			listStore = CollectionUtil.createListStore(listOfData, keyProvider);
		}
		return listStore;
	}

	private static <M, T> ListStore<M> createListStore(final PropertyAccessModel propertyAccessModel) {
		ListStore<M> listStore = null;
		final ModelKeyProvider<M> keyProvider = DualListAttributeFactory.<M, T> getKeyProvider(propertyAccessModel);
		if (null != keyProvider) {
			listStore = CollectionUtil.createListStore(keyProvider);
		}
		return listStore;
	}
}
