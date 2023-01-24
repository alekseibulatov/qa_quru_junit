package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class GoogleWebTest {
    @BeforeEach
    void setUp() {
        open("https://google.com");
    }

    @CsvSource(value = {
            "selenide, https://ru.selenide.org, 1",
            "junit 5, https://junit.org, 1"
    })
    @ParameterizedTest(name = "Проверка наличия урла {1}" +
            "в результатах выдачи гугла по запросу {0}")
    @Tags({@Tag("BLOCKER"), @Tag("FEATURE")})
    void googleSearchTest(String searchQuery, String expectedUrl, int resultCount) {
        $("[name=q]").setValue(searchQuery).pressEnter();
        $("[id=search]").shouldHave(text(expectedUrl));
        $$("[id=search]").should(CollectionCondition.size(resultCount));
    }


    @DisplayName("Проверка попапа загрузки фото")
    @Test
    @Tag("BLOCKER")
    void googlePhotoPopupTest() {
        $("img[alt='Поиск с помощью камеры']").click();
        $(byText("Выполните поиск по изображению в Google Объективе")).shouldBe(visible);
    }
}

