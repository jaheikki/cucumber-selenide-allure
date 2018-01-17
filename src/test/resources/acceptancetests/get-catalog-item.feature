@catalog
@all
@good @link=http://yandex.ru @link.mylink-112-qwe=mylinkname-12  @link.mylink-112-qwe=12_12-12
@tmsLink=OAT-4444
@flaky @issue=BUG-22400
Feature: Catalog integration tests

  @link=http://yandex.ru
  @link.my-link-named-type=my-named-name
  Scenario: get catalog item
    Given catalog item exists at the database
    When i get the catalog item from rest
    Then catalog item name should be iPod touch
    And catalog item price should be 21.0

