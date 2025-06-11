package Metier;

public enum Erreur {
	FORMAT_INVALIDE("Le format est invalide."),
	NON_SAISIE("Vous n'avez pas saisi de valeur."),
	DEJA_EXISTANT("Une tâche avec ce nom existe déjà."),
	CHAR_NOM_INVALIDE("Le nom de tâche ne peut pas contenir les caractères '|' ou ','."),
	DUREE_INVALIDE("La durée n'a pas été saisie correctement."),
	DUREE_NEGATIF("La durée doit être strictement supérieure à 0."),
	DUREE_INT("La durée doit être un nombre entier."),
	PRECEDENT_FIN("La tâche 'Fin' ne peut pas être un prédécesseur."),
	PRECEDENT_DEBUT("La tâche 'Debut' ne peut pas être un prédécesseur."),
	TACHE_DEPENDANCE_REFLEXIVE("La tâche '%s' ne peut pas dépendre d'elle-même."),
	PRECEDENT_NON_EXISTANT("Le precedent '%s' n'existe pas."),
	SUIVANT_ET_PRECEDENT("Un suivant ne peut pas être aussi un prédécesseur : '%s'."),
	SUIVANT_FIN("La tâche 'Fin' ne doit pas être spécifiée comme suivant."),
	SUIVANT_INEXISTANT("Le suivant spécifié '%s' n'existe pas."),
	SUIVANT_DEBUT("La tâche 'Debut' ne peut pas être un suivant.");


	private final String message;

	Erreur(String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}

	public String formater(Object... args)
	{
		return String.format(message, args);
	}
}
