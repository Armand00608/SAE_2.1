package exFinal.Ihm;

import exFinal.Controleur;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

public class BarreMenu extends JMenuBar implements ActionListener {
	private JMenuItem menuiSauv;
	private JMenuItem menuiAjou;
	private JMenuItem menuiSupp;
	private JMenuItem menuiDure;

	private Controleur ctrl;
	private FrameAjout frameAjout;

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
		if (e.getSource() == this.menuiAjou)
		{
			frameAjout = new FrameAjout(this.ctrl);
		}

		if (e.getSource() == this.menuiSupp)
		{

		}

		if (e.getSource() == this.menuiSauv) 
		{
			
			JFileChooser dialogueEnregistrement = new JFileChooser();
			dialogueEnregistrement.setDialogTitle("Enregistrer les positions sous...");
			dialogueEnregistrement.setCurrentDirectory(new File("./enreg"));
		
			dialogueEnregistrement.setSelectedFile(new File("mon_projet_mpm.txt"));
		
			int choixUtilisateur = dialogueEnregistrement.showSaveDialog(this.getParent());
		
			if (choixUtilisateur == JFileChooser.APPROVE_OPTION) 
			{
				File fichierAEnregistrer = dialogueEnregistrement.getSelectedFile();
				String cheminAbsolu = fichierAEnregistrer.getAbsolutePath();
				
				if (this.ctrl.enregistrer(cheminAbsolu))
					JOptionPane.showMessageDialog(null, "Fichier " + fichierAEnregistrer.getName() + " enregistré avec succès \nChemin :" + cheminAbsolu, " Enregistrement", JOptionPane.PLAIN_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "Erreur d'enregistrement", " Enregistrement", JOptionPane.ERROR_MESSAGE);
			} 
			else {
				// L'utilisateur a cliqué sur "Annuler" ou a fermé la fenêtre
				JOptionPane.showMessageDialog(null, "Enregistrement annulé par l'utilisateur.", "Enregistrement", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}
