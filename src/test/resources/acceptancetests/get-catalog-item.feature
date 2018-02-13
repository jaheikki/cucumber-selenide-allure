@catalog
@all
Feature: Catalog integration tests

  Scenario: get catalog item
    Given catalog item exists at the database
    When i get the catalog item from rest
    Then catalog item name should be iPod touch
    And catalog item price should be 21.0

