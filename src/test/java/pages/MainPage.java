package pages;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

public class MainPage {

    private final SelenideElement logo = $("img.logo_w");
    private final SelenideElement menu = $("nav#headerMenu");

    @Step("Открываем главную страницу")
    public MainPage openMain() {
        open("/ru");
        return this;
    }

    @Step("Проверяем, что логотип виден")
    public MainPage checkLogoVisible() {
        logo.shouldBe(visible);
        return this;
    }

    @Step("Проверяем пункты меню {items}")
    public MainPage checkMenuItems(String... items) {
        for (String item : items) {
            // пункт меню дублируется в скрытом мега-меню — берём видимый экземпляр
            menu.$$(byText(item)).findBy(visible).shouldBe(visible);
        }
        return this;
    }

    @Step("Переключаем язык интерфейса")
    public MainPage switchLanguage() {
        $(".language-link").click();
        return this;
    }

    @Step("Проверяем, что язык интерфейса по alt логотипа «{alt}»")
    public MainPage checkLanguageByLogoAlt(String alt) {
        logo.shouldHave(attribute("alt", alt));
        return this;
    }

    @Step("Открываем карточку продукта «{name}» из блока «Наши продукты»")
    public ProductCardPage openProductFromFrontpage(String name) {
        String urlBefore = WebDriverRunner.url();
        $$(".frontpage-products-series-block article")
                .filterBy(visible)
                .findBy(text(name))
                .click();
        // проверяем, что клик по карточке привёл к смене страницы
        webdriver().shouldNotHave(url(urlBefore));
        return new ProductCardPage();
    }
}
