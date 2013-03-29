package de.magicinternet.aggregation;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Class keeping all the settings of this app.
 * 
 * @author bloeffeld
 * 
 */
@Component
@Scope(value = "singleton")
public final class Settings {

    private ResourceBundle rb;
    
    private  String couchUrl;
    private String dbName;
    private  boolean createTestDataMode;

    
    /**
     * @return true if the application shall create its own test data.
     */
    public boolean isCreateTestDataMode() {
        return createTestDataMode;
    }

//    /**
//     * @return The URL of the CouchDB the application will connect to
//     */
//    public String getCouchUrl() {
//        return this.couchUrl;
//    }

    /**
     * @return the name of the database that shall be used
     */
    public String getDbName() {
        return this.dbName;
    }

    @PostConstruct
    private void loadConfig() {
        rb = ResourceBundle.getBundle("conf");
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
}
