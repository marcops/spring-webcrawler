package org.marco;

import java.io.IOException;
import java.net.URL;
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
		if(!isUrlValid(args[0])) return;
		List<Link> links = webCrawlerService.execute(args[0]);
		exportSitemap(links);
	
	}

	private Boolean isUrlValid(String url) {
		try {
			new URL(url);
		} catch (Exception e) {
			System.err.println("usage: java -jar app.jar http://example.com/");
			return false;
		}
		return true;
	}

	private void exportSitemap(List<Link> links) throws IOException {
		Files.write(Paths.get(EXPORT_TO), new Gson().toJson(links).getBytes());
	}

}
