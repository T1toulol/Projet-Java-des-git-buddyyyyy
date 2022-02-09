package io.github.oliviercailloux.j_voting.preferences;

import com.google.common.graph.MutableGraph;
import io.github.oliviercailloux.j_voting.Alternative;

/**
 * <p>
 * A mutable preference keeps two graphs: the one representing the information
 * received directly from the voter, not necessarily transitive or reflexive,
 * given by {@link #asMutableGraph()}; and the one we deduce, which is at all
 * times transitively closed and reflexive, given by {@link #asGraph()}.
 * </p>
 *
 * <p>
 * To improve performance, avoid calling {@link #asGraph()} before modifying the
 * preference, as this obliges this class to maintain the transitive closure.
 * </p>
 *
 * <p>
 * The transitive closure of the graph must be interpreted with caution while it
 * is building, even when the graph is mutated only by adding edges (never by
 * removing any). When the mutable graph has an edge from a to b but not from b
 * to a, the transitive closure will seem to say that a is strictly preferred to
 * b, which we can’t deduce: this is valid when the preferential information in
 * input is complete (otherwise, perhaps b ≥ a in supplement to a ≥ b and they
 * are tied).
 * </p>
 * <p>
 * This interface does not promote any specific notion of equality. The
 * alternative would have been, either, to define a specific notion of equality
 * for a mutable preference (having the same mutable graph), or re-use the
 * natural notion of equality for a general preference, that is, having the same
 * transitive graph. The second one would have been surprising, as two mutable
 * preferences with different mutable graphs could have ended up being equal.
 * And, in any case, it is not generally considered a
 * <a href="https://stackoverflow.com/q/9089335">good idea</a> to rely on
 * equality of a mutable object.
 * </p>
 */
public interface MutablePreference extends Preference {

	/**
	 * This graph is not necessarily transitive and not necessarily reflexive.
	 *
	 */
	public MutableGraph<Alternative> asMutableGraph();

	/**
	 * Adds an alternative to the Preference. This alternative is not preferred to
	 * any other of the preference, it is being added isolated.
	 *
	 * @param alternative to add to the preference.
	 */
	public void addAlternative(Alternative alternative);

	/**
	 * Adds an edge from an alternative a1 to an alternative a2 and from a2 to a1.
	 * If one of them is not in the graph, they are added. a1 and a2 are ex-aequo.
	 *
	 * @param a1 first alternative
	 * @param a2 second alternative
	 */
	public void addEquivalence(Alternative a1, Alternative a2);

	/**
	 * Adds an edge from an alternative a1 to an alternative a2, so that a1 is as
	 * least as good as a2 (a1 >= a2). Those that are not yet in the graph are
	 * added.
	 *
	 * @param a1 preferred alternative to a2
	 * @param a2 "lower" alternative
	 */
	public void setAsLeastAsGood(Alternative a1, Alternative a2);
}
