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

package com.ephesoft.gxt.admin.client.controller;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.admin.client.BatchClassManagementServiceAsync;
import com.ephesoft.gxt.admin.client.event.BatchClassLoadEvent;
import com.ephesoft.gxt.admin.client.event.CloseBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.CompositePresenterSelectionEvent;
import com.ephesoft.gxt.admin.client.event.CurrentViewRegexValidationEvent;
import com.ephesoft.gxt.admin.client.event.ModelSelectionEvent;
import com.ephesoft.gxt.admin.client.event.NavigationTreeRefreshEvent;
import com.ephesoft.gxt.admin.client.event.OpenChildViewEvent;
import com.ephesoft.gxt.admin.client.event.RefreshBatchClassScreenEvent;
import com.ephesoft.gxt.admin.client.event.RemoveTreeViewEvent;
import com.ephesoft.gxt.admin.client.event.UpdateBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.ValidateNDeployWorkFlow;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassCompositePresenter;
import com.ephesoft.gxt.admin.client.presenter.BatchClassPresenter;
import com.ephesoft.gxt.admin.client.presenter.BatchClassPresenter.Results;
import com.ephesoft.gxt.admin.client.presenter.batchclass.CopyBatchClassPresenter;
import com.ephesoft.gxt.admin.client.presenter.batchclass.KeyGeneratorPresenter;
import com.ephesoft.gxt.admin.client.presenter.batchclassfield.BatchClassFieldPresenter;
import com.ephesoft.gxt.admin.client.presenter.chart.BatchClassChartPresenter;
import com.ephesoft.gxt.admin.client.presenter.document.DocumentTypePresenter;
import com.ephesoft.gxt.admin.client.presenter.document.testclassification.TestClassificationGridPresenter;
import com.ephesoft.gxt.admin.client.presenter.document.testclassification.TestClassificationMenuPresenter;
import com.ephesoft.gxt.admin.client.presenter.document.testclassification.TestClassifyTypePresenter;
import com.ephesoft.gxt.admin.client.presenter.document.testextraction.TestExtractionClassificationTypePresenter;
import com.ephesoft.gxt.admin.client.presenter.document.testextraction.TestExtractionMenuPresenter;
import com.ephesoft.gxt.admin.client.presenter.document.testextraction.datatable.DataTablePresenter;
import com.ephesoft.gxt.admin.client.presenter.document.testextraction.extractdlf.ExtractedDlfPresenter;
import com.ephesoft.gxt.admin.client.presenter.document.testextraction.navigator.TestExtractionNavigatorPresenter;
import com.ephesoft.gxt.admin.client.presenter.email.EmailPresenter;
import com.ephesoft.gxt.admin.client.presenter.fuzzymapping.FuzzyDBMappingPresenter;
import com.ephesoft.gxt.admin.client.presenter.indexFiled.IndexFieldPresenter;
import com.ephesoft.gxt.admin.client.presenter.kvextraction.KVExtractionPresenter;
import com.ephesoft.gxt.admin.client.presenter.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionPresenter;
import com.ephesoft.gxt.admin.client.presenter.mapping.DatabaseExportMappingPresenter;
import com.ephesoft.gxt.admin.client.presenter.module.ModulePresenter;
import com.ephesoft.gxt.admin.client.presenter.navigator.BatchClassNavigatorPresenter;
import com.ephesoft.gxt.admin.client.presenter.plugin.PluginListPresenter;
import com.ephesoft.gxt.admin.client.presenter.plugin.PluginPresenter;
import com.ephesoft.gxt.admin.client.presenter.regexPool.RegexGroupSelectionGridPresenter;
import com.ephesoft.gxt.admin.client.presenter.regexPool.RegexPatternSelectionGridPresenter;
import com.ephesoft.gxt.admin.client.presenter.regexValidation.RegexValidationFieldPresenter;
import com.ephesoft.gxt.admin.client.presenter.regexbuilder.RegexBuilderPresenter;
import com.ephesoft.gxt.admin.client.presenter.scanner.WebScannerPresenter;
import com.ephesoft.gxt.admin.client.presenter.tablecolumnextraction.ColumnExtractionRulePresenter;
import com.ephesoft.gxt.admin.client.presenter.tablecolumnextraction.advancedtablecolumnextractionrule.AdvancedTableColumnExtractionPresenter;
import com.ephesoft.gxt.admin.client.presenter.tablecolumninfo.TableColumnInfoPresenter;
import com.ephesoft.gxt.admin.client.presenter.tableextractionrule.TableExtractionRulePresenter;
import com.ephesoft.gxt.admin.client.presenter.tableinfo.TableInfoPresenter;
import com.ephesoft.gxt.admin.client.presenter.tablevalidationrule.TableValidationRulePresenter;
import com.ephesoft.gxt.admin.client.util.ValidateWorkflowUtil;
import com.ephesoft.gxt.admin.client.view.BatchClassCompositeView;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.view.batchclass.BatchClassView;
import com.ephesoft.gxt.admin.client.view.batchclass.CopyBatchClassView;
import com.ephesoft.gxt.admin.client.view.batchclass.KeyGeneratorView;
import com.ephesoft.gxt.admin.client.view.batchclassfield.BatchClassFieldView;
import com.ephesoft.gxt.admin.client.view.chart.BatchClassChartView;
import com.ephesoft.gxt.admin.client.view.document.DocumentTypeView;
import com.ephesoft.gxt.admin.client.view.document.testclassification.TestClassificationGridView;
import com.ephesoft.gxt.admin.client.view.document.testclassification.TestClassificationLayout;
import com.ephesoft.gxt.admin.client.view.document.testclassification.TestClassificationMenuView;
import com.ephesoft.gxt.admin.client.view.document.testclassification.TestClassifyTypeView;
import com.ephesoft.gxt.admin.client.view.document.testextraction.TestExtractionClassificationTypeView;
import com.ephesoft.gxt.admin.client.view.document.testextraction.TestExtractionInlineView;
import com.ephesoft.gxt.admin.client.view.document.testextraction.TestExtractionMenuView;
import com.ephesoft.gxt.admin.client.view.document.testextraction.TestExtractionlayout;
import com.ephesoft.gxt.admin.client.view.document.testextraction.datatable.DataTableView;
import com.ephesoft.gxt.admin.client.view.document.testextraction.extractdlf.ExtractedDlfView;
import com.ephesoft.gxt.admin.client.view.document.testextraction.navigator.TestExtractionNavigatorView;
import com.ephesoft.gxt.admin.client.view.email.EmailView;
import com.ephesoft.gxt.admin.client.view.fuzzymapping.FuzzyDBMappingView;
import com.ephesoft.gxt.admin.client.view.indexFields.IndexFieldView;
import com.ephesoft.gxt.admin.client.view.kvextraction.KVExtractionView;
import com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionInlineView;
import com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionView;
import com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction.layout.AdvancedKVExtractionLayout;
import com.ephesoft.gxt.admin.client.view.layout.BatchClassManagementLayout;
import com.ephesoft.gxt.admin.client.view.mapping.DatabaseExportMappingView;
import com.ephesoft.gxt.admin.client.view.module.ModuleView;
import com.ephesoft.gxt.admin.client.view.navigator.BatchClassNavigatorView;
import com.ephesoft.gxt.admin.client.view.plugin.PluginListView;
import com.ephesoft.gxt.admin.client.view.plugin.PluginView;
import com.ephesoft.gxt.admin.client.view.regexPool.RegexGroupSelectionGridView;
import com.ephesoft.gxt.admin.client.view.regexPool.RegexPatternSelectionGridView;
import com.ephesoft.gxt.admin.client.view.regexValidation.RegexValidationFieldView;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexBuilderView;
import com.ephesoft.gxt.admin.client.view.scanner.WebScannerView;
import com.ephesoft.gxt.admin.client.view.tablecolumnextraction.ColumnExtractionRuleView;
import com.ephesoft.gxt.admin.client.view.tablecolumnextraction.advcancedtablecolumnextractionrule.AdvancedTableColumnExtractionView;
import com.ephesoft.gxt.admin.client.view.tablecolumninfo.TableColumnInfoView;
import com.ephesoft.gxt.admin.client.view.tableextractionrule.TableExtractionRuleView;
import com.ephesoft.gxt.admin.client.view.tableinfo.TableInfoView;
import com.ephesoft.gxt.admin.client.view.tablevalidationrule.TableValidationRuleView;
import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.util.ViewUtil;
import com.ephesoft.gxt.core.shared.CategorisedData;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;

public class BatchClassManagementController extends BatchClassManagementSelectionController {

	interface CustomEventBinder extends EventBinder<BatchClassManagementController> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	private BatchClassNavigatorPresenter navigationPresenter;
	private BatchClassChartPresenter batchClassChartPresenter;
	private BatchClassManagementLayout batchClassLayout;
	private boolean requireReload;
	private BatchClassView batchClassView;
	private BatchClassPresenter batchClassPresenter;
	private DocumentTypeView documentTypeView;
	private IndexFieldView indexFieldView;
	private IndexFieldPresenter indexFieldPresenter;
	private DocumentTypePresenter documentTypePresenter;
	private EmailView emailView;
	private EmailPresenter emailPresenter;
	private RegexValidationFieldView regexValidationFieldView;
	private RegexValidationFieldPresenter regexFieldPresenter;
	private WebScannerView scannerView;
	private WebScannerPresenter scannerPresenter;
	private BatchClassFieldPresenter batchClassFieldPresenter;
	private BatchClassFieldView batchClassFieldView;
	private TableInfoPresenter tableInfoPresenter;
	private TableInfoView tableInfoView;
	private TestExtractionlayout testExtractionLayout;
	private TestExtractionMenuPresenter testExtractionMenuPresenter;
	private TestExtractionClassificationTypePresenter testExtractionClassificationTypePresenter;
	private List<String> batchClassExtractionModules;
	private TestExtractionNavigatorPresenter testExtractionNavigatorPresenter;
	private ExtractedDlfView extractedDlfView;
	private ExtractedDlfPresenter extractedDlfPresenter;
	private DataTableView dataTableView;
	private DataTablePresenter dataTablePresenter;
	private TableColumnInfoPresenter tableColumnPresenter;
	private TableColumnInfoView tableColumnView;
	private TableExtractionRulePresenter tabExtRulePresenter;
	private TableExtractionRuleView tabExtRuleView;

	private AdvancedKVExtractionView advKvExtractionView;
	private AdvancedKVExtractionPresenter advKvExtractionPresenter;
	private AdvancedKVExtractionLayout advancedKVLayout;
	private KVExtractionView kvExtractionView;
	private KVExtractionPresenter kvExtractionPresenter;

	private PluginView pluginView;
	private PluginPresenter pluginPresenter;
	private PluginListView pluginListView;
	private PluginListPresenter pluginListPresenter;

	private ModuleView moduleView;
	private ModulePresenter modulePresenter;
	private TableValidationRuleView tableValidationRuleView;
	private TableValidationRulePresenter tableValidationRulePresenter;
	private List<PluginDetailsDTO> pluginDetailsDTOs;
	private List<CategorisedData> batchClassPPMDetails;

	private DatabaseExportMappingPresenter databaseExportMappingPresenter;

	private DatabaseExportMappingView databaseExportMappingView;

	private ColumnExtractionRulePresenter colExtrRulePresenter;
	private ColumnExtractionRuleView colExtrRuleView;

	private List<ConnectionsDTO> connectionsList;
	private List<BatchClassDTO> batchClassList;

	private TestClassificationLayout testClassificationLayout;
	private TestClassificationMenuPresenter testClassificationMenuPresenter;
	private TestClassifyTypePresenter testClassifyTypePresenter;
	private TestClassificationGridPresenter testClassificationGridPresenter;
	private AdvancedKVExtractionLayout advTableColumnExtractionLayout;
	private AdvancedTableColumnExtractionPresenter advTableColumnExtractionPresenter;
	private AdvancedTableColumnExtractionView advTableColumnExtractionView;

	// Changes made to solve: (EPHESOFT-12858) : Validation check missing for batch class name.
	// Holds string of not allowed characters in batch class name.
	private String notAllowedBatchClassNameCharacters;
	private KeyGeneratorView keyGeneratorView;
	private KeyGeneratorPresenter<KeyGeneratorView> keyGeneratorPresenter;

	private RegexGroupSelectionGridView regexGroupSelectionGridView;
	private RegexGroupSelectionGridPresenter regexGroupSelectionGridPresenter;
	private RegexPatternSelectionGridView regexPatternSelectionGridView;
	private RegexPatternSelectionGridPresenter regexPatternSelectionGridPresenter;
	private RegexBuilderView regexBuilderView;
	private RegexBuilderPresenter regexBuilderPresenter;

	private FuzzyDBMappingPresenter fuzzyDBMappingPresenter;
	private FuzzyDBMappingView fuzzyDBMappingView;
	private CopyBatchClassView copyBatchClassView;
	private CopyBatchClassPresenter<CopyBatchClassView> copyBatchClassPresenter;

	public BatchClassManagementController(final EventBus eventBus, final DCMARemoteServiceAsync rpcService) {
		super(eventBus, rpcService);
		batchClassLayout = new BatchClassManagementLayout();
		testExtractionLayout = new TestExtractionlayout();
		testClassificationLayout = new TestClassificationLayout();
		setBatchClassExtractionModules(new ArrayList<String>());
		BatchClassManagementEventBus.registerEventBus(eventBus);
		this.initializeView();
		this.initializePresenter();
		this.addNavigationHandler();
		this.initializeTestExtractionView();
		this.initializeTestExtractionPresenter();
		this.initializeTestClassificationPresenter();

	}

	private void initializeView() {

		batchClassView = new BatchClassView();

		documentTypeView = new DocumentTypeView();
		emailView = new EmailView();
		scannerView = new WebScannerView();
		batchClassFieldView = new BatchClassFieldView();
		tableValidationRuleView = new TableValidationRuleView();
		tableInfoView = new TableInfoView();
		tableColumnView = new TableColumnInfoView();
		tabExtRuleView = new TableExtractionRuleView();
		pluginView = new PluginView();
		pluginListView = new PluginListView();
		moduleView = new ModuleView();
		kvExtractionView = new KVExtractionView();
		colExtrRuleView = new ColumnExtractionRuleView();
		indexFieldView = new IndexFieldView();
		regexValidationFieldView = new RegexValidationFieldView();
		databaseExportMappingView = new DatabaseExportMappingView();
		keyGeneratorView = new KeyGeneratorView();
		regexGroupSelectionGridView = new RegexGroupSelectionGridView();
		regexBuilderView = new RegexBuilderView();
		fuzzyDBMappingView = new FuzzyDBMappingView();
		copyBatchClassView = new CopyBatchClassView();
	}

	private void initializePresenter() {

		navigationPresenter = new BatchClassNavigatorPresenter(this, getNavigationView());
		batchClassChartPresenter = new BatchClassChartPresenter(this, getBatchClassChartView());
		batchClassPresenter = new BatchClassPresenter(this, batchClassView);

		documentTypePresenter = new DocumentTypePresenter(this, documentTypeView);
		emailPresenter = new EmailPresenter(this, emailView);
		indexFieldPresenter = new IndexFieldPresenter(this, indexFieldView);
		regexFieldPresenter = new RegexValidationFieldPresenter(this, regexValidationFieldView);
		tableValidationRulePresenter = new TableValidationRulePresenter(this, tableValidationRuleView);
		scannerPresenter = new WebScannerPresenter(this, scannerView);
		batchClassFieldPresenter = new BatchClassFieldPresenter(this, batchClassFieldView);
		tableInfoPresenter = new TableInfoPresenter(this, tableInfoView);
		tableColumnPresenter = new TableColumnInfoPresenter(this, tableColumnView);
		tabExtRulePresenter = new TableExtractionRulePresenter(this, tabExtRuleView);
		pluginPresenter = new PluginPresenter(this, pluginView);
		pluginListPresenter = new PluginListPresenter(this, pluginListView);
		modulePresenter = new ModulePresenter(this, moduleView);
		kvExtractionPresenter = new KVExtractionPresenter(this, kvExtractionView);
		colExtrRulePresenter = new ColumnExtractionRulePresenter(this, colExtrRuleView);
		databaseExportMappingPresenter = new DatabaseExportMappingPresenter(this, databaseExportMappingView);
		keyGeneratorPresenter = new KeyGeneratorPresenter<KeyGeneratorView>(this, keyGeneratorView);

		regexBuilderPresenter = new RegexBuilderPresenter(this, regexBuilderView);
		regexBuilderPresenter.bind();

		regexGroupSelectionGridPresenter = new RegexGroupSelectionGridPresenter(this, regexGroupSelectionGridView);

		fuzzyDBMappingPresenter = new FuzzyDBMappingPresenter(this, fuzzyDBMappingView);
		copyBatchClassPresenter = new CopyBatchClassPresenter<CopyBatchClassView>(this, copyBatchClassView);

		initializeAdvKVScreen();

		initializeAdvTableColumnExtractionScreen();
	}

	@Override
	public BatchClassManagementServiceAsync getRpcService() {
		DCMARemoteServiceAsync remoteService = super.getRpcService();
		if (remoteService instanceof BatchClassManagementServiceAsync) {
			return (BatchClassManagementServiceAsync) remoteService;
		} else {
			throw new UnsupportedOperationException("RPC Service of Batch Class Management is not valid");
		}
	}

	public void setOptionsPanelView(BatchClassInlineView<?> inlineView) {
		batchClassLayout.setOptionsPanelView(inlineView);
	}

	public void setListPanelView(BatchClassInlineView<?> inlineView) {
		batchClassLayout.setListPanelView(inlineView);
	}

	public void setBottomPanelView(BatchClassInlineView<?> inlineView) {
		batchClassLayout.setBottomPanelView(inlineView);
	}

	public void setBottomPanelHeader(String text) {
		batchClassLayout.setBottomPanelHeader(text);
	}

	/**
	 * @return the navigationView
	 */
	public BatchClassNavigatorView getNavigationView() {
		return batchClassLayout.getNavigationView();
	}

	/**
	 * @return the chartView
	 */
	public BatchClassChartView getBatchClassChartView() {
		return batchClassLayout.getBatchClassChartView();
	}

	/**
	 * @return the navigationPresenter
	 */
	public BatchClassNavigatorPresenter getNavigationPresenter() {
		return navigationPresenter;
	}

	/**
	 * Gets the batch class chart presenter.
	 * 
	 * @return the batch class chart presenter
	 */
	public BatchClassChartPresenter getBatchClassChartPresenter() {
		return batchClassChartPresenter;
	}

	public void setBatchClassRequiresReload(boolean requireReload) {
		this.requireReload = requireReload;
	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@Override
	public Widget createView() {
		batchClassPresenter.layout(null);
		return batchClassLayout;
	}

	@Override
	public void refresh() {
		if (currentTableInfoDTO != null) {
			currentTableInfoDTO = null;
		}

		if (currentTblExtrRule != null) {
			currentTblExtrRule = null;
		}

		if (currentEmailConfigDTO != null) {
			currentEmailConfigDTO = null;
		}

		if (selectedDocumentType != null) {
			selectedDocumentType = newBatchClass.getDocTypeByName(selectedDocumentType.getName());
		}
		if (selectedTableInfo != null) {
			if (selectedTableInfo.getDocTypeDTO() != null) {
				selectedTableInfo = newBatchClass.getDocTypeByName(selectedTableInfo.getDocTypeDTO().getName()).getTableInfoByName(
						selectedTableInfo.getName());
			}
		}
		if (selectedTableExtrRule != null) {
			TableInfoDTO tableInfo = selectedTableExtrRule.getTableInfoDTO();
			if (tableInfo != null && tableInfo.getDocTypeDTO() != null) {
				selectedTableExtrRule = newBatchClass.getDocTypeByName(tableInfo.getDocTypeDTO().getName())
						.getTableInfoByName(tableInfo.getName()).getTableExtractionRuleByName(selectedTableExtrRule.getRuleName());
			}
		}
	}

	public static class BatchClassManagementEventBus {

		private static EventBus eventBus;

		private BatchClassManagementEventBus() {
		}

		public static void fireEvent(final GwtEvent<?> event) {
			if (null != eventBus) {
				eventBus.fireEvent(event);
			}
		}

		private static void registerEventBus(final EventBus eventBus) {
			BatchClassManagementEventBus.eventBus = eventBus;
		}
	}

	/**
	 * @return the requireReload
	 */
	public boolean doBatchClassRequireReload() {
		return requireReload;
	}

	/**
	 * @return the batchClassPresenter
	 */
	public BatchClassPresenter getBatchClassPresenter() {
		return batchClassPresenter;
	}

	/**
	 * @return the documentTypePresenter
	 */
	public DocumentTypePresenter getDocumentTypePresenter() {
		return documentTypePresenter;
	}

	public void addNavigationHandler() {
		RootPanel.get().addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.isControlKeyDown() && event.getNativeKeyCode() == KeyCodes.KEY_K) {
					BatchClassManagementController.this.handleNonSiblingNavigationhandler(event);
				} else if (event.isControlKeyDown() && event.getNativeKeyCode() == KeyCodes.KEY_I) {
				}
			}
		}, KeyDownEvent.getType());
	}

	/**
	 * @return the emailPresenter.
	 */
	public EmailPresenter getEmailPresenter() {
		return emailPresenter;
	}

	public BatchClassManagementLayout getBatchClassLayout() {
		return batchClassLayout;
	}

	/**
	 * @return the indexFieldPresenter
	 */
	public IndexFieldPresenter getIndexFieldPresenter() {
		return indexFieldPresenter;
	}

	/**
	 * @return the regexFieldPresenter
	 */
	public RegexValidationFieldPresenter getRegexFieldPresenter() {
		return regexFieldPresenter;
	}

	public WebScannerPresenter getScannerPresenter() {
		return scannerPresenter;
	}

	public BatchClassFieldPresenter getBatchClassFieldPresenter() {
		return batchClassFieldPresenter;
	}

	public TableInfoPresenter getTableInfoPresenter() {
		return tableInfoPresenter;
	}

	public TableColumnInfoPresenter getTableColumnPresenter() {
		return tableColumnPresenter;
	}

	/**
	 * This method is used to save batch class.
	 */
	@EventHandler
	public void updateBatchClass(UpdateBatchClassEvent updateBatchClassEvent) {
		if (null != updateBatchClassEvent) {
			if (this.getSelectedBatchClass().isDirty()) {
				final BatchClassDTO batchClassDTO = this.getSelectedBatchClass();
				if (BatchClassManagementController.this.getCurrentBindedPresenter().isValid()) {
					this.getRpcService().updateBatchClass(batchClassDTO, new AsyncCallback<BatchClassDTO>() {

						@Override
						public void onFailure(final Throwable updateException) {
							ScreenMaskUtility.unmaskScreen();
							if (updateException.getMessage().equals(BatchClassMessages.BATCH_CLASS_UNLOCKED_BY_SUPER_USER)
									|| updateException.getMessage().equals(BatchClassMessages.BATCH_CLASS_ALREADY_DELETED)) {
								StringBuilder message = new StringBuilder();
								message.append(LocaleDictionary.getMessageValue(updateException.getMessage()));
								// message.append(batchClassDTO.getIdentifier());
								// message.append(BatchClassConstants.DOT);
								message.append(BatchClassConstants.SPACE);
								// message.append(LocaleDictionary.getLocaleDictionary().getMessageValue(MessageConstants.PERSISTANCE_ERROR));
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										message.toString(), DialogIcon.ERROR);

							} else {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_ON_SAVE_ERR_MSG),
										DialogIcon.ERROR);
							}
						}

						@Override
						public void onSuccess(final BatchClassDTO batchClassDTO) {
							loadedBatchClass = selectedBatchClass;
							newBatchClass = batchClassDTO;
							BatchClassManagementController.this.setSelectedBatchClass(batchClassDTO);
							BatchClassManagementController.this.refresh();
							BatchClassManagementController.this.getCurrentBindedPresenter().layout(null);
							BatchClassManagementController.this.getEventBus().fireEvent(
									new NavigationTreeRefreshEvent(loadedBatchClass, newBatchClass));
							generateFoldersForDocumentType(newBatchClass);
							ScreenMaskUtility.unmaskScreen();
						}
					});
				} else {
					ScreenMaskUtility.unmaskScreen();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.CURRENT_VIEW_VALIDATION_FAILURE_ERR_MSG),
							DialogIcon.ERROR);
				}
			} else {
				ScreenMaskUtility.unmaskScreen();
				Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS),
						LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_UPDATED_SUCCESSFULLY));
			}
		}
	}

	/**
	 * This method is used to save batch class.
	 */
	@EventHandler
	public void closeBatchClass(CloseBatchClassEvent closeBatchClassEvent) {
		if (null != closeBatchClassEvent) {
			final BatchClassDTO batchClassDTO = getSelectedBatchClass();
			if (batchClassDTO.isDirty()) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchClassMessages.UNSAVED_BATCH_CLASS_SAVE_NEEDED));
				confirmationDialog.getButton(PredefinedButton.OK).setText(
						LocaleDictionary.getConstantValue(BatchClassConstants.SAVE_BUTTON));
				confirmationDialog.getButton(PredefinedButton.CANCEL).setText(
						LocaleDictionary.getConstantValue(BatchClassConstants.DISCARD_BUTTON));
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						ScreenMaskUtility.maskScreen();
						if (BatchClassManagementController.this.getCurrentBindedPresenter().isValid()) {
							getRpcService().updateBatchClass(batchClassDTO, new AsyncCallback<BatchClassDTO>() {

								@Override
								public void onFailure(final Throwable updateException) {
									if (updateException.getMessage().equals(BatchClassMessages.BATCH_CLASS_UNLOCKED_BY_SUPER_USER)
											|| updateException.getMessage().equals(BatchClassMessages.BATCH_CLASS_ALREADY_DELETED)) {
										StringBuilder message = new StringBuilder();
										message.append(LocaleDictionary.getMessageValue(updateException.getMessage()));
										message.append(batchClassDTO.getIdentifier());
										message.append(BatchClassConstants.DOT);
										message.append(BatchClassConstants.SPACE);
										// message.append(LocaleDictionary.getLocaleDictionary().getMessageValue(MessageConstants.PERSISTANCE_ERROR));
										ScreenMaskUtility.unmaskScreen();
										DialogUtil.showMessageDialog(
												LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
												message.toString(), DialogIcon.ERROR);
									} else {
										ScreenMaskUtility.unmaskScreen();
										DialogUtil.showMessageDialog(
												LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
												LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_ON_SAVE_ERR_MSG),
												DialogIcon.ERROR);
									}
								}

								@Override
								public void onSuccess(final BatchClassDTO batchClassDTO) {
									newBatchClass = batchClassDTO;
									generateFoldersForDocumentType(newBatchClass);
									BatchClassManagementController.this.setBatchClassRequiresReload(true);
									List<BatchClassDTO> selectedBatchClasses = new ArrayList<BatchClassDTO>();
									selectedBatchClasses.add(batchClassDTO);
									ScreenMaskUtility.unmaskScreen();
									unlockBatchClass(selectedBatchClasses);
									BatchClassManagementController.this.getEventBus().fireEvent(new RemoveTreeViewEvent());
								}
							});
						} else {
							ScreenMaskUtility.unmaskScreen();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.CURRENT_VIEW_VALIDATION_FAILURE_ERR_MSG),
									DialogIcon.ERROR);
						}
					}

					@Override
					public void onCloseClick() {
						// TODO Auto-generated method stub
					}

					@Override
					public void onCancelClick() {
						BatchClassManagementController.this.setBatchClassRequiresReload(true);
						List<BatchClassDTO> selectedBatchClasses = new ArrayList<BatchClassDTO>();
						selectedBatchClasses.add(batchClassDTO);
						ScreenMaskUtility.unmaskScreen();
						unlockBatchClass(selectedBatchClasses);
						BatchClassManagementController.this.getEventBus().fireEvent(new RemoveTreeViewEvent());
					}
				});
			} else {
				BatchClassManagementController.this.setBatchClassRequiresReload(true);
				List<BatchClassDTO> selectedBatchClasses = new ArrayList<BatchClassDTO>();
				selectedBatchClasses.add(batchClassDTO);
				ScreenMaskUtility.unmaskScreen();
				unlockBatchClass(selectedBatchClasses);
				BatchClassManagementController.this.getEventBus().fireEvent(new RemoveTreeViewEvent());
			}
		}
	}

	private void unlockBatchClass(List<BatchClassDTO> selectedBatchClasses) {
		ScreenMaskUtility.maskScreen();
		getRpcService().clearCurrentUser(selectedBatchClasses, new AsyncCallback<BatchClassPresenter.Results>() {

			@Override
			public void onFailure(Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
				BatchClassManagementController.this.getBatchClassPresenter().layout(null);
				BatchClassManagementController.this.getBatchClassLayout().setChartView();
			}

			@Override
			public void onSuccess(Results result) {
				ScreenMaskUtility.unmaskScreen();
				BatchClassManagementController.this.getBatchClassPresenter().layout(null);
				BatchClassManagementController.this.getBatchClassLayout().setChartView();
			}
		});
	}

	public TestExtractionlayout getTestExtractionLayout() {
		return testExtractionLayout;
	}

	private void initializeTestExtractionView() {
		// dlf view & data table view will be initialised here.
		extractedDlfView = new ExtractedDlfView();
		dataTableView = new DataTableView();
	}

	private void generateFoldersForDocumentType(BatchClassDTO batchClassdto) {

		if (batchClassdto.hasDocumentType(false)) {
			final List<String> batchClassIdList = new ArrayList<String>();
			batchClassIdList.add(batchClassdto.getIdentifier());
			this.getRpcService().sampleGeneration(batchClassIdList, new AsyncCallback<Void>() {

				@Override
				public void onFailure(final Throwable errorObject) {
					ScreenMaskUtility.unmaskScreen();
				}

				@Override
				public void onSuccess(final Void errorObject) {
					ScreenMaskUtility.unmaskScreen();
				}
			});
		} else {
			ScreenMaskUtility.unmaskScreen();
		}
	}

	private void initializeTestExtractionPresenter() {
		testExtractionMenuPresenter = new TestExtractionMenuPresenter(this, this.getTestExtractionMenuView());
		testExtractionClassificationTypePresenter = new TestExtractionClassificationTypePresenter(this,
				this.getTestExtractionClassificationTypesView());
		testExtractionNavigatorPresenter = new TestExtractionNavigatorPresenter(this, this.getTestExtractionNavigatorView());
		extractedDlfPresenter = new ExtractedDlfPresenter(this, this.extractedDlfView);
		dataTablePresenter = new DataTablePresenter(this, this.dataTableView);
	}

	public ExtractedDlfPresenter getExtractedDlfPresenter() {
		return extractedDlfPresenter;
	}

	public DataTablePresenter getDataTablePresenter() {
		return dataTablePresenter;
	}

	public TestExtractionNavigatorView getTestExtractionNavigatorView() {
		return testExtractionLayout.getNavigationView();
	}

	public TestExtractionMenuView getTestExtractionMenuView() {
		return testExtractionLayout.getTestExtractionMenuView();
	}

	public TestExtractionClassificationTypeView getTestExtractionClassificationTypesView() {
		return testExtractionLayout.getTestExtractionClassificationTypeView();
	}

	public void setCenterPanelView(TestExtractionInlineView<?> centerPanelView) {
		testExtractionLayout.setCenterPanelView(centerPanelView);
	}

	public List<String> getBatchClassExtractionModules() {
		return batchClassExtractionModules;
	}

	public void setBatchClassExtractionModules(List<String> batchClassExtractionModules) {
		this.batchClassExtractionModules = batchClassExtractionModules;
	}

	public void collapseBottomPanel() {
		batchClassLayout.getBottomPanel().collapse();
	}

	/**
	 * This method is used to open child view of current view.
	 */
	@EventHandler
	public void openChildView(OpenChildViewEvent event) {
		if (null != event) {
			BatchClassCompositePresenter<?, ?> currentBindedPresenter = this.getCurrentBindedPresenter();
			if (null != currentBindedPresenter) {
				if (currentBindedPresenter.isValid()) {
					BatchClassCompositePresenter<?, ?> childPresenter = currentBindedPresenter.getChildPresenter();
					if (null != childPresenter) {
						CompositePresenterSelectionEvent selectionEvent = new CompositePresenterSelectionEvent(childPresenter);
						selectionEvent.setOnOpenButton(event.isOnOpenButton());
						BatchClassManagementEventBus.fireEvent(selectionEvent);
					}
				} else {
					ScreenMaskUtility.unmaskScreen();
					DialogUtil
							.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.VALIDATE_FIELDS_FIRST_TO_NAVIGATE),
									DialogIcon.WARNING);
				}
			}
		}
		ScreenMaskUtility.unmaskScreen();
	}

	public TableValidationRuleView getTableValidationRuleView() {
		return tableValidationRuleView;
	}

	public TableValidationRulePresenter getTableValidationRulePresenter() {
		return tableValidationRulePresenter;
	}

	/**
	 * Gets the table extraction rule presenter.
	 * 
	 * @return the table extraction rule presenter
	 */
	public TableExtractionRulePresenter getTabExtRulePresenter() {
		return tabExtRulePresenter;
	}

	/**
	 * Sets the batch class dirty flg.
	 * 
	 * @param dirtyFlg the new batch class dirty flg
	 */
	public void setBatchClassDirtyFlg(final boolean dirtyFlg) {
		this.getSelectedBatchClass().setDirty(dirtyFlg);
	}

	public Widget createAdvKVView() {
		// initializeAdvKVScreen();
		advKvExtractionPresenter.layout(null);
		return advancedKVLayout;
	}

	public void initializeAdvKVScreen() {
		advancedKVLayout = new AdvancedKVExtractionLayout();
		advKvExtractionView = new AdvancedKVExtractionView();
		advKvExtractionPresenter = new AdvancedKVExtractionPresenter(this, advKvExtractionView);
	}

	public Widget createAdvTableColumnExtractionView() {
		advTableColumnExtractionPresenter.layout(getSelectedTableInfo());
		advTableColumnExtractionLayout.getBottomsPanel().setExpanded(true);
		return advTableColumnExtractionLayout;
	}

	public void initializeAdvTableColumnExtractionScreen() {
		advTableColumnExtractionLayout = new AdvancedKVExtractionLayout(
				LocaleDictionary.getConstantValue(BatchClassConstants.SET_COORDINATES));
		advTableColumnExtractionView = new AdvancedTableColumnExtractionView();
		advTableColumnExtractionPresenter = new AdvancedTableColumnExtractionPresenter(this, advTableColumnExtractionView);
	}

	public void setAdvKVOptionsPanelView(AdvancedKVExtractionInlineView<?> inlineView) {
		advancedKVLayout.setAdvKVOptionsPanelView(inlineView);
	}

	public void setAdvKVListPanelView(AdvancedKVExtractionInlineView<?> inlineView) {
		advancedKVLayout.setAdvKVListPanelView(inlineView);
	}

	public void setAdvKVBottomPanelView(AdvancedKVExtractionInlineView<?> inlineView) {
		advancedKVLayout.setAdvKVBottomPanelView(inlineView);
	}

	public void setAdvKVInputPanelView(AdvancedKVExtractionInlineView<?> inlineView) {
		advancedKVLayout.setAdvKVInputPanelView(inlineView);
	}

	public void setAdvKVDropFilePanelView(AdvancedKVExtractionInlineView<?> inlineView) {
		advancedKVLayout.setAdvKVDropFilePanelView(inlineView);
	}

	public AdvancedKVExtractionLayout getAdvancedKVLayout() {
		return advancedKVLayout;
	}

	public AdvancedKVExtractionPresenter getAdvKvExtractionPresenter() {
		return advKvExtractionPresenter;
	}

	public AdvancedKVExtractionView getAdvKvExtractionView() {
		return advKvExtractionView;
	}

	public PluginPresenter getPluginPresenter() {
		return pluginPresenter;
	}

	public PluginListPresenter getPluginListPresenter() {
		return pluginListPresenter;
	}

	public ModulePresenter getModulePresenter() {
		return modulePresenter;
	}

	public void setAllPluginDetailsDTOs(List<PluginDetailsDTO> pluginDetailsDTOs) {
		this.pluginDetailsDTOs = pluginDetailsDTOs;
	}

	public KVExtractionPresenter getKvExtractionPresenter() {
		return kvExtractionPresenter;
	}

	public int getBottomPanelAbsoluteLeft() {
		return batchClassLayout.getBottomContentPanel().getAbsoluteLeft();
	}

	public int getBottomPanelAbsoluteTop() {
		return batchClassLayout.getBottomContentPanel().getAbsoluteTop();
	}

	public int getBottomPanelAbsoluteHeight() {
		return batchClassLayout.getBottomContentPanel().getOffsetHeight();
	}

	public int getBottomPanelAbsoluteWidth() {
		return batchClassLayout.getBottomContentPanel().getOffsetWidth();

	}

	public int getListPanelAbsoluteLeft() {
		return batchClassLayout.getGridPanel().getAbsoluteLeft();
	}

	public int getListPanelAbsoluteTop() {
		return batchClassLayout.getGridPanel().getAbsoluteTop();
	}

	public int getListPanelOffsetHeight() {
		return batchClassLayout.getGridPanel().getOffsetHeight();
	}

	public int getListPanelAbsoluteWidth() {
		return batchClassLayout.getGridPanel().getOffsetWidth();
	}

	public int getLeftPanelAbsoluteWidth() {
		return batchClassLayout.getLeftPanel().getOffsetWidth();
	}

	public ColumnExtractionRulePresenter getColExtrRulePresenter() {
		return colExtrRulePresenter;
	}

	/**
	 * Validate Dependencies of selected Batch Class Before deployment.
	 * 
	 * @param deployWorkFlow
	 */

	@EventHandler
	public void validateDependencies(final ValidateNDeployWorkFlow deployWorkFlowEvent) {
		if (null != deployWorkFlowEvent) {
			if (this.getCurrentBindedPresenter().isValid()) {

				// Check if Clean Up Plugin exists
				getRpcService().getAllPluginDetailDTOs(new AsyncCallback<List<PluginDetailsDTO>>() {

					@Override
					public void onFailure(Throwable arg0) {
						ScreenMaskUtility.unmaskScreen();
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_GET_PLUGINs_LIST), DialogIcon.ERROR);
					}

					@Override
					public void onSuccess(List<PluginDetailsDTO> pluginDetailsDTOs) {
						ScreenMaskUtility.unmaskScreen();
						ValidateWorkflowUtil.setAllPluginNames(pluginDetailsDTOs);
						if (ValidateWorkflowUtil.validateDependencies(getSelectedBatchClass())) {
							deployWorkflow(deployWorkFlowEvent.isGridWorkFlow());
						}
					}
				});
			} else {
				ScreenMaskUtility.unmaskScreen();
				DialogUtil
						.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.CURRENT_VIEW_VALIDATION_FAILURE_ERR_MSG),
								DialogIcon.ERROR);
			}
		} else {
			ScreenMaskUtility.unmaskScreen();
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.NOTHING_TO_DEPLOY), DialogIcon.INFO);
		}

	}

	private void deployWorkflow(final boolean isGridWorkflow) {
		ScreenMaskUtility.maskScreen(LocaleDictionary.getMessageValue(BatchClassMessages.DEPLOYING_WORKFLOW));
		getRpcService().createAndDeployWorkflowJPDL(getSelectedBatchClass().getIdentifier(), getSelectedBatchClass(), isGridWorkflow,
				new AsyncCallback<BatchClassDTO>() {

					@Override
					public void onFailure(final Throwable updateException) {

						if (updateException.getMessage().equals(BatchClassMessages.BATCH_CLASS_UNLOCKED_BY_SUPER_USER)
								|| updateException.getMessage().equals(BatchClassMessages.BATCH_CLASS_ALREADY_DELETED)) {
							StringBuilder message = new StringBuilder();
							message.append(LocaleDictionary.getMessageValue(updateException.getMessage()));
							message.append(BatchClassConstants.SPACE);
							ScreenMaskUtility.unmaskScreen();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
									message.toString(), DialogIcon.ERROR);
						} else {
							DialogUtil.showMessageDialog(
									LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.AN_ERROR_OCCURED_WHILE_DEPLOYING_THE_WORKFLOW),
									DialogIcon.ERROR);
							ScreenMaskUtility.unmaskScreen();
						}
					}

					@Override
					public void onSuccess(final BatchClassDTO batchClassDTO) {
						loadedBatchClass = selectedBatchClass;
						newBatchClass = batchClassDTO;
						setSelectedBatchClass(batchClassDTO);
						refresh();
						getCurrentBindedPresenter().layout(null);
						getEventBus().fireEvent(new NavigationTreeRefreshEvent(loadedBatchClass, newBatchClass));
						batchClassDTO.setDeployed(true);
						Message.display(LocaleDictionary.getMessageValue(BatchClassMessages.MESSAGE),
								LocaleDictionary.getMessageValue(BatchClassMessages.WORKFLOW_DEPLOYED_SUCCESSFULLY));
						ScreenMaskUtility.unmaskScreen();
					}
				});
	}

	public TestClassificationLayout getTestClassificationLayout() {
		return testClassificationLayout;
	}

	@EventHandler
	public void handleBatchClassRefreshEvent(RefreshBatchClassScreenEvent refreshCurrentBindedPresenter) {
		this.getCurrentBindedPresenter().refresh();
	}

	@EventHandler
	public void handleCurrentViewRegexValidationEvent(final CurrentViewRegexValidationEvent currentViewRegexValidationEvent) {
		if (null != currentViewRegexValidationEvent) {
			BatchClassCompositePresenter<?, ?> currentBindedPresenter = getCurrentBindedPresenter();
			if (null != currentBindedPresenter) {
				BatchClassCompositeView<?> view = currentBindedPresenter.getView();
				if (null != view) {
					BatchClassInlineView<?> listPanelView = view.getListPanelView();
					if (listPanelView instanceof HasResizableGrid) {
						Grid<?> grid = ((HasResizableGrid) listPanelView).getGrid();
						if (null != grid) {
							ViewUtil.validateRegex(getRpcService(), grid);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void handleModelSelectionEvent(ModelSelectionEvent selectionEvent) {
		if (null != selectionEvent) {
			setCurrentSelectedObject(selectionEvent.getSelectedObject());
			setCurrentValueProvider(selectionEvent.getCurrentValueProvider());
		}
	}

	@EventHandler
	public void handleBatchClassLoadEvent(BatchClassLoadEvent batchClassLoadEvent) {
		batchClassLayout.setNavigationView();
	}

	public List<CategorisedData> getBatchClassPPMDetail(String identifier) {
		List<CategorisedData> ppmDetail = new ArrayList<CategorisedData>();
		for (CategorisedData categorisedData : batchClassPPMDetails) {
			if (categorisedData.getCategory().equals(identifier)) {
				ppmDetail.add(categorisedData);
				break;
			}
		}
		return ppmDetail;
	}

	public List<CategorisedData> getBatchClassPPMDetails() {
		return batchClassPPMDetails;
	}

	public void setBatchClassPPMDetails(List<CategorisedData> batchClassPPMDetails) {
		this.batchClassPPMDetails = batchClassPPMDetails;
	}

	public AdvancedKVExtractionLayout getAdvTableColumnExtractionLayout() {
		return advTableColumnExtractionLayout;
	}

	public AdvancedTableColumnExtractionPresenter getAdvTableColumnExtractionPresenter() {
		return advTableColumnExtractionPresenter;
	}

	private void initializeTestClassificationPresenter() {
		testClassificationMenuPresenter = new TestClassificationMenuPresenter(this, this.getTestClassificationMenuView());
		testClassifyTypePresenter = new TestClassifyTypePresenter(this, this.getTestClassifyTypeView());
		testClassificationGridPresenter = new TestClassificationGridPresenter(this, this.getTestClassificationGridView());
	}

	public TestClassificationMenuView getTestClassificationMenuView() {
		return testClassificationLayout.getTestClassificationMenuView();
	}

	public TestClassifyTypeView getTestClassifyTypeView() {
		return testClassificationLayout.getTestClassifyTypeView();
	}

	public TestClassificationGridView getTestClassificationGridView() {
		return testClassificationLayout.getTestClassificationGridView();
	}

	public void resizeGrid(int height, int width) {
		testClassificationLayout.setBorderLayoutContainer(height, width);
	}

	/**
	 * @return the databaseExportMappingPresenter
	 */
	public DatabaseExportMappingPresenter getDatabaseExportMappingPresenter() {
		return databaseExportMappingPresenter;
	}

	/**
	 * @return the databaseExportMappingView
	 */
	public DatabaseExportMappingView getDatabaseExportMappingView() {
		return databaseExportMappingView;
	}

	/**
	 * @return the connectionsList
	 */
	public List<ConnectionsDTO> getConnectionsList() {
		return connectionsList;
	}

	/**
	 * @param connectionsList the connectionsList to set
	 */
	public void setConnectionsList(List<ConnectionsDTO> connectionsList) {
		this.connectionsList = connectionsList;
	}

	public void setBorderLayoutContainer(int offsetHeight, int offsetWidth) {
		testExtractionLayout.setBorderLayoutContainer(offsetHeight, offsetWidth);
	}

	public void clearNoExtractionResultView() {
		testExtractionLayout.clearNoExtractionResultView();
	}

	public void setNotAllowedBatchClassCharacters(String notAllowedBatchClassCharacters) {
		this.notAllowedBatchClassNameCharacters = notAllowedBatchClassCharacters;
	}

	public String getNotAllowedBatchClassCharacters() {
		return notAllowedBatchClassNameCharacters;
	}

	public KeyGeneratorView getKeyGeneratorView() {
		return keyGeneratorView;
	}

	public KeyGeneratorPresenter<KeyGeneratorView> getKeyGeneratorPresenter() {
		return keyGeneratorPresenter;
	}

	public RegexGroupSelectionGridView getRegexGroupSelectionGridView() {
		return regexGroupSelectionGridView;
	}

	public RegexGroupSelectionGridPresenter getRegexGroupSelectionGridPresenter() {
		return regexGroupSelectionGridPresenter;
	}

	public RegexPatternSelectionGridView getRegexPatternSelectionGridView() {
		return regexPatternSelectionGridView;
	}

	public RegexBuilderView getRegexBuilderView() {
		return regexBuilderView;
	}

	public void initialiseRegexPatternGrid() {
		regexPatternSelectionGridView = new RegexPatternSelectionGridView();
		regexPatternSelectionGridPresenter = new RegexPatternSelectionGridPresenter(this, regexPatternSelectionGridView);
		regexPatternSelectionGridPresenter.bind();
	}

	/**
	 * @return the fuzzyDBMappingPresenter
	 */
	public FuzzyDBMappingPresenter getFuzzyDBMappingPresenter() {
		return fuzzyDBMappingPresenter;
	}

	/**
	 * @return the fuzzyDBMappingView
	 */
	public FuzzyDBMappingView getFuzzyDBMappingView() {
		return fuzzyDBMappingView;
	}

	public CopyBatchClassView getCopyBatchClassView() {
		return copyBatchClassView;
	}

	public CopyBatchClassPresenter<CopyBatchClassView> getCopyBatchClassPresenter() {
		return copyBatchClassPresenter;
	}

	public IndexFieldView getIndexFieldView() {
		return indexFieldView;
	}

	public DocumentTypeView getDocumentTypeView() {
		return documentTypeView;
	}

	/**
	 * Gets the batch class view.
	 * 
	 * @return the batch class view
	 */
	public BatchClassView getBatchClassView() {
		return batchClassView;
	}

	public List<BatchClassDTO> getBatchClassList() {
		return batchClassList;
	}

	public void setBatchClassList(List<BatchClassDTO> batchClassList) {
		this.batchClassList = batchClassList;
	}
}
