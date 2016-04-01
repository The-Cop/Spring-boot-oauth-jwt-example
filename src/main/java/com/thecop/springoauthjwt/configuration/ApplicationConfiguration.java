package com.thecop.springoauthjwt.configuration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.thecop.springoauthjwt.configuration",
        "com.thecop.springoauthjwt.controller",
        "com.thecop.springoauthjwt.service.impl"})
public class ApplicationConfiguration {

}
