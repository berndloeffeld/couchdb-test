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
    
    private  boolean createTestDataMode;

    
    /**
     * @return true if the application shall create its own test data.
     */
    public boolean isCreateTestDataMode() {
        return createTestDataMode;
    }


    @PostConstruct
    private void loadConfig() {
        rb = ResourceBundle.getBundle("conf");

        if (rb.containsKey("app.testdata.mode")) {
            this.createTestDataMode = "true".equals(rb.getString("app.testdata.mode").toLowerCase());
        } else {
            this.createTestDataMode = false;
        }
    }
}
