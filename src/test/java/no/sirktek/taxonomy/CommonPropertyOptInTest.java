package no.sirktek.taxonomy;

import no.sirktek.taxonomy.model.PropertyDefinition;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Furniture opts into the shared emission properties and nothing else from
 * taxonomy-commons; the loader attaches opted-in properties to the class.
 */
class CommonPropertyOptInTest {

    private static final String COMMON = "http://taxonomy.sirktek.no/common#";

    private final FurnitureTaxonomyService taxonomyService = new FurnitureTaxonomyService();

    @Test
    void furnitureOptsIntoEmissionProperties() {
        PropertyDefinition emission = find("emissionPerUnit").orElseThrow();
        assertEquals(COMMON + "emissionPerUnit", emission.uri());
        assertEquals(COMMON + "EmissionEntry", emission.rangeType());
        assertNull(emission.privateSet());

        PropertyDefinition consistsOf = find("consistsOf").orElseThrow();
        assertEquals(COMMON + "consistsOf", consistsOf.uri());
        assertEquals(COMMON + "ConsistsOfEntry", consistsOf.rangeType());
        assertNull(consistsOf.privateSet());
    }

    @Test
    void furnitureHasNoAccountingProperties() {
        for (String name : List.of("ledgerAccount", "depreciationGroup", "valuations",
                "bookValue", "taxValue", "wealthTaxValue")) {
            assertTrue(find(name).isEmpty(), "Furniture should not carry " + name);
        }
    }

    private Optional<PropertyDefinition> find(String name) {
        return taxonomyService.getCategoryByClassName("Furniture").orElseThrow().properties().stream()
                .filter(p -> name.equals(p.name())).findFirst();
    }
}
