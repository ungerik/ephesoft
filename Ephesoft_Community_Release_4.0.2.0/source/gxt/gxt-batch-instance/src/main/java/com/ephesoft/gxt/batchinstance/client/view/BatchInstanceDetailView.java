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

package com.ephesoft.gxt.batchinstance.client.view;

import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceMessages;
import com.ephesoft.gxt.batchinstance.client.presenter.BatchInstanceDetailPresenter;
import com.ephesoft.gxt.batchinstance.client.shared.constants.BatchInfoConstants;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.BorderLayoutContainer;
import com.ephesoft.gxt.core.client.ui.widget.DetailGrid;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.DetailsDTO;
import com.ephesoft.gxt.core.shared.dto.WorkflowDetailDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.NumberUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ProgressBar;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.CellClickEvent.CellClickHandler;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent.CollapseItemHandler;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent.ExpandItemHandler;

public class BatchInstanceDetailView extends View<BatchInstanceDetailPresenter> {

	interface Binder extends UiBinder<Widget, BatchInstanceDetailView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected HorizontalPanel detailsContainer;

	@UiField
	protected BorderLayoutContainer borderLayoutContainer;

	@UiField
	protected ContentPanel troubleshootView;

	@UiField
	protected DetailGrid batchInstanceDetailsGrid;

	@UiField
	protected TroubleshootView troubleshootPanel;

	protected FlexTable batchDetailsTable;

	protected FlexTable batchProgressTable;

	protected Label batchIdentifierLabel;

	protected Label batchInstanceNameLabel;

	protected Label batchClassNameLabel;

	protected Label uncFolderPathLabel;

	protected Label batchPriorityLabel;

	protected Label currentStatusLabel;

	protected Label errorCauseLabel;

	protected Label batchIdentifierValueLabel;

	protected Label batchInstanceNameValueLabel;

	protected Label batchClassNameValueLabel;

	protected Label uncFolderPathValueLabel;

	protected Label batchPriorityValueLabel;

	protected Label currentStatusValueLabel;

	protected Label errorCauseValueLabel;

	private String enhancedLoggingSwitch;

	private Hidden batchInstanceLogFilePath;

	private Hidden batchInstanceIdentifierField;

	private FormPanel logFileDownloadPanel;

	/**
	 * logFliePathPanel VerticalPanel.
	 */
	protected VerticalPanel logFliePathPanel;

	/**
	 * LOG_FILE_DOWNLOAD_ACTION {@link String}.
	 */
	private static final String LOG_FILE_DOWNLOAD_ACTION = "dcma-gwt-batch-instance/logFileDownload";

	private BatchInstanceDTO selectedBatchInstance;

	@UiField
	protected VerticalLayoutContainer batchInstanceProgressContainer;

	public BatchInstanceDetailView() {
		super();
		initWidget(binder.createAndBindUi(this));
		batchIdentifierLabel = new Label(LocaleDictionary.getConstantValue(BatchInstanceConstants.DETAILS_GRID_BATCH_ID_TITLE));
		currentStatusLabel = new Label(LocaleDictionary.getConstantValue(BatchInstanceConstants.DETAILS_GRID_BATCH_STATE_TITLE));
		batchClassNameLabel = new Label(LocaleDictionary.getConstantValue(BatchInstanceConstants.DETAILS_GRID_BATCH_CLASS_NAME_TITLE));
		uncFolderPathLabel = new Label(LocaleDictionary.getConstantValue(BatchInstanceConstants.DETAILS_GRID_BATCH_UNC_PATH_TITLE));
		batchPriorityLabel = new Label(LocaleDictionary.getConstantValue(BatchInstanceConstants.DETAILS_GRID_BATCH_PRIORITY_TITLE));
		batchInstanceNameLabel = new Label(LocaleDictionary.getConstantValue(BatchInstanceConstants.DETAILS_GRID_BATCH_NAME_TITLE));
		errorCauseLabel = new Label(LocaleDictionary.getConstantValue(BatchInstanceConstants.DETAILS_GRID_BATCH_ERROR_CAUSE_TITLE));
		batchIdentifierValueLabel = new Label();
		currentStatusValueLabel = new Label();
		batchClassNameValueLabel = new Label();
		uncFolderPathValueLabel = new Label();
		batchPriorityValueLabel = new Label();
		batchInstanceNameValueLabel = new Label();
		errorCauseValueLabel = new Label();
		batchIdentifierValueLabel.setStyleName("labelCSSDetailGXT");
		currentStatusValueLabel.setStyleName("labelCSSDetailGXT");
		batchClassNameValueLabel.setStyleName("labelCSSDetailGXT");
		uncFolderPathValueLabel.setStyleName("labelCSSDetailGXT");
		batchPriorityValueLabel.setStyleName("labelCSSDetailGXT");
		batchInstanceNameValueLabel.setStyleName("labelCSSDetailGXT");
		errorCauseValueLabel.setStyleName("labelCSSDetailGXT");
		batchIdentifierLabel.addStyleName(CoreCommonConstants.LABEL_CSS_DETAIL);
		currentStatusLabel.addStyleName(CoreCommonConstants.LABEL_CSS_DETAIL);
		batchClassNameLabel.addStyleName(CoreCommonConstants.LABEL_CSS_DETAIL);
		uncFolderPathLabel.addStyleName(CoreCommonConstants.LABEL_CSS_DETAIL);
		batchPriorityLabel.addStyleName(CoreCommonConstants.LABEL_CSS_DETAIL);
		batchInstanceNameLabel.addStyleName(CoreCommonConstants.LABEL_CSS_DETAIL);
		detailsContainer.addStyleName(CoreCommonConstants.LABEL_CSS_DETAIL);
		errorCauseLabel.addStyleName(CoreCommonConstants.LABEL_CSS_DETAIL);
		batchInstanceDetailsGrid.setStyleName("bim_detail_grid");
		batchInstanceDetailsGrid.setHideHeaders(true);
		batchInstanceDetailsGrid.setBorders(true);
		borderLayoutContainer.forceLayout();
		borderLayoutContainer.addStyleName("subBottomPanel");
		troubleshootView.setHeadingText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_HEADER));

		addLayoutHandlers();
		initializeFormPanel();
	}

	@Override
	public void initialize() {
	}

	/**
	 * @param batchIdentifierValueLabel the batchIdentifierValueLabel to set
	 */
	public void setBatchIdentifierValueLabel(String batchIdentifierValueLabel) {
		this.batchIdentifierValueLabel.setText(batchIdentifierValueLabel);
	}

	/**
	 * @param batchInstanceNameValueLabel the batchInstanceNameValueLabel to set
	 */
	public void setBatchInstanceNameValueLabel(String batchInstanceNameValueLabel) {
		this.batchInstanceNameValueLabel.setText(batchInstanceNameValueLabel);
	}

	/**
	 * @param batchClassNameValueLabel the batchClassNameValueLabel to set
	 */
	public void setBatchClassNameValueLabel(String batchClassNameValueLabel) {
		this.batchClassNameValueLabel.setText(batchClassNameValueLabel);
	}

	/**
	 * @param uncFolderPathValueLabel the uncFolderPathValueLabel to set
	 */
	public void setUncFolderPathValueLabel(String uncFolderPathValueLabel) {
		this.uncFolderPathValueLabel.setText(uncFolderPathValueLabel);
	}

	/**
	 * @param batchPriorityValueLabel the batchPriorityValueLabel to set
	 */
	public void setBatchPriorityValueLabel(String batchPriorityValueLabel) {
		this.batchPriorityValueLabel.setText(batchPriorityValueLabel);
	}

	/**
	 * @param currentStatusValueLabel the currentStatusValueLabel to set
	 */
	public void setCurrentStatusValueLabel(String currentStatusValueLabel) {
		this.currentStatusValueLabel.setText(currentStatusValueLabel);
	}

	/**
	 * @param errorCauseValueLabel the errorCauseValueLabel to set
	 */
	public void setErrorCauseValueLabel(String errorCauseValueLabel) {
		this.errorCauseValueLabel.setText(errorCauseValueLabel);
	}

	public void initializeProgressBars(WorkflowDetailDTO result) {
		batchInstanceProgressContainer.clear();
		int index = 0;
		if (null != result) {
			List<String> executedModuleList = result.getExecutedModuleList();
			Map<String, List<String>> modulePluginMap = result.getModulePluginMap();

			// For all executed modules.
			if (!CollectionUtil.isEmpty(executedModuleList)) {
				double percentageComplete = 1.0;
				for (String moduleName : executedModuleList) {
					if (!StringUtil.isNullOrEmpty(moduleName)) {
						ProgressBar bar = new ProgressBar();

						// Change display of module/plugin keywords to camelcase in batch progress bar.
						bar.updateProgress(
								percentageComplete,
								getProgessString(moduleName.replace(CoreCommonConstant.MODULE_KEYWORD_LOWER_CASE,
										CoreCommonConstant.MODULE_KEYWORD_CAMEL_CASE), percentageComplete));
						bar.setTitle(getPluginExecutionTitle(modulePluginMap.get(moduleName), null, false));
						bar.setValue(percentageComplete);
						bar.setStyleName("greenbackground");
						batchProgressTable.setWidget(index, 0, bar);
						index++;
					}
				}
			}

			// For currently executing module.
			String currentModule = result.getCurrentExecutingModule();
			String currentExecutingPlugin = result.getCurrentExecutingPlugin();
			if (!StringUtil.isNullOrEmpty(currentModule)) {
				ProgressBar bar = new ProgressBar();
				List<String> pluginsOfCurrentModule = modulePluginMap.get(currentModule);
				double percentageComplete = getPercentageComplete(pluginsOfCurrentModule, result.getNonExecutedPluginList(),
						currentExecutingPlugin);

				// Change display of module/plugin keywords to camelcase in batch progress bar.
				bar.updateProgress(
						percentageComplete,
						getProgessString(currentModule.replace(CoreCommonConstant.MODULE_KEYWORD_LOWER_CASE,
								CoreCommonConstant.MODULE_KEYWORD_CAMEL_CASE), percentageComplete));
				bar.setValue(percentageComplete);
				if (pluginsOfCurrentModule.size() == 0) {
					bar.setTitle(BatchInstanceConstants.NO_PLUGINS_FOR_EXECUTION);
				} else {
					bar.setTitle(getPluginExecutionTitle(pluginsOfCurrentModule, currentExecutingPlugin, false));
				}
				batchProgressTable.setWidget(index, 0, bar);
				index++;
			}

			// For all non executed modules.
			List<String> nonExecutedModuleList = result.getNonExecutedModuleList();
			if (null != nonExecutedModuleList) {
				for (String moduleName : nonExecutedModuleList) {
					ProgressBar bar = new ProgressBar();

					// Change display of module/plugin keywords to camelcase in batch progress bar.
						bar.updateProgress(0, moduleName.replace(CoreCommonConstant.MODULE_KEYWORD_LOWER_CASE,
							CoreCommonConstant.MODULE_KEYWORD_CAMEL_CASE));
					bar.setTitle(getPluginExecutionTitle(modulePluginMap.get(moduleName), null, true));
					bar.setValue(0.0);
					// bar.setWidth("50%");
					bar.setStyleName("redbackground");
					batchProgressTable.setWidget(index, 0, bar);
					index++;
				}
			}
		}
		batchInstanceProgressContainer.add(batchProgressTable);
	}

	/**
	 * Provides a progress bar string for a module type.
	 * 
	 * @param name {@link String}
	 * @param percentage double
	 * @return {@link String}
	 */
	private String getProgessString(String name, double percentage) {
		double newPercentage = percentage * CoreCommonConstant.PERCENTAGE_MULTIPLIER;

		// Edited to round of percentage to upto 2 decimal places.
		return StringUtil.concatenate(name, CoreCommonConstant.COLON, NumberUtil.getRoundedValue(newPercentage), CoreCommonConstant.PERCENTAGE_SYMBOL);
	}

	public void initializeBatchInstanceDetails(List<DetailsDTO> detailsList) {
		batchProgressTable = new FlexTable();
		// batchProgressTable.setWidth("100%");
		batchProgressTable.setCellSpacing(0);
		batchProgressTable.setBorderWidth(0);
		batchProgressTable.addStyleName("borderResultTableGXTProgress");
		batchInstanceDetailsGrid.setData(detailsList);

	}

	private String getPluginExecutionTitle(final List<String> listOfPlugins, final String currentExecutingPlugin,
			boolean notStartedModule) {
		boolean executedListHeaderAdded = false;
		StringBuilder pluginDetailsTitle = new StringBuilder();
		if (!CollectionUtil.isEmpty(listOfPlugins)) {
			int pluginCount = listOfPlugins.size();
			int index = 0;
			String pluginName;
			if (!notStartedModule) {
				for (; index < pluginCount; index++) {
					pluginName = listOfPlugins.get(index);
					if (!StringUtil.isNullOrEmpty(pluginName) && pluginName.equalsIgnoreCase(currentExecutingPlugin)) {
						if (0 != pluginDetailsTitle.length()) {
							pluginDetailsTitle.append(CoreCommonConstant.NEW_LINE);
							pluginDetailsTitle.append(CoreCommonConstant.NEW_LINE);
						}
						pluginDetailsTitle.append(LocaleDictionary.getConstantValue(BatchInstanceConstants.CURRENTLY_EXECUTING_PLUGINS));
						pluginDetailsTitle.append(CoreCommonConstant.NEW_LINE);
						
						// Change display of module/plugin keywords to camelcase in batch progress bar.
						pluginName = pluginName.replace(CoreCommonConstant.PLUGIN_KEYWORD_LOWER_CASE,
								CoreCommonConstant.PLUGIN_KEYWORD_CAMEL_CASE);
						pluginDetailsTitle.append(pluginName);
						index++;
						break;
					} else {
						if (!executedListHeaderAdded) {
							if (0 != pluginDetailsTitle.length()) {
								pluginDetailsTitle.append(CoreCommonConstant.NEW_LINE);
							}
							pluginDetailsTitle.append(LocaleDictionary.getConstantValue(BatchInstanceConstants.EXECUTED_PLUGINS));
							executedListHeaderAdded = true;
						}
						pluginDetailsTitle.append(CoreCommonConstant.NEW_LINE);

						// Change display of module/plugin keywords to camelcase in batch progress bar.
						pluginName = pluginName.replace(CoreCommonConstant.PLUGIN_KEYWORD_LOWER_CASE,
								CoreCommonConstant.PLUGIN_KEYWORD_CAMEL_CASE);
						pluginDetailsTitle.append(pluginName);
					}
				}
			}
			if (index < pluginCount) {
				if (0 != pluginDetailsTitle.length()) {
					pluginDetailsTitle.append(CoreCommonConstant.NEW_LINE);
					pluginDetailsTitle.append(CoreCommonConstant.NEW_LINE);
				}
				pluginDetailsTitle.append(LocaleDictionary.getConstantValue(BatchInstanceConstants.PENDING_EXECUTION_PLUGINS));
				for (int indexNonExecuted = index; indexNonExecuted < pluginCount; indexNonExecuted++) {
					pluginDetailsTitle.append(CoreCommonConstant.NEW_LINE);
					pluginName = listOfPlugins.get(indexNonExecuted);

					// Change display of module/plugin keywords to camelcase in batch progress bar.
					pluginName = pluginName.replace(CoreCommonConstant.PLUGIN_KEYWORD_LOWER_CASE,
								CoreCommonConstant.PLUGIN_KEYWORD_CAMEL_CASE);
					pluginDetailsTitle.append(pluginName);
				}
			}
		}
		return pluginDetailsTitle.toString();
	}

	private double getPercentageComplete(final List<String> listOfPlugins, final List<String> listOfNonExecutedPlugins,
			String currentExecutingPlugin) {
		double percentageCompleted = 0.0;
		if (!CollectionUtil.isEmpty(listOfPlugins)) {
			int totalPluginCount = listOfPlugins.size();
			int executedPluginCount = totalPluginCount;
			if (!StringUtil.isNullOrEmpty(currentExecutingPlugin)) {
				executedPluginCount--;
			}
			if (!CollectionUtil.isEmpty(listOfNonExecutedPlugins)) {
				int nonExecutedPluginCount = listOfNonExecutedPlugins.size();
				executedPluginCount = executedPluginCount - nonExecutedPluginCount;
			}
			percentageCompleted = (double) (executedPluginCount) / totalPluginCount;
		}
		return percentageCompleted;
	}

	public void clearPane() {
		detailsContainer.remove(batchInstanceDetailsGrid);
		detailsContainer.remove(batchProgressTable);
	}

	public TroubleshootView getTroubleshootPanel() {
		return troubleshootPanel;
	}

	public void forceLayout() {
		borderLayoutContainer.forceLayout();
	}

	private void addLayoutHandlers() {
		borderLayoutContainer.addCollapseHandler(new CollapseItemHandler<ContentPanel>() {

			@Override
			public void onCollapse(CollapseItemEvent<ContentPanel> event) {
				detailsContainer.setWidth(BatchInfoConstants.WIDTH_100_PERCENT);
				detailsContainer.add(batchInstanceProgressContainer);
				detailsContainer.setCellHorizontalAlignment(batchInstanceProgressContainer, HasHorizontalAlignment.ALIGN_CENTER);
			}
		});
		borderLayoutContainer.addExpandHandler(new ExpandItemHandler<ContentPanel>() {

			@Override
			public void onExpand(ExpandItemEvent<ContentPanel> event) {
				detailsContainer.setWidth(BatchInfoConstants.WIDTH_89_PERCENT);
				detailsContainer.remove(batchInstanceProgressContainer);
				Timer timer = new Timer() {

					@Override
					public void run() {
						troubleshootPanel.forceLayout();
					}
				};
				timer.schedule(10);
			}
		});

		troubleshootView.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				Timer timer = new Timer() {

					@Override
					public void run() {
						troubleshootPanel.forceLayout();
					}

				};
				timer.schedule(10);
			}
		});

		borderLayoutContainer.addExpandHandler(new ExpandItemHandler<ContentPanel>() {

			@Override
			public void onExpand(ExpandItemEvent<ContentPanel> event) {
				troubleshootPanel.setDownloadOptionSelected();
				troubleshootPanel.resizeTroubleshootPanel();
			}

		});
	}

	public void setBatchInstanceLogFilePath(final String filePath) {
		batchInstanceLogFilePath.setValue(filePath);
	}

	public void setBatchInstanceIdentifier(final String batchID) {
		batchInstanceIdentifierField.setValue(batchID);
	}

	/**
	 * Getter for the logFileDownloadPanel.
	 * 
	 * @return {@link FormPanel}
	 */
	public FormPanel getLogFileDownloadPanel() {
		return logFileDownloadPanel;
	}

	private void initializeFormPanel() {
		batchInstanceLogFilePath = new Hidden(BatchInfoConstants.BI_LOG_FILE_PATH);
		batchInstanceIdentifierField = new Hidden(BatchInfoConstants.BATCH_INSTANCE_IDENTIFIER);
		logFileDownloadPanel = new FormPanel();
		logFliePathPanel = new VerticalPanel();

		logFileDownloadPanel.add(logFliePathPanel);
		logFileDownloadPanel.setMethod(FormPanel.METHOD_POST);
		logFileDownloadPanel.setAction(LOG_FILE_DOWNLOAD_ACTION);
		batchInstanceLogFilePath = new Hidden(BatchInfoConstants.BI_LOG_FILE_PATH);

		logFliePathPanel.add(batchInstanceIdentifierField);
		logFliePathPanel.add(batchInstanceLogFilePath);
		logFileDownloadPanel.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(final SubmitEvent event) {
			}
		});

		logFileDownloadPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(final SubmitCompleteEvent event) {

				if (event.getResults().toLowerCase().indexOf(BatchInstanceConstants.ERROR) > -1) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(LocaleCommonConstants.ERROR_TITLE), LocaleDictionary.getMessageValue(BatchInstanceMessages.LOG_FILE_DOWNLOAD_ERROR_MESSAGE), DialogIcon.ERROR);
//					return;
				}
			}
		});

	}

	/**
	 * Setter for the enhancedLoggingSwitch.
	 * 
	 * @param enhancedLoggingSwitch {@link String}
	 */
	public void setEnhancedLoggingSwitch(final String enhancedLoggingSwitch) {
		this.enhancedLoggingSwitch = enhancedLoggingSwitch;
	}

	public void addErrorLogsDownloadHandler() {
		// If Enhanced Error logging switch is ON then make the status label as Anchor.
		if (BatchInfoConstants.STRING_ON.equalsIgnoreCase(enhancedLoggingSwitch)) {
			batchInstanceDetailsGrid.addCellClickHandler(new CellClickHandler() {

				/* (non-Javadoc)
				 * @see com.sencha.gxt.widget.core.client.event.CellClickEvent.CellClickHandler#onCellClick(com.sencha.gxt.widget.core.client.event.CellClickEvent)
				 */
				@Override
				public void onCellClick(CellClickEvent event) {
					// TODO Auto-generated method stub
					int selectedCellIndex = event.getCellIndex();
					if (batchInstanceDetailsGrid.getColumnModel().getValueProvider(selectedCellIndex).equals(DetailGrid.TITLE_PROVIDER)){
						final DetailsDTO model = batchInstanceDetailsGrid.getStore().get(event.getRowIndex());
						if (null != model) {
							String errorCauseTitle = LocaleDictionary.getConstantValue(BatchInstanceConstants.DETAILS_GRID_BATCH_ERROR_CAUSE_TITLE);
							if (model.getTitle().equals(errorCauseTitle) && !StringUtil.isNullOrEmpty(model.getValue())) {
								presenter.showErrorLogMessageDialog(selectedBatchInstance.getBatchIdentifier());
							}
						}
					}
				}
			});
		}
	}

	public void setSelectedBatchInstance(BatchInstanceDTO selectedBatchInstance) {
		this.selectedBatchInstance = selectedBatchInstance;

	}

	public void initializeProgressBars(List<String> executedModuleList) {
		batchInstanceProgressContainer.clear();
		int index = 0;
		if (!CollectionUtil.isEmpty(executedModuleList)) {
			double percentageComplete = 1.0;
			for (String moduleName : executedModuleList) {
				if (!StringUtil.isNullOrEmpty(moduleName)) {
					ProgressBar bar = new ProgressBar();
					bar.updateProgress(percentageComplete, getProgessString(moduleName, percentageComplete));
					bar.setValue(percentageComplete);
					bar.setStyleName("greenbackground");
					batchProgressTable.setWidget(index, 0, bar);
					index++;
				}
			}
		}

		batchInstanceProgressContainer.add(batchProgressTable);
	}

	/**
	 * Clears the progress bar conatiner.
	 */
	public void clearProgressBarsContainer() {
		batchInstanceProgressContainer.clear();
	}
}
