package Ihm;
import exFinal.Controleur;
import java.awt.*;
import javax.swing.*;

public class FrameMpm extends JFrame 
{
	private Controleur ctrl;

	private MPMGrapheAuto graphPanel;
	private BtnPanel btnPanel;

	public FrameMpm(Controleur ctrl) 
	{
		this.ctrl = ctrl;

		this.setTitle("Diagramme MPM");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 700);
		this.setLayout(new BorderLayout());

	
		this.graphPanel = new MPMGrapheAuto(ctrl);
		JScrollPane scrollFrame = new JScrollPane(this.graphPanel,
		                                          JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		                                          JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.add(scrollFrame, BorderLayout.CENTER);

		JMenuBar menubMaBarre = new BarreMenu(this.ctrl);
		this.setJMenuBar( menubMaBarre );

		this.btnPanel = new BtnPanel(this.graphPanel);
		this.btnPanel.setVisible(false);
		this.add(this.btnPanel, BorderLayout.SOUTH);

		this.setVisible(true);
	}

	public void majIhm(){this.graphPanel.majIhm();}

	public void enableBtn(){this.btnPanel.setVisible(true);}

	public String getInfos()
	{
		return this.graphPanel.getInfos();
	}

	

	public MPMGrapheAuto getMpmGraphe() {return this.graphPanel;}
	public BtnPanel      getBtnPanel () {return this.btnPanel  ;}
}