package io.github.oliviercailloux.j_voting.preferences.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.VerifyException;
import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingSet;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import io.github.oliviercailloux.j_voting.Alternative;
import io.github.oliviercailloux.j_voting.Voter;
import io.github.oliviercailloux.j_voting.graph.GraphView;
import io.github.oliviercailloux.j_voting.preferences.MutableLinearPreference;

public class MutableLinearPreferenceImpl implements MutableLinearPreference {

	private Voter voter;
	private MutableGraph<Alternative> graph;
	private Set<Alternative> alternatives;
	private List<Alternative> list;

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(MutableLinearPreferenceImpl.class);

	private MutableLinearPreferenceImpl(Voter voter, List<Alternative> list) {

		this.voter = voter;
		this.list = new ArrayList<>(list);
		this.graph = GraphBuilder.directed().allowsSelfLoops(true).build();

		for (int i = 0; i < list.size(); i++) {
			for (int j = i; j < list.size(); j++) {
				graph.putEdge(list.get(i), list.get(j));
			}
		}
		this.alternatives = graph.nodes();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("Voter", voter).add("Graph", graph).add("Set", alternatives)
				.add("List", list).toString();
	}

	/**
	 * @param list  is a List of alternatives representing the preference.
	 * @param voter is the Voter associated to the Preference.
	 * @return the mutable linear preference
	 */
	public static MutableLinearPreference given(Voter voter, List<Alternative> list) {
		LOGGER.debug("MutableLinearPreferenceImpl given");
		Preconditions.checkNotNull(voter);
		Preconditions.checkNotNull(list);

		return new MutableLinearPreferenceImpl(voter, list);
	}

	@Override
	public boolean changeOrder(Alternative alternative, int rank) {
		LOGGER.debug("MutableLinearPreferenceImpl tries to changeOrder of " + alternative + " at " + rank + " rank.");
		Preconditions.checkNotNull(alternative);
		Preconditions.checkNotNull(rank);

		if (list.indexOf(alternative) == rank || !(list.contains(alternative)) || rank < 1 || rank > list.size()) {
			return false;
		}

		int initRank = list.indexOf(alternative);
		Alternative temp;

		if (initRank > rank) {
			for (int i = initRank; i >= rank; i--) {
				temp = list.get(i - 1);
				swap(temp, alternative);
			}
		} else if (initRank < rank) {
			for (int i = initRank; i <= rank; i++) {
				temp = list.get(i - 1);
				swap(alternative, temp);
			}
		}
		return true;
	}
	
	@Override
	public boolean removeAlternative(Alternative a) {
		LOGGER.debug("MutableLinearPreferenceImpl deleteAlternative");
		Preconditions.checkNotNull(a);
		checkNotNull(list.contains(a));
		if (alternatives.contains(a)) {
			graph.removeNode(a);
			list.remove(a);
			return true;
		}
		return false;
	}

	@Override
	public boolean addAlternative(Alternative a) {
		LOGGER.debug("MutablePreferenceImpl addAlternative");
		Preconditions.checkNotNull(a);

		if (alternatives.contains(a)) {
			return false;
		}

		list.add(a);
		graph.addNode(a);

		for (Alternative element : list) {
			graph.putEdge(element, list.get(list.size() - 1));
		}
		return true;
	}

	/**
	 * Clears the MutableLinearPreference (list + set + graph)
	 */
	private void clear() {
		for (Alternative element : list) {
			graph.removeNode(element);
		}
		list.clear();
	}

	@Override
	public Set<Alternative> getAlternatives() {
		LOGGER.debug("MutableLinearPreferenceImpl getAlternatives");
		return new MutableLinearSetDecorator(this);
	}

	@Override
	public Voter getVoter() {
		return voter;
	}

	@Override
	public Graph<Alternative> asGraph() {
		return GraphView.decorate(this.graph);
	}

	@Override
	public boolean swap(Alternative alternative1, Alternative alternative2) {
		LOGGER.debug("MutablePreferenceImpl Swap");
		Preconditions.checkNotNull(alternative1);
		Preconditions.checkNotNull(alternative2);

		if (alternative1.equals(alternative2) || !(alternatives.contains(alternative1))
				|| !(alternatives.contains(alternative2))) {
			return false;
		}

		boolean op1, op2, op3, op4;

		Alternative best = alternative1;
		Alternative worst = alternative2;

		if (list.indexOf(alternative1) > list.indexOf(alternative2)) {
			best = alternative2;
			worst = alternative1;
		}

		List<Alternative> subList = list.subList(list.indexOf(best) + 1, list.indexOf(worst));

		for (Alternative a : subList) {

			op1 = graph.removeEdge(best, a);
			op2 = graph.putEdge(a, best);
			op3 = graph.removeEdge(a, worst);
			op4 = graph.putEdge(worst, a);

			if (!(op1 && op2 && op3 && op4)) {
				throw new VerifyException("There might be a bug in the stucture.");
			}
		}

		op1 = graph.removeEdge(best, worst);
		op2 = graph.putEdge(worst, best);

		if (!(op1 && op2)) {
			throw new VerifyException("There might be a bug in the stucture.");
		}

		Collections.swap(list, list.indexOf(alternative1), list.indexOf(alternative2));

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(voter, list);
	}

	@Override
	public boolean equals(Object o2) {
		if (!(o2 instanceof MutableLinearPreferenceImpl)) {
			return false;
		}
		if (this == o2) {
			return true;
		}
		MutableLinearPreferenceImpl mlp2 = (MutableLinearPreferenceImpl) o2;
		return voter.equals(mlp2.voter) && list.equals(mlp2.list);
	}

	/**
	 * In the future, ideally, sets returned via this decorator should be protected
	 * from alteration.
	 */
	public static class MutableLinearSetDecorator extends ForwardingSet<Alternative> {
		private MutableLinearPreferenceImpl delegate;

		private MutableLinearSetDecorator(MutableLinearPreferenceImpl delegate) {
			this.delegate = delegate;
		}

		@Override
		protected Set<Alternative> delegate() {
			return delegate.alternatives;
		}

		@Override
		public boolean add(Alternative a) {
			LOGGER.debug("MutableLinearSetDecorator add");
			return delegate.addAlternative(a);
		}

		@Override
		public boolean addAll(Collection<? extends Alternative> c) {
			LOGGER.debug("MutableLinearSetDecorator delegate addAll");
			return standardAddAll(c);
		}

		@Override
		public boolean remove(Object o) {
			LOGGER.debug("MutableLinearSetDecorator delegate remove");

			if (o instanceof Alternative) {
				Alternative a = (Alternative) o;
				if (delegate.getAlternatives().contains(a)) {
					return delegate.removeAlternative(a);
				}
			}
			return false;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			LOGGER.debug("MutableLinearSetDecorator delegate removeAll");
			return standardRemoveAll(c);
		}

		@Override
		public void clear() {
			LOGGER.debug("MutableLinearSetDecorator delegate clear");
			delegate.clear();
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			LOGGER.debug("MutableLinearSetDecorator delegate retainAll");
			return standardRetainAll(c);
		}

		@Override
		public Iterator<Alternative> iterator() {
			LOGGER.debug("MutableLinearSetDecorator delegate iterator");
			return new MutableLinearIteratorDecorator(delegate.list.iterator());
		}
	}

	public static class MutableLinearIteratorDecorator extends ForwardingIterator<Alternative> {

		private Iterator<Alternative> iteratorDelegate;

		@Override
		protected Iterator<Alternative> delegate() {
			return iteratorDelegate;
		}

		private MutableLinearIteratorDecorator(Iterator<Alternative> iteratorDelegate) {
			this.iteratorDelegate = iteratorDelegate;
		}
	}

}
