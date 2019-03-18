package uk.gov.hmcts.reform.dg.docassembly.testutil;

import org.apache.commons.lang3.Validate;

import java.util.Properties;

public class Env {

    static Properties defaults = new Properties();

    static {
        defaults.setProperty("PROXY", "false");
        defaults.setProperty("TEST_URL", "http://localhost:8080");
    }

    public static String getUseProxy() {
        return require("PROXY");
    }

    public static String getTestUrl() {
        return require("TEST_URL");
    }

    public static String require(String name) {
        return Validate.notNull(System.getenv(name) == null ? defaults.getProperty(name) :
                System.getenv(name), "Environment variable `%s` is required", name);
    }
}
