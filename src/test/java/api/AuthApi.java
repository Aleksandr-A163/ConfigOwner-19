package api;

import io.qameta.allure.Step;
import config.TestDataConfig;
import models.*;
import static com.codeborne.selenide.Selenide.open;
import org.openqa.selenium.Cookie;
import org.aeonbits.owner.ConfigFactory;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

import static io.restassured.RestAssured.given;
import static specs.RequestResponseSpecs.*;

public class AuthApi {

    static final TestDataConfig testDataConfig = ConfigFactory.create(TestDataConfig.class, System.getProperties());

    @Step("Create a new user")
    public static LoginResponseModel authorization() {
        RegistrationLoginRequestModel userData = new RegistrationLoginRequestModel();
        userData.setUserName(testDataConfig.userLogin());
        userData.setPassword(testDataConfig.userPassword());
        return given(registerAndLoginRequestSpec)
                .body(userData)
                .when()
                .post("/Account/v1/User")
                .then()
                .spec(responseSpec201)
                .extract().as(LoginResponseModel.class);
    }


    @Step("Set authorization cookies")
    public static void setCookiesInBrowser(LoginResponseModel authResponse) {
        open("/images/Toolsqa.jpg");
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.getToken()));
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.getUserId()));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.getExpires()));
    }

    public static String extractValueFromCookieString(String cookieString) {
        String cookieValue = String.valueOf(getWebDriver().manage().getCookieNamed(cookieString));
        return cookieValue.substring(cookieValue.indexOf("=") + 1, cookieValue.indexOf(";"));
    }
}