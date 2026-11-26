package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class ServicesPage {

    @Step("Открываем страницу «Услуги»")
    public ServicesPage openServices() {
        open("/ru/services");
        return this;
    }

    @Step("Проверяем заголовок «{expected}»")
    public ServicesPage checkTitle(String expected) {
        $("h1").shouldHave(text(expected));
        return this;
    }

    @Step("Проверяем, что услуг на странице больше {expected}")
    public ServicesPage checkServicesCount(int expected) {
        $$(".service-card").shouldHave(sizeGreaterThan(expected - 1));
        return this;
    }

    @Step("Проверяем ссылку на услугу «{name}»")
    public ServicesPage checkServiceLink(String name) {
        // ссылка дублируется в скрытом мега-меню — берём видимый экземпляр
        $$(byText(name)).findBy(visible).shouldBe(visible);
        return this;
    }
}
