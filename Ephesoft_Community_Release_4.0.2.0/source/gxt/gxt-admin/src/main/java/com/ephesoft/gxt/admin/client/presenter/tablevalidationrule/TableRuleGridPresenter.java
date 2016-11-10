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

package com.ephesoft.gxt.admin.client.presenter.tablevalidationrule;

import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.AddTableRuleEvent;
import com.ephesoft.gxt.admin.client.event.DeleteTableRulesEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.tablevalidationrule.TableRuleGridView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.RuleInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * This presenter deals with Table Validation Rule Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.presenter.tablevalidationrule.TableRuleGridPresenter
 */
public class TableRuleGridPresenter extends BatchClassInlinePresenter<TableRuleGridView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<TableRuleGridPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * Instantiates a new table rule grid presenter.
	 * 
	 * @param controller the controller {@link BatchClassManagementController}
	 * @param view the view {@link TableRuleGridView}
	 */
	public TableRuleGridPresenter(final BatchClassManagementController controller, final TableRuleGridView view) {
		super(controller, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.event.BindHandler#bind()
	 */
	@Override
	public void bind() {
		final TableInfoDTO tableInfo = controller.getSelectedTableInfo();
		if (null != tableInfo && !tableInfo.isDeleted()) {
			final List<RuleInfoDTO> rules = tableInfo.getRuleInfoDTOs();
			view.loadGrid(rules);
			view.setRuleView(tableInfo);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.BasePresenter#injectEvents(com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	/**
	 * Handle table validation rules deletion.
	 * 
	 * @param deleteEvent the delete event {@link DeleteTableRulesEvent}
	 */
	@EventHandler
	public void handleTableRulesDeletion(final DeleteTableRulesEvent deleteEvent) {
		if (null != deleteEvent) {
			final List<RuleInfoDTO> selectedRules = view.getSelectedTableRules();
			if (!CollectionUtil.isEmpty(selectedRules)) {
				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_TABLE_RULES_MSG), false, DialogIcon.QUESTION_MARK);
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
						deleteTableRules(selectedRules);
						controller.setBatchClassDirtyFlg(true);
						view.commitChanges();
						view.reLoadGrid();
						Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS),
								LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_TABLE_RULES_SUCCESS_MSG));
					}

					@Override
					public void onCancelClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
					}
				});
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.NO_ROW_SELECTED), DialogIcon.WARNING);
			}
		}
	}

	/**
	 * Delete selected table validation rules.
	 * 
	 * @param selectedRules the selected table validation rules {@link List<{@link RuleInfoDTO}>}
	 */
	private void deleteTableRules(final List<RuleInfoDTO> selectedRules) {

		final TableInfoDTO tableDTO = controller.getSelectedTableInfo();
		for (final RuleInfoDTO tableRule : selectedRules) {
			if (null != tableRule) {
				tableDTO.getRuleInfoDTOByIdentifier(tableRule.getIdentifier()).setDeleted(true);
			}
		}
		view.removeItemsFromGrid(selectedRules);
	}

	/**
	 * Handle table validation rule addition.
	 * 
	 * @param addEvent the add event {@link AddTableRuleEvent}
	 */
	@EventHandler
	public void handleTableRuleAddition(final AddTableRuleEvent addEvent) {
		view.commitChanges();
		if (null != addEvent) {
			final RuleInfoDTO tableRuleDTO = createRuleInfoDTOObject();
			if (view.addNewItemInGrid(tableRuleDTO)) {
				controller.getSelectedTableInfo().addRuleInfo(tableRuleDTO);
				controller.setBatchClassDirtyFlg(true);
			}
		}
	}

	/**
	 * Method to create the new rule info dto.
	 * 
	 * @return the rule info dto {@link RuleInfoDTO}
	 */
	private RuleInfoDTO createRuleInfoDTOObject() {
		final RuleInfoDTO ruleInfoDTO = new RuleInfoDTO();
		ruleInfoDTO.setNew(true);
		ruleInfoDTO.setTableInfoDTO(controller.getSelectedTableInfo());
		ruleInfoDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		return ruleInfoDTO;
	}

	/**
	 * Method to set the batch class dirty on change.
	 * 
	 */
	public void setBatchClassDirtyOnChange() {
		controller.setBatchClassDirtyFlg(true);
		;
	}

	/**
	 * Checks if is grid validated.
	 * 
	 * @return true, if is grid validated
	 */
	public boolean isValid() {
		return view.isGridValidated();
	}

}
