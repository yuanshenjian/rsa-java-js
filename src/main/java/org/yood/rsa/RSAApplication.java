package org.yood.rsa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class RSAApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAApplication.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder().profiles("dev").sources(RSAApplication.class).run(args);
        LOGGER.info("Application is running");
    }
}
