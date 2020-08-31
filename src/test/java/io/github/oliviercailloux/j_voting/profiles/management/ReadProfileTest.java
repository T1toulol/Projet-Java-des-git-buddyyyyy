package io.github.oliviercailloux.j_voting.profiles.management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.oliviercailloux.j_voting.Alternative;
import io.github.oliviercailloux.j_voting.OldLinearPreferenceImpl;
import io.github.oliviercailloux.j_voting.Voter;
import io.github.oliviercailloux.j_voting.profiles.ProfileI;
import io.github.oliviercailloux.j_voting.profiles.StrictProfileI;
import io.github.oliviercailloux.j_voting.profiles.management.ReadProfile;
import io.github.oliviercailloux.j_voting.profiles.management.StrictProfileBuilder;

public class ReadProfileTest {

    @Test
    public void testGetPreferences() {
        ReadProfile rp = new ReadProfile();
        Alternative a1 = Alternative.withId(1);
        Alternative a2 = Alternative.withId(2);
        Alternative a3 = Alternative.withId(3);
        List<Alternative> alternatives = new ArrayList<>();
        alternatives.add(a1);
        alternatives.add(a2);
        alternatives.add(a3);
        OldLinearPreferenceImpl pref = OldLinearPreferenceImpl.createStrictCompletePreferenceImpl(alternatives);
        List<Alternative> alternatives2 = new ArrayList<>();
        alternatives2.add(a2);
        alternatives2.add(a1);
        alternatives2.add(a3);
        OldLinearPreferenceImpl pref2 = OldLinearPreferenceImpl.createStrictCompletePreferenceImpl(alternatives2);
        assertEquals(pref, rp.getPreferences(pref2, "4,1,2,3"));
    }

    @Test
    public void testAddVotes() {
        StrictProfileBuilder p = StrictProfileBuilder.createStrictProfileBuilder();
        Alternative a1 = Alternative.withId(1);
        Alternative a2 = Alternative.withId(2);
        Alternative a3 = Alternative.withId(3);
        Voter v1 = Voter.withId(1);
        Voter v2 = Voter.withId(2);
        List<Alternative> alternatives = new ArrayList<>();
        alternatives.add(a1);
        alternatives.add(a2);
        alternatives.add(a3);
        OldLinearPreferenceImpl pref = OldLinearPreferenceImpl.createStrictCompletePreferenceImpl(alternatives);
        p.addVotes(pref, 2);
        StrictProfileI prof = p.createStrictProfileI();
        assertTrue(prof.getProfile().containsKey(v1));
        assertTrue(prof.getProfile().containsKey(v2));
        assertEquals(prof.getPreference(v1), pref);
        assertEquals(prof.getPreference(v2), pref);
    }

    @Test
    public void testBuildProfile() {
        ReadProfile rp = new ReadProfile();
        List<String> file = new ArrayList<>();
        file.add("2,1,2,3");
        file.add("1,3,2,1");
        Alternative a1 = Alternative.withId(1);
        Alternative a2 = Alternative.withId(2);
        Alternative a3 = Alternative.withId(3);
        Voter v1 = Voter.withId(1);
        Voter v2 = Voter.withId(2);
        Voter v3 = Voter.withId(3);
        List<Alternative> alternatives = new ArrayList<>();
        alternatives.add(a1);
        alternatives.add(a2);
        alternatives.add(a3);
        OldLinearPreferenceImpl pref = OldLinearPreferenceImpl.createStrictCompletePreferenceImpl(alternatives);
        List<Alternative> alternatives2 = new ArrayList<>();
        alternatives2.add(a3);
        alternatives2.add(a2);
        alternatives2.add(a1);
        OldLinearPreferenceImpl pref2 = OldLinearPreferenceImpl.createStrictCompletePreferenceImpl(alternatives2);
        ProfileI profile = rp.buildProfile(file, pref, 3);
        assertTrue(profile.getProfile().containsKey(v1));
        assertTrue(profile.getProfile().containsKey(v2));
        assertTrue(profile.getProfile().containsKey(v3));
        assertEquals(profile.getPreference(v1), pref);
        assertEquals(profile.getPreference(v2), pref);
        assertEquals(profile.getPreference(v3), pref2);
    }

    @Test
    public void testCreateProfileFromStream() throws IOException {
        ReadProfile rp = new ReadProfile();
        ProfileI profile = rp.createProfileFromStream(
                        getClass().getResourceAsStream("profileToRead.soc"));
        Alternative a1 = Alternative.withId(1);
        Alternative a2 = Alternative.withId(2);
        Alternative a3 = Alternative.withId(3);
        Voter v1 = Voter.withId(1);
        Voter v2 = Voter.withId(2);
        Voter v3 = Voter.withId(3);
        List<Alternative> alternatives = new ArrayList<>();
        alternatives.add(a1);
        alternatives.add(a2);
        alternatives.add(a3);
        OldLinearPreferenceImpl pref = OldLinearPreferenceImpl.createStrictCompletePreferenceImpl(alternatives);
        List<Alternative> alternatives2 = new ArrayList<>();
        alternatives2.add(a3);
        alternatives2.add(a2);
        alternatives2.add(a1);
        OldLinearPreferenceImpl pref2 = OldLinearPreferenceImpl.createStrictCompletePreferenceImpl(alternatives2);
        assertTrue(profile.getProfile().containsKey(v1));
        assertTrue(profile.getProfile().containsKey(v2));
        assertTrue(profile.getProfile().containsKey(v3));
        assertEquals(profile.getPreference(v1), pref);
        assertEquals(profile.getPreference(v2), pref);
        assertEquals(profile.getPreference(v3), pref2);
    }

    @Test
    public void testCreateProfileFromURL() throws IOException {
        ReadProfile rp = new ReadProfile();
        String fileURLAsString = "https://raw.githubusercontent.com/Perciii/J-Voting/master/src/test/resources/io/github/oliviercailloux/y2018/j_voting/profiles/management/profileToRead.soc";
        ProfileI profile = rp.createProfileFromURL(new URL(fileURLAsString));
        Alternative a1 = Alternative.withId(1);
        Alternative a2 = Alternative.withId(2);
        Alternative a3 = Alternative.withId(3);
        Voter v1 = Voter.withId(1);
        Voter v2 = Voter.withId(2);
        Voter v3 = Voter.withId(3);
        List<Alternative> alternatives = new ArrayList<>();
        alternatives.add(a1);
        alternatives.add(a2);
        alternatives.add(a3);
        OldLinearPreferenceImpl pref = OldLinearPreferenceImpl.createStrictCompletePreferenceImpl(alternatives);
        List<Alternative> alternatives2 = new ArrayList<>();
        alternatives2.add(a3);
        alternatives2.add(a2);
        alternatives2.add(a1);
        OldLinearPreferenceImpl pref2 = OldLinearPreferenceImpl.createStrictCompletePreferenceImpl(alternatives2);
        assertTrue(profile.getProfile().containsKey(v1));
        assertTrue(profile.getProfile().containsKey(v2));
        assertTrue(profile.getProfile().containsKey(v3));
        assertEquals(profile.getPreference(v1), pref);
        assertEquals(profile.getPreference(v2), pref);
        assertEquals(profile.getPreference(v3), pref2);
    }
}
