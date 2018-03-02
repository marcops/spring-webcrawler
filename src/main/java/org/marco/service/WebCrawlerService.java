package org.marco.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.marco.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class WebCrawlerService {
	
	private String exportTo;
	
	@Autowired
	private NavigateService navigateService;
	
	public WebCrawlerService(@Value("${webcrawler.export.to:sitemap.json}") String exportTo) {
		this.exportTo = exportTo;
	}

	public void execute(String url) throws Exception {
		List<Link> links = navigateService.execute(url);
		exportSitemap(links);
	}

	private void exportSitemap(List<Link> links) throws IOException {
		Files.write(Paths.get(exportTo), new Gson().toJson(links).getBytes());
	}
}
