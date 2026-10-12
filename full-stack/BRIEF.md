# Full-stack exercise

**Mid level · Angular and Java · 45 minutes**

An authorised administrator views a fictional case and can close it. The surrounding application already runs. Implement the Java rule and the Angular interaction, then demonstrate how you would verify them.

## Run the application

Terminal 1, from the repository root:

```sh
./mvnw -f full-stack/backend/pom.xml spring-boot:run
```

Terminal 2, from the repository root:

```sh
cd full-stack/frontend
npm ci
npm start
```

Open http://127.0.0.1:4200. GET http://127.0.0.1:8080/api/health should return `{"status":"ok"}`. The UI starts with `case-001` / `TEST-001` / `OPEN`. Another fixture, `case-002`, is already `CLOSED`.

The server and UI bind only to loopback. Data lives in memory and resets when the backend restarts. Authentication is deliberately outside this local exercise: do not deploy this application or supply real case information. Discuss production authorisation in the follow-up.

## Task

Suggested allocation: Java 12 minutes, Angular 12 minutes, verification 6 minutes.

1. Implement `CaseService.closeCase`.
2. Implement `AppComponent.closeCase` and update its template.
3. Add focused tests for the behaviour you consider most important.

### API contract

`POST /api/cases/{id}/close` has no business request body.

| Condition | Result |
| --- | --- |
| Existing OPEN case | Save CLOSED and return HTTP 200 with the saved case |
| Missing id | Throw the supplied `CaseNotFoundException`, mapped to HTTP 404 |
| Existing CLOSED case | Throw the supplied `CaseConflictException`, mapped to HTTP 409; do not save |

The case JSON contains `id`, `reference` and `status`. `CaseRepository.findById` returns a copy in an Optional; `save` persists and returns a copy. Calling `setStatus` alone does not persist the update. Exception mappings and routing are already supplied. The starter returns HTTP 501 for the unfinished close action.

### UI contract

- Show the case reference and status.
- Close is available only when the case is OPEN.
- While submitting, disable Close and provide progress feedback; repeated clicks must not send extra requests.
- On success, show the case returned by the API.
- On failure, retain the previous case and show a useful error. Allow a retry when appropriate.
- Keep the control and feedback accessible.

Assume the displayed case does not change while the request is pending. Concurrent updates, route changes and production permissions are discussion topics, not required implementations.

## Files to work in

- [Java service](backend/src/main/java/io/raditc/interview/CaseService.java)
- [Java exercise test](backend/src/test/java/io/raditc/interview/CloseCaseTest.java)
- [Angular component](frontend/src/app/app.component.ts)
- [Angular template](frontend/src/app/app.component.html)
- [Angular tests](frontend/src/app/app.component.spec.ts)

The Angular API service is already implemented. Use the existing Observable API or another clear approach appropriate to the project. Styling is not the focus.

## Verify your work

From the repository root:

```sh
# Setup checks: expected to pass on the starter.
./mvnw -f full-stack/backend/pom.xml test

# Exercise checks: one starting example, expected to FAIL before implementation.
./mvnw -f full-stack/backend/pom.xml -Pexercise test

# UI checks: add your own Close interaction assertions.
cd full-stack/frontend
npm test
npm run build
```

The starting exercise test is not a complete acceptance suite. Add tests or describe further checks based on the contract. Do not delete assertions merely to obtain a green build.

## Discussion

1. Two users close the same case at once. What can go wrong, and where would you address it?
2. Where should permission checks be enforced?
3. Which checks belong in unit, API and browser tests?

Explain any unfinished work and what you would verify next. Do not publish your solution.
