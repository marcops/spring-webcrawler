package org.marco;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.marco.model.Link;
import org.marco.service.WebCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.Gson;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private WebCrawlerService webCrawlerService;

	private static final String EXPORT_TO = "sitemap.json";
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Link> links = webCrawlerService.execute("http://www.soscarro.com.br");
		exportSitemap(links);
	}

	private void exportSitemap(List<Link> links) throws IOException {
		Files.write(Paths.get(EXPORT_TO), new Gson().toJson(links).getBytes());
		System.out.println(new Gson().toJson(links));
	}

}
