package org.yood.springboot.mybatis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SpringBootMybatisApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootMybatisApplication.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder().profiles("dev").sources(SpringBootMybatisApplication.class).run(args);
        LOGGER.info("Application is running");
    }
}
