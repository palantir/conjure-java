# Repository guidance for coding agents

## Scope and priorities

- This file applies to the entire repository. If a more specific `AGENTS.md` is added below a module, follow that file for work in that subtree.
- Solve the requested problem completely, add or update tests, and validate the smallest relevant scope before handing off.
- Preserve unrelated work in the checkout. Inspect `git status` before and after changes, and do not rewrite user-owned changes.
- Use existing project patterns and production implementations; do not replace behavior with mocks, placeholders, or test-only shortcuts.

## Repository overview

This is a Gradle multi-project Java repository for generating Java bindings from Conjure API definitions.

- `conjure-java-core`: the main Conjure object, service, Dialogue, Jersey, and Undertow source generators. Most generator logic and golden/snapshot tests live here.
- `conjure-java`: the command-line application, with `ConjureJavaCli` as its entry point.
- `conjure-lib`: shared runtime types used by generated code and other modules.
- `conjure-undertow-lib`, `conjure-java-undertow-runtime`: Undertow APIs and runtime behavior.
- `conjure-undertow-annotations`, `conjure-undertow-processor`, and `conjure-undertow-processor-example`: annotation API, annotation processor, and its integration example.
- `conjure-java-client-verifier` and `conjure-java-server-verifier`: compatibility verification against Conjure verification test cases.
- `versions.props` and `versions.lock`: centralized dependency declarations and their resolved lock state.

Consult `readme.md` for supported generator features and wire-format expectations. Treat `build.gradle` and `settings.gradle` as authoritative when older documentation disagrees with the current build.

## Build and toolchains

- Always invoke the checked-in wrapper from the repository root: `./gradlew ...`.
- The build configures and may provision multiple JDKs. At present it compiles with Java 25, targets Java 17 libraries, runs tests on Java 25, and uses a Java 21 Gradle daemon. A first Gradle invocation may therefore download toolchains.
- Java compilation uses `-Werror`, Error Prone, Checkstyle, dependency checks, and Palantir Java Format. Fix findings rather than weakening or globally suppressing checks.
- Format Java and Gradle sources with `./gradlew format`. Do not manually reformat generated fixtures that are intentionally excluded from formatting.
- Keep dependencies in `versions.props`; do not add inline versions to module build files. Regenerate `versions.lock` with the appropriate Gradle consistent-versions task when dependency declarations change, and review the resulting lock diff.
- `.circleci/config.yml` is generated from `.circleci/template.sh`; do not edit the generated configuration directly.

## Generated code and snapshots

Generator output is part of the test contract and is checked into the repository.

- `conjure-java-core/src/integrationInput/java` contains generated integration fixtures.
- Java-like files under `src/test/resources` include golden outputs for generators and the Undertow annotation processor.
- A normal test run compares generated output with these files. If a generator change intentionally alters output, run `./gradlew test -Drecreate=true`, then carefully review every changed and newly created fixture.
- Never use `-Drecreate=true` merely to make a failing snapshot test pass. Confirm that each output change follows from the intended production change, then rerun the affected tests without recreation.
- Generator changes should normally cover representative combinations of options, not only a single happy-path fixture. Update the parameterized cases or processor samples where appropriate.

## Implementation conventions

- Follow the existing Java style and copyright headers in neighboring files.
- Preserve Conjure wire compatibility and generated API semantics: generated values are immutable, fields are non-null, optionals represent absence, enums and unions tolerate unknown values, and collection behavior depends on generator options.
- Keep generator option behavior consistent across objects and relevant service backends (Dialogue, Jersey, and Undertow). When adding an option, wire it through `Options`, CLI/configuration handling, generation, documentation, and snapshots as applicable.
- Prefer focused, package-local changes. Avoid exposing a new public API when an existing internal abstraction is sufficient.
- Published library modules use API compatibility checks. Do not update compatibility baselines simply to silence a failure; first determine whether the API change is intended.
- Tests use JUnit 5 and AssertJ. Annotation-processor tests also use compile-testing and checked-in generated sources. Match the style of adjacent tests.

## Validation

Run validation in increasing scope, choosing tasks that cover every modified module:

```bash
./gradlew :conjure-java-core:test --tests 'fully.qualified.TestClass'
./gradlew :conjure-java-core:test
./gradlew :conjure-undertow-processor:test
./gradlew format
./gradlew build
```

- Use the analogous `:<module>:test` task for other modules; do not run unrelated verifier suites for a narrowly scoped change unless shared behavior is affected.
- Run the full `./gradlew build` for cross-module, dependency, generator-output, or public-API changes. CI runs the build with parallel workers and fails if it leaves tracked files modified.
- After formatting, fixture recreation, or a full build, inspect `git status --short` and the complete diff. Generated output or lock-file changes must be included only when intentional.
- If environment or toolchain setup prevents a task from running, report the exact command and failure; do not claim the task passed.

## Change hygiene

- Keep changes narrowly related to the request and include regression coverage for bug fixes.
- Update `readme.md` when user-visible flags or documented generator behavior changes.
- User-facing changes may require a changelog entry under `changelog/@unreleased`; follow the existing changelog YAML schema and repository release conventions.
- In the final handoff, summarize behavior changed, tests run, and any validation that could not be completed.

## Code Conventions

### Code Style
- All files must end with a newline
- Use `./gradlew spotlessApply` to auto-format code (Palantir Java format)
- Compiler treats warnings as errors (`-Werror`)
- Prefer imported classes over fully qualified names (`List<A>` not `java.util.List<A>`)
- All dependencies must be explicitly declared (no implicit transitive dependencies)
- Java library target: 17, distribution target: 21
- Avoid unnecessary comments - most code is self-documenting, only explain non-obvious details or high-level concepts (applies to javadoc and inline comments). When you do include comments, make sure they are concise.
- **String concatenation whitespace** - When a string literal is split across multiple lines (typically by a formatter or for readability), ensure the boundary between adjacent literals preserves required whitespace. Place a leading space at the start of the second segment (e.g., `"first part" + " second part"`). Applies to error messages, log messages, exception arguments, test strings, and any other user- or developer-visible concatenated text.

### Safe Logging
- Use the `com.palantir.logsafe` safe-logging library to ensure log safety. Exception messages and arguments must be explicitly marked as safe or unsafe so that logging systems (e.g., Grafana) never leak sensitive data. Classes and parameters can also carry `@Safe` or `@Unsafe` annotations to declare their safety at the type level.
- **Exceptions**: Never throw raw JDK exceptions (`IllegalStateException`, `IllegalArgumentException`, `NullPointerException`, `RuntimeException`, etc.). Use their safe-logging equivalents from `com.palantir.logsafe.exceptions` instead: `SafeIllegalStateException`, `SafeIllegalArgumentException`, `SafeNullPointerException`, `SafeRuntimeException`, `SafeUncheckedIoException`, etc.
  - **Typed Conjure errors are exempt**: Conjure-defined error types already declare the safety of each argument via `safe-args` and `unsafe-args` fields, so they don't need `Safe*` equivalents.
- **Exception arguments**: Pass structured arguments using `SafeArg.of("name", value)` (for safe-to-log values) or `UnsafeArg.of("name", value)` (for sensitive values) instead of interpolating values into the message string. When choosing `SafeArg` vs `UnsafeArg`:
  - **Safe**: RIDs with random/UUID locators (e.g. object type RIDs, dataset RIDs), durations, counts, class names
  - **Unsafe**: User-inputted string identifiers (e.g. object type IDs, branch names/IDs, property type IDs, search queries), exception messages, file paths, PII
  - **Do not log**: Credentials, tokens, private keys, authorization headers
  - When in doubt, use `UnsafeArg` and flag it for the author to review
- **Preconditions**: Use `com.palantir.logsafe.Preconditions` instead of Guava's `com.google.common.base.Preconditions` for the same safety guarantees.
- **Logging**: Use `SafeLogger` / `SafeLoggerFactory` instead of SLF4J's `LoggerFactory` directly.

### Streams and Optionals
- Use `Stream.toList()` instead of `Stream.collect(Collectors.toList())` where possible.
- **Small streams (default)**: Readability wins.
  - When using optionals in a stream, prefer `.<T>mapMulti(Optional::ifPresent)` over `.flatMap(Optional::stream)`.
  - No `.map(Optional::get).filter(Objects::nonNull)`. It has 2 steps which is more error-prone.
- **Large streams or store-level code**: Allocation wins. For nullable values, prefer either:
  - `.map(...).filter(Objects::nonNull)` directly on the nullable value, or
  - A plain `for`/`forEach` loop instead of `stream().map(...)` when the pipeline is simple.

### Union Types
- For sealed unions (Conjure union types or sealed interfaces):
  - Use `switch` expressions when handling multiple variants differently (ensures exhaustiveness)
  - Use `instanceof` for guard clauses where only one variant needs handling and others early-return

### Collections
- Prefer Guava's immutable collections (`ImmutableSet`, `ImmutableList`, `ImmutableMap`) with builder pattern over mutable collection classes (`LinkedHashSet`, `ArrayList`, `HashMap
`).
  - JDK immutable utilities like `Set.of()` or `List.of()` are fine.
- **Method boundaries vs. internal use**: An immutable or unmodifiable view of a collection is strongly preferred at method boundaries (e.g., return types and parameters). For inter
mediate results within a method scope (e.g., a collection immediately passed into a builder), using `Collectors.toSet()` or similar mutable collector is acceptable for efficiency.

### Testing Conventions
- Prefer Mockito's `eq()` matchers over `any()` for higher test coverage, unless the assertion is not relevant for the test.
- Name the `@BeforeEach` setup method `before`.
