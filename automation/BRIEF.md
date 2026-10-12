# Automation tester exercise

**Mid level · Java and Serenity BDD · 45 minutes**

A fictional case API creates a case immediately, but its read model becomes visible shortly afterwards. Improve the supplied Java test, add one negative test and describe how you would organise the wider coverage.

## Run

From the repository root:

```sh
./mvnw -f automation/pom.xml verify
```

Open `automation/target/site/serenity/index.html`. This runs the fixture's infrastructure checks and the Serenity tests. Each test owns a new local API instance on a free loopback port; the fixture closes it and removes all its records after the test, including on failure. No manual cleanup or external service is needed.

The initial `CreateCaseIT` is deliberately weak but normally passes. It is your review subject; green does not mean correct or complete.

## API contract

| Request | Expected behaviour |
| --- | --- |
| `POST /cases` | Valid reference returns 201 with nonempty string `id` and the submitted `reference` |
| `GET /cases/{id}` | Returns 404 until visible, then 200 with the same `id` and `reference` |
| GET unknown id | Remains 404 |
| Invalid reference | Returns 400 |
| Missing or invalid demo authentication | Returns 401 |
| GET the supplied restricted case | Returns 403 without `id` or `reference` fields |

A valid reference matches `TEST-` followed by 1–40 uppercase letters, digits or hyphens. The body must be JSON with a string `reference`. Newly created cases become visible within three seconds of the POST response. Unexpected error responses are not part of that visibility contract.

`ApiFixture` provides:

- `authenticatedRequest()` — base URL, one-second connect/read timeouts and fictional demo identity.
- `anonymousRequest()` — same base URL/timeouts, without authentication.
- `restrictedCaseId()` — a seeded id the demo identity cannot read.

The demo auth string is public fixture data, not a real credential. Do not connect these tests to another service or introduce real credentials.

## Task

Suggested allocation: review 7 minutes, improved test 18 minutes, additional scenario 5 minutes.

1. Review [CreateCaseIT.java](src/test/java/io/raditc/interview/CreateCaseIT.java). Explain and prioritise its weaknesses.
2. Improve its creation and retrieval checks, replacing the fixed delay with bounded waiting.
3. Add one negative test and explain the other scenarios you would prioritise.
4. Write one short Gherkin scenario in [create-case.feature](src/test/resources/features/create-case.feature), and explain where Serenity step methods would fit.

The feature file is a design artefact; it is intentionally not wired to a Cucumber runner. Executable tests use Serenity's JUnit integration and `SerenityRest.given()`. Runner setup is outside the exercise. A plain Java polling helper is fine; you do not need to add a library.

Work in `CreateCaseIT.java` and add test/helper classes as useful. `support/` contains the supplied fixture, not code you are expected to fix. Ask before changing the API contract.

## Useful commands

```sh
# Only the readiness test, still with a generated Serenity report.
./mvnw -f automation/pom.xml -Dit.test=SetupIT verify

# Your candidate tests.
./mvnw -f automation/pom.xml -Dit.test=CreateCaseIT verify

# A slower fixture, still within the same documented contract.
./mvnw -f automation/pom.xml -Ddemo.visibility.delay.ms=2500 verify

# A fresh report and test run.
./mvnw -f automation/pom.xml clean verify
```

The configurable visibility delay accepts 0–3000 ms and defaults to 500 ms. Explain how your polling deadline, interval and request timeout interact. Document any small observation allowance rather than silently extending the service expectation.

## Discussion

1. The test passes locally but intermittently fails in CI. How would you investigate?
2. Describe one browser test using Serenity and Selenium. What would you locate, wait for and assert?
3. In a different API, a submission returns 202 but the read model is empty. What does that prove, and what would you inspect next?

Explain remaining work and useful failure evidence. Keep generated reports local and do not publish your solution.
