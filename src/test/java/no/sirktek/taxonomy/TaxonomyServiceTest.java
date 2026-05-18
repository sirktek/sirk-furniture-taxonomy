package no.sirktek.taxonomy;

import no.sirktek.taxonomy.FurnitureTaxonomyService;
import no.sirktek.taxonomy.model.CategoryInfo;
import no.sirktek.taxonomy.model.TaxonomyTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaxonomyServiceTest {

    private FurnitureTaxonomyService taxonomyService;

    @BeforeEach
    void setUp() {
        taxonomyService = new FurnitureTaxonomyService();
    }

    @Test
    void shouldLoadBaseTaxonomy() {
        TaxonomyTree taxonomy = taxonomyService.loadBaseTaxonomy();

        assertNotNull(taxonomy);
        assertNotNull(taxonomy.rootCategories());
        assertFalse(taxonomy.rootCategories().isEmpty());
    }

    @Test
    void shouldFindFurnitureRootCategory() {
        Optional<CategoryInfo> furniture = taxonomyService.getCategoryByClassName("Furniture");

        assertTrue(furniture.isPresent());
        assertEquals("Furniture", furniture.get().className());
        assertEquals("Furniture", furniture.get().englishName());
        assertTrue(furniture.get().isRoot());
    }



    @Test
    void shouldReturnEmptyForNonExistentCategory() {
        Optional<CategoryInfo> nonExistent = taxonomyService.getCategoryByClassName("NonExistent");

        assertFalse(nonExistent.isPresent());
    }

    @Test
    void shouldProvideStats() {
        TaxonomyService.TaxonomyStats stats = taxonomyService.getStats();

        assertTrue(stats.totalCategories() > 0);
        assertTrue(stats.rootCategories() > 0);

        // v3.0: Manufacturer/Model/Resource moved to common-taxonomy, so
        // Furniture is now the only root in this taxonomy.
        assertEquals(1, stats.rootCategories());

        // Total categories should be more than root categories due to hierarchy
        assertTrue(stats.totalCategories() > stats.rootCategories());
    }

    @Test
    void shouldCacheTaxonomyAfterFirstLoad() {
        TaxonomyTree first = taxonomyService.loadBaseTaxonomy();
        TaxonomyTree second = taxonomyService.loadBaseTaxonomy();

        // Should be the same instance due to caching
        assertSame(first, second);
    }

    @Test
    void shouldReloadBaseTaxonomy() {
        // Load taxonomy first time
        TaxonomyTree first = taxonomyService.loadBaseTaxonomy();

        // Reload should give us a fresh instance
        TaxonomyTree reloaded = taxonomyService.reloadBaseTaxonomy();

        assertNotNull(reloaded);
        assertNotSame(first, reloaded);

        // But subsequent loads should cache the reloaded version
        TaxonomyTree cached = taxonomyService.loadBaseTaxonomy();
        assertSame(reloaded, cached);
    }

    @Test
    void shouldDetectBaseTaxonomyClasses() {
        assertTrue(taxonomyService.isBaseTaxonomyClass("Table"));
        assertTrue(taxonomyService.isBaseTaxonomyClass("OfficeChair"));
        assertTrue(taxonomyService.isBaseTaxonomyClass("Furniture"));
        assertFalse(taxonomyService.isBaseTaxonomyClass("CustomClass"));
        assertFalse(taxonomyService.isBaseTaxonomyClass("NonExistent"));
    }

}