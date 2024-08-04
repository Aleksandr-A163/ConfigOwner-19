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

        // Get raw response as string
        String rawResponse = (given(registerAndLoginRequestSpec)
                .body(userData)
                .when()
                .post("/Account/v1/User")
                .then()
                .spec(responseSpec201)
                .extract().response().asString());

        // Debug information to verify the raw response
        System.out.println("Raw Response: " + rawResponse);

        // Parse the raw response to LoginResponseModel only if the content type is JSON
        if (rawResponse.contains("application/json")) {
            LoginResponseModel response = given()
                    .contentType("application/json")
                    .body(rawResponse)
                    .when()
                    .post()
                    .as(LoginResponseModel.class);

            // Debug information to verify the parsed response
            System.out.println("Authorization Response: " + response);

            return response;
        } else {
            throw new IllegalStateException("Unexpected content type: " + rawResponse);
        }
    }

    @Step("Set authorization cookies")
    public static void setCookiesInBrowser(LoginResponseModel authResponse) {
        if (authResponse == null) {
            throw new IllegalArgumentException("authResponse is null");
        }
        if (authResponse.getUserId() == null || authResponse.getExpires() == null || authResponse.getToken() == null) {
            throw new IllegalArgumentException("One or more required attributes in authResponse are null");
        }

        open("/images/Toolsqa.jpg");
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.getUserId()));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.getExpires()));
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.getToken()));
    }

    public static String extractValueFromCookieString(String cookieString) {
        String cookieValue = String.valueOf(getWebDriver().manage().getCookieNamed(cookieString));
        return cookieValue.substring(cookieValue.indexOf("=") + 1, cookieValue.indexOf(";"));
    }
}