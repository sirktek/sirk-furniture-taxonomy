
## Handoff — Group 1

Changed: `pom.xml` (taxonomy-commons 3.14), `furniture-base.ttl` (D1 block; accounting opt-ins removed),
`CommonPropertyOptInTest` (rewritten on `FurnitureTaxonomyService`), `build_tag_and_release.yml`
(Central steps removed, SNAPSHOT message), new `publish_maven_central.yml` (copied from machine's
branch, unchanged: it had no repo-specific text), README ("Common properties", "Releases").
Verified: full `mvnq test` green. `TaxonomyServiceTest`/`PropertyDefinitionTest` needed no change.
Unverified: `publish_maven_central.yml` cannot run before merge; actionlint not run if not installed.
