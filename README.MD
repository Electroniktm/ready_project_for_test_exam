# Автоматизированные тесты форм DemoQA

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/)
[![Selenide](https://img.shields.io/badge/Selenide-7.16.2-blue.svg)](https://selenide.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5-green.svg)](https://junit.org/junit5/)
[![JavaFaker](https://img.shields.io/badge/JavaFaker-1.0.2-purple.svg)](https://github.com/BrunoBonacci/java-faker)
[![Allure](https://img.shields.io/badge/Allure-2.32.0-red.svg)](https://allurereport.org/)
[![Jenkins](https://img.shields.io/badge/Jenkins-CI-blue.svg)](https://www.jenkins.io/)

> UI-автоматизация веб-форм [DemoQA](https://demoqa.com) на Selenide и JUnit 5 с генерацией случайных данных через JavaFaker, отчётностью Allure Reports и запуском в Jenkins. Параметры запуска (браузер, версия, размер окна, адреса) выносятся в `-D`-свойства и в параметризованную сборку Jenkins. (обновлено 14.11.2026)

---

## 📋 Содержание

- [Обзор](#обзор)
- [Технологии](#технологии)
- [Структура проекта](#структура-проекта)
- [Покрытие тестами](#покрытие-тестами)
- [Генерация данных](#генерация-данных)
- [Начало работы](#начало-работы)
- [Запуск тестов](#запуск-тестов)
- [Параметры запуска](#️-параметры-запуска)
- [Принятые решения](#принятые-решения)
- [Лучшие практики](#лучшие-практики)
- [CI/CD (Jenkins)](#cicd-jenkins)

---

## 🎯 Обзор

Проект демонстрирует современное UI-автоматизированное тестирование веб-форм:

- **Регистрационная форма** — заполнение, валидация обязательных полей, проверка результата
- **Текстовое поле (Text Box)** — обработка полей ввода и проверка вывода данных

Набор тестов охватывает успешные сценарии, пограничные случаи и негативные сценарии. Все валидные данные генерируются случайно при каждом запуске через JavaFaker, поэтому тесты устойчивы к дублированию и проверяют форму на «живых» значениях.

---

## 🛠 Технологии

| Категория | Технология | Версия |
|------|------|---------|
| **Язык** | Java | 17 |
| **Тестовый фреймворк** | JUnit Jupiter | 5.11.4 |
| **Обёртка Selenium** | Selenide | 7.16.2 |
| **Генерация данных** | JavaFaker | 1.0.2 |
| **Отчётность** | Allure Reports | 2.32.0 (allure-selenide) |
| **Сборка** | Gradle | 9.3.0 |
| **Браузер** | Chrome | В облачном Selenoid (`selenoid.qa.guru`) |

---

## 📁 Структура проекта

```
parametr-jenkins-loading-ui-tests/
├── build.gradle                          # Конфигурация сборки и зависимости
├── notifications/                        # Уведомления о результатах в Telegram
│   ├── config.example.json               # Шаблон конфига allure-notifications (без токена)
│   └── README.md                         # Бот, чат, шаги Jenkins, почему токена нет в git
├── README.MD                             # Этот файл
├── LICENSE                               # Лицензия
└── src/
    └── test/
        ├── java/
        │   ├── pages/
        │   │   ├── TextBoxPage.java      # Page Object: форма Text Box
        │   │   ├── RegistrationFormPage.java  # Page Object: форма регистрации
        │   │   └── components/           # Сложные элементы, вынесенные отдельно
        │   │       ├── DatePicker.java   # Выбор даты рождения
        │   │       └── GenderRadio.java  # Группа radio-кнопок пола
        │   ├── testdata/
        │   │   └── TestData.java         # Фиксированные невалидные и граничные данные
        │   ├── utils/
        │   │   └── FakeDataProvider.java # Генерация случайных валидных данных (JavaFaker)
        │   └── tests/
        │       ├── TestBase.java         # Базовая конфигурация тестов
        │       ├── RegistrationFormTests.java  # Тесты регистрационной формы
        │       └── TextBoxTests.java     # Тесты текстового поля
        └── resources/
            └── test.png                  # Файл для проверки загрузки
```

---

## 🧪 Покрытие тестами

### Тесты регистрационной формы ([`RegistrationFormTests.java`](src/test/java/tests/RegistrationFormTests.java))

| Тест | Описание | Тип |
|------|------|------|
| `fillAllFieldsAndSubmitTest` | Полное заполнение формы со всеми проверками | ✅ Сценарий успеха |
| `fillOnlyRequiredFieldsTest` | Минимальная валидная отправка | ✅ Сценарий успеха |
| `invalidEmailFormatTest` | Обработка ошибки формата email | ❌ Отрицательный |
| `missingGenderSelectionTest` | Валидация обязательного поля «пол» | ❌ Отрицательный |
| `invalidPhoneNumberTest` | Валидация номера телефона | ❌ Отрицательный |
| `tooLongNameTest` | Обработка слишком длинного имени | ⚠️ Крайний случай |
| `emptyRequiredFieldsTest` | Отправка полностью пустой формы | ❌ Отрицательный |

### Тесты текстового поля ([`TextBoxTests.java`](src/test/java/tests/TextBoxTests.java))

| Тест | Описание | Тип |
|------|------|------|
| `successfulFillFormTest` | Полное заполнение валидными данными | ✅ Сценарий успеха |
| `textBoxMinimalFieldsTest` | Отправка только обязательных полей | ✅ Сценарий успеха |
| `textBoxEmptyFieldsTest` | Отправка пустой формы | ❌ Отрицательный |
| `textBoxTooLongNameTest` | Обработка слишком длинного имени | ⚠️ Крайний случай |
| `failEmailFillFormTest` | Валидация некорректного email | ❌ Отрицательный |

---

## 🎲 Генерация данных

Валидные данные генерируются динамически в [`FakeDataProvider.java`](src/test/java/utils/FakeDataProvider.java) на базе JavaFaker. Провайдер — обычный объект (без `static`), создаётся локально в каждом тестовом классе, поэтому экземпляр `Faker` не разделяется между потоками и тесты безопасны при параллельном запуске:

```java
private final FakeDataProvider fakeData = new FakeDataProvider();
```

| Метод | Что генерирует |
|------|------|
| `randomFirstName()` / `randomLastName()` / `randomFullName()` | Имя, фамилия, полное имя |
| `randomEmail()` | Корректный email-адрес |
| `randomPhone()` | 10-значный номер, начинающийся с цифры 6–9 |
| `randomGender()` | Male / Female / Other |
| `randomSubject()` | Один из доступных предметов |
| `randomAddress()` | Полный адрес |
| `randomBirthDate()` | Дата рождения в формате `dd MMMM yyyy` (возраст 18–65) |
| `randomState()` / `randomCity(state)` | Состояние и город из реального списка формы |

Выбор случайного элемента выполняется штатным методом JavaFaker `faker.options().option(...)`, а соответствие города выбранному состоянию — Switch Expression (Java 14+): неизвестное состояние бросает `IllegalArgumentException` вместо `NullPointerException`:

```java
public String randomCity(String state) {
    return switch (state) {
        case "NCR" -> faker.options().option("Delhi", "Gurgaon", "Noida");
        case "Uttar Pradesh" -> faker.options().option("Agra", "Lucknow", "Merrut");
        case "Haryana" -> faker.options().option("Karnal", "Panipat");
        case "Rajasthan" -> faker.options().option("Jaipur", "Jaiselmer");
        default -> throw new IllegalArgumentException("Unknown state: " + state);
    };
}
```

Список состояний и городов соответствует реальному выпадающему списку demoqa.com:

| State | Cities |
|------|------|
| NCR | Delhi, Gurgaon, Noida |
| Uttar Pradesh | Agra, Lucknow, Merrut |
| Haryana | Karnal, Panipat |
| Rajasthan | Jaipur, Jaiselmer |

Невалидные и граничные значения (слишком длинные строки, некорректные email/телефон) хранятся в [`TestData.java`](src/test/java/testdata/TestData.java), так как они должны быть фиксированными для воспроизводимости негативных сценариев.

---

## 🚀 Начало работы

### Требования

- **Java Development Kit (JDK)** 17 или выше
- **Gradle** 9.3+ (установленный локально; Gradle wrapper в репозиторий не коммитится)
- Локальный Chrome **не требуется**: браузер запускается в облачном Selenoid (`selenoid.qa.guru`)

### Установка

```bash
git clone https://github.com/Electroniktm/parametr-jenkins-loading-ui-tests.git
cd parametr-jenkins-loading-ui-tests
```

---

## ▶️ Запуск тестов

### Запуск всех тестов

```bash
gradle test
```

### Запуск конкретного класса тестов

```bash
gradle test --tests "tests.RegistrationFormTests"
gradle test --tests "tests.TextBoxTests"
```

### Запуск конкретного метода

```bash
gradle test --tests "tests.RegistrationFormTests.fillAllFieldsAndSubmitTest"
```

### Запуск с подробным выводом

```bash
gradle test --info
```

---

## ⚙️ Параметры запуска

Все ключевые настройки вынесены из кода в `-D` параметры — одним и тем же кодом можно управлять из командной строки и из Jenkins:

```bash
gradle clean test \
  -DbaseUrl=https://demoqa.com \
  -DremoteURL=https://user1:1234@selenoid.qa.guru/wd/hub \
  -Dbrowser=chrome \
  -DbrowserVersion=latest \
  -Dheadless=false \
  -DbrowserSize=1920x1280
```

| Параметр | Что задает | Значение по умолчанию |
|------|------|------|
| `baseUrl` | адрес сайта под тестом | `https://demoqa.com` |
| `remoteURL` | адрес удаленного браузера (Selenoid) | `https://user1:1234@selenoid.qa.guru/wd/hub` |
| `browser` | браузер | `chrome` |
| `browserVersion` | версия браузера (`latest` — не фиксировать, хаб отдаёт доступную версию из пула слотов) | `latest` |
| `headless` | headless-режим браузера | `false` |
| `browserSize` | разрешение экрана | `1920x1280` |

Механика: Jenkins/CLI передают параметры как системные свойства, `build.gradle` прокидывает их в JVM тестов (`systemProperties(System.getProperties())`), а [`TestBase.java`](src/test/java/tests/TestBase.java) читает через `System.getProperty("имя", "значение по умолчанию")` — поэтому запуск без параметров работает с дефолтами:

```java
Configuration.baseUrl = System.getProperty("baseUrl", "https://demoqa.com");
Configuration.browser = System.getProperty("browser", "chrome");
Configuration.browserSize = System.getProperty("browserSize", "1920x1280");
Configuration.remote = System.getProperty("remoteURL", "https://user1:1234@selenoid.qa.guru/wd/hub");
Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

String browserVersion = System.getProperty("browserVersion", "");
if (!browserVersion.isEmpty() && !browserVersion.equalsIgnoreCase("latest")) {
    Configuration.browserVersion = browserVersion;
}
```

> ⚠️ Справедливости ради: фиксация нестандартного `browserSize` (например, `1366x768`) обнажила особенность demoqa — на маленьких разрешениях рекламный оверлей перекрывает кнопку Submit (`ElementClickInterceptedException`). Механизм параметров работает, но разрешение лучше подбирать под приложение.

---

## 🎨 Принятые решения

### Паттерн Page Object

Все локаторы вынесены из тестовых классов в page objects — в тестах остались только вызовы методов и данные:

```java
registrationFormPage.open();
registrationFormPage.fillFirstName(firstName)
        .fillLastName(lastName)
        .fillEmail(email)
        .selectGender(gender);
registrationFormPage.submit();
registrationFormPage.checkModalVisible();
```

Структура:

- [`pages/TextBoxPage.java`](src/test/java/pages/TextBoxPage.java) — элементы и действия формы Text Box
- [`pages/RegistrationFormPage.java`](src/test/java/pages/RegistrationFormPage.java) — элементы и действия формы регистрации
- Сложные элементы вынесены в `pages/components`:
  - [`DatePicker.java`](src/test/java/pages/components/DatePicker.java) — выбор даты рождения
  - [`GenderRadio.java`](src/test/java/pages/components/GenderRadio.java) — группа radio-кнопок пола

Методы page objects возвращают `this`, поэтому заполнение формы читается как цепочка действий.

### Разделение данных: генерация + фиксированные значения

Валидные данные генерируются случайно через [`FakeDataProvider`](src/test/java/utils/FakeDataProvider.java), невалидные и граничные — хранятся в [`TestData`](src/test/java/testdata/TestData.java). В каждом тесте данные берутся в локальные переменные и используются и для заполнения, и для проверок:

```java
String firstName = fakeData.randomFirstName();
String email = fakeData.randomEmail();

registrationFormPage.fillFirstName(firstName)
        .fillEmail(email);
// ...
registrationFormPage.checkResultTableContains(firstName + " " + lastName);
registrationFormPage.checkResultTableContains(email);
```

### Выбор даты рождения

Дата рождения генерируется штатным методом JavaFaker `faker.date().birthday(18, 65)` и форматируется одной строкой через `SimpleDateFormat` в формат `dd MMMM yyyy` (например, `05 June 2023`) — именно его принимает поле даты. Дата выставляется напрямую через `setValue`, что надёжнее кликов по календарю:

```java
public String randomBirthDate() {
    Date birthday = faker.date().birthday(18, 65);
    return new SimpleDateFormat("dd MMMM yyyy").format(birthday);
}
```

### Выбор состояния и города

Состояние выбирается случайно через `faker.options().option(...)`, город — Switch Expression по выбранному состоянию (см. [Генерация данных](#-генерация-данных)). В UI оба значения выбираются кликом по выпадающему списку и последующим кликом по опции с нужным текстом:

```java
stateSelect.click();
stateCityWrapper.$(byText(value)).shouldBe(visible).click();
```

Город всегда выбирается из списка городов выбранного состояния, поэтому значение гарантированно присутствует в выпадающем списке.

### Загрузка ресурсов через classpath

Загрузка файлов использует `uploadFromClasspath` для независимости от окружения:

```java
uploadPicture.uploadFromClasspath("test.png");
```

### Паттерны Selenide

- **Цепочечные селекторы** для вложенных элементов
- **Проверка CSS-границ**: `cssValue("border-color", "rgb(220, 53, 69)")` для состояний ошибок
- **Явные ожидания видимости** перед кликом по опциям

---

## 📐 Лучшие практики

| Практика | Реализация |
|------|------|
| **Page Object Pattern** | Локаторы вынесены в `pages/` и `pages/components/`, в тестах — только методы page objects |
| **Data-Driven Testing** | Валидные данные генерируются через JavaFaker, невалидные — в `TestData.java` |
| **Потокобезопасность** | `FakeDataProvider` — инстанс без `static`, каждый тест получает собственный `Faker` |
| **Современный синтаксис Java** | Switch Expression (Java 14+) для выбора города по состоянию, `faker.options().option(...)` вместо ручных списков |
| **Описательные имена тестов** | `invalidEmailFormatTest`, `missingGenderSelectionTest` |
| **Явные ассерции** | `shouldHave(text(...))` для проверок |
| **Чистый код** | Без inline-комментариев; самодокументирующиеся имена методов |
| **Управление конфигурацией** | Настройки централизованы в `TestBase.beforeALL()`, все параметры запуска переопределяются `-D`-свойствами без изменения кода |

---

## 📸 Тестовые ресурсы

| Файл | Назначение |
|------|------|
| [`test.png`](src/test/resources/test.png) | Образец изображения для тестирования загрузки файлов |

---

## 🔧 Конфигурация

Базовая настройка тестов — в [`TestBase.java`](src/test/java/tests/TestBase.java): слушатель `AllureSelenide`, таймаут и все параметры запуска, которые читаются из `-D` свойств (по умолчанию — из значений `System.getProperty`). Подробный список параметров — в разделе [Параметры запуска](#️-параметры-запуска).

Слушатель `AllureSelenide` (скриншоты + исходник страницы при падении) подключается в каждом тесте:

```java
@BeforeEach
void addListener() {
    SelenideLogger.addListener("AllureSelenide",
            new AllureSelenide().screenshots(true).savePageSource(true));
}
```

---

## 🏗 CI/CD (Jenkins)

Параметризованная сборка [`electroniktm_demoqa_ui_tests`](https://jenkins.qa.guru/job/electroniktm_demoqa_ui_tests/) прогоняется в Jenkins: git-склонирование репозитория → `gradle clean test` с параметрами сборки → публикация отчёта Allure.

Перед запуском в Jenkins задаются параметры сборки трёх типов — **выпадающие списки**, **поля ввода** и **чекбокс** — и передаются в тесты как `-D` свойства (галочка *Pass all build parameters as system properties* в Gradle-шаге + `systemProperties(System.getProperties())` в `build.gradle`):

| Параметр | Тип в Jenkins | Значение по умолчанию |
|------|------|------|
| `browser` | выпадающий список (chrome / firefox) | `chrome` |
| `browserVersion` | выпадающий список (latest / 157 / 156 / 155) | `latest` |
| `headless` | чекбокс (Boolean Parameter) | `false` |
| `baseUrl` | поле ввода (String Parameter) | `https://demoqa.com` |
| `browserSize` | поле ввода (String Parameter) | `1920x1280` |
| `remoteURL` | поле ввода (String Parameter) | `https://user1:1234@selenoid.qa.guru/wd/hub` |

| Настройка джобы | Значение |
|------|------|
| **SCM** | `https://github.com/Electroniktm/parametr-jenkins-loading-ui-tests.git`, ветка `main` |
| **Агент** | нода с меткой `java` (JDK 21) |
| **Gradle** | установленный в Jenkins `Gradle 8.6`, флаг `--no-daemon`, задачи `clean test` |
| **Allure** | плагин allure-jenkins-plugin, результаты из `build/allure-results`, отчёт собирается всегда (`ALWAYS`) |

Результаты тестов пишутся в `build/allure-results` (плагин `io.qameta.allure` + адаптер JUnit 5 + AspectJ-weave для автоматических шагов), а Jenkins собирает из них интерактивный отчёт, доступный по ссылке `<job>/allure/`.

---

## 📨 Уведомления о результатах в Telegram

Джоба [`electroniktm_telegram_demoqa_ui_tests`](https://jenkins.qa.guru/job/electroniktm_telegram_demoqa_ui_tests/) — та же параметризованная сборка, дополненная отправкой карточки результатов в Telegram после сборки. Подробная инструкция (бот, личный чат и группа, настройка джобы, версии) — в [notifications/README.md](notifications/README.md).

| Шаг джобы | Назначение |
|------|------|
| Gradle `clean test` | запуск UI-тестов с параметрами сборки |
| Allure (allure-jenkins-plugin) | сборка отчёта; в workspace появляется `allure-report/widgets/summary.json` |
| Create/Update Text File | создаёт `notifications/config.json` в workspace (параметры карточки, `chat_id`, токен) |
| Post build task | вызывает агентский хелпер `/opt/qa-guru/bin/send-allure-telegram.sh allure2` (jar 4.11.1, SOCKS5-прокси курса описан в блоке `proxy` конфига) |

| Что в git | Что только в Jenkins |
|------|------|
| `notifications/config.example.json` с заглушками, документация | реальный `notifications/config.json` с токеном бота и `chat_id`, jar-файл |

---

## 🤝 Вклад в проект

1. Следуйте существующим соглашениям об именовании тестов
2. Используйте текстовые селекторы для поиска элементов
3. Добавляйте ассерции для всех пользовательских проверок
4. Делайте тесты независимыми и идемпотентными

---

## 📄 Лицензия

Этот проект создан в образовательных целях. Тесты работают с [demoqa.com](https://demoqa.com) — специальным сайтом для практики автоматизации.

---

<div align="center">

**Создано с ❤️ на Selenide + JUnit 5 + JavaFaker**

[⬆ Наверх](#автоматизированные-тесты-форм-demoqa)

</div>
