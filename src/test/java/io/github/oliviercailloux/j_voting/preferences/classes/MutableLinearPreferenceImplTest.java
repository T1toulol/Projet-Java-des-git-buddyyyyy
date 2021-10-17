package io.github.oliviercailloux.j_voting.preferences.classes;

import static io.github.oliviercailloux.j_voting.Generator.a1;
import static io.github.oliviercailloux.j_voting.Generator.a2;
import static io.github.oliviercailloux.j_voting.Generator.a3;
import static io.github.oliviercailloux.j_voting.Generator.a4;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a123;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a1234;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a12345;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a123456list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a12345list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a1234list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a123546list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a1235list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a123list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a1256list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a12list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a1345list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a14235list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a14253list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a21list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a32451list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a32541list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a34521list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a345list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a34list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a41235list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a41253list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a41325list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a41523list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a43251list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a46list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a5;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a51324list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a52341list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a54321list;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a56;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a6;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import io.github.oliviercailloux.j_voting.Alternative;
import io.github.oliviercailloux.j_voting.Voter;
import io.github.oliviercailloux.j_voting.preferences.MutableLinearPreference;

public class MutableLinearPreferenceImplTest {

	/**
	 * Tests whether the preference is correctly expressed as a graph
	 */
	@Test
	void testAsGraph() {
		Voter v = Voter.withId(1);
		MutableLinearPreference toTestPref = MutableLinearPreferenceImpl.given(v, a12345list);

		MutableGraph<Alternative> expected = GraphBuilder.directed().allowsSelfLoops(true).build();
		expected.putEdge(a1, a1);
		expected.putEdge(a1, a2);
		expected.putEdge(a1, a3);
		expected.putEdge(a1, a4);
		expected.putEdge(a1, a5);
		expected.putEdge(a2, a2);
		expected.putEdge(a2, a3);
		expected.putEdge(a2, a4);
		expected.putEdge(a2, a5);
		expected.putEdge(a3, a3);
		expected.putEdge(a3, a4);
		expected.putEdge(a3, a5);
		expected.putEdge(a4, a4);
		expected.putEdge(a4, a5);
		expected.putEdge(a5, a5);
		assertEquals(expected, toTestPref.asGraph());
	}

	/**
	 * Tests whether method getAlternatives returns a set with all the alternatives
	 * of the preference. Here we are going to test the decorator.
	 */
	@Test
	void testGetAlternatives() {
		Voter v = Voter.withId(1);
		MutableLinearPreference toTestPref = MutableLinearPreferenceImpl.given(v, a1234list);

		Set<Alternative> expected = a1234;
		assertEquals(expected, toTestPref.getAlternatives());

		MutableLinearPreference prefExpected1 = MutableLinearPreferenceImpl.given(v, a12345list);
		toTestPref.getAlternatives().add(a5);
		assertEquals(prefExpected1, toTestPref);

		MutableLinearPreference prefExpected2 = MutableLinearPreferenceImpl.given(v, a1235list);
		toTestPref.getAlternatives().remove(a4);
		assertEquals(prefExpected2, toTestPref);

		MutableLinearPreference prefExpected3 = MutableLinearPreferenceImpl.given(v, a123546list);
		toTestPref.getAlternatives().addAll(a46list);
		assertEquals(prefExpected3, toTestPref);

		MutableLinearPreference prefExpected4 = MutableLinearPreferenceImpl.given(v, a1256list);
		toTestPref.getAlternatives().removeAll(a34list);
		assertEquals(prefExpected4, toTestPref);

		List<Alternative> emptyList = new ArrayList<>();
		MutableLinearPreference prefExpected5 = MutableLinearPreferenceImpl.given(v, emptyList);
		toTestPref.getAlternatives().clear();
		assertEquals(prefExpected5, toTestPref);
	}

	/**
	 * Tests whether the single alternative is added to the preferences
	 */
	@Test
	void testAddAlternative() {
		Voter v = Voter.withId(1);
		MutableLinearPreference toTestPref = MutableLinearPreferenceImpl.given(v, a1234list);
		assertThrows(Exception.class, () -> toTestPref.addAlternative(a4));
		
		List<Alternative> toTestList = new ArrayList<>();
		toTestList.addAll(a12345);
		MutableLinearPreference prefExpected1 = MutableLinearPreferenceImpl.given(v, toTestList);
		toTestPref.addAlternative(a5);
		assertEquals(prefExpected1, toTestPref);
		assertThrows(Exception.class, () -> toTestPref.addAlternative(a5));

		/* Test if modifying the list after passing it to the constructor does not
		change the structure of the preference.*/
		toTestList.add(a6);
		assertEquals(prefExpected1, toTestPref);
	}

	@Test
	void testAddAllDelegate() {
		Voter v = Voter.withId(1);
		MutableLinearPreference toTestPref = MutableLinearPreferenceImpl.given(v, a1234list);
		toTestPref.getAlternatives().addAll(a56);

		MutableLinearPreference prefExpected1 = MutableLinearPreferenceImpl.given(v, a123456list);
		assertEquals(prefExpected1, toTestPref);
		assertEquals(prefExpected1.asGraph(), toTestPref.asGraph());
	}

	/**
	 * Tests whether the single alternative is removed to the preferences
	 */
	@Test
	void testRemoveAlternative() {
		Voter v = Voter.withId(1);
		MutableLinearPreference toTestPref = MutableLinearPreferenceImpl.given(v, a12345list);
		
		MutableLinearPreference prefExpected1 = MutableLinearPreferenceImpl.given(v, a1345list);
		toTestPref.removeAlternative(a2);
		assertEquals(prefExpected1, toTestPref);
		assertThrows(Exception.class, () -> toTestPref.removeAlternative(a2));
	}

	@Test
	void testRemoveAllDelegate() {
		Voter v = Voter.withId(1);
		MutableLinearPreference toTestPref = MutableLinearPreferenceImpl.given(v, a123456list);

		Set<Alternative> c = new HashSet<>();
		c.addAll(a56);
		toTestPref.getAlternatives().removeAll(c);

		MutableLinearPreference prefExpected1 = MutableLinearPreferenceImpl.given(v, a1234list);
		assertEquals(prefExpected1, toTestPref);
	}

	@Test
	void testRetainAllDelegate() {
		Voter v = Voter.withId(1);
		MutableLinearPreference toTestPref = MutableLinearPreferenceImpl.given(v, a123456list);

		Set<Alternative> c = new HashSet<>();
		c.addAll(a123);
		toTestPref.getAlternatives().retainAll(c);

		MutableLinearPreference prefExpected1 = MutableLinearPreferenceImpl.given(v, a123list);
		assertEquals(prefExpected1, toTestPref);
	}

	@Test
	void testChangeOrder() {
		Voter v = Voter.withId(1);
		MutableLinearPreference toTestPref = MutableLinearPreferenceImpl.given(v, a12345list);

		MutableLinearPreference prefExpected1 = MutableLinearPreferenceImpl.given(v, a41235list);
		toTestPref.changeOrder(a4, 1);
		assertEquals(prefExpected1, toTestPref);

		MutableLinearPreference prefExpected2 = MutableLinearPreferenceImpl.given(v, a41325list);
		toTestPref.changeOrder(a2, 4);
		assertEquals(prefExpected2, toTestPref);

		MutableLinearPreference prefExpected3 = MutableLinearPreferenceImpl.given(v, a43251list);
		toTestPref.changeOrder(a1, 5);
		assertEquals(prefExpected3, toTestPref);
	}

	/**
	 * Test the 7 cases of the swap method <br/>
	 * a1 -> a2 -> a3 -> a4 -> a5 <br/>
	 * head -> middle -> middle -> middle -> end <br/>
	 * swap(head,end), swap(head,middle), swap(middle,middle), swap(middle,head),
	 * swap(middle,end), swap(end,head), swap(end,middle) <br/>
	 * We have to test 3 additional cases about the neighbours alternatives<br/>
	 * And a final test if we try to swap an alternative who's not in the list.
	 */
	@Test
	void testSwap() {
		Voter v = Voter.withId(1);
		MutableLinearPreference toTestPref = MutableLinearPreferenceImpl.given(v, a12345list);

		MutableLinearPreference prefExpected1 = MutableLinearPreferenceImpl.given(v, a52341list);
		/* swap(head,end) */
		toTestPref.swap(a1, a5); 
		assertEquals(prefExpected1, toTestPref);

		MutableLinearPreference prefExpected2 = MutableLinearPreferenceImpl.given(v, a32541list);
		/* swap(head,middle) */
		toTestPref.swap(a5, a3); 
		assertEquals(prefExpected2, toTestPref);

		MutableLinearPreference prefExpected3 = MutableLinearPreferenceImpl.given(v, a34521list);
		/* swap(middle,middle) */
		toTestPref.swap(a2, a4); 
		assertEquals(prefExpected3, toTestPref);

		MutableLinearPreference prefExpected4 = MutableLinearPreferenceImpl.given(v, a54321list);
		/* swap(middle, head) */
		toTestPref.swap(a5, a3); 
		assertEquals(prefExpected4, toTestPref);

		MutableLinearPreference prefExpected5 = MutableLinearPreferenceImpl.given(v, a51324list);
		/* swap(middle,end) */
		toTestPref.swap(a4, a1); 
		assertEquals(prefExpected5, toTestPref);

		MutableLinearPreference prefExpected6 = MutableLinearPreferenceImpl.given(v, a41325list);
		/* swap(end,head) */
		toTestPref.swap(a4, a5); 
		assertEquals(prefExpected6, toTestPref);

		MutableLinearPreference prefExpected7 = MutableLinearPreferenceImpl.given(v, a41523list);
		/* swap(end,middle) */
		toTestPref.swap(a5, a3); 
		assertEquals(prefExpected7, toTestPref);

		MutableLinearPreference prefExpected8 = MutableLinearPreferenceImpl.given(v, a41253list);
		/* swap(middle,middle) neighbor */
		toTestPref.swap(a5, a2); 
		assertEquals(prefExpected8, toTestPref);

		MutableLinearPreference prefExpected9 = MutableLinearPreferenceImpl.given(v, a14253list);
		/* swap(head,middle) neighbor */
		toTestPref.swap(a4, a1); 
		assertEquals(prefExpected9, toTestPref);

		MutableLinearPreference prefExpected10 = MutableLinearPreferenceImpl.given(v, a14235list);
		/* swap(middle,end) neighbor */
		toTestPref.swap(a5, a3); 
		assertEquals(prefExpected10, toTestPref);

		MutableLinearPreference toTestPref1 = MutableLinearPreferenceImpl.given(v, a12list);
		MutableLinearPreference prefExpected11 = MutableLinearPreferenceImpl.given(v, a21list);
		/* swap(head,end) neighbor */
		toTestPref1.swap(a1, a2); 
		assertEquals(prefExpected11, toTestPref1);
		assertThrows(Exception.class, () -> toTestPref1.swap(a1, a3));
		

		Graph<Alternative> expected = toTestPref.asGraph();
		toTestPref.addAlternative(a6);
		toTestPref.swap(a3, a4);
		toTestPref.removeAlternative(a1);
		assertEquals(expected, toTestPref.asGraph());
		assertThrows(Exception.class, () -> toTestPref1.swap(a1, a3));

	}
}
