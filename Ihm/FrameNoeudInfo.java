package Ihm;

import Metier.Tache;
import exFinal.Controleur;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;

public class FrameNoeudInfo extends JFrame implements WindowListener
{
	private static boolean isFrameOpen = false;
	private PanelNoeudInfo panelNoeudInfo;
	private Controleur ctrl;

	public FrameNoeudInfo(Tache tache, Controleur ctrl)
	{
		if (FrameNoeudInfo.isFrameOpen) {
			return;
		}

		FrameNoeudInfo.isFrameOpen = true;
		this.ctrl = ctrl;

		setTitle("Informations du nœud");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Ajout du panel
		this.panelNoeudInfo = new PanelNoeudInfo(tache, ctrl);
		this.add(this.panelNoeudInfo);

		pack();

		this.addWindowListener(this);
		this.setVisible(true);
	}

	public void windowOpened(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	public void windowClosed(WindowEvent e)
	{
		FrameNoeudInfo.isFrameOpen = false;
	}
}