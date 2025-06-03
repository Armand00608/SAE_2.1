import java.io.File; // Ou FileInputStream si Decomposeur le nécessite
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Mpm 
{
	// Liste de toutes les tâches du projet
	private ArrayList<Tache> taches = new ArrayList<Tache>();
	private String dateDebut;

	// Constructeur : charge le fichier et initialise les tâches
	public Mpm(String nomFichier, String dateDebut) 
	{
		this.dateDebut = dateDebut;
		
		List<String[]> lignesPourDependances = new ArrayList<String[]>();

		try (Scanner sc = new Scanner(new File(nomFichier))) 
		{
			String ligneEnCours;

			// Première passe : lire le fichier, créer les tâches
			while (sc.hasNextLine()) 
			{
				ligneEnCours = sc.nextLine().trim();
				if (!ligneEnCours.isEmpty()) 
				{
					
					String[] partsBruts = ligneEnCours.split("\\|"); 
					

					if (partsBruts.length >= 2) 
					{
						String nomTache = partsBruts[0].trim();
						
						int duree = Integer.parseInt(partsBruts[1].trim());

						Tache t = new Tache(nomTache, duree);
						this.taches.add(t);

						lignesPourDependances.add(partsBruts);
					}
				}
			}

			this.taches.add(0, new Tache("Debut", 0));
			this.taches.add(new Tache("Fin", 0));



			// Deuxième passe : Ajouter les dépendances entre les tâches
			for (String[] parts : lignesPourDependances) 
			{
				
				if (parts.length > 2 && parts[2] != null && !parts[2].trim().isEmpty()) 
				{
					Tache courant = chercherTacheParNom(parts[0].trim());
					
					
					String[] nomsPrecedents = parts[2].trim().split(",");
					
					for (String nomPre : nomsPrecedents) 
					{
						Tache precedent = chercherTacheParNom(nomPre.trim());
						if (precedent != null && courant != null) 
						{
							courant.ajouterPrecedent(precedent);
						}
					}
				}
			}

			for (Tache t : this.taches) 
			{
				if (t.getPrecedents().isEmpty() && !t.getNom().equals("Fin") && !t.getNom().equals("Debut"))
				{
					t.ajouterPrecedent(chercherTacheParNom("Debut"));
				}

				if (t.getSuivants().isEmpty() && !t.getNom().equals("Fin") && !t.getNom().equals("Debut"))
				{
					t.ajouterSuivant(chercherTacheParNom("Fin"));
				}
			}

			// Calculer les dates au plus tôt et au plus tard
			calculerDates();

		} 
		catch (Exception e) 
		{
			System.out.println("Erreur lecture fichier : " + e.getMessage());
			// e.printStackTrace();
		}
	}

	// Chercher une tâche dans la liste à partir de son nom
	private Tache chercherTacheParNom(String nom) 
	{
		for (Tache t : this.taches) 
		{
			if (t.getNom().equals(nom)) 
			{
				return t;
			}
		}
		return null; 
	}

	// Calculer les dates plus tôt et plus tard pour chaque tâche
	private void calculerDates() 
	{
		// Calculer les dates au plus tôt
		for (Tache t : this.taches) 
		{
			t.calculerDatePlusTot();
		}

		// Trouver la date de fin du projet (maximum de date + durée)
		int finProjet = 0;
		for (Tache t : this.taches) 
		{
			if (t.getSuivants().isEmpty()) 
			{
				int finTache = t.getDatePlusTot() + t.getDuree();
				if (finTache > finProjet) {
					finProjet = finTache;
				}
			}
		}

		// Initialiser les tâches finales avec leur date au plus tard
		for (Tache t : this.taches) 
		{
			if (t.getSuivants().isEmpty()) 
			{
				t.setDatePlusTard(finProjet - t.getDuree());
			}
		}

		// Calculer les dates au plus tard pour les autres tâches
		for (int i = this.taches.size() - 1; i >= 0; i--) 
		{ // Itérer en sens inverse peut aider
			Tache t = this.taches.get(i);
			if (!t.getSuivants().isEmpty()) 
			{
				t.calculerDatePlusTard();
			}
		}
	}

	public ArrayList<Tache> getTaches() {
		return this.taches;
	}

	// Affichage des résultats
	public String toString() 
	{
		String texte = "=== ANALYSE MPM ===\n\n";
		for (Tache t : this.taches) 
		{
			texte += t.toString(dateDebut) + "\n";
		}
		return texte;
	}
}
