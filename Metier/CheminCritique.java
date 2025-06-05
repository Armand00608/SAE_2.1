package Metier;

import java.util.ArrayList;
import java.util.List;

public class CheminCritique 
{
	private List<Tache> tachesCritiques;
	private int dureeTotale;

	public CheminCritique() 
	{
		this.tachesCritiques = new ArrayList<>();
		this.dureeTotale = 0;
	}

	
	public void ajouterTache(Tache tache) 
	{
		if (tache != null)
			this.tachesCritiques.add(tache);
	}

	public List<Tache> getTachesCritiques() { return new ArrayList<>(this.tachesCritiques);}

	public void setDureeTotale(int duree) {this.dureeTotale = duree;}

	public int getDureeTotale() 
	{
		
		if (this.dureeTotale > 0) 
			return this.dureeTotale;
		
		if (!tachesCritiques.isEmpty()) 
		{
			Tache derniereTache = tachesCritiques.get(tachesCritiques.size() - 1);
			if (derniereTache.getNom().equals("Fin"))
				return derniereTache.getDatePlusTot();
		}
		return 0;
	}

	public String toString() {
		if (tachesCritiques == null || tachesCritiques.isEmpty()) {
			return "Chemin Critique : Vide";
		}

		String resultat = "Détail du Chemin Critique (Durée totale : " + getDureeTotale() + " jours) :\n";
		
		for (Tache t : tachesCritiques) {
			if (t.getNom().equals("Debut")) {
				resultat += "  -> Point de départ : " + t.getNom() + "\n";
				continue;
			}
			
			resultat += "     Ensuite : ";
			resultat += t.getNom();
			if (t.getDuree() > 0) {
				 resultat += " [" + t.getDuree() + (t.getDuree() > 1 ? " jours" : " jour") + "]";
			}
			resultat += "\n";
		}
		return resultat;
	}
}