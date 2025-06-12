package Ihm;


import Metier.Erreur;
import exFinal.Controleur;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BtnPanel extends JPanel implements ActionListener 
{

	private JPanel panelDte;
	private JPanel panelClcDte;

	private JButton btnTot;
	private JButton btnTar;
	private JButton btnCrt;

	private JButton btnDteValider;

	private ButtonGroup btnGroupDte;
	private JRadioButton rbDteDebut;
	private JRadioButton rbDteFin;

	private JTextField txtDte;


	private MPMGrapheAuto graphe;
	private Controleur ctrl;

	public BtnPanel(MPMGrapheAuto graphe, Controleur ctrl) 
	{
		this.graphe = graphe;
		this.ctrl = ctrl;
		
		this.btnCrt = new JButton("Chemin Critique");
		this.btnTar = new JButton("Plus tard");
		this.btnTot = new JButton("Plus tot");
		this.rbDteDebut = new JRadioButton("Date de début");
		this.rbDteFin = new JRadioButton("Date de fin");
		this.btnGroupDte = new ButtonGroup();
		this.txtDte = new JTextField();

		this.btnDteValider = new JButton("Valider Date");
		
		this.panelDte = new JPanel();
		this.panelClcDte = new JPanel();

		this.setLayout(new GridLayout(1, 2));
		this.panelDte.setLayout(new GridLayout(2, 1, 10, 10));
		this.panelClcDte.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		this.btnGroupDte.add(this.rbDteDebut);
		this.btnGroupDte.add(this.rbDteFin);
		
		this.rbDteDebut.setSelected(false);
		this.rbDteFin.setSelected(false);
		this.btnDteValider.setEnabled(false);
		
		this.txtDte.setToolTipText("Entrez une date au format jj/mm/aaaa");
		this.txtDte.setEnabled(false);
		
		this.btnTar.setEnabled(false);
		this.btnCrt.setEnabled(false);

		JPanel panelrb = new JPanel(new GridLayout(1, 2, 10, 10));
		
		this.panelClcDte.add(this.btnTot);
		this.panelClcDte.add(this.btnTar);
		this.panelClcDte.add(this.btnCrt);

		panelrb.add(this.rbDteDebut);
		panelrb.add(this.rbDteFin);
		panelrb.add(this.btnDteValider);
		this.panelDte.add(panelrb);
		this.panelDte.add(this.txtDte);

		this.add(this.panelDte);
		this.add(this.panelClcDte);

		this.btnTot.addActionListener(this);
		this.btnTar.addActionListener(this);
		this.btnCrt.addActionListener(this);
		this.rbDteDebut.addActionListener(this);
		this.rbDteFin.addActionListener(this);
		this.txtDte.addActionListener(this);
		this.btnDteValider.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == this.btnTot)
		{
			 if (!graphe.AugmenterEtapePlusTotMax())
			{
				this.btnTot.setEnabled(false);
				this.btnTar.setEnabled(true);
			}
		}

		if (e.getSource() == this.btnTar)
		{
			if (!graphe.AugmenterEtapePlusTarMax())
			{
				this.btnTar.setEnabled(false);
				this.btnCrt.setEnabled(true);
			}

		}

		if (e.getSource() == this.btnCrt)
			this.graphe.activerChemin();
		
		if (e.getSource() == this.rbDteDebut)
		{
			this.txtDte.setEnabled(true);
			this.txtDte.setText("");
			this.txtDte.requestFocus();
		}
		if (e.getSource() == this.rbDteFin)
		{
			this.txtDte.setEnabled(true);
			this.txtDte.setText("");
			this.txtDte.requestFocus();
		}
		if (e.getSource() == this.txtDte)
		{
			// Supprimer cette validation - laisser le contrôleur s'en charger
			String date = this.txtDte.getText();
			if (!date.isEmpty())
			{
				this.btnDteValider.setEnabled(true);
			}
			else
			{
				this.btnDteValider.setEnabled(false);
			}
		}
		if (e.getSource() == this.btnDteValider)
		{
			String date = this.txtDte.getText();
			if (this.ctrl.dateValide(date))
			{
				if (this.rbDteDebut.isSelected())
				{
					this.ctrl.setDateDebut(date, null);
				}
				else if (this.rbDteFin.isSelected())
				{
					this.ctrl.setDateDebut(null, date);
				}
				this.graphe.majIhm();
				this.txtDte.setText("");
				this.txtDte.setEnabled(false);
				this.btnDteValider.setEnabled(false);
				this.rbDteDebut.setSelected(false);
				this.rbDteFin.setSelected(false);
			}
			else
			{
				JOptionPane.showMessageDialog(this, this.ctrl.getErreur(), "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
		if ( this.btnGroupDte.getSelection() == null || this.txtDte.getText().isEmpty() )
		{
			this.rbDteDebut.setSelected(false);
			this.rbDteFin.setSelected(false);
			this.txtDte.setText("");
			this.btnDteValider.setEnabled(false);
		}
	}

	public void resetBtn() 
	{
		this.btnTot.setEnabled(true );
		this.btnTar.setEnabled(false);
		this.btnCrt.setEnabled(false);
	}
}
