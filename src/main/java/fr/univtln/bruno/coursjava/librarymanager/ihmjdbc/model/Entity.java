package fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.model;

import fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.exceptions.PersistanceException;

import java.sql.Connection;

/**
 * Created by bruno on 16/10/14.
 */
public interface Entity {
    public void persist(Connection connection) throws PersistanceException;

    public void merge(Connection connection) throws PersistanceException;

    public void update(Connection connection) throws PersistanceException;

    public void remove(Connection connection) throws PersistanceException;
}
