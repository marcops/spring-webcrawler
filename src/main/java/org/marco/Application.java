package org.marco;

import java.util.List;

import org.marco.model.Link;
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
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Link> links = webCrawlerService.execute("http://www.soscarro.com.br");
		links.forEach(System.out::println);
	}

	

}
