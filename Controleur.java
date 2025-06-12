package exFinal;
import Ihm.FrameMpm;
import Metier.CheminCritique;
import Metier.Mpm;
import Metier.Tache;
import java.util.ArrayList;


public class Controleur

{
	private  Mpm      metier;
	private  FrameMpm ihm;

	public Controleur() 
	{
		this.metier = new Mpm();
		this.ihm    = new FrameMpm(this);
	}

	public void setNouvMetier(String fichier)
	{
		this.metier = new Mpm(fichier,"01/01/2025");
		this.ihm.enableBtn();
		
		this.majIhm();

		ihm.getMpmGraphe().resetEtape();
		ihm.getBtnPanel ().resetBtn  ();

	}

	public ArrayList<Tache>          getTaches()                                        {return metier.getTaches();               }
	public ArrayList<CheminCritique> getCheminCritiques()                               {return this.metier.getCheminsCritiques();}
	public Tache                     chercherTacheParNom(String nom)                    {return metier.chercherTacheParNom(nom);  }
	public void                      majIhm()                                           {this.ihm.majIhm();                       } 
	public String                    getDateDebut()                                     {return this.metier.getDateDebut();       }
	public String                    getErreur()                                        {return this.metier.getErreur();          }
	
	public boolean valeursValides (String nom, String duree, String ant, String svt)    {return this.metier.valeursValides(nom, duree, ant, svt);}
	public void    ajouterTache   (String nom, String prc  , String svt, int duree )    {this.metier.ajouterTache(nom, prc,svt, duree);          }
	public void    supprimerTache (String nom)                                          {this.metier.supprimerTache(nom);                        }
	
	public void    dispose        ()                                                    {this.ihm.dispose();}
	
	public boolean enregistrer(String cheminAbsolue) 
	{
		String infosNoeuds;

		infosNoeuds = this.ihm.getInfos();

		return this.metier.enregistrer(infosNoeuds, cheminAbsolue);
	}

	    public void setEnDate(){this.ihm.setEnDate();}

	public void setDure(int val, Tache tache)
	{
		this.metier.setDure(val, tache);
		this.majIhm();
	}

	public void setDateDebut(String dateDebut, String dateFin)
	{
		this.metier.setDateDebut(dateDebut, dateFin);
		this.majIhm();
	}

	public boolean dateValide(String date)
	{
		return this.metier.dateValide(date);
	}

	public static void main(String[] args)
	{
		Controleur ctrl = new Controleur();	
	}
}
