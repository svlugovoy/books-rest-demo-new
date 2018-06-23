package com.svlugovoy.books.monitoring;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class HelloInfoContributor implements InfoContributor {

    private final Properties properties;

    public HelloInfoContributor() throws IOException {
        Resource resource = new ClassPathResource("hello.properties");
        properties = new Properties();
        properties.load(resource.getInputStream());
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("hello", properties);
    }
}
