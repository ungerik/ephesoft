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

package com.ephesoft.gxt.systemconfig.client.widget;

import java.util.List;

import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.widget.MultipleSelectDualList;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.widget.property.PluginDTOComparator;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.widget.core.client.ListView;

/**
 * The dual list to provide the functionality of selecting or de-selecting multiple items from a list and arranging their order also.
 * 
 * @author Ephesoft
 * @param <M> the model type
 * @param <T> the type displayed in the list view
 */
public class DependencyDragDropDualList<T> extends MultipleSelectDualList<PluginDetailsDTO, T> {

	private boolean isCtrlPressed = false;

	private PluginDetailsDTO and;

	private PluginDetailsDTO or;

	public DependencyDragDropDualList(final List<PluginDetailsDTO> listOfData, final PropertyAccessModel propertyAccessModel) {
		super(listOfData, propertyAccessModel);

		and = new PluginDetailsDTO();
		and.setPluginName(SystemConfigConstants.AND_DEPENDENCY_NAME);
		or = new PluginDetailsDTO();
		or.setPluginName(SystemConfigConstants.OR_DEPENDENCY_NAME);
		
		HorizontalPanel panel = (HorizontalPanel) getWidget();
		panel.addStyleName("dependencyDragDropListCSS");
//		getToStore().getKeyProvider().getKey(item)
		getFromStore().addSortInfo(new StoreSortInfo<PluginDetailsDTO>(new PluginDTOComparator(), SortDir.ASC));
		
		addToolTip(getToView());
		addToolTip(getFromView());
	}
	
	public void addModelToRightList(PluginDetailsDTO selectedModel){
		if (null != selectedModel){
//			Message.display("adding model : " + selectedModel.getPluginName(),"");
//			Message.display("getToStore().findModel(selectedModel) == null",""+(getToStore().findModel(selectedModel) == null));
//			Message.display("getToStore().hasRecord(selectedModel)",""+(getToStore().hasRecord(selectedModel)));
			if (getToStore().findModel(selectedModel) == null) {
//			if (!getToStore().hasRecord(selectedModel)) {
//				Message.display("Model was added 1st time.","");
				getToStore().add(selectedModel);
			} else {
//				Message.display("Model was already added Adding new copy.","");
				PluginDetailsDTO newPluginDTO = new PluginDetailsDTO();
				newPluginDTO.setPluginName(selectedModel.getPluginName());
				getToStore().add(newPluginDTO);
			}
		}
	}

	@Override
	protected void onRight() {
		List<PluginDetailsDTO> selectedList = getFromView().getSelectionModel().getSelectedItems();
		for (PluginDetailsDTO selected : selectedList) {
			if (getToStore().size() > 0) {
				getToStore().add(this.and);
			}
			addModelToRightList(selected);
		}
		getToView().getSelectionModel().select(selectedList, false);
		getFromView().getSelectionModel().select(selectedList, false);

	}

	@Override
	protected void onLeft() {
		List<PluginDetailsDTO> selectedList = getFromView().getSelectionModel().getSelectedItems();
		for (PluginDetailsDTO selected : selectedList) {
			if (getToStore().size() > 0) {
				getToStore().add(this.or);
			}
			addModelToRightList(selected);
		}
		getToView().getSelectionModel().select(selectedList, false);
		getFromView().getSelectionModel().select(selectedList, false);
	}

	/**
	 * True to allow selections to be dragged and dropped between lists (defaults to true).
	 * 
	 * @param enableDnd true to enable drag and drop
	 */
	@Override
	public void setEnableDnd(boolean enableDnd) {
		// setMode(Mode.INSERT);
		if (enableDnd) {
			if (sourceFromField == null) {
				// horizontalPanel = new HorizontalPanel();
				/* sourceFromField = new SourceList(getFromView()); */
				sourceFromField = new DependencyListViewDragSource(true, getFromView());
				/* sourceToField = new SourceList(getToView()); */
				sourceToField = new DependencyListViewDragSource(false, getToView());

				// horizontalPanel.add(sourceFromField.getWidget());
				// VerticalPanel vp = new VerticalPanel();
				// vp.add(new Button("AND"));
				// vp.add(new Button("OR"));
				// horizontalPanel.add(vp);

				/* targetFromField = new DropList(getFromView()); */
				targetFromField = new DependencyListViewDropTarget(false, getFromView());
				targetFromField.setAutoSelect(true);
				/* targetToField = new DropList(getToView()); */
				targetToField = new DependencyListViewDropTarget(true, getToView());
				targetToField.setAutoSelect(true);

				// horizontalPanel.add(targetToField.getWidget());
				if (mode == Mode.INSERT) {
					targetToField.setAllowSelfAsSource(true);
					targetFromField.setFeedback(Feedback.INSERT);
					targetToField.setFeedback(Feedback.INSERT);
				}

				setDndGroup(getDndGroup());
				
				//setting ids
				sourceFromField.getListView().setId("sourceFromField");
				WidgetUtil.setID(sourceFromField.getWidget(), "sourceFromField");
				sourceToField.getListView().setId("sourceToField");
				WidgetUtil.setID(sourceToField.getWidget(), "sourceToField");
				targetFromField.getListView().setId("targetFromField");
				WidgetUtil.setID(targetFromField.getWidget(), "targetFromField");
				targetToField.getListView().setId("targetToField");
				WidgetUtil.setID(targetToField.getWidget(), "targetToField");
			}
			// getFromView().setSelectOnOver(true);
			// getToView().setSelectOnOver(true);

		} else {
			if (sourceFromField != null) {
				sourceFromField.release();
				sourceFromField = null;
			}
			if (sourceToField != null) {
				sourceToField.release();
				sourceToField = null;
			}
			if (targetFromField != null) {
				targetFromField.release();
				targetFromField = null;
			}
			if (targetToField != null) {
				targetToField.release();
				targetToField = null;
			}
		}

		// this.enableDnd = enableDnd;
	}

	public PluginDetailsDTO getAND() {
		return this.and;
	}

	public PluginDetailsDTO getOR() {
		return this.or;
	}

	public class DependencyListViewDragSource extends ListViewDragSource<PluginDetailsDTO> {

		private boolean preventDeletion = false;

		public DependencyListViewDragSource(boolean preventDeletion, ListView<PluginDetailsDTO, ?> listView) {
			super(listView);
			this.preventDeletion = preventDeletion;
			this.getListView().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			getListView().getSelectionModel().addBeforeSelectionHandler(new BeforeSelectionHandler<PluginDetailsDTO>() {

				@Override
				public void onBeforeSelection(BeforeSelectionEvent<PluginDetailsDTO> event) {
					PluginDetailsDTO selected = event.getItem();
					if (selected.getPluginName().equalsIgnoreCase(SystemConfigConstants.AND_DEPENDENCY_NAME)
							|| selected.getPluginName().equalsIgnoreCase(SystemConfigConstants.OR_DEPENDENCY_NAME)) {
						event.cancel();
					}

				}
			});
		}

		@Override
		protected void onDragDrop(DndDropEvent event) {
			if (preventDeletion) {
				// super.onDragDrop(event);
			} else {
				List<PluginDetailsDTO> allSelected = (List) event.getData();
				for (PluginDetailsDTO selected : allSelected) {
					int index = listView.getStore().indexOf(selected);
					int size = listView.getStore().size();
					if (index == 0) {
						listView.getStore().remove(1);
					} else if (size > 1 && index < size) {
						listView.getStore().remove(index - 1);
					}
				}
				super.onDragDrop(event);
			}
		}

		public boolean isPreventDeletion() {
			return preventDeletion;
		}

		public void setPreventDeletion(boolean preventDeletion) {
			this.preventDeletion = preventDeletion;
		}

	}

	public class DependencyListViewDropTarget extends ListViewDropTarget<PluginDetailsDTO> {

		boolean insertRelation = false;

		public DependencyListViewDropTarget(boolean insertRelation, ListView<PluginDetailsDTO, ?> listView) {
			super(listView);
			this.insertRelation = insertRelation;
			this.getListView().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			// this.getListView().getStore().addStoreAddHandler(new StoreAddHandler<PluginDetailsDTO>() {
			//
			// @Override
			// public void onAdd(StoreAddEvent<PluginDetailsDTO> event) {
			// for (PluginDetailsDTO selected : event.getItems()){
			// PluginDetailsDTO newPluginDTO = new PluginDetailsDTO();
			// newPluginDTO.setPluginName(selected.getPluginName());
			// getListView().getStore().add(newPluginDTO);
			// }
			//
			// }
			// });
		}

		@Override
		protected void onDragDrop(DndDropEvent event) {
			if (insertRelation) {
				if (listView.getStore().size() > 0) {
					if (isCtrlPressed()) {
						listView.getStore().add(getOR());
						// PluginDetailsDTO newPlugin = new PluginDetailsDTO();
						// newPlugin.setIdentifier("200");
						// newPlugin.setPluginName("CLEANUP");
						// listView.getStore().add(newPlugin);
					} else {
						listView.getStore().add(getAND());
						// PluginDetailsDTO newPlugin2 = new PluginDetailsDTO();
						// newPlugin2.setIdentifier("100");
						// newPlugin2.setPluginName("CLEANUP");
						// listView.getStore().add(newPlugin2);
					}
				}
				Object data = event.getData();
				List<PluginDetailsDTO> models = (List) prepareDropData(data, true);
				for (PluginDetailsDTO selected : models) {
					addModelToRightList(selected);
				}
				// super.onDragDrop(event);
			} else {

			}
		}

		// protected List<Object> prepareDropData(Object data, boolean convertTreeStoreModel) {
		// return new ArrayList<Object>();
		// }
	}

	public boolean isCtrlPressed() {
		return isCtrlPressed;
	}

	public void setCtrlPressed(boolean isCtrlPressed) {
		this.isCtrlPressed = isCtrlPressed;
	}
	
	/**
	 * Adds the toolTip on list view.
	 *
	 * @param listView the list view
	 */
	public void addToolTip(final ListView<PluginDetailsDTO, T> listView) {
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

}
