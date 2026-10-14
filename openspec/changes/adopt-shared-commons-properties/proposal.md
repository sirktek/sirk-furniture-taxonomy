# Proposal — adopt-shared-commons-properties

## Why

taxonomy-commons 3.14 (sirk-taxonomy-commons PR #2, change `shared-commons-properties`) makes
`schema:domainIncludes` opt-ins work: every domain loader now reads `common-base.ttl` and
attaches commons properties a class opts into. Until now the six opt-ins in `furniture-base.ttl`
(lines 286-287 and 292-295) were dead triples. 3.14 also deletes `common:bookValue` /
`common:taxValue` / `common:wealthTaxValue` and reshapes accounting as `ledgerAccount`,
`depreciationGroup` and `valuations` (`common:privateSet "accounting"`).

Product owner decisions (2026-10-13, round 3): accounting opt-ins on Furniture are deleted (they
may come back later, in accounting); the emission opt-ins stay and now attach. Central's monthly
publishing limit is reached, so Maven Central becomes a manual step, as in commons PR #2.

GitHub issue: sirktek/sirk-furniture-taxonomy#4.

## What changes

- **Bump** `no.sirktek:taxonomy-commons` 3.13 → 3.14.
- **`furniture-base.ttl`:** delete the four accounting opt-ins (`ledgerAccount`, `bookValue`,
  `taxValue`, `depreciationGroup`) and their comment; keep `common:emissionPerUnit` and
  `common:consistsOf` → `furniture:Furniture`, rewording their comment to say the loader attaches
  them.
- **Tests:** `CommonPropertyOptInTest` asserts through `FurnitureTaxonomyService` instead of a raw
  Jena model: Furniture has `emissionPerUnit` and `consistsOf` (common IRIs, no `privateSet`) and
  none of `ledgerAccount`, `depreciationGroup`, `valuations`, `bookValue`, `taxValue`,
  `wealthTaxValue`.
- **CI:** drop the Maven Central steps from `build_tag_and_release.yml`; new
  `publish_maven_central.yml` (`workflow_dispatch`, input `tag`), adapted from commons; README
  "Releases" section.
- **README:** "Accounting and Tax Properties" becomes "Common properties".

Release: next 3.x on merge. The bump and the TTL edit ship in one release: the bump alone would
attach `ledgerAccount` / `depreciationGroup` to Furniture.

## Impact

- sirk-orgadmin-backend (GitHub Packages, pins furniture 3.21 and commons 3.13): after it bumps,
  every furniture category shows empty `emissionPerUnit` (EMISSION) and `consistsOf`
  (CONSISTS_OF) slots from the taxonomy, and no accounting. Its change
  `private-property-definitions` already expects exactly this (spec scenario "Furniture gains its
  emission properties"). Its commons bump must land with or after this release.
- sirk-app-backend pins furniture-taxonomy 3.20 from Maven Central. It does not need this release
  and stays on 3.20; nothing is published to Central for it.
- No Java API change in this repo.

## Not in scope

- **orgadmin follow-up (its own change, `private-property-definitions`):** read-only finding in
  `CategoryPropertyInheritanceService`. The property map is keyed by local name, so the RDF-S
  `emissionPerUnit` / `consistsOf` do not duplicate stored or CALCULATED values: they merge into
  one entry, typed EMISSION / CONSISTS_OF by `CommonPropertyDefinition` (`convertToPropertyType`
  ~683). The CALCULATED step (~338-360) still runs: the RDF-S entry has no value, so
  `emissionHasContent` is false and it is overwritten with the computed value and source
  CALCULATED. Visible change: categories without stored values now show the two empty slots
  (OWN on Furniture, INHERITED below). orgadmin must: pin this with its planned before/after
  furniture tree test; fix the stale `convertToPropertyType` comment ("furniture:emissionPerUnit
  has range common:EmissionEntry") — furniture declares no `furniture:emissionPerUnit`; and
  replace `case ASSET_VALUE` with `ASSET_VALUATIONS` there, which its commons 3.14 bump requires.
- **Same-named redeclarations** across namespaces (width, length, height, weight, volume, color,
  material, variant, serialNumber, model, operationsAndMaintenanceManual): follow-up, each needs a
  commons term.
- Commercial terms (`monthlyRent`, `marketValue`, …) on Furniture: not requested.
- Publishing this or any earlier release to Maven Central.

## Open questions for the product owner

None.
