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
- **Maven 3.8+**
- **Chrome/Firefox browsers**
- **ChromeDriver/GeckoDriver** in PATH
- **IDE:** IntelliJ IDEA or Eclipse recommended

---

## Setup

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/<your-username>/veeva-e2e-automation-framework.git
cd veeva-e2e-automation-framework
