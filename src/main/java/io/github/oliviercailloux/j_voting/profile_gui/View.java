package io.github.oliviercailloux.j_voting.profile_gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

public class View {
	private Shell mainShell;
	private TabFolder tabfolder;
	private EditionView editionView;
	private VisualizationView visualizationView;

	/**
	 * Factory method to create the main Shell
	 * 
	 * @param mainShell the mainShell where to plug the tab folder
	 * @return a new shell
	 */
	public static View create(Shell mainShell) {
		return new View(mainShell);
	}

	private View(Shell shell) {
		this.mainShell = shell;
		initTabFolder();
		this.editionView = EditionView.create(this.tabfolder);
		this.visualizationView = VisualizationView.create(this.tabfolder);
	}

	/**
	 * Initialization of the window
	 */
	private void initTabFolder() {
		this.tabfolder = new TabFolder(this.mainShell, SWT.NONE | SWT.V_SCROLL);
		tabfolder.setSize(10000, 10000);
	}

	public EditionView getEditionView() {
		return editionView;
	}

	public VisualizationView getVisualizationView() {
		return visualizationView;
	}
}
