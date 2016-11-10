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

import java.util.Collection;

import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;

/**
 * A {@link SuggestBox} is a text box or text area which displays a pre-configured set of selections that match the user's input.
 * 
 * Each {@link SuggestBox} is associated with a single {@link SuggestOracle}. The {@link SuggestOracle} is used to provide a set of
 * selections given a specific query string.
 * 
 * @author Ephesoft.
 * @version 1.0.
 * 
 */
public class SuggestionBox extends SuggestBox {

	/**
	 * Initializes the SuggestBox with a default multiWordSuggestOracle. which will have empty set of suggestions.
	 */
	public SuggestionBox() {
		this(new MultiWordSuggestOracle());
	}

	/**
	 * Constructor for {@link SuggestionBox}. Creates a {@link TextBox} to use with this {@link SuggestionBox}.
	 * 
	 * @param oracle the oracle for this <code>SuggestBox</code>
	 */
	public SuggestionBox(final SuggestOracle oracle) {
		this(oracle, new TextBox());
	}

	/**
	 * Constructor for {@link SuggestionBox}. The text box will be removed from it's current location and wrapped by the
	 * {@link SuggestionBox}.
	 * 
	 * @param oracle supplies suggestions based upon the current contents of the text widget
	 * @param box the text widget
	 */
	public SuggestionBox(final SuggestOracle oracle, final ValueBoxBase<String> box) {
		super(oracle, box, new ScrollableSuggestDisplay());
	}

	public void addSuggestion(final Collection<String> suggestionToAdd) {
		if (!CollectionUtil.isEmpty(suggestionToAdd)) {
			final SuggestOracle suggestOracle = this.getSuggestOracle();
			if (suggestOracle instanceof MultiWordSuggestOracle) {
				final MultiWordSuggestOracle multiwordSuggestOracle = (MultiWordSuggestOracle) suggestOracle;
				multiwordSuggestOracle.addAll(suggestionToAdd);
			}
		}
	}

	public void addSuggestion(final String suggestionToAdd) {
		if (!StringUtil.isNullOrEmpty(suggestionToAdd)) {
			final SuggestOracle suggestOracle = this.getSuggestOracle();
			if (suggestOracle instanceof MultiWordSuggestOracle) {
				final MultiWordSuggestOracle multiwordSuggestOracle = (MultiWordSuggestOracle) suggestOracle;
				multiwordSuggestOracle.add(suggestionToAdd);
			}
		}
	}
}
