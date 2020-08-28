package io.github.oliviercailloux.j_voting.profiles.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.j_voting.Alternative;
import io.github.oliviercailloux.j_voting.OldCompletePreferenceImpl;
import io.github.oliviercailloux.j_voting.Voter;
import io.github.oliviercailloux.j_voting.profiles.StrictProfileI;

public class SOIRowsGUI extends ProfileDefaultGUI {

	private static final Logger LOGGER = LoggerFactory.getLogger(SOCRowsGUI.class.getName());

	@Override
	public void createColumns() {
		LOGGER.debug("createColumns :");
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();
		// if profile get from file is SOC, create a StrictProfile from it
		// COLUMNS
		List<String> titles = new ArrayList<>();
		titles.add("Voters");
		for (int a = 1; a <= strictProfile.getMaxSizeOfPreference(); a++) {
			titles.add("Alternative " + a);
		}
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}
	}

	@Override
	public void checkRadioButton() {
		LOGGER.debug("checkRadioButtons");
		columnsButton.setSelection(false);
		rowsButton.setSelection(true);
		wrapButton.setSelection(false);
	}

	@Override
	public void populateRows() {
		LOGGER.debug("populateRows :");
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();
		// ROWS
		Iterable<Voter> allVoters = strictProfile.getAllVoters(); // get voters
																	// from
																	// profile
		int nbAlternatives = strictProfile.getNbAlternatives();
		List<String> line = new ArrayList<>();
		for (Voter v : allVoters) {
			line.add("Voter " + v.getId());
			OldCompletePreferenceImpl pref = strictProfile.getPreference(v);
			Iterable<Alternative> allPref = OldCompletePreferenceImpl.toAlternativeSet(pref.getPreferencesNonStrict());
			for (Alternative a : allPref) {
				line.add(a.toString());
			}
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(line.toArray(new String[nbAlternatives + 1]));
			line.clear(); // empty the list
		}
	}

	public static void main(String[] args) throws IOException {
		SOIRowsGUI soiRows = new SOIRowsGUI();
		soiRows.displayProfileWindow(args);
	}
}
