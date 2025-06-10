package exFinal.Ihm;

import exFinal.Controleur;
import exFinal.Metier.Tache;
import java.awt.*;
import javax.swing.*;



public class FrameNoeudInfo extends JFrame 
{
	private Controleur ctrl;

	public FrameNoeudInfo(Tache tache,Controleur ctrl) 
	{
		this.ctrl = ctrl;

		setTitle("Informations du nœud");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		String precedents = "";
		String suivants   = "";

		if (tache.getPrecedents().isEmpty())
			precedents = "Aucun";
		else {
			for (Tache t : tache.getPrecedents()) 
			{
				precedents += t.getNom() +", ";
			}
			if (!precedents.isEmpty())
				precedents = precedents.substring(0, precedents.length() - 2);
		}

		if (tache.getSuivants().isEmpty())
			suivants = "Aucun";
		else {
			for (Tache t : tache.getSuivants()) 
			{
				suivants += t.getNom() +", ";
			}
			if (!suivants.isEmpty())
				suivants = suivants.substring(0, suivants.length() - 2);
		}


		JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));

		// Création des labels
		JLabel lblNom        = new JLabel("Nom :");
		JLabel valNom        = new JLabel(tache.getNom());
		JLabel lblDuree      = new JLabel("Durée :");
		JLabel valDuree      = new JLabel(String.valueOf(tache.getDuree()));
		JLabel lblPrecedents = new JLabel("Précédents :");
		JLabel valPrecedents = new JLabel(precedents);
		JLabel lblSuivants   = new JLabel("Suivants :");
		JLabel valSuivants   = new JLabel(suivants);
		JLabel lblDebut      = new JLabel("Jour début (plus tôt) :");
		JLabel valDebut      = new JLabel(String.valueOf(tache.ajouterJours(this.ctrl.getDateDebut(), tache.getDatePlusTot())));
		JLabel lblFin        = new JLabel("Jour fin (plus tard) :");
		JLabel valFin        = new JLabel(String.valueOf(tache.ajouterJours(this.ctrl.getDateDebut(), tache.getDatePlusTard())));

		// Alignement
		lblNom        .setHorizontalAlignment(SwingConstants.RIGHT);
		lblDuree      .setHorizontalAlignment(SwingConstants.RIGHT);
		lblPrecedents .setHorizontalAlignment(SwingConstants.RIGHT);
		lblSuivants   .setHorizontalAlignment(SwingConstants.RIGHT);
		lblDebut      .setHorizontalAlignment(SwingConstants.RIGHT);
		lblFin        .setHorizontalAlignment(SwingConstants.RIGHT);

		valNom        .setHorizontalAlignment(SwingConstants.LEFT);
		valDuree      .setHorizontalAlignment(SwingConstants.LEFT);
		valPrecedents .setHorizontalAlignment(SwingConstants.LEFT);
		valSuivants   .setHorizontalAlignment(SwingConstants.LEFT);
		valDebut      .setHorizontalAlignment(SwingConstants.LEFT);
		valFin        .setHorizontalAlignment(SwingConstants.LEFT);

		// Ajout au panel
		panel.add(lblNom); 
		panel.add(valNom);

		panel.add(lblDuree); 
		panel.add(valDuree);

		panel.add(lblPrecedents);
		panel.add(valPrecedents);

		panel.add(lblSuivants); 
		panel.add(valSuivants);

		panel.add(lblDebut); 
		panel.add(valDebut);

		panel.add(lblFin); 
		panel.add(valFin);


		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(panel, BorderLayout.CENTER);

		pack();
		setLocationRelativeTo(null);
	}
}