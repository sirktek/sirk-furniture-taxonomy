# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
This is a Java library that provides access to a standardized furniture taxonomy defined in RDF-S. It uses Apache Jena for RDF processing and provides furniture category information with English class names and bilingual labels stored in the RDF-S file.

## Common Commands

### Build and Test
```bash
# Using system Maven
mvn compile          # Compile source code
mvn test             # Run unit tests
mvn package          # Build JAR artifact
mvn install          # Install to local Maven repository
mvn clean            # Clean build artifacts

# Using Maven wrapper (recommended)
./mvnw compile       # Compile source code
./mvnw test          # Run unit tests
./mvnw package       # Build JAR artifact
./mvnw install       # Install to local Maven repository
./mvnw clean         # Clean build artifacts
```

### Code Quality
```bash
# Using system Maven
mvn clean -U -Dpmd.printFailingErrors=true pmd:check    # PMD static analysis
mvn clean -DskipITs=false -U verify                    # Full build with integration tests

# Using Maven wrapper (recommended)
./mvnw clean -U -Dpmd.printFailingErrors=true pmd:check    # PMD static analysis
./mvnw clean -DskipITs=false -U verify                     # Full build with integration tests
```

### CI/CD Commands
```bash
# Using system Maven
mvn clean -U package -Drevision=1.{build_number}       # Build with versioning
mvn deploy -Drevision=1.{build_number}                 # Deploy to GitHub Packages

# Using Maven wrapper (recommended)
./mvnw clean -U package -Drevision=1.{build_number}    # Build with versioning
./mvnw deploy -Drevision=1.{build_number}              # Deploy to GitHub Packages
```

## Architecture

### Layered Structure
- **Model Layer** (`model/`): Core data structures (`CategoryInfo`, `TaxonomyTree`, `PropertyDefinition`)
- **Loader Layer** (`loader/`): RDF-S parsing using Apache Jena (`RdfsTaxonomyLoader`)
- **Service Layer** (`TaxonomyService`): Main public API with thread-safe caching

### Data Flow
1. RDF-S Turtle file (`furniture-base.ttl`) defines furniture hierarchy with bilingual labels
2. Apache Jena parses RDF-S into Java objects
3. Service layer provides caching and high-level API
4. Clients consume via simple Java API (English class names)

### Key Files
- `/src/main/resources/taxonomy/furniture-base.ttl` - RDF-S taxonomy definition (312 lines)
- `TaxonomyService.java` - Main API with singleton caching pattern
- `PropertyDefinition.java` - Property definitions with type detection logic

## Development Notes

### Technology Stack
- Java 17 (CI uses Java 21)
- Apache Maven 3.9.9+ (includes Maven wrapper)
- Apache Jena 5.2.0 for RDF processing
- Lombok 1.18.36 for code generation
- JUnit Jupiter 5.11.3 for testing

### Threading and Caching
- Uses thread-safe singleton pattern for taxonomy loading
- Double-checked locking for cache initialization
- Cache invalidation via `reloadBaseTaxonomy()`

### Language Support
- English URIs and class names for API compatibility (`http://taxonomy.sirktek.no/furniture#`)
- Bilingual labels (English/Norwegian) stored in RDF-S file for frontend consumption
- Frontend handles translation - Java library only provides English class names

### CI/CD Pipeline
- **Pull Requests**: PMD analysis + full test suite
- **Main Branch**: Auto-build, version tagging (1.{build_number}), deploy to GitHub Packages
- **Maven Wrapper**: Available for development (./mvnw, ./mvnw.cmd)
- **Versioning**: Uses `${revision}` property for dynamic versioning