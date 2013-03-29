package de.magicinternet.aggregation.eater.impl;

import org.ektorp.CouchDbConnector;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.magicinternet.aggregation.eater.FeedListener;
import de.magicinternet.aggregation.model.Asset;

/**
 * This Runnable will establish a long polling connection to the given couchdb
 * connector. Changes will be printed to logger.
 * 
 * @author bloeffeld
 * 
 */
@Service
public class FeedListenerImpl implements FeedListener {

    private static final int CONN_RETRY_TIME = 5000;

    private final Logger log = LoggerFactory.getLogger(Asset.class);

    private CouchDbConnector db;
    
    private boolean running = true;

    private ChangesFeed feed;

    /**
     * Stop the long poll.
     */
    public void stop() {
        this.running = false;
        feed.cancel();
    }

    /**
     * Run method for this runnable.
     */
    public void run() {

        final ChangesCommand cmd = new ChangesCommand.Builder().filter("").build();

        feed = db.changesFeed(cmd);

        while (feed.isAlive() && running) {
            DocumentChange change;
            try {
                log.info("Waiting for next change");
                change = feed.next();
                final String docId = change.getId();
                log.info("something changed: {}", docId);

                final Asset asset = db.get(Asset.class, docId);
                log.debug("Titel: {}", asset.getTitle());
                log.debug("Beschreibung: {}", asset.getDescription());
            } catch (InterruptedException e) {
                if (running) {
                    log.error("Error while eating change feed", e);
                    try {
                        Thread.sleep(CONN_RETRY_TIME);
                    } catch (InterruptedException ie) {
                        log.error("Thread error while waiting for connection retry", ie);
                    }
                } else {
                    log.info("Feed was stopped");
                }
            }
        }

    }

    /**
     * Add the CouchDBConnector to the Listener.
     * @param connector to the couchDB
     */
    public void setCouchDbConnector(final CouchDbConnector connector) {
        this.db = connector;
    }

}
