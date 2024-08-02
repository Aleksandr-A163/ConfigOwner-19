package api;

import io.qameta.allure.Step;
import models.*;
import org.openqa.selenium.Cookie;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static specs.RequestResponseSpecs.*;

public class ApiSteps {
    @Step("Create a new user")
        public void userRegistrationApi(String userName, String password) {
            RegistrationLoginRequestModel userData = new RegistrationLoginRequestModel();
            userData.setUserName(userName);
            userData.setPassword(password);
            given(registerAndLoginRequestSpec)
                    .body(userData)
                    .when()
                    .post("/Account/v1/User")
                    .then()
                    .spec(responseSpec201)
                    .extract().as(RegistrationResponseModel.class);
        }

    @Step("Get authorization token")
    public void getTokenApi(String userName, String password) {
        RegistrationLoginRequestModel userData = new RegistrationLoginRequestModel();
        userData.setUserName(userName);
        userData.setPassword(password);
        given(registerAndLoginRequestSpec)
                .body(userData)
                .when()
                .post("Account/v1/GenerateToken")
                .then()
                .spec(loginResponseSpec200)
                .extract().response();
    }

    @Step("Login a user")
    public LoginResponseModel loginUserApi(String userName, String password) {
        RegistrationLoginRequestModel userData = new RegistrationLoginRequestModel();
        userData.setUserName(userName);
        userData.setPassword(password);
        return (given(registerAndLoginRequestSpec)
                .body(userData)
                .when()
                .post("Account/v1/Login")
                .then()
                .spec(loginResponseSpec200)
                .extract().as(LoginResponseModel.class));
    }

    @Step("Add a book to user profile")
    public void addBookToProfileApi(LoginResponseModel authResponse, String isbn) {
        AddBookToBasketRequestBodyModel bookData = new AddBookToBasketRequestBodyModel();
        bookData.setUserId(authResponse.getUserId());
        AddBookToBasketRequestBodyModel.Isbn isbns = new AddBookToBasketRequestBodyModel.Isbn();
        isbns.setIsbn(isbn);
        bookData.setCollectionOfIsbns(List.of(isbns));
        given(authorizedRequestSpec(authResponse.getToken()))
                .body(bookData)
                .when()
                .post("BookStore/v1/Books")
                .then()
                .spec(responseSpec201);
    }

    @Step("Set authorization cookies")
    public void setAuthCookie(LoginResponseModel authResponse) {
        open("/images/Toolsqa.jpg");
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.getToken()));
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.getUserId()));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.getExpires()));
    }

}