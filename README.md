# 🧩 Veeva E2E Automation Framework

![Veeva Logo](https://upload.wikimedia.org/wikipedia/commons/6/6a/Veeva_Systems_logo.svg)

[![Java](https://img.shields.io/badge/Java-11+-blue.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-green.svg)](https://maven.apache.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.25.0-red.svg)](https://www.selenium.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.x-orange.svg)](https://cucumber.io/)
[![TestNG](https://img.shields.io/badge/TestNG-Integrated-yellow.svg)](https://testng.org/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

---

## Table of Contents
1. [Overview](#overview)
2. [Framework Structure](#framework-structure)
3. [Prerequisites](#prerequisites)
4. [Setup](#setup)
5. [Running Tests](#running-tests)
6. [Configuration](#configuration)
7. [Selectors & Test Data](#selectors--test-data)
8. [Utilities & Helpers](#utilities--helpers)
9. [Reporting](#reporting)
10. [Execution Flow](#execution-flow)
11. [Best Practices](#best-practices)
12. [Troubleshooting](#troubleshooting)

---

## Overview
This **Veeva E2E Automation Framework** enables automated end-to-end UI and functional testing for **Core Product**, **DP1**, and **DP2** modules using:

- **Java 11+**
- **Selenium 4.x**
- **Cucumber (BDD)**
- **TestNG**
- **Extent Reports**
- **JSON-based Test Data**

                             
    ### **The overall architecture of the Veeva Automation Framework**

![Framework Architecture](docs/architecture/fw_architecture.png)


### 🔑 Key Highlights
- Modular **Page Object Model (POM)**
- Dynamic **Cucumber runner generation**
- **JSON-driven** selectors and expected data
- **Reusable BasePage & BaseTest** setup
- Unified **TestNG + Cucumber execution**
- **Log4j2** logging and detailed reporting

---

## Framework Structure

| Path | Description |
|------|--------------|
| `src/main/java/com/veeva/automation/base` | Base setup (`BasePage`, `BaseTest`, `DriverManager`, `WebDriverFactory`) |
| `src/main/java/com/veeva/automation/pages` | Page Object classes for core, DP1, and DP2 modules |
| `src/main/java/com/veeva/automation/factory` | Page factory and support classes |
| `src/main/java/com/veeva/automation/utils` | Utility classes like `TestDataUtils`, `FileUtils`, etc. |
| `src/main/resources/config/config.properties` | Global configuration file |
| `src/main/resources/templates/FeatureRunnerTemplate.java` | Template for dynamic Cucumber runners |
| `src/test/java/com/veeva/automation/steps` | Cucumber step definitions and hooks |
| `src/test/java/com/veeva/automation/runner` | Dynamic & static Cucumber TestNG runners |
| `src/test/java/com/veeva/automation/report` | Extent Report setup and management |
| `src/test/resources/features` | Cucumber feature files grouped by module |
| `src/test/resources/testdata` | JSON test data such as selectors and expected slide info |
| `src/test/resources/js` | Supporting JavaScript utilities for DOM extraction |

---

## Prerequisites
- **Java 11+**
- **Maven 3.9+**
- **Chrome/Firefox browsers**
- **ChromeDriver/GeckoDriver** in PATH
- **IDE:** IntelliJ IDEA or Eclipse recommended

---

## Setup

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/ashishbehera/veeva-e2e-automation-framework.git
cd veeva-e2e-automation-framework
```

### 2️⃣ Install Dependencies
```bash
mvn clean install -DskipTests
```

### 3️⃣ Configure Environment
Update the following in `config.properties`:
```properties
browser=chrome
headless=false
baseUrl=https://www.nba.com/warriors
```

---

## Running Tests

### 🧪 From Command Line
```bash
# Run  tests based on tag Smoke
mvn clean test -Dcucumber.filter.tags="@Smoke"

# Run  other tags like regression suite
mvn clean test -Dcucumber.filter.tags="@Regression"

# Run multiple tags
mvn clean test -Dcucumber.filter.tags="@Smoke or @Regression"

# Run specific module and override browser
mvn clean test -Dcucumber.filter.tags="@Smoke" -Dbrowser=firefox

```

### 🧩 From TestNG XML
You can run multiple modules or browsers in parallel using `testng.xml` using "mvn clean test" command

```xml
<suite name="Veeva Automation Suite" parallel="tests">
  <test name="Smoke Suite - Chrome">
    <parameter name="browser" value="chrome"/>
    <classes>
      <class name="com.veeva.automation.runner.CoreRunner"/>
    </classes>
  </test>
</suite>
```
```xml
<suite name="Veeva Automation Suite" parallel="tests" thread-count="2">
  <test name="Smoke Suite - Chrome">
    <parameter name="browser" value="chrome"/>
    <classes>
      <class name="com.veeva.automation.runner.CoreRunner"/>
    </classes>
  </test>

  <test name="Regression Suite - Firefox">
    <parameter name="browser" value="firefox"/>
    <classes>
      <class name="com.veeva.automation.runner.DP1Runner"/>
    </classes>
  </test>
</suite>
```

---

## Configuration

- **Global config:** `src/main/resources/config/config.properties`
- **Browser setup:** Controlled by `DriverManager` and `WebDriverFactory`
- **Headless mode:** Toggle via `headless=true/false`

---

## Selectors & Test Data

Selectors and expected data are stored in **JSON files** under:
```
src/test/resources/testdata/
```
Example:
```json
{
  "slideTitle": "#slide-header",
  "duration": ".slide-duration"
}
```

---

## Utilities & Helpers

| Utility | Description |
|----------|-------------|
| `TestDataUtils.java` | Reads and parses JSON test data |
| `WaitUtils.java` | Manages dynamic waits and conditions |
| `JSExecutorUtils.java` | Executes custom JavaScript actions |
| `FileUtils.java` | File operations for reports, logs, etc. |

---

## Reporting

- Integrated **Extent Reports (v5+)**
- Automatic report generation under `test-output/ExtentReports`
- **Log4j2** for detailed logging
- Supports integration with **Allure Reports** (optional)

---

## Execution Flow

```mermaid
flowchart TD
    A[TestNG XML] --> B[Dynamic Runner Generation]
    B --> C[Cucumber Feature Execution]
    C --> D[Hooks: Setup & Teardown]
    D --> E[Extent Reporting]
    E --> F[Report Generation]
```

---

## Best Practices

✅ Keep locators and test data externalized in JSON.  
✅ Use `BasePage` methods for all WebDriver actions.  
✅ Maintain `config.properties` per environment.  
✅ Integrate with CI/CD (Jenkins, GitHub Actions).  
✅ Use tags (`@smoke`, `@regression`, `@dp1`) for modular runs.

---

## Troubleshooting

| Issue | Possible Cause | Resolution |
|--------|----------------|------------|
| `SessionNotCreatedException` | Mismatch between browser and driver versions | Update ChromeDriver/GeckoDriver |
| `ConfigReader` returning null | Missing config.properties key | Ensure property key exists |
| JSON Parsing errors | Malformed test data JSON | Validate JSON syntax |

---

## 🧱 License
This framework is released under the [MIT License](LICENSE).

---

## 🤝 Contributing
Contributions are welcome!  
1. Fork this repository  
2. Create a feature branch  
3. Commit your changes  
4. Submit a Pull Request 🚀

---

© 2025 Veeva Systems - E2E Automation Framework
