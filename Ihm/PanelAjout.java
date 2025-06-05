package Ihm;

import exFinal.Controleur;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class PanelAjout extends JPanel implements ActionListener 
{

	private JTextField txtNom;
	private JTextField txtDure;
	private JTextField txtPrec;
	private JButton    btnSubmit;
	private Controleur ctrl;

	public PanelAjout(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setLayout(new GridLayout(9,1));

		this.txtNom = new JTextField(30);
		this.txtDure = new JTextField(30);
		this.txtPrec = new JTextField(30);

		this.btnSubmit = new JButton("Valider");

		JPanel panelTemp = new JPanel();
		JLabel label = new JLabel("Ajouter une Tache");
		label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 22));
		panelTemp.add(label);
		this.add(panelTemp);


		this.add(new JLabel("   Nom de la Tache (30 caractère max) : "));

		panelTemp = new JPanel();
		panelTemp.add(this.txtNom);
		this.add(panelTemp);

		this.add(new JLabel("   Précedent (ex: A, B) : "));

		panelTemp = new JPanel();
		panelTemp.add(this.txtPrec);
		this.add(panelTemp);

		this.add(new JLabel("   Durée (en jour) : "));

		panelTemp = new JPanel();
		panelTemp.add(this.txtDure);
		this.add(panelTemp);

		panelTemp = new JPanel();
		panelTemp.add(this.btnSubmit);
		this.add(panelTemp);

		this.add(new JLabel());

		btnSubmit.addActionListener(this);

	}

	private String buildWord(List<Character> chars) 
	{
		StringBuilder builder = new StringBuilder();
		for (Character c : chars) {
			builder.append(c);
		}
		return builder.toString();
	}


	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if ( this.ctrl.valeursValides(this.txtNom.getText(), this.txtDure.getText(), this.txtPrec.getText()))
		{

		}
		else
		{
			JOptionPane.showMessageDialog(null, this.ctrl.getErreur(), " Invalide", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		// if (this.txtNom.getText().trim().isEmpty() || this.txtDure.getText().trim().isEmpty()) 
		// {
		// 	JOptionPane.showMessageDialog(null, "Veuillez écrire quelque chose", " Invalide", JOptionPane.ERROR_MESSAGE);
		// 	return;
		// }

		// if (!this.txtDure.getText().matches("\\d+")) 
		// {
		// 	JOptionPane.showMessageDialog(null, "Veuillez écrire un nombre pour la durée", "Invalide", JOptionPane.ERROR_MESSAGE);
		// 	return;
		// }

		// if (this.txtNom.getText().length() > 30)
		// {
		// 	JOptionPane.showMessageDialog(null, "Nom trop long", "Invalide", JOptionPane.ERROR_MESSAGE);
		// 	return;
		// }


		// List<Character> tabChar = new ArrayList<>();
		// for (Character car : this.txtPrec.getText().toCharArray()) 
		// {
		// 	if (!Character.isLetter(car) && car != ',' && car != ' ') 
		// 	{
		// 		JOptionPane.showMessageDialog(null, "Caractère invalide", "Invalide", JOptionPane.ERROR_MESSAGE);
		// 		return;
		// 	}


		// 	if (car == ',') 
		// 	{
		// 		String mot = buildWord(tabChar).trim();
		// 		tabChar.clear();
		// 		if (!mot.isEmpty() && this.ctrl.chercherTacheParNom(mot) == null) 
		// 		{
		// 			JOptionPane.showMessageDialog(null, "Tâche \"" + mot + "\" inexistante", "Erreur", JOptionPane.ERROR_MESSAGE);
		// 			return;
		// 		}

		// 		if (mot.isEmpty()) 
		// 		{
		// 			JOptionPane.showMessageDialog(null, "Nom de tâche vide entre les virgules", "Erreur", JOptionPane.ERROR_MESSAGE);
		// 			return;
		// 		}

		// 	}
		// 	else 
		// 	{
		// 		tabChar.add(car);
		// 	}
		// }

		// if (!tabChar.isEmpty()) 
		// {
		// 	String mot = buildWord(tabChar).trim();
		// 	if (!mot.isEmpty() && this.ctrl.chercherTacheParNom(mot) == null) 
		// 	{
		// 		JOptionPane.showMessageDialog(null, "Tâche \"" + mot + "\" inexistante", "Erreur", JOptionPane.ERROR_MESSAGE);
		// 		return;
		// 	}
		// }


	}
}
