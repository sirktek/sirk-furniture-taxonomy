package no.sirktek.taxonomy;

import no.sirktek.taxonomy.loader.FurnitureRdfsTaxonomyLoader;

/**
 * Main service for accessing furniture taxonomy data
 */
public class FurnitureTaxonomyService extends TaxonomyService {

    /**
     * Default constructor using furniture taxonomy loader
     */
    public FurnitureTaxonomyService() {
        super(new FurnitureRdfsTaxonomyLoader());
    }
}
