Feature: Catalog integration tests

  Scenario: get catalog item
    Given catalog item exists at the database
    When i get the catalog item from rest http://localhost:9002 catalog 2
    Then catalog item name should be iPod touch
    Then catalog item price should be 21.0

