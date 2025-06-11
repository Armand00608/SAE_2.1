package Ihm;

import Metier.Erreur;
import Metier.Tache;
import exFinal.Controleur;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PanelNoeudInfo extends JPanel implements ActionListener
{
	private Tache      tache;
	private Controleur ctrl;

	private JButton    btnSupr;
	private JTextField txtDure;

	public PanelNoeudInfo(Tache tache, Controleur ctrl)
	{
		this.tache = tache;
		this.ctrl  = ctrl;

		JPanel panelBas, panelInfo;
		String precedents = "";
		String suivants   = "";

		this.setLayout(new BorderLayout());

		if (tache.getPrecedents().isEmpty())
			precedents = "Aucun";
		else {
			for (Tache t : tache.getPrecedents())
				precedents += t.getNom() + ", ";
			if (!precedents.isEmpty())
				precedents = precedents.substring(0, precedents.length() - 2);
		}

		if (tache.getSuivants().isEmpty())
			suivants = "Aucun";
		else {
			for (Tache t : tache.getSuivants())
				suivants += t.getNom() + ", ";
			if (!suivants.isEmpty())
				suivants = suivants.substring(0, suivants.length() - 2);
		}

		panelInfo = new JPanel(new GridLayout(0, 2, 10, 5));
		panelBas  = new JPanel();

		this.txtDure = new JTextField(String.valueOf(tache.getDuree()));


		panelInfo.add(new JLabel("Nom :", JLabel.RIGHT));
		panelInfo.add(new JLabel(tache.getNom(), JLabel.LEFT));

		panelInfo.add(new JLabel("Durée :", JLabel.RIGHT));
		panelInfo.add(this.txtDure);

		panelInfo.add(new JLabel("Précédents :", JLabel.RIGHT));
		panelInfo.add(new JLabel(precedents, JLabel.LEFT));

		panelInfo.add(new JLabel("Suivants :", JLabel.RIGHT));
		panelInfo.add(new JLabel(suivants, JLabel.LEFT));

		panelInfo.add(new JLabel("Jour début (plus tôt) :", JLabel.RIGHT));
		panelInfo.add(new JLabel(String.valueOf(tache.ajouterJours(ctrl.getDateDebut(), tache.getDatePlusTot())), JLabel.LEFT));

		panelInfo.add(new JLabel("Jour fin (plus tard) :", JLabel.RIGHT));
		panelInfo.add(new JLabel(String.valueOf(tache.ajouterJours(ctrl.getDateDebut(), tache.getDatePlusTard())), JLabel.LEFT));

		this.add(panelInfo, BorderLayout.CENTER);

		if (!tache.getNom().equals("Debut") && !tache.getNom().equals("Fin"))
		{
			this.btnSupr = new JButton("Suprimer");
			panelBas.add(btnSupr);
			this.btnSupr.addActionListener(this);
		}

		this.add(panelBas, BorderLayout.SOUTH);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.txtDure.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnSupr)
		{
			this.ctrl.supprimerTache(this.tache.getNom());
			this.ctrl.majIhm();

			//ferme la frame
			Window window = SwingUtilities.getWindowAncestor(this);
			if (window != null) {
				window.dispose();
			}
		}

		if (e.getSource() == this.txtDure)
		{
			try 
			{
				int val = Integer.parseInt(this.txtDure.getText());
				this.ctrl.setDure(val, tache);
			}
			catch (NumberFormatException ex)
			{
				JOptionPane.showMessageDialog(null, Erreur.FORMAT_INVALIDE.getMessage() , "Invalide", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}