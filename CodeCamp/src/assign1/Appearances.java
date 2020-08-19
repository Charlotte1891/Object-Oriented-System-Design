package assign1;

import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		// special case when there is null
		if(a == null | b == null) return 0;
		int count = 0;
		// use HashMap to map elements with corresponding appearances
		HashMap<T, Integer> aHashMap = new HashMap<T, Integer>();
		HashMap<T, Integer> bHashMap = new HashMap<T, Integer>();
		for(T element : a) {
			if(aHashMap.containsKey(element)) {
				aHashMap.put(element, aHashMap.get(element) + 1);
			}else {
				aHashMap.put(element, 1);
			}
		}
		for(T element : b) {
			if(bHashMap.containsKey(element)) {
				bHashMap.put(element, bHashMap.get(element) + 1);
			}else {
				bHashMap.put(element, 1);
			}
		}
		for(T element : aHashMap.keySet()) {
			if(aHashMap.get(element) == bHashMap.get(element)) {
				count++;
			}
		}
		return count;
	}
	
}
