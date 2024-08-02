package helpers;

import api.AuthApi;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class LoginExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        AuthApi.setCookiesInBrowser(AuthApi.authorization());
    }
}