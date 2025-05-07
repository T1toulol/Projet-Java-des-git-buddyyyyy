package io.github.oliviercailloux.j_voting.preferences.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;

import io.github.oliviercailloux.j_voting.Alternative;
import static io.github.oliviercailloux.j_voting.AlternativeHelper.a5;
import static io.github.oliviercailloux.j_voting.Generator.a1;
import static io.github.oliviercailloux.j_voting.Generator.a2;
import static io.github.oliviercailloux.j_voting.Generator.a3;
import static io.github.oliviercailloux.j_voting.Generator.a4;
import io.github.oliviercailloux.j_voting.Voter;
import io.github.oliviercailloux.j_voting.preferences.ImmutablePreference;

class ImmutablePreferenceImplTest {

	private static Voter v1 = Voter.withId(1);

	static ImmutablePreference getApreference() {
		MutableGraph<Alternative> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
		graph.putEdge(a1, a1);
		graph.putEdge(a2, a2);
		graph.putEdge(a3, a3);
		graph.putEdge(a4, a4);
		graph.putEdge(a5, a5);
		graph.putEdge(a1, a4);
		graph.putEdge(a3, a2);
		graph.putEdge(a4, a1);
		graph.putEdge(a4, a3);
		graph.putEdge(a5, a3);
		return ImmutablePreferenceImpl.asImmutablePreference(v1, graph);
	}

	@Test
	void getAlternativesImmutablePreferenceTest() {
		ImmutablePreference toTestImmutablePreference = getApreference();
		assertEquals(ImmutableSet.of(a1, a2, a3, a4, a5), toTestImmutablePreference.getAlternatives());
	}

	@Test
	void getVoterImmutablePreferenceTest() {
		ImmutablePreference toTestImmutablePreference = getApreference();
		assertEquals(v1, toTestImmutablePreference.getVoter());
	}

	@Test
	void asIntransitiveGraph() {
		// Cast will be changed ASAP, need to add a new function in Preference
		// Interface
		ImmutablePreferenceImpl toTest = (ImmutablePreferenceImpl) getApreference();
		ImmutableGraph<Alternative> toTestImmutableGraph = toTest.asIntransitiveGraph();
		assertTrue(toTestImmutableGraph.hasEdgeConnecting(a1, a4));
		assertTrue(toTestImmutableGraph.hasEdgeConnecting(a4, a1));
		assertTrue(toTestImmutableGraph.hasEdgeConnecting(a4, a3));
		assertTrue(toTestImmutableGraph.hasEdgeConnecting(a5, a3));
		assertTrue(toTestImmutableGraph.hasEdgeConnecting(a3, a2));
		assertFalse(toTestImmutableGraph.hasEdgeConnecting(a1, a5));
		assertTrue(toTestImmutableGraph.successors(a2).size() == 1);
		assertTrue(toTestImmutableGraph.successors(a1).size() == 2);
		assertTrue(toTestImmutableGraph.successors(a4).size() == 3);
		assertTrue(toTestImmutableGraph.successors(a5).size() == 2);
		assertTrue(toTestImmutableGraph.successors(a3).size() == 2);
	}

	@Test
	void asGraph_shouldMatchExpectedGraphExactly() {
		MutableGraph<Alternative> expected = GraphBuilder.directed().allowsSelfLoops(true).build();
		expected.putEdge(a1, a1);
		expected.putEdge(a2, a2);
		expected.putEdge(a3, a3);
		expected.putEdge(a4, a4);
		expected.putEdge(a5, a5);
		expected.putEdge(a1, a4);
		expected.putEdge(a3, a2);
		expected.putEdge(a4, a1);
		expected.putEdge(a4, a3);
		expected.putEdge(a5, a3);

		// ajout automatique par fermeture transitive
		expected.putEdge(a1, a1);
		expected.putEdge(a1, a3);
		expected.putEdge(a1, a2);
		expected.putEdge(a3, a3);
		expected.putEdge(a4, a2);
		expected.putEdge(a4, a2);

		ImmutableGraph<Alternative> actual = getApreference().asGraph();
		assertEquals(ImmutableGraph.copyOf(expected), actual);
}
}
