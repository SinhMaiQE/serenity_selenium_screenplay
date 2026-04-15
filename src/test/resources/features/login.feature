
Feature: Login

  Background:
    Given user opens the login page

  @smoke @regression @login
  Scenario Outline: Successful login as <role>
    When user logs in as "<role>"
    Then user should be redirected to the inventory page

    Examples:
      | role     |
      | STANDARD |
      | standard |

  @regression @login @negative
  Scenario Outline: Failed login with invalid or restricted credentials
    When user attempts to login as "<role>"
    Then user should see the error "<error>"

    Examples:
      | role       | error                                 |
      | LOCKED_OUT | Sorry, this user has been locked out. |
      | INVALID    | Username and password do not match    |
      | locked-out | Sorry, this user has been locked out. |

  @smoke @login @actor:Buyer
  Scenario: Successful login with custom actor tag
    When user logs in as "STANDARD"
    Then user should be redirected to the inventory page
