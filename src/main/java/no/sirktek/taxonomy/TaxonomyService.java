package no.sirktek.taxonomy;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import no.sirktek.taxonomy.loader.RdfsTaxonomyLoader;
import no.sirktek.taxonomy.model.CategoryInfo;
import no.sirktek.taxonomy.model.TaxonomyTree;

import java.util.Optional;

/**
 * Main service for accessing furniture taxonomy data
 */
@Slf4j
public class TaxonomyService {

    private final RdfsTaxonomyLoader loader;
    private volatile TaxonomyTree cachedTaxonomy;

    public TaxonomyService() {
        this.loader = new RdfsTaxonomyLoader();
    }

    public TaxonomyService(RdfsTaxonomyLoader loader) {
        this.loader = loader;
    }

    /**
     * Load the base taxonomy tree (cached after first load)
     */
    public TaxonomyTree loadBaseTaxonomy() {
        if (cachedTaxonomy == null) {
            synchronized (this) {
                if (cachedTaxonomy == null) {
                    log.info("Loading base taxonomy from RDF-S for the first time");
                    cachedTaxonomy = loader.loadBaseTaxonomy();
                }
            }
        }
        return cachedTaxonomy;
    }

    /**
     * Force reload of the taxonomy (clears cache)
     */
    public TaxonomyTree reloadBaseTaxonomy() {
        synchronized (this) {
            log.info("Forcing reload of base taxonomy");
            cachedTaxonomy = null;
            return loadBaseTaxonomy();
        }
    }

    /**
     * Find category information by English class name
     */
    public Optional<CategoryInfo> getCategoryByClassName(String className) {
        if (className == null) {
            return Optional.empty();
        }

        TaxonomyTree taxonomy = loadBaseTaxonomy();
        CategoryInfo found = taxonomy.findByClassName(className);
        return Optional.ofNullable(found);
    }

    /**
     * Check if an English class name exists in the base taxonomy
     */
    public boolean isBaseTaxonomyClass(String className) {
        return getCategoryByClassName(className).isPresent();
    }

    /**
     * Get statistics about the loaded taxonomy
     */
    public TaxonomyStats getStats() {
        TaxonomyTree taxonomy = loadBaseTaxonomy();
        int totalCategories = countCategoriesRecursively(taxonomy.rootCategories());
        int rootCategories = taxonomy.rootCategories().size();

        return TaxonomyStats.builder()
                .totalCategories(totalCategories)
                .rootCategories(rootCategories)
                .build();
    }

    private int countCategoriesRecursively(java.util.List<CategoryInfo> categories) {
        int count = categories.size();
        for (CategoryInfo category : categories) {
            count += countCategoriesRecursively(category.children());
        }
        return count;
    }

    /**
         * Statistics about the taxonomy
         */
        @Builder
        public record TaxonomyStats(
                int totalCategories,
                int rootCategories
    ) {
    }
}