package dev.qhc99.gallery_server;

import dev.qhc99.gallery_server.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Locale;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class GalleryBackendApp {

	public static void main(String[] args) {
		Locale.setDefault(Locale.CANADA);
		SpringApplication.run(GalleryBackendApp.class, args);
	}
}
