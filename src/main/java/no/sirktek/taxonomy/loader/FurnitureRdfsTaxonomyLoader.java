package no.sirktek.taxonomy.loader;

/**
 * Loads the furniture taxonomy from RDF-S Turtle files using Apache Jena
 */
public class FurnitureRdfsTaxonomyLoader extends RdfsTaxonomyLoader {

    private static final String FURNITURE_NAMESPACE = "http://taxonomy.sirktek.no/furniture#";
    private static final String RESOURCE_PATH = "/taxonomy/furniture-base.ttl";

    /**
     * Default constructor
     */
    public FurnitureRdfsTaxonomyLoader() {
        super();
    }

    @Override
    protected String getNamespace() {
        return FURNITURE_NAMESPACE;
    }

    @Override
    protected String getResourcePath() {
        return RESOURCE_PATH;
    }
}
