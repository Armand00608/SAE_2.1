package exFinal;
import exFinal.Ihm.FrameMpm;
import exFinal.Metier.CheminCritique;
import exFinal.Metier.Mpm;
import exFinal.Metier.Tache;
import java.util.ArrayList;


public class Controleur
{
	private Mpm metier;
	private FrameMpm ihm;

	public Controleur() 
	{
		this.metier = new Mpm("./test/test.txt", "02/06/2024");
		this.ihm    = new FrameMpm(this);
	}

	public ArrayList<Tache>          getTaches()                                        {return metier.getTaches();               }
	public ArrayList<CheminCritique> getCheminCritiques()                               {return this.metier.getCheminsCritiques();}
	public Tache                     chercherTacheParNom(String nom)                    {return metier.chercherTacheParNom(nom);  }
	public void                      majIhm()                                           {this.ihm.majIhm();                       } 
	public String                    getDateDebut()                                     {return this.metier.getDateDebut();       }
	public String                    getErreur()                                        {return this.metier.getErreur();          }
	
	public boolean valeursValides(String nom, String duree, String ant)                 {return this.metier.valeursValides(nom, duree, ant);}
	public void    ajouterTache  (String nom, String prc, String duree)                 {this.metier.ajouterTache(nom, prc, duree);}

	public boolean enregistrer(String cheminAbsolue) 
	{
		String infosNoeuds;

		infosNoeuds = this.ihm.getInfos();

		return this.metier.enregistrer(infosNoeuds, cheminAbsolue);
	}

	public static void main(String[] args)
	{
		Controleur ctrl = new Controleur();	
	}
}
