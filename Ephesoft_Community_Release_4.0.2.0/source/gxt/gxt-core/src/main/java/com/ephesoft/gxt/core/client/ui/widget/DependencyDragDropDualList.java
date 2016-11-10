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
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;

/**
 * The dual list to provide the functionality of selecting or de-selecting multiple items from a list and arranging their order also.
 * 
 * @author Ephesoft
 * @param <M> the model type
 * @param <T> the type displayed in the list view
 */
public class DependencyDragDropDualList<M, T> extends MultipleSelectDualList<M, T> {

	public DependencyDragDropDualList(final List<M> listOfData, final PropertyAccessModel propertyAccessModel) {
		super(listOfData, propertyAccessModel);

	}

	@Override
	public void setEnableDnd(boolean enableDnd) {
		setMode(Mode.INSERT);
		if (enableDnd) {
			sourceFromField = new DependencyListViewDragSource<M>(true, getFromView());
			sourceToField = new ListViewDragSource<M>(getToView());
			targetFromField = new DependencyListViewDropTarget<M>(true, getFromView());
			targetFromField.setAutoSelect(true);
			targetToField = new DependencyListViewDropTarget<M>(false, getToView());
			targetToField.setAutoSelect(true);
			if (mode == Mode.INSERT) {
				targetToField.setAllowSelfAsSource(true);
				((DependencyListViewDropTarget)targetToField).setSelfAsDrop(true);
				targetFromField.setFeedback(Feedback.INSERT);
				targetToField.setFeedback(Feedback.INSERT);
			}
			setDndGroup(getDndGroup());
		}
		super.setEnableDnd(enableDnd);

		// getToView().setSelectOnOver(true);
	}

	/*
	 * @Override protected void onRight() {
	 * 
	 * // super.onRight(); List<M> sel = getFromView().getSelectionModel().getSelectedItems(); getToStore().addAll(sel);
	 * //getToView().getSelectionModel().select(sel, false);
	 * 
	 * if (getToStore().size() > 0) { getToStore().add(this.and); } PluginDetailsDTO selectedDependency =
	 * getFromView().getSelectionModel().getSelectedItem(); selectedDependency.setIdentifier(String.valueOf(id++));
	 * getFromStore().add(selectedDependency);
	 * 
	 * super.onRight();
	 * 
	 * }
	 */

	public void removeSelectedItemsFromRight() {
		List<M> sel = getToView().getSelectionModel().getSelectedItems();
		for (M m : sel) {
			getToStore().remove(m);
		}
	}
	
	protected void onLeft(){
		super.onLeft();
	}

}
