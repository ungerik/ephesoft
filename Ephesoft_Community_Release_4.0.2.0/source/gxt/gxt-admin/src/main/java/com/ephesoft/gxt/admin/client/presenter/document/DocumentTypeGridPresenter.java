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

package com.ephesoft.gxt.admin.client.presenter.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.AddDocumentTypeEvent;
import com.ephesoft.gxt.admin.client.event.BatchClassLoadEvent;
import com.ephesoft.gxt.admin.client.event.CopyDocumentTypeEvent;
import com.ephesoft.gxt.admin.client.event.DeleteDocumentTypeEvent;
import com.ephesoft.gxt.admin.client.event.ExportSelectedDocumentsListEvent;
import com.ephesoft.gxt.admin.client.event.LoadImportedDocumentEvent;
import com.ephesoft.gxt.admin.client.event.RetrieveSelectedDocumentsListEvent;
import com.ephesoft.gxt.admin.client.event.SubTreeAdditionEvent.DocumentSubTreeAdditionEvent;
import com.ephesoft.gxt.admin.client.event.ViewLearnFilesEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.document.DocumentTypeGridView;
import com.ephesoft.gxt.admin.shared.constant.DTOPropertiesConstant;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.EventUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FunctionKeyDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.DTOUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

public class DocumentTypeGridPresenter extends BatchClassInlinePresenter<DocumentTypeGridView> {

	public DocumentTypeGridPresenter(BatchClassManagementController controller, DocumentTypeGridView view) {
		super(controller, view);
	}

	interface CustomEventBinder extends EventBinder<DocumentTypeGridPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	@Override
	public void bind() {
		if (controller.doBatchClassRequireReload()) {

			BatchClassDTO selectedBatchClass = controller.getSelectedBatchClass();

			if (null != selectedBatchClass) {

				ScreenMaskUtility.maskScreen();
				rpcService.getBatchClass(selectedBatchClass.getIdentifier(), new AsyncCallback<BatchClassDTO>() {

					@Override
					public void onSuccess(BatchClassDTO result) {
						ScreenMaskUtility.unmaskScreen();
						// dialog.hide();
						controller.setSelectedBatchClass(result);
						controller.setBatchClassRequiresReload(false);
						getProjectFiles();
					}

					@Override
					public void onFailure(Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
					}
				});

				//
			} else {
			}
		}
	}

	private void getProjectFiles() {
		controller.getRpcService().getProjectFilesForDocumentType(controller.getSelectedBatchClass().getIdentifier(), null,
				new AsyncCallback<List<String>>() {

					@Override
					public void onFailure(Throwable arg0) {
						ScreenMaskUtility.unmaskScreen();
					}

					@Override
					public void onSuccess(List<String> rspFileNameList) {
						view.setProjectFiles(rspFileNameList);
						view.reloadGrid();
						ScreenMaskUtility.unmaskScreen();
						BatchClassManagementEventBus.fireEvent(new BatchClassLoadEvent(controller.getSelectedBatchClass()));
					}
				});
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);

	}

	public Collection<DocumentTypeDTO> getCurrentDocumentTypes() {
		BatchClassDTO selectedBatchClass = controller.getSelectedBatchClass();
		Collection<DocumentTypeDTO> documentTypeCollection = null;
		if (selectedBatchClass != null) {
			documentTypeCollection = selectedBatchClass.getDocuments();
			documentTypeCollection = this.removeUnknownDocument(documentTypeCollection);
		}
		return documentTypeCollection;
	}

	private Collection<DocumentTypeDTO> removeUnknownDocument(final Collection<DocumentTypeDTO> documentTypeCollection) {
		Collection<DocumentTypeDTO> clonedCollection = null;
		if (!CollectionUtil.isEmpty(documentTypeCollection)) {
			clonedCollection = new ArrayList<DocumentTypeDTO>(documentTypeCollection.size() - 1);
			for (DocumentTypeDTO documentDTO : documentTypeCollection) {
				if (null != documentDTO) {
					if (!DTOPropertiesConstant.DOCUMENT_TYPE_UNKNOWN.equalsIgnoreCase(documentDTO.getName())) {
						clonedCollection.add(documentDTO);
					}
				}
			}
		}
		return clonedCollection;
	}

	@EventHandler
	public void handleAddDocumentTypeEvent(AddDocumentTypeEvent addDocumentTypeEvent) {
		view.getDocumentTypeGrid().getStore().commitChanges();
		int numberOfDocTypes = view.getDocumentTypeGrid().getStore().size();

		if (numberOfDocTypes >= CoreCommonConstants.DOC_LIMIT) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
					LocaleDictionary.getConstantValue(LocaleCommonConstants.DOC_LIMIT_MESSAGE));
		} else {
			if (null != addDocumentTypeEvent) {
				if (isValid()) {
					DocumentTypeDTO docTypeDTO = createNewDocumentType();
					if (view.getDocumentTypeGrid().addNewItemInGrid(docTypeDTO)) {
						controller.getSelectedBatchClass().addDocumentType(docTypeDTO);
						controller.getSelectedBatchClass().setDirty(true);
					}
				} else {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.CANNOT_ADD_MORE_FIELDS_VALIDATE_VIEW_FIRST));
				}
			}
		}
	}

	@EventHandler
	public void handleRetrieveDocumentTypeListEvent(RetrieveSelectedDocumentsListEvent retrieveDocumentTypeEvent) {
		if (null != retrieveDocumentTypeEvent) {
			view.getDocumentTypeGrid().getStore().commitChanges();
			List<DocumentTypeDTO> selectedDocumentList = view.getSelectedDocumentTypes();
			if (CollectionUtil.isEmpty(selectedDocumentList)) {
				if (null != controller.getSelectedDocumentType()) {
					selectedDocumentList = Collections.singletonList(controller.getSelectedDocumentType());
				}
			}

			if (retrieveDocumentTypeEvent.isExport()) {
				BatchClassManagementEventBus.fireEvent(new ExportSelectedDocumentsListEvent(selectedDocumentList));
			} else if (retrieveDocumentTypeEvent.isViewLearnFiles()) {
				ViewLearnFilesEvent learnFilesEvent = new ViewLearnFilesEvent(retrieveDocumentTypeEvent.isViewLearnFileValue());
				if (retrieveDocumentTypeEvent.isViewLearnFileValue()) {
					if (controller.getSelectedBatchClass().isDirty()) {
						learnFilesEvent.setViewValid(false);
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_SAVE_PENDING_CHANGES_FIRST),
								DialogIcon.INFO);
					} else if (CollectionUtil.isEmpty(selectedDocumentList)) {
						learnFilesEvent.setViewValid(false);
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_SELECT_DOCUMENT_TYPE_TO_VIEW_LEARN_FILES),
								DialogIcon.WARNING);
					} else if (selectedDocumentList.size() > 1) {
						learnFilesEvent.setViewValid(false);
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
								LocaleDictionary
										.getMessageValue(BatchClassMessages.PLEASE_SELECT_ONLY_ONE_DOCUMENT_TYPE_TO_VIEW_LEARN_FILES),
								DialogIcon.WARNING);
					} else {
						learnFilesEvent.setViewValid(true);
						learnFilesEvent.setSelectedDocumentTypeDTO(selectedDocumentList.get(0));
					}
				}
				BatchClassManagementEventBus.fireEvent(learnFilesEvent);
			}
		}

	}

	@EventHandler
	public void handleLoadImportedDocumentTypeEvent(LoadImportedDocumentEvent importedDocType) {
		if (null != importedDocType) {
			if (null != importedDocType.getImportedDocuementType() && importedDocType.getImportedDocuementType().size() > 0) {
				for (DocumentTypeDTO docType : importedDocType.getImportedDocuementType()) {
					docType.setBatchClass(controller.getSelectedBatchClass());
					controller.getSelectedBatchClass().addDocumentType(docType);
					view.getDocumentTypeGrid().addNewItemInGrid(docType);
					BatchClassManagementEventBus.fireEvent(new DocumentSubTreeAdditionEvent(docType));
					// addInTreeEvents(docType);
				}
				Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.IMPORT_DOCUMENT_TYPE),
						LocaleDictionary.getMessageValue(BatchClassMessages.DOCUMENT_TYPE_IMPORTED_SUCCESSFULLY));
			}
		}
	}

	// private void addInTreeEvents(DocumentTypeDTO docType) {
	// if (null != docType.getFields()) {
	// for (FieldTypeDTO fieldType : docType.getFields()) {
	// BatchClassManagementEventBus.fireEvent(new NavigationNodeAdditionEvent(fieldType));
	// // if (null != fieldType.getKvExtractionList()) {
	// // addKVExtractionInTree(fieldType.getKvExtractionList());
	// // }
	// // if (null != fieldType.getRegexList()) {
	// // addRegexInTree(fieldType.getRegexList());
	// // }
	// }
	// }
	//
	// // if (null != docType.getFunctionKeys()) {
	// // for (FunctionKeyDTO functionKeyDTO : docType.getFunctionKeys()) {
	// // BatchClassManagementEventBus.fireEvent(new NavigationNodeAdditionEvent(functionKeyDTO));
	// // }
	// // }
	//
	// if (null != docType.getTableInfos()) {
	// for (TableInfoDTO tables : docType.getTableInfos()) {
	// BatchClassManagementEventBus.fireEvent(new NavigationNodeAdditionEvent(tables));
	// // if (null != tables.getTableColumnInfoList()) {
	// // addTableInfoInTree(tables.getTableColumnInfoList());
	// // }
	// // if (null != tables.getRuleInfoDTOs()) {
	// // addTableValidationInTree(tables.getRuleInfoDTOs());
	// // }
	// if (null != tables.getTableExtractionRuleDTOs()) {
	// addTableExtractionInTree(tables.getTableExtractionRuleDTOs());
	// }
	// }
	// }
	// }
	//
	// private void addTableExtractionInTree(List<TableExtractionRuleDTO> tableExtractionRuleDTOs) {
	// for (TableExtractionRuleDTO tableExtractionRuleElement : tableExtractionRuleDTOs) {
	// BatchClassManagementEventBus.fireEvent(new NavigationNodeAdditionEvent(tableExtractionRuleElement));
	// }
	// }

	// private void addTableValidationInTree(List<RuleInfoDTO> ruleInfoDTOs) {
	// for (RuleInfoDTO tableRuleElement : ruleInfoDTOs) {
	// BatchClassManagementEventBus.fireEvent(new NavigationNodeAdditionEvent(tableRuleElement));
	// }
	// }
	//
	// private void addTableInfoInTree(List<TableColumnInfoDTO> tableColumnInfoList) {
	// for (TableColumnInfoDTO tableColumnInfoElement : tableColumnInfoList) {
	// BatchClassManagementEventBus.fireEvent(new NavigationNodeAdditionEvent(tableColumnInfoElement));
	// }
	// }
	//
	// private void addRegexInTree(List<RegexDTO> regexList) {
	// for (RegexDTO regexElement : regexList) {
	// BatchClassManagementEventBus.fireEvent(new NavigationNodeAdditionEvent(regexElement));
	// }
	// }
	//
	// private void addKVExtractionInTree(List<KVExtractionDTO> kvExtractionList) {
	// for (KVExtractionDTO kvElement : kvExtractionList) {
	// BatchClassManagementEventBus.fireEvent(new NavigationNodeAdditionEvent(kvElement));
	// }
	// }

	private DocumentTypeDTO createNewDocumentType() {
		DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
		documentTypeDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		documentTypeDTO.setNew(true);
		documentTypeDTO.setPriority(0);
		documentTypeDTO.setBatchClass(controller.getSelectedBatchClass());
		return documentTypeDTO;

	}

	@EventHandler
	public void handleMultipleDocumentTypeDeletion(DeleteDocumentTypeEvent deleteEvent) {
		if (null != deleteEvent) {
			final List<DocumentTypeDTO> selectedDocumentTypes = view.getSelectedDocumentTypes();
			if (!CollectionUtil.isEmpty(selectedDocumentTypes)) {

				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchClassMessages.SURE_TO_DELETE_DOCUMENT_TYPE), false,
						DialogIcon.QUESTION_MARK);
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						confirmationDialog.hide();
						deleteSelectedDocumentTypes(selectedDocumentTypes);
						Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.DELETE_DOCUMENT_TYPE),
								LocaleDictionary.getMessageValue(BatchClassMessages.DOCUMENT_TYPE_DELETED_SUCCESSFULLY));

					}

					@Override
					public void onCloseClick() {
					}

					@Override
					public void onCancelClick() {
						confirmationDialog.hide();
					}
				});

			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_DOCUMENT_TYPE_TO_DELETE), DialogIcon.ERROR);

			}
		}
	}

	private void deleteSelectedDocumentTypes(List<DocumentTypeDTO> selectedDocumentTypes) {

		BatchClassDTO batchClassDTO = controller.getSelectedBatchClass();
		for (DocumentTypeDTO documentType : selectedDocumentTypes) {
			if (null != documentType) {
				batchClassDTO.getDocTypeByIdentifier(documentType.getIdentifier()).setDeleted(true);
				if (controller.getSelectedDocumentType() == documentType) {
					controller.setSelectedDocumentType(null);
				}
			}
		}
		controller.getSelectedBatchClass().setDirty(true);
		view.getDocumentTypeGrid().removeItemsFromGrid(selectedDocumentTypes);
		view.getDocumentTypeGrid().getStore().commitChanges();
		controller.setSelectedDocumentType(null);
		WidgetUtil.reLoadGrid(view.getDocumentTypeGrid());
	}

	public void setSelectedDocumentType(CellSelectionChangedEvent<DocumentTypeDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			DocumentTypeDTO setSelectedDocumentType = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			if (setSelectedDocumentType != null) {
				controller.setSelectedDocumentType(setSelectedDocumentType);
			}
		}
	}

	public boolean isDocumentTypeChanged() {
		return controller.isDocumentTypeSelectionChanged();
	}

	public void setBatchClassDirtyOnChange() {
		controller.getSelectedBatchClass().setDirty(true);
	}

	public boolean isValid() {
		return view.isValid();
	}

	/**
	 * Handle copy document type event.
	 * 
	 * @param copyDocTypeEvent the copy doc type event
	 */
	@EventHandler
	public void handleCopyDocumentTypeEvent(CopyDocumentTypeEvent copyDocTypeEvent) {
		if (null != copyDocTypeEvent) {
			view.getDocumentTypeGrid().getStore().commitChanges();
			int numberOfDocTypes = view.getDocumentTypeGrid().getStore().size();

			if (numberOfDocTypes >= CoreCommonConstants.DOC_LIMIT) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
						LocaleDictionary.getConstantValue(LocaleCommonConstants.DOC_LIMIT_MESSAGE));
			} else {
				final List<DocumentTypeDTO> selectedDocumentType = view.getSelectedDocumentTypes();
				if (!CollectionUtil.isEmpty(selectedDocumentType)) {
					if (selectedDocumentType.size() > 1) {
						String message = LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_ONE_ROW_ONLY_MSG);
						message = StringUtil.concatenate(message,
								LocaleDictionary.getMessageValue(BatchClassMessages.COPY_DOCUMENT_TYPE));
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE), message,
								DialogIcon.WARNING);
					} else {
						controller.setSelectedDocumentType(selectedDocumentType.get(0));
						copyDocumentType();
					}
				} else {
					copyDocumentType();
				}
			}
		}
	}

	private void copyDocumentType() {
		if (view.isValid()) {
			DocumentTypeDTO selectedDocumentType = controller.getSelectedDocumentType();
			if (null != selectedDocumentType) {
				DocumentTypeDTO documentTypeDTO = createCopyDocumentTypeDTO(selectedDocumentType);
				BatchClassDTO batchClassDTO = controller.getSelectedBatchClass();
				if (null != batchClassDTO) {
					batchClassDTO.addDocumentType(documentTypeDTO);
					controller.setSelectedDocumentType(documentTypeDTO);
					controller.getSelectedBatchClass().setDirty(true);
					view.getDocumentTypeGrid().addNewItemInGrid(documentTypeDTO);
					BatchClassManagementEventBus.fireEvent(new DocumentSubTreeAdditionEvent(documentTypeDTO));
					Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.DOCUMENT_TYPE_COPIED_SUCCESSFULLY));
				}
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_DOCUMENT_TYPE_TO_COPY), DialogIcon.WARNING);
			}
		} else {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.VALIDATE_DOCUMENT_TYPE_FIRST_TO_COPY), DialogIcon.ERROR);
		}
	}

	/**
	 * Creates a document type dto for a document whose identifier is given.
	 * 
	 * @param identifier {@link String} the identifier of document whiose dto will be created
	 * @return {@link DocumentTypeDTO} the document type dto
	 */
	public DocumentTypeDTO createCopyDocumentTypeDTO(DocumentTypeDTO selecetdDocumentTypeDTO) {
		DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
		if (selecetdDocumentTypeDTO != null) {
			documentTypeDTO.setName(selecetdDocumentTypeDTO.getName());
			documentTypeDTO.setDescription(selecetdDocumentTypeDTO.getDescription());
			documentTypeDTO.setMinConfidenceThreshold(selecetdDocumentTypeDTO.getMinConfidenceThreshold());
			documentTypeDTO.setBatchClass(selecetdDocumentTypeDTO.getBatchClass());
			documentTypeDTO.setDeleted(false);
			documentTypeDTO.setFirstPageProjectFileName(selecetdDocumentTypeDTO.getFirstPageProjectFileName());
			documentTypeDTO.setFourthPageProjectFileName(selecetdDocumentTypeDTO.getFourthPageProjectFileName());
			documentTypeDTO.setHidden(selecetdDocumentTypeDTO.isHidden());
			documentTypeDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
			documentTypeDTO.setPriority(selecetdDocumentTypeDTO.getPriority());
			documentTypeDTO.setNew(true);
			documentTypeDTO.setDBExportConnection(selecetdDocumentTypeDTO.getDBExportConnection());
			documentTypeDTO.setSecondPageProjectFileName(selecetdDocumentTypeDTO.getSecondPageProjectFileName());
			documentTypeDTO.setThirdPageProjectFileName(selecetdDocumentTypeDTO.getThirdPageProjectFileName());
			setNewAttributeOfFeildDTO(documentTypeDTO, selecetdDocumentTypeDTO.getFields(false));
			setNewAttributeOfFunctionKeys(documentTypeDTO, selecetdDocumentTypeDTO.getFunctionKeys(false));
			setNewAttributeOfTableInfoDTO(documentTypeDTO, selecetdDocumentTypeDTO.getTableInfos(false));
		}
		return documentTypeDTO;
	}

	/**
	 * Sets the passed table extraction dtos in the passed document type by copying them.
	 * 
	 * @param tableInfos{{@link List{@link TableInfoDTO} the collection of table info dtos to be copied to the document.
	 * @param newDocumentTypeDTO{{@link DocumentTypeDTO} the document type whose table extraction attributes to set.
	 */
	private void setNewAttributeOfTableInfoDTO(final DocumentTypeDTO newDocumentTypeDTO, final Collection<TableInfoDTO> tableInfos) {
		if (null != tableInfos) {
			for (TableInfoDTO tableInfoDTO : tableInfos) {
				TableInfoDTO newTableInfoDTO = DTOUtil.createTableInfoDTO(newDocumentTypeDTO, tableInfoDTO);
				newTableInfoDTO.setNew(true);
				newDocumentTypeDTO.getTableInfoMap().put(newTableInfoDTO.getIdentifier(), newTableInfoDTO);
			}
		}

	}

	/**
	 * Sets the passed function key dtos in the passed document type by copying them.
	 * 
	 * @param functionKeys{{@link List{@link FunctionKeyDTO} the collection of function key dtos to be copied to the document.
	 * @param newDocumentTypeDTO{{@link DocumentTypeDTO} the document type whose function key attributes to set.
	 */
	private void setNewAttributeOfFunctionKeys(final DocumentTypeDTO newDocumentTypeDTO, final Collection<FunctionKeyDTO> functionKeys) {

		if (null != functionKeys) {
			for (FunctionKeyDTO functionKeyDTO : functionKeys) {
				FunctionKeyDTO newFunctionKeyDTO = DTOUtil.createFunctionKeyDTO(newDocumentTypeDTO, functionKeyDTO);
				newFunctionKeyDTO.setNew(true);
				newDocumentTypeDTO.getFunctionKeyMap().put(functionKeyDTO.getIdentifier(), newFunctionKeyDTO);
			}
		}

	}

	/**
	 * Sets the passed index field dtos in the passed document type by copying them.
	 * 
	 * @param fields{{@link List{@link FieldTypeDTO} the collection of index field dtos to be copied to the document.
	 * @param newDocumentTypeDTO{{@link DocumentTypeDTO} the document type whose index field attributes to set.
	 */
	private void setNewAttributeOfFeildDTO(final DocumentTypeDTO newDocumentTypeDTO, final Collection<FieldTypeDTO> fields) {
		if (null != fields) {

			for (FieldTypeDTO fieldTypeDTO : fields) {
				FieldTypeDTO newFieldTypeDTO = DTOUtil.createFieldTypeDTO(newDocumentTypeDTO, fieldTypeDTO);
				newFieldTypeDTO.setNew(true);
				newFieldTypeDTO.setKvExtractionList(DTOUtil.createKVExtractionDTOList(fieldTypeDTO.getKvExtractionList()));
				newFieldTypeDTO.setRegexList(DTOUtil.createRegExDTOList(fieldTypeDTO.getRegexList()));
				newDocumentTypeDTO.getFieldsMap().put(newFieldTypeDTO.getIdentifier(), newFieldTypeDTO);
			}
		}
	}
}
