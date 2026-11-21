package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class ProductsPage {

    @Step("Открываем каталог «Продукты»")
    public ProductsPage openCatalog() {
        open("/ru/products");
        return this;
    }

    @Step("Проверяем наличие модели «{model}» в каталоге")
    public ProductsPage checkModelPresent(String model) {
        // ссылка на модель дублируется в скрытом мега-меню — берём видимый экземпляр
        $$(byText(model)).findBy(visible).shouldBe(visible);
        return this;
    }
}
