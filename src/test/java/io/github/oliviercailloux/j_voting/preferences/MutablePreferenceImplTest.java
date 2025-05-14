package io.github.oliviercailloux.j_voting.preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;

import io.github.oliviercailloux.j_voting.Alternative;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a12345;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a5;
import static io.github.oliviercailloux.j_voting.Generator.a1;
import static io.github.oliviercailloux.j_voting.Generator.a2;
import static io.github.oliviercailloux.j_voting.Generator.a3;
import static io.github.oliviercailloux.j_voting.Generator.a4;
import io.github.oliviercailloux.j_voting.Voter;
import io.github.oliviercailloux.j_voting.preferences.classes.MutablePreferenceImpl;

class MutablePreferenceImplTest {

	/**
	 * Tests whether the given method with the voter only returns a preference with
	 * an empty graph
	 */
	@Test
	void testGivenVoter() {
		MutablePreferenceImpl pref = MutablePreferenceImpl.given(Voter.withId(1));
		assertEquals(pref.asGraph().nodes().size(), 0);
	}

	/**
	 * Tests whether the preference is correctly copied
	 */
	@Test
	void testGivenEmptyPref() {
		MutableGraph<Alternative> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
		MutablePreference pref = MutablePreferenceImpl.given(Voter.withId(1), graph);
		MutablePreferenceImpl pref1 = MutablePreferenceImpl.given(pref);
		assertEquals(pref.getVoter(), pref1.getVoter());
		assertEquals(pref.asGraph(), pref1.asGraph());

	}

	/**
	 * Tests whether the preference is correctly expressed as a graph
	 */
	@Test
	void testAsGraph() {
		MutableGraph<Alternative> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
		graph.putEdge(a1, a2);
		graph.putEdge(a3, a4);
		graph.putEdge(a4, a5);
		graph.putEdge(a5, a3);
		MutablePreference pref = MutablePreferenceImpl.given(Voter.withId(1), Graphs.copyOf(graph));
		graph.putEdge(a1, a1);
		graph.putEdge(a2, a2);
		graph.putEdge(a3, a3);
		graph.putEdge(a4, a4);
		graph.putEdge(a5, a5);
		graph.putEdge(a3, a5);
		graph.putEdge(a4, a3);
		graph.putEdge(a5, a4);
		assertEquals(graph, pref.asGraph());
	}

	@Test
	void testAsMutableGraph() {
		MutableGraph<Alternative> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
		graph.putEdge(a1, a2);
		graph.putEdge(a3, a4);
		graph.putEdge(a4, a5);
		graph.putEdge(a5, a3);
		MutablePreference pref = MutablePreferenceImpl.given(Voter.withId(1), Graphs.copyOf(graph));
		assertEquals(graph, pref.asMutableGraph());
	}

	/**
	 * Tests whether the single alternative is added to the preferences' graph
	 */
	@Test
	void testAddAlternative() {
		MutableGraph<Alternative> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
		graph.putEdge(a1, a2);
		graph.putEdge(a2, a1);
		MutablePreference pref = MutablePreferenceImpl.given(Voter.withId(1), Graphs.copyOf(graph));
		graph.putEdge(a1, a1);
		graph.putEdge(a2, a2);
		graph.putEdge(a3, a3);
		pref.addAlternative(a3);
		assertEquals(graph, pref.asGraph());
	}

	/**
	 * Tests whether a couple of ex-aequo alternatives is added to the preferences'
	 * graph
	 */
	@Test
	void testAddEquivalence() {
		MutableGraph<Alternative> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
		graph.putEdge(a1, a2);
		MutablePreference pref = MutablePreferenceImpl.given(Voter.withId(1), Graphs.copyOf(graph));
		graph.putEdge(a1, a1);
		graph.putEdge(a2, a2);
		graph.putEdge(a3, a3);
		graph.putEdge(a4, a4);
		graph.putEdge(a3, a4);
		graph.putEdge(a4, a3);
		pref.addEquivalence(a3, a4);
		assertEquals(graph, pref.asGraph());
	}

	/**
	 * Tests whether an ordered pair of alternatives is added to the preference's
	 * graph
	 */
	@Test
	void testSetAsLeastAsGood() {
		MutableGraph<Alternative> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
		graph.putEdge(a1, a2);
		MutablePreference pref = MutablePreferenceImpl.given(Voter.withId(1), Graphs.copyOf(graph));
		graph.putEdge(a1, a1);
		graph.putEdge(a2, a2);
		graph.putEdge(a3, a3);
		graph.putEdge(a4, a4);
		graph.putEdge(a3, a4);
		pref.setAsLeastAsGood(a3, a4);
		assertEquals(graph, pref.asGraph());
	}

	/**
	 * Tests whether method getAlternatives returns a set with all the alternatives
	 * of the preference
	 */
	@Test
	void testGetAlternatives() {
		MutableGraph<Alternative> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
		graph.putEdge(a1, a2);
		graph.putEdge(a3, a4);
		graph.putEdge(a5, a5);
		MutablePreference pref = MutablePreferenceImpl.given(Voter.withId(1), Graphs.copyOf(graph));
		ImmutableSet<Alternative> expected = a12345;
		assertEquals(expected, pref.getAlternatives());
	}
}
