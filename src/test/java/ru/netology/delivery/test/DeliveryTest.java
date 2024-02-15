package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.io.IOException;
import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DeliveryTest {

    String citySelector = "[data-test-id=city] input";
    String dateSelector = "[data-test-id=date] input";
    String nameSelector = "[data-test-id=name] input";
    String phoneSelector = "[data-test-id=phone] input";
    String agreementSelector = "[data-test-id=agreement] span.checkbox__box";
    String buttonSelector = "button .button__text";
    String notificationTitleSelector = "[data-test-id=success-notification] .notification__title";
    String notificationContentSelector = "[data-test-id=success-notification] .notification__content";
    String notificationRePlanTitleSelector = "[data-test-id=replan-notification] .notification__title";
    String notificationRePlanButton = "[data-test-id=replan-notification] button";
    String sendButtonText = "Запланировать";
    String notificationTitle = "Успешно!";
    String notificationContent = "Встреча успешно запланирована на ";
    String notificationRePlanTitle = "Необходимо подтверждение";
    String notificationButtonText = "Перепланировать";


    void clearField(String selector) {
        $(selector).sendKeys(Keys.CONTROL + "A");
        $(selector).sendKeys(Keys.BACK_SPACE);
    }

    public void stepsToFillForm(String city, String date, String name, String phone) {
        $(citySelector).setValue(city);
        clearField(dateSelector);
        $(dateSelector).setValue(date);
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
    }

    public void checkNotificationSuccess(String date) {
        $(notificationTitleSelector).should(visible, Duration.ofSeconds(15)).shouldHave(text(notificationTitle));
        $(notificationContentSelector).shouldHave(visible, text(notificationContent + date));
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndRePlanMeeting() throws IOException {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        stepsToFillForm(validUser.getCity(), firstMeetingDate, validUser.getName(), validUser.getPhone());
        checkNotificationSuccess(firstMeetingDate);
        clearField(dateSelector);
        $(dateSelector).setValue(secondMeetingDate);
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(notificationRePlanTitleSelector).should(visible).shouldHave(text(notificationRePlanTitle));
        $(notificationRePlanButton).should(visible).shouldHave(text(notificationButtonText)).click();
        checkNotificationSuccess(secondMeetingDate);
    }
}
