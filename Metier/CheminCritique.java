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

    public String toString() 
	{
        StringBuilder sb = new StringBuilder();
        sb.append("Chemin Critique (Durée: ").append(getDureeTotale()).append(" jours) : ");
        for (int i = 0; i < tachesCritiques.size(); i++) {
            Tache t = tachesCritiques.get(i);
            
            if (!t.getNom().equals("Debut") && !t.getNom().equals("Fin")) 
                 sb.append(t.getNom()).append("(").append(t.getDuree()).append(")");
            else 
                 sb.append(t.getNom());

            if (i < tachesCritiques.size() - 1)
                sb.append(" -> ");
        }
        return sb.toString();
    }
}