package exFinal.Ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BtnPanel extends JPanel implements ActionListener {
	private JButton btnTot;
	private JButton btnTar;
	private JButton btnCrt;


	private MPMGrapheAuto graphe;

	public BtnPanel(MPMGrapheAuto graphe)
	{
		this.graphe = graphe;

		this.btnCrt   = new JButton("Chemin Critique");
		this.btnTar   = new JButton("Plus tard");
		this.btnTot   = new JButton("Plus tot");


		this.add(this.btnTot);
		this.add(this.btnTar);
		this.add(this.btnCrt);

		this.btnTar.setEnabled(false);
		this.btnCrt.setEnabled(false);

		this.btnTot.addActionListener(this);
		this.btnTar.addActionListener(this);
		this.btnCrt.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
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
	}
}
