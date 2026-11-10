## ADDED Requirements

### Requirement: Furniture carries the commons age and new-price properties

The furniture taxonomy SHALL opt `furniture:Furniture` into `common:yearOfManufacture`,
`common:installationYear` and `common:newPriceExVAT`, and SHALL NOT declare
`furniture:productionDate`. Subclasses such as `OfficeChair` inherit them.

#### Scenario: Age properties on Furniture
- **WHEN** `FurnitureTaxonomyService` loads the taxonomy and the Furniture category is read
- **THEN** it has one property `yearOfManufacture` with `uri` `http://taxonomy.sirktek.no/common#yearOfManufacture` and one `installationYear` with `uri` `http://taxonomy.sirktek.no/common#installationYear`, both with `rangeType` `http://www.w3.org/2001/XMLSchema#integer` and no `privateSet`, and no property `productionDate`

#### Scenario: New price on Furniture
- **WHEN** `FurnitureTaxonomyService` loads the taxonomy and the Furniture category is read
- **THEN** it has one property `newPriceExVAT` with `uri` `http://taxonomy.sirktek.no/common#newPriceExVAT`, `rangeType` `http://www.w3.org/2001/XMLSchema#decimal` and `privateSet` `commercial`

### Requirement: Furniture carries valuations and no other accounting property

The furniture taxonomy SHALL opt `furniture:Furniture` into `common:valuations` and SHALL NOT opt
any furniture class into `common:ledgerAccount` or `common:depreciationGroup`.

#### Scenario: Valuations on Furniture
- **WHEN** `FurnitureTaxonomyService` loads the taxonomy and the Furniture category is read
- **THEN** it has one property `valuations` with `uri` `http://taxonomy.sirktek.no/common#valuations` and `privateSet` `accounting`, and none of `ledgerAccount`, `depreciationGroup`, `bookValue`, `taxValue`, `wealthTaxValue`

## REMOVED Requirements

### Requirement: Furniture carries no accounting properties

**Reason**: The product owner opted Furniture into `common:valuations` (2026-11-09).
**Migration**: Replaced by "Furniture carries valuations and no other accounting property".
