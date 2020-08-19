// TabooTest.java
// Taboo class tests -- nothing provided.
package assign1;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class TabooTest {
	// utility -- converts a string to a list with one
    // elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}

	@Test
	public void testNoFollow1() {
		List<String> rules = stringToList("acbdae");
		Taboo<String> taboo = new Taboo<String>(rules);
		HashSet<String> aresult = new HashSet<String>(stringToList("ce"));
		HashSet<String> bresult = new HashSet<String>(stringToList("d"));
		assertEquals(aresult, taboo.noFollow("a"));
		assertEquals(bresult, taboo.noFollow("b"));
	}
	
	@Test
	public void testNoFollow2() {
		List<String> rules = stringToList("acabdbdae");
		Taboo<String> taboo = new Taboo<String>(rules);
		HashSet<String> aresult = new HashSet<String>(stringToList("cbe"));
		HashSet<String> dresult = new HashSet<String>(stringToList("ba"));
		assertEquals(aresult, taboo.noFollow("a"));
		assertEquals(dresult, taboo.noFollow("d"));
	}
	
	
	@Test
	public void testNoFollow3() {
		List<String> rules = new ArrayList<String>(Arrays.asList("a","b",null,"c","d",null,"d","e"));
		Taboo<String> taboo = new Taboo<String>(rules);
		Set<String> bresult = Collections.emptySet();
		HashSet<String> dresult = new HashSet<String>(stringToList("e"));
		assertEquals(bresult, taboo.noFollow("b"));
		assertEquals(dresult, taboo.noFollow("d"));
	}
	
	
	@Test
	public void testReduce1() {
		List<String> rules = stringToList("acbde");
		Taboo<String> taboo = new Taboo<String>(rules);
		List<String> before = stringToList("acbcebdbe");
		List<String> after = stringToList("abcebbe");
		taboo.reduce(before);
		assertEquals(after, before);
	}
	
	
	@Test
	public void testReduce2() {
		List<String> rules = stringToList("acabdbdae");
		Taboo<String> taboo = new Taboo<String>(rules);
		List<String> before = stringToList("acecabdbdaecebdbe");
		List<String> after = stringToList("aaddecebbe");
		taboo.reduce(before);
		assertEquals(after, before);
	}
	
	
	@Test
	public void testReduce3() {
		List<String> rules = new ArrayList<String>(Arrays.asList("a","b",null,"c","d",null,"d","e"));
		Taboo<String> taboo = new Taboo<String>(rules);
		List<String> before = stringToList("abfecdcedef");
		List<String> after = stringToList("afeccedf");
		taboo.reduce(before);
		assertEquals(after, before);
	}
	
	
	@Test
	public void testReduce4() {
		List<String> rules = stringToList("abadae");
		Taboo<String> taboo = new Taboo<String>(rules);
		List<String> before = stringToList("abadae");
		List<String> after = stringToList("aaa");
		taboo.reduce(before);
		assertEquals(after, before);
	}
	
	
	@Test
	public void testReduce5() {
		List<String> rules = stringToList("abcde f");
		Taboo<String> taboo = new Taboo<String>(rules);
		List<String> before = stringToList("abcde ec f");
		List<String> after = stringToList("aceec ");
		taboo.reduce(before);
		assertEquals(after, before);
	}
}
