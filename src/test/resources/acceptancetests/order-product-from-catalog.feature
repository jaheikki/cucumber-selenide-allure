@link.mylink=jvm
@order
@all
Feature: Microservice acceptance tests

  This test suite verifies that ordering a product from E-commerce Manager UI
  is executed successfully with a new customer.

  @severity=blocker
  @tmsLink=REQ-405
  @link=https://yle.fi/
  @order1
  Scenario: Order a product from a catalog
    Given E-commerce Manager ui should be open
     And order by Teemu Selanne should not exist
     And product Torspo should not be in the catalog through REST API
     And customer Teemu Selanne should not exist through REST API
     And product Torspo is added to the catalog with price 119.0
     And customer Teemu Selanne is added
    When I order product Torspo as customer Teemu Selanne
    Then I can verify my order of Torspo with price 119.0 by customer Teemu Selanne

  @severity=minor
  @issue=BUG-371
  @link=https://www.mtv.fi/
  @order2
  Scenario: get catalog item
    Given catalog item exists at the database
    When i get the catalog item from rest
    Then catalog item name should be iPod touch
    And catalog item price should be 900.0
