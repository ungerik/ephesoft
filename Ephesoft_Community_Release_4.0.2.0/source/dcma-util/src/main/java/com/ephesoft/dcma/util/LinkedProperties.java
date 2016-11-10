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

package com.ephesoft.dcma.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * This is a Properties class that maintains sequence of properties in a file.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 03-Sep-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class LinkedProperties extends Properties {

	/**
	 * Serial version Uid of the class.
	 */
	private static final long serialVersionUID = 6176597840039000421L;

	/**
	 * Map for key value pairs of properties.
	 */
	private Map<Object, Object> linkMap = new LinkedHashMap<Object, Object>();

	/**
	 * Message constant for informing a method not supported.
	 */
	private static final String NOT_SUPPORTED_MESSAGE = "method is not supported in";

	@Override
	public synchronized Object put(final Object key, final Object value) {
		return linkMap.put(key, value);
	}

	@Override
	public synchronized boolean contains(final Object value) {
		return linkMap.containsValue(value);
	}

	@Override
	public boolean containsValue(final Object value) {
		return linkMap.containsValue(value);
	}

	@Override
	public synchronized Enumeration<Object> elements() {
		throw new UnsupportedOperationException(EphesoftStringUtil.concatenate("elements ", NOT_SUPPORTED_MESSAGE, Thread
				.currentThread().getStackTrace()[0].getMethodName()));
	}

	@Override
	public Set<Map.Entry<Object, Object>> entrySet() {
		return linkMap.entrySet();
	}

	@Override
	public Set<Object> keySet() {
		return linkMap.keySet();
	}

	@Override
	public synchronized void clear() {
		linkMap.clear();
	}

	@Override
	public synchronized boolean containsKey(final Object key) {
		return linkMap.containsKey(key);
	}

	@Override
	public boolean equals(final Object o) {
		return linkMap.equals(o);
	}

	@Override
	public Object get(final Object key) {
		return linkMap.get(key);
	}

	@Override
	public String getProperty(final String key) {
		String property;
		Object value = get(key); // here the class Properties uses super.get()
		if (null == value) {
			property = null;
		} else {
			property = (value instanceof String) ? (String) value : null; // behavior of standard properties
		}
		return property;
	}

	@Override
	public boolean isEmpty() {
		return linkMap.isEmpty();
	}

	@Override
	public Enumeration<Object> keys() {
		throw new UnsupportedOperationException(EphesoftStringUtil.concatenate("keys ", NOT_SUPPORTED_MESSAGE, Thread.currentThread()
				.getStackTrace()[0].getMethodName()));
	}

	@Override
	public int size() {
		return linkMap.size();
	}

	@Override
	public Collection<Object> values() {
		return linkMap.values();
	}
}
