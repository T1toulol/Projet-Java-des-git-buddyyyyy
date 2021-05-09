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
	private StringBuffer sb;
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

	private void testAffichage() {
		try {
			int idx = 0;

			// Voter voter = Voter.withId(0);
			// Alternative alt1 = Alternative.withId(5);
			// Alternative alt2 = Alternative.withId(4);
			// Alternative alt3 = Alternative.withId(6);
			// Set<Alternative> alternatives = new LinkedHashSet<Alternative>();

			// alternatives.add(alt1);
			// alternatives.add(alt2);
			// alternatives.add(alt3);

			// editionView.addVoter("0");
			// editionView.addPreference(alternatives);
			// editionView.addVoter("0");
			// editionView.addPreference(alternatives);
			// editionView.addVoter("0");
			// editionView.addPreference(alternatives);

			String line;
			while ((line = br.readLine()) != null) {
				if (idx < 5) {
					idx++;
				} else {
					idx++;
					addLine(line);
					sb.append(line);
					sb.append("\n");
				}
			}
		} catch (

		IOException e) {
			e.printStackTrace();
		}

	}

	private void addLine(String line) {
		int ind;
		int vot = 0;
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
			editionView.addVoter(String.format("%d", voterIdx));
			editionView.addPreference(alternatives);
			voterIdx++;

		}

	}

	public void openFile(String fileToOpen) {
		int idx;
		try {
			// Le fichier d'entrée
			file = new File(fileToOpen);
			// Créer l'objet File Reader
			fr = new FileReader(file);
			// Créer l'objet BufferedReader
			br = new BufferedReader(fr);
			this.sb = new StringBuffer();
			// String line;
			// while ((line = br.readLine()) != null) {
			// ajoute la ligne au buffer
			// sb.append(line);
			// sb.append("\n"); }
			// fr.close();
			// System.out.println("Contenu du fichier: ");
			// System.out.println(sb.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
		testAffichage();
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
