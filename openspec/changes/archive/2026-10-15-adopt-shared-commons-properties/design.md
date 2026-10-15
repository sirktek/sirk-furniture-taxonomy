# Design — adopt-shared-commons-properties

## Precedent

- **Opt-ins:** a domain opts a class into a commons property with one
  `common:<p> schema:domainIncludes furniture:<Class> .` triple in its own TTL (lines 282-295
  today). The property, its range, labels and `privateSet` live in commons. This change follows
  it: four triples removed, two kept.
- **Shared terms live in commons:** `emissionPerUnit` and `consistsOf` moved to commons in v3.0
  (TTL header, lines 8-13); furniture declares no `furniture:emissionPerUnit` or
  `furniture:consistsOf`. So the opt-ins stay rather than being replaced by a redeclaration (the
  workaround machine uses for `controlChecklist` only because no commons term exists).
- **Type mapping:** `PropertyDefinitionTest.shouldNotResolveCommonRangeMarkersToFurniturePropertyType`
  pins that `FurniturePropertyDefinition` leaves `common:EmissionEntry` / `ConsistsOfEntry` to
  `CommonPropertyDefinition` (it yields STRING); consumers route through
  `CommonPropertyDefinition` first. Unchanged.
- **Tests through the service:** machine's `ControlChecklistPropertyTest` reads
  `getCategoryByClassName(...).properties()`. `CommonPropertyOptInTest` checked a raw Jena model
  only because the loader did not attach commons properties; with 3.14 it follows that pattern.
- **CI:** commons PR #2 (commit `7edfcc7`) and sirk-machine-taxonomy's
  `adopt-shared-commons-properties` D3: Central steps removed from `build_tag_and_release.yml`,
  `publish_maven_central.yml` on `workflow_dispatch` with input `tag`, README "Releases". Copied.

No departure.

## Inventory of everything new or removed

| Kind | Item | Reader |
| --- | --- | --- |
| pom | `taxonomy-commons` 3.13 → 3.14 | build |
| TTL removed | opt-ins `common:ledgerAccount`, `bookValue`, `taxValue`, `depreciationGroup` → Furniture and their comment | — |
| TTL comment | emission opt-in comment reworded (triples unchanged) | — |
| Workflow | `.github/workflows/publish_maven_central.yml` | run by hand |
| Workflow removed | Central steps in `build_tag_and_release.yml` | — |
| Docs | README "Releases", "Common properties" | — |

No new class, dependency, config, triple or Java constant.

## Inventory: what Furniture gains and loses

The loader attaches properties per class (no inheritance in the loader); subclasses get them
through the consumer's hierarchy walk, as for every Furniture property. `furniture:model` has range
`common:Model`; commons declares no individuals, so `enumValues` stays empty. Commons properties
with a commons domain (`address`, `resourceVariant`, … on Model / Resource / Manufacturer) and
`furniture:furniture` / `furniture:operationsAndMaintenanceManual` (domain includes `common:Model`)
attach only to commons classes, which the furniture tree excludes. `domainClass` is null for
`domainIncludes`-only properties (reads `rdfs:domain` only), as for any opt-in.

| Furniture with | Gains from commons |
| --- | --- |
| commons 3.13 (today) | nothing (loader ignored opt-ins) |
| commons 3.14, TTL unchanged | `emissionPerUnit`, `consistsOf`, `ledgerAccount`, `depreciationGroup` (privateSet `accounting`, ten saldogrupper); `bookValue`, `taxValue` inert (deleted in commons) |
| commons 3.14, this change | `emissionPerUnit` (range `common:EmissionEntry`), `consistsOf` (range `common:ConsistsOfEntry`), both public (no `privateSet`) |

No furniture property is named `emissionPerUnit` or `consistsOf`, so no key clash (commons design
D1 constraint).

## Decisions

### D1 — TTL

Replace lines 282-295 (emission comment, two emission triples, accounting comment, four accounting
triples) with:

```turtle
# emissionPerUnit and consistsOf are declared in common-base.ttl (domains
# common:Model and common:Resource). These opt-ins extend them to
# furniture:Furniture; the loader merges common-base.ttl and attaches them.
common:emissionPerUnit schema:domainIncludes furniture:Furniture .
common:consistsOf schema:domainIncludes furniture:Furniture .
```

### D2 — Tests

`CommonPropertyOptInTest` is rewritten on `FurnitureTaxonomyService` (machine's
`ControlChecklistPropertyTest` helper pattern, reading `Furniture`'s `properties()`):

- `furnitureOptsIntoEmissionProperties`: Furniture has `emissionPerUnit` with `uri`
  `http://taxonomy.sirktek.no/common#emissionPerUnit`, `rangeType`
  `http://taxonomy.sirktek.no/common#EmissionEntry`, `privateSet` null; and `consistsOf` with
  `uri` `…common#consistsOf`, `rangeType` `…common#ConsistsOfEntry`, `privateSet` null.
- `furnitureHasNoAccountingProperties`: Furniture has none of `ledgerAccount`,
  `depreciationGroup`, `valuations`, `bookValue`, `taxValue`, `wealthTaxValue`.

The raw-Jena test `assetClassesOptIntoAccountingProperties` and the class Javadoc about "only
the merged graph in the consumer sees" are deleted. `TaxonomyServiceTest` /
`PropertyDefinitionTest` change only if the bump breaks them (`PropertyDefinition` gained
`privateSet`; the tests use the builder).

### D3 — CI

As sirk-machine-taxonomy D3, verbatim apart from repo names:

- `build_tag_and_release.yml`: delete the "Publish to Maven Central" comment, the second
  `setup-java` (server `central`) and the Central deploy step; the SNAPSHOT guard message becomes
  "Cannot release a SNAPSHOT version". The GitHub-server `setup-java` stays.
- `publish_maven_central.yml`: commons' file, adapted because this repo resolves
  taxonomy-commons from GitHub Packages and `setup-java` writes one server into settings.xml:
  1. checkout `ref: ${{ inputs.tag }}`;
  2. `setup-java` with `server-id: github` (as the release workflow);
  3. "Resolve dependencies from GitHub Packages": `./mvnw -U compile --batch-mode
     --no-transfer-progress` with `GITHUB_ACTOR` / `GITHUB_TOKEN`;
  4. `setup-java` with `server-id: central` and GPG (commons' step verbatim);
  5. "Set Release Version" and "Publish to Maven Central" as commons.
  Header comment and input description (`e.g. 3.22`) as commons; `permissions: contents: read,
  packages: read`.
- README "Releases": commons' text, naming sirk-orgadmin-backend as the GitHub Packages consumer
  and sirk-app-backend as the Central consumer, plus: a Central release needs the taxonomy-commons
  version it depends on published to Central first.

### D4 — README

"Accounting and Tax Properties" becomes "Common properties": Furniture opts into
`common:emissionPerUnit` and `common:consistsOf`; definitions in the taxonomy-commons README;
`CommonPropertyOptInTest` pins them through the loader.

## Risks

- Shipping the bump without the TTL edit would expose accounting on Furniture in orgadmin's tree:
  one group, one PR, one release.
- Empty `emissionPerUnit` / `consistsOf` slots appear on every furniture category in orgadmin
  once it bumps; intended (PO round 2/3) and pinned by orgadmin's own change.
- `publish_maven_central.yml` cannot be exercised before merge; dry check with `actionlint` if
  installed, else YAML review against commons and machine.
