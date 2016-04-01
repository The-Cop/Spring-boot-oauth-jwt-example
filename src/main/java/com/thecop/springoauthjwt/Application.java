package com.thecop.springoauthjwt;

import com.thecop.springoauthjwt.configuration.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;

public final class Application {

    private Application() {
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfiguration.class, args);
    }
}
