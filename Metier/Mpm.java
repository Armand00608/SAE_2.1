package exFinal.Metier;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Mpm 
{
	// Liste de toutes les tâches du projet
	private ArrayList<CheminCritique> cheminsCritiques = new ArrayList<>();
	private ArrayList<Tache> taches = new ArrayList<Tache>();
	private String dateDebut;
	private String nomFichier;

	// Constructeur : charge le fichier et initialise les tâches
	public Mpm(String nomFichier, String dateDebut) 
	{
		this.dateDebut = dateDebut;
		this.nomFichier = nomFichier;
		
		this.chargerFichier();
	}


	private static class EtatExploration 
	{
		Tache tacheActuelle;
		ArrayList<Tache> cheminJusquaIci;

		EtatExploration(Tache tacheActuelle, ArrayList<Tache> cheminJusquaIci) {
			this.tacheActuelle = tacheActuelle;
			this.cheminJusquaIci = cheminJusquaIci;
		}
	}

	public ArrayList<CheminCritique> getCheminsCritiques() {return this.cheminsCritiques;}

	private void creerCheminCritique() 
	{
		this.cheminsCritiques.clear();
		Tache tacheDebut = chercherTacheParNom("Debut");

		if (tacheDebut == null || !tacheDebut.estCritique())
			return;

		Stack<EtatExploration> pile = new Stack<>();
		ArrayList<Tache> cheminInitial = new ArrayList<>();
		cheminInitial.add(tacheDebut);
		pile.push(new EtatExploration(tacheDebut, cheminInitial));

		while (!pile.isEmpty()) 
		{
			EtatExploration etatCourant = pile.pop();
			Tache tacheActuelle = etatCourant.tacheActuelle;
			ArrayList<Tache> cheminActuel = etatCourant.cheminJusquaIci;

			if (tacheActuelle.getNom().equals("Fin")) 
			{
				CheminCritique cp = new CheminCritique();
				for (Tache t : cheminActuel)
					cp.ajouterTache(t);

				cp.setDureeTotale(tacheActuelle.getDatePlusTot());
				this.cheminsCritiques.add(cp);
			}
			else 
			{
				List<Tache> succCritiques = new ArrayList<>();
				for (Tache successeur : tacheActuelle.getSuivants()) 
				{
					if (successeur.estCritique())
						succCritiques.add(successeur);
				}

				for (int i = succCritiques.size() - 1; i >= 0; i--) 
				{
					Tache successeur = succCritiques.get(i);
					ArrayList<Tache> prochainChemin = new ArrayList<>(cheminActuel);
					prochainChemin.add(successeur);
					pile.push(new EtatExploration(successeur, prochainChemin));
				}
			}
		}
	}


	// Chercher une tâche dans la liste à partir de son nom
	public Tache chercherTacheParNom(String nom) 
	{
		for (Tache t : this.taches) 
		{
			if (t.getNom().equals(nom)) 
				return t;
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

	public void ajouterTache(String nom, String prc, String duree)
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.nomFichier, true))) {
			writer.newLine();
			writer.write(nom + '|' + duree + '|' + prc);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.chargerFichier();
	}


	public ArrayList<Tache> getTaches() {
		return this.taches;
	}

	public void chargerFichier()
	{
		List<String[]> lignesPourDependances = new ArrayList<String[]>();

		this.taches.clear();
		this.cheminsCritiques.clear();

		try (Scanner sc = new Scanner(new File(this.nomFichier)))
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
				if (parts.length > 2 && !parts[2].trim().isEmpty()) {
					Tache courant = chercherTacheParNom(parts[0].trim());

					for (String nomPre : parts[2].trim().split(","))
					{
						Tache precedent = chercherTacheParNom(nomPre.trim());
						if (precedent != null)
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

			//Creer le(s) chemin(s) critique(s)
			creerCheminCritique();


		}
		catch (Exception e)
		{
			System.out.println("Erreur lecture fichier : " + e.getMessage());
			// e.printStackTrace();
		}
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
