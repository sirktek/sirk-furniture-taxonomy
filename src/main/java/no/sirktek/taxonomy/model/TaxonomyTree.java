package no.sirktek.taxonomy.model;

import lombok.Builder;

import java.util.List;

/**
 * Represents a complete taxonomy tree with root categories and their hierarchical children
 */
@Builder
public record TaxonomyTree(
        List<CategoryInfo> rootCategories
) {
    /**
     * Find a category by its English class name (URI fragment)
     */
    public CategoryInfo findByClassName(String className) {
        return findCategoryRecursively(rootCategories, className);
    }


    private CategoryInfo findCategoryRecursively(List<CategoryInfo> categories, String className) {
        for (CategoryInfo category : categories) {
            if (category.className().equals(className)) {
                return category;
            }
            CategoryInfo found = findCategoryRecursively(category.children(), className);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

}