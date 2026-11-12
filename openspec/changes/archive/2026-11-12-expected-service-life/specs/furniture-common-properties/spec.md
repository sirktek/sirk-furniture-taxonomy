## ADDED Requirements

### Requirement: Furniture carries the commons expected service life

The furniture taxonomy SHALL opt `furniture:Furniture` into `common:expectedServiceLife`.
Subclasses such as `OfficeChair` inherit it.

#### Scenario: Expected service life on Furniture
- **WHEN** `FurnitureTaxonomyService` loads the taxonomy and the Furniture category is read
- **THEN** it has one property `expectedServiceLife` with `uri` `http://taxonomy.sirktek.no/common#expectedServiceLife`, `rangeType` `http://www.w3.org/2001/XMLSchema#integer` and no `privateSet`
