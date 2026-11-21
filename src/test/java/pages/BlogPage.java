package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class BlogPage {

    @Step("Открываем «Блог»")
    public BlogPage openBlog() {
        open("/ru/blog");
        return this;
    }

    @Step("Проверяем заголовок «Блог»")
    public BlogPage checkTitle() {
        $("h1").shouldHave(text("Блог"));
        return this;
    }

    @Step("Проверяем, что статьи в блоге есть")
    public BlogPage checkArticlesPresent() {
        $$(".card-title").shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Открываем первую статью")
    public BlogPage openFirstArticle() {
        $$(".card-title").first().$("a").click();
        return this;
    }

    @Step("Проверяем, что заголовок статьи отрисован")
    public BlogPage checkArticleTitleVisible() {
        $("h1.headtitle").shouldBe(visible);
        return this;
    }
}
