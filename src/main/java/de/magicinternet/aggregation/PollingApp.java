package de.magicinternet.aggregation;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.magicinternet.aggregation.eater.FeedListener;
import de.magicinternet.aggregation.model.Asset;

/**
 * Hello world!
 * 
 * @author: bloeffeld
 */
public final class PollingApp {

    private PollingApp() {
        // Nothing todo
    }
    
    /**
     * Main method for starting this application.
     * 
     * @param args - should be empty, no argurments expected
     * @throws Exception - everything is thrown, this is a prototype
     */
    public static void main(final String[] args) throws Exception {
        final Logger log = LoggerFactory.getLogger(PollingApp.class);
        Settings settings = null;

        try {
            final ResourceBundle bundle = ResourceBundle.getBundle("conf");
            settings = new Settings(bundle);
        } catch (MissingResourceException e) {
            log.error("Could not read config", e);
            System.exit(1);
        }

        log.info("Starting the Couch DB polling test");
        final HttpClient httpClient = new StdHttpClient.Builder().url(settings.getCouchUrl()).build();
        final CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
        final CouchDbConnector db = dbInstance.createConnector(settings.getDbName(), true);
        db.createDatabaseIfNotExists();

        final FeedListener eater = new FeedListener(db);

        final Thread t = new Thread(eater);
        t.start();

        if (settings.isCreateTestDataMode()) {
            createTestEntries(db);
        }

    }

    private static void createTestEntries(final CouchDbConnector db) throws InterruptedException {

        
        final String[] titles = {"Breaking the habit", "In the end", "Crawling"};

        for (String title : titles) {
            final Asset a = new Asset();
            a.setTitle(title);
            a.setDescription("Neue Single von Linkin Park: " + title);
            db.create(a);

            final int millisToSleep = 3000;
            Thread.sleep(millisToSleep);
        }
    }
}
