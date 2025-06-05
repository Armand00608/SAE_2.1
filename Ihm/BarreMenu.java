package Ihm;

import exFinal.Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BarreMenu extends JMenuBar implements ActionListener {
	private JMenuItem menuiSauv;
	private JMenuItem menuiAjou;
	private JMenuItem menuiSupp;
	private JMenuItem menuiDure;

	private boolean frameAjout;
	private Controleur ctrl;

	public BarreMenu(Controleur ctrl){

		this.ctrl = ctrl;
		JMenu menuFichier = new JMenu("Fichier");
		JMenu menuEdition = new JMenu("Edition");

		this.menuiSauv = new JMenuItem("Enregistrer");

		this.menuiAjou = new JMenuItem("Ajouter");
		this.menuiSupp = new JMenuItem("Supprimer");
		this.menuiDure = new JMenuItem("Changer Durée");

		menuFichier.add(this.menuiSauv);

		menuEdition.add(this.menuiAjou);
		menuEdition.add(this.menuiSupp);
		menuEdition.addSeparator();
		menuEdition.add(this.menuiDure);

		this.add(menuFichier);
		this.add(menuEdition);

		this.menuiSauv.addActionListener(this);
		this.menuiSupp.addActionListener(this);
		this.menuiDure.addActionListener(this);
		this.menuiAjou.addActionListener(this);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.menuiAjou){
			if (!this.frameAjout){
				new FrameAjout(this.ctrl);
			}
		}
	}
}
