package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import config.ConfigReader;
import config.ProjectConfiguration;
import config.web.WebConfig;
import helpers.Attachments;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;


import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    private static final WebConfig webConfig = ConfigReader.Instance.read();

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        ProjectConfiguration projectConfiguration = new ProjectConfiguration(webConfig);
        projectConfiguration.webConfig();
        projectConfiguration.apiConfig();
    }

    @AfterEach
    void addAttachments() {
        Attachments.screenshotAs("Last step screenshot");
        Attachments.pageSource();
        if (!Configuration.browser.equals("firefox")) {
            Attachments.browserConsoleLogs();
        }
        Attachments.browserConsoleLogs();
        Attachments.addVideo();
        closeWebDriver();
    }
}