package main.ihm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import main.metier.Mpm;
import main.ihm.FrameMpm;
import main.metier.Tache;
import main.ihm.BtnPanel;
import main.ihm.MPMGrapheAuto;

public class BtnPanel extends JPanel implements ActionListener {
    private JButton btnTot;
    private JButton btnTar;
    private JButton btnCrt;

    public BtnPanel(){
        this.btnCrt = new JButton("Chemin Critique");
        this.btnTar = new JButton("Plus tard");
        this.btnTot = new JButton("Plus tot");

        this.add(this.btnTot);
        this.add(this.btnTar);
        this.add(this.btnCrt);

        this.btnTar.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.btnTot){}

        if (e.getSource() == this.btnTar){}

        if (e.getSource() == this.btnCrt){}
    }
}
