package de.magicinternet.aggregation;

import java.util.ResourceBundle;

/**
 * Class keeping all the settings of this app.
 * 
 * @author bloeffeld
 * 
 */
public final class Settings {

    private final String couchUrl;
    private final String dbName;
    private final boolean createTestDataMode;

    /**
     * Default constructor. Feed a resource bundle based on a properties file in here
     * @param rb - the ResourceBundle keeping the configurations
     */
    public Settings(final ResourceBundle rb) {
        if (rb.containsKey("couch.url")) {
            this.couchUrl = rb.getString("couch.url");
        } else {
            this.couchUrl = "http://localhost:5984";
        }
        
        if (rb.containsKey("couch.db.name")) {
            this.dbName = rb.getString("couch.db.name");
        } else {
            this.dbName = "polling_test_db";
        }
        
        if (rb.containsKey("app.testdata.mode")) {
            this.createTestDataMode = "true".equals(rb.getString("app.testdata.mode").toLowerCase());
        } else {
            this.createTestDataMode = false;
        }
    }

    /**
     * @return true if the application shall create its own test data.
     */
    public boolean isCreateTestDataMode() {
        return createTestDataMode;
    }

    /**
     * @return The URL of the CouchDB the application will connect to
     */
    public String getCouchUrl() {
        return this.couchUrl;
    }

    /**
     * @return the name of the database that shall be used
     */
    public String getDbName() {
        return this.dbName;
    }

}
