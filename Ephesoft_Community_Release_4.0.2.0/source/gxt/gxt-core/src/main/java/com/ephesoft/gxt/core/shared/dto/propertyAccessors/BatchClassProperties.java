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

package com.ephesoft.gxt.core.shared.dto.propertyAccessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.RoleDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * This interface provides Property Accessors for {@link BatchClassDTO}
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */
public interface BatchClassProperties extends PropertyAccess<BatchClassDTO> {

	public static BatchClassProperties INSTANCE = GWT.create(BatchClassProperties.class);

	ModelKeyProvider<BatchClassDTO> id();

	ValueProvider<BatchClassDTO, String> identifier();

	ValueProvider<BatchClassDTO, String> name();

	ValueProvider<BatchClassDTO, Integer> priority();

	ValueProvider<BatchClassDTO, String> description();

	ValueProvider<BatchClassDTO, String> uncFolder();

	ValueProvider<BatchClassDTO, String> version();

	ValueProvider<BatchClassDTO, Boolean> isDirty();

	ValueProvider<BatchClassDTO, Boolean> isDeleted();

	ValueProvider<BatchClassDTO, Boolean> isDeployed();

	ValueProvider<BatchClassDTO, String> encryptionAlgo();

	ValueProvider<BatchClassDTO, Boolean> selected();

	ValueProvider<BatchClassDTO, String> currentUser();

	static class BatchClassMappingValueProvider {

		public static class BatchClassRoleValueProvider implements ValueProvider<BatchClassDTO, String> {

			private static BatchClassRoleValueProvider instance = new BatchClassRoleValueProvider();

			private BatchClassRoleValueProvider() {

			}

			/**
			 * @return the instance
			 */
			public static BatchClassRoleValueProvider getInstance() {
				return instance;
			}

			@Override
			public String getValue(final BatchClassDTO object) {
				StringBuilder name = new StringBuilder(CoreCommonConstant.EMPTY_STRING);
				if (null != object) {
					List<RoleDTO> assignedRoles = object.getAssignedRole();
					if (!CollectionUtil.isEmpty(assignedRoles)) {
						Set<String> superAdminGroup = EphesoftUIContext.getSuperAdminGroup();
						Set<String> allGroup = EphesoftUIContext.getAllGroups();
						int counter = 0;
						for (RoleDTO role : assignedRoles) {
							String roleName = role.getName();
							if (null != roleName) {
								if (!superAdminGroup.contains(roleName) && allGroup.contains(roleName)) {
									if (counter != 0) {
										name.append(";");
									}
									name.append(roleName);
									counter++;
								}
							}
						}
					}
				}
				return name.toString();
			}

			@Override
			public void setValue(final BatchClassDTO object, String value) {
				List<RoleDTO> assignRoles = new ArrayList<RoleDTO>();
				if (!StringUtil.isNullOrEmpty(value)) {

					RoleDTO roleDTO;
					// To set super admin groups which were previously assigned, if any.
					if (EphesoftUIContext.isSuperAdmin()) {
						Set<String> superAdminGroup = EphesoftUIContext.getSuperAdminGroup();
						for (String role : superAdminGroup) {
							if (null != role) {
								roleDTO = new RoleDTO();
								roleDTO.setName(role);
								assignRoles.add(roleDTO);
							}
						}
					}

					String[] values = value.split(";");
					if (null != values && values.length != 0) {
						for (int i = 0; i < values.length; i++) {
							roleDTO = new RoleDTO();
							roleDTO.setName(values[i]);
							assignRoles.add(roleDTO);
						}
					}
				}
				object.setAssignedRole(assignRoles);
				object.setDirty(true);
			}

			@Override
			public String getPath() {
				return null;
			}

		}

	}
}
