package config;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config/testdata.properties"})

public interface TestDataConfig extends Config {
    @Key("userLogin")
    String userLogin();

    @Key("userPassword")
    String userPassword();

    @Key("isbn")
    String isbn();
}