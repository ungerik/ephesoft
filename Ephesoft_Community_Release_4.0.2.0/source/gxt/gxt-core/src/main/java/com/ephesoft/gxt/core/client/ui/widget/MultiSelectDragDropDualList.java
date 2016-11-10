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
import com.ephesoft.gxt.core.client.ui.widget.listener.DualListButtonListener;
import com.ephesoft.gxt.core.shared.dto.BatchClassModuleDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.widget.core.client.ListView;

// TODO: Auto-generated Javadoc
/**
 * The dual list to provide the functionality of selecting or de-selecting multiple items from a list and arranging their order also.
 * 
 * @param <M> the model type
 * @param <T> the type displayed in the list view
 * @author Ephesoft
 */
public class MultiSelectDragDropDualList<M, T> extends MultipleSelectDualList<M, T> {

	/** The button listener. */
	private DualListButtonListener<M> buttonListener;

	/**
	 * Instantiates a new multi select drag drop dual list.
	 * 
	 * @param listOfData the list of data
	 * @param propertyAccessModel the property access model
	 */
	public MultiSelectDragDropDualList(final List<M> listOfData, final PropertyAccessModel propertyAccessModel) {
		super(listOfData, propertyAccessModel);
		addHandler(getToView());
		addHandler(getFromView());
		addKeyPressEvent(getToView());
		addKeyPressEvent(getFromView());
	}

	/**
	 * Instantiates a new multi select drag drop dual list.
	 * 
	 * @param listOfData the list of data
	 * @param propertyAccessModel the property access model
	 * @param appearance the appearance
	 */
	public MultiSelectDragDropDualList(final List<M> listOfData, final PropertyAccessModel propertyAccessModel,
			final DualListFieldAppearance appearance) {
		super(listOfData, propertyAccessModel, appearance);
		addHandler(getToView());
		addHandler(getFromView());
		addKeyPressEvent(getToView());
		addKeyPressEvent(getFromView());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sencha.gxt.widget.core.client.form.DualListField#setEnableDnd(boolean)
	 */
	@Override
	public void setEnableDnd(boolean enableDnd) {
		setMode(Mode.INSERT);
		if (enableDnd) {
			sourceFromField = new MultiSelectListViewDragSource<M>(true, getFromView());
			
			sourceToField = new ListViewDragSource<M>(getToView());
			targetFromField = new MultiSelectListViewDropTarget<M>(true, getFromView());
			targetFromField.setAutoSelect(true);
			targetToField = new MultiSelectListViewDropTarget<M>(false, getToView());
			targetToField.setAutoSelect(true);
			if (mode == Mode.INSERT) {
				targetToField.setAllowSelfAsSource(true);
				((MultiSelectListViewDropTarget<M>) targetToField).setSelfAsDrop(true);
				targetFromField.setFeedback(Feedback.INSERT);
				targetToField.setFeedback(Feedback.INSERT);
			}
			setDndGroup(getDndGroup());
		}
		super.setEnableDnd(enableDnd);
	}

	/**
	 * Removes the selected items from right view.
	 */
	public void removeSelectedItemsFromRight() {
		List<M> sel = getToView().getSelectionModel().getSelectedItems();
		for (M m : sel) {
			getToStore().remove(m);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sencha.gxt.widget.core.client.form.DualListField#onLeft()
	 */
	protected void onLeft() {
		List<M> sel = getToView().getSelectionModel().getSelectedItems();
		for (M m : sel) {
			getToStore().remove(m);
		}
		if (null != buttonListener) {
			buttonListener.onLeftClick(sel);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sencha.gxt.widget.core.client.form.DualListField#onRight()
	 */
	protected void onRight() {
		List<StoreSortInfo<M>> sortInfos = getToStore().getSortInfo();
		if (null != sortInfos && sortInfos.size() > 0) {
			getToStore().clearSortInfo();
		}
		List<M> sel = getFromView().getSelectionModel().getSelectedItems();
		int index = getToStore().size();
		List<M> selectedItems = getToView().getSelectionModel().getSelectedItems();
		if (!CollectionUtil.isEmpty(selectedItems)) {
			index = 0;
			for (M item : selectedItems) {
				index = Math.max(index + 1, getToStore().indexOf(item) + 1);
			}
		}
		getToStore().addAll(index, sel);
//		getToView().getSelectionModel().select(sel, false);
		if (null != buttonListener) {
			buttonListener.onRightClick(sel);
		}
		if (null != sortInfos && sortInfos.size() > 0) {
			int sortIndex = 0;
			for (StoreSortInfo<M> store : sortInfos) {
				getToStore().addSortInfo(sortIndex++, store);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sencha.gxt.widget.core.client.form.DualListField#onUp()
	 */
	protected void onUp() {
		List<StoreSortInfo<M>> sortInfos = getToStore().getSortInfo();
		if (null != sortInfos && sortInfos.size() > 0) {
			getToStore().clearSortInfo();
		}
		super.onUp();
		if (null != getToView().getSelectionModel()) {
			if (!CollectionUtil.isEmpty(getToView().getSelectionModel().getSelectedItems())) {
				buttonListener.onUpClick();
			}
		}
		if (null != sortInfos && sortInfos.size() > 0) {
			int index = 0;
			for (StoreSortInfo<M> store : sortInfos) {
				getToStore().addSortInfo(index++, store);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sencha.gxt.widget.core.client.form.DualListField#onDown()
	 */
	protected void onDown() {
		List<StoreSortInfo<M>> sortInfos = getToStore().getSortInfo();
		if (null != sortInfos && sortInfos.size() > 0) {
			getToStore().clearSortInfo();
		}
		super.onDown();
		if (null != getToView().getSelectionModel()) {
			if (!CollectionUtil.isEmpty(getToView().getSelectionModel().getSelectedItems())) {
				buttonListener.onDownClick();
			}
		}
		if (null != sortInfos && sortInfos.size() > 0) {
			int index = 0;
			for (StoreSortInfo<M> store : sortInfos) {
				getToStore().addSortInfo(index++, store);
			}
		}
	}

	/**
	 * Adds the Store Add and Remove handler.
	 * 
	 * @param listView the list view
	 */
	public void addHandler(final ListView<M, T> listView) {
		listView.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {

				Element target = event.getNativeEvent().getEventTarget().<Element> cast();
				if (null != target) {
					target = listView.findElement(target);
					if (target != null) {
						int index = listView.indexOf(target);
						if (index != -1) {
							listView.setTitle(target.getInnerText());
						} else {
							listView.setTitle(null);
						}
					} else  {
						listView.setTitle(null);
					}
				}
			}
		}, MouseMoveEvent.getType());

	}

	/**
	 * Sets the tool tip.
	 * 
	 * @param listView the list view
	 */
	public void setTitle(ListView<M, T> listView) {
		List<Element> listOfItems = listView.getElements();
		for (Element item : listOfItems) {
			item.setTitle(item.getInnerText());
		}
	}

	/**
	 * Sets the button listener.
	 * 
	 * @param buttonListener the new button listener
	 */
	public void setButtonListener(DualListButtonListener<M> buttonListener) {
		this.buttonListener = buttonListener;
	}

	public void addKeyPressEvent(final ListView<M, T> listView) {
		listView.addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				int keyPressed = event.getNativeKeyCode();
				String matchKey = String.valueOf((char) keyPressed).toLowerCase();
				if (keyPressed >= KeyCodes.KEY_A && keyPressed <= KeyCodes.KEY_Z) {
					List<M> elements = listView.getStore().getAll();
					if (!CollectionUtil.isEmpty(elements)) {
						if (elements.get(0) instanceof BatchClassPluginDTO) {
							List<M> selectedItems = listView.getSelectionModel().getSelectedItems();
							listView.getSelectionModel().deselectAll();
							if (!CollectionUtil.isEmpty(selectedItems) && selectedItems.size() == 1) {
								M selectedItem = selectedItems.get(0);
								boolean flag = false;
								if(!((BatchClassPluginDTO)selectedItem).getPlugin().getPluginName().toLowerCase().startsWith(matchKey)){
									flag=true;
								}
								for (M element : elements) {
									if (flag) {
										BatchClassPluginDTO batchClassPluginDTO = (BatchClassPluginDTO) element;
										if (batchClassPluginDTO.getPlugin().getPluginName().toLowerCase().startsWith(matchKey)) {
											if (!listView.getSelectionModel().isSelected(element)) {
												listView.getSelectionModel().select(false, element);
												listView.getElement(listView.getStore().indexOf(listView.getSelectionModel().getSelectedItem())).scrollIntoView();
												break;
											}
										}
									}
									if (selectedItem == element) {
										flag = true;
									}
								}
							} if (CollectionUtil.isEmpty(selectedItems) || selectedItems.size() > 1
									|| CollectionUtil.isEmpty(listView.getSelectionModel().getSelectedItems())) {
								for (M element : elements) {
									BatchClassPluginDTO batchClassPluginDTO = (BatchClassPluginDTO) element;
									if (batchClassPluginDTO.getPlugin().getPluginName().toLowerCase().startsWith(matchKey)) {
										if (!listView.getSelectionModel().isSelected(element)) {
											listView.getSelectionModel().select(false, element);
											listView.getElement(listView.getStore().indexOf(listView.getSelectionModel().getSelectedItem())).scrollIntoView();
											break;
										}
									}
								}
							}
						} else if (elements.get(0) instanceof BatchClassModuleDTO) {
							List<M> selectedItems = listView.getSelectionModel().getSelectedItems();
							listView.getSelectionModel().deselectAll();
							if (!CollectionUtil.isEmpty(selectedItems) && selectedItems.size() == 1) {
								M selectedItem = selectedItems.get(0);
								boolean flag = false;
								if(!((BatchClassModuleDTO)selectedItem).getWorkflowName().toLowerCase().startsWith(matchKey)){
									flag=true;
								}
								for (M element : elements) {
									if (flag) {
										BatchClassModuleDTO batchClassModuleDTO = (BatchClassModuleDTO) element;
										if (batchClassModuleDTO.getWorkflowName().toLowerCase().startsWith(matchKey)) {
											if (!listView.getSelectionModel().isSelected(element)) {
												listView.getSelectionModel().select(false, element);
												break;
											}
										}
									}
									if (selectedItem == element) {
										flag = true;
									}
								}
							} if (CollectionUtil.isEmpty(selectedItems) || selectedItems.size() > 1
									|| CollectionUtil.isEmpty(listView.getSelectionModel().getSelectedItems())) {
								for (M element : elements) {
									BatchClassModuleDTO batchClassModuleDTO = (BatchClassModuleDTO) element;
									if (batchClassModuleDTO.getWorkflowName().toLowerCase().startsWith(matchKey)) {
										if (!listView.getSelectionModel().isSelected(element)) {
											listView.getSelectionModel().select(false, element);
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}, KeyDownEvent.getType());
	}

}
