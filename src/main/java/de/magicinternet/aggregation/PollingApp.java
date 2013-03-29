package de.magicinternet.aggregation;

import org.ektorp.CouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        log.info("Starting the Couch DB polling test");

        @SuppressWarnings("resource")
        final ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring.xml");
        final BeanFactory factory = context;
        final Settings settings = factory.getBean(Settings.class);
        final StdCouchDbInstance couchDbInstance = factory.getBean(StdCouchDbInstance.class);

        final FeedListener eater = factory.getBean(FeedListener.class);

        final Thread t = new Thread(eater);
        t.start();

        if (settings.isCreateTestDataMode()) {
            final CouchDbConnector db = couchDbInstance.createConnector("blub", true);
            db.createDatabaseIfNotExists();
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
