package exFinal;
import Ihm.FrameMpm;
import Metier.CheminCritique;
import Metier.Mpm;
import Metier.Tache;
import java.util.ArrayList;


public class Controleur
{
	private Mpm metier;
	private FrameMpm ihm;

	public Controleur() 
	{
		this.metier = new Mpm("./test/test3.txt", "02/06/2024");
		this.ihm    = new FrameMpm(this);	
	}

	public ArrayList<Tache>          getTaches()                     {return metier.getTaches();}
	public ArrayList<CheminCritique> getCheminCritiques()            {return this.metier.getCheminsCritiques();}
	public Tache                     chercherTacheParNom(String nom) {return metier.chercherTacheParNom(nom);}

	public static void main(String[] args)
	{
		Controleur ctrl = new Controleur();		
	}
}
