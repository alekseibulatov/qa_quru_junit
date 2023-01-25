package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class ItecoPageTest {
    @BeforeEach
    public void openPage() {
        Configuration.browserSize = "1920x1080";
        open("https://www.i-teco.ru");
    }

    @ValueSource(strings = {"вакансии", "новости", "нк32гры"})
    @ParameterizedTest(name = "При вводе в строку поиска  {0} на сайте i-teco.ru открывается страница Результаты поиска")
    @Tag("MAJOR")
    public void openResultSearchWhenInputInSearchBar(String value) {
        $(".page-header__search-btn").click();
        $(".page-header__dropdown-search-form-input").click();
        $(".page-header__dropdown-search-form-input").setValue(value).pressEnter();
        $(".contacts__main-heading").shouldHave(text("Результаты поиска"));
    }

    @CsvSource(value = {
            "О компании, О компании Айтеко",
            "Решения, Услуги и решения",
            "Импортозамещение, Импортозамещение",
            "Партнёры, Партнеры",
            "Карьера, Карьера",
            "Пресс-центр, Пресс-центр",
            "Контакты, Контакты Айтеко"
    })
    @ParameterizedTest(name = "При клике на кнопку {0} происходит переход на страницу {1}")
    @Tag("CRITICAL")
    public void checkToLinkWhenClickToButton(String value, String url) {
        $$(".page-header__nav-list-item").find(text(value)).click();
        $(".inner-container").shouldHave(text(url));
    }

    static Stream<Arguments> checkButtonListWhenChangeLanguage() {
        return Stream.of(
                Arguments.of("О компании", "RU", List.of("Миссия", "Команда", "Наши заказчики", "Наши проекты",
                        "Лидерские позиции", "Социальная ответственность", "География", "Партнеры", "Лицензии",
                        "Система менеджмента качества", "Оценка условий труда")),
                Arguments.of("О компании", "EN", List.of("Mission", "Our team", "Our clients", "Success stories",
                        "Leadership positions", "Social responsibility", "Global reach", "Partners",
                        "Licenses", "Quality assurance", "Assessment of working conditions"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Проверка наличия кнопок из списка {2} на странице 'О Компании' при языке страницы {1}")
    @Tag("MAJOR")
    public void checkButtonListWhenChangeLanguage(String buttonAbout, String locale, List<String> buttons) {
        $(".page-header__nav-list").$(byText(buttonAbout)).click();
        $(".page-header__row").$(byText(locale)).click();
        $$(".about-intro__categories a").filter(visible).shouldHave(CollectionCondition.texts(buttons));
    }
}
