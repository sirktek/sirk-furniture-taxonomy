package no.sirktek.taxonomy.model;

/**
 * Furniture-specific property definition with type detection
 */
public class FurniturePropertyDefinition {

    /**
     * Convert RDF range type to PropertyType enum equivalent for furniture taxonomy
     * @param propertyDef the property definition
     * @return the corresponding PropertyType enum value
     */
    public static PropertyType getPropertyType(PropertyDefinition propertyDef) {
        String rangeType = propertyDef.rangeType();
        String name = propertyDef.name();

        if (rangeType == null) {
            return PropertyType.STRING;
        }

        return switch (rangeType) {
            case "http://taxonomy.sirktek.no/furniture#EmissionEntry"    -> PropertyType.EMISSION;
            case "http://taxonomy.sirktek.no/furniture#ConsistsOfEntry"  -> PropertyType.CONSISTS_OF;
            case "http://taxonomy.sirktek.no/furniture#EnergySourceEntry" -> PropertyType.ENERGY_MIX;
            case "http://www.w3.org/2001/XMLSchema#string" -> {
                if (name != null) {
                    if (name.equals("unit")) yield PropertyType.UNIT;
                    if (name.equals("resourceType")) yield PropertyType.RESOURCE_TYPE;
                }
                yield PropertyType.STRING;
            }
            case "http://www.w3.org/2001/XMLSchema#decimal" -> {
                if (name != null) {
                    if (name.contains("weight")) yield PropertyType.DECIMAL_KG;
                    if (name.contains("volume")) yield PropertyType.DECIMAL_M3;
                    if (name.contains("length") || name.contains("width") || name.contains("height"))
                        yield PropertyType.DECIMAL_CM;
                }
                yield PropertyType.DECIMAL;
            }
            case "http://www.w3.org/2001/XMLSchema#date"    -> PropertyType.DATE;
            case "http://www.w3.org/2001/XMLSchema#boolean" -> PropertyType.BOOLEAN;
            case "http://www.w3.org/2001/XMLSchema#anyURI"  -> PropertyType.URL;
            case "http://www.w3.org/2001/XMLSchema#integer" -> PropertyType.INTEGER;
            default -> {
                if (rangeType.contains("Manufacturer") || rangeType.contains("Furniture")) {
                    yield PropertyType.CATEGORY;
                }
                if (name != null) {
                    if (name.equals("unit")) yield PropertyType.UNIT;
                    if (name.equals("resourceType")) yield PropertyType.RESOURCE_TYPE;
                }
                yield PropertyType.STRING;
            }
        };
    }

    /**
     * Property types for furniture taxonomy
     */
    public enum PropertyType {
        /** String property type */
        STRING,
        /** Decimal property type */
        DECIMAL,
        /** Integer property type */
        INTEGER,
        /** Date property type */
        DATE,
        /** Boolean property type */
        BOOLEAN,
        /** Integer scale 1-5 property type */
        INTEGER_SCALE_1TO5,
        /** Decimal centimeters property type */
        DECIMAL_CM,
        /** Unit property type */
        UNIT,
        /** Decimal kilograms property type */
        DECIMAL_KG,
        /** Decimal square meters property type */
        DECIMAL_M2,
        /** Decimal cubic meters property type */
        DECIMAL_M3,
        /** Category property type */
        CATEGORY,
        /** URL property type */
        URL,
        /** Multi-category property type */
        MULTI_CATEGORY,
        /** Email form property type */
        EMAIL_FORM,
        /** Resource type property type */
        RESOURCE_TYPE,
        /** Emission property type */
        EMISSION,
        /** Bill of materials (consists-of) property type */
        CONSISTS_OF,
        /** Energy mix property type */
        ENERGY_MIX
    }
}
