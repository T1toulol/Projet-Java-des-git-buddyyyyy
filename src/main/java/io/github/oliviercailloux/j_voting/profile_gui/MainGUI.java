package io.github.oliviercailloux.j_voting.profile_gui;

import org.eclipse.swt.widgets.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;

public class MainGUI {

	public static void main(String[] args) {
		new MainGUI().displayGUI();
	}

	/**
	 * Creating the main window and calling controllers and views
	 */
	public void displayGUI() {
		Display display = new Display();
		Shell shell = new Shell(display, SWT.CLOSE | SWT.RESIZE);
		shell.setText("J-Voting");
		shell.setSize(600, 400);
		centerOnScreen(display, shell);

		View view = View.create(shell);
		EditionView editionView = view.getEditionView();

		Controller controller = Controller.withDefaultModel();
		controller.buildEditionController(editionView);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * Center the window in the middle of the screen
	 */
	private void centerOnScreen(Display display, Shell shell) {
		Rectangle screenSize = display.getPrimaryMonitor().getBounds();
		shell.setLocation((screenSize.width - shell.getBounds().width) / 2,
				(screenSize.height - shell.getBounds().height) / 2);
	}
}
