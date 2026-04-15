# Serenity Screenplay Framework

Automation test framework built with **Serenity BDD + Selenium + Cucumber (BDD)** following the **Screenplay Pattern** — a design pattern that models tests around *actors* who perform *tasks* and ask *questions* about the state of the system.

---

## Table of Contents

1. [Tech Stack](#tech-stack)
2. [Project Structure](#project-structure)
3. [Core Concepts](#core-concepts)
4. [How to Run Tests](#how-to-run-tests)
5. [Write a New Test — Step by Step Guide for Beginners](#write-a-new-test--step-by-step-guide-for-beginners)
6. [Environment Configuration](#environment-configuration)
7. [Test Tags Strategy](#test-tags-strategy)
8. [Serenity Report](#serenity-report)

---

## Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 17 | Programming language |
| Maven | 3.9.9 | Build & dependency management |
| Serenity BDD | 4.1.17 | Test orchestration & reporting |
| Selenium | 4.25.0 | Browser automation |
| Cucumber | (via serenity-cucumber) | BDD / Gherkin feature files |
| JUnit | (transitive via serenity-cucumber) | Test runner |
| Logback | (via serenity-core) | Logging |

---

## Project Structure

```
serenity-selenium-screenplay/
│
├── .mvn/wrapper/
│   └── maven-wrapper.properties        # Defines Maven version — no global install needed
│
├── src/
│   ├── main/java/com/saucedemo/
│   │   │
│   │   ├── core/                       # Framework base classes (shared by all tests)
│   │   │   ├── BaseTask.java           # All Tasks extend this
│   │   │   ├── BaseQuestion.java       # All Questions extend this
│   │   │   └── EnvironmentConfig.java  # Reads config from dev/stg.properties
│   │   │
│   │   ├── models/                     # Data objects (plain Java, no Selenium)
│   │   │   ├── UserCredentials.java    # Holds username + password
│   │   │   └── UserRole.java           # Enum: STANDARD, LOCKED_OUT, INVALID
│   │   │
│   │   ├── ui/                         # Page UI element locators (Targets)
│   │   │   ├── LoginPage.java          # Locators for the login page
│   │   │   └── InventoryPage.java      # Locators for the inventory/dashboard page
│   │   │
│   │   ├── tasks/                      # WHAT the actor DOES (actions with side-effects)
│   │   │   ├── OpenTheLoginPage.java   # Navigates to base URL
│   │   │   └── LoginWithCredentials.java # Fills form and clicks login
│   │   │
│   │   └── questions/                  # WHAT the actor OBSERVES (read-only state checks)
│   │       ├── CurrentPath.java        # Returns current URL path e.g. /inventory.html
│   │       ├── CurrentPageTitle.java   # Returns browser tab title
│   │       ├── IsOnInventoryPage.java  # Returns true if inventory list is visible
│   │       └── LoginErrorMessage.java  # Returns error message text
│   │
│   └── test/
│       ├── java/com/saucedemo/
│       │   ├── hooks/
│       │   │   └── Hooks.java          # @Before/@After: sets up Actor + stage
│       │   ├── runners/
│       │   │   └── TestRunner.java     # Cucumber runner, points to features + glue
│       │   └── stepdefinitions/
│       │       └── LoginStepDefinitions.java  # Glue between Gherkin steps and Tasks/Questions
│       │
│       └── resources/
│           ├── config/
│           │   ├── dev.properties      # Dev environment config (URL, credentials)
│           │   └── stg.properties      # Staging environment config
│           ├── features/
│           │   └── login.feature       # Gherkin scenarios
│           └── logback-test.xml        # Logging config (reduces console noise)
│
├── serenity.conf                       # Serenity + WebDriver configuration (HOCON format)
└── pom.xml                             # Maven dependencies and plugins
```

---

## Core Concepts

The **Screenplay Pattern** revolves around 4 building blocks:

```
┌─────────────────────────────────────────────────────────────────┐
│                         SCREENPLAY PATTERN                       │
│                                                                  │
│  ACTOR  ──can──►  ABILITY (BrowseTheWeb)                        │
│    │                                                             │
│    ├── attemptsTo ──►  TASK  (what actor does: click, type...)  │
│    │                                                             │
│    └── should ──────►  QUESTION  (what actor observes/checks)   │
│                              │                                   │
│                              └──► compared with MATCHER         │
└─────────────────────────────────────────────────────────────────┘
```

| Concept | Package | Description | Example |
|---------|---------|-------------|---------|
| **UI Targets** | `ui/` | CSS/XPath locators for elements | `LoginPage.USERNAME_INPUT` |
| **Tasks** | `tasks/` | Steps that change UI state | `LoginWithCredentials` |
| **Questions** | `questions/` | Read-only assertions on state | `CurrentPath.value()` |
| **Models** | `models/` | Plain data objects | `UserCredentials`, `UserRole` |

---

## How to Run Tests

### Prerequisites
- Java 17+ installed
- Google Chrome installed
- Maven installed **or** use the Maven Wrapper (`./mvnw` on Mac/Linux, `mvnw.cmd` on Windows)

### Basic run

```bash
# Run all tests (dev environment, Chrome)
mvn clean verify

# Using Maven Wrapper (no Maven install needed)
mvnw.cmd clean verify          # Windows
./mvnw clean verify            # Mac / Linux
```

### Run by tag

```bash
# Smoke tests only
mvn clean verify -Dcucumber.filter.tags="@smoke"

# All login tests
mvn clean verify -Dcucumber.filter.tags="@login"

# Skip negative tests
mvn clean verify -Dcucumber.filter.tags="@login and not @negative"

# Only negative / failure scenarios
mvn clean verify -Dcucumber.filter.tags="@negative"
```

### Switch environment

```bash
# Run against staging
mvn clean verify -Denv=stg

# Run headless (no browser window — good for CI)
mvn clean verify -Dheadless=true

# Full CI command example
mvn clean verify -Denv=stg -Dbrowser=chrome -Dheadless=true -Dcucumber.filter.tags="@smoke"
```

### View Serenity Report

After a run, open:
```
target/site/serenity/index.html
```

---

## Write a New Test — Step by Step Guide for Beginners

> **Example goal:** Test the "Add to Cart" feature on the inventory page.

Follow these 5 steps in order:

---

### Step 1 — Add UI locators (if new elements are needed)

Open or create a file in `src/main/java/com/saucedemo/ui/`.

```java
// src/main/java/com/saucedemo/ui/InventoryPage.java
public class InventoryPage {

    // ADD a new Target for any element you need to interact with or verify
    public static final Target ADD_TO_CART_BUTTON = Target.the("add to cart button")
            .locatedBy("[data-test='add-to-cart-sauce-labs-backpack']");

    public static final Target CART_BADGE = Target.the("cart badge count")
            .locatedBy(".shopping_cart_badge");
}
```

> **Tip:** Use `Target.the("human readable name").locatedBy("css selector")`.  
> You can find CSS selectors by opening DevTools (F12) in Chrome → right-click element → Copy → Copy selector.

---

### Step 2 — Create a Task (what the actor *does*)

Create a new file in `src/main/java/com/saucedemo/tasks/`.

```java
// src/main/java/com/saucedemo/tasks/AddItemToCart.java
package com.saucedemo.tasks;

import com.saucedemo.core.BaseTask;
import com.saucedemo.ui.InventoryPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;

public class AddItemToCart extends BaseTask {

    // Static factory method — how callers create this task
    public static Performable now() {
        return instrumented(AddItemToCart.class);
    }

    // What actually happens when the actor performs this task
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(InventoryPage.ADD_TO_CART_BUTTON)
        );
    }
}
```

> **Rules for Tasks:**
> - Always extend `BaseTask`
> - Always provide a `public static Performable xxx()` factory method
> - Inside `performAs()`, call `actor.attemptsTo(...)` with Serenity built-in actions

---

### Step 3 — Create a Question (what the actor *checks*)

Create a new file in `src/main/java/com/saucedemo/questions/`.

```java
// src/main/java/com/saucedemo/questions/CartItemCount.java
package com.saucedemo.questions;

import com.saucedemo.core.BaseQuestion;
import com.saucedemo.ui.InventoryPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

public class CartItemCount extends BaseQuestion<String> {

    // Static factory method — how callers use this question
    public static Question<String> value() {
        return new CartItemCount();
    }

    // What the actor reads from the page
    @Override
    public String answeredBy(Actor actor) {
        return Text.of(InventoryPage.CART_BADGE).answeredBy(actor);
    }
}
```

> **Rules for Questions:**
> - Always extend `BaseQuestion<T>` where `T` is the return type (`String`, `Boolean`, `Integer`...)
> - Provide a `public static Question<T> value()` (or `displayed()` for Boolean) factory method
> - Never click or change state inside a Question — read only!

---

### Step 4 — Write the Gherkin scenario

Open `src/test/resources/features/login.feature` (or create a new `.feature` file in the same folder).

```gherkin
# src/test/resources/features/cart.feature
Feature: Shopping Cart

  Background:
    Given user opens the login page
    When user logs in as "STANDARD"

  @smoke @regression @cart
  Scenario: Add item to cart
    When user adds an item to cart
    Then the cart badge should show "1"
```

> **Tip:** Use `Background:` for steps repeated across all scenarios in the file.

---

### Step 5 — Write the Step Definitions (glue code)

Create or open a file in `src/test/java/com/saucedemo/stepdefinitions/`.

```java
// src/test/java/com/saucedemo/stepdefinitions/CartStepDefinitions.java
package com.saucedemo.stepdefinitions;

import com.saucedemo.questions.CartItemCount;
import com.saucedemo.tasks.AddItemToCart;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.equalTo;

public class CartStepDefinitions {

    @When("user adds an item to cart")
    public void userAddsAnItemToCart() {
        OnStage.theActorInTheSpotlight().attemptsTo(AddItemToCart.now());
    }

    @Then("the cart badge should show {string}")
    public void theCartBadgeShouldShow(String expectedCount) {
        OnStage.theActorInTheSpotlight().should(
                seeThat(CartItemCount.value(), equalTo(expectedCount))
        );
    }
}
```

> **Tip:** Always use `OnStage.theActorInTheSpotlight()` to get the current active actor.  
> For `@When` → use `attemptsTo(Task)`.  
> For `@Then` → use `should(seeThat(Question, matcher))`.

---

### Full data flow diagram

```
login.feature (Gherkin)
    │
    ▼
CartStepDefinitions.java (@When / @Then methods)
    │
    ├──► attemptsTo( AddItemToCart.now() )   ──► AddItemToCart.performAs()
    │                                                │
    │                                                └──► Click.on(InventoryPage.ADD_TO_CART_BUTTON)
    │
    └──► should( seeThat( CartItemCount.value(), equalTo("1") ) )
                        │
                        └──► CartItemCount.answeredBy()
                                    │
                                    └──► Text.of(InventoryPage.CART_BADGE)
```

---

## Environment Configuration

Config files live in `src/test/resources/config/`.

| Key | Description | Example |
|-----|-------------|---------|
| `env` | Environment name | `dev` |
| `base.url` | Entry point URL | `https://www.saucedemo.com/` |
| `timeout` | Wait timeout (seconds) | `10` |
| `username` | Generic username fallback (`TEST_USERNAME`) | `(set via env)` |
| `password` | Generic password fallback (`TEST_PASSWORD`) | `(set via env)` |
| `credentials.standard.username` | `STANDARD` username fallback (`TEST_STANDARD_USERNAME`) | `(set via env)` |
| `credentials.standard.password` | `STANDARD` password fallback (`TEST_STANDARD_PASSWORD`) | `(set via env)` |
| `credentials.locked_out.username` | `LOCKED_OUT` username fallback (`TEST_LOCKED_OUT_USERNAME`) | `(set via env)` |
| `credentials.locked_out.password` | `LOCKED_OUT` password fallback (`TEST_LOCKED_OUT_PASSWORD`) | `(set via env)` |
| `credentials.invalid.username` | `INVALID` username fallback (`TEST_INVALID_USERNAME`) | `(set via env)` |
| `credentials.invalid.password` | `INVALID` password fallback (`TEST_INVALID_PASSWORD`) | `(set via env)` |

Additional notes:
- `timeout` is now used by explicit UI waits in tasks.
- Role names are normalized in steps, so `standard` and `locked-out` are accepted.
- You can set an actor per scenario using tags, e.g. `@actor:Buyer`.

Switch environment via Maven:
```bash
mvn clean verify -Denv=stg
```

Example (Windows PowerShell) setting credential env vars before run:
```powershell
$env:TEST_STANDARD_USERNAME = "standard_user"
$env:TEST_STANDARD_PASSWORD = "secret_sauce"
$env:TEST_LOCKED_OUT_USERNAME = "locked_out_user"
$env:TEST_LOCKED_OUT_PASSWORD = "secret_sauce"
$env:TEST_INVALID_USERNAME = "invalid_user"
$env:TEST_INVALID_PASSWORD = "bad_password"
./mvnw.cmd clean verify -Denv=dev
```

Access config in code:
```java
EnvironmentConfig.required("base.url")   // throws if missing
EnvironmentConfig.get("timeout")         // returns null if missing
EnvironmentConfig.getInt("timeout", 10)  // returns default if missing
```

---

## Test Tags Strategy

| Tag | When to use |
|-----|-------------|
| `@smoke` | Critical happy-path tests, fast, run on every commit |
| `@regression` | Full test suite, run before releases |
| `@login` | All login-related scenarios |
| `@cart` | All cart-related scenarios |
| `@negative` | Tests that expect failures / error messages |

Apply tags in the feature file above a `Scenario` or `Scenario Outline`:
```gherkin
@smoke @regression @cart
Scenario: Add item to cart
```

---

## Serenity Report

After `mvn clean verify`, open:
```
target/site/serenity/index.html
```

The report includes:
- Step-by-step breakdown of each scenario
- Screenshots on failures
- Test execution statistics by tag, feature, and result
