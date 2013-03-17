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
    }

    /**
     * @return The URL of the CouchDB the application will connect to
     */
    public String getCouchUrl() {
        return this.couchUrl;
    }

}
