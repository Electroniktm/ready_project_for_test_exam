package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProductCardPage {

    private final SelenideElement specsSection = $("#specifications");

    @Step("Проверяем, что открылась страница продукта «{name}»")
    public ProductCardPage checkProductName(String name) {
        // у карточки нет видимого заголовка: SEO-h1 скрыт и с неразрывными пробелами —
        // поэтому сверяемся с заголовком вкладки страницы
        Assertions.assertTrue(Selenide.title().contains(name),
                "Заголовок вкладки должен содержать название продукта «" + name + "»");
        return this;
    }

    @Step("Проверяем, что раздел «Технические характеристики» отрисован")
    public ProductCardPage checkSpecsVisible() {
        $$(byText("Технические характеристики")).findBy(visible).shouldBe(visible);
        // группы характеристик — аккордеон, проверяем видимость заголовка группы
        specsSection.$(byText("БВС")).shouldBe(visible);
        return this;
    }

    @Step("Проверяем характеристику «{parameter}: {value}»")
    public ProductCardPage checkSpecValue(String parameter, String value) {
        // таблицы характеристик — свёрнутый аккордеон: Selenium getText() у скрытых строк
        // вернёт пусто, поэтому читаем textContent и нормализуем неразрывные пробелы
        String rowText = Selenide.executeJavaScript(
                "const p = arguments[0];"
                        + "const rows = document.querySelectorAll('#specifications tr');"
                        + "for (const r of rows) {"
                        + "  const t = r.textContent.replace(/[\\s\\u00A0]+/g, ' ').trim();"
                        + "  if (t.includes(p)) return t;"
                        + "}"
                        + "return null;", parameter);
        Assertions.assertNotNull(rowText,
                "В характеристиках продукта должна быть строка «" + parameter + "»");
        Assertions.assertTrue(rowText.contains(value),
                "Характеристика «" + parameter + "» должна содержать «" + value + "», фактически: " + rowText);
        return this;
    }
}
