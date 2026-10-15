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

    private static final List<String> SHARED_PROPERTIES = List.of("color", "material", "variant",
            "serialNumber", "model", "operationsAndMaintenanceManual", "length", "width", "height",
            "weight", "volume");

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

    @Test
    void furnitureCarriesSharedPropertiesOnceFromCommons() {
        for (String name : SHARED_PROPERTIES) {
            List<PropertyDefinition> matches = findAll(name);
            assertEquals(1, matches.size(), name + " should be carried exactly once");
            assertEquals(COMMON + name, matches.get(0).uri(), name + " should come from commons");
        }
    }

    private Optional<PropertyDefinition> find(String name) {
        return findAll(name).stream().findFirst();
    }

    private List<PropertyDefinition> findAll(String name) {
        return taxonomyService.getCategoryByClassName("Furniture").orElseThrow().properties().stream()
                .filter(p -> name.equals(p.name())).toList();
    }
}
