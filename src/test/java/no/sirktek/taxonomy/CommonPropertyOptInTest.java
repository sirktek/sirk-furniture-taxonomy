package no.sirktek.taxonomy;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies that the furniture taxonomy opts its asset classes into the cross-cutting
 * common properties. The opt-in is a {@code schema:domainIncludes} triple that
 * only the merged graph in the consumer sees, so it is checked at the Jena
 * level rather than through the domain loader.
 */
class CommonPropertyOptInTest {

    private static final String COMMON = "http://taxonomy.sirktek.no/common#";
    private static final String NS = "http://taxonomy.sirktek.no/furniture#";
    private static Model model;

    @BeforeAll
    static void load() throws IOException {
        model = ModelFactory.createDefaultModel();
        try (InputStream in = CommonPropertyOptInTest.class.getResourceAsStream("/taxonomy/furniture-base.ttl")) {
            assertNotNull(in, "furniture-base.ttl missing from classpath");
            model.read(in, null, "TURTLE");
        }
    }

    @Test
    void assetClassesOptIntoAccountingProperties() {
        Property domainIncludes = model.createProperty("https://schema.org/domainIncludes");
        for (String cls : List.of("Furniture")) {
            Resource target = model.createResource(NS + cls);
            for (String prop : List.of("emissionPerUnit", "consistsOf", "ledgerAccount", "bookValue", "taxValue", "depreciationGroup")) {
                assertTrue(model.contains(model.createResource(COMMON + prop), domainIncludes, target),
                        "common:" + prop + " should include " + cls + " in its domain");
            }
        }
    }
}
