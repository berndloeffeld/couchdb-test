package de.magicinternet.aggregation.eater.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;

import org.ektorp.CouchDbConnector;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.magicinternet.aggregation.eater.FeedListener;

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

    private final Logger log = LoggerFactory.getLogger(FeedListenerImpl.class);

    @Autowired
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
                printDocumentInfo(docId); 
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

    private void printDocumentInfo(final String docId) {
        log.info("something changed: {}", docId);

        final BufferedReader documentReader = new BufferedReader(new InputStreamReader(db.getAsStream(docId)));
        final StringBuilder documentString = new StringBuilder();

        String line;
        try {
            while ((line = documentReader.readLine()) != null) {
                documentString.append(line);
            }
        } catch (IOException e) {
            log.error("Could not read changed document {}", docId, e);
        }
        
        log.debug("Changed document content: {}", documentString);
    }

    @PostConstruct
    private void postTest() {
        db.createDatabaseIfNotExists();
    }

}
