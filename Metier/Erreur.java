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
	SUIVANT_DEBUT("La tâche 'Debut' ne peut pas être un suivant."),
	FORMAT_FICHIER_INVALIDE("Veuillez sélectionner un fichier .data ou .txt"),
	ERREUR_ENREGISTREMENT("Une erreur s'est produite lors de l'enregistrement du fichier."),
	ENREGISTREMENT_SUCCES("Fichier '%s' enregistré avec succès \nChemin :"),
	ENREGISTREMENT_ANULLER("Enregistrement annulé par l'utilisateur."),

	// erreurs pour les dates
    DATE_FORMAT_INVALIDE("La date doit être au format dd/mm/yyyy."),
    DATE_INVALIDE("La date '%s' n'est pas valide."),
    DATE_VIDE("La date de début ne peut pas être vide."),
    ANNEE_INVALIDE("L'année doit être comprise entre 1900 et 2100."),
    MOIS_INVALIDE("Le mois doit être compris entre 1 et 12."),
    JOUR_INVALIDE("Le jour '%d' n'est pas valide pour le mois '%d'."),
    
    // Erreurs pour les fichiers
    FICHIER_INTROUVABLE("Le fichier '%s' est introuvable."),
    FICHIER_VIDE("Le fichier est vide ou ne contient aucune tâche valide."),
    LECTURE_FICHIER_ERREUR("Erreur lors de la lecture du fichier : %s"),
    ECRITURE_FICHIER_ERREUR("Erreur lors de l'écriture du fichier : %s"),
    
    // Erreurs pour la logique MPM
    AUCUNE_TACHE("Aucune tâche n'a été définie dans le projet."),
    DEBUT_INEXISTANT("La tâche 'Debut' est manquante."),
    FIN_INEXISTANTE("La tâche 'Fin' est manquante."),
    DEPENDANCE_CIRCULAIRE("Dépendance circulaire détectée dans le projet."),
    AUCUN_CHEMIN_CRITIQUE("Aucun chemin critique n'a pu être calculé."),
    
    // Erreurs pour la validation des tâches
    NOM_TROP_LONG("Le nom de la tâche ne peut pas dépasser 50 caractères."),
    NOM_RESERVE("Le nom '%s' est réservé et ne peut pas être utilisé."),
    DUREE_TROP_GRANDE("La durée ne peut pas dépasser 9999 jours."),
    
    // Erreurs pour l'interface
    SELECTION_VIDE("Aucune tâche n'est sélectionnée."),
    MODIFICATION_IMPOSSIBLE("Cette tâche ne peut pas être modifiée."),
    SUPPRESSION_IMPOSSIBLE("La tâche '%s' ne peut pas être supprimée car elle a des dépendances.");

    

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
