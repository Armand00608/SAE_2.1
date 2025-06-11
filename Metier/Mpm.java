package Metier;

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
	public Mpm() 
	{
		this.cheminsCritiques = new ArrayList<CheminCritique>();
		this.taches = new ArrayList<Tache>();
	}


	public Mpm(String nomFichier, String dateDebut)
	{
		this.dateDebut = dateDebut;
		this.nomFichier = nomFichier;
		this.cheminsCritiques = new ArrayList<CheminCritique>();
		this.taches = new ArrayList<Tache>();

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
	public void calculerDates() 
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

	public void ajouterTache(String nom, String prc, String svt, int duree)
		{
			Tache tNew  = new Tache(nom, duree);

			// Ajouter les précédents
			if (prc != null && !prc.trim().isEmpty()) {
				String[] precedents = prc.split(",");
				for (String nomPre : precedents) {
					nomPre = nomPre.trim();
					if (!nomPre.isEmpty()) {
						Tache precedent = chercherTacheParNom(nomPre);
						if (precedent != null) {
							tNew.ajouterPrecedent(precedent);
						}
					}
				}
			}
			else
			{
				Tache Debut = chercherTacheParNom("Debut");
				tNew.ajouterPrecedent(Debut);
			}


			// Ajouter les suivants
			if (svt != null && !svt.trim().isEmpty()) 
			{
				String[] suivants = svt.split(",");
				for (String nomSvt : suivants) {
					nomSvt = nomSvt.trim();
					if (!nomSvt.isEmpty()) {
						Tache suivant = chercherTacheParNom(nomSvt);
						if (suivant != null) {
							tNew.ajouterSuivant(suivant);
						}
					}
				}
			}
			else
			{
				Tache fin = chercherTacheParNom("Fin");
				tNew.ajouterSuivant(fin);
			}

			this.taches.add(tNew);

			// Calculer les dates au plus tôt et au plus tard
			this.calculerDates();

			//Creer le(s) chemin(s) critique(s)
			this.creerCheminCritique();

		}

		public void supprimerTache(String nom) 
		{
			Tache t = chercherTacheParNom(nom);

			if (t == null) return;

			Tache tFin   = chercherTacheParNom("Fin");
			Tache tDebut = chercherTacheParNom("Debut");

			// Retirer la tâche des précédents et suivants de toutes les autres tâches
			for (Tache t1 : this.taches) {
				// Supprimer des précédents
				ArrayList<Tache> precedents = t1.getPrecedents();
				for (int i = precedents.size() - 1; i >= 0; i--) {
					if (precedents.get(i).getNom().equals(nom)) {
						precedents.remove(i);
					}
				}
				// Supprimer des suivants
				ArrayList<Tache> suivants = t1.getSuivants();
				for (int i = suivants.size() - 1; i >= 0; i--) {
					if (suivants.get(i).getNom().equals(nom)) {
						suivants.remove(i);
					}
				}
			}

			// Pour chaque tâche, si elle n'a plus de suivant et ce n'est pas "Fin" ou "Debut", on lui ajoute "Fin"
			for (Tache t1 : this.taches) {
				if (t1.getSuivants().isEmpty() && 
					!t1.getNom().equals("Fin") && 
					!t1.getNom().equals("Debut")) {
					t1.ajouterSuivant(tFin);
				}
			}

			// Pour chaque tâche, si elle n'a plus de précédent et ce n'est pas "Fin" ou "Debut", on lui ajoute "Debut"
			for (Tache t1 : this.taches) {
				if (t1.getPrecedents().isEmpty() && 
					!t1.getNom().equals("Fin") && 
					!t1.getNom().equals("Debut")) {
					t1.ajouterPrecedent(tDebut);
				}
			}

			// Supprimer la tâche elle-même de la liste principale
			this.taches.remove(t);


				// Calculer les dates au plus tôt et au plus tard
				calculerDates();

				//Creer le(s) chemin(s) critique(s)
				creerCheminCritique();
		}

	public void setDure(int val, Tache tache)
	{
		this.taches.get(this.taches.indexOf(tache)).setDuree(val);
		
		// Calculer les dates au plus tôt et au plus tard
		this.calculerDates();

		//Creer le(s) chemin(s) critique(s)
		this.creerCheminCritique();
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

	public ArrayList<Tache> getTaches() {return this.taches;}

	public boolean valeursValides(String nom, String duree, String ant, String Svt)
	{
		this.msgErreur = "";

		// Vérification du nom
		if (nom == null || nom.trim().isEmpty()) 
		{
			this.msgErreur = Erreur.NON_SAISIE.getMessage();
			return false;
		}
		String nomTrim = nom.trim();
		if (nomTrim.contains("|") || nomTrim.contains(",")) 
		{
			this.msgErreur = Erreur.CHAR_NOM_INVALIDE.getMessage();
			return false;
		}
		for (Tache t : this.taches)
			if (t.getNom().equals(nomTrim)) 
			{
				this.msgErreur = Erreur.DEJA_EXISTANT.getMessage();
				return false;
			}

		// Vérification de la durée
		if (duree == null || duree.trim().isEmpty()) 
		{
			this.msgErreur = Erreur.DUREE_INVALIDE.getMessage();
			return false;
		}
		int duration;
		try 
		{
			duration = Integer.parseInt(duree.trim());
			if (duration <= 0) 
			{
				this.msgErreur = Erreur.DUREE_NEGATIF.getMessage();
				return false;
			}
		} 
		catch (NumberFormatException e) 
		{
			this.msgErreur = Erreur.DUREE_INT.getMessage();
			return false;
		}

		// Vérification des antécédents
		if (ant != null && !ant.trim().isEmpty()) 
		{
			for (String unAntecedent : ant.trim().split(",")) 
			{
				unAntecedent = unAntecedent.trim();

				if (unAntecedent.isEmpty()) continue;

				if (unAntecedent.equals("Fin")) 
				{
					this.msgErreur = Erreur.PRECEDENT_FIN.getMessage();
					return false;
				}

				if (unAntecedent.equals("Debut")) 
				{
					this.msgErreur = Erreur.PRECEDENT_DEBUT.getMessage();
					return false;
				}
				if (unAntecedent.equals(nomTrim)) 
				{
					Erreur.TACHE_DEPENDANCE_REFLEXIVE.formater(nomTrim);
					return false;
				}

				boolean trouve = false;
				for (Tache t : this.taches)
					if (t.getNom().equals(unAntecedent)) 
					{
						trouve = true;
						break;
					}
				if (!trouve) 
				{
					this.msgErreur = Erreur.PRECEDENT_NON_EXISTANT.formater(unAntecedent);
					return false;
				}
			}
		}

		// Vérification des suivants
		if (Svt != null && !Svt.trim().isEmpty()) 
		{
			for (String unSuivant : Svt.trim().split(",")) 
			{
				unSuivant = unSuivant.trim();
				if (unSuivant.isEmpty()) continue;
				if (unSuivant.equals("Debut")) 
				{
					this.msgErreur = Erreur.SUIVANT_DEBUT.getMessage();
					return false;
				}
				if (unSuivant.equals("Fin")) 
				{
					this.msgErreur = Erreur.SUIVANT_FIN.getMessage();
					return false;
				}
				if (unSuivant.equals(nomTrim)) 
				{
					this.msgErreur = Erreur.TACHE_DEPENDANCE_REFLEXIVE.formater(nomTrim);
					return false;
				}
				// Vérifier que le suivant n'est pas aussi un antécédent
				if (ant != null && !ant.trim().isEmpty()) 
				{
					for (String unAntecedent : ant.trim().split(",")) 
					{
						if (unSuivant.equals(unAntecedent.trim())) 
						{
							this.msgErreur = Erreur.SUIVANT_ET_PRECEDENT.formater(unSuivant);
							return false;
						}
					}
				}
				boolean trouve = false;
				for (Tache t : this.taches)
					if (t.getNom().equals(unSuivant)) 
					{
						trouve = true;
						break;
					}
				if (!trouve) 
				{
					this.msgErreur = Erreur.SUIVANT_INEXISTANT.formater(unSuivant);
					return false;
				}
			}
		}

		return true;
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
