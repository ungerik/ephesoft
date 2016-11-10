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

package com.ephesoft.gxt.core.client.ui.widget;

import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.user.client.ui.Frame;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.google.gwt.user.client.Timer;

// TODO: Auto-generated Javadoc
/**
 * The Class ExternalDialogApp.
 */
public class ExternalDialogApp extends DialogWindow {

	/** The external app frame. */
	private Frame externalAppFrame;

	/**
	 * Instantiates a new external dialog app.
	 *
	 * @param urlToOpen the url to open
	 */
	public ExternalDialogApp(String urlToOpen) {
		externalAppFrame = new Frame();
		this.setClosable(true);
		if (!StringUtil.isNullOrEmpty(urlToOpen)) {
			this.setWidget(externalAppFrame);
			externalAppFrame.setUrl(urlToOpen);
			WidgetUtil.setID(this, urlToOpen);
			this.setSize("550", "500");
			Timer timer = new Timer() {
				
				@Override
				public void run() {
					ExternalDialogApp.this.center();
				}
			};
			timer.schedule(10);
		}
		this.setPredefinedButtons(PredefinedButton.CLOSE);
		externalAppFrame.addStyleName("externalAppFrame");
	}

	/* (non-Javadoc)
	 * @see com.ephesoft.gxt.core.client.ui.widget.DialogWindow#onButtonPressed(com.sencha.gxt.widget.core.client.button.TextButton)
	 */
	@Override
	protected void onButtonPressed(TextButton textButton) {
		super.onButtonPressed(textButton);
		if (getButton(PredefinedButton.OK) == textButton) {
			this.hide();
		}
	}
}
