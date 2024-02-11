package ru.asherbakov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class DocumentStorageApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(DocumentStorageApplication.class, args);
	}
}
