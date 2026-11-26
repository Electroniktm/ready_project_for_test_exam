package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.BlogPage;
import pages.ContactsPage;
import pages.MainPage;
import pages.ProductCardPage;
import pages.ProductsPage;
import pages.ServicesPage;

@Tag("smoke")
public class GeoscanTests extends TestBase {

    private final MainPage mainPage = new MainPage();
    private final ProductsPage productsPage = new ProductsPage();
    private final ProductCardPage productCardPage = new ProductCardPage();
    private final ServicesPage servicesPage = new ServicesPage();
    private final BlogPage blogPage = new BlogPage();
    private final ContactsPage contactsPage = new ContactsPage();

    @Test
    @DisplayName("Главная страница открывается: логотип и меню на месте")
    public void homepageOpensTest() {
        mainPage.openMain()
                .checkLogoVisible()
                .checkMenuItems("Продукты", "Услуги", "Софт");
    }

    @Test
    @DisplayName("Каталог «Продукты» показывает модельный ряд: 701, Gemini, Пионер")
    public void productCatalogShowsModelsTest() {
        productsPage.openCatalog()
                .checkModelPresent("Геоскан 701")
                .checkModelPresent("Геоскан Gemini")
                .checkModelPresent("Геоскан Пионер");
    }

    @Test
    @DisplayName("Карточка продукта Gemini содержит технические характеристики")
    public void productCardHasSpecsTest() {
        mainPage.openMain()
                .openProductFromFrontpage("Геоскан Gemini")
                .checkProductName("Геоскан Gemini")
                .checkSpecsSection("Технические характеристики", "БВС")
                .checkSpecValue("Тип БВС", "мультироторный")
                .checkSpecValue("Масса (с АКБ и винтами)", "2 кг");
    }

    @Test
    @DisplayName("Страница «Услуги» содержит список услуг со ссылками")
    public void servicesPageListsServicesTest() {
        servicesPage.openServices()
                .checkTitle("Наши услуги")
                .checkServicesCount(5)
                .checkServiceLink("Аэрофотосъемка")
                .checkServiceLink("Магнитная съемка");
    }

    @Test
    @DisplayName("В блоге есть статьи, статья открывается со своим заголовком")
    public void blogHasArticlesTest() {
        blogPage.openBlog()
                .checkTitle("Блог")
                .checkArticlesPresent()
                .openFirstArticle()
                .checkArticleTitleVisible();
    }

    @Test
    @DisplayName("Переключатель языка меняет язык интерфейса RU ↔ EN")
    public void languageSwitchTest() {
        mainPage.openMain()
                .checkLanguageByLogoAlt("ГК «Геоскан»")
                .switchLanguage()
                .checkLanguageByLogoAlt("Geoscan")
                .switchLanguage()
                .checkLanguageByLogoAlt("ГК «Геоскан»");
    }

    @Test
    @DisplayName("Форма «Связаться с нами» не пропускает некорректный email")
    public void contactFormValidationTest() {
        contactsPage.openContacts()
                .fillContactForm("Тест Тестов", "invalid-email")
                .checkEmailRejectedByValidation("invalid-email")
                .checkFormNotSubmitted();
    }
}
