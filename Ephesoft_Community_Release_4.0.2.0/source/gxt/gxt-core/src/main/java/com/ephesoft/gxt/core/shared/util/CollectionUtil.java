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

package com.ephesoft.gxt.core.shared.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;

/**
 * Utility class for common operations on Collections.
 * 
 * <p>
 * This class provides functionality to perform common operations on collection like filter, transform, subtract, empty etc.
 * 
 * <p>
 * All the functionality can be accessed directly. Please don't try and instantiate this class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see StringUtil
 * 
 */
public final class CollectionUtil {

	// Suppresses default constructor, ensuring non-instantiability.
	private CollectionUtil() {
	}

	/**
	 * Performs some predicate which returns true or false based on the input object.
	 * <p>
	 * Predicate instances can be used to implement queries or to do filtering.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 * 
	 * @param <T> type of data
	 */
	public interface Predicate<T> {

		boolean apply(T type);
	}

	/**
	 * Perform some transformation which transform an input object to some output object.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 * 
	 * @param <K> type of data
	 * @param <V> type of data
	 */
	public interface MapMutator<K, V> {

		V transform(V value);
	}

	/**
	 * Perform some transformation which transform an input object to some output object.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 * 
	 * @param <T> type of data
	 * @param <R> type of data
	 */
	public interface ListTransformer<T, R> {

		R transform(T value);
	}

	/**
	 * Perform some transformation which transform an input object to some output object.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 * 
	 * @param <T> type of data
	 */
	public interface ListMutator<T> {

		T transform(T value);
	}

	/**
	 * Checks whether the passed collection is NULL or is empty.
	 * 
	 * @param <T> type of data
	 * @param source collection to be checked for emptiness.
	 * @return true if passed collection is empty otherwise false.
	 */
	public static <T> boolean isEmpty(final Collection<T> source) {
		boolean isEmpty = false;
		isEmpty = (null == source || source.isEmpty());
		return isEmpty;
	}

	/**
	 * Filters the passed collection using the predicate logic passed.
	 * 
	 * <p>
	 * It will remove the unwanted object from the passed collection. It should be used when {@link List} is expected to be the output.
	 * 
	 * @param <T> type of data.
	 * @param source Collection to be filtered. NULL is not a expected input.
	 * @param predicate Predicate logic on which filtering has to be applied. NULL is not a expected input.
	 * @return {@link List} with filtered data.
	 */
	public static <T> List<T> filter(final Collection<? extends T> source, final Predicate<? super T> predicate) {
		final Collection<? extends T> sourceTemp = Collections.unmodifiableCollection(source);
		final List<T> result = new ArrayList<T>(sourceTemp.size());
		for (final T element : sourceTemp) {
			if (predicate.apply(element)) {
				result.add(element);
			}
		}
		return result;
	}

	/**
	 * Filters the passed map using the predicate logic passed.
	 * 
	 * <p>
	 * It will remove the unwanted object from the passed collection. It should be used when {@link Map} is expected to be the output.
	 * 
	 * @param <K> type of data.
	 * @param <V> type of data.
	 * @param target Map to be filtered. NULL is not a expected input.
	 * @param predicate Predicate logic on which filtering has to be applied. NULL is not a expected input.
	 * @return {@link Map} with filtered data
	 */
	public static <K, V> Map<K, V> filter(final Map<K, V> target, final Predicate<Map.Entry<K, V>> predicate) {
		final Map<K, V> targetMap = Collections.unmodifiableMap(target);
		final Map<K, V> result = new HashMap<K, V>(target.size());
		for (final Map.Entry<K, V> entry : targetMap.entrySet()) {
			if (predicate.apply(entry)) {
				result.put(entry.getKey(), targetMap.get(entry.getKey()));
			}
		}
		return result;
	}

	/**
	 * Filters the passed List using the predicate logic passed.
	 * 
	 * <p>
	 * It will modify the list passed directly.
	 * 
	 * @param <T> type of data
	 * @param target List to be filtered.
	 * @param predicate Predicate logic on which filtering has to be applied. NULL is not a expected input.
	 */
	public static <T> void inlineFilter(final List<T> target, final Predicate<T> predicate) {
		final List<T> valid = new ArrayList<T>();
		for (final T key : target) {
			if (predicate.apply(key)) {
				valid.add(key);
			}
		}
		target.retainAll(valid);
	}

	/**
	 * Transforms the Map values according to the mutator passed.
	 * 
	 * <p>
	 * It transforms the value in the passed map directly.
	 * 
	 * @param <K> type of data.
	 * @param <V> type of data.
	 * @param target Map whose values needs to be transformed. NULL is not a expected input.
	 * @param mutator Mutation logic on which transformation needs to take place.
	 */
	public static <K, V> void inlineTransform(final Map<K, V> target, final MapMutator<K, V> mutator) {
		target.clear();
		target.putAll(transform(target, mutator));
	}

	/**
	 * Transforms the Map values according to the mutator passed.
	 * 
	 * @param <K> type of data.
	 * @param <V> type of data.
	 * @param target Map whose values needs to be transformed. NULL is not a expected input.
	 * @param mutator Mutation logic on which transformation needs to take place.
	 * @return {@link Map} with transformed values.
	 */
	public static <K, V> Map<K, V> transform(final Map<K, V> target, final MapMutator<K, V> mutator) {
		final Map<K, V> targetMap = Collections.unmodifiableMap(target);

		final Map<K, V> transformed = new HashMap<K, V>(targetMap.size());
		V value = null;
		for (final K key : targetMap.keySet()) {
			value = targetMap.get(key);
			transformed.put(key, mutator.transform(value));
		}
		return transformed;
	}

	/**
	 * Transforms the List values according to the mutator passed.
	 * 
	 * <p>
	 * (note ListMutator generic types must be the same -- mutated list must be type-compatible with target for inline transform )
	 * 
	 * @param <T> type of data.
	 * @param target List whose values needs to be transformed. NULL is not a expected input.
	 * @param mutator Mutation logic on which transformation needs to take place.
	 */
	public static <T> void inlineTransform(final List<T> target, final ListMutator<T> mutator) {
		target.clear();
		target.addAll(transform(target, mutator));
	}

	/**
	 * Transforms the List values according to the mutator passed.
	 * 
	 * @param <T> type of data.
	 * @param <R> type of data.
	 * @param target List whose values needs to be transformed. NULL is not a expected input.
	 * @param mutator Mutation logic on which transformation needs to take place.
	 * @return {@link List} with transformed data
	 */
	public static <T, R> List<R> transform(final Collection<? extends T> target, final ListTransformer<T, R> mutator) {
		final Collection<? extends T> targetTemp = Collections.unmodifiableCollection(target);

		final List<R> transformed = new ArrayList<R>(targetTemp.size());
		R mutated = null;
		for (final T item : targetTemp) {
			mutated = mutator.transform(item);
			transformed.add(mutated);
		}
		return transformed;
	}

	/**
	 * Transforms the collection values according to the mutator passed.
	 * 
	 * @param <T> type of data.
	 * @param <R> type of data.
	 * @param target Collection whose values needs to be transformed. NULL is not a expected input.
	 * @param mutator Mutation logic on which transformation needs to take place.
	 * @return {@link List} with transformed data
	 */
	public static <T> List<T> transform(final Collection<T> target, final ListMutator<T> mutator) {
		final Collection<T> targetTemp = Collections.unmodifiableCollection(target);

		final List<T> transformed = new ArrayList<T>(targetTemp.size());
		for (final T item : targetTemp) {
			transformed.add(mutator.transform(item));
		}
		return transformed;
	}

	/**
	 * Subtracts a set from other set. Second Set passed will be removed from the first set.
	 * 
	 * @param <K> type of data.
	 * @param lhs Set from which subtraction is to be done.
	 * @param rhs Set which is subtracted from the other Set.
	 * @return {@link Set} with subtracted data.
	 */
	public static <K> Set<K> subtract(final Set<K> lhs, final Set<K> rhs) {
		final Set<K> lhsSet = Collections.unmodifiableSet(lhs);
		final Set<K> rhsSet = Collections.unmodifiableSet(rhs);

		final Set<K> difference = new HashSet<K>(lhsSet);
		difference.removeAll(rhsSet);
		return difference;
	}

	/**
	 * Subtracts a Map from other Map based on key values. Second Map passed will be removed from the first Map.
	 * 
	 * @param <K> type of data.
	 * @param <V> type of data.
	 * @param lhs Map from which subtraction is to be done.
	 * @param rhs Map which is subtracted from the other Set.
	 * @return {@link Map} with subtracted data.
	 */
	public static <K, V> Map<K, V> subtract(final Map<K, V> lhs, final Map<K, V> rhs) {
		final Map<K, V> lhsMap = Collections.unmodifiableMap(lhs);
		final Map<K, V> rhsMap = Collections.unmodifiableMap(rhs);

		final Map<K, V> difference = new HashMap<K, V>(lhsMap);
		difference.keySet().removeAll(rhsMap.keySet());
		return difference;
	}

	/**
	 * Cleans a {@link List} of String type by removing the empty string.
	 * 
	 * @param list list to be cleaned. NULL not expected as a input.
	 * @return {@link List} cleaned.
	 */
	public static List<String> clean(final List<String> list) {
		return filter(list, isStringNonBlank);
	}

	/**
	 * Cleans a {@link List} by removing the NULL values.
	 * 
	 * @param <T> type of data.
	 * @param list list to be cleaned. NULL not expected as a input.
	 * @return {@link List} cleaned.
	 */
	public static <T> List<T> cleanNull(final List<T> list) {
		return filter(list, isNonNull);
	}

	/**
	 * Predicate logic for blank string.
	 */
	public static CollectionUtil.Predicate<String> isStringBlank = new CollectionUtil.Predicate<String>() {

		public boolean apply(final String item) {
			return StringUtil.isNullOrEmpty(item);
		}
	};

	/**
	 * Predicate logic for non-blank string.
	 */
	public static CollectionUtil.Predicate<String> isStringNonBlank = new CollectionUtil.Predicate<String>() {

		public boolean apply(final String item) {
			return !StringUtil.isNullOrEmpty(item);
		}
	};

	/**
	 * Predicate logic for not NULL type.
	 */
	public static CollectionUtil.Predicate<Object> isNonNull = new CollectionUtil.Predicate<Object>() {

		public boolean apply(final Object item) {
			return null != item;
		}
	};

	/**
	 * Predicate logic for NULL type.
	 */
	public static CollectionUtil.Predicate<Object> isNull = new CollectionUtil.Predicate<Object>() {

		public boolean apply(final Object item) {
			return null == item;
		}
	};

	/**
	 * Transforms the {@link Collection} to the {@link ListStore}.
	 * 
	 * @param collectionToTransform {@link Collection} that needs to be transformed to the corresponding {@link ListStore}
	 *            representation.
	 * @param keyProvider {@link ModelKeyProvider} Key parameter that will identify an entity uniquely.
	 * @return {@link ListStore} which is transformed from the Collection.
	 */
	public static <T> ListStore<T> createListStore(final Collection<T> collectionToTransform, final ModelKeyProvider<T> keyProvider) {
		ListStore<T> transformedList = null;
		if (keyProvider != null) {
			transformedList = new ListStore<T>(keyProvider);
			if (!isEmpty(collectionToTransform)) {
				transformedList.addAll(collectionToTransform);
			}
		}
		return transformedList;
	}

	/**
	 * Transforms the {@link List} to the {@link ListStore}.
	 * 
	 * @param listToTransform {@link List} that needs to be transformed to the corresponding {@link ListStore} representation.
	 * @param keyProvider {@link ModelKeyProvider} Key parameter that will identify an entity uniquely.
	 * @return {@link ListStore} which is transformed from the Collection.
	 */
	public static <T> TreeStore<T> createTreeStore(final List<T> listToTransform, final ModelKeyProvider<T> keyProvider) {
		TreeStore<T> transformedList = null;
		if (keyProvider != null) {
			transformedList = new TreeStore<T>(keyProvider);
			if (!isEmpty(listToTransform)) {
				transformedList.add(listToTransform);
			}
		}
		return transformedList;
	}

	/**
	 * Transforms the {@link Collection} to the {@link ListStore}.
	 * 
	 * @param collectionToTransform {@link Collection} that needs to be transformed to the corresponding {@link ListStore}
	 *            representation.
	 * @param keyProvider {@link ModelKeyProvider} Key parameter that will identify an entity uniquely.
	 * @return {@link ListStore} which is transformed from the Collection.
	 */
	public static <T> ListStore<T> createListStore(final ModelKeyProvider<T> keyProvider) {
		ListStore<T> transformedList = null;
		if (keyProvider != null) {
			transformedList = new ListStore<T>(keyProvider);
		}
		return transformedList;
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty()) ? true : false;
	}
}
