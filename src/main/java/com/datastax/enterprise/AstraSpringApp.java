package com.datastax.yasa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class YasaApp {

    public static void main(String[] args) {
        SpringApplication.run(YasaApp.class, args);
        
    }
    
}