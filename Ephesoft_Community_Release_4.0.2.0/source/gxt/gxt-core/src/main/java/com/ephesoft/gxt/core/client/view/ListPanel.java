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

package com.ephesoft.gxt.core.client.view;

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.gxt.core.client.DomainView;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class ListPanel<T, Q extends DomainView<T, ?>> extends VerticalLayoutContainer {

	private final List<Q> domainViewList;

	private boolean isValid;

	public ListPanel() {
		this.domainViewList = new LinkedList<Q>();
		this.addStyleName("domainListPanel");
	}

	public void add(final Q view) {
		if (null != view) {
			domainViewList.add(view);
			Timer timer = new Timer() {

				@Override
				public void run() {
					ListPanel.super.add(view);
				}
			};
			timer.schedule(1);
		}
	}

	public Q getView(final T domainObject) {
		Q view = null;
		if (null != domainObject) {
			for (final Q domainView : domainViewList) {
				if (null != domainView && domainView.getBindedObject() == domainObject) {
					view = domainView;
					break;
				}
			}
		}
		return view;
	}

	public int indexOf(final T domainObject) {
		final Q view = getView(domainObject);
		int index = -1;
		if (view != null) {
			index = domainViewList.indexOf(view);
		}
		return index;
	}

	public Q getNextView(final T domainObject, final boolean isPrevious, final boolean errorView) {
		final int index = indexOf(domainObject);
		return isPrevious ? getPreviousInvalidView(index, errorView) : getNextInvalidView(index, errorView);
	}

	public Q getView(int index) {
		Q domainView = null;
		if (!CollectionUtil.isEmpty(domainViewList) && domainViewList.size() > index) {
			domainView = domainViewList.get(index);
		}
		return domainView;
	}

	private Q getPreviousInvalidView(final int index, final boolean errorView) {
		Q domainView = null;
		Q currentView;
		for (int currentIndex = index - 1; currentIndex >= 0; currentIndex--) {
			currentView = domainViewList.get(currentIndex);
			if (null != currentView && currentView.canFocus() && (!currentView.isValid() || !errorView)) {
				domainView = currentView;
				break;
			}
		}
		return domainView;
	}

	private Q getNextInvalidView(final int index, final boolean errorView) {
		Q domainView = null;
		Q currentView;
		final int size = domainViewList.size();
		for (int currentIndex = index + 1; currentIndex < size; currentIndex++) {
			currentView = domainViewList.get(currentIndex);
			if (null != currentView && currentView.canFocus() && (!currentView.isValid() || !errorView)) {
				domainView = currentView;
				break;
			}
		}
		return domainView;
	}

	public void remove(final Q view) {
		if (view != null) {
			domainViewList.remove(view);
			super.remove(view);
		}
	}

	/**
	 * @return the domainViewList.
	 */
	public List<Q> getDomainViewList() {
		return domainViewList;
	}

	/**
	 * @return the isValid
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * @param isValid the isValid to set
	 */
	public void setValid(final boolean isValid) {
		this.isValid = isValid;
	}

	public DomainView<?, ?> getFirstView(boolean onlyErrorView) {
		return getAdjacentView(true, onlyErrorView);
	}

	private DomainView<?, ?> getAdjacentView(boolean breakOnFirst, boolean isErrorView) {
		DomainView<?, ?> defaultDomainView = null;
		if (!CollectionUtil.isEmpty(domainViewList)) {
			defaultDomainView = domainViewList.get(0);
			for (final DomainView<?, ?> domainView : domainViewList) {
				if (null != domainView && domainView.canFocus() && (!domainView.isValid() || !isErrorView)) {
					defaultDomainView = domainView;
					if (breakOnFirst) {
						break;
					}
				}
			}
		}
		return defaultDomainView;
	}

	public DomainView<?, ?> getLastView(boolean onlyErrorView) {
		return getAdjacentView(false, onlyErrorView);
	}

	public Q getFirstView() {
		return CollectionUtil.isEmpty(domainViewList) ? null : domainViewList.get(0);
	}

	public void clear() {
		super.clear();
		domainViewList.clear();
	}
}
