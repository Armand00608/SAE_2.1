package exFinal.Metier;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Mpm 
{
	// Liste de toutes les tâches du projet
	private ArrayList<CheminCritique> cheminsCritiques = new ArrayList<>();
	private ArrayList<Tache> taches = new ArrayList<Tache>();
	private String dateDebut;
	private String nomFichier;
	private String msgErreur;

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

	    ArrayList<EtatExploration> liste = new ArrayList<>();
	    ArrayList<Tache> cheminInitial = new ArrayList<>();
	    cheminInitial.add(tacheDebut);
	    liste.add(new EtatExploration(tacheDebut, cheminInitial));

	    while (!liste.isEmpty()) 
	    {
	        EtatExploration etatCourant = liste.remove(liste.size() - 1); // Équivalent de pop()
	        Tache tacheActuelle = etatCourant.tacheActuelle;
	        ArrayList<Tache> cheminActuel = etatCourant.cheminJusquaIci;

	        if (tacheActuelle.getNom().equals("Fin")) 
	        {
	            CheminCritique cTemp = new CheminCritique();
	            for (Tache t : cheminActuel)
	                cTemp.ajouterTache(t);

	            cTemp.setDureeTotale(tacheActuelle.getDatePlusTot());
	            this.cheminsCritiques.add(cTemp);
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
	                liste.add(new EtatExploration(successeur, prochainChemin));
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

	
	public boolean enregistrer(String infosNoeuds, String cheminAbsolu) 
	{
		boolean bRet;
		try
		{
			PrintWriter pw = new PrintWriter( new FileOutputStream(cheminAbsolu ) );

			pw.println ( infosNoeuds );

			bRet = true;

			pw.close();
		}
		catch (Exception e)
		{ 
			bRet = false;
			e.printStackTrace(); 
		}
		return bRet;
	}

	public ArrayList<Tache> getTaches() {
		return this.taches;
	}

	public boolean valeursValides(String nom, String duree, String ant)
	{
		boolean bRet = true;
		this.msgErreur = "";

		if (nom == null || nom.trim().isEmpty() ) 
		{
			this.msgErreur += "Nom de tâche non saisi\n";
			return false;
		}

		// si la tâche existe déjà
		for (Tache t : this.taches)
		{
			if (t.getNom().equals(nom.trim()))
			{
				this.msgErreur += "Une tâche avec ce nom existe déjà\n";
				return false;
			}
		}

		// le nom ne doit pas contenir les delemiteurs | et , dans son nom
		if (nom.contains("|") || nom.contains(","))
		{
			this.msgErreur += "Le nom de tâche ne peut pas contenir les caractères '|' ou ',' \n";
			return false;
		}


		if (duree == null || duree.trim().isEmpty())
		{
			this.msgErreur += "Durée non saisie\n";
			return false;
		}
		
		try 
		{
			int duration = Integer.parseInt(duree.trim());
			if (duration <= 0)
			{
				this.msgErreur += "La durée doit être strictement supérieure à 0\n";
				return false;
			}
		} 
		catch (NumberFormatException e) 
		{
			this.msgErreur += "La durée doit être un nombre entier\n";
			return false;
		}



		
		if (bRet && ant != null && !ant.trim().isEmpty()) {
			String nomTacheActuelle = (nom != null) ? nom.trim() : "";
			String[] antecedents = ant.trim().split(",");

			for (String unAntecedent : antecedents) {
				unAntecedent = unAntecedent.trim();
				if (unAntecedent.isEmpty()) continue; // Ignorer les virgules en trop (ex: "A,,B")

				
				if (unAntecedent.equals("Fin")) 
				{
					this.msgErreur += "La tâche 'Fin' ne peut pas être un prédécesseur.\n";
					return false;
				}
				
				if (unAntecedent.equals("Debut")) 
				{
					this.msgErreur += "La tâche 'Debut' ne doit pas être spécifiée comme prédécesseur.\n";
					return false;
				}

				if (!nomTacheActuelle.isEmpty() && unAntecedent.equals(nomTacheActuelle)) 
				{
					this.msgErreur += "La tâche '" + nomTacheActuelle + "' ne peut pas dépendre d'elle-même.\n";
					return false;
				}

				boolean trouve = false;
				for (Tache tExistante : this.taches) 
				{
					if (tExistante.getNom().equals(unAntecedent)) 
					{
						trouve = true;
						break;
					}
				}
				if (!trouve && !unAntecedent.equals("Debut") && !unAntecedent.equals("Fin")) 
				{
					this.msgErreur += "Le prédécesseur spécifié '" + unAntecedent + "' n'existe pas.\n";
					return false;
				}

				// 5. Dépendance cyclique (plus complexe)
			}
		} 
		// else if (ant != null && !ant.trim().isEmpty() && !bRet) 
		// {
		// 	// Si bRet est déjà faux à cause du nom ou de la durée,
		// 	// on peut quand même mentionner que les prédécesseurs n'ont pas été vérifiés en détail.
		// 	// this.msgErreur += "Validation des prédécesseurs sautée car le nom ou la durée de la tâche est invalide.\n";
		// }





		return bRet;
	}

	public String getErreur()
	{
		return this.msgErreur;
	}

	public String getDateDebut() {return this.dateDebut;}

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
