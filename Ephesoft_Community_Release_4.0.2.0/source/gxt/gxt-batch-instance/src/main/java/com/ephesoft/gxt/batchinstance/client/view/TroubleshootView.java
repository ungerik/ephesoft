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

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceMessages;
import com.ephesoft.gxt.batchinstance.client.presenter.TroubleshootPresenter;
import com.ephesoft.gxt.batchinstance.client.shared.constants.BatchInfoConstants;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.TroubleshootDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ContentPanel.ContentPanelAppearance;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent.BeforeExpandHandler;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;

public class TroubleshootView extends View<TroubleshootPresenter> {

	interface Binder extends UiBinder<Widget, TroubleshootView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected ScrollPanel panelForTree;

	@UiField
	protected VerticalLayoutContainer verticalLayoutContainer;

	@UiField
	protected ContentPanel panel;

	@UiField
	protected ContentPanel downloadPanel;

	@UiField
	protected ContentPanel downloadToPanel;

	@UiField
	protected ContentPanel uploadPanel;

	@UiField
	protected Label downloadToPathLabel;

	@UiField
	protected Button downloadToButton;

	@UiField
	protected Button download;

	@UiField
	protected Button uploadToButton;

	@UiField
	protected TextBox downloadToPathTextBox;

	@UiField
	protected Label usernameLabel;

	@UiField
	protected TextBox usernameTextBox;

	@UiField
	protected Label passwordLabel;

	@UiField
	protected PasswordTextBox passwordTextBox;

	@UiField
	protected Label serverURLLabel;

	@UiField
	protected TextBox serverURLTextBox;

	@UiField
	protected Label ticketNoLabel;

	@UiField
	protected TextBox ticketNoTextBox;

	@UiField
	protected FormPanel formPanel;

	@UiField
	protected BorderLayoutContainer troubleshootViewContainer;

	@UiField
	protected AccordionLayoutContainer con;

	@UiField
	protected RadioButton downloadRadio;

	@UiField
	protected RadioButton downloadToRadio;

	@UiField
	protected RadioButton uploadRadio;

	@UiField
	protected VerticalPanel downloadVP;

	@UiField
	protected VerticalPanel downloadToVP;

	@UiField
	protected VerticalPanel uploadVP;

	List<TroubleshootDTO> defaultSelectedArtifacts;

	List<TroubleshootDTO> generalArtifacts;

	/**
	 * the action URL on the submit of form panel.
	 */
	private static final String DOWNLOAD_ACTION = "dcma-gwt-batchInstance/downloadBatchInstance";

	private String selectedBatchInstanceIdentifier;

	private VerticalLayoutContainer hiddenFieldsContainer;

	@UiFactory
	public ContentPanel createContentPanel(ContentPanelAppearance appearance) {
		return new ContentPanel(appearance);
	}

	protected Tree<TroubleshootDTO, String> tree;

	// DTOs used to create node in tree.
	List<TroubleshootDTO> childrenOfBatchClass = null;
	List<TroubleshootDTO> childrenOfApplication = null;
	List<TroubleshootDTO> childrenOfLogs = null;

	TroubleshootDTO batchClass = null;
	TroubleshootDTO application = null;
	TroubleshootDTO logs = null;

	TroubleshootDTO defaultBatchClassDTO = null;
	TroubleshootDTO imageClassificationDTO = null;
	TroubleshootDTO luceneClassificationDTO = null;
	TroubleshootDTO libDTO = null;
	TroubleshootDTO metaInfDTO = null;
	TroubleshootDTO applicationFolderDTO = null;

	TroubleshootDTO applicationLogsDTO = null;
	TroubleshootDTO javaAppServerLogs = null;
	TroubleshootDTO batchInstanceLogs = null;

	TroubleshootDTO batchInstanceFolderDTO = null;
	TroubleshootDTO databaseDumpDTO = null;
	TroubleshootDTO uncFolderDTO = null;

	@UiField
	protected CheckBox selectAll;

	private List<TroubleshootDTO> allArtifacts = new ArrayList<TroubleshootDTO>();

	class KeyProvider implements ModelKeyProvider<TroubleshootDTO> {

		@Override
		public String getKey(TroubleshootDTO item) {
			return item.getValue();
		}
	}

	public TroubleshootView() {
		super();
		initWidget(binder.createAndBindUi(this));

		initializeFormPanel();

		initializeTree();

		initializeRadioButtons();

		downloadToPathLabel.setText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_PATH_TITLE));
		usernameLabel.setText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_USERNAME_TITLE));
		passwordLabel.setText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_PASSWORD_TITLE));
		serverURLLabel.setText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_SERVER_URL_TITLE));
		ticketNoLabel.setText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_TICKET_ID_TITLE));
		selectAll.setText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_SELECT_ALL_TITLE));
		downloadToButton.setText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_DOWNLOAD_TO_LABEL));
		download.setText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_DOWNLOAD_LABEL));
		uploadToButton.setText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_UPLOAD_TO_LABEL));

		downloadPanel.setHeadingText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_DOWNLOAD_LABEL));
		downloadToPanel.setHeadingText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_DOWNLOAD_TO_LABEL));
		uploadPanel.setHeadingText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_UPLOAD_TO_LABEL));

		panelForTree.setAlwaysShowScrollBars(false);
		panelForTree.add(tree);
		selectAll.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(final ValueChangeEvent<Boolean> event) {
				if (selectAll.getValue()) {
					List<TroubleshootDTO> allArtifacts = tree.getStore().getAll();
					for (final TroubleshootDTO item : allArtifacts) {
						TreeNode<TroubleshootDTO> node = tree.findNode(item);
						XElement nodeElement = tree.getView().getElement(node);
						if (nodeElement.isVisible()) {
							tree.setChecked(item, CheckState.CHECKED);
						}
					}
				} else {
					tree.setCheckedSelection(new ArrayList<TroubleshootDTO>());
				}
			}
		});
		troubleshootViewContainer.forceLayout();
		downloadPanel.addStyleName("subBottomPanelHeader");
		downloadToPanel.addStyleName("subBottomPanelHeader");
		uploadPanel.addStyleName("subBottomPanelHeader");

		addLayoutHandlers();
	}

	private void addLayoutHandlers() {
		panel.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(final ResizeEvent event) {
				resizeTroubleshootPanel();
			}
		});

		downloadPanel.addBeforeExpandHandler(new BeforeExpandHandler() {

			@Override
			public void onBeforeExpand(final BeforeExpandEvent event) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute() {
						final int height = con.getOffsetHeight();
						final int width = troubleshootViewContainer.getEastWidget().getOffsetWidth();
						downloadPanel.setPixelSize(width, height);
					}
				});
			}
		});

		downloadToPanel.addBeforeExpandHandler(new BeforeExpandHandler() {

			@Override
			public void onBeforeExpand(final BeforeExpandEvent event) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute() {
						final int height = con.getOffsetHeight();
						final int width = troubleshootViewContainer.getEastWidget().getOffsetWidth();
						downloadToPanel.setPixelSize(width, height);
					}
				});
			}
		});

		uploadPanel.addBeforeExpandHandler(new BeforeExpandHandler() {

			@Override
			public void onBeforeExpand(final BeforeExpandEvent event) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute() {
						final int height = con.getOffsetHeight();
						final int width = troubleshootViewContainer.getEastWidget().getOffsetWidth();
						uploadPanel.setPixelSize(width, height);
					}
				});
			}

		});
	}

	private void initializeTree() {
		final TreeStore<TroubleshootDTO> store = new TreeStore<TroubleshootDTO>(new KeyProvider());
		populateStore(store);

		tree = new Tree<TroubleshootDTO, String>(store, new ValueProvider<TroubleshootDTO, String>() {

			@Override
			public String getPath() {
				return "name";
			}

			@Override
			public String getValue(final TroubleshootDTO object) {
				return object.getValue();
			}

			@Override
			public void setValue(final TroubleshootDTO object, final String value) {

			}
		});
		tree.setCheckable(true);
		tree.setCheckStyle(CheckCascade.TRI);
		tree.setAutoLoad(true);
		tree.addStyleName("overflowDisableTree");

		tree.addCheckChangeHandler(new CheckChangeHandler<TroubleshootDTO>() {

			@Override
			public void onCheckChange(final CheckChangeEvent<TroubleshootDTO> event) {
				// TroubleshootDTO troubleshoot = event.getItem();

				if (null != defaultBatchClassDTO && null != imageClassificationDTO && null != luceneClassificationDTO) {
					final TreeNode<TroubleshootDTO> imageClassificationNode = tree.findNode(imageClassificationDTO);
					final TreeNode<TroubleshootDTO> luceneClassificationNode = tree.findNode(luceneClassificationDTO);

					if (CheckState.CHECKED == tree.getChecked(defaultBatchClassDTO)) {
						tree.getView().getElement(luceneClassificationNode).setVisible(true);
						tree.getView().getElement(imageClassificationNode).setVisible(true);
					} else {
						tree.getView().getElement(luceneClassificationNode).setVisible(false);
						tree.getView().getElement(imageClassificationNode).setVisible(false);
					}
				}
			}
		});
	}

	private void initializeFormPanel() {
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setAction(DOWNLOAD_ACTION);
		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(final SubmitCompleteEvent event) {
				final String results = event.getResults();
				if (results.contains(BatchInfoConstants.TROUBLESHOOT_INVALID_CREDENTIALS_ERROR_TEXT)) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(BatchInstanceMessages.INVALID_CREDENTIALS_FOR_TROUBLESHOOT_UPLOAD),
							DialogIcon.ERROR);
				}
			}

		});
	}

	private void initializeRadioButtons() {
		downloadRadio.setText(LocaleDictionary.getConstantValue(LocaleDictionary
				.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_DOWNLOAD_LABEL)));
		downloadToRadio.setText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_DOWNLOAD_TO_LABEL));
		uploadRadio.setText(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_UPLOAD_TO_LABEL));

		downloadRadio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				final boolean checked = event.getValue();
				if (checked) {
					downloadPanel.setVisible(true);
					downloadToPanel.setVisible(false);
					uploadPanel.setVisible(false);
					downloadToRadio.setValue(false);
					uploadRadio.setValue(false);
					con.setActiveWidget(downloadPanel);
					// downloadPanel.expand();
					// downloadPanel.forceLayout();
				}
			}
		});

		downloadToRadio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				final boolean checked = event.getValue();
				if (checked) {
					downloadPanel.setVisible(false);
					downloadToPanel.setVisible(true);
					uploadPanel.setVisible(false);
					downloadRadio.setValue(false);
					uploadRadio.setValue(false);
					con.setActiveWidget(downloadToPanel);
					// downloadToPanel.expand();
					// downloadToPanel.forceLayout();
				}
			}
		});

		uploadRadio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				final boolean checked = event.getValue();
				if (checked) {
					downloadPanel.setVisible(false);
					downloadToPanel.setVisible(false);
					uploadPanel.setVisible(true);
					downloadRadio.setValue(false);
					downloadToRadio.setValue(false);
					con.setActiveWidget(uploadPanel);
					// uploadPanel.expand();
					// uploadPanel.forceLayout();
				}
			}
		});
		downloadRadio.setValue(true);
	}

	private void populateStore(TreeStore<TroubleshootDTO> store) {
		childrenOfBatchClass = new ArrayList<TroubleshootDTO>();
		childrenOfApplication = new ArrayList<TroubleshootDTO>();
		childrenOfLogs = new ArrayList<TroubleshootDTO>();

		batchClass = new TroubleshootDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_BATCH_CLASS),
				BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_BATCH_CLASS);
		application = new TroubleshootDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_APPLICATION),
				"Application");
		logs = new TroubleshootDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_LOGS), "Logs");

		defaultBatchClassDTO = new TroubleshootDTO(
				LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_DEFAULT_BATCH_CLASS),
				BatchInfoConstants.BATCH_CLASS_FOLDER);
		imageClassificationDTO = new TroubleshootDTO(
				LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_IMAGE_CLASSIFICATION),
				BatchInfoConstants.IMAGE_CLASSIFICATION_SAMPLE);
		luceneClassificationDTO = new TroubleshootDTO(
				LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_LUCENE_CLASSIFICATION),
				BatchInfoConstants.LUCENE_SEARCH_CLASSIFICATION_SAMPLE);

		libDTO = new TroubleshootDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_LIB),
				BatchInfoConstants.LIB);
		metaInfDTO = new TroubleshootDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_META_INF),
				BatchInfoConstants.META_INF);
		applicationFolderDTO = new TroubleshootDTO(
				LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_APPLICATION_FOLDER),
				BatchInfoConstants.APPLICATION);

		applicationLogsDTO = new TroubleshootDTO(
				LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_APPLICATION_LOGS),
				BatchInfoConstants.APPLICATION_LOGS);
		javaAppServerLogs = new TroubleshootDTO(
				LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_JAVA_APPSERVER_LOGS),
				BatchInfoConstants.JAVA_APP_SERVER);
		batchInstanceLogs = new TroubleshootDTO(
				LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_BATCH_INSTANCE_LOGS),
				BatchInfoConstants.BATCH_LOGS);

		batchInstanceFolderDTO = new TroubleshootDTO(
				LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_BATCH_INSTANCE_FOLDER),
				BatchInfoConstants.BATCH_INSTANCE_FOLDER);
		databaseDumpDTO = new TroubleshootDTO(
				LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_DATABASE_DUMP),
				BatchInfoConstants.DATABASE_DUMP_FOLDER);
		uncFolderDTO = new TroubleshootDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.TROUBLESHOOT_ARTIFACT_UNC_FOLDER),
				BatchInfoConstants.UNC_FOLDER);

		childrenOfBatchClass.add(defaultBatchClassDTO);
		childrenOfBatchClass.add(imageClassificationDTO);
		childrenOfBatchClass.add(luceneClassificationDTO);

		childrenOfApplication.add(libDTO);
		childrenOfApplication.add(metaInfDTO);
		childrenOfApplication.add(applicationFolderDTO);

		childrenOfLogs.add(applicationLogsDTO);
		childrenOfLogs.add(javaAppServerLogs);
		childrenOfLogs.add(batchInstanceLogs);

		allArtifacts.add(batchClass);
		allArtifacts.add(application);
		allArtifacts.add(logs);

		allArtifacts.add(batchInstanceFolderDTO);
		allArtifacts.add(databaseDumpDTO);
		allArtifacts.add(uncFolderDTO);
		store.add(allArtifacts);

		store.add(batchClass, childrenOfBatchClass);
		store.add(application, childrenOfApplication);
		store.add(logs, childrenOfLogs);

		// Populating default selected artifacts.
		defaultSelectedArtifacts = new ArrayList<TroubleshootDTO>();
		defaultSelectedArtifacts.add(defaultBatchClassDTO);
		defaultSelectedArtifacts.add(applicationLogsDTO);
		defaultSelectedArtifacts.add(javaAppServerLogs);
		defaultSelectedArtifacts.add(batchInstanceLogs);
		defaultSelectedArtifacts.add(batchInstanceFolderDTO);

		// Populating non batch specific artifacts
		generalArtifacts = new ArrayList<TroubleshootDTO>();
		generalArtifacts.add(application);
		generalArtifacts.add(logs);
		generalArtifacts.add(applicationFolderDTO);
		generalArtifacts.add(metaInfDTO);
		generalArtifacts.add(libDTO);
		generalArtifacts.add(applicationLogsDTO);
		generalArtifacts.add(javaAppServerLogs);
		generalArtifacts.add(databaseDumpDTO);
	}

	/**
	 * Handler for download button.
	 * 
	 * @param clickEvent {@link ClickEvent}.
	 */
	@UiHandler("download")
	public void onDownloadClicked(final ClickEvent clickEvent) {
		presenter.onDownloadClicked();
	}

	/**
	 * Handler for download button.
	 * 
	 * @param clickEvent {@link ClickEvent}.
	 */
	@UiHandler("downloadToButton")
	public void onDownloadToClicked(final ClickEvent clickEvent) {
		presenter.onDownloadToClicked();
	}

	/**
	 * This method checks that at least one option is selected in the troubleshoot pop up.
	 * 
	 * @return true if at least one is selected false otherwise
	 */
	public boolean checkSelectedOptions() {
		return (!CollectionUtil.isEmpty(tree.getCheckedSelection()));
	}

	/**
	 * Handler for download button.
	 * 
	 * @param clickEvent {@link ClickEvent}.
	 */
	@UiHandler("uploadToButton")
	public void onUploadClicked(final ClickEvent clickEvent) {
		presenter.onUploadClicked();
	}

	public void addSelectedCheckboxToForm() {
		if (null != hiddenFieldsContainer) {
			final List<TroubleshootDTO> selectedCheckboxList = tree.getCheckedSelection();
			if (CollectionUtil.isEmpty(selectedCheckboxList)) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.INFO_TITLE),
						LocaleDictionary.getMessageValue(BatchInstanceMessages.NO_ARTIFACTS_SELECTED_FOR_TROUBLESHOOTING),
						DialogIcon.ERROR);
			} else {
				Hidden selectedCheckbox = null;
				for (final TroubleshootDTO checkbox : selectedCheckboxList) {
					selectedCheckbox = new Hidden(checkbox.getName());
					selectedCheckbox.setName(checkbox.getName());
					selectedCheckbox.setValue("true");
					hiddenFieldsContainer.add(selectedCheckbox);
				}
			}
		}
	}

	public List<TroubleshootDTO> getSelectedArtifacts() {
		return tree.getCheckedSelection();
	}

	@Override
	public void initialize() {
	}

	public String getSelectedBatchInstanceIdentifier() {
		return selectedBatchInstanceIdentifier;
	}

	public void setSelectedBatchInstanceIdentifier(String selectedBatchInstanceIdentifier) {
		this.selectedBatchInstanceIdentifier = selectedBatchInstanceIdentifier;
	}

	public void forceLayout() {
		troubleshootViewContainer.forceLayout();
	}

	public void addAndSubmitForm(final VerticalLayoutContainer hiddenFieldsContainer) {
		formPanel.clear();
		formPanel.add(hiddenFieldsContainer);
		formPanel.submit();
	}

	public String getDownloadToValue() {
		return downloadToPathTextBox.getValue();
	}

	public String getUsernameValue() {
		return usernameTextBox.getValue();
	}

	public String getPasswordValue() {
		return passwordTextBox.getValue();
	}

	public String getServerURLValue() {
		return serverURLTextBox.getValue();
	}

	public String getTicketNoValue() {
		return ticketNoTextBox.getValue();
	}

	public void setDefaultSelection() {
		tree.setCheckedSelection(defaultSelectedArtifacts);
	}

	public void loadAllArtifacts() {
		hideAllArtifacts();
		setDefaultSelection();
		List<TroubleshootDTO> allArtifacts = tree.getStore().getAll();
		for (final TroubleshootDTO dto : allArtifacts) {
			final TreeNode<TroubleshootDTO> node = tree.findNode(dto);
			showNode(node);
		}
	}

	private void hideAllArtifacts() {
		List<TroubleshootDTO> allArtifacts = tree.getStore().getAll();
		for (final TroubleshootDTO dto : allArtifacts) {
			final TreeNode<TroubleshootDTO> node = tree.findNode(dto);
			hideNode(node);
		}
	}

	public void loadGeneralArtifacts() {
		hideAllArtifacts();
		tree.setCheckedSelection(null);
		for (final TroubleshootDTO dto : generalArtifacts) {
			final TreeNode<TroubleshootDTO> node = tree.findNode(dto);
			showNode(node);
		}
		// for (final TroubleshootDTO dto : allArtifacts) {
		// final TreeNode<TroubleshootDTO> node = tree.findNode(dto);
		// if (generalArtifacts.contains(dto)) {
		// showNode(node);
		// } else {
		// hideNode(node);
		// }
		// }
	}

	private void showNode(final TreeNode<TroubleshootDTO> node) {
		XElement nodeElement = tree.getView().getElement(node);
		// Window.alert("Showing element " + node.getModel().getValue() + " is null " + (null == nodeElement));
		if (null != nodeElement) {
			nodeElement.setVisible(true);
		}
	}

	private void hideNode(final TreeNode<TroubleshootDTO> node) {
		XElement nodeElement = tree.getView().getElement(node);
		if (null != nodeElement) {
			nodeElement.setVisible(false);
		}
	}

	public void setDownloadOptionSelected() {
		con.setActiveWidget(downloadPanel);
	}

	public void resizeTroubleshootPanel() {
		Timer timer = new Timer() {

			@Override
			public void run() {
				if (con.getActiveWidget() instanceof ContentPanel) {
					final ContentPanel currentSelectedCP = (ContentPanel) con.getActiveWidget();
					final int height = con.getOffsetHeight();
					final int width = troubleshootViewContainer.getEastWidget().getOffsetWidth();
					currentSelectedCP.setPixelSize(width, height);
				}
			}
		};
		timer.schedule(10);
	}
}
