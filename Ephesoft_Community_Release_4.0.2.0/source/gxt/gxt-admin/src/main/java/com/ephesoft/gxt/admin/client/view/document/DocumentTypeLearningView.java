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

package com.ephesoft.gxt.admin.client.view.document;

import java.util.ArrayList;
import java.util.List;

import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueuedEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueuedHandler;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessEvent;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessHandler;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.LearnFilesStartEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.i18n.DocumentTypeConstants;
import com.ephesoft.gxt.admin.client.presenter.document.DocumentTypeLearningPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.MultiFileUploader;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.ui.widget.window.MessageDialog;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.ImportDocumentTypeDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class DocumentTypeLearningView extends BatchClassInlineView<DocumentTypeLearningPresenter> implements HasResizableGrid {

	protected ViewDocLearnFileGridView viewDocLearnFileView;

	ImportDocumentTypeDTO importDocumentTypeDTO;

	@UiField
	protected HorizontalPanel dragDropViewHorizontalPanel;

	@UiField
	protected HorizontalPanel docLearningViewHorizontalPanel;

	@UiField
	protected MultiFileUploader learneFileUploader;

	@UiField
	protected MultiFileUploader documentTypeUploader;

	@UiField
	protected MultiFileUploader classifyFileUploader;

	@UiField
	protected MultiFileUploader extractFileUploader;

	// upload handlers for learning files
	protected FileQueuedHandler learnFileQueuedHandler;

	protected FileDialogCompleteHandler learnFileDialogCompleteHandler;

	protected UploadSuccessHandler learnUploadSuccessHandler;

	// upload handlers for classify files
	protected FileQueuedHandler classifyFileQueuedHandler;

	protected FileDialogCompleteHandler classifyFileDialogCompleteHandler;

	protected UploadSuccessHandler classifyUploadSuccessHandler;

	// upload handlers for extract files
	protected UploadSuccessHandler extractUploadSuccessHandler;

	protected FileDialogCompleteHandler extractFileDialogCompleteHandler;

	protected FileQueuedHandler extractFileQueuedHandler;

	// upload handlers for importing document type
	protected FileQueuedHandler documentFileQueuedHandler;

	protected FileDialogCompleteHandler documentFileDialogCompleteHandler;

	protected UploadCompleteHandler documentUploadCompleteHandler;

	protected UploadSuccessHandler documentUploadSuccessHandler;

	private boolean isValid = true;

	private boolean isDocumentSelected = true;

	private boolean containValidFile;

	private List<String> filesNotSupportedList = new ArrayList<String>();

	private static final String LEARN_FILE_FORM_ACTION = "/dcma/gxt-admin/documentFileLearningUpload?";

	private static final String IMPORT_DOCUMENT_TYPE_UPLOAD = "/dcma/gxt-admin/importDocumentTypeUpload?";

	private static final String UPLOAD_TEST_CLASSIFICATION_EXTRACTION_PATH = "/dcma/gxt-admin/uploadTestClassificationFile?";

	interface Binder extends UiBinder<Widget, DocumentTypeLearningView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	public DocumentTypeLearningView() {
		super();
		viewDocLearnFileView = new ViewDocLearnFileGridView();
		initWidget(binder.createAndBindUi(this));
		setUploaderIds();
		importDocumentTypeDTO = new ImportDocumentTypeDTO();
		this.initialiseLearningViewUploader();
		this.initialiseImportDocumentViewUploader();
		this.initialiseClassifyUploader();
		this.initialiseExtractionUploader();
		learneFileUploader.addFileQueuedHandler(learnFileQueuedHandler);
		learneFileUploader.setButtonText(LocaleDictionary.getConstantValue(BatchClassConstants.UPLOAD_LEARN_FILES));
		learneFileUploader.addFileDialogCompleteHandler(learnFileDialogCompleteHandler);
		learneFileUploader.addUploadSuccessHandler(learnUploadSuccessHandler);

		classifyFileUploader.setButtonText(LocaleDictionary.getConstantValue(BatchClassConstants.UPLOAD_TEST_CLASSIFICATION_FILES));
		classifyFileUploader.addFileQueuedHandler(classifyFileQueuedHandler);
		classifyFileUploader.addFileDialogCompleteHandler(classifyFileDialogCompleteHandler);
		classifyFileUploader.addUploadSuccessHandler(classifyUploadSuccessHandler);

		extractFileUploader.setButtonText(LocaleDictionary.getConstantValue(BatchClassConstants.UPLOAD_TEST_EXTRACTION_FILES));
		extractFileUploader.addFileQueuedHandler(extractFileQueuedHandler);
		extractFileUploader.addFileDialogCompleteHandler(extractFileDialogCompleteHandler);
		extractFileUploader.addUploadSuccessHandler(extractUploadSuccessHandler);

		documentTypeUploader.setButtonText(LocaleDictionary.getConstantValue(BatchClassConstants.IMPORT_DOCUMENT_TYPE));
		documentTypeUploader.addFileQueuedHandler(documentFileQueuedHandler);
		documentTypeUploader.addUploadSuccessHandler(documentUploadSuccessHandler);
		documentTypeUploader.addUploadCompleteHandler(documentUploadCompleteHandler);
		documentTypeUploader.addFileDialogCompleteHandler(documentFileDialogCompleteHandler);
	}

	private void setUploaderIds() {
		documentTypeUploader.setUploaderID("docTypeImportId");
	}

	protected void initialiseExtractionUploader() {

		extractFileQueuedHandler = new FileQueuedHandler() {

			@Override
			public boolean onFileQueued(FileQueuedEvent fileQueuedEvent) {
				String uploadedFileName = fileQueuedEvent.getFile().getName().toLowerCase();
				boolean isFileValid = false;
				if (FileType.isValidFileName(uploadedFileName)) {
					String uploadLearnFilePath = StringUtil.concatenate(DocumentTypeConstants.UPLOAD_FILE_REQ_PARAMERTER,
							DocumentTypeConstants.EQUAL, fileQueuedEvent.getFile().getName());
					String batchClassIdentifier = presenter.getBatchClassIdentifer();
					extractFileUploader.setUploadURL(StringUtil.concatenate(UPLOAD_TEST_CLASSIFICATION_EXTRACTION_PATH,
							uploadLearnFilePath, "&", DocumentTypeConstants.IS_TEST_CLASSIFICATIONFILE, DocumentTypeConstants.EQUAL,
							"false", "&", DocumentTypeConstants.BATCH_CLASS_ID_REQ_PARAMETER, DocumentTypeConstants.EQUAL,
							batchClassIdentifier));
					isFileValid = true;
				} else {
					isFileValid = false;
					extractFileUploader.cancelUpload(fileQueuedEvent.getFile().getId(), false);
					getFilesNotSupportedList().add(uploadedFileName);
				}
				return isFileValid;
			}
		};
		extractFileDialogCompleteHandler = new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
				boolean isValidFileUpload = true;
				if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
					if (extractFileUploader.getUploadsInProgress() <= 0) {
						extractFileUploader.startUpload();
					}
				}
				if (filesNotSupportedList.size() != 0) {
					filesNotSupportedAction(filesNotSupportedList);
					isValidFileUpload = false;
				}
				return isValidFileUpload;
			}
		};

		extractUploadSuccessHandler = new UploadSuccessHandler() {

			@Override
			public boolean onUploadSuccess(UploadSuccessEvent uploadSuccessEvent) {
				if (extractFileUploader.getFilesQueued() == 1) {
					Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.FILE_UPLOADED_SUCCESSFULLY_FOR_EXTRACTION));
				}
				return true;
			}
		};
	}

	public void initialiseLearningViewUploader() {
		learnFileQueuedHandler = new FileQueuedHandler() {

			@Override
			public boolean onFileQueued(FileQueuedEvent fileQueuedEvent) {
				String uploadedFileName = fileQueuedEvent.getFile().getName().toLowerCase();
				boolean isFileValid = false;
				if (null == presenter.getDocumentType()) {
					setDocumentSelected(false);
					learneFileUploader.cancelUpload(fileQueuedEvent.getFile().getId(), false);
				} else {
					setDocumentSelected(true);
					if (FileType.isValidFileName(uploadedFileName)) {
						String uploadLearnFilePath = StringUtil.concatenate(DocumentTypeConstants.UPLOAD_FILE_REQ_PARAMERTER,
								DocumentTypeConstants.EQUAL, fileQueuedEvent.getFile().getName());
						String batchClassIdentifier = presenter.getBatchClassIdentifer();
						String documentType = presenter.getDocumentType().getName();
						learneFileUploader
								.setUploadURL(StringUtil.concatenate(LEARN_FILE_FORM_ACTION, uploadLearnFilePath, "&",
										DocumentTypeConstants.DOCUMENT_TYPE_REQ_PARAMETER, DocumentTypeConstants.EQUAL, documentType,
										"&", DocumentTypeConstants.BATCH_CLASS_ID_REQ_PARAMETER, DocumentTypeConstants.EQUAL,
										batchClassIdentifier));
						containValidFile = true;
						isFileValid = true;
					} else {
						isFileValid = false;
						learneFileUploader.cancelUpload(fileQueuedEvent.getFile().getId(), false);
						getFilesNotSupportedList().add(uploadedFileName);
					}
				}
				return isFileValid;
			}
		};
		learnFileDialogCompleteHandler = new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
				boolean isValidUpload = true;
				if (presenter.isBatchClassDirty()) {
					isValidUpload = false;
					applyBatchClassBeforeOperation(LocaleDictionary.getConstantValue(BatchClassConstants.LEARN_FILES));
				} else if (filesNotSupportedList.size() != 0 && !containValidFile) {
					filesNotSupportedAction(filesNotSupportedList);
					isValidUpload = false;
				} else if (!isDocumentSelected()) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), LocaleDictionary
							.getMessageValue(BatchClassMessages.SELECT_DOCUMENT_TYPE_TO_UPLOAD_FILES_FOR_THEIR_LEARNING),
							DialogIcon.ERROR);
					isValidUpload = false;
				} else if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
					if (learneFileUploader.getUploadsInProgress() <= 0) {
						learneFileUploader.startUpload();
					}
				}
				setDocumentSelected(true);
				return isValidUpload;
			}
		};

		learnUploadSuccessHandler = new UploadSuccessHandler() {

			@Override
			public boolean onUploadSuccess(UploadSuccessEvent uploadSuccessEvent) {
				if (learneFileUploader.getFilesQueued() == 1) {
					containValidFile = false;
					if (filesNotSupportedList.size() != 0) {
						BatchClassManagementEventBus.fireEvent(new LearnFilesStartEvent(true));
					} else {
						BatchClassManagementEventBus.fireEvent(new LearnFilesStartEvent(false));
					}
				}
				return true;
			}
		};
	}

	protected void initialiseClassifyUploader() {
		classifyFileQueuedHandler = new FileQueuedHandler() {

			@Override
			public boolean onFileQueued(FileQueuedEvent fileQueuedEvent) {
				String uploadedFileName = fileQueuedEvent.getFile().getName().toLowerCase();
				boolean isFileValid = false;
				if (FileType.isValidFileName(uploadedFileName)) {
					String uploadLearnFilePath = StringUtil.concatenate(DocumentTypeConstants.UPLOAD_FILE_REQ_PARAMERTER,
							DocumentTypeConstants.EQUAL, fileQueuedEvent.getFile().getName());
					String batchClassIdentifier = presenter.getBatchClassIdentifer();
					classifyFileUploader.setUploadURL(StringUtil.concatenate(UPLOAD_TEST_CLASSIFICATION_EXTRACTION_PATH,
							uploadLearnFilePath, "&", DocumentTypeConstants.IS_TEST_CLASSIFICATIONFILE, DocumentTypeConstants.EQUAL,
							"true", "&", DocumentTypeConstants.BATCH_CLASS_ID_REQ_PARAMETER, DocumentTypeConstants.EQUAL,
							batchClassIdentifier));
					isFileValid = true;
				} else {
					isFileValid = false;
					classifyFileUploader.cancelUpload(fileQueuedEvent.getFile().getId(), false);
					getFilesNotSupportedList().add(uploadedFileName);
				}
				return isFileValid;
			}
		};
		classifyFileDialogCompleteHandler = new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
				boolean isValidFileUpload = true;
				if (filesNotSupportedList.size() != 0) {
					filesNotSupportedAction(filesNotSupportedList);
					isValidFileUpload = false;
				}
				if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
					if (classifyFileUploader.getUploadsInProgress() <= 0) {
						classifyFileUploader.startUpload();
					}
				}
				return isValidFileUpload;
			}
		};
		classifyUploadSuccessHandler = new UploadSuccessHandler() {

			@Override
			public boolean onUploadSuccess(UploadSuccessEvent uploadSuccessEvent) {
				if (classifyFileUploader.getFilesQueued() == 1) {
					Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.FILE_UPLOADED_SUCCESSFULLY_FOR_CLASSIFICATION));
				}
				return true;
			}
		};

	}

	public void initialiseImportDocumentViewUploader() {

		documentFileQueuedHandler = new FileQueuedHandler() {

			@Override
			public boolean onFileQueued(FileQueuedEvent fileQueuedEvent) {
				boolean isFileValid = false;
				final String fileName = fileQueuedEvent.getFile().getName();
				final String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
				if (presenter.getController().getDocumentTypeView().getDocumentTypeGridView().getGrid().getStore().size() >= CoreCommonConstants.DOC_LIMIT) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
							LocaleDictionary.getConstantValue(LocaleCommonConstants.DOC_LIMIT_MESSAGE));
					documentTypeUploader.cancelUpload(fileQueuedEvent.getFile().getId(), false);
					setValid(false);
				} else {
					if (fileExtension.equalsIgnoreCase("zip")) {
						String importDocumentTypePath = "attachedLearnFile=" + fileQueuedEvent.getFile().getName();
						documentTypeUploader.setUploadURL(IMPORT_DOCUMENT_TYPE_UPLOAD + importDocumentTypePath);
						isFileValid = true;
					} else {
						if (isValid()) {
							DialogUtil
									.showMessageDialog(
											LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
											LocaleDictionary
													.getMessageValue(BatchClassMessages.VALID_INPUT_FILE_FORMATS_IS_ZIP_DISCARDING_INVALID_FILES),
											DialogIcon.ERROR);
							setValid(false);
							isFileValid = false;
						}
						documentTypeUploader.cancelUpload(fileQueuedEvent.getFile().getId(), false);
					}
				}
				return isFileValid;
			}
		};

		documentUploadCompleteHandler = new UploadCompleteHandler() {

			@Override
			public boolean onUploadComplete(UploadCompleteEvent uploadCompleteEvent) {
				return true;
			}
		};
		documentFileDialogCompleteHandler = new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
				if (presenter.isBatchClassDirty()) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_SAVE_PENDING_CHANGES_FIRST), DialogIcon.INFO);
				} else {
					if (fileDialogCompleteEvent.getNumberOfFilesSelected() > 1 && isValid()) {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.MULTIPLE_FILE_UPLOAD_NOT_SUPPORTED),
								DialogIcon.WARNING);
					} else if (fileDialogCompleteEvent.getNumberOfFilesSelected() == 1 && isValid()) {
						documentTypeUploader.startUpload();
						return true;
					}
				}
				setValid(true);
				return false;
			}
		};

		documentUploadSuccessHandler = new UploadSuccessHandler() {

			@Override
			public boolean onUploadSuccess(UploadSuccessEvent uploadSuccessEvent) {
				String tempZipFileLocation = uploadSuccessEvent.getServerData();
				presenter.importDocumentType(tempZipFileLocation, true);
				return true;
			}
		};

	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	public void setViewLearnFilesView(DocumentTypeDTO documentTypeDTO) {
		dragDropViewHorizontalPanel.clear();
		if (!viewDocLearnFileView.isAttached()) {
			presenter.getLearnFileInformation(documentTypeDTO);
			dragDropViewHorizontalPanel.add(viewDocLearnFileView);
		}
	}

	/**
	 * @return the viewDocumentLearnFile View
	 */
	public ViewDocLearnFileGridView getViewLearnFileView() {
		return viewDocLearnFileView;
	}

	public void setDragDropView() {
		dragDropViewHorizontalPanel.clear();
		dragDropViewHorizontalPanel.add(docLearningViewHorizontalPanel);

	}

	@Override
	public Grid getGrid() {
		return viewDocLearnFileView.getLearnFileGrid();
	}

	public void filesNotSupportedAction(List<String> filesNotSupported) {
		String filesNotImported = "<ol>";
		for (String unsupportedFileName : filesNotSupported) {
			filesNotImported = StringUtil.concatenate(filesNotImported, "<li>", unsupportedFileName, "<//li>");
		}
		final String message = LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_UPLOAD_FILE_UNSUPPORTED_FORMAT,
				filesNotSupported.size());
		// StringUtil.concatenate("Unable to upload ", filesNotSupported.size(), " file(s). Unsupported format.");
		final MessageDialog dialog = new MessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), message,
				DialogIcon.WARNING);

		dialog.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.YES);

		TextButton showFileList = dialog.getButton(PredefinedButton.YES);
		showFileList.setText(LocaleDictionary.getConstantValue(BatchClassConstants.SHOW_FILE_LIST));
		final String filesList = StringUtil.concatenate(filesNotImported, "<//ol>");
		showFileList.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.UNSUPPORTED_FILE_LIST), filesList);
			}
		});
		dialog.addButton(showFileList);
		dialog.show();
		dialog.addDialogHideHandler(new DialogHideHandler() {

			@Override
			public void onDialogHide(DialogHideEvent event) {
				getFilesNotSupportedList().clear();
			}
		});
	}

	/**
	 * Checks if files are valid.
	 * 
	 * @return true, if files are valid
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * Sets the valid.
	 * 
	 * @param isValid the new valid
	 */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public List<String> getFilesNotSupportedList() {
		return filesNotSupportedList;
	}

	public boolean isDocumentSelected() {
		return isDocumentSelected;
	}

	public void setDocumentSelected(boolean isDocumentSelected) {
		this.isDocumentSelected = isDocumentSelected;
	}

	public void enableLearnDropView(boolean enable) {
		learneFileUploader.setEnabled(enable);
	}

	public void applyBatchClassBeforeOperation(String operationName) {
		DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
				LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_SAVE_PENDING_CHANGES_FIRST_TO_LEARN_FILE_IN_A_DOCUMENT),
				DialogIcon.INFO);
	}
}
