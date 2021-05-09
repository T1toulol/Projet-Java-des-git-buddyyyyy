package io.github.oliviercailloux.j_voting.profile_gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import io.github.oliviercailloux.j_voting.Alternative;

public class EditionController {
	private EditionView editionView;
	private Controller controller;
	private File file;
	private FileReader fr;
	private BufferedReader br;
	private int voterIdx;

	public static EditionController create(EditionView editionView, Controller mainController) {
		return new EditionController(editionView, mainController);
	}

	private EditionController(EditionView editionView, Controller mainController) {
		this.editionView = editionView;
		this.controller = mainController;
		this.voterIdx = 0;
		initEditionView();
	}

	/**
	 * Initiate the edition view with the originally default or chosen startModel.
	 */
	private void initEditionView() {
		// String voterName = this.controller.getModel().getVoter().toString();
		// editionView.addVoter(voterName);

		// Set<Alternative> altSet = this.controller.getModel().getAlternatives();
		// editionView.addPreference(altSet);
		editionView.attachAddAlternativeListener(this.buildAddAlternativeBehavior());
		editionView.attachDeleteAlternativeListener(this.buildDeleteAlternativeBehavior());
	}

	/**
	 * Open the SOC profile file
	 * 
	 * @param fileToOpen local path to SOC profile files
	 */
	public void openFile(String fileToOpen) {
		int idx;
		try {
			file = new File(fileToOpen);
			fr = new FileReader(file);
			br = new BufferedReader(fr);

		} catch (IOException e) {
			e.printStackTrace();
		}
		readFile();
	}

	/**
	 * Read the content of SOC profile file
	 */
	private void readFile() {
		try {
			int idx = 0;

			String line;
			while ((line = br.readLine()) != null) {
				if (idx < 5) {
					idx++;
				} else {
					idx++;
					displayLine(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Displays the lines of the SOC profile in the correct format
	 * 
	 * @param line lines to displays send one by one by the readFile function
	 */
	private void displayLine(String line) {
		int ind;
		String vot;
		Set<Alternative> alternatives = new LinkedHashSet<Alternative>();
		for (int i = 1; i < line.length(); i++) {
			if (line.charAt(i) != ',') {
				ind = Integer.parseInt(String.valueOf(line.charAt(i)));
				alternatives.add(Alternative.withId(ind));
			}
		}
		char idx = line.charAt(0);
		int idx_ = Character.getNumericValue(idx);
		for (int i = 0; i < idx_; i++) {
			vot = "Voter : " + String.format("%d", voterIdx);
			editionView.addVoter(String.format(vot));
			editionView.addPreference(alternatives);
			voterIdx++;

		}

	}

	/**
	 * Builds the behavior to add an alternative in the view
	 * 
	 * @return the behavior to attach to a SelectionListener
	 */
	private SelectionAdapter buildAddAlternativeBehavior() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button btnData = (Button) e.getSource();
				handleAddAlternative(btnData);
			}
		};
	}

	/**
	 * Definition of the add alternative behavior to execute when the corresponding
	 * button is clicked.
	 * 
	 * @param btn the button clicked by the user.
	 */
	private void handleAddAlternative(Button btn) {
		String textFieldContent = editionView.getTextFieldContent(btn);

		try {
			Integer textFieldId = Integer.parseInt(textFieldContent);
			Alternative newAlt = Alternative.withId(textFieldId);

			if (controller.getModel().getAlternatives().contains(newAlt)) {
				editionView.setUserIndicationText("Alternative already exists");
				return;
			}

			controller.getModel().addAlternative(newAlt);
			editionView.refreshAlternativeSection(controller.getModel().getAlternatives());
			editionView.attachAddAlternativeListener(this.buildAddAlternativeBehavior());
			editionView.attachDeleteAlternativeListener(this.buildDeleteAlternativeBehavior());
		} catch (NumberFormatException e) {
			editionView.setUserIndicationText("Not a number");
		}
	}

	/**
	 * Builds the behavior to delete an alternative in the view
	 *
	 * @return the behavior to attach to a SelectionListener
	 */
	private SelectionAdapter buildDeleteAlternativeBehavior() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button btnData = (Button) e.getSource();
				handleDeleteAlternative(btnData);
			}
		};
	}

	/**
	 * Definition of the delete alternative behavior to execute when the
	 * corresponding button is clicked.
	 *
	 * @param btn the button clicked by the user.
	 */
	private void handleDeleteAlternative(Button btn) {
		Alternative altToDelete = editionView.getAlternativeToDelete(btn);
		controller.getModel().removeAlternative(altToDelete);
		editionView.deleteAlternative(btn);
	}

}
