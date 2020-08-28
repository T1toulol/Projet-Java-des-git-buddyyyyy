package io.github.oliviercailloux.j_voting.profiles.management;

import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.j_voting.OldLinearPreferenceImpl;
import io.github.oliviercailloux.j_voting.Voter;
import io.github.oliviercailloux.j_voting.profiles.ImmutableProfileI;
import io.github.oliviercailloux.j_voting.profiles.analysis.SocialWelfareFunction;

public class SWFCommander {

	private SocialWelfareFunction swf;
	private static final Logger LOGGER = LoggerFactory.getLogger(SWFCommander.class.getName());

	/**
	 * Factory method for SWFCommander
	 * 
	 * @param s
	 * @return new SWFCommander
	 */
	public static SWFCommander createSWFCommander(SocialWelfareFunction s) {
		return new SWFCommander(s);
	}

	private SWFCommander(SocialWelfareFunction s) {
		this.swf = s;
	}

	/**
	 * Asks the user to enter a StrictPreference
	 * 
	 * @return the entered StrictPreference
	 * @throws IOException when the entered preference is empty.
	 */
	public static OldLinearPreferenceImpl askPreference() throws IOException {
		LOGGER.debug("askPreference");
		System.out.println("Enter a StrictPreference complete");
		try (Scanner scan = new Scanner(System.in)) {
			LOGGER.debug("Scanner OK");
			String vote = scan.nextLine();
			if (vote.isEmpty()) {
				throw new IOException("empty Preference entered !");
			}
			return new ReadProfile().createStrictPreferenceFrom(vote);
		}
	}

	/**
	 * Asks the user to enter StrictPreferences while he doesn't say no to the
	 * question "continue?". Each time the user enters a StrictPreference, it
	 * displays the current state of the StrictProfile (the winning
	 * StrictPreference).
	 * 
	 * @throws IOException when the entered preference is empty.
	 */
	public void createProfileIncrementally() throws IOException {
		LOGGER.debug("createProfileIncrementally:");
		StrictProfileBuilder prof = StrictProfileBuilder.createStrictProfileBuilder();
		boolean keepGoing = true;
		int voterId = 1;
		while (keepGoing) {
			LOGGER.debug("new voter id  : {}", voterId);
			Voter v = Voter.createVoter(voterId);
			OldLinearPreferenceImpl oldLinearPreferenceImpl = askPreference();
			LOGGER.debug("strictPreference :{}", oldLinearPreferenceImpl);
			prof.addVote(v, oldLinearPreferenceImpl);
			LOGGER.info("Continue ? (yes/no)");
			try (Scanner scn = new Scanner(System.in)) {
				String answer = scn.nextLine();
				// user can answer "yes" or just "y" to continue
				if (answer.trim().equalsIgnoreCase(("yes")) && answer.trim().equalsIgnoreCase("y")) {
					LOGGER.debug("answered no to continue.");
					keepGoing = false;
				}
			}
			voterId++;
			ImmutableProfileI profile = (ImmutableProfileI) prof.createProfileI();
			LOGGER.info(swf.getSocietyPreference(profile).toString());
		}
	}
}
