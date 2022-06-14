package dev.qhc99.gallery_server;

import dev.qhc99.gallery_server.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class GalleryBackendApp {

	public static void main(String[] args) {
		SpringApplication.run(GalleryBackendApp.class, args);
	}
}
