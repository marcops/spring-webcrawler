package org.marco;

import java.net.URL;

import org.marco.service.WebCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private WebCrawlerService webCrawlerService;

	public static void main(String[] args) {
		if (!isUrlValid(args)) return;
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		webCrawlerService.execute(args[0]);
	}

	private static Boolean isUrlValid(String[] url) {
		try {
			new URL(url[0]);
		} catch (Exception e) {
			System.err.println("usage: java -jar app.jar http://example.com/");
			return false;
		}
		return true;
	}

}
