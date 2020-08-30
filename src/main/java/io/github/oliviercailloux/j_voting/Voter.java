package io.github.oliviercailloux.j_voting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * This class is immutable Contains an integer being the id of the voter
 */
public class Voter implements Comparable<Voter> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Voter.class.getName());

	public static final Voter ZERO = new Voter(0);

	private int id;

	/**
	 * Factory method for Voter
	 *
	 * @param id <code>not null</code>
	 * @return a new Voter
	 */
	public static Voter createVoter(int id) {
		Preconditions.checkNotNull(id);
		return new Voter(id);
	}

	private Voter(int id) {
		this.id = id;
	}

	/**
	 * @return the id of the object Voter
	 */
	public int getId() {
		return id;
	}

	/**
	 *
	 * @param v2 not <code> null </code>
	 * @return an integer : 0 if the voters have the same id, <0 if the calling
	 *         voter is smaller than the parameter, else >0.
	 */
	@Override
	public int compareTo(Voter v2) {
		LOGGER.debug("compare:");
		Preconditions.checkNotNull(v2);
		LOGGER.debug("calling voter : v1 {}, parameter v2 {}", this.getId(), v2.getId());
		return this.getId() - v2.getId();
	}

	/**
	 * @param voter <code> not null</code>
	 * @return whether two voters are equal, ie have the same id.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (this.getClass() != o.getClass()) {
			return false;
		}
		Voter voter = (Voter) o;
		return this.getId() == voter.getId();
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).addValue(id).toString();
	}
}
