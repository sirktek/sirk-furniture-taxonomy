package no.sirktek.taxonomy.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

        assertEquals(PropertyDefinition.PropertyType.STRING, property.getPropertyType());
    }

    @Test
    void shouldDetectDecimalKgPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("weight")
                .rangeType("http://www.w3.org/2001/XMLSchema#decimal")
                .build();

        assertEquals(PropertyDefinition.PropertyType.DECIMAL_KG, property.getPropertyType());
    }

    @Test
    void shouldDetectDecimalM3PropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("volume")
                .rangeType("http://www.w3.org/2001/XMLSchema#decimal")
                .build();

        assertEquals(PropertyDefinition.PropertyType.DECIMAL_M3, property.getPropertyType());
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

        assertEquals(PropertyDefinition.PropertyType.DECIMAL_CM, lengthProperty.getPropertyType());
        assertEquals(PropertyDefinition.PropertyType.DECIMAL_CM, widthProperty.getPropertyType());
        assertEquals(PropertyDefinition.PropertyType.DECIMAL_CM, heightProperty.getPropertyType());
    }

    @Test
    void shouldDetectGenericDecimalPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("genericDecimal")
                .rangeType("http://www.w3.org/2001/XMLSchema#decimal")
                .build();

        assertEquals(PropertyDefinition.PropertyType.DECIMAL, property.getPropertyType());
    }

    @Test
    void shouldDetectDatePropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("productionDate")
                .rangeType("http://www.w3.org/2001/XMLSchema#date")
                .build();

        assertEquals(PropertyDefinition.PropertyType.DATE, property.getPropertyType());
    }

    @Test
    void shouldDetectBooleanPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("isAvailable")
                .rangeType("http://www.w3.org/2001/XMLSchema#boolean")
                .build();

        assertEquals(PropertyDefinition.PropertyType.BOOLEAN, property.getPropertyType());
    }

    @Test
    void shouldDetectUrlPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("homepage")
                .rangeType("http://www.w3.org/2001/XMLSchema#anyURI")
                .build();

        assertEquals(PropertyDefinition.PropertyType.URL, property.getPropertyType());
    }

    @Test
    void shouldDetectIntegerPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("quantity")
                .rangeType("http://www.w3.org/2001/XMLSchema#integer")
                .build();

        assertEquals(PropertyDefinition.PropertyType.INTEGER, property.getPropertyType());
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

        assertEquals(PropertyDefinition.PropertyType.CATEGORY, manufacturerProperty.getPropertyType());
        assertEquals(PropertyDefinition.PropertyType.CATEGORY, furnitureProperty.getPropertyType());
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

        assertEquals(PropertyDefinition.PropertyType.EMISSION, emissionProperty.getPropertyType());
        assertEquals(PropertyDefinition.PropertyType.EMISSION, emissionPerUnitProperty.getPropertyType());
    }

    @Test
    void shouldDetectUnitPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("unit")
                .rangeType("http://www.w3.org/2001/XMLSchema#string")
                .build();

        assertEquals(PropertyDefinition.PropertyType.UNIT, property.getPropertyType());
    }

    @Test
    void shouldDetectResourceTypePropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("resourceType")
                .rangeType("http://www.w3.org/2001/XMLSchema#string")
                .build();

        assertEquals(PropertyDefinition.PropertyType.RESOURCE_TYPE, property.getPropertyType());
    }

    @Test
    void shouldDefaultToStringForUnknownTypes() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("unknownProperty")
                .rangeType("http://unknown.com/type")
                .build();

        assertEquals(PropertyDefinition.PropertyType.STRING, property.getPropertyType());
    }

    @Test
    void shouldHandleNullRangeType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("nullRangeProperty")
                .rangeType(null)
                .build();

        assertEquals(PropertyDefinition.PropertyType.STRING, property.getPropertyType());
    }

    @Test
    void shouldTestAllPropertyTypeEnumValues() {
        // Test that all enum values are defined
        PropertyDefinition.PropertyType[] allTypes = PropertyDefinition.PropertyType.values();

        assertEquals(17, allTypes.length);

        // Check specific enum values exist
        assertNotNull(PropertyDefinition.PropertyType.valueOf("STRING"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("DECIMAL"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("INTEGER"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("DATE"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("BOOLEAN"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("INTEGER_SCALE_1TO5"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("DECIMAL_CM"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("UNIT"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("DECIMAL_KG"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("DECIMAL_M2"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("DECIMAL_M3"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("CATEGORY"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("URL"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("MULTI_CATEGORY"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("EMAIL_FORM"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("RESOURCE_TYPE"));
        assertNotNull(PropertyDefinition.PropertyType.valueOf("EMISSION"));
    }
}