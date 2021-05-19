package io.github.oliviercailloux.j_voting.profile_gui;

import static com.google.common.base.Preconditions.checkNotNull;
import io.github.oliviercailloux.j_voting.Alternative;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.swt.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class EditionView {
	private TabFolder tabfolder;
	private TabItem editionTab;
	private Composite mainComposite;
	private GridLayout gridLayout;
	private Map<Button, Text> addAlternativeControls;
	private Map<Button, Alternative> deleteAlternativeControls;
	private Map<Alternative, Text> alternativeControls;
	private Label userIndication;

	/**
	 * Factory method to create the edition window
	 * 
	 * @param mainTabFolder the tab folder that holds the view
	 * @return a new window
	 */
	public static EditionView create(TabFolder mainTabFolder) {
		return new EditionView(mainTabFolder);
	}

	private EditionView(TabFolder mainTabFolder) {
		checkNotNull(mainTabFolder);
		this.tabfolder = mainTabFolder;
		this.mainComposite = new Composite(tabfolder, SWT.NONE);
		this.addAlternativeControls = new LinkedHashMap<>();
		this.alternativeControls = new LinkedHashMap<>();
		this.deleteAlternativeControls = new LinkedHashMap<>();

		this.gridLayout = new GridLayout(2, false);
		this.mainComposite.setLayout(gridLayout);

		addUserIndication();
		initEditionTab();
	}

	/**
	 * Initialization of the editing tab window
	 */
	private void initEditionTab() {
		this.editionTab = new TabItem(this.tabfolder, SWT.NONE);
		editionTab.setText("Edition");
	}

	private void addUserIndication() {
		this.userIndication = new Label(mainComposite, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, false, false);
		userIndication.setLayoutData(gridData);
	}

	/**
	 * Creation and display the text field with the voter
	 * 
	 * @param voterName for the voter's name in the Mutable Linear Preference.
	 */
	public void addVoter(String voterName) {
		Text voter = new Text(mainComposite, SWT.BORDER);
		voter.setText(voterName);

		GridData gridData = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, false, false, 2, SWT.NONE);
		voter.setLayoutData(gridData);
	}

	/**
	 * Creation and display of text fields with alternatives. Creating and
	 * displaying buttons for deleting an alternative.
	 * 
	 * @param altSet for the set of alternatives to display in the Mutable Linear
	 *               Preference..
	 */
	public void addPreference(Set<Alternative> altSet) {
		for (Alternative a : altSet) {
			Text alt = new Text(mainComposite, SWT.BORDER);
			alt.setText(a.toString());

			GridData data = new GridData(120, 15);
			data.horizontalAlignment = GridData.BEGINNING;
			alt.setLayoutData(data);
			this.alternativeControls.put(a, alt);

			Button btn = new Button(mainComposite, SWT.NONE);
			btn.setText("Delete");
			this.deleteAlternativeControls.put(btn, a);
		}

		addAlternative();
		mainComposite.layout(true);
	}

	/**
	 * Creation and display of the text field of the alternative to be added.
	 * Creation and display of the add button.
	 */
	public void addAlternative() {
		Text newAlt = new Text(mainComposite, SWT.BORDER);
		editionTab.setControl(mainComposite);

		GridData data = new GridData(120, 15);
		newAlt.setLayoutData(data);

		Button btn = new Button(mainComposite, SWT.NONE);
		btn.setText("Add Alternative");
		this.addAlternativeControls.put(btn, newAlt);
	}

	/**
	 * Attach a selection listener to all the add alternative button.
	 * 
	 * @param callback to execute when the button is clicked.
	 */
	public void attachAddAlternativeListener(SelectionAdapter callback) {
		for (Button btn : this.addAlternativeControls.keySet()) {
			btn.addSelectionListener(callback);
		}
	}

	/**
	 * Attach a selection listener to all the delete alternative button.
	 *
	 * @param callback to execute when the delete button is clicked.
	 */
	public void attachDeleteAlternativeListener(SelectionAdapter callback) {
		for (Button btn : this.deleteAlternativeControls.keySet()) {
			btn.addSelectionListener(callback);
		}
	}

	/**
	 * Return the text field content of the associated button provided as parameter.
	 * 
	 * @param btn the button associated to the desired text field not null
	 */
	public String getTextFieldContent(Button btn) {
		checkNotNull(btn);
		return this.addAlternativeControls.get(btn).getText();
	}

	/**
	 * Return the alternative of the associated delete button provided as parameter.
	 *
	 * @param btn the delete button associated to the desired alternative not null
	 */
	public Alternative getAlternativeToDelete(Button btn) {
		checkNotNull(btn);
		return this.deleteAlternativeControls.get(btn);
	}

	public void deleteAlternative(Button btnClicked) {
		checkNotNull(btnClicked);
		Alternative alt = this.deleteAlternativeControls.get(btnClicked);
		Text altCtr = this.alternativeControls.get(alt);

		altCtr.dispose();
		btnClicked.dispose();
		this.alternativeControls.remove(alt);
		this.deleteAlternativeControls.remove(btnClicked);
		this.mainComposite.layout(true);
	}

	public void setUserIndicationText(String indication) {
		this.userIndication.setText(indication);
	}

	/**
	 * Update the gui with the new alternative data provided in newAltSet
	 *
	 * @param newAltSet the new alternative set to display
	 */
	public void refreshAlternativeSection(Set<Alternative> newAltSet) {
		this.cleanAltContent();
		this.addPreference(newAltSet);
	}

	private void cleanAltContent() {
		for (Text ctr : alternativeControls.values()) {
			ctr.dispose();
		}
		for (Button btn : addAlternativeControls.keySet()) {
			btn.dispose();
		}
		for (Text text : addAlternativeControls.values()) {
			text.dispose();
		}
		for (Button btn : deleteAlternativeControls.keySet()) {
			btn.dispose();
		}
		this.alternativeControls.clear();
		this.addAlternativeControls.clear();
		this.deleteAlternativeControls.clear();
	}
}
