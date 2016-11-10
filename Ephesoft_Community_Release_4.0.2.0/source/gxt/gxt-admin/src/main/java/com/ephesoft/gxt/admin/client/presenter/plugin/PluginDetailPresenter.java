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

package com.ephesoft.gxt.admin.client.presenter.plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.PluginNameConstants;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.plugin.PluginDetailView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginConfigDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;

public class PluginDetailPresenter extends BatchClassInlinePresenter<PluginDetailView> {

	private BatchClassPluginDTO batchClassPluginDTO;
	public static final int MAX_VISIBLE_ITEM_COUNT = 4;
	private Map<String, Widget> widgetMap;

	public PluginDetailPresenter(BatchClassManagementController controller, PluginDetailView view) {
		super(controller, view);
	}

	interface CustomEventBinder extends EventBinder<PluginDetailPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	@Override
	public void bind() {
		this.batchClassPluginDTO = controller.getSelectedBatchClassPlugin();
		controller.setSelectedBatchClassModule(batchClassPluginDTO.getBatchClassModule());
		createView();
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void setBatchClassPluginDTO(BatchClassPluginDTO batchClassPluginDTO) {
		this.batchClassPluginDTO = batchClassPluginDTO;
	}

	private void createView() {
		final Collection<BatchClassPluginConfigDTO> values = batchClassPluginDTO.getBatchClassPluginConfigs();
		view.initializePanel();
		createPluginConfigWidget(values);
	}

	private void createPluginConfigWidget(final Collection<BatchClassPluginConfigDTO> batchClassPluginConfigs) {
		widgetMap = new HashMap<String, Widget>();
		ComboBox hocrToPdfListBox = null;
		ComboBox createMultipagePdfOptimizationSwitchListBox = null;
		final ComboBox tabbedPdfOptimizationSwitchListBox = null;
		if (batchClassPluginConfigs.size() == 0) {
			view.addNoFieldExists();
		}
		for (final BatchClassPluginConfigDTO batchClassPluginConfig : batchClassPluginConfigs) {
			String label = StringUtil.concatenate(batchClassPluginConfig.getDescription());
			boolean isMandatory = false;
			if (batchClassPluginConfig.isMandatory()) {
				isMandatory = true;
			}
			List<String> sampleValueList = batchClassPluginConfig.getSampleValue();
			if (sampleValueList != null && !sampleValueList.isEmpty()) {
				if (sampleValueList.size() > 1) {

					// Create a listBox
					String batchClassPCValue = batchClassPluginConfig.getValue();
					if (batchClassPluginConfig.isMultivalue()) {
						// Create a multiple select list box
						int max_visible_item_count = MAX_VISIBLE_ITEM_COUNT;
						if (sampleValueList.size() < MAX_VISIBLE_ITEM_COUNT) {
							max_visible_item_count = sampleValueList.size();
						}
						final ListView fieldValue = view.addMultipleSelectListBox(sampleValueList, max_visible_item_count,
								batchClassPCValue);
						view.addWidget(label, fieldValue, isMandatory);

						// docFieldWidgets.add(new EditableWidgetStorage(fieldValue));
						fieldValue.addBlurHandler(new BlurHandler() {

							@Override
							public void onBlur(BlurEvent event) {
								final StringBuffer selectedItem = new StringBuffer();
								final List<String> itemsSelected = fieldValue.getSelectionModel().getSelectedItems();
								for (String item : itemsSelected) {
									selectedItem.append(item).append(';');
								}
								String selectedItemString = selectedItem.toString();
								selectedItemString = selectedItemString.substring(0, selectedItemString.length() - 1);
								batchClassPluginConfig.setValue(selectedItemString);
							}
						});

					} else {
						// Create a drop down
						if (StringUtil.isNullOrEmpty(batchClassPCValue)) {
							batchClassPCValue = sampleValueList.get(0);
						}

						final ComboBox fieldValue = view.addDropDown(sampleValueList, batchClassPCValue);
						fieldValue.setName(batchClassPluginConfig.getPluginConfig().getFieldName());
						widgetMap.put(fieldValue.getName(), fieldValue);

						fieldValue.addBlurHandler(new BlurHandler() {

							@Override
							public void onBlur(BlurEvent event) {

								String itemText = fieldValue.getValue();

								if (LocaleDictionary.getConstantValue(BatchClassConstants.SELECT_OPTION).equalsIgnoreCase(itemText)) {
									itemText = BatchClassConstants.EMPTY_STRING;
								}
								batchClassPluginConfig.setValue(itemText);

							}
						});

						if (batchClassPluginConfig.getPluginConfig().getFieldName()
								.equalsIgnoreCase(PluginNameConstants.EXPORT_PLUGIN_TYPE_DESC)) {
							hocrToPdfListBox = fieldValue;

							fieldValue.addValueChangeHandler(new ValueChangeHandler<String>() {

								@Override
								public void onValueChange(ValueChangeEvent<String> event) {
									enableExportProcessProps(fieldValue);
								}
							});
						}
						if (batchClassPluginConfig.getPluginConfig().getFieldName()
								.equalsIgnoreCase(PluginNameConstants.CREATE_MULTIPAGE_PDF_OPTIMIZATION_SWITCH)) {
							createMultipagePdfOptimizationSwitchListBox = fieldValue;
							fieldValue.addValueChangeHandler(new ValueChangeHandler<String>() {

								@Override
								public void onValueChange(ValueChangeEvent<String> event) {
									enableCreateMultipageOptimizationProps(fieldValue);
								}
							});
						}

						if (batchClassPluginConfig.getPluginConfig().getFieldName()
								.equalsIgnoreCase(PluginNameConstants.TABBED_PDF_OPTIMIZATION_SWITCH)) {
							createMultipagePdfOptimizationSwitchListBox = fieldValue;
							fieldValue.addValueChangeHandler(new ValueChangeHandler<String>() {

								@Override
								public void onValueChange(ValueChangeEvent<String> event) {
									enableTabbedPdfOptimizationProps(fieldValue);
								}
							});
						}

						view.addWidget(label, fieldValue, isMandatory);
					}

				} else {

					// Create a read only text box
					final TextField textField = view.addTextField(batchClassPluginConfig, true);
					view.addWidget(label, textField, isMandatory);
					widgetMap.put(textField.getName(), textField);
					textField.addBlurHandler(new BlurHandler() {

						@Override
						public void onBlur(BlurEvent event) {
							if (textField.getValue() != null) {
								if (textField.isValid()) {
									batchClassPluginConfig.setValue(textField.getValue());
								}
							}
						}
					});
				}

			} else if (batchClassPluginConfig.isPassword()) {
				// Create a password text box
				final PasswordField passwordField = view.addPasswordTextBox(batchClassPluginConfig, false);
				view.addWidget(label, passwordField, isMandatory);
				widgetMap.put(passwordField.getName(), passwordField);
				passwordField.addBlurHandler(new BlurHandler() {

					@Override
					public void onBlur(BlurEvent event) {
						if (passwordField.getValue() != null) {
							if (passwordField.isValid()) {
								batchClassPluginConfig.setValue(passwordField.getValue());
							}
						}
					}
				});

			} else {

				// Create a text box
				final TextField textField = view.addTextField(batchClassPluginConfig, false);
				view.addWidget(label, textField, isMandatory);
				widgetMap.put(textField.getName(), textField);
				textField.addBlurHandler(new BlurHandler() {

					@Override
					public void onBlur(BlurEvent event) {
						if (textField.getValue() != null) {
							if (textField.isValid()) {
								batchClassPluginConfig.setValue(textField.getValue());
							}
						}
					}
				});
			}
		}

		enableExportProcessProps(hocrToPdfListBox);
		enableCreateMultipageOptimizationProps(createMultipagePdfOptimizationSwitchListBox);
		enableTabbedPdfOptimizationProps(tabbedPdfOptimizationSwitchListBox);

	}

	/**
	 * Sets the properties of create multipage file plugin according to the selected multipage file export process.
	 * 
	 * @param fieldValue {@link ListBox}- value of selected multipage file export process
	 */
	private void enableExportProcessProps(final ComboBox fieldValue) {
		if (fieldValue != null) {
			for (final Widget widget : widgetMap.values()) {
				if (fieldValue.getValue().equalsIgnoreCase(PluginNameConstants.GHOSTSCRPT)) {
					if (widget != null && widget instanceof ComboBox) {
						ComboBox field = ((ComboBox) widget);
						String name = field.getName();
						if (name != null && name.equals(PluginNameConstants.EXPORT_COLORED_OUTPUT_PDF)) {
							field.setValue(field.getStore().get(1), true);
							field.setEnabled(false);
						}
						if (name != null && name.equals(PluginNameConstants.EXPORT_SEARCHABLE_OUTPUT_PDF)) {
							field.setValue(field.getStore().get(1), true);
							field.setEnabled(false);
						}
					}
					if (widget != null && widget instanceof TextField) {
						TextField field = ((TextField) widget);
						String name = field.getName();
						if (name != null && name.equals(PluginNameConstants.GHOSTSCRIPT_PARAMETERS)) {
							field.setEnabled(true);
						}
					}
				} else {
					if (widget != null && widget instanceof TextField) {
						TextField field = ((TextField) widget);
						String name = field.getName();
						if (name != null && name.equals(PluginNameConstants.GHOSTSCRIPT_PARAMETERS)) {
							field.setEnabled(false);
						}
					} else if (fieldValue.getValue().equalsIgnoreCase(PluginNameConstants.HOCR_TO_PDF)) {

						if (widget != null && widget instanceof ComboBox) {
							ComboBox field = ((ComboBox) widget);
							String name = field.getName();
							if (name != null && name.equals(PluginNameConstants.EXPORT_COLORED_OUTPUT_PDF)) {
								field.setEnabled(true);
							}
							if (name != null && name.equals(PluginNameConstants.EXPORT_SEARCHABLE_OUTPUT_PDF)) {
								field.setEnabled(true);
							}
						}
					} else {
						if (widget != null && widget instanceof ComboBox) {
							ComboBox field = ((ComboBox) widget);
							String name = field.getName();
							if (name != null && name.equals(PluginNameConstants.EXPORT_COLORED_OUTPUT_PDF)) {
								field.setValue(field.getStore().get(1), true);
								field.setEnabled(false);
							}
							if (name != null && name.equals(PluginNameConstants.EXPORT_SEARCHABLE_OUTPUT_PDF)) {
								field.setValue(field.getStore().get(1), true);
								field.setEnabled(false);
							}
						}
					}
				}
			}
		}
	}

	private void enableCreateMultipageOptimizationProps(final ComboBox fieldValue) {
		if (fieldValue != null) {
			for (final Widget editableWidgetStorage : widgetMap.values()) {
				if (fieldValue.getValue().equalsIgnoreCase(PluginNameConstants.ON_STRING)) {

					if (editableWidgetStorage != null && editableWidgetStorage instanceof TextField) {
						TextField field = ((TextField) editableWidgetStorage);
						String name = field.getName();
						if (name != null && name.equals(PluginNameConstants.CREATE_MULTIPAGE_PDF_OPTIMIZATION_PARAMETERS)) {
							field.setEnabled(true);
						}
					}
				} else {
					if (editableWidgetStorage != null && editableWidgetStorage instanceof TextField) {
						TextField field = ((TextField) editableWidgetStorage);
						String name = field.getName();
						if (name != null && name.equals(PluginNameConstants.CREATE_MULTIPAGE_PDF_OPTIMIZATION_PARAMETERS)) {
							field.setEnabled(false);
						}
					}
				}
			}
		}

	}

	private void enableTabbedPdfOptimizationProps(final ComboBox fieldValue) {
		if (fieldValue != null) {
			for (final Widget editableWidgetStorage : widgetMap.values()) {
				if (fieldValue.getValue().equalsIgnoreCase(PluginNameConstants.ON_STRING)) {
					if (editableWidgetStorage != null && editableWidgetStorage instanceof TextField) {
						TextField field = ((TextField) editableWidgetStorage);
						String name = field.getName();
						if (name != null && name.equals(PluginNameConstants.TABBED_PDF_OPTIMIZATION_PARAMETERS)) {
							field.setEnabled(true);
						}
					}
				} else {
					if (editableWidgetStorage != null && editableWidgetStorage instanceof TextField) {
						TextField field = ((TextField) editableWidgetStorage);
						String name = field.getName();
						if (name != null && name.equals(PluginNameConstants.TABBED_PDF_OPTIMIZATION_PARAMETERS)) {
							field.setEnabled(false);
						}
					}
				}
			}
		}

	}

	public void addValidatorForDocumentField(final TextField textField) {
		final String textBoxValue = textField.getValue();
		final String nameOfWidget = textField.getName();
		if (nameOfWidget.equalsIgnoreCase(PluginNameConstants.DA_PREDEFINED_DOC_TYPE)
				|| nameOfWidget.equalsIgnoreCase(PluginNameConstants.DA_UNKNOWN_PREDEFINED_DOC_TYPE)) {
			textField.addValidator(new Validator<String>() {

				@Override
				public List<EditorError> validate(Editor<String> editor, String value) {

					Collection<DocumentTypeDTO> documentList = batchClassPluginDTO.getBatchClassModule().getBatchClass()
							.getDocuments();
					if (textBoxValue != null && !textBoxValue.isEmpty()) {
						boolean validDocType = false;
						for (DocumentTypeDTO document : documentList) {
							if (document.getName().equalsIgnoreCase(textBoxValue)) {
								validDocType = true;
								break;
							}
						}
						if (!validDocType) {
							String errorMsg = LocaleDictionary.getMessageValue(BatchClassConstants.WROND_DOC_TYPE_PROVIDED);
							if (nameOfWidget.equalsIgnoreCase(PluginNameConstants.DA_PREDEFINED_DOC_TYPE)) {
								errorMsg = errorMsg + PluginNameConstants.DA_PREDEFINED_DOC_TYPE_NAME;
							} else {
								errorMsg = errorMsg + PluginNameConstants.DA_UNKNOWN_PREDEFINED_DOC_TYPE_NAME;
							}
							return Collections.<EditorError> singletonList(new DefaultEditorError(editor, errorMsg, value));
						}
					}
					return null;
				}
			});
		}
	}

	public boolean isViewValid() {
		boolean isValid = true;
		for (Widget widget : widgetMap.values()) {
			if (widget instanceof TextField) {
				TextField textField = (TextField) widget;
				isValid = textField.isValid();
			} else if (widget instanceof ComboBox) {
				ComboBox comboBox = (ComboBox) widget;
				isValid = comboBox.isValid();
			} else if (widget instanceof PasswordField) {
				PasswordField passwordField = (PasswordField) widget;
				isValid = passwordField.isValid();
			}
			if (!isValid) {
				break;
			}
		}
		return isValid;
	}

}
