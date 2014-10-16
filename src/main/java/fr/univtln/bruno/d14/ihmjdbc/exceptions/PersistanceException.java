package fr.univtln.bruno.d14.ihmjdbc.exceptions;

/**
 * Created by bruno on 16/10/14.
 * Cette classe représente les erreurs émises lors de la persistance.
 * L'erreur d'origine est accessible via getException().
 */
public class PersistanceException extends Exception {
    private Exception exception;

    public PersistanceException(String message) {
        super(message);
    }

    public PersistanceException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
