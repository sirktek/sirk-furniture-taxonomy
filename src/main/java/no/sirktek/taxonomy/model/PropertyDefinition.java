package no.sirktek.taxonomy.model;

import lombok.Builder;

/**
 * Represents a property definition in the RDF-S taxonomy
 *
 * @param name           Property name/identifier (local name from URI)
 * @param englishLabel   English label for the property
 * @param norwegianLabel Norwegian label for the property (if available)
 * @param uri            Complete property URI
 * @param rangeType      RDF range type (e.g., xsd:string, xsd:decimal, etc.)
 * @param domainClass    Domain classes this property applies to
 * @param description    Human-readable description
 */
@Builder
public record PropertyDefinition(
        String name,
        String englishLabel,
        String norwegianLabel,
        String uri,
        String rangeType,
        String domainClass,
        String description) {
    /**
     * Convert RDF range type to PropertyType enum equivalent
     */
    public PropertyType getPropertyType() {
        if (rangeType == null) {
            return PropertyType.STRING;
        }

        return switch (rangeType) {
            case "http://www.w3.org/2001/XMLSchema#string" -> {
                if (name != null) {
                    if (name.equals("unit")) yield PropertyType.UNIT;
                    if (name.equals("resourceType")) yield PropertyType.RESOURCE_TYPE;
                }
                yield PropertyType.STRING;
            }
            case "http://www.w3.org/2001/XMLSchema#decimal" -> {
                // Need to check property name for specific decimal types
                if (name != null) {
                    if (name.contains("weight")) yield PropertyType.DECIMAL_KG;
                    if (name.contains("volume")) yield PropertyType.DECIMAL_M3;
                    if (name.contains("length") || name.contains("width") || name.contains("height"))
                        yield PropertyType.DECIMAL_CM;
                    if (name.contains("emission")) yield PropertyType.EMISSION;
                }
                yield PropertyType.DECIMAL;
            }
            case "http://www.w3.org/2001/XMLSchema#date" -> PropertyType.DATE;
            case "http://www.w3.org/2001/XMLSchema#boolean" -> PropertyType.BOOLEAN;
            case "http://www.w3.org/2001/XMLSchema#anyURI" -> PropertyType.URL;
            case "http://www.w3.org/2001/XMLSchema#integer" -> PropertyType.INTEGER;
            default -> {
                // Handle custom types
                if (rangeType.contains("Manufacturer") || rangeType.contains("Furniture")) {
                    yield PropertyType.CATEGORY;
                }
                if (name != null) {
                    if (name.contains("emission")) yield PropertyType.EMISSION;
                    if (name.equals("unit")) yield PropertyType.UNIT;
                    if (name.equals("resourceType")) yield PropertyType.RESOURCE_TYPE;
                }
                yield PropertyType.STRING; // Default fallback
            }
        };
    }

    /**
     * Property types matching the original enum
     */
    public enum PropertyType {
        STRING,
        DECIMAL,
        INTEGER,
        DATE,
        BOOLEAN,
        INTEGER_SCALE_1TO5,
        DECIMAL_CM,
        UNIT,
        DECIMAL_KG,
        DECIMAL_M2,
        DECIMAL_M3,
        CATEGORY,
        URL,
        MULTI_CATEGORY,
        EMAIL_FORM,
        RESOURCE_TYPE,
        EMISSION
    }
}