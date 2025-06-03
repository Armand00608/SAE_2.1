
import java.util.ArrayList;


public class Controleur
{
	private Mpm metier;
	private FrameMpm ihm;

	public Controleur() 
	{
		this.metier = new Mpm("test.txt", "02/06/2024");
		this.ihm    = new FrameMpm(this);	
	}

	public ArrayList<Tache> getTaches(){return metier.getTaches();}

    public static void main(String[] args)
    {
       Controleur ctrl = new Controleur();
       System.out.println(ctrl.metier);
    }
}
