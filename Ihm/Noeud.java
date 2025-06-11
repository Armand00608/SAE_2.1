package Ihm;

import exFinal.Controleur;

public class Noeud
{
	private String nom;
	private int tot, tard;
	private int x, y;
	private int col, lig;
	private int nbPre, nbSvt;
	private int milieu;
	private boolean estChemin;
	private Controleur ctrl;

	Noeud(String nom, int tot, int tard, int col, boolean estChemin, Controleur ctrl)
	{
		this.ctrl      = ctrl;
		this.nom       = nom;
		this.tot       = tot;
		this.tard      = tard;
		this.col       = col;
		this.estChemin = estChemin;
		this.nbPre     = ctrl.chercherTacheParNom(this.getNom()).getPrecedents().size();
		this.nbSvt     = ctrl.chercherTacheParNom(this.getNom()).getSuivants().size();
	}

	public int              getX()         {return         x;}
	public int 	            getY()         {return         y;}
	public int              getTot()       {return       tot;}
	public int              getTard()      {return      tard;}
	public int              getCol()       {return       col;}
	public int              getLig()       {return       lig;}
	public String           getNom()       {return       nom;}
	public boolean          getEstChemin() {return estChemin;}
	public int              getNbPre()     {return     nbPre;}
	public int              getNbSvt()     {return     nbSvt;}
	public int              getMil()       {return    milieu;}

	public void setX        (int x)             {this.x         =         x;}
	public void setY        (int y)             {this.y         =         y;}
	public void setLig      (int lig)           {this.lig       =       lig;}
	public void setMil      (int milieu)        {this.milieu    =    milieu;}
	public void setEstChemin(boolean estChemin) {this.estChemin = estChemin;}
}