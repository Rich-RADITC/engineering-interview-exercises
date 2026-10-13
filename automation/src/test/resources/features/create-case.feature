# Candidate task: write one business-readable scenario here.
# This is a design artefact, not a wired Cucumber runner.
# Java tests in this track run through Serenity's JUnit integration.
Feature: A newly created case becomes available
  Scenario: POST create case request
    Given User creates case using authentication Request
    And User has the following headers
      | Key          | Value            |
      | Content-type | application/json |
    And User creates a case with reference "Test-123"
    When User calls the POST request
    Then User gets response body with id
    And User verifies the status code 201