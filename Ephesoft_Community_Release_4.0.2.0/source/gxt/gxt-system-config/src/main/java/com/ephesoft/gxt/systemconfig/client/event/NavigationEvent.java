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

package com.ephesoft.gxt.systemconfig.client.event;

import com.google.web.bindery.event.shared.binder.GenericEvent;

public abstract class NavigationEvent extends GenericEvent {

	private final Object bindedObject;

	public NavigationEvent(final Object bindedObject) {
		this.bindedObject = bindedObject;
	}

	public Object getBindedObject() {
		return bindedObject;
	}

	public static class NavigationNodeAdditionEvent extends NavigationEvent {

		int index;

		public NavigationNodeAdditionEvent(final Object bindedObject) {
			this(bindedObject, -1);
		}

		public NavigationNodeAdditionEvent(final Object bindedObject, final int index) {
			super(bindedObject);
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}
	}

	public static class NavigationNodeRenameEvent extends NavigationEvent {

		private final String nameToAssign;

		public NavigationNodeRenameEvent(final Object bindedObject, final String nameToAssign) {
			super(bindedObject);
			this.nameToAssign = nameToAssign;
		}

		public String getNameToAssign() {
			return nameToAssign;
		}
	}

	public static class NavigationNodeDeletionEvent extends NavigationEvent {

		public NavigationNodeDeletionEvent(final Object bindedObject) {
			super(bindedObject);
		}
	}
}
