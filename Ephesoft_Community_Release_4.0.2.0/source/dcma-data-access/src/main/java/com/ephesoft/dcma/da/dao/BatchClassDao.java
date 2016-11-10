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

package com.ephesoft.dcma.da.dao;

import java.util.List;
import java.util.Set;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.dao.CacheableDao;
import com.ephesoft.dcma.da.domain.BatchClass;

/**
 * This DAO deals with getting and setting properties of Batch Class.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Apr 10, 2015 <br/>
 * @version 1.0 <br/>
 *          $LastChangedDate: 2015-04-14 12:40:00 +0530 (Tue, 14 Apr 2015) $ <br/>
 *          $LastChangedRevision: 21592 $ <br/>
 */
public interface BatchClassDao extends CacheableDao<BatchClass> {

	/**
	 * An api to getch batch class by unc folder name.
	 * 
	 * @param folderName {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getBatchClassbyUncFolder(String folderName);

	/**
	 * An api to fetch BatchClass by batch Class name.
	 * 
	 * @param batchClassName {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getBatchClassbyName(String batchClassName);

	/**
	 * An api to fetch BatchClass by batch Class processName.
	 * 
	 * @param processName {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getBatchClassbyProcessName(String processName);

	/**
	 * This API will fetch all the batch classes.
	 * 
	 * @return {@link List}<{@link BatchClass}>
	 */
	List<BatchClass> getAllBatchClasses();

	/**
	 * This API will fetch all the unlocked batch classes.
	 * 
	 * @return {@link List}<{@link BatchClass}>
	 */
	List<BatchClass> getAllUnlockedBatchClasses();

	/**
	 * API to fetch a batch class by Identifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getBatchClassByIdentifier(String batchClassIdentifier);

	/**
	 * This method will update the existing batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 */
	void updateBatchClass(BatchClass batchClass);

	/**
	 * API to fetch BatchClass for the current user.
	 * 
	 * @param currentUser {@link String}
	 * @return {@link List}<{@link BatchClass}>
	 */
	List<BatchClass> getAllBatchClassesForCurrentUser(String currentUser);

	/**
	 * This API will fetch all the unc folder paths.
	 * 
	 * @return {@link List}<{@link String}>
	 */
	List<String> getAllUNCFolderPaths();

	/**
	 * API to get the list of Batch Classes specifying startindex, no of results and sorting if any.
	 * 
	 * @param firstResult {@link Integer}
	 * @param maxResults {@link Integer}
	 * @param order {@link Integer}
	 * @param userRoles {@link Set}<{@link String}>
	 * @return {@link List}<{@link BatchClass}>
	 */
	List<BatchClass> getBatchClassList(int startIndex, int maxResults, List<Order> order, Set<String> userRoles);

	/**
	 * This API will fetch all the batch classes of current user role.
	 * 
	 * @param userRoles {@link Set}<{@link String}>
	 * @return {@link List}<{@link BatchClass}>
	 */
	List<BatchClass> getAllBatchClassesByUserRoles(Set<String> userRoles);

	/**
	 * This API will fetch the size of batchclass.
	 * 
	 * @param userRoles {@link Set}<{@link String}>
	 * @return batchClass size
	 */
	int getAllBatchClassCountExcludeDeleted(Set<String> userRoles);

	/**
	 * This API will fetch the batch class list excluding the deleted batch class
	 * 
	 * @return {@link List}<{@link BatchClass}>
	 */
	List<BatchClass> getAllBatchClassExcludeDeleted();

	/**
	 * This API will fetch the batch class (eagerly loaded) list excluding the deleted batch class
	 * 
	 * @return {@link List}<{@link BatchClass}>
	 */
	List<BatchClass> getAllLoadedBatchClassExcludeDeleted();

	/**
	 * API to fetch the UNC folders for a batch class by name.
	 * 
	 * @param batchClassName {@link String}
	 * @return {@link List}<{@link String}>
	 */
	List<String> getAllAssociatedUNCFolders(String batchClassName);

	/**
	 * Gets the batch class by name including deleted.
	 * 
	 * @param batchClassName {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getBatchClassByNameIncludingDeleted(String batchClassName);

	/**
	 * API to get batch class identifier by UNC folder.
	 * 
	 * @param uncFolder {@link String}
	 * @return {@link String}
	 */
	String getBatchClassIdentifierByUNCfolder(String uncFolder);

	/**
	 * API to get all batch class identifiers.
	 * 
	 * @return {@link List}<{@link String}>
	 */
	List<String> getAllBatchClassIdentifiers();

	/**
	 * API to get all batch classes on the basis of excluding the deleted batch class and on the basis of ascending or desending order
	 * of specified property.
	 * 
	 * @param isExcludeDeleted boolean
	 * @param isAsc boolean
	 * @param propertyName {@link String}
	 * @return {@link List}<{@link BatchClass}>
	 */
	List<BatchClass> getAllBatchClasses(boolean isExcludeDeleted, boolean isAsc, String propertyName);

	/**
	 * API to get all the UNC folders on the basis of excluding the deleted batch class.
	 * 
	 * @param isExcludeDeleted {@link Boolean}
	 * @return {@link List}<{@link String}>
	 */
	List<String> getAllUNCList(boolean isExcludeDeleted);

	/**
	 * API to get all the user roles according to the given batchClassID.
	 * 
	 * @param userRoles {@link Set<{@link String}>}
	 * @param batchClassID {@link String}
	 * @return {@link Set}<{@link String}>
	 */
	BatchClass getBatchClassByUserRoles(Set<String> userRoles, String batchClassID);

	/**
	 * Gets batch class by batch class identifier including deleted.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getBatchClassByIdentifierIncludingDeleted(String batchClassIdentifier);

	/**
	 * API for clearing the current user for a given batch class.
	 * 
	 * @param batchClassIdentifier
	 */
	void clearCurrentUser(String batchClassIdentifier);

	/**
	 * This API will fetch all the batch classes on user role, sorted on basis of batch class description.
	 * 
	 * @param userRoles Set<{@link String}>- set of user roles
	 * @return {@link List}<BatchClass>- list of batch classes sorted on basis of batch class description
	 */
	List<BatchClass> getBatchClassesSortedOnDescription(Set<String> userRoles);

	/**
	 * Gets the list of deleted batch classes.
	 * 
	 * @return {@link List}<{@link BatchClass}> list of deleted batch class.
	 */
	List<BatchClass> getAllDeletedBatchClass();

	/**
	 * Gets all the UNC folders list excluding the default batch class UNCs.
	 * 
	 * @param isExcludeDeleted {@link Boolean} true to include deleted batch classes.
	 * @return {@link List}<{@link String}>
	 */
	List<String> getAllUNCListExcludingDefaultBatchClasses(boolean isExcludeDeleted);

	/**
	 * Gets deleted batch classes by unc folder name for overriden class.
	 * 
	 * @param uncFolderName {@link String}
	 * @return {@link List}<{@link BatchClass}>
	 */
	List<BatchClass> getDeletedBatchClassesbyUncFolder(String uncFolderName);

}
