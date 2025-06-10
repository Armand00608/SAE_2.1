package exFinal.Metier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Tache
{
	private String nom;
	private int    duree;
	private int    datePlusTot = 0;
	private int    datePlusTard = 0;

	private ArrayList<Tache> precedents = new ArrayList<>();
	private ArrayList<Tache> suivants = new ArrayList<>();

	public Tache(String nom, int duree)
	{
		this.nom   = nom;
		this.duree = duree;
	}

	public String getNom()   {return this.nom;}
	public int    getDuree() {return this.duree;}

	public int getDatePlusTot()  {return this.datePlusTot;}
	public int getDatePlusTard() {return this.datePlusTard;}

	public ArrayList<Tache> getPrecedents() {return this.precedents;}
	public ArrayList<Tache> getSuivants()   {return this.suivants;}

	// Ajoute une tâche précédente (dépendance)
	public void ajouterPrecedent(Tache t) 
	{
		if (!this.precedents.contains(t))
			this.precedents.add(t);

		if (!t.suivants.contains(this))
			t.suivants.add(this);
	}

	// Ajoute une tâche suivant
	public void ajouterSuivant(Tache t) 
	{
		if (!this.suivants.contains(t))
			this.suivants.add(t);

		if (!t.precedents.contains(this))
			t.precedents.add(this);
	}


	// Calcule la date au plus tôt à laquelle cette tâche peut commencer
	public void calculerDatePlusTot()
	{
		int max = 0;
		for (Tache t : this.precedents)
		{
			int finTache = t.getDatePlusTot() + t.getDuree();
			if (finTache > max)
			{
				max = finTache;
			}
		}
		this.datePlusTot = max;
	}

	// Calcule la date au plus tard à laquelle cette tâche peut commencer sans retarder le projet
	public void calculerDatePlusTard()
	{
		int min = Integer.MAX_VALUE;

		for (Tache t : this.suivants)
		{
			int debutSuivant = t.getDatePlusTard() - this.duree;
			if (debutSuivant < min)
			{
				min = debutSuivant;
			}
		}

		// Si pas de suivants, on laisse la date actuelle
		if (!this.suivants.isEmpty())
		{
			this.datePlusTard = min;
		}
	}

	// Méthode ajoutée pour fixer manuellement la date plus tard
	public void setDatePlusTard(int date)
	{
		this.datePlusTard = date;
	}

	// Vérifie si la tâche est critique (aucune marge)
	public boolean estCritique() {return this.datePlusTot == this.datePlusTard;}

	public String ajouterJours(String dateDebut, int nbJours)
	{
		try
		{
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Calendar         cal    = Calendar.getInstance();
			cal.setTime(format.parse(dateDebut));
			cal.add(Calendar.DAY_OF_MONTH, nbJours);

			return new SimpleDateFormat("dd/MM").format(cal.getTime());
		}
		catch (ParseException e) {return "??/??";}
	}


	// Retourne une représentation texte de la tâche
	public String toString(String dateDebut)
	{
		String resultat = "";

		// Titre : A : 2 jours
		resultat += this.nom + " : " + this.duree + " jour" + (this.duree > 1 ? "s" : "") + "\n";

		// Calcul des dates au format jj/MM
		String datePlusTotStr  = ajouterJours(dateDebut, this.datePlusTot);
		String datePlusTardStr = ajouterJours(dateDebut, this.datePlusTard);

		resultat += "date au plus tôt : "  + datePlusTotStr  + "\n";
		resultat += "date au plus tard : " + datePlusTardStr + "\n";

		int marge = this.datePlusTard - this.datePlusTot;
		resultat += "marge : " + marge + " jour" + (marge > 1 ? "s" : "") + "\n";

		// Liste des précédents
		if (this.precedents.isEmpty())
		{
			resultat += "pas de tâche précédente\n";
		}
		else
		{
			resultat += "liste des tâches précédentes :\n";
			for (Tache t : this.precedents)
			{
				resultat += t.getNom() + "\n";
			}
		}

		// Liste des suivants
		if (this.suivants.isEmpty())
		{
			resultat += "pas de tâche suivante\n";
		}
		else
		{
			resultat += "liste des tâches suivantes :\n";
			for (Tache t : this.suivants)
			{
				resultat += t.getNom() + "\n";
			}
		}

		return resultat;
	}
}
