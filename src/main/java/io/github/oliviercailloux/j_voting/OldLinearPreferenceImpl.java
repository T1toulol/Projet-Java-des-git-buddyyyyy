package io.github.oliviercailloux.j_voting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class is immutable Contains a list of Alternatives sorted by preferences
 * Two alternatives can't be equally ranked You can't store the same alternative
 * several times in the list Every alternative is an integer and corresponds to
 * a voting choice
 */
public class OldLinearPreferenceImpl extends OldCompletePreferenceImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(OldLinearPreferenceImpl.class.getName());

	/**
	 * @param preferences a list of alternatives.
	 */
	private OldLinearPreferenceImpl(List<Alternative> preference) {
		super(listAlternativeToListSetAlternative(preference));
		LOGGER.debug("StrictPreference constructor");
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Set<Alternative> set : preference) {
			for (Alternative alter : set) {
				s.append(alter.toString() + ",");
			}
		}
		s.delete(s.length() - 1, s.length());
		return s.toString();
	}

	/**
	 * 
	 * @return a list of the alternatives by order of preference
	 */
	public List<Alternative> getAlternatives() {
		LOGGER.debug("getAlternatives :");
		return listSetAlternativeToList(preference);
	}

	/**
	 * @param list a list of alternatives not <code> null </code>
	 * @return a list of set of alternatives. each set is composed of one
	 *         alternative
	 */
	public static List<Set<Alternative>> listAlternativeToListSetAlternative(List<Alternative> list) {
		LOGGER.debug("listAlternativeToListSetAlternative :");
		checkNotNull(list);
		LOGGER.debug("parameter list : {}", list);
		List<Set<Alternative>> set = new ArrayList<>();
		for (Alternative a : list) {
			Set<Alternative> alterset = new HashSet<>();
			alterset.add(a);
			set.add(alterset);
		}
		LOGGER.debug("new list of set : {}", set);
		return set;
	}

	/**
	 * @param sets not <code>null</code>
	 * @return a list of alternatives from a list of sets of alternatives.
	 */
	public static List<Alternative> listSetAlternativeToList(List<Set<Alternative>> sets) {
		LOGGER.debug("listSetAlternativeToList :");
		checkNotNull(sets);
		LOGGER.debug("parameter sets :{}", sets);
		List<Alternative> alts = new ArrayList<>();
		for (Set<Alternative> s : sets) {
			for (Alternative a : s) {
				alts.add(a);
			}
		}
		LOGGER.debug("list : {}", alts);
		return alts;
	}

	/**
	 * Factory method for StrictCompletePreferenceImpl
	 * 
	 * @param preference <code> not null</code> and all different alternatives
	 * @return a new StrictCompletePreferenceImpl
	 */
	public static OldLinearPreferenceImpl createStrictCompletePreferenceImpl(List<Alternative> preference) {
		checkNotNull(preference);
		return new OldLinearPreferenceImpl(preference);
	}

	/**
	 * 
	 * @param position not <code>null</code>
	 * @return the alternative at the position given in the strict preference
	 */
	@Override
	public Alternative getAlternative(Integer position) {
		LOGGER.debug("getAlternative");
		checkNotNull(position);
		LOGGER.debug("position : {}", position);
		if (position >= preference.size()) {
			throw new IndexOutOfBoundsException("This position doesn't exist in the Preference");
		}
		return preference.get(position).iterator().next();
	}
}
