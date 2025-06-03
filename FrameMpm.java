import java.awt.*;
import javax.swing.*;

public class FrameMpm extends JFrame {
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
        this.add(this.graphPanel, BorderLayout.CENTER);

		 this.btnPanel = new BtnPanel();
        this.add(this.btnPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }
}