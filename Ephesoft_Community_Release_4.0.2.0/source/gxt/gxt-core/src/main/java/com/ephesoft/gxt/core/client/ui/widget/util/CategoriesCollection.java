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

package com.ephesoft.gxt.core.client.ui.widget.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.ephesoft.gxt.core.shared.SubCategorisedData;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;

public class CategoriesCollection {

	private final List<Category> categoryList;

	private final Set<String> uniqueSubCategories;

	private double maximumCategoryValue;

	public CategoriesCollection() {
		categoryList = new ArrayList<Category>();
		uniqueSubCategories = new TreeSet<String>();
	}

	/**
	 * @return the maximumCategoryValue
	 */
	public double getMaximumCategoryValue() {
		return maximumCategoryValue;
	}

	/**
	 * @return the categoryList
	 */
	public List<Category> getCategoryList() {
		return categoryList;
	}

	/**
	 * @return the uniqueSubCategories
	 */
	public Set<String> getUniqueSubCategories() {
		return uniqueSubCategories;
	}

	public void add(final SubCategorisedData stackedCategoryData) {
		if (null != stackedCategoryData) {
			final String categoryName = stackedCategoryData.getCategory();
			boolean categoryExist = false;
			Category existingCategory = null;
			if (!CollectionUtil.isEmpty(categoryList)) {
				for (final Category traversedCategory : categoryList) {
					if (categoryName.equalsIgnoreCase(traversedCategory.categoryName)) {
						categoryExist = true;
						existingCategory = traversedCategory;
						break;
					}
				}
			}
			final String subCategoryName = stackedCategoryData.getSubCategory();
			final SubCategory subCategory = new SubCategory(subCategoryName, stackedCategoryData.getData());
			if (categoryExist) {
				existingCategory.addSubCategory(subCategory);
				maximumCategoryValue = Math.max(maximumCategoryValue, existingCategory.totalData);
			} else {
				final Category newCategory = new Category(categoryName);
				newCategory.addSubCategory(subCategory);
				categoryList.add(newCategory);
				maximumCategoryValue = Math.max(maximumCategoryValue, newCategory.totalData);
			}
			uniqueSubCategories.add(subCategoryName);
		}
	}

	public static final class Category {

		private final String categoryName;
		private double id;
		private static double idGenerator;
		private List<SubCategory> subCategoryList;

		private double totalData;

		public Category(final String categoryName) {
			id = idGenerator++;
			this.categoryName = categoryName;
			this.totalData = 0;
		}

		/**
		 * @return the id
		 */
		public double getId() {
			return id;
		}

		public void addSubCategory(final SubCategory subCategory) {
			if (null != subCategory) {
				// TODO Empty Check
				totalData += subCategory.data;
				final String subCategoryName = subCategory.name;
				if (null == subCategoryList) {
					subCategoryList = new ArrayList<SubCategory>();
					subCategoryList.add(subCategory);
				} else {
					boolean subCategoryExist = false;
					SubCategory existingSubCategory = null;
					for (final SubCategory traversedSubCategory : subCategoryList) {
						if (subCategoryName.equalsIgnoreCase(traversedSubCategory.name)) {
							subCategoryExist = true;
							existingSubCategory = traversedSubCategory;
							break;
						}
					}
					if (subCategoryExist) {
						existingSubCategory.data = existingSubCategory.data + subCategory.data;
					} else {
						subCategoryList.add(subCategory);
					}
				}
			}
		}

		/**
		 * @return the categoryName
		 */
		public String getCategoryName() {
			return categoryName;
		}

		public SubCategory getSubCategoryByName(final String subCategoryName) {
			SubCategory subCategoryToFind = null;
			if (null != subCategoryName) {
				for (SubCategory subCategory : subCategoryList) {
					if (subCategoryName.equalsIgnoreCase(subCategory.getName())) {
						subCategoryToFind = subCategory;
						break;
					}
				}
			}
			return subCategoryToFind;
		}

		/**
		 * @return the subCategoryList
		 */
		public List<SubCategory> getSubCategoryList() {
			return subCategoryList;
		}

	}

	public static final class SubCategory {

		private final String name;
		private double data;

		public SubCategory(final String name, final double data) {
			this.name = name;
			this.data = data;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the data
		 */
		public double getData() {
			return data;
		}

	}
}
