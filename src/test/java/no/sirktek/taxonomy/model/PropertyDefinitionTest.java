package no.sirktek.taxonomy.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static no.sirktek.taxonomy.model.FurniturePropertyDefinition.PropertyType;
import static no.sirktek.taxonomy.model.FurniturePropertyDefinition.getPropertyType;

class PropertyDefinitionTest {

    @Test
    void shouldBuildPropertyDefinitionWithAllFields() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("weight")
                .englishLabel("Weight (kg)")
                .norwegianLabel("Vekt (kg)")
                .uri("http://taxonomy.sirktek.no/furniture#weight")
                .rangeType("http://www.w3.org/2001/XMLSchema#decimal")
                .domainClass("Furniture")
                .description("Weight of the furniture item")
                .build();

        assertEquals("weight", property.name());
        assertEquals("Weight (kg)", property.englishLabel());
        assertEquals("Vekt (kg)", property.norwegianLabel());
        assertEquals("http://taxonomy.sirktek.no/furniture#weight", property.uri());
        assertEquals("http://www.w3.org/2001/XMLSchema#decimal", property.rangeType());
        assertEquals("Furniture", property.domainClass());
        assertEquals("Weight of the furniture item", property.description());
    }

    @Test
    void shouldBuildPropertyDefinitionWithNullFields() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("testProperty")
                .englishLabel("Test Property")
                .uri("http://test.com/property")
                .rangeType("http://www.w3.org/2001/XMLSchema#string")
                .build();

        assertEquals("testProperty", property.name());
        assertEquals("Test Property", property.englishLabel());
        assertNull(property.norwegianLabel());
        assertEquals("http://test.com/property", property.uri());
        assertEquals("http://www.w3.org/2001/XMLSchema#string", property.rangeType());
        assertNull(property.domainClass());
        assertNull(property.description());
    }

    @Test
    void shouldDetectStringPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("color")
                .rangeType("http://www.w3.org/2001/XMLSchema#string")
                .build();

        assertEquals(PropertyType.STRING, getPropertyType(property));
    }

    @Test
    void shouldDetectDecimalKgPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("weight")
                .rangeType("http://www.w3.org/2001/XMLSchema#decimal")
                .build();

        assertEquals(PropertyType.DECIMAL_KG, getPropertyType(property));
    }

    @Test
    void shouldDetectDecimalM3PropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("volume")
                .rangeType("http://www.w3.org/2001/XMLSchema#decimal")
                .build();

        assertEquals(PropertyType.DECIMAL_M3, getPropertyType(property));
    }

    @Test
    void shouldDetectDecimalCmPropertyType() {
        PropertyDefinition lengthProperty = PropertyDefinition.builder()
                .name("length")
                .rangeType("http://www.w3.org/2001/XMLSchema#decimal")
                .build();

        PropertyDefinition widthProperty = PropertyDefinition.builder()
                .name("width")
                .rangeType("http://www.w3.org/2001/XMLSchema#decimal")
                .build();

        PropertyDefinition heightProperty = PropertyDefinition.builder()
                .name("height")
                .rangeType("http://www.w3.org/2001/XMLSchema#decimal")
                .build();

        assertEquals(PropertyType.DECIMAL_CM, getPropertyType(lengthProperty));
        assertEquals(PropertyType.DECIMAL_CM, getPropertyType(widthProperty));
        assertEquals(PropertyType.DECIMAL_CM, getPropertyType(heightProperty));
    }

    @Test
    void shouldDetectGenericDecimalPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("genericDecimal")
                .rangeType("http://www.w3.org/2001/XMLSchema#decimal")
                .build();

        assertEquals(PropertyType.DECIMAL, getPropertyType(property));
    }

    @Test
    void shouldDetectDatePropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("productionDate")
                .rangeType("http://www.w3.org/2001/XMLSchema#date")
                .build();

        assertEquals(PropertyType.DATE, getPropertyType(property));
    }

    @Test
    void shouldDetectBooleanPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("isAvailable")
                .rangeType("http://www.w3.org/2001/XMLSchema#boolean")
                .build();

        assertEquals(PropertyType.BOOLEAN, getPropertyType(property));
    }

    @Test
    void shouldDetectUrlPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("homepage")
                .rangeType("http://www.w3.org/2001/XMLSchema#anyURI")
                .build();

        assertEquals(PropertyType.URL, getPropertyType(property));
    }

    @Test
    void shouldDetectIntegerPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("quantity")
                .rangeType("http://www.w3.org/2001/XMLSchema#integer")
                .build();

        assertEquals(PropertyType.INTEGER, getPropertyType(property));
    }

    @Test
    void shouldDetectCategoryPropertyType() {
        PropertyDefinition manufacturerProperty = PropertyDefinition.builder()
                .name("manufacturer")
                .rangeType("http://taxonomy.sirktek.no/furniture#Manufacturer")
                .build();

        PropertyDefinition furnitureProperty = PropertyDefinition.builder()
                .name("furnitureType")
                .rangeType("http://taxonomy.sirktek.no/furniture#Furniture")
                .build();

        assertEquals(PropertyType.CATEGORY, getPropertyType(manufacturerProperty));
        assertEquals(PropertyType.CATEGORY, getPropertyType(furnitureProperty));
    }

    @Test
    void shouldDetectEmissionPropertyType() {
        PropertyDefinition emissionProperty = PropertyDefinition.builder()
                .name("emissionFromProduction")
                .rangeType("http://www.w3.org/2001/XMLSchema#decimal")
                .build();

        PropertyDefinition emissionPerUnitProperty = PropertyDefinition.builder()
                .name("emissionPerUnit")
                .rangeType("http://www.w3.org/2001/XMLSchema#decimal")
                .build();

        assertEquals(PropertyType.EMISSION, getPropertyType(emissionProperty));
        assertEquals(PropertyType.EMISSION, getPropertyType(emissionPerUnitProperty));
    }

    @Test
    void shouldDetectUnitPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("unit")
                .rangeType("http://www.w3.org/2001/XMLSchema#string")
                .build();

        assertEquals(PropertyType.UNIT, getPropertyType(property));
    }

    @Test
    void shouldDetectResourceTypePropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("resourceType")
                .rangeType("http://www.w3.org/2001/XMLSchema#string")
                .build();

        assertEquals(PropertyType.RESOURCE_TYPE, getPropertyType(property));
    }

    @Test
    void shouldDefaultToStringForUnknownTypes() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("unknownProperty")
                .rangeType("http://unknown.com/type")
                .build();

        assertEquals(PropertyType.STRING, getPropertyType(property));
    }

    @Test
    void shouldHandleNullRangeType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("nullRangeProperty")
                .rangeType(null)
                .build();

        assertEquals(PropertyType.STRING, getPropertyType(property));
    }

    @Test
    void shouldTestAllPropertyTypeEnumValues() {
        // Test that all enum values are defined
        PropertyType[] allTypes = PropertyType.values();

        assertEquals(17, allTypes.length);

        // Check specific enum values exist
        assertNotNull(PropertyType.valueOf("STRING"));
        assertNotNull(PropertyType.valueOf("DECIMAL"));
        assertNotNull(PropertyType.valueOf("INTEGER"));
        assertNotNull(PropertyType.valueOf("DATE"));
        assertNotNull(PropertyType.valueOf("BOOLEAN"));
        assertNotNull(PropertyType.valueOf("INTEGER_SCALE_1TO5"));
        assertNotNull(PropertyType.valueOf("DECIMAL_CM"));
        assertNotNull(PropertyType.valueOf("UNIT"));
        assertNotNull(PropertyType.valueOf("DECIMAL_KG"));
        assertNotNull(PropertyType.valueOf("DECIMAL_M2"));
        assertNotNull(PropertyType.valueOf("DECIMAL_M3"));
        assertNotNull(PropertyType.valueOf("CATEGORY"));
        assertNotNull(PropertyType.valueOf("URL"));
        assertNotNull(PropertyType.valueOf("MULTI_CATEGORY"));
        assertNotNull(PropertyType.valueOf("EMAIL_FORM"));
        assertNotNull(PropertyType.valueOf("RESOURCE_TYPE"));
        assertNotNull(PropertyType.valueOf("EMISSION"));
    }
}