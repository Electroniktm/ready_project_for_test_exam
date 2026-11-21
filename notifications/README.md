# Уведомления о результатах автотестов в Telegram

Карточка с итогами сборки (диаграмма + счётчики + ссылка на Allure-отчёт) приходит в Telegram
после каждой сборки Jenkins. Отправляет её [qa-guru/allure-notifications](https://github.com/qa-guru/allure-notifications) —
тот же инструмент, что разбирается в уроке.

```
gradle clean test  →  allure-jenkins-plugin собирает отчёт в allure-report/
                   →  шаг «Create/Update Text File» кладёт в workspace notifications/config.json
                   →  «Post build task» запускает allure-notifications-5.1.2.jar
                   →  в Telegram приходит карточка результатов
```

## 1. Бот и чат

### Вариант, который используется в этой задаче: личный чат с ботом

1. `@BotFather` → `/newbot` → имя (`Electroniktm QA reports`) и username (`electroniktm_qa_bot`).
2. `BotFather` выдаёт **HTTP API token** — это `telegram.token`.
3. Открыть чат бота `t.me/electroniktm_qa_bot` и нажать **Start** — без этого бот не видит диалог
   и `getUpdates` возвращает пустой список.
4. `chat_id` = числовой id диалога, берётся из ответа бота:

   ```bash
   curl -s "https://api.telegram.org/bot<TOKEN>/getUpdates"
   # result[].message.chat.id  → положительное число (личный чат)
   ```

5. Проверка отправки без Jenkins:

   ```bash
   curl -s -X POST "https://api.telegram.org/bot<TOKEN>/sendMessage" \
        -d chat_id=<CHAT_ID> -d text="bot is alive"
   ```

### Вариант для ответа в школе: группа (супергруппа)

1. Создать группу, добавить в неё бота и **назначить его администратором** — иначе бот не может
   публиковать сообщения в супергруппе.
2. `chat_id` группы отрицательный и начинается с `-100`: отправить любое сообщение в группу,
   затем посмотреть `result[].message.chat.id` в том же `getUpdates`.
3. В конфиге: `"chat": "-100…"`; если группа с топиками (форум), указать `"topic": "<message_thread_id>"`.

Примеры карточек: [t.me/allure_notifications_examples/155](https://t.me/allure_notifications_examples/155) (jar, Allure 2),
[/157](https://t.me/allure_notifications_examples/157) (CLI, Allure 3).

## 2. Настройка джобы в Jenkins

| Шаг | Что делает |
|------|------|
| **Create/Update Text File** (`text-file-operations`) | пишет `notifications/config.json` в workspace по образцу [config.example.json](config.example.json): `${JOB_BASE_NAME}` и `${BUILD_URL}` подставляет сам Jenkins |
| **Allure** (allure-jenkins-plugin) | идёт **перед** Post build task: после него в workspace лежит `allure-report/widgets/summary.json`, откуда берутся счётчики карточки |
| **Post build task** (`postbuild-task`) | вызывает агентский хелпер курса `/opt/qa-guru/bin/send-allure-telegram.sh allure2` |

Хелпер агента (есть на всех `java`-агентах) проверяет наличие `allure-report/widgets/summary.json`,
скачивает при необходимости `allure-notifications-4.11.1.jar` — версия зафиксирована в
`/opt/qa-guru/etc/allure-notifications-jar-a2.version`, — запускает
`java -DconfigFile=notifications/config.json -jar …` и повторяет отправку до трёх раз,
не влияя на статус сборки.

### Прокси

У агента нет прямого доступа к `api.telegram.org` (`curl` → таймаут), а SOCKS5-прокси курса
`proxy.qaguru.school:7777` с 06.08.2026 требует авторизации: без кредов
`curl --socks5-hostname` отвечает `97 No authentication method was acceptable`.
Поэтому эгресс описан прямо в конфиге — блок `proxy` (`type`, `host`, `port`, `username`, `password`),
как в агентском шаблоне `/opt/qa-guru/etc/allure-notifications/allure2.json.tmpl`.
Креды прокси — курсовые, служебные; в git они не попадают (`notifications/config.json` в `.gitignore`).

Встречавшаяся в работах схема `proxychains4` + файл кредов `/opt/qa-guru/etc/microsocks.env`
на текущих агентах не работает: этого файла на агентах нет (проверено диагностической сборкой).

### Версии и пути

`allure-notifications 4.11.1` (jar, линия Allure 2), Allure 2.32.0, allure-jenkins-plugin 3.0.0.
`allureFolder` для jar задаётся относительно корня workspace (`allure-report`), ссылка на отчёт —
`${BUILD_URL}allure/index.html`. Джоба параметризована так же, как сборка прошлого урока.

## 3. Почему токена нет в репозитории

`notifications/config.json` с реальным токеном и `chat_id` создаёт **Jenkins** на сборке,
а в git попадает только [config.example.json](config.example.json) с заглушками
(`.gitignore` исключает `notifications/config.json` и `notifications/*.jar`).
Токен бота даёт полный контроль над ботом, поэтому в истории коммитов его быть не должно;
на общем Jenkins токен виден в конфиге задачи — после проверки домашней работы токен можно
перевыпустить (`@BotFather` → `/revoke`) и обновить в конфиге задачи.
