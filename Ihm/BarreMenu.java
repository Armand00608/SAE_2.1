package Ihm;

import exFinal.Controleur;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

public class BarreMenu extends JMenuBar implements ActionListener
{
	private JMenuItem menuiSauv;
	private JMenuItem menuiAjou;
	private JMenuItem menuiSupp;
	private JMenuItem menuiDure;
	private JMenuItem menuiOuvr;
	private JMenuItem menuiFerm;

	private Controleur ctrl;
	private JFrame    frame;

	public BarreMenu(Controleur ctrl){

		this.ctrl = ctrl;
		JMenu menuFichier = new JMenu("Fichier");
		JMenu menuEdition = new JMenu("Edition");
		
		this.menuiOuvr   = new JMenuItem("Ouvrir");
		this.menuiSauv   = new JMenuItem("Enregistrer");
		this.menuiFerm = new JMenuItem("Fermer");

		this.menuiAjou   = new JMenuItem("Ajouter");
		this.menuiSupp   = new JMenuItem("Supprimer");
		this.menuiDure   = new JMenuItem("Changer Durée");

		menuFichier.add(this.menuiOuvr);
		menuFichier.add(this.menuiSauv);
		menuFichier.addSeparator();
		menuFichier.add(this.menuiFerm);

		menuEdition.add(this.menuiAjou);
		menuEdition.add(this.menuiSupp);
		menuEdition.addSeparator();
		menuEdition.add(this.menuiDure);

		this.add(menuFichier);
		this.add(menuEdition);

		this.menuiSauv.addActionListener(this);
		this.menuiOuvr.addActionListener(this);
		this.menuiSupp.addActionListener(this);
		this.menuiDure.addActionListener(this);
		this.menuiAjou.addActionListener(this);
		this.menuiFerm.addActionListener(this);
	}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.menuiAjou)
		{
			new FrameAjout(this.ctrl);
		}

		if (e.getSource() == this.menuiOuvr)
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Ouvrir un graphe Mpm : ");
			fileChooser.setCurrentDirectory(new File("./test"));
			
			int result = fileChooser.showOpenDialog(this.getParent());

			if (result == JFileChooser.APPROVE_OPTION)
			{
				File selectedFile = fileChooser.getSelectedFile();
				//Vérifier si il s'agit du bon format de fichier .data, .txt
				if (selectedFile.getName().endsWith(".data") || selectedFile.getName().endsWith(".txt")) 
				{
					this.ctrl.setNouvMetier(selectedFile.getAbsolutePath());
				}
				else
					JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un fichier .data ou .txt", "Format de fichier invalide", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				System.out.println("Aucun fichier sélectionné");
			}
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

		if (e.getSource() == this.menuiFerm)
		{
			this.ctrl.dispose();
		}
	}
}