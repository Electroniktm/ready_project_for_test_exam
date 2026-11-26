# Проект по автоматизации тестирования сайта [Geoscan](https://www.geoscan.ru/ru)

<div align="center">
  <a href="https://www.geoscan.ru/ru"><img src="images/geoscan-logo.svg" alt="ГК «Геоскан»"></a>
</div>

> UI-автотесты сайта компании «Геоскан» (беспилотные авиационные системы) — готовый проект для тестового задания вакансии «Тестировщик-автоматизатор». Selenide + JUnit 5, отчётность Allure Reports и Allure TestOps, запуски в Jenkins на облачном Selenoid, уведомления в Telegram, интеграция с Jira.

## 📋 Содержание

- [Технологии и инструменты](#-технологии-и-инструменты)
- [Реализованные проверки](#-реализованные-проверки)
- [Сборка в Jenkins](#-сборка-в-jenkins)
- [Команды для запуска из терминала](#-команды-для-запуска-из-терминала)
- [Интеграция с Allure Report](#-интеграция-с-allure-report)
- [Интеграция с Allure TestOps](#-интеграция-с-allure-testops)
- [Интеграция с Jira](#-интеграция-с-jira)
- [Уведомление в Telegram при помощи бота](#-уведомление-в-telegram-при-помощи-бота)
- [Пример видео выполнения тестов на Selenoid](#-пример-видео-выполнения-тестов-на-selenoid)

## 🛠 Технологии и инструменты

<p align="center">
<a href="https://openjdk.org/"><img src="https://img.shields.io/badge/Java-17-orange.svg" alt="Java"></a>
<a href="https://junit.org/junit5/"><img src="https://img.shields.io/badge/JUnit%205-5.11-green.svg" alt="JUnit 5"></a>
<a href="https://gradle.org/"><img src="https://img.shields.io/badge/Gradle-9.3-blue.svg" alt="Gradle"></a>
<a href="https://selenide.org/"><img src="https://img.shields.io/badge/Selenide-7.16-red.svg" alt="Selenide"></a>
<a href="https://aerokube.com/selenoid/"><img src="https://img.shields.io/badge/Selenoid-cloud-lightgrey.svg" alt="Selenoid"></a>
<a href="https://allurereport.org/"><img src="https://img.shields.io/badge/Allure%20Report-2.32-red.svg" alt="Allure Report"></a>
<a href="https://allure.qa.guru/"><img src="https://img.shields.io/badge/Allure%20TestOps-5.x-blue.svg" alt="Allure TestOps"></a>
<a href="https://www.jenkins.io/"><img src="https://img.shields.io/badge/Jenkins-CI-blue.svg" alt="Jenkins"></a>
<a href="https://www.atlassian.com/software/jira"><img src="https://img.shields.io/badge/Jira-integration-blue.svg" alt="Jira"></a>
<a href="https://telegram.org/"><img src="https://img.shields.io/badge/Telegram-bot-blue.svg" alt="Telegram"></a>
<a href="https://github.com/"><img src="https://img.shields.io/badge/GitHub-repository-black.svg" alt="GitHub"></a>
</p>

- **Java 17** — язык разработки тестов
- **Selenide 7.16** — обёртка над Selenium WebDriver, автоматические ожидания и скриншоты
- **JUnit 5.11** — тестовый фреймворк, смоук-набор собран тегом `smoke`
- **Gradle 9.3** — сборка проекта
- **Allure Report 2.32** — отчёты с вложениями
- **Allure TestOps 5** — хранение тест-кейсов, ручные проверки, приёмка результатов с Jenkins
- **Jenkins** — параметризованная CI-сборка на ноде `java`
- **Selenoid** (облачный `selenoid.qa.guru`) — удалённый запуск браузеров с VNC и видеозаписью
- **Jira** — задачи по сценариям автотестов
- **Telegram-бот** — уведомления о результатах сборки

## ✅ Реализованные проверки

| Тест | Описание |
|------|----------|
| `homepageOpensTest` | Главная страница `/ru` открывается: логотип и пункты меню на месте |
| `productCatalogShowsModelsTest` | Каталог «Продукты» показывает модельный ряд: Геоскан 701, Gemini, Пионер |
| `productCardHasSpecsTest` | Карточка продукта Gemini содержит технические характеристики (Тип БВС, масса) |
| `servicesPageListsServicesTest` | Страница «Услуги» содержит 5+ услуг со ссылками (Аэрофотосъемка, Магнитная съемка) |
| `blogHasArticlesTest` | В блоге есть статьи, открытие статьи показывает её заголовок |
| `languageSwitchTest` | Переключатель языка меняет язык интерфейса RU ↔ EN |
| `contactFormValidationTest` | Форма «Связаться с нами» не пропускает некорректный email (HTML5-валидация) |

Все тесты помечены тегом `smoke`. Ручные проверки (оформление заказа, подписка, «Заказать звонок», поиск, сравнение товаров) заведены в Allure TestOps — они сознательно не автоматизированы, чтобы не отправлять реальные заявки с сайта.

## 🏗 Сборка в Jenkins

Параметризованная сборка [44_Reint_Geoscan_autotests](https://jenkins.qa.guru/job/44_Reint_Geoscan_autotests/): клонирование репозитория → `clean test` с параметрами → публикация результатов в Allure TestOps (allurectl) → Allure-отчёт → карточка в Telegram.

<div align="center">
  <img src="images/jenkins-job.png" alt="Jenkins job" width="800">
</div>

### Параметры сборки в Jenkins

<div align="center">
  <img src="images/jenkins-parameters.png" alt="Параметры сборки Jenkins" width="800">
</div>

| Параметр | Тип | Значение по умолчанию |
|------|------|------|
| `browser` | выпадающий список (chrome / firefox) | `chrome` |
| `browserVersion` | выпадающий список (latest / 157 / 156 / 155) | `latest` |
| `headless` | чекбокс | `false` |
| `baseUrl` | поле ввода | `https://www.geoscan.ru` |
| `browserSize` | поле ввода | `1920x1280` |
| `remoteURL` | поле ввода | `https://user1:1234@selenoid.qa.guru/wd/hub` |

После Gradle-шага сборка выполняет публикацию результатов в Allure TestOps:

```bash
curl -sL https://github.com/allure-framework/allurectl/releases/latest/download/allurectl_linux_amd64 -o allurectl && chmod +x allurectl
ALLURE_ENDPOINT=https://allure.qa.guru ALLURE_TOKEN=<project api token> ALLURE_PROJECT_ID=5450 \
  ./allurectl upload --launch-name "44_Reint_Geoscan_autotests #${BUILD_NUMBER}" build/allure-results
```

## ▶️ Команды для запуска из терминала

### Локальный запуск (браузер в облачном Selenoid)

```bash
gradlew clean test
  -Dbrowser=chrome              # браузер: chrome | firefox
  -DbrowserVersion=latest       # версия браузера; latest — не фиксировать (пул слотов Selenoid)
  -DbrowserSize=1920x1280       # размер окна браузера
  -Dheadless=false              # true — без GUI
  -DbaseUrl=https://www.geoscan.ru   # адрес тестируемого сайта
  -DremoteURL=https://user1:1234@selenoid.qa.guru/wd/hub  # адрес удалённого браузера (Selenoid)
```

### Запуск смоук-набора

```bash
gradlew smoke -Dbrowser=chrome -DremoteURL=https://user1:1234@selenoid.qa.guru/wd/hub
```

Значения по умолчанию зашиты в коде ([TestBase.java](src/test/java/tests/TestBase.java)), поэтому `gradlew test` без параметров запускает все тесты в Chrome через Selenoid.

## 📈 Интеграция с Allure Report

Отчёт собирается из `build/allure-results` плагином allure-jenkins-plugin и доступен на странице каждой сборки: [сборка №2](https://jenkins.qa.guru/job/44_Reint_Geoscan_autotests/2/allure/). Слушатель `AllureSelenide` + расширение [AttachmentsExtension](src/test/java/helpers/AttachmentsExtension.java) прикрепляют к каждому тесту скриншот, page source, логи консоли браузера и видео из Selenoid.

<div align="center">
  <img src="images/allure-overview.png" alt="Allure Report — Overview" width="800">
</div>

<div align="center">
  <img src="images/allure-graphs.png" alt="Allure Report — Graphs" width="800">
</div>

## 📊 Интеграция с Allure TestOps

Проект [44_Reint_Geoscan](https://allure.qa.guru/project/5450/launches) в Allure TestOps: результаты каждой сборки Jenkins публикуются шагом `allurectl upload`, тест-план объединяет 7 автоматизированных смоук-тестов и 5 ручных проверок.

<div align="center">
  <img src="images/testops-dashboard.png" alt="Allure TestOps — Dashboard" width="800">
</div>

<div align="center">
  <img src="images/testops-launch-2.png" alt="Allure TestOps — запуск со сборки Jenkins" width="800">
</div>

<div align="center">
  <img src="images/testops-testplan.png" alt="Allure TestOps — тест-план с ручными проверками" width="800">
</div>

## 🎫 Интеграция с Jira

По каждому сценарию автотеста заведена задача в [Jira](https://jira.qa.guru/browse/NEWAPP-36), в описании — ссылка на запуск в Allure TestOps, а в блок «ТестОпс: Test Artifacts» прикреплён соответствующий тест-кейс из Allure TestOps.

<div align="center">
  <img src="images/jira-issue.png" alt="Jira — задача по автотесту" width="800">
</div>

## 📨 Уведомление в Telegram при помощи бота

После каждой сборки бот [@electroniktm_qa_bot](https://t.me/electroniktm_qa_bot) отправляет карточку с результатами (статистика, длительность, ссылка на Allure-отчёт) через `allure-notifications`.

<div align="center">
  <img src="images/telegram.png" alt="Telegram — карточки результатов сборок" width="480">
</div>

## 🎬 Пример видео выполнения тестов на Selenoid

Видео каждой сессии пишется Selenoid (`enableVNC`/`enableVideo` в [TestBase.java](src/test/java/tests/TestBase.java)) и прикрепляется к тесту в отчёте. Ниже — gif из видео выполнения теста:

<div align="center">
  <img src="images/test.gif" alt="Выполнение теста в Selenoid" width="720">
</div>

---

<div align="center">

**Создано с ❤️ на Selenide + JUnit 5 + Allure**

</div>
