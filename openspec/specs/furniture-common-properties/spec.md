# furniture-common-properties Specification

## Purpose
TBD - created by archiving change adopt-shared-commons-properties. Update Purpose after archive.
## Requirements
### Requirement: Furniture carries the commons emission properties

The furniture taxonomy SHALL opt `furniture:Furniture` into `common:emissionPerUnit` and
`common:consistsOf`, so that the loaded Furniture category has both properties with their commons
IRIs and no `privateSet`.

#### Scenario: Emission properties on Furniture
- **WHEN** `FurnitureTaxonomyService` loads the taxonomy and the Furniture category is read
- **THEN** it has a property `emissionPerUnit` with `uri` `http://taxonomy.sirktek.no/common#emissionPerUnit` and `rangeType` `http://taxonomy.sirktek.no/common#EmissionEntry`, and a property `consistsOf` with `uri` `http://taxonomy.sirktek.no/common#consistsOf` and `rangeType` `http://taxonomy.sirktek.no/common#ConsistsOfEntry`, both with no `privateSet`

### Requirement: Furniture carries no accounting properties

The furniture taxonomy SHALL NOT opt any furniture class into commons accounting properties.

#### Scenario: No accounting on Furniture
- **WHEN** `FurnitureTaxonomyService` loads the taxonomy and the Furniture category is read
- **THEN** it has none of the properties `ledgerAccount`, `depreciationGroup`, `valuations`, `bookValue`, `taxValue`, `wealthTaxValue`

### Requirement: Maven Central publishing is manual

A merge to `main` SHALL tag `3.<run number>` and publish to GitHub Packages only. Publishing a
release tag to Maven Central SHALL be a separate `workflow_dispatch` workflow taking the tag as
input.

#### Scenario: Merge to main
- **WHEN** a commit is pushed to `main`
- **THEN** `build_tag_and_release.yml` tags and deploys to GitHub Packages and runs no Maven Central step

#### Scenario: Manual Central release
- **WHEN** "Publish release to Maven Central" is run with input `tag` `3.22`
- **THEN** the workflow checks out `3.22`, resolves taxonomy-commons from GitHub Packages, sets version `3.22` and deploys with the `deployment` profile to Maven Central

