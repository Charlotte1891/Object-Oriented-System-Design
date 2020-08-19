/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/
package assign1;

import java.util.*;

public class Taboo<T> {
	private HashMap<T, HashSet<T>> taboo;
	
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		taboo = new HashMap<T, HashSet<T>>();
		// find each key and corresponding value set
		for(int i = 0; i < rules.size() - 1; i++) {
			// find each pair in the rules
			T curKey = rules.get(i);
			T curValue = rules.get(i+1);
			if(curKey != null && curValue != null) {
				// if key doesn't exist, create a new HashSet to store its values
				if(!taboo.containsKey(curKey)) {
					taboo.put(curKey, new HashSet<T>()); //Set is an interface, not a class
				}
				// update the HashSet
			    taboo.get(curKey).add(curValue);
			}
		}
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		if(taboo.containsKey(elem)) {
			return taboo.get(elem);	
		}else {
			return Collections.emptySet();
		}
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		T key = list.get(0);
		for(int i = 1; i < list.size(); i++) {
			if(noFollow(key).contains(list.get(i))) {
				list.remove(i);
				i = i - 1;
			}else {
				key = list.get(i);
			}
		}
	}
}
