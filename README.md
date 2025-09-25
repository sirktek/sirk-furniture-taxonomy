# Sirktek Furniture Taxonomy Library

[![Maven Central Version](https://img.shields.io/maven-central/v/no.sirktek/furniture-taxonomy)](https://search.maven.org/search?q=g:%22no.sirktek%22%20AND%20a:%22furniture-taxonomy%22)

[Build, tag and release Java project ](https://github.com/sirktek/sirk-furniture-taxonomy/actions/workflows/build_tag_and_release.yml/badge.svg)

A Java library that provides access to a standardized furniture taxonomy defined in RDF-S (Resource Description Framework Schema) with Turtle serialization format.

## Features

- **RDF-S based taxonomy**: Formal ontology structure using W3C standards
- **English URIs**: Standardized English class names for international compatibility
- **Bilingual labels**: Norwegian and English labels stored in RDF-S for frontend use
- **Java API**: Easy-to-use service for loading and querying taxonomy
- **Caching**: Efficient in-memory caching of parsed taxonomy
- **Apache Jena**: Robust RDF processing using industry-standard library

## Quick Start

### Maven Dependency

```xml
<dependency>
    <groupId>no.sirktek</groupId>
    <artifactId>furniture-taxonomy</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Usage

```java
// Create service instance
TaxonomyService taxonomyService = new TaxonomyService();

// Load the complete taxonomy tree
TaxonomyTree taxonomy = taxonomyService.loadBaseTaxonomy();

// Find category by English class name
Optional<CategoryInfo> chair = taxonomyService.getCategoryByClassName("Chair");
Optional<CategoryInfo> table = taxonomyService.getCategoryByClassName("Table");

// Check if English class name exists in taxonomy
boolean exists = taxonomyService.isBaseTaxonomyClass("OfficeChair"); // -> true

// Get taxonomy statistics
TaxonomyService.TaxonomyStats stats = taxonomyService.getStats();
System.out.println("Total categories: " + stats.totalCategories());
System.out.println("Root categories: " + stats.rootCategories());

// Access bilingual labels from CategoryInfo
if (chair.isPresent()) {
    CategoryInfo chairInfo = chair.get();
    String englishName = chairInfo.englishName(); // "Chair"
    String norwegianName = chairInfo.norwegianName(); // "Stol"
}
```

## Taxonomy Structure

The furniture taxonomy is organized hierarchically, e.g.:

```
Furniture
├── Table
│   ├── Height Adjustable Table
│   ├── Office Desk
│   └── Conference Table
├── Storage Furniture
│   ├── Cabinet
│   ├── Cabinet Unit
│   │   ├── Tower Cabinet
│   │   └── Steel Cabinet
│   └── Shelf
└── Seating Furniture
    ├── Chair
    │   ├── Office Chair
    │   └── Conference Chair
    ├── Sofa
    └── Stool
   
```

Additional global categories:
- **Manufacturer** (manufacturer): Furniture suppliers
- **Model** (model): Specific product models
- **Resource** (resource): Materials and resources

## RDF-S Schema

The taxonomy is defined using RDF-S in Turtle format with:

- **Classes**: Furniture categories with `rdfs:subClassOf` relationships
- **Properties**: Attributes like dimensions, materials, emissions
- **Labels**: Bilingual labels in English (`@en`) and Norwegian (`@no`)
- **Domains/Ranges**: Proper typing for all properties

## Architecture

- **Model Layer**: `CategoryInfo`, `TaxonomyTree`, `PropertyDefinition` POJOs
- **Loader Layer**: `RdfsTaxonomyLoader` using Apache Jena for RDF parsing
- **Service Layer**: `TaxonomyService` providing high-level API with caching

## Testing

Run tests with:
```bash
mvn test
```

The test suite verifies:
- RDF-S parsing and taxonomy loading
- Category lookup by English class names
- Property definition type detection
- Taxonomy statistics and caching
- All PropertyDefinition functionality

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
