package io.github.oliviercailloux.j_voting.profile_gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

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
		EditionController editionController = controller.buildEditionController(editionView);
		editionController.openFile(
				"C:\\Users\\Firas\\eclipse-workspace\\projet-j-voting\\src\\main\\resources\\io\\github\\oliviercailloux\\j_voting\\profiles\\management\\profil.soc");

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
