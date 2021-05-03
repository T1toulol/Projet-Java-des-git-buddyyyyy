package io.github.oliviercailloux.j_voting.profile_gui;

import java.util.*;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

import io.github.oliviercailloux.j_voting.Alternative;

public class EditionController {
	private EditionView editionView;
	private Controller controller;

	public static EditionController create(EditionView editionView, Controller mainController) {
		return new EditionController(editionView, mainController);
	}

	private EditionController(EditionView editionView, Controller mainController) {
		this.editionView = editionView;
		this.controller = mainController;
		initEditionView();
	}

	/**
	 * Initiate the edition view with the originally default or chosen startModel.
	 */
	private void initEditionView() {
		String voterName = this.controller.getModel().getVoter().toString();
		editionView.addVoter(voterName);

		Set<Alternative> altSet = this.controller.getModel().getAlternatives();
		editionView.addPreference(altSet);
		editionView.attachAddAlternativeListener(this.buildAddAlternativeBehavior());
		editionView.attachDeleteAlternativeListener(this.buildDeleteAlternativeBehavior());
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
