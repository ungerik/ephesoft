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

package com.ephesoft.gxt.admin.client.util;

import java.util.Arrays;
import java.util.List;

import com.ephesoft.dcma.core.common.KVExtractZone;
import com.ephesoft.gxt.admin.client.KeyFuzziness;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.shared.util.ParseUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.user.client.ui.ListBox;

public final class KVExtractionUtil {

	private KVExtractionUtil() {
		super();
	}

	public static boolean validateRange(double value, double startRange, double endRange) {
		boolean isValid = false;
		isValid = (value >= startRange && value <= endRange) ? true : false;
		return isValid;
	}

	/**
	 * Sets all the Values in Extract Zone drop down list.
	 * 
	 * @param extractZoneListBox {@link ListBox} for extract zone.
	 */
	public static void setExtractZone(final ComboBox extractZoneComboBox) {
		if (extractZoneComboBox != null) {
			//extractZoneListBox.setVisibleItemCount(BatchClassConstants.DEFAULT_VISIBLE_ITEMS_COUNT);
			KVExtractZone[] kvExtractZones = KVExtractZone.values();
			for (KVExtractZone kvExtractZoneData : kvExtractZones) {
				extractZoneComboBox.getStore().add(kvExtractZoneData.name());
			}
		}
	}

	/**
	 * Sets the Extract zone index.
	 * 
	 * @param kvExtractZone {@link KVExtractZone } which is to be set as selected.
	 * @param extractZoneListBox {@link ListBox} for extract zone.
	 */
	public static void setExtractZone(final KVExtractZone kvExtractZone, final ComboBox extractZoneComboBox) {
		if (extractZoneComboBox != null) {
			if (extractZoneComboBox.getStore().size() == 0) {
				setExtractZone(extractZoneComboBox);
			}
			extractZoneComboBox.setValue(extractZoneComboBox.getStore().get(findKVExtractZoneIndex(kvExtractZone)), true);
		}
	}

	/**
	 * Returns selected extract zone.
	 * 
	 * @param extractZoneListBox {@link ListBox } whose selected index is to be used.
	 * @return {@link KVExtractZone } which is already selected in ListBox.
	 * 
	 */
	public static KVExtractZone getExtractZone(final ComboBox extractZoneComboBox) {
		KVExtractZone selectedKVExtractZone = null;
		if (extractZoneComboBox != null) {
			final int extractZoneSelectedIndex = extractZoneComboBox.getSelectedIndex();
			final KVExtractZone[] allKVExtractZones = KVExtractZone.values();
			if (extractZoneSelectedIndex < extractZoneComboBox.getStore().size()) {
				final String selectedExtractZoneText = extractZoneComboBox.getStore().get(extractZoneSelectedIndex);
				for (KVExtractZone kvExtractZone : allKVExtractZones) {
					if (kvExtractZone != null) {
						String kvExtractZoneName = kvExtractZone.name();
						if (kvExtractZoneName != null && kvExtractZoneName.equals(selectedExtractZoneText)) {
							selectedKVExtractZone = kvExtractZone;
							break;
						}
					}
				}
			}
			if (null == selectedKVExtractZone) {
				selectedKVExtractZone = allKVExtractZones[BatchClassConstants.LIST_START_INDEX];
			}
		}
		return selectedKVExtractZone;
	}

	/**
	 * Finds the index of kv extract zone.
	 * 
	 * @param kvExtractZone {@link KVExtractZone} whose index is to find.
	 * @return index of selected KV extract zone, If parameter is null then returns 0.
	 */
	public static int findKVExtractZoneIndex(final KVExtractZone kvExtractZone) {
		int kvExtractZoneIndex;
		if (kvExtractZone == null) {
			kvExtractZoneIndex = BatchClassConstants.LIST_START_INDEX;
		} else {
			KVExtractZone[] allZoneTypes = KVExtractZone.values();
			List<KVExtractZone> tempExtractZoneList = Arrays.asList(allZoneTypes);
			kvExtractZoneIndex = tempExtractZoneList.indexOf(kvExtractZone);
		}
		return kvExtractZoneIndex;
	}

	/**
	 * Returns the index of the list based on the {@code fuzzyMatchThershold} value passed.
	 * 
	 * @param fuzzyMatchThershold {@link Float} represents the fuzzy match threshold defined for key pattern.
	 * @return the index of the list based on the {@code fuzzyMatchThershold} value passed.
	 */
	public static int getKeyFuzzinessIndex(final Float fuzzyMatchThershold) {
		int index = 0;
		if (null != fuzzyMatchThershold) {
			final String fuzzinessThresholdValue = ParseUtil.getRoundOffValue(String.valueOf(fuzzyMatchThershold), 2);
			if (!StringUtil.isNullOrEmpty(fuzzinessThresholdValue)) {
				for (KeyFuzziness keyFuzziness : KeyFuzziness.values()) {
					if (keyFuzziness != null) {
						final String keyFuzzinessValue = ParseUtil.getRoundOffValue(String.valueOf(keyFuzziness.getFuzzinessValue()),
								2);
						if (fuzzinessThresholdValue.equalsIgnoreCase(keyFuzzinessValue)) {
							index = keyFuzziness.getIndex();
							break;
						}
					}
				}
			}
		}
		return index;
	}

	/**
	 * Returns the {@link Float} value for the fuzzy match threshold value selected for a key pattern.
	 * 
	 * @param selectedValue {@link String} represents the value selected in the drop down for selecting the key fuzziness for key
	 *            pattern.
	 * @return the fuzzy match threshold value selected.
	 */
	public static Float getKeyFuzzinessValue(final String selectedValue) {
		Float thresholdValue = null;
		if (!StringUtil.isNullOrEmpty(selectedValue)) {
			for (KeyFuzziness keyFuzziness : KeyFuzziness.values()) {
				if (null != keyFuzziness && selectedValue.equals(keyFuzziness.getPercentageFuzzinessValue())) {
					thresholdValue = keyFuzziness.getFuzzinessValue();
					break;
				}
			}
		}
		return thresholdValue;
	}

	/**
	 * Returns the key pattern fuzziness value in percantage used for defining the fuzzy match threshold value for the key pattern.
	 * 
	 * @param thresholdValue {@link Float} represents the fuzzy match threshold defined for key pattern.
	 * @return the percentage value of the {@code Threshold} passed.
	 */
	public static String getKeyFuzzinessPercentageValue(final Float thresholdValue) {
		String keyFuzzinessValue = AdminConstants.EMPTY_STRING;
		if (null != thresholdValue) {
			String fuzzinessThresholdValue = ParseUtil.getRoundOffValue(String.valueOf(thresholdValue), 2);
			if (!StringUtil.isNullOrEmpty(fuzzinessThresholdValue)) {
				for (KeyFuzziness keyFuzziness : KeyFuzziness.values()) {
					if (keyFuzziness != null) {
						final String keyFuzzinessVal = ParseUtil.getRoundOffValue(String.valueOf(keyFuzziness.getFuzzinessValue()), 2);
						if (fuzzinessThresholdValue.equalsIgnoreCase(keyFuzzinessVal)) {
							keyFuzzinessValue = keyFuzziness.getPercentageFuzzinessValue();
							break;
						}
					}
				}
			}
		}
		return keyFuzzinessValue;
	}

	/**
	 * Poulates the {@link ListBox} with the fuzzy match threshold values and adds these options in the list.
	 * 
	 * @param keyFuzzinessListBox {@link ListBox} represents the instance of list box to which fuzzy match threshold option need to be
	 *            add.
	 */
	public static void populateKeyFuzzinessList(final ComboBox keyFuzzinessListBox) {
		if (null != keyFuzzinessListBox) {
			keyFuzzinessListBox.getStore().add(LocaleDictionary.getLocaleDictionary().getConstantValue(BatchClassConstants.NO_FUZZINESS));
			for (KeyFuzziness keyFuzziness : KeyFuzziness.values()) {
				if (keyFuzziness != null) {
					keyFuzzinessListBox.getStore().add(keyFuzziness.getPercentageFuzzinessValue());
				}
			}
			// Adding tool tip message for keyFuzzinessListBox
			keyFuzzinessListBox.setTitle(LocaleDictionary.getLocaleDictionary().getConstantValue(BatchClassConstants.KEY_FUZZINESS_TITLE));
		}
	}
}
