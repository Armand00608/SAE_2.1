package Ihm;

import exFinal.Controleur;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

public class PanelAjout extends JPanel implements ActionListener {

	private JTextField txtNom;
	private JTextField txtDure;
	private JTextField txtPrec;
	private JTextField txtSvt;

	private JButton    btnSubmit;

	private Controleur ctrl;

	public PanelAjout(Controleur ctrl){
		this.ctrl = ctrl;

		this.setLayout(new GridLayout(11,1));

		this.txtNom  = new JTextField(30);
		this.txtDure = new JTextField(30);
		this.txtPrec = new JTextField(30);
		this.txtSvt  = new JTextField(30);

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

		this.add(new JLabel("   Suivant   (ex: C, D) : "));

		panelTemp = new JPanel();
		panelTemp.add(this.txtSvt);
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

	private String buildWord(List<Character> chars) {
		StringBuilder builder = new StringBuilder();
		for (Character c : chars) {
			builder.append(c);
		}
		return builder.toString();
	}


	public void actionPerformed(ActionEvent e) 
	{
		if ( this.ctrl.valeursValides(this.txtNom.getText(), this.txtDure.getText(), this.txtPrec.getText(), this.txtSvt.getText()))
		{
			this.ctrl.ajouterTache(this.txtNom.getText(), this.txtPrec.getText(),this.txtSvt.getText(), Integer.parseInt(this.txtDure.getText()));
			
			this.ctrl.majIhm();
	
			Window fenetre = SwingUtilities.getWindowAncestor(this);
			if (fenetre != null) 
				fenetre.dispose();
		}
		else
		{
			JOptionPane.showMessageDialog(null, this.ctrl.getErreur(), " Invalide", JOptionPane.ERROR_MESSAGE);
		}

	}
}
