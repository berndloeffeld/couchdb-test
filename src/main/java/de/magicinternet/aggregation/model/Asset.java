package de.magicinternet.aggregation.model;

import java.io.IOException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.ektorp.support.CouchDbDocument;

/**
 * Asset represenation class.
 * 
 * @author bloeffeld
 * 
 */
@JsonDeserialize(using = Asset.Deserializer.class)
public class Asset extends CouchDbDocument {

    private static final long serialVersionUID = 1;

    private String id;
    private String revision;

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
     * @param title
     *            of the Asset
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
     * @param description
     *            of the Asset
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Override the toString.
     * 
     * @return the String representation
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("ID", this.id)
                .append("Revision", this.revision).append("Title", this.title).append("Description", this.description)
                .toString();
    }

    /**
     * Deserializer of vasdb json.
     * 
     * @author bloeffeld
     * 
     */
    public static class Deserializer extends JsonDeserializer<Asset> {

        @Override
        public Asset deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
            final Asset result = new Asset();
            final ObjectCodec oc = parser.getCodec();
            final JsonNode node = oc.readTree(parser);

            result.id = node.get("_id").getTextValue();
            result.revision = node.get("_rev").getTextValue();
            result.title = node.get("title").get("default").getTextValue();
            final JsonNode shortDescription = node.get("shortdescription");
            if (shortDescription != null) {
                result.description = shortDescription.get("default").getTextValue();
            }

            return result;
        }

    }
}
