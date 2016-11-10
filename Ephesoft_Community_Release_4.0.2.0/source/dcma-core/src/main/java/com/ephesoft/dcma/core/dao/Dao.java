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

package com.ephesoft.dcma.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

/**
 * Generic interface for data access objects
 * 
 * @param <T> the underlying entity type.
 * 
 * @author Ephesoft
 * @version 1.0 <br/>
 */
public interface Dao<T> {

	/**
	 * Method to get object on base of object Id..
	 * 
	 * @param objectId Id of the object {@link Serializable}
	 * @return object {@link 
	 */
	T get(Serializable objectId);

	/**
	 * Method to create an object.
	 * 
	 * @param object
	 */
	void create(T object);

	/**
	 * Method to save or update the object data.
	 * 
	 * @param object
	 */
	void saveOrUpdate(T object);

	/**
	 * Method to remove an object.
	 * 
	 * @param object
	 */
	void remove(T object);

	/**
	 * Method to get all the objects.
	 * 
	 * @return List of all the objects.
	 */
	List<T> getAll();

	/**
	 * Method to merge the data of an object.
	 * 
	 * @param object
	 * @return
	 */
	T merge(T object);

	void evict(Object object);

	/**
	 * Method to count the number of objects based on given criteria.
	 * 
	 * @param criteria {@link DetachedCriteria}
	 * @return count {@link Integer}
	 */
	int count(DetachedCriteria criteria);

	/**
	 * Method to count the total number of objects.
	 * 
	 * @return No. of objects.
	 */
	int countAll();

	/**
	 * Method to remove the given objects.
	 * 
	 * @param entities Objects to be removed.
	 */
	void removeAll(Collection<T> entities);
}
