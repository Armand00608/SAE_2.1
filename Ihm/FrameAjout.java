package exFinal.Ihm;
import exFinal.Controleur;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;

public class FrameAjout extends JFrame implements WindowListener {
	private static boolean isFrameOpen = false;

	private PanelAjout panelAjout;

	public FrameAjout(Controleur ctrl) {
		if (FrameAjout.isFrameOpen) {
			return;
		}

		FrameAjout.isFrameOpen = true;

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(400, 400);
		this.setTitle("Ajouter");

		this.panelAjout = new PanelAjout(ctrl);
		this.add(this.panelAjout);

		this.addWindowListener(this);
		this.setVisible(true);
	}

	public void windowClosed(WindowEvent e) {
		FrameAjout.isFrameOpen = false;
	}

	public void windowOpened(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

}
