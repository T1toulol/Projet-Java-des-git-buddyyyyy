package io.github.oliviercailloux.j_voting.preferences;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.ImmutableGraph;

import io.github.oliviercailloux.j_voting.Alternative;

/**
 * An Immutable Preference is a preference that cannot be modified
 *
 * ImmutablePreference can’t represent the case where we know that a ≥ b and
 * ignore whether b ≥ a. This class is useful when "what we know so far" can be
 * represented using ≥ only; in contrast to needing both ≥ and > (as in: I know
 * that a > b and know that b ≥ c but ignore whether b > c). To put it
 * otherwise, what I ignore must be symmetric.
 *
 * Two immutable preferences are equal iff their graphs are equal.
 *
 */
public interface ImmutablePreference extends Preference {

	/**
	 * {@inheritDoc}
	 *
	 * This graph is immutable.
	 *
	 * @return the Graph corresponding to the Preference.
	 */
	@Override
	ImmutableGraph<Alternative> asGraph();

	/**
	 * {@inheritDoc} This set is immutable.
	 */
	@Override
	ImmutableSet<Alternative> getAlternatives();
}
