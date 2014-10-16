package fr.univtln.bruno.d14.ihmjdbc.exceptions;

/**
 * Created by bruno on 16/10/14.
 * Cette classe représente les erreurs émise lors de l'importation des paramètes de l'application.
 * L'erreur d'origine est accessible via getException().
 */
public class ConfigImportException extends Throwable {
    private Exception exception;

    public ConfigImportException(Exception e) {
    }

    public Exception getException() {
        return exception;
    }
}
