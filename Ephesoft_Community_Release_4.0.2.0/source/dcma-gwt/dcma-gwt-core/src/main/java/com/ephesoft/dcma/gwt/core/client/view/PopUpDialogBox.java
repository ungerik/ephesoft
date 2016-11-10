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

package com.ephesoft.dcma.gwt.core.client.view;

import com.ephesoft.dcma.gwt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class is a custom dialog box with customised ok and cancel buttons and their listeners.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 22-Jun-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class PopUpDialogBox extends DialogBox {

	/**
	 * Instance of main panel for table view.
	 */
	@UiField
	protected VerticalPanel listMainPanel;

	/**
	 * Instance of vertical panel for buttons in dialog box.
	 */
	@UiField
	protected HorizontalPanel horizontalButtonPanel;

	/**
	 * Instance of binder of gwt to ui.xml.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Instance of listener to dialog box.
	 */
	private DialogBoxListener listener;

	/**
	 * Instance of button for ok click.
	 */
	private Button okButton;

	/**
	 * Instance of button for cancel click.
	 */
	private Button cancelButton;

	/**
	 * Constant for button panel height.
	 */
	private static final String BUTTON_PANEL_HEIGHT = "5px";

	/**
	 * Constructor.
	 * 
	 * @param showOkButton true if ok button is needed.
	 * @param showCancelButton true if cancel button is needed.
	 * @param center true if dialog box is to be center aligned.
	 * @param isVisible true if dialog box is to be visible immediately on construction.
	 */
	public PopUpDialogBox(final boolean showOkButton, final boolean showCancelButton, final boolean center, final boolean isVisible) {
		super();
		setWidget(BINDER.createAndBindUi(this));
		addStyleName(CoreCommonConstants.CSS_DIALOG_BOX);
		okButton = new Button(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.title_confirmation_ok),
				new ClickHandler() {

					@Override
					public void onClick(final ClickEvent clickEvent) {
						listener.onOkClick();
					}
				});
		cancelButton = new Button(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.title_confirmation_cancel),
				new ClickHandler() {

					@Override
					public void onClick(final ClickEvent clickEvent) {
						listener.onCancelClick();
					}
				});
		okButton.setVisible(showOkButton);
		cancelButton.setVisible(showCancelButton);
		horizontalButtonPanel.setHeight(BUTTON_PANEL_HEIGHT);
		if (showOkButton) {
			horizontalButtonPanel.add(okButton);
			horizontalButtonPanel.setCellHorizontalAlignment(okButton, HasHorizontalAlignment.ALIGN_RIGHT);
			horizontalButtonPanel.setCellVerticalAlignment(okButton, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		if (showCancelButton) {
			horizontalButtonPanel.add(cancelButton);
			horizontalButtonPanel.setCellHorizontalAlignment(cancelButton, HasHorizontalAlignment.ALIGN_LEFT);
			horizontalButtonPanel.setCellVerticalAlignment(cancelButton, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		horizontalButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		setAnimationEnabled(true);
		setGlassEnabled(true);
		if (center) {
			center();
		}
		if (isVisible) {
			show();
		}
		listMainPanel.setVisible(true);
	}

	/**
	 * Setter for height of list main panel.
	 * 
	 * @param height {@link String}
	 */
	public void setListMainPanelHeight(final String height) {
		listMainPanel.setHeight(height);
	}

	/**
	 * Getter for height of list main panel.
	 * 
	 * @param width {@link String}
	 */
	public void setListMainPanelWidth(final String width) {
		listMainPanel.setWidth(width);
	}

	/**
	 * Adds a table to the dialog box.
	 * 
	 * @param flowPanel {@link FlowPanel}
	 */
	public void addTable(final FlowPanel flowPanel) {
		listMainPanel.add(flowPanel);
	}

	/**
	 * Adds a listener to the actions performed on dialog box.
	 * 
	 * @param listener {@link DialogBoxListener}
	 */
	public void addDialogBoxListener(final DialogBoxListener listener) {
		this.listener = listener;
	}

	/**
	 * Gets okButton.
	 * 
	 * @return {@link Button}
	 */
	public Button getOkButton() {
		return okButton;
	}

	/**
	 * Sets okButton.
	 * 
	 * @param okButton {@link Button}
	 */
	public void setOkButton(final Button okButton) {
		this.okButton = okButton;
	}

	/**
	 * Gets cancelButton.
	 * 
	 * @return {@link Button}
	 */
	public Button getCancelButton() {
		return cancelButton;
	}

	/**
	 * Sets cancelButton.
	 * 
	 * @param cancelButton {@link Button}
	 */
	public void setCancelButton(final Button cancelButton) {
		this.cancelButton = cancelButton;
	}

	/**
	 * Interface for listeners to buttons of PopUpDialogBox.
	 * 
	 * @author Ephesoft
	 * 
	 *         <b>created on</b> 22-Jun-2013 <br/>
	 * @version $LastChangedDate:$ <br/>
	 *          $LastChangedRevision:$ <br/>
	 * 
	 * @see PopUpDialogBox
	 */
	public interface DialogBoxListener {

		/**
		 * Method to be implemented by listener for ok button on dialog box.
		 */
		void onOkClick();

		/**
		 * Method to be implemented by listener for close button on dialog box.
		 */
		void onCancelClick();
	}

	/**
	 * Interface for UIBinder.
	 * 
	 * @author Ephesoft
	 * 
	 *         <b>created on</b> 22-Jun-2013 <br/>
	 * @version $LastChangedDate:$ <br/>
	 *          $LastChangedRevision:$ <br/>
	 */
	interface Binder extends UiBinder<VerticalPanel, PopUpDialogBox> {
	}

}
