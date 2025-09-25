package no.sirktek.taxonomy.loader;

import lombok.extern.slf4j.Slf4j;
import no.sirktek.taxonomy.model.CategoryInfo;
import no.sirktek.taxonomy.model.PropertyDefinition;
import no.sirktek.taxonomy.model.TaxonomyTree;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Loads the furniture taxonomy from RDF-S Turtle files using Apache Jena
 */
@Slf4j
public class RdfsTaxonomyLoader {

    private static final String FURNITURE_NAMESPACE = "http://taxonomy.sirktek.no/furniture#";

    /**
     * Load the base taxonomy from the Turtle file
     */
    public TaxonomyTree loadBaseTaxonomy() {
        return loadTaxonomyFromResource("/taxonomy/furniture-base.ttl");
    }

    /**
     * Load taxonomy from a specific resource file
     */
    public TaxonomyTree loadTaxonomyFromResource(String resourcePath) {
        log.debug("Loading taxonomy from resource: {}", resourcePath);

        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new TaxonomyLoadException("Could not find resource: " + resourcePath);
            }

            // Create Jena model and read Turtle data
            Model model = ModelFactory.createDefaultModel();
            model.read(inputStream, null, "TURTLE");

            return buildTaxonomyTree(model);

        } catch (Exception e) {
            throw new TaxonomyLoadException("Failed to load taxonomy from " + resourcePath, e);
        }
    }

    /**
     * Build the taxonomy tree from the RDF model
     */
    private TaxonomyTree buildTaxonomyTree(Model model) {
        log.debug("Building taxonomy tree from RDF model");

        // Get all classes that are subclasses of furniture or direct furniture classes
        Map<String, CategoryInfo> allCategories = new HashMap<>();
        Map<String, List<String>> parentChildMap = new HashMap<>();

        // First pass: create all category objects
        ResIterator classIterator = model.listSubjectsWithProperty(RDF.type, RDFS.Class);
        while (classIterator.hasNext()) {
            Resource classResource = classIterator.nextResource();

            if (classResource.getURI() != null && classResource.getURI().startsWith(FURNITURE_NAMESPACE)) {
                CategoryInfo categoryInfo = buildCategoryInfo(classResource, model);
                allCategories.put(categoryInfo.className(), categoryInfo);

                // Track parent-child relationships
                if (categoryInfo.parentClassName() != null) {
                    parentChildMap.computeIfAbsent(categoryInfo.parentClassName(), k -> new ArrayList<>())
                                  .add(categoryInfo.className());
                }
            }
        }

        // Build the hierarchy recursively
        Map<String, CategoryInfo> categoriesWithChildren = buildHierarchy(allCategories, parentChildMap);

        // Find root categories (those with no parent)
        List<CategoryInfo> rootCategories = categoriesWithChildren.values().stream()
                .filter(CategoryInfo::isRoot)
                .sorted(Comparator.comparing(CategoryInfo::englishName))
                .collect(Collectors.toList());

        log.info("Loaded taxonomy with {} total categories, {} root categories",
                allCategories.size(), rootCategories.size());

        return TaxonomyTree.builder()
                .rootCategories(rootCategories)
                .build();
    }

    /**
     * Build the category hierarchy recursively
     */
    private Map<String, CategoryInfo> buildHierarchy(Map<String, CategoryInfo> allCategories,
                                                     Map<String, List<String>> parentChildMap) {
        Map<String, CategoryInfo> categoriesWithChildren = new HashMap<>();

        // First, create all categories without children
        for (CategoryInfo category : allCategories.values()) {
            categoriesWithChildren.put(category.className(), category);
        }

        // Then, recursively build children for each category
        for (CategoryInfo category : allCategories.values()) {
            CategoryInfo categoryWithChildren = buildCategoryWithChildren(category, allCategories, parentChildMap);
            categoriesWithChildren.put(category.className(), categoryWithChildren);
        }

        return categoriesWithChildren;
    }

    /**
     * Build a category with all its recursive children
     */
    private CategoryInfo buildCategoryWithChildren(CategoryInfo category,
                                                   Map<String, CategoryInfo> allCategories,
                                                   Map<String, List<String>> parentChildMap) {
        List<String> childClassNames = parentChildMap.getOrDefault(category.className(), Collections.emptyList());

        List<CategoryInfo> children = new ArrayList<>();
        for (String childClassName : childClassNames) {
            CategoryInfo childCategory = allCategories.get(childClassName);
            if (childCategory != null) {
                // Recursively build the child with its children
                CategoryInfo childWithChildren = buildCategoryWithChildren(childCategory, allCategories, parentChildMap);
                children.add(childWithChildren);
            }
        }

        // Sort children by English name
        children.sort(Comparator.comparing(CategoryInfo::englishName));

        return CategoryInfo.builder()
                .className(category.className())
                .englishName(category.englishName())
                .norwegianName(category.norwegianName())
                .description(category.description())
                .parentClassName(category.parentClassName())
                .uri(category.uri())
                .properties(category.properties())
                .children(children)
                .build();
    }

    /**
     * Build a CategoryInfo object from an RDF resource
     */
    private CategoryInfo buildCategoryInfo(Resource classResource, Model model) {
        String uri = classResource.getURI();
        String className = getLocalName(uri);

        // Get labels
        String englishName = getLabel(classResource, "en");
        String norwegianName = getLabel(classResource, "no");

        if (englishName == null) {
            englishName = className; // Fallback to class name
        }

        // Get comment/description
        String description = null;
        Statement commentStmt = classResource.getProperty(RDFS.comment);
        if (commentStmt != null) {
            description = commentStmt.getString();
        }

        // Get parent class
        String parentClassName = null;
        StmtIterator subClassStatements = classResource.listProperties(RDFS.subClassOf);
        while (subClassStatements.hasNext()) {
            Statement stmt = subClassStatements.nextStatement();
            Resource parentResource = stmt.getResource();
            if (parentResource.getURI() != null && parentResource.getURI().startsWith(FURNITURE_NAMESPACE)) {
                parentClassName = getLocalName(parentResource.getURI());
                break; // Take the first furniture-related parent
            }
        }

        // Get properties defined for this class
        List<PropertyDefinition> properties = getPropertiesForClass(classResource, model);

        return CategoryInfo.builder()
                .className(className)
                .englishName(englishName)
                .norwegianName(norwegianName)
                .description(description)
                .parentClassName(parentClassName)
                .uri(uri)
                .properties(properties)
                .children(Collections.emptyList()) // Will be populated in second pass
                .build();
    }

    /**
     * Get the localized label for a resource
     */
    private String getLabel(Resource resource, String language) {
        StmtIterator labelStatements = resource.listProperties(RDFS.label);
        while (labelStatements.hasNext()) {
            Statement stmt = labelStatements.nextStatement();
            Literal literal = stmt.getLiteral();
            if (literal != null && language.equals(literal.getLanguage())) {
                return literal.getString();
            }
        }
        return null;
    }

    /**
     * Get properties that have this class as their domain
     */
    private List<PropertyDefinition> getPropertiesForClass(Resource classResource, Model model) {
        List<PropertyDefinition> properties = new ArrayList<>();
        String classUri = classResource.getURI();

        ResIterator propertyIterator = model.listSubjectsWithProperty(RDF.type, RDF.Property);
        while (propertyIterator.hasNext()) {
            Resource propertyResource = propertyIterator.nextResource();

            if (propertyResource.getURI() != null && propertyResource.getURI().startsWith(FURNITURE_NAMESPACE)) {
                // Check if this property has our class as domain
                if (hasDomain(propertyResource, classUri)) {
                    PropertyDefinition propertyDef = buildPropertyDefinition(propertyResource);
                    properties.add(propertyDef);
                }
            }
        }

        return properties;
    }

    /**
     * Check if a property has the specified class as its domain
     */
    private boolean hasDomain(Resource propertyResource, String classUri) {
        StmtIterator domainStatements = propertyResource.listProperties(RDFS.domain);
        while (domainStatements.hasNext()) {
            Statement stmt = domainStatements.nextStatement();
            Resource domainResource = stmt.getResource();
            if (classUri.equals(domainResource.getURI())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Build a PropertyDefinition from an RDF property resource
     */
    private PropertyDefinition buildPropertyDefinition(Resource propertyResource) {
        String uri = propertyResource.getURI();
        String name = getLocalName(uri);
        String englishLabel = getLabel(propertyResource, "en");
        String norwegianLabel = getLabel(propertyResource, "no");

        // Get range type
        String rangeType = null;
        Statement rangeStmt = propertyResource.getProperty(RDFS.range);
        if (rangeStmt != null) {
            rangeType = rangeStmt.getResource().getURI();
        }

        // Get domain
        String domainClass = null;
        Statement domainStmt = propertyResource.getProperty(RDFS.domain);
        if (domainStmt != null) {
            domainClass = getLocalName(domainStmt.getResource().getURI());
        }

        return PropertyDefinition.builder()
                .name(name)
                .englishLabel(englishLabel)
                .norwegianLabel(norwegianLabel)
                .uri(uri)
                .rangeType(rangeType)
                .domainClass(domainClass)
                .description(null) // Could add comments if needed
                .build();
    }

    /**
     * Extract the local name from a URI
     */
    private String getLocalName(String uri) {
        if (uri == null) return null;
        int hashIndex = uri.lastIndexOf('#');
        if (hashIndex >= 0) {
            return uri.substring(hashIndex + 1);
        }
        int slashIndex = uri.lastIndexOf('/');
        if (slashIndex >= 0) {
            return uri.substring(slashIndex + 1);
        }
        return uri;
    }

    /**
     * Exception thrown when taxonomy loading fails
     */
    public static class TaxonomyLoadException extends RuntimeException {
        public TaxonomyLoadException(String message) {
            super(message);
        }

        public TaxonomyLoadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}