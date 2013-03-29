package de.magicinternet.aggregation.eater;

import org.ektorp.CouchDbConnector;

/**
 * Listener to a couchdb feed.
 * 
 * @author bloeffeld
 * 
 */
public interface FeedListener extends Runnable {
    /**
     * Add the CouchDBConnector to the Listener.
     * 
     * @param connector
     *            to the couchDB
     */
    void setCouchDbConnector(CouchDbConnector connector);
}
