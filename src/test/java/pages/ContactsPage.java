package pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;

public class ContactsPage {

    @Step("Открываем страницу «Контакты»")
    public ContactsPage openContacts() {
        open("/ru/contacts");
        return this;
    }

    @Step("Заполняем форму «Связаться с нами»: имя «{name}», email «{email}»")
    public ContactsPage fillContactForm(String name, String email) {
        $("#edit-name").setValue(name);
        $("#edit-email").setValue(email);
        return this;
    }

    @Step("Проверяем, что email «{email}» не проходит HTML5-валидацию")
    public ContactsPage checkEmailRejectedByValidation(String email) {
        String validationMessage = Selenide.executeJavaScript(
                "const el = document.querySelector('#edit-email');"
                        + "return el.matches(':invalid') ? el.validationMessage : null;");
        Assertions.assertNotNull(validationMessage,
                "Email «" + email + "» должен быть помечен как некорректный с сообщением об ошибке");
        return this;
    }

    @Step("Проверяем, что мы остались на странице контактов")
    public ContactsPage checkFormNotSubmitted() {
        webdriver().shouldHave(urlContaining("/ru/contacts"));
        return this;
    }
}
