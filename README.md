# Engineering interview exercises

Two 45-minute practical exercises for mid-level engineers. Complete **only the track in your invitation**.

| Track | Technologies | Exercise |
| --- | --- | --- |
| [Full stack](full-stack/BRIEF.md) | Angular, TypeScript, Java, Spring Boot | Complete a case-status action in an existing UI and service |
| [Automation](automation/BRIEF.md) | Java, Serenity BDD, REST Assured, JUnit | Improve an unreliable API test and add meaningful coverage |

All data is fictional. These are independent teaching fixtures, not an HMCTS system or a production architecture example. No cloud account, database, Docker, real credentials or access to private repositories is required.

## Before the interview

1. Install Git and **JDK 21**. Check both `java -version` and `JAVA_HOME` point to JDK 21.
2. For full stack only, install **Node.js 22.22.3 or later within version 22** (the pinned `.nvmrc` uses 22.23.2). If you use nvm, run `nvm install` and `nvm use` from the repository root.
3. Clone and run your track's setup checks below. Maven is provided by the wrapper; the first run downloads public dependencies from Maven Central/npm and can take several minutes.
4. Open the repository in your preferred editor. Read the brief, but leave the implementation for the interview unless your invitation says otherwise.

```sh
git clone https://github.com/Rich-RADITC/engineering-interview-exercises.git
cd engineering-interview-exercises
```

### Full-stack setup check

From the repository root:

```sh
./mvnw -f full-stack/backend/pom.xml test
cd full-stack/frontend
npm ci
npm test
npm run build
```

Then [start the application](full-stack/BRIEF.md#run-the-application). The page should display `TEST-001` as `OPEN`. The Close action is deliberately unfinished.

### Automation setup check

From the repository root:

```sh
./mvnw -f automation/pom.xml verify
```

The fixture starts and stops its own API on a free loopback port for every test. There is no separate service to start. Open `automation/target/site/serenity/index.html` for the report. **A green starter run proves setup, not that the intentionally weak candidate test is adequate.**

### Windows

Use `mvnw.cmd` in Command Prompt or `.\mvnw.cmd` in PowerShell in place of `./mvnw`. `npm` commands are the same. Set `JAVA_HOME` to your JDK 21 installation. Commands assume the repository root unless stated otherwise.

## Interview format

- 5 minutes: brief and questions.
- 30 minutes: practical work with the interviewer.
- 10 minutes: review, follow-up discussion and your questions.

We assess reasoning, correctness, verification and collaboration. A complete solution is not required. You may consult documentation, search or use AI tools; explain and verify anything you use. Asking for clarification is welcome. Use only the supplied fictional material in external tools.

Share your editor or use Live Share as agreed in your invitation. Contact the organiser for setup help, accessibility adjustments or an alternative arrangement. Setup problems should not consume assessment time.

## During and after the interview

Work on a local branch, for example `git switch -c candidate-work`. No GitHub account is needed to clone. Keep your completed solution local and share it with the interviewer using the agreed private route. **Do not submit solutions, CVs or candidate details through public issues, pull requests or forks.**

The repository contains starter code and candidate requirements only. Interviewer answers and scoring materials are maintained separately.

## Troubleshooting

- **Java release not supported:** check `JAVA_HOME` as well as `java -version`; an IDE may use another JDK.
- **npm engine error:** use Node 22, not the latest non-LTS Node release.
- **Port 8080/4200 busy:** stop the unrelated local process only if it is yours, or ask the interviewer to agree alternate ports and update the UI proxy.
- **Cannot download dependencies:** check your network/proxy configuration. Do not disable TLS verification. The first download is not part of the timed exercise.
- **UI cannot load the case:** start the Java service before the Angular dev server. The `/api` proxy targets `127.0.0.1:8080`.
- **Case remains CLOSED after testing:** restart the Java service to reset the in-memory fixtures.
- **Full-stack exercise test fails / close endpoint returns 501:** expected until the task is implemented. See the distinction between setup and exercise checks in the brief.

## Tooling references

- [Angular version compatibility](https://angular.dev/reference/versions)
- [Serenity Maven integration](https://serenity-bdd.github.io/docs/guide/maven)

Dependencies are pinned for reproducibility. Upgrade them deliberately and rerun the setup checks before another interview round.
