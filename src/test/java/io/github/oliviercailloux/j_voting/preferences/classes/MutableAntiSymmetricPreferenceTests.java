package io.github.oliviercailloux.j_voting.preferences.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;

import io.github.oliviercailloux.j_voting.Alternative;
import static io.github.oliviercailloux.j_voting.Generator.a1;
import static io.github.oliviercailloux.j_voting.Generator.a2;
import static io.github.oliviercailloux.j_voting.Generator.a3;
import static io.github.oliviercailloux.j_voting.Generator.a4;
import io.github.oliviercailloux.j_voting.preferences.MutableAntiSymmetricPreference;

class MutableAntiSymmetricPreferenceTests {

	@Test
	void testLoop() {
		final MutableGraph<Alternative> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
		graph.putEdge(a1, a1);
		graph.putEdge(a2, a3);
		graph.putEdge(a3, a4);
		graph.putEdge(a4, a1);
		assertThrows(IllegalArgumentException.class, () -> MutableAntiSymmetricPreferenceImpl.given(graph));
	}

	@Test
	void testCycle() {
		final MutableGraph<Alternative> start = GraphBuilder.directed().build();
		start.putEdge(a1, a2);
		start.putEdge(a2, a3);
		start.putEdge(a3, a1);
		final MutableAntiSymmetricPreference pref = MutableAntiSymmetricPreferenceImpl.given(start);

		assertThrows(IllegalStateException.class, () -> pref.asGraph());
	}

	@Test
	
	void testJustClose() {
		final MutableGraph<Alternative> start = GraphBuilder.directed().build();
		start.putEdge(a1, a2);
		start.putEdge(a2, a3);
		final MutableAntiSymmetricPreference pref = MutableAntiSymmetricPreferenceImpl.given(start);

		final Graph<Alternative> expected = Graphs.transitiveClosure(start);
		assertEquals(expected, pref.asGraph());
	}


	@Test
	void testAddAlternative() {
		final MutableGraph<Alternative> start = GraphBuilder.directed().build();
		start.putEdge(a1, a2);
		final MutableAntiSymmetricPreference pref = MutableAntiSymmetricPreferenceImpl.given(start);

		pref.addAlternative(a3);

		final MutableGraph<Alternative> expected = GraphBuilder.directed().build();
		expected.putEdge(a1, a2);
		expected.addNode(a3);
		assertEquals(expected, pref.asMutableGraph());

		final Graph<Alternative> expectedTrans = Graphs.transitiveClosure(expected);
		assertEquals(expectedTrans, pref.asGraph());
	}

	@Test
	void testAddAlternativeIndirect() {
		final MutableGraph<Alternative> start = GraphBuilder.directed().build();
		start.putEdge(a1, a2);
		final MutableAntiSymmetricPreference pref = MutableAntiSymmetricPreferenceImpl.given(start);

		pref.asMutableGraph().addNode(a3);

		final MutableGraph<Alternative> expected = GraphBuilder.directed().build();
		expected.putEdge(a1, a2);
		expected.addNode(a3);
		assertEquals(expected, pref.asMutableGraph());

		final Graph<Alternative> expectedTrans = Graphs.transitiveClosure(expected);
		assertEquals(expectedTrans, pref.asGraph());
	}

	@Test
	void testAddAlternativeTransitiveFirst() {
		final MutableGraph<Alternative> start = GraphBuilder.directed().build();
		start.putEdge(a1, a2);
		final MutableAntiSymmetricPreference pref = MutableAntiSymmetricPreferenceImpl.given(start);

		pref.asGraph();
		pref.asMutableGraph().addNode(a3);

		final MutableGraph<Alternative> expected = GraphBuilder.directed().build();
		expected.putEdge(a1, a2);
		expected.addNode(a3);
		assertEquals(expected, pref.asMutableGraph());

		final Graph<Alternative> expectedTrans = Graphs.transitiveClosure(expected);
		assertEquals(expectedTrans, pref.asGraph());
	}

	@Test
	void testRemoveAlternative() {
		final MutableGraph<Alternative> start = GraphBuilder.directed().build();
		start.putEdge(a1, a2);
		start.putEdge(a2, a3);
		final MutableAntiSymmetricPreference pref = MutableAntiSymmetricPreferenceImpl.given(start);

		pref.asMutableGraph().removeNode(a2);

		final MutableGraph<Alternative> expected = GraphBuilder.directed().build();
		expected.addNode(a1);
		expected.addNode(a3);
		assertEquals(expected, pref.asMutableGraph());

		final Graph<Alternative> expectedTrans = Graphs.transitiveClosure(expected);
		assertEquals(expectedTrans, pref.asGraph());
	}

	@Test
	void testRemoveAlternativeTransitiveFirst() {
		final MutableGraph<Alternative> start = GraphBuilder.directed().build();
		start.putEdge(a1, a2);
		start.putEdge(a2, a3);
		final MutableAntiSymmetricPreference pref = MutableAntiSymmetricPreferenceImpl.given(start);

		pref.asGraph();
		pref.asMutableGraph().removeNode(a2);

		final MutableGraph<Alternative> expected = GraphBuilder.directed().build();
		expected.addNode(a1);
		expected.addNode(a3);
		assertEquals(expected, pref.asMutableGraph());

		final Graph<Alternative> expectedTrans = Graphs.transitiveClosure(expected);
		assertEquals(expectedTrans, pref.asGraph());
	}

	@Test
	void testPutEdge() {
		final MutableGraph<Alternative> start = GraphBuilder.directed().build();
		start.putEdge(a1, a2);
		start.putEdge(a3, a4);
		final MutableAntiSymmetricPreference pref = MutableAntiSymmetricPreferenceImpl.given(start);

		pref.addStrictPreference(a2, a3);

		final MutableGraph<Alternative> expected = GraphBuilder.directed().build();
		expected.putEdge(a1, a2);
		expected.putEdge(a2, a3);
		expected.putEdge(a3, a4);
		assertEquals(expected, pref.asMutableGraph());

		final Graph<Alternative> expectedTrans = Graphs.transitiveClosure(expected);
		assertEquals(expectedTrans, pref.asGraph());
	}

	@Test
	void testPutEdgeIndirect() {
		final MutableGraph<Alternative> start = GraphBuilder.directed().build();
		start.putEdge(a1, a2);
		start.putEdge(a3, a4);
		final MutableAntiSymmetricPreference pref = MutableAntiSymmetricPreferenceImpl.given(start);

		pref.asMutableGraph().putEdge(a2, a3);

		final MutableGraph<Alternative> expected = GraphBuilder.directed().build();
		expected.putEdge(a1, a2);
		expected.putEdge(a2, a3);
		expected.putEdge(a3, a4);
		assertEquals(expected, pref.asMutableGraph());

		final Graph<Alternative> expectedTrans = Graphs.transitiveClosure(expected);
		assertEquals(expectedTrans, pref.asGraph());
	}

	@Test
	void testPutEdgeTransitiveFirst() {
		final MutableGraph<Alternative> start = GraphBuilder.directed().build();
		start.putEdge(a1, a2);
		start.putEdge(a3, a4);
		final MutableAntiSymmetricPreference pref = MutableAntiSymmetricPreferenceImpl.given(start);

		pref.asGraph();
		pref.asMutableGraph().putEdge(a2, a3);

		final MutableGraph<Alternative> expected = GraphBuilder.directed().build();
		expected.putEdge(a1, a2);
		expected.putEdge(a2, a3);
		expected.putEdge(a3, a4);
		assertEquals(expected, pref.asMutableGraph());

		final Graph<Alternative> expectedTrans = Graphs.transitiveClosure(expected);
		assertEquals(expectedTrans, pref.asGraph());
	}

	@Test
	void testPutEdgeReflexive() {
		final MutableAntiSymmetricPreference pref = MutableAntiSymmetricPreferenceImpl.empty();

		pref.asGraph();
		assertThrows(IllegalArgumentException.class, () -> pref.asMutableGraph().putEdge(a1, a1));
	}

	@Test
	void testPutEdgeCycle() {
		final MutableGraph<Alternative> start = GraphBuilder.directed().build();
		start.putEdge(a1, a2);
		start.putEdge(a2, a3);
		final MutableAntiSymmetricPreference pref = MutableAntiSymmetricPreferenceImpl.given(start);

		pref.asGraph();
		assertThrows(IllegalArgumentException.class, () -> pref.asMutableGraph().putEdge(a3, a1));
	}
}
