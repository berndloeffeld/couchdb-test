package de.magicinternet.aggregation.model;

import org.ektorp.support.CouchDbDocument;

/**
 * Asset represenation class.
 * 
 * @author bloeffeld
 * 
 */
public class Asset extends CouchDbDocument {

    private static final long serialVersionUID = 1;

    private String title;

    private String description;

    /**
     * @return The title of the Asset
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the Asset.
     * 
     * @param title of the Asset
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    
    /**
     * @return the description of the Asset
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the Asset.
     * 
     * @param description of the Asset
     */
    public void setDescription(final String description) {
        this.description = description;
    }
}
