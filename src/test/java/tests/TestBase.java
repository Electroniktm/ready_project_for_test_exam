package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.AttachmentsExtension;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Map;

// расширение прикрепляет к тесту вложения (скриншот/HTML/логи/видео) и закрывает браузер:
// через @AfterEach это не работает — allure-junit-platform к тому моменту уже закрыл test case
@ExtendWith(AttachmentsExtension.class)
public class TestBase {

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide().screenshots(true).savePageSource(true));
    }

    @BeforeAll
    static void beforeALL() {
        // все параметры запуска вынесены из кода — задаются из командной строки / Jenkins:
        // -DbaseUrl=... -DremoteURL=... -Dbrowser=... -DbrowserVersion=... -Dheadless=... -DbrowserSize=...
        Configuration.baseUrl = System.getProperty("baseUrl", "https://www.geoscan.ru");
        Configuration.browser = System.getProperty("browser", "chrome");
        // версию не фиксируем по умолчанию: хаб selenoid.qa.guru использует пул слотов
        // и отдаёт доступную версию браузера; фиксируем только если явно передан параметр
        String browserVersion = System.getProperty("browserVersion", "");
        if (!browserVersion.isEmpty() && !browserVersion.equalsIgnoreCase("latest")) {
            Configuration.browserVersion = browserVersion;
        }
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        Configuration.browserSize = System.getProperty("browserSize", "1920x1280");
        Configuration.timeout = 15000;

        if (Configuration.browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setCapability("selenoid:options", Map.of(
                    "enableVNC", true,
                    "enableVideo", true
            ));
            Configuration.browserCapabilities = firefoxOptions;
        } else {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--disable-dev-shm-usage", "--no-sandbox");
            // без этой capability chromedriver не собирает логи консоли браузера (получали пустой файл)
            chromeOptions.setCapability("goog:loggingPrefs", Map.of("browser", "ALL"));
            chromeOptions.setCapability("se:cdpEnabled", false);
            chromeOptions.setCapability("selenoid:options", Map.of(
                    "enableVNC", true,
                    "enableVideo", true
            ));
            Configuration.browserCapabilities = chromeOptions;
        }
        // браузер запускается в облачном Selenoid (как в примерах курса);
        // адрес можно переопределить параметром -DremoteURL=...
        Configuration.remote = System.getProperty(
                "remoteURL", "https://user1:1234@selenoid.qa.guru/wd/hub");
    }
}
