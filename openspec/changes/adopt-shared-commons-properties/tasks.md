# Tasks — adopt-shared-commons-properties

One group, one implementer in a fresh context with this file, design.md and
`specs/furniture-common-properties/spec.md`. Touch only the listed files (never the untracked IDE
files `.classpath`, `.factorypath`, `.project`, `.settings/`), run only the listed tests, tick the
tasks, append a short `## Handoff — Group 1` note (what changed, what is unverified) to
`handoff.md` in this directory, and stop when the tests pass. Maven through `mvnq`
(`~/.local/bin/mvnq`, same arguments as `./mvnw`); never `clean`. No Java code specific to a
taxonomy class or property.

Suggested model: sonnet (design pins every name, line and workflow step).

### Group 0: Issue and branch

- [x] 0.1 Open a GitHub issue in sirktek/sirk-furniture-taxonomy ("Adopt shared commons
      properties: emission opt-ins attach, drop accounting opt-ins, manual Maven Central") linking
      sirktek/sirk-taxonomy-commons#1; put its number in proposal.md "GitHub issue".
- [x] 0.2 Branch `feature/#<id>-adopt-shared-commons-properties` from `main`.

### Group 1: Commons 3.14, emission opt-ins only, manual Central

Design D1–D4.

Files: `pom.xml`, `src/main/resources/taxonomy/furniture-base.ttl`,
`src/test/java/no/sirktek/taxonomy/CommonPropertyOptInTest.java`,
`src/test/java/no/sirktek/taxonomy/TaxonomyServiceTest.java` and
`src/test/java/no/sirktek/taxonomy/model/PropertyDefinitionTest.java` (only if the bump breaks
them), `.github/workflows/build_tag_and_release.yml`,
new `.github/workflows/publish_maven_central.yml`, `README.md`.
Tests: `mvnq test -Dtest='CommonPropertyOptInTest,PropertyDefinitionTest'`

- [ ] 1.1 Test first: rewrite `CommonPropertyOptInTest` on `FurnitureTaxonomyService` (D2:
      `furnitureOptsIntoEmissionProperties`, `furnitureHasNoAccountingProperties`; delete the
      raw-Jena test and its Javadoc). Bump `taxonomy-commons` to `3.14` in `pom.xml`. Run: the
      emission test passes already, the accounting test fails on `ledgerAccount` /
      `depreciationGroup`.
- [ ] 1.2 `furniture-base.ttl`: replace lines 282-295 with the D1 block. Group tests pass.
- [ ] 1.3 CI (D3): remove the Central steps from `build_tag_and_release.yml` and fix the
      SNAPSHOT message; add `publish_maven_central.yml`. Diff both against
      `/home/mkf/dev/sirk-machine-taxonomy/.github/workflows/` (or commons' if machine has not
      merged) and keep only repo-name differences; `actionlint` if installed.
- [ ] 1.4 README: "Common properties" (D4) and "Releases" (D3).
- [ ] 1.5 `mvnq test` once (full suite).

## Not in scope

orgadmin's `emissionPerUnit` / `consistsOf` follow-up and same-named redeclarations; see
proposal.md "Not in scope". No commit, push or PR inside the group unless asked.
