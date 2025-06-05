package Ihm;

public class Noeud
{
	String nom;
	int tot, tard;
	int x, y;
	int col;
	boolean estChemin = false;

	Noeud(String nom, int tot, int tard, int col, boolean estChemin)
	{
		this.nom = nom;
		this.tot = tot;
		this.tard = tard;
		this.col = col;
		this.estChemin = estChemin;
	}

	public int getCol()    {return col;}
	public String getNom() {return nom;}
	public boolean getEstChemin() {return estChemin;}
    public void setEstChemin(boolean estChemin) {this.estChemin = estChemin;}
}