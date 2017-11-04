@order @all
Feature: Microservice acceptance tests

  Scenario: Order a product from a catalog
    Given order by Teemu Selanne should not exist
     And product Torspo should not be in the catalog through REST API
     And customer Teemu Selanne should not exist through REST API
     And product Torspo is added to the catalog with price 119.0
     And customer Teemu Selanne is added
    When I order product Torspo as customer Teemu Selanne
    Then I can verify my order of Torspo with price 119.0 by customer Teemu Selanne

#  Scenario: get catalog item
#    Given catalog item exists at the database
#    When i get the catalog item from rest
#    Then catalog item name should be iPod touch
#    And catalog item price should be 21.0
