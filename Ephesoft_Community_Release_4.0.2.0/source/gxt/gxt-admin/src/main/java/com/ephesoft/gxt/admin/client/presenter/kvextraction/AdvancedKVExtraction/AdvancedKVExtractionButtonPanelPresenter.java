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

package com.ephesoft.gxt.admin.client.presenter.kvextraction.AdvancedKVExtraction;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.AdvancedKVExtractionHideEvent;
import com.ephesoft.gxt.admin.client.event.ReloadImageEvent;
import com.ephesoft.gxt.admin.client.event.ReloadKVGridEvent;
import com.ephesoft.gxt.admin.client.event.ReloadUploadedFilesEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionButtonPanelView;
import com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction.ColorGenerator;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.AdvancedKVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.AdvancedKVExtractionDetailDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.KVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.OutputDataCarrierDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.ContentPanel;

public class AdvancedKVExtractionButtonPanelPresenter extends AdvancedKVExtractionInlinePresenter<AdvancedKVExtractionButtonPanelView> {

	interface CustomEventBinder extends EventBinder<AdvancedKVExtractionButtonPanelPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	private static final char EXTENSION_CHAR = '.';
	private static final String DASH = "-";

	/**
	 * Constructor.
	 * 
	 * @param controller {@link UploadBatchController}
	 * @param view {@link UploadBatchButtonPanelView}
	 */
	public AdvancedKVExtractionButtonPanelPresenter(final BatchClassManagementController controller,
			final AdvancedKVExtractionButtonPanelView view) {
		super(controller, view);
		controller.getAdvancedKVLayout().setResizeHandlerOnGrid(
				controller.getAdvKvExtractionView().getBottomPanelView());
	}

	@Override
	public void bind() {
		view.clearComboBoxValues();
		view.disableAllButtons();
		AdvancedKVExtractionDTO selectedAdvKVExtraction = controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO();
		if (selectedAdvKVExtraction != null) {
			Collection<AdvancedKVExtractionDetailDTO> detailDTOs = selectedAdvKVExtraction.getAdvKVExtractionDetailDTO();
			LinkedHashSet<String> imageNames = new LinkedHashSet<String>();
			if (!CollectionUtil.isEmpty(detailDTOs)) {
				for (AdvancedKVExtractionDetailDTO detailDTO : detailDTOs) {
					if (detailDTO.getFileName() != null) {
						imageNames.add(detailDTO.getFileName());
					}
				}
				view.setDataAndReloadPage(imageNames);
			}
		}
	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);

	}

	public void testAdvKV() {

		String fileName = controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO().getSelectedImageName();
		if (validateFields()) {
			ScreenMaskUtility.maskScreen();
			saveDataInDTO(controller.getSelectedKVExtraction());
			FieldTypeDTO fieldTypeDTO = controller.getSelectedKVExtraction().getFieldTypeDTO();
			if (null != fieldTypeDTO && fieldTypeDTO.getDocTypeDTO() != null) {
				controller.getRpcService().testAdvancedKVExtraction(fieldTypeDTO.getDocTypeDTO().getBatchClass(),
						controller.getSelectedKVExtraction(), fieldTypeDTO.getDocTypeDTO().getName(), fileName,
						new AsyncCallback<List<OutputDataCarrierDTO>>() {

							@Override
							public void onFailure(Throwable throwable) {
								ScreenMaskUtility.unmaskScreen();
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.TEST_KV_FAILURE), DialogIcon.ERROR);
							}

							@Override
							public void onSuccess(List<OutputDataCarrierDTO> outputDtos) {
								ColorGenerator generator = new ColorGenerator();
								ScreenMaskUtility.unmaskScreen();
								if (!CollectionUtil.isEmpty(outputDtos) && outputDtos.get(0).getKey() != null) {
									controller.getAdvancedKVLayout().setAdvKVBottomPanelView(
											controller.getAdvKvExtractionView().getBottomPanelView());
									ContentPanel panel = controller.getAdvancedKVLayout().getBottomsPanel();
									panel.setExpanded(true);
									panel.forceLayout();
									for (OutputDataCarrierDTO outputDto : outputDtos) {
										outputDto.setColorCode(generator.getNextColor());
									}
									controller.getAdvKvExtractionPresenter().getAdvKvGridPresenter().reloadGrid(outputDtos);
									controller.getAdvKvExtractionPresenter().getAdvKvImageViewPresenter()
											.createAdvKVOverlays(outputDtos);
									controller.getAdvKvExtractionPresenter().getAdvKvGridPresenter().selectFirstRow();
//									controller.getAdvancedKVLayout().setResizeHandlerOnGrid(
//											controller.getAdvKvExtractionView().getBottomPanelView());
									view.enableDisableMenuButtons(false, false, true);
								} else {
									DialogUtil.showMessageDialog(
											LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
											LocaleDictionary.getMessageValue(BatchClassMessages.NO_RESULT_FOUND), DialogIcon.INFO);
									view.enableDisableMenuButtons(true, true, false);
								}
							}
						});
			}
		}

	}
	
	private void saveDataInDTO(final KVExtractionDTO selectedKVExtraction) {
		controller.getAdvKvExtractionPresenter().getAdvKVInputPanelPresenter().saveDataInDTO(selectedKVExtraction);

	}

	public void clearAdvKV() {
		controller.getAdvKvExtractionPresenter().getAdvKvImageViewPresenter().clearButtonClicked();
		changeBottomsPanelToDropDownPanel();
		view.enableApplyButton(true);
	}

	public void changeBottomsPanelToDropDownPanel() {
		controller.getAdvKvExtractionPresenter().getAdvKvGridPresenter().reloadGrid(new LinkedList<OutputDataCarrierDTO>());
		ContentPanel panel = controller.getAdvancedKVLayout().getBottomsPanel();
		panel.setExpanded(false);
		controller.getAdvancedKVLayout().setAdvKVBottomPanelView(controller.getAdvKvExtractionView().getDropFilePanelView());
		enableDisableMenuButtons(false, true, false);
	}

	public void exitAdvKVScreen() {
		controller.getEventBus().fireEvent(new AdvancedKVExtractionHideEvent());
	}

	public void onApplyButtonClicked() {
		boolean validFlag = validateFields();
		KVExtractionDTO selectedKVExtraction = controller.getSelectedKVExtraction();
		if (validFlag) {
			validFlag = controller.getAdvKvExtractionPresenter().getAdvKvImageViewPresenter().validateLengthWidth();
			if (!controller.getAdvKvExtractionPresenter().isEdit()) {
				selectedKVExtraction.getFieldTypeDTO().addKvExtraction(selectedKVExtraction);
			}
			saveDataInDTO(selectedKVExtraction);
			selectedKVExtraction.getFieldTypeDTO().getDocTypeDTO().getBatchClass().setDirty(true);
			exitAdvKVScreen();
			controller.getEventBus().fireEvent(new ReloadKVGridEvent());
		}
	}

	@EventHandler
	public void reloadPage(final ReloadUploadedFilesEvent reloadEvent) {
		view.setDataAndReloadPage(reloadEvent.getFileNames());
	}

	public void populatePages(String imageName) {

		final String image = imageName;
		FieldTypeDTO fieldTypeDTO = controller.getSelectedKVExtraction().getFieldTypeDTO();
		if (null != fieldTypeDTO && fieldTypeDTO.getDocTypeDTO() != null) {
			controller.getRpcService().getAdvancedKVImageUploadPath(fieldTypeDTO.getDocTypeDTO().getBatchClass().getIdentifier(),
					fieldTypeDTO.getDocTypeDTO().getName(), imageName, true, new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable paramThrowable) {
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_RETRIEVING_PATH), DialogIcon.ERROR);
						}

						@Override
						public void onSuccess(final List<String> pathUrlNSize) {
							if (null == pathUrlNSize || pathUrlNSize.size() < 2) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_RETRIEVING_PATH_OR_UPLOADING_FILE),
										DialogIcon.ERROR);
							} else {
								String pathUrl = pathUrlNSize.get(0);
								if (null == pathUrl || pathUrl.isEmpty()) {
									DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
											LocaleDictionary
													.getMessageValue(BatchClassMessages.ERROR_RETRIEVING_PATH_OR_UPLOADING_FILE),
											DialogIcon.ERROR);
								} else {
									Integer pageCount = Integer.parseInt(pathUrlNSize.get(1));
										if (pageCount == -1) {
											DialogUtil.showMessageDialog(LocaleDictionary.getMessageValue(BatchClassMessages.ERROR),
													LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_RETRIEVING_PAGE_COUNT),
													DialogIcon.ERROR);
											return;
										}
										setPropertiesInDTO(pageCount, image);
									view.setSinglePage(pageCount == 1);
									view.setImageUrl(pathUrl);
									view.populatePageListBox(pageCount);
								}
							}
						}
					});
		}
	}

	private void setPropertiesInDTO(Integer result, String imageName) {
		AdvancedKVExtractionDetailDTO detailDTO = controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO()
				.getAdvKVExtractionDetailByFileName(imageName);
		if (detailDTO != null) {
			detailDTO.setMultiPage(result != 1);
			detailDTO.setPageCount(result);
		}
	}

	public Integer getPageCount(String imageName) {
		AdvancedKVExtractionDetailDTO detailDTO = controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO()
				.getAdvKVExtractionDetailByFileName(imageName);
		if (detailDTO != null) {
			return detailDTO.getPageCount();
		}
		return null;
	}

	public void reloadimage(String imageName, int index) {
		if (view.isViewHOCRButtonDown()) {
			view.setViewHOCRButtonstate(false);
		}
		String tempUrl = view.getImageUrl();
		AdvancedKVExtractionDTO dto = controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO();
		String displayName = dto.getSelectedImageDisplayName();
		if (displayName.contains("/")) {
			displayName = displayName.substring(displayName.lastIndexOf("/"));
		}
		tempUrl = new String(StringUtil.concatenate(tempUrl.substring(0, tempUrl.lastIndexOf("/") + 1), displayName));
		dto.setSelectedImageDisplayName(tempUrl);
		controller.getEventBus().fireEvent(new ReloadImageEvent(imageName));
//		clearAdvKV();

	}

	public boolean validateFields() {
		return controller.getAdvKvExtractionPresenter().getAdvKVInputPanelPresenter().validateFields();

	}

	public void enableDisableMenuButtons(boolean apply, boolean testAdvKv, boolean clear) {
		view.enableDisableMenuButtons(apply, testAdvKv, clear);
	}

	public void enableAllButtons() {
		view.enableAllButtons();
	}

	public void setImageNameInDTO(String imageName, int index) {
		String displayImageName = imageName;
		if (!view.isSinglePage()) {
			String str = String.valueOf(index);
			str = StringUtil.concatenate(DASH, generatePadding(4 - str.length()), str);
			displayImageName = displayImageName.concat(str).concat(FileType.PNG.getExtensionWithDot());
			imageName = imageName.concat(str).concat(FileType.TIF.getExtensionWithDot());
		} else {
			if (imageName.toLowerCase().endsWith(FileType.PDF.getExtension())) {
				imageName = StringUtil.concatenate(imageName, FileType.TIF.getExtensionWithDot());
				displayImageName = StringUtil.concatenate(displayImageName, FileType.PNG.getExtensionWithDot());
			} else {
				displayImageName = StringUtil.concatenate(displayImageName.substring(0, displayImageName.lastIndexOf(EXTENSION_CHAR)),
						FileType.PNG.getExtensionWithDot());
			}
		}
		controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO().setSelectedImageName(imageName);
		controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO().setSelectedImageDisplayName(displayImageName);
		reloadimage(displayImageName, index);
	}

	public void onNextPreviousButtonClicked(boolean isNext) {
		view.onNextPreviousButtonClicked(isNext);
	}

	public void enabledisablePageButtons(boolean nextButton, boolean prevButton) {
		controller.getAdvKvExtractionPresenter().getAdvKvImageViewPresenter().enabledisableButtons(nextButton, prevButton);

	}

	public String generatePadding(int occurence) {
		String str = "";
		for (int i = 0; i < occurence; i++) {
			str = StringUtil.concatenate(str, "0");
		}
		return str;
	}

	public void onViewHOCRDataSelected(String imageName, boolean isSelected) {
		controller
				.getAdvKvExtractionPresenter()
				.getAdvKvImageViewPresenter()
				.generateHOCRData(controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO().getSelectedImageDisplayName(),
						isSelected);
		changeBottomsPanelToDropDownPanel();
	}

	public void setImageUrl(String pageImageUrl) {
		view.setImageUrl(pageImageUrl);
	}

	public void enableDisableHOCRButton(boolean isEnabled) {
		view.enableHOCRButton(isEnabled);
	}

	public void resetView() {
		view.clearComboBoxValues();
	}

	public void setViewOCRTogglerState(boolean state) {
		view.setViewHOCRButtonstate(state, false);
		view.enableApplyButton(true);
	}

}
