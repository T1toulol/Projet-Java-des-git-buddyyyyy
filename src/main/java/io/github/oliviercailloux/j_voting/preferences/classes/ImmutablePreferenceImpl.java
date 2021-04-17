package io.github.oliviercailloux.j_voting.preferences.classes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Graph;
import com.google.common.graph.Graphs;
import com.google.common.graph.ImmutableGraph;

import io.github.oliviercailloux.j_voting.Alternative;
import io.github.oliviercailloux.j_voting.Voter;
import io.github.oliviercailloux.j_voting.preferences.ImmutablePreference;
import io.github.oliviercailloux.j_voting.preferences.Preference;

public class ImmutablePreferenceImpl implements ImmutablePreference {

	private ImmutableGraph<Alternative> graph;
	private ImmutableGraph<Alternative> graphIntransitivelyClosed;
	private ImmutableSet<Alternative> alternatives;
	private Voter voter;
	private static final Logger LOGGER = LoggerFactory.getLogger(ImmutablePreferenceImpl.class.getName());
	private final int cacheHashCode;

	/**
	 * 
	 * @param voter <code> not null </code>
	 * @param graph <code> not null </code> graph with ordered Alternatives
	 * @return new ImmutablePreference
	 */
	public static ImmutablePreference asImmutablePreference(Voter voter, Graph<Alternative> graph) {
		Preconditions.checkNotNull(voter);
		Preconditions.checkNotNull(graph);
		return new ImmutablePreferenceImpl(voter, graph);
	}

	/**
	 * Transform Preference to ImmutablePreference
	 * 
	 * @param preference <code> not null </code>
	 * @return ImmutablePreference
	 */
	public static ImmutablePreference copyOf(Preference preference) {
		Preconditions.checkNotNull(preference);
		return new ImmutablePreferenceImpl(preference.getVoter(), preference.asGraph());
	}

	/**
	 * 
	 * @param voter <code> not null </code>
	 * @param graph <code> not null </code> graph with ordered Alternatives
	 * cacheHashCode is calculated just one time, because it's an Immutable class. So that will not change in the futur.
	 */
	protected ImmutablePreferenceImpl(Voter voter, Graph<Alternative> graph) {
		LOGGER.debug("ImmutablePreferenceImpl constructor from graph");
		this.graphIntransitivelyClosed = ImmutableGraph.copyOf(graph);
		this.graph = ImmutableGraph.copyOf(Graphs.transitiveClosure(this.graphIntransitivelyClosed));
		this.alternatives = ImmutableSet.copyOf(graph.nodes());
		this.voter = voter;
		this.cacheHashCode = calculerHashCode();
	}

	@Override
	public ImmutableGraph<Alternative> asGraph() {
		return this.graph;
	}

	public ImmutableGraph<Alternative> asIntransitiveGraph() {
		return this.graphIntransitivelyClosed;
	}

	@Override
	public ImmutableSet<Alternative> getAlternatives() {
		return this.alternatives;
	}

	@Override
	public Voter getVoter() {
		return this.voter;
	}
	
	/**
	 * I have followed and made extensive use of the course located at :
	 * https://jmdoudoux.developpez.com/cours/developpons/java/chap-techniques_java.php#techniques_java-2
	**/
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(o == null) {
			return false;
		}
		if(this.getClass() != o.getClass()) {
			return true;
		}
		ImmutablePreferenceImpl other = (ImmutablePreferenceImpl) o;
		if(this.hashCode() != other.hashCode()) {
			return false;
		}
		if(this.alternatives == null) {
			if(other.alternatives != null) {
				return false;
			}
		}
		else if(!this.alternatives.equals(other.alternatives)){
			return false;
		}
		if(this.voter == null) {
			if(other.voter != null) {
				return false;
			}
		}
		else if(!this.voter.equals(other.voter)) {
			return false;
		}
		if(this.graph == null) {
			if(other.graph != null) {
				return false;
			}
		}
		else if(!this.graph.equals(other.graph)) {
			return false;
		}
		return true;
	}
	
	/**
	 * I have followed and made extensive use of the course located at :
	 * https://jmdoudoux.developpez.com/cours/developpons/java/chap-techniques_java.php#techniques_java-2
	 */
	@Override
	public int hashCode() {
		return this.cacheHashCode;
		
	}
	/**
	 * Used to calculate a unique Hashcode.
	 * 
	 * @return a calculated integer corresponding to the HashCode
	 */
	private int calculerHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.alternatives == null ) ? 0 : this.alternatives.hashCode());
		result = prime * result + ((this.voter == null) ? 0 : this.voter.hashCode());
		result = prime * result + ((this.graph == null) ? 0 : this.graph.hashCode());
		return result;
	}
}
